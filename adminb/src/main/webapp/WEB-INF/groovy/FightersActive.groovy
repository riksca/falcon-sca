import org.calontir.marshallate.falcon.user.Security
import org.calontir.marshallate.falcon.user.SecurityFactory
import org.calontir.marshallate.falcon.dto.Fighter
import org.calontir.marshallate.falcon.db.FighterDAO
import org.calontir.marshallate.falcon.db.ScaGroupDAO
import com.google.appengine.api.datastore.Entity
import com.google.appengine.api.blobstore.BlobInfo
import com.google.appengine.api.blobstore.BlobKey
import org.calontir.marshallate.falcon.dto.FighterListItem
import org.calontir.marshallate.falcon.dto.Authorization
import org.calontir.marshallate.falcon.utils.MarshalUtils
import org.calontir.marshallate.falcon.common.*
import com.google.appengine.api.datastore.*
import static com.google.appengine.api.datastore.FetchOptions.Builder.*
import groovy.json.JsonSlurper
import org.joda.time.DateTime
import groovy.xml.MarkupBuilder

def getFighterList() {
	def jsonStr
	def blobKeyStr
	namespace.of("system") {
		name = "calontir.snapshotkey"
		def query = new Query("properties")
		query.addFilter("name", Query.FilterOperator.EQUAL, name)
		PreparedQuery preparedQuery = datastore.prepare(query)
		def entities = preparedQuery.asList( withLimit(10) )
		def entity = entities[0]
		blobKeyStr = entity.property
	}
	BlobKey blobKey = new BlobKey(blobKeyStr)
	blobKey.withReader { Reader reader ->
		jsonStr = reader.text
	}

	return jsonStr
}

Security security = SecurityFactory.getSecurity()
def jsonStr = getFighterList()

def jsonObj = new JsonSlurper().parseText(jsonStr)

dao = new FighterDAO()

dt = new DateTime(jsonObj.dateSaved)
def fighters = dao.getFighterListItems(dt)
mapList = jsonObj.scaNames
keyMap = [:]
mapList.each {
	keyMap[it.fighterId] = it
	it.status = FighterStatus.valueOf(it.status)
}

fighters.each {
	def fmap = [:]
	fmap.scaName = it.scaName
	fmap.id = it.fighterId
	fmap.authorizations = it.authorizations
	if(it.group) {
		fmap.group = it.group
	} else {
		fmap.group = "Unknown or Out of Kingdom"
	}
	fmap.status = it.status

	if(keyMap.containsKey(it.fighterId)) {
		tempF = keyMap[it.fighterId]
		mapList.remove(tempF)
		keyMap.remove(it.fighterId)
	}

	mapList << fmap
}

int count = 0
int active = 0
int marshal = 0

mapList.each { f ->
	count++
	if(f.status.equals(FighterStatus.ACTIVE)) {
		active++
	}
	if(f.authorizations.contains('Marshal')) {
		marshal++
	}
}

StringWriter writer = new StringWriter()
def build = new MarkupBuilder(writer)
build.html{
	head{
		title('Report')
	}
	body{
		p"Number of fighters ${count}"
		p"Number of active fighters ${active}"
		p"Number of marshals ${marshal}"
	}
}
println writer.toString()
