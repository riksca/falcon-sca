package org.calontir.marshallate.falcon.db;

import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.QueryResultList;
import com.google.appengine.api.search.Document;
import com.google.appengine.api.search.Field;
import com.google.appengine.api.search.Index;
import com.google.appengine.api.search.IndexSpec;
import com.google.appengine.api.search.PutException;
import com.google.appengine.api.search.Results;
import com.google.appengine.api.search.ScoredDocument;
import com.google.appengine.api.search.SearchServiceFactory;
import com.google.appengine.api.search.StatusCode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang.StringUtils;
import org.calontir.marshallate.falcon.ValidationException;
import org.calontir.marshallate.falcon.dto.Address;
import org.calontir.marshallate.falcon.dto.Authorization;
import org.calontir.marshallate.falcon.dto.DataTransfer;
import org.calontir.marshallate.falcon.dto.Email;
import org.calontir.marshallate.falcon.dto.Fighter;
import org.calontir.marshallate.falcon.dto.FighterListItem;
import org.calontir.marshallate.falcon.dto.FighterResultWrapper;
import org.calontir.marshallate.falcon.dto.Note;
import org.calontir.marshallate.falcon.dto.Phone;
import org.calontir.marshallate.falcon.dto.ScaGroup;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

/**
 *
 * @author rik
 */
public class FighterDAO {

    static final Logger logger = Logger.getLogger(FighterDAO.class.getName());

    final DatastoreService datastore;
    final private FighterCache fCache;

    public FighterDAO() {
        this.datastore = DatastoreServiceFactory.getDatastoreService();
        this.fCache = FighterCache.getInstance();
    }

