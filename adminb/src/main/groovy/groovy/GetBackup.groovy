import org.calontir.marshallate.falcon.user.Security
import org.calontir.marshallate.falcon.user.SecurityFactory
import org.calontir.marshallate.falcon.dto.Fighter
import org.calontir.marshallate.falcon.db.FighterDAO
import com.google.appengine.api.datastore.Entity
import com.google.appengine.api.blobstore.BlobInfo
import com.google.appengine.api.blobstore.BlobKey
import org.calontir.marshallate.falcon.dto.FighterListItem
import org.calontir.marshallate.falcon.dto.Authorization
import org.calontir.marshallate.falcon.utils.MarshalUtils
import org.calontir.marshallate.falcon.common.*
import com.google.appengine.api.datastore.*
import static com.google.appengine.api.datastore.FetchOptions.Builder.*

def getBackup() {
	def backupStr
	def blobKeyStr
	namespace.of("system") {
		name = "calontir.backupkey"
		def query = new Query("properties")
		query.addFilter("name", Query.FilterOperator.EQUAL, name)
		PreparedQuery preparedQuery = datastore.prepare(query)
		def entities = preparedQuery.asList( withLimit(10) )
		def entity = entities[0]
		blobKeyStr = entity.property
	}
	BlobKey blobKey = new BlobKey(blobKeyStr)
	blobKey.withReader { Reader reader ->
		backupStr = reader.text
	}

	return backupStr
}

Security security = SecurityFactory.getSecurity()
if(!security.isRoleOrGreater(UserRoles.CARD_MARSHAL)) {
	html.html {
		body {
			p "Must be a Card Marshal to execute backup"
		}
	}
	return
}

def backupFile = getBackup()

out <<  backupFile
