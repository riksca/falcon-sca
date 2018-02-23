import org.calontir.marshallate.falcon.utils.MarshalUtils
import org.calontir.marshallate.falcon.user.Security
import org.calontir.marshallate.falcon.user.SecurityFactory
import org.calontir.marshallate.falcon.dto.ScaGroup
import com.google.appengine.api.datastore.Entity
import com.google.appengine.api.blobstore.BlobKey
import com.google.appengine.api.datastore.*
import com.google.appengine.tools.cloudstorage.*
import static com.google.appengine.api.datastore.FetchOptions.Builder.*
import com.google.appengine.api.ThreadManager
import java.nio.ByteBuffer;


def runToday = false
final int limitNum = 200

namespace.of("system") {
    name = "calontir.lastbackup"
    def query = new Query("properties")
    query.addFilter("name", Query.FilterOperator.EQUAL, name)
    PreparedQuery preparedQuery = datastore.prepare(query)
    def entities = preparedQuery.asList( withDefaults() )
    if(entities != null && entities.size() > 0) {
        def entity = entities[0]
        Date savedDate = new Date(entity.property).clearTime()
        Date today = new Date().clearTime()
        runToday = savedDate == today
    }
}

if(runToday) {
    html.html {
        body {
            p "Back already run today"
        }
    }
    return
}

