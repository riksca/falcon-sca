package org.calontir.marshallate.falcon.db;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.calontir.marshallate.falcon.dto.ScaGroup;

/**
 *
 * @author rik
 */
public class ScaGroupDAO {

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

    public static class LocalCacheImpl extends LocalCacheAbImpl {

        private static LocalCacheImpl _instance = new LocalCacheImpl();

        public static LocalCacheImpl getInstance() {
            return _instance;
        }
    }
    static private LocalCacheImpl localCache = (LocalCacheImpl) LocalCacheImpl.getInstance();

    public ScaGroup getScaGroup(long scaGroupId) throws NotFoundException {
        ScaGroup sg = (ScaGroup) localCache.getValue(scaGroupId);
        if (sg == null) {
            try {
                Key key = KeyFactory.createKey("ScaGroup", scaGroupId);
                Entity groupEntity = datastore.get(key);
                sg = new ScaGroup();
                sg.setGroupLocation((String) groupEntity.getProperty("groupLocation"));
                sg.setGroupName((String) groupEntity.getProperty("groupName"));
                localCache.put(scaGroupId, sg);
            } catch (EntityNotFoundException ex) {
                Logger.getLogger(ScaGroupDAO.class.getName()).log(Level.SEVERE, null, ex);
                throw new NotFoundException("ScaGroup", scaGroupId);
            }
        }
        return sg;
    }

    public ScaGroup getScaGroupByName(String groupName) {
        ScaGroup sg = (ScaGroup) localCache.getValue(groupName);
        if (sg == null) {
            Query.Filter groupNameFilter = new Query.FilterPredicate("groupName", Query.FilterOperator.EQUAL, groupName);
            Query query = new Query("ScaGroup").setFilter(groupNameFilter);
            Entity entity = datastore.prepare(query).asSingleEntity();
            if (entity != null) {
                sg = new ScaGroup();
                sg.setGroupLocation((String) entity.getProperty("groupLocation"));
                sg.setGroupName((String) entity.getProperty("groupName"));
                localCache.put(groupName, sg);
            }
        }
        return sg;
    }

    public Key getScaGroupKey(String groupName) {
        Query.Filter groupNameFilter = new Query.FilterPredicate("groupName", Query.FilterOperator.EQUAL, groupName);
        Query query = new Query("ScaGroup").setFilter(groupNameFilter);
        Entity entity = datastore.prepare(query).asSingleEntity();
        return entity == null ? null : entity.getKey();
    }

    public List<ScaGroup> getScaGroup() {
        List<ScaGroup> retList = (List<ScaGroup>) localCache.getValue("sgList");
        if (retList == null) {
            Query query = new Query("ScaGroup").addSort("groupName");
            List<Entity> sgList = datastore.prepare(query).asList(FetchOptions.Builder.withDefaults());
            retList = new ArrayList<>();
            for (Entity groupEntity : sgList) {
                ScaGroup group = new ScaGroup();
                group.setGroupLocation((String) groupEntity.getProperty("groupLocation"));
                group.setGroupName((String) groupEntity.getProperty("groupName"));
                retList.add(group);
            }
            localCache.put("sgList", retList);
        }
        return retList;
    }

    public List<String> getScaGroupNamesByRegion(String region) {
        final Query groupQuery = new Query("ScaGroup");
        final Filter localFilter = new FilterPredicate("groupLocation", FilterOperator.EQUAL, region);
        groupQuery.setFilter(localFilter);
        final PreparedQuery pq = datastore.prepare(groupQuery);
        final List<String> groupNames = new ArrayList<>();
        for (final Entity entity : pq.asQueryResultIterable()) {
            groupNames.add((String) entity.getProperty("groupName"));
        }
        return groupNames;
    }

    public Key saveScaGroup(ScaGroup scaGroup) {
        localCache.clear();

        Query.Filter groupNameFilter = new Query.FilterPredicate("groupName", Query.FilterOperator.EQUAL, scaGroup.getGroupName());
        Query query = new Query("ScaGroup").setFilter(groupNameFilter);
        Entity groupEntity = datastore.prepare(query).asSingleEntity();
        if (groupEntity == null) {
            groupEntity = new Entity("ScaGroup");
        }
        groupEntity.setProperty("groupLocation", scaGroup.getGroupLocation());
        groupEntity.setProperty("groupName", scaGroup.getGroupName());

        return datastore.put(groupEntity);
    }
}