    public Fighter getFighter(long fighterId) {
        Fighter retVal = fCache.getFighter(fighterId);
        if (retVal == null) {
            Entity fighter;
            try {
                fighter = getFighterEntity(fighterId);
                retVal = DataTransfer.convert(fighter, datastore);
                fCache.put(retVal);
            } catch (EntityNotFoundException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
        }

        return retVal;
    }

    public FighterResultWrapper getFighters(int pageSize, int offset, boolean getDeleted) {
        return getFighters(pageSize, null, offset, getDeleted);
    }

    public FighterResultWrapper getFighters(int pageSize, Cursor cursor, boolean getDeleted) {
        return getFighters(pageSize, cursor, 0, getDeleted);
    }

    protected FighterResultWrapper getFighters(int pageSize, Cursor cursor, int offset, boolean getDeleted) {
        final Query query = new Query("Fighter");
        if (!getDeleted) {
            final Query.Filter scaGroupFilter = new Query.FilterPredicate("status", Query.FilterOperator.NOT_EQUAL, "DELETED");
            query.setFilter(scaGroupFilter).addSort("status");
        }
        query.addSort("scaName");
        final FetchOptions fetchOptions = FetchOptions.Builder.withLimit(pageSize);
        if (cursor != null) {
            fetchOptions.startCursor(cursor);
        } else if (offset > 0) {
            fetchOptions.offset(offset);
        }
        final PreparedQuery pq = datastore.prepare(query);
        final QueryResultList<Entity> results = pq.asQueryResultList(fetchOptions);
        final List<FighterListItem> retArray = new ArrayList<>();
        for (final Entity f : results) {
            final FighterListItem fli = DataTransfer.convertToListItem(f, datastore);
            retArray.add(fli);
        }
        final Cursor newCursor = results.getCursor();
        final FighterResultWrapper result = new FighterResultWrapper();
        result.setFighters(retArray);
        result.setCursor(newCursor);
        return result;
    }

    public FighterResultWrapper getFightersSortedByScaName(Integer pageSize) {
        final Query query = new Query("Fighter").addSort("scaName");
        final FetchOptions fetchOptions = FetchOptions.Builder.withLimit(pageSize);
        final PreparedQuery pq = datastore.prepare(query);
        final QueryResultList<Entity> results = pq.asQueryResultList(fetchOptions);
        final List<FighterListItem> retArray = new ArrayList<>();
        for (final Entity f : results) {
            final FighterListItem fli = DataTransfer.convertToListItem(f, datastore);
            retArray.add(fli);
        }
        final Cursor newCursor = results.getCursor();
        final FighterResultWrapper result = new FighterResultWrapper();
        result.setFighters(retArray);
        result.setCursor(newCursor);
        return result;
    }

    public FighterResultWrapper getFightersSortedByGroup(Integer pageSize) {
        final Query query = new Query("Fighter").addSort("scaGroup");
        final FetchOptions fetchOptions = FetchOptions.Builder.withLimit(pageSize);
        final PreparedQuery pq = datastore.prepare(query);
        final QueryResultList<Entity> results = pq.asQueryResultList(fetchOptions);
        final List<FighterListItem> retArray = new ArrayList<>();
        for (final Entity f : results) {
            final FighterListItem fli = DataTransfer.convertToListItem(f, datastore);
            retArray.add(fli);
        }
        final Cursor newCursor = results.getCursor();
        final FighterResultWrapper result = new FighterResultWrapper();
        result.setFighters(retArray);
        result.setCursor(newCursor);
        return result;
    }

    public FighterResultWrapper getFightersSortedByStatus(Integer pageSize) {
        final Query query = new Query("Fighter").addSort("status");
        final FetchOptions fetchOptions = FetchOptions.Builder.withLimit(pageSize);
        final PreparedQuery pq = datastore.prepare(query);
        final QueryResultList<Entity> results = pq.asQueryResultList(fetchOptions);
        final List<FighterListItem> retArray = new ArrayList<>();
        for (final Entity f : results) {
            final FighterListItem fli = DataTransfer.convertToListItem(f, datastore);
            retArray.add(fli);
        }
        final Cursor newCursor = results.getCursor();
        final FighterResultWrapper result = new FighterResultWrapper();
        result.setFighters(retArray);
        result.setCursor(newCursor);
        return result;
    }

    public FighterResultWrapper getFightersByGroup(ScaGroup scaGroup, int pageSize, int offset, boolean getDeleted) {
        return getFightersByGroup(scaGroup, pageSize, null, offset, getDeleted);
    }

    public FighterResultWrapper getFightersByGroup(ScaGroup scaGroup, int pageSize, Cursor cursor, boolean getDeleted) {
        return getFightersByGroup(scaGroup, pageSize, cursor, 0, getDeleted);
    }

    protected FighterResultWrapper getFightersByGroup(ScaGroup scaGroup, int pageSize, Cursor cursor, int offset, boolean getDeleted) {
        ScaGroupDAO groupDao = new ScaGroupDAO();
        Key groupKey = groupDao.getScaGroupKey(scaGroup.getGroupName());
        final Query query = new Query("Fighter");
        Query.Filter scaGroupFilter = new Query.FilterPredicate("scaGroup", Query.FilterOperator.EQUAL, groupKey);

        if (getDeleted) {
            query.setFilter(scaGroupFilter).addSort("scaName");
        } else {
            final Query.Filter statusFilter = new Query.FilterPredicate("status", Query.FilterOperator.NOT_EQUAL, "DELETED");
            final Query.Filter compositeFilter = Query.CompositeFilterOperator.and(scaGroupFilter, statusFilter);
            query.setFilter(compositeFilter).addSort("status").addSort("scaName");
        }
        final FetchOptions fetchOptions = FetchOptions.Builder.withLimit(pageSize);
        if (cursor != null) {
            fetchOptions.startCursor(cursor);
        } else if (offset > 0) {
            fetchOptions.offset(offset);
        }
        final PreparedQuery pq = datastore.prepare(query);
        final QueryResultList<Entity> results = pq.asQueryResultList(fetchOptions);
        final List<FighterListItem> retArray = new ArrayList<>();
        for (final Entity f : results) {
            final FighterListItem fli = DataTransfer.convertToListItem(f, datastore);
            retArray.add(fli);
        }
        final Cursor newCursor = results.getCursor();
        final FighterResultWrapper result = new FighterResultWrapper();
        result.setFighters(retArray);
        result.setCursor(newCursor);
        return result;
    }

    public List<FighterListItem> searchFighters(String searchString) {
        logger.finer("Search String");

        IndexSpec indexSpec = IndexSpec.newBuilder().setName("fighters").build();
        Index index = SearchServiceFactory.getSearchService().getIndex(indexSpec);

        final List<FighterListItem> retval = new ArrayList<>();
        Results<ScoredDocument> results = index.search(searchString);
        for (ScoredDocument document : results) {
            final Long id = Long.valueOf(document.getId());
            try {
                logger.log(Level.FINER, "Found {0}", id);
                Entity f = getFighterEntity(id);
                FighterListItem fli = DataTransfer.convertToListItem(f, datastore);
                retval.add(fli);
            } catch (EntityNotFoundException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
        }

        return retval;
    }

    private Entity getFighterEntity(long fighterId) throws EntityNotFoundException {
        Key fighterKey = KeyFactory.createKey(Fighter.class.getSimpleName(), fighterId);

        Entity fighter = datastore.get(fighterKey);
        return fighter;
    }

    public Fighter getFighterByGoogleId(String userId) {
        logger.log(Level.FINE, "Getting {0}", userId);
        Fighter retval = fCache.getFighterByGoogleId(userId);
        if (retval == null) {
            final Query.Filter filter = new Query.FilterPredicate("googleId", Query.FilterOperator.EQUAL, userId);
            final Query query = new Query("Fighter").setFilter(filter);
            final List<Entity> fighters = datastore.prepare(query).asList(FetchOptions.Builder.withDefaults());

            if (fighters != null && fighters.size() > 0) {
                retval = DataTransfer.convert(fighters.get(0), datastore);
            } else {
                retval = getByGoogleIdLowerCase(userId);
            }
        }
        if (retval != null) {
            fCache.put(retval);
        }
        return retval;
    }

    //TODO: if it returns a value, the record should be updated to reflect the correct address.
    private Fighter getByGoogleIdLowerCase(final String userId) {
        final Query.Filter filter = new Query.FilterPredicate("googleId", Query.FilterOperator.EQUAL, userId.toLowerCase());
        final Query query = new Query("Fighter").setFilter(filter);
        final List<Entity> fighters = datastore.prepare(query).asList(FetchOptions.Builder.withDefaults());

        if (fighters != null && fighters.size() > 0) {
            return DataTransfer.convert(fighters.get(0), datastore);
        }
        return null;
    }

    public List<Fighter> queryFightersByScaName(String scaName) {
        List<Fighter> retArray = new ArrayList<>();
        if (StringUtils.isBlank(scaName)) {
            return retArray;
        }
        Query.Filter filter = new Query.FilterPredicate("scaName", Query.FilterOperator.EQUAL, scaName);
        Query query = new Query("Fighter").setFilter(filter);
        List<Entity> fighters = datastore.prepare(query).asList(FetchOptions.Builder.withDefaults());
        if (fighters == null || fighters.isEmpty()) {
            List<Entity> allFighters = returnAllFighters();
            fighters = filterByScaName(allFighters, scaName);
        }
        for (Entity f : fighters) {
            retArray.add(DataTransfer.convert(f, datastore));
        }
        return retArray;
    }

    public List<FighterListItem> getFighterListByScaName(String scaName) {
        List<FighterListItem> retArray = new ArrayList<>();
        List<FighterListItem> fighters = getFighterListItems();
        if (StringUtils.isBlank(scaName)) {
            return fighters;
        }
        for (FighterListItem f : fighters) {
            if (f.getScaName() != null
                    && f.getScaName().toUpperCase().contains(scaName.toUpperCase())) {
                retArray.add(f);
            }
        }
        return retArray;
    }

    private List<Entity> filterByScaName(List<Entity> allFighters, String scaName) {
        List<Entity> retList = new ArrayList<>();
        if (scaName == null || StringUtils.isBlank(scaName)) {
            return retList;
        }

        for (Entity f : allFighters) {
            if (f.hasProperty("scaName")) {
                String entityScaName = (String) f.getProperty("scaName");
                if (entityScaName.toUpperCase().contains(scaName.toUpperCase())) {
                    retList.add(f);
                }
            }
        }
        return retList;
    }

    public List<Fighter> getMinorCount() {
        final LocalDate now = new LocalDate();
        final LocalDate minorDate = now.minusYears(18);

        List<Fighter> retArray = new ArrayList<>();
        Query.Filter filter = new Query.FilterPredicate("dateOfBirth", Query.FilterOperator.GREATER_THAN, minorDate.toDateTimeAtStartOfDay().toDate());
        Query query = new Query("Fighter").setFilter(filter);
        List<Entity> fighters = datastore.prepare(query).asList(FetchOptions.Builder.withDefaults());
        for (Entity f : fighters) {
            retArray.add(DataTransfer.convert(f, datastore));
        }
        return retArray;
    }

    public List<FighterListItem> getFighterListItems(DateTime dt) {
        if (dt == null || dt.getYear() == 1966) {
            return getFighterListItems();
        }

        List<Entity> fighters = getAllFightersAsOf(dt);
        Map<Long, FighterListItem> fighterListMap = new HashMap<>();
        for (Entity f : fighters) {
            FighterListItem fli = DataTransfer.convertToListItem(f, datastore);
            fighterListMap.put(fli.getFighterId(), fli);
        }
        List<FighterListItem> retVal = new ArrayList<>(fighterListMap.values());
        Collections.sort(retVal);
        return retVal;
    }

    public List<FighterListItem> getFighterListItems() {
        List<Entity> fighters = getAllFightersAsOf(fCache.getLastUpdate());
        Map<Long, FighterListItem> fighterListMap = new HashMap<>();
        for (Entity f : fighters) {
            FighterListItem fli = DataTransfer.convertToListItem(f, datastore);
            fighterListMap.put(fli.getFighterId(), fli);
        }
        fCache.putAll(fighterListMap);
        return fCache.getFighterList();
    }

    private List<Entity> getAllFightersAsOf(DateTime dt) {
        if (dt == null || dt.toLocalDate().equals(new LocalDate(1966, 3, 1))) {
            logger.log(Level.INFO, "Getting all fighters");
            return returnAllFighters();
        }
        logger.log(Level.INFO, "Getting fighters as of {0}", dt.toString());
        Query.Filter lastUpdatedFilter = new Query.FilterPredicate("lastUpdated", Query.FilterOperator.GREATER_THAN, dt.toDate());
        Query query = new Query("Fighter").setFilter(lastUpdatedFilter);
        List<Entity> fighters = datastore.prepare(query).asList(FetchOptions.Builder.withDefaults());
        logger.log(Level.INFO, "Getting {0} fighters ", fighters.size());

        return fighters;
    }

    private List<Entity> returnAllFighters() {
        Query query = new Query("Fighter").addSort("scaName");
        List<Entity> fighters = datastore.prepare(query).asList(FetchOptions.Builder.withDefaults());
        return fighters;
    }

    public List<Fighter> getFighters() {
        List<Entity> fighters = returnAllFighters();
        List<Fighter> retArray = new ArrayList<>();
        for (Entity f : fighters) {
            retArray.add(DataTransfer.convert(f, datastore));
        }
        return retArray;
    }

    public Long saveFighter(Fighter fighter, Long userId) throws ValidationException {
        return this.saveFighter(fighter, userId, true);
    }

    private void saveFighterToIndex(Fighter fighter) {
        Document doc = Document.newBuilder().setId(fighter.getFighterId().toString())
                .addField(Field.newBuilder().setName("scaName").setText(fighter.getScaName()))
                .addField(Field.newBuilder().setName("modernName").setText(fighter.getModernName()))
                .addField(Field.newBuilder().setName("googeId").setText(fighter.getGoogleId()))
                .build();

        IndexSpec indexSpec = IndexSpec.newBuilder().setName("fighters").build();
        Index index = SearchServiceFactory.getSearchService().getIndex(indexSpec);

        try {
            index.put(doc);
        } catch (PutException e) {
            if (StatusCode.TRANSIENT_ERROR.equals(e.getOperationResult().getCode())) {
                // retry putting the document
            }
        }
    }

    public Long saveFighter(Fighter fighter, Long userId, boolean validate) throws ValidationException {
        final Long keyValue;

        Entity fighterEntity = null;

        if (fighter.getFighterId() != null && fighter.getFighterId() > 0) {
            Key fighterKey = KeyFactory.createKey(Fighter.class.getSimpleName(), fighter.getFighterId());
            try {
                fighterEntity = datastore.get(fighterKey);
            } catch (EntityNotFoundException ex) {
                fighterEntity = null;
            }
            fCache.remove(fighter.getFighterId());
        }
        fighterEntity = DataTransfer.convert(fighter, fighterEntity);
        if (validate) {
            validate(fighter);
        }

        fighterEntity.setProperty("lastUpdated", new Date());
        fighterEntity.setProperty("userUpdated", userId);
        final Key key = datastore.put(fighterEntity);

        fighter.setFighterId(key.getId());
        saveFighterToIndex(fighter);

        final Query noteQuery = new Query("Note").setAncestor(key);
        Entity noteEntity = datastore.prepare(noteQuery).asSingleEntity();
        if (fighter.getNote() != null) {
            if (StringUtils.isBlank(fighter.getNote().getBody())) {
                if (noteEntity != null) {
                    datastore.delete(noteEntity.getKey());
                }
            } else {
                final Note note = fighter.getNote();
                if (noteEntity == null) {
                    noteEntity = new Entity("Note", key);
                }
                noteEntity.setProperty("body", note.getBody());
                noteEntity.setProperty("updated", note.getUpdated());
                datastore.put(noteEntity);
            }
        }

        final Query authQuery = new Query("Authorization").setAncestor(key);
        List<Entity> authorizationEntities = datastore.prepare(authQuery).asList(FetchOptions.Builder.withDefaults());
        saveAuths(fighter, key, authorizationEntities);

        final Query phoneQuery = new Query("Phone").setAncestor(key);
        List<Entity> phoneEntities = datastore.prepare(phoneQuery).asList(FetchOptions.Builder.withDefaults());
        savePhone(fighter, key, phoneEntities);

        final Query emailQuery = new Query("Email").setAncestor(key);
        final List<Entity> emailEntities = datastore.prepare(emailQuery).asList(FetchOptions.Builder.withDefaults());
        saveEmails(fighter, key, emailEntities);

        final Query addressQuery = new Query("Address").setAncestor(key);
        List<Entity> addressEntities = datastore.prepare(addressQuery).asList(FetchOptions.Builder.withDefaults());
        saveAddresses(fighter, key, addressEntities);

        keyValue = key.getId();

        return keyValue;
    }

    private void savePhone(Fighter fighter, Key key, List<Entity> phoneEntities) {
        if (fighter.getPhone() == null || fighter.getPhone().isEmpty()) {
            List<Key> keys = new ArrayList<>();
            for (Entity entity : phoneEntities) {
                keys.add(entity.getKey());
            }
            datastore.delete(keys);
        } else {
            List<Entity> phones = new ArrayList<>();
            int i = 0;
            for (Phone phone : fighter.getPhone()) {
                Entity phoneEntity;
                if (phoneEntities.size() > i) {
                    phoneEntity = phoneEntities.get(i);
                } else {
                    phoneEntity = new Entity("Phone", key);
                }
                phoneEntity.setProperty("phoneNumber", phone.getPhoneNumber());
                phoneEntity.setProperty("type", phone.getType());
                phones.add(phoneEntity);
                ++i;
            }

            datastore.put(phones);
        }
    }

    private void saveAuths(Fighter fighter, Key key, List<Entity> authorizationEntities) {
        if (fighter.getAuthorization() == null || fighter.getAuthorization().isEmpty()) {
            List<Key> keys = new ArrayList<>();
            for (Entity entity : authorizationEntities) {
                keys.add(entity.getKey());
            }
            datastore.delete(keys);
        } else {
            final List<Entity> auths = new ArrayList<>();
            int i = 0;
            final AuthTypeDAO authTypeDAO = new AuthTypeDAO();
            for (Authorization auth : fighter.getAuthorization()) {
                final Entity authEntity;
                if (authorizationEntities.size() > i) {
                    authEntity = authorizationEntities.get(i);
                } else {
                    authEntity = new Entity("Authorization", key);
                }
                final Key authTypeKey = authTypeDAO.getAuthTypeKeyByCode(auth.getCode());
                authEntity.setProperty("authType", authTypeKey);
                authEntity.setProperty("date", auth.getDate());
                auths.add(authEntity);
                ++i;
            }
            datastore.put(auths);
            final List<Key> keys = new ArrayList<>();
            for (; i < authorizationEntities.size(); ++i) {
                final Entity authEntity = authorizationEntities.get(i);
                keys.add(authEntity.getKey());
            }
            if (!keys.isEmpty()) {
                datastore.delete(keys);
            }
        }
    }

    private void saveAddresses(Fighter fighter, Key fighterKey, List<Entity> addressEntities) {
        if (fighter.getAddress() != null && !fighter.getAddress().isEmpty()) {
            List<Entity> addresses = new ArrayList<>(fighter.getAddress().size());
            int i = 0;
            for (Address address : fighter.getAddress()) {
                Entity addressEntity;
                if (addressEntities.size() > i) {
                    addressEntity = addressEntities.get(i);
                } else {
                    addressEntity = new Entity("Address", fighterKey);
                }
                addressEntity.setProperty("address1", address.getAddress1());
                addressEntity.setProperty("address2", address.getAddress2());
                addressEntity.setProperty("city", address.getCity());
                addressEntity.setProperty("district", address.getDistrict());
                addressEntity.setProperty("postalCode", address.getPostalCode());
                addressEntity.setProperty("state", address.getState());
                addressEntity.setProperty("type", address.getType());
                addresses.add(addressEntity);
                ++i;
            }
            datastore.put(addresses);
        } else {
            List<Key> keys = new ArrayList<>();
            for (Entity entity : addressEntities) {
                keys.add(entity.getKey());
            }
            datastore.delete(keys);
        }
    }

    private void saveEmails(Fighter fighter, Key fighterKey, List<Entity> emailEntities) {
        if (fighter.getEmail() != null && !fighter.getEmail().isEmpty()) {
            final List<Entity> emails = new ArrayList<>();
            int i = 0;
            for (Email email : fighter.getEmail()) {
                if (StringUtils.isNotEmpty(email.getEmailAddress())) {
                    final Entity emailEntity;
                    if (emailEntities.size() > i) {
                        emailEntity = emailEntities.get(i);
                    } else {
                        emailEntity = new Entity("Email", fighterKey);
                    }
                    emailEntity.setProperty("emailAddress", email.getEmailAddress());
                    emailEntity.setProperty("type", email.getType());
                    emails.add(emailEntity);
                    ++i;
                }
            }
            datastore.put(emails);

            final List<Key> keys = new ArrayList<>();
            for (; i < emailEntities.size(); ++i) {
                final Entity entity = emailEntities.get(i);
                keys.add(entity.getKey());
            }
            if (!keys.isEmpty()) {
                datastore.delete(keys);
            }
        } else {
            List<Key> keys = new ArrayList<>();
            for (Entity entity : emailEntities) {
                keys.add(entity.getKey());
            }
            datastore.delete(keys);
        }
    }

    public void deleteFighter(Long fighterId) {
        fCache.remove(fighterId);
        Key fighterKey = KeyFactory.createKey(Fighter.class.getSimpleName(), fighterId);
        datastore.delete(fighterKey);
    }

    protected boolean validate(Fighter fighter) throws ValidationException {
        if (fighter.getScaGroup() == null) {
            throw new ValidationException("Please select SCA Group");
        }

        Query.Filter scaNameFilter = new Query.FilterPredicate("scaName", Query.FilterOperator.EQUAL, fighter.getScaName());
        Query query = new Query("Fighter").setFilter(scaNameFilter);

        List<Entity> fighters = datastore.prepare(query).asList(FetchOptions.Builder.withDefaults());
        if (fighters != null && fighters.size() > 0) {
            throw new ValidationException(fighter.getScaName() + " already exists in the database");
        }
        if (StringUtils.isNotBlank(fighter.getScaMemberNo())) {
            Query.Filter scaMemberNoFilter = new Query.FilterPredicate("scaMemberNo", Query.FilterOperator.EQUAL, fighter.getScaMemberNo());
            query = new Query("Fighter").setFilter(scaMemberNoFilter);
            fighters = datastore.prepare(query).asList(FetchOptions.Builder.withDefaults());
            if (fighters != null && fighters.size() > 0) {
                throw new ValidationException("A fighter with member no " + fighter.getScaMemberNo() + " already exists in the database");
            }
        }
        if (StringUtils.isNotBlank(fighter.getGoogleId())) {
            Query.Filter googleIdFilter = new Query.FilterPredicate("googleId", Query.FilterOperator.EQUAL, fighter.getGoogleId());
            query = new Query("Fighter").setFilter(googleIdFilter);
            fighters = datastore.prepare(query).asList(FetchOptions.Builder.withDefaults());
            if (fighters != null && fighters.size() > 0) {
                throw new ValidationException("A fighter with Google Id " + fighter.getGoogleId() + " already exists in the database");
            }
        }
        if (fighter.getAddress() != null && fighter.getAddress().size() > 0) {
            Address address = fighter.getAddress().get(0);
            Query.Filter modernNameFilter = new Query.FilterPredicate("modernName", Query.FilterOperator.EQUAL, fighter.getModernName());
            query = new Query("Fighter").setFilter(modernNameFilter);
            fighters = datastore.prepare(query).asList(FetchOptions.Builder.withDefaults());
            if (fighters != null && fighters.size() > 0) {
                for (Entity f : fighters) {
                    Query addressQuery = new Query("Address").setAncestor(f.getKey());
                    List<Entity> addressList = datastore.prepare(addressQuery).asList(FetchOptions.Builder.withDefaults());
                    if (addressList.size() > 0) {
                        Entity a = addressList.get(0);
                        if (StringUtils.equals((String) a.getProperty("address1"), address.getAddress1())
                                && StringUtils.equals((String) a.getProperty("city"), address.getCity())
                                && StringUtils.equals((String) a.getProperty("state"), address.getState())) {
                            throw new ValidationException("A fighter with that modern name and address already exists in the database");
                        }
                    }
                }
            }
        }
        return true;
    }

    public int getTotalCount(boolean useDeleted) {
        Query query;
        if (!useDeleted) {
            final Query.Filter deleteFilter = new Query.FilterPredicate("status", Query.FilterOperator.NOT_EQUAL, "DELETED");
            query = new Query("Fighter").setFilter(deleteFilter).setKeysOnly();
        } else {
            query = new Query("Fighter").setKeysOnly();
        }
        FetchOptions fetchOptions = FetchOptions.Builder.withDefaults();
        PreparedQuery pq = datastore.prepare(query);
        return pq.countEntities(fetchOptions);
    }

    public int getFighterCountInGroup(ScaGroup scaGroup, boolean useDeleted) {
        ScaGroupDAO groupDao = new ScaGroupDAO();
        Key groupKey = groupDao.getScaGroupKey(scaGroup.getGroupName());
        Query.Filter scaGroupFilter = new Query.FilterPredicate("scaGroup", Query.FilterOperator.EQUAL, groupKey);
        Query query;

        if (!useDeleted) {
            final Query.Filter deleteFilter = new Query.FilterPredicate("status", Query.FilterOperator.NOT_EQUAL, "DELETED");
            final Query.Filter validFilter = Query.CompositeFilterOperator.and(scaGroupFilter, deleteFilter);
            query = new Query("Fighter").setFilter(validFilter).setKeysOnly();
        } else {
            query = new Query("Fighter").setFilter(scaGroupFilter).setKeysOnly();
        }

        FetchOptions fetchOptions = FetchOptions.Builder.withDefaults();
        PreparedQuery pq = datastore.prepare(query);
        return pq.countEntities(fetchOptions);
    }

    public int getMinorCountInGroup(ScaGroup scaGroup) {
        final LocalDate now = new LocalDate();
        final LocalDate minorDate = now.minusYears(18);
        final ScaGroupDAO groupDao = new ScaGroupDAO();
        final Key groupKey = groupDao.getScaGroupKey(scaGroup.getGroupName());
        final Query.Filter scaGroupFilter = new Query.FilterPredicate("scaGroup", Query.FilterOperator.EQUAL, groupKey);
        final Query.Filter filter = new Query.FilterPredicate("dateOfBirth", Query.FilterOperator.GREATER_THAN, minorDate.toDateTimeAtStartOfDay().toDate());
        final Query.Filter validFilter = Query.CompositeFilterOperator.and(scaGroupFilter, filter);
        final Query query = new Query("Fighter").setFilter(validFilter).setKeysOnly();
        final FetchOptions fetchOptions = FetchOptions.Builder.withDefaults();
        final PreparedQuery pq = datastore.prepare(query);
        return pq.countEntities(fetchOptions);
    }

}
