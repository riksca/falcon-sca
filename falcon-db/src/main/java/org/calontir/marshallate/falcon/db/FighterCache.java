package org.calontir.marshallate.falcon.db;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.calontir.marshallate.falcon.dto.Fighter;
import org.calontir.marshallate.falcon.dto.FighterListItem;

/**
 *
 * @author rik
 */
public class FighterCache implements LocalCache {
    // TODO: Realized that this is not threadsafe. Most methods hear have to be syncorized our we
    // are going to be handing different records because of race conditions.

    final private static FighterCache _instance = new FighterCache();
    private final Map<Long, Fighter> _fighterMap = Collections.synchronizedMap(new HashMap<Long, Fighter>());
    private final Map<String, Long> _fighterByGoogleId = Collections.synchronizedMap(new HashMap<String, Long>());
    private final Map<Long, FighterListItem> _fighterListMap = Collections.synchronizedMap(new HashMap<Long, FighterListItem>());
    private static DateTime lastUpdated = null;

    private FighterCache() {
    }

    public static FighterCache getInstance() {
        return _instance;
    }

    public DateTime getLastUpdate() {
        return lastUpdated;
    }

    private synchronized void _updated() {
        DateTime now = new DateTime();
        if (lastUpdated == null || now.isAfter(lastUpdated)) {
            lastUpdated = now;
        }
    }

    public Fighter getFighter(Long id) {
        return _fighterMap.get(id);
    }

    public Fighter getFighterByGoogleId(String googleId) {
        Long key = _fighterByGoogleId.get(googleId);
        if (key == null) {
            return null;
        }
        return getFighter(key);
    }

    public FighterListItem getFighterListItem(Long id) {
        return _fighterListMap.get(id);
    }

    public synchronized List<FighterListItem> getFighterList() {
        List<FighterListItem> retVal = new ArrayList<FighterListItem>(_fighterListMap.values());
        Collections.sort(retVal);
        return retVal;
    }

    public synchronized void putAll(Map<Long, FighterListItem> fighterMap) {
        _updated();
        _fighterListMap.putAll(fighterMap);
    }

    public synchronized void put(Fighter fighter) {
        _fighterMap.put(fighter.getFighterId(), fighter);
        if (!StringUtils.isEmpty(fighter.getGoogleId())) {
            _fighterByGoogleId.put(fighter.getGoogleId(), fighter.getFighterId());
        }
    }

    public synchronized void put(FighterListItem fighter) {
        _updated();
        _fighterListMap.put(fighter.getFighterId(), fighter);
    }

    public void remove(Long id) {
        removeFighter(id);
        removeFighterListItem(id);
    }

    public void removeFighter(Long id) {
        _fighterMap.remove(id);
    }

    public void removeFighterListItem(Long id) {
        _fighterListMap.remove(id);
    }

    @Override
    public void clear() {
        _fighterMap.clear();
        _fighterByGoogleId.clear();
        _fighterListMap.clear();
        lastUpdated = null;
    }

    @Override
    public int getCount() {
        throw new UnsupportedOperationException("getCount Not supported yet.");
    }
}