Thread thread = ThreadManager.createBackgroundThread(new Runnable() {
        public void run() {

            Security security = SecurityFactory.getSecurity()

            def fighterCount = datastore.execute {
                select count from Fighter
            }

            logger.StoreDatabase.info "Count returns " + fighterCount

            def loopTimes = fighterCount.div(limitNum).intValue() + 1

            logger.StoreDatabase.info "Looping " + loopTimes + " times"

            def fighterIndex = search.index("fighters")

            def mapList = []
            long savedCount = 0L
            for (i in 0..loopTimes) {

                def fighters = datastore.iterate {
                    select all from Fighter
                    prefetchSize fighterCount
                    chunkSize limitNum
                    limit limitNum
                    offset i == 0 ? 0 : limitNum * i
                    restart automatically
                }

                for (fighter in fighters) {
                    def response = fighterIndex.put {
                        document(id: fighter.key.id) {
                            scaName text: fighter.scaName
                            modernName text: fighter.modernName
                            googeId text: fighter.googleId
                        }
                    }
                    def fmap = [:]
                    fmap.fighterId = fighter.key.id
                    fmap.scaName = fighter.scaName
                    fmap.scaMemberNo = fighter.scaMemberNo
                    fmap.modernName = fighter.modernName
                    fmap.dateOfBirth = fighter.dateOfBirth
                    fmap.googleId = fighter.googleId
                    def emailList = []
                    def emails = datastore.execute {
                        select all from 'Email'
                        ancestor fighter.key
                    }
                    emails.each {email ->
                        def emailMap = [:]
                        emailMap.emailAddress = email.emailAddress
                        emailMap.type = email.type
                        emailList << emailMap
                    }
                    fmap.email = emailList
                    def addresses = datastore.execute {
                        select all from Address
                        ancestor fighter.key
                    }
                    def addressList = []
                    addresses.each { address->
                        def addressMap = [:]
                        addressMap.address1 = address.address1
                        addressMap.address2 = address.address2
                        addressMap.city = address.city
                        addressMap.district = address.district
                        addressMap.postalCode = address.postalCode
                        addressMap.state = address.state
                        addressMap.type = address.type
                        addressList << addressMap
                    }
                    fmap.address = addressList
                    def phones = datastore.execute {
                        select all from Phone
                        ancestor fighter.key
                    }
                    def phoneList = []
                    phones.each {phone ->
                        def phoneMap = [:]
                        phoneMap.phoneNumber = phone.phoneNumber
                        phoneMap.type = phone.type
                        phoneList << phoneMap
                    }
                    fmap.phone = phoneList
                    def authList = []
                    def authorizations = datastore.execute {
                        select all from Authorization
                        ancestor fighter.key
                    }
                    authorizations.each { auth ->
                        def authMap = [:]
                        def authType = auth.authType.get()
                        authMap.code = authType.code
                        authMap.description = authType.description
                        authMap.date = auth.date
                        authMap.orderValue = authType.orderValue
                        authList << authMap
                    }
                    fmap.authorization = authList
                    if(fighter.scaGroup) {
                        def group = fighter.scaGroup.get()
                        fmap.group = group.groupName
                    } else {
                        fmap.group = "Unknown or Out of Kingdom"
                    }
                    fmap.role = fighter.role
                    fmap.status = fighter.status
                    fmap.treaty = fighter.treaty?.name
                    mapList << fmap
                    ++savedCount
                }
            }

            logger.StoreDatabase.info "mapList size " + mapList.size()

            if (savedCount == fighterCount) {
                logger.BackupData.info "Saved " + savedCount + " fighters"
            } else {
                logger.BackupData.warning "Only found " + savedCount + " fighters. Ending"
                return
            }

            def scaGroups = datastore.execute {
                select all from 'ScaGroup' as ScaGroup
            }

            def groupsMap = [:]
            scaGroups.each { group ->
                groupsMap[group.groupName] = group
            }

            def reportIndex = search.index("reports")
            def reportList = []
            namespace.of("calontir") {
                def reports = datastore.iterate {
                    select all from 'Report'
                    restart automatically
                }
                reports.each { r ->
                    def params = datastore.execute {
                        select all from 'ReportParams'
                        where reportKey == r.key
                    }
                    def rmap = [:]
                    rmap.id = r.key.id
                    rmap.dateEntered = r.dateEntered
                    rmap.reportType = r.reportType
                    rmap.marshalName = r.marshalName
                    rmap.marshalId = r.marshalId
                    rmap.googleId = r.googleId
                    def paramMap = [:]
                    params.each {param ->
                        paramMap[param.name] = param.value
                    }
                    rmap.reportParams = paramMap
                    reportList << rmap
                    def response = reportIndex.put {
                        document(id: r.key.id) {
                            marshalName text: r.marshalName
                            reportType text: r.reportType
                            googleId text: r.googleId
                            group text: paramMap["Group"]
                            region text : groupsMap[paramMap["Group"]]?.groupLocation
                            dateEntered date : Date.parse("yyyy-MM-dd'T'HH:mm:ss", r.dateEntered)
                        }
                    }
                }
            }

            def json = new groovy.json.JsonBuilder()
            def now = new Date()
            def root = json {
                dateBackedUp String.format('%tF %<tT', now.time)
                fighters mapList
                reports reportList
            }

            //def file = files.createNewBlobFile("text/json", String.format("backup%tY%tm%td.json", now, now, now))
            // we need to do somethign else with the json string.

            GcsService gcsService = GcsServiceFactory.createGcsService();
            GcsFilename filename = new GcsFilename("falcon-sca.appspot.com", String.format("backup%tY%tm%td.json", now, now, now));
            GcsFileOptions options = new GcsFileOptions.Builder()
            .mimeType("text/html")
            .acl("public-read")
            .addUserMetadata("calontir.backup", String.format("backup%tY%tm%td.json", now, now, now))
            .build();

            GcsOutputChannel writeChannel = gcsService.createOrReplace(filename, options);
            writeChannel.write(ByteBuffer.wrap(json.toString().getBytes("UTF8")));
            writeChannel.close();

            namespace.of("system") {
                def lastbackupKey = "calontir.lastbackup"
                query = new Query("properties")
                query.addFilter("name", Query.FilterOperator.EQUAL, lastbackupKey)
                preparedQuery = datastore.prepare(query)
                entities = preparedQuery.asList( withDefaults() )
                entities.each {
                    it.delete()
                }

                sysTable = new Entity("properties")
                sysTable.name = lastbackupKey
                sysTable.property = new Date().time

                sysTable.save()
            }
        }
    });
thread.start()

html.html {
    body {
        p "Backup Started"
    }
}

