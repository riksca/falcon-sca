package org.calontir.marshallate.falcon.server;

import com.google.appengine.api.NamespaceManager;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.modules.ModulesService;
import com.google.appengine.api.modules.ModulesServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.calontir.marshallate.falcon.ValidationException;
import org.calontir.marshallate.falcon.client.FighterInfo;
import org.calontir.marshallate.falcon.client.FighterListInfo;
import org.calontir.marshallate.falcon.client.FighterListResultWrapper;
import org.calontir.marshallate.falcon.client.FighterService;
import org.calontir.marshallate.falcon.common.UserRoles;
import org.calontir.marshallate.falcon.db.AuthTypeDAO;
import org.calontir.marshallate.falcon.db.FighterDAO;
import org.calontir.marshallate.falcon.db.ReportDAO;
import org.calontir.marshallate.falcon.db.ScaGroupDAO;
import org.calontir.marshallate.falcon.db.TableUpdatesDao;
import org.calontir.marshallate.falcon.dto.AuthType;
import org.calontir.marshallate.falcon.dto.Fighter;
import org.calontir.marshallate.falcon.dto.FighterListItem;
import org.calontir.marshallate.falcon.dto.FighterResultWrapper;
import org.calontir.marshallate.falcon.dto.Report;
import org.calontir.marshallate.falcon.dto.ScaGroup;
import org.calontir.marshallate.falcon.dto.TableUpdates;
import org.calontir.marshallate.falcon.user.Security;
import org.calontir.marshallate.falcon.user.SecurityFactory;
import org.joda.time.DateTime;

/**
 *
 * @author rikscarborough
 */
public class FighterServiceImpl extends RemoteServiceServlet implements FighterService {

    private static final Logger log = Logger.getLogger(FighterServiceImpl.class.getName());

    @Override
    public FighterListInfo getListItems(Date targetDate) {
        FighterListInfo retval = new FighterListInfo();
        FighterDAO fighterDao = new FighterDAO();
        TableUpdatesDao tuDao = new TableUpdatesDao();
        TableUpdates tu = tuDao.getTableUpdates("Fighter");
        List<FighterListItem> fighters;
        fighters = fighterDao.getFighterListItems(new DateTime(targetDate));
        retval.setUpdateInfo(true);

        return convert(fighters);
    }

    private FighterListInfo convert(List<FighterListItem> fighters) {
        final FighterListInfo retval = new FighterListInfo();
        final List<FighterInfo> retValList = new ArrayList<>();
        for (FighterListItem fli : fighters) {
            if (fli != null) {
                final FighterInfo info = new FighterInfo();
                info.setFighterId(fli.getFighterId() == null ? 0 : fli.getFighterId());
                info.setScaName(fli.getScaName() == null ? "" : fli.getScaName());
                info.setAuthorizations(fli.getAuthorizations() == null ? "" : fli.getAuthorizations());
                info.setGroup(fli.getGroup() == null ? "" : fli.getGroup());
                info.setStatus(fli.getStatus() == null ? "" : fli.getStatus().toString());
                info.setMinor(fli.isMinor());
                info.setRole(fli.getRole() == null ? "" : fli.getRole());
                info.setSupport(fli.isSupport());
                retValList.add(info);
            }
        }
        retval.setFighterInfo(retValList);

        return retval;
    }

    @Override
    public Fighter getFighter(Long id) {
        FighterDAO fighterDao = new FighterDAO();

        return fighterDao.getFighter(id);
    }

    @Override
    public FighterListResultWrapper getFighters(String cursor, Integer pageSize, Integer offset) {
        final Security security = SecurityFactory.getSecurity();
        final FighterDAO fighterDao = new FighterDAO();
        FighterResultWrapper fighterResults;
        if (cursor == null) {
            fighterResults = fighterDao.getFighters(pageSize, offset, security.isRoleOrGreater(UserRoles.CARD_MARSHAL));
        } else {
            Cursor startCursor = Cursor.fromWebSafeString(cursor);
            fighterResults = fighterDao.getFighters(pageSize, startCursor, security.isRoleOrGreater(UserRoles.CARD_MARSHAL));
        }
        final String newCursor = fighterResults.getCursor() == null ? null : fighterResults.getCursor().toWebSafeString();

        final FighterListResultWrapper fighterListResults = new FighterListResultWrapper();
        fighterListResults.setFighters(convert(fighterResults.getFighters()));
        fighterListResults.setCursor(newCursor);
        fighterListResults.setPageSize(pageSize);
        fighterListResults.setCount(fighterDao.getTotalCount(security.isRoleOrGreater(UserRoles.CARD_MARSHAL)));
        return fighterListResults;
    }

    @Override
    public FighterListResultWrapper getFightersByGroup(ScaGroup group, String cursor, Integer pageSize, Integer offset) {
        final Security security = SecurityFactory.getSecurity();
        final FighterDAO fighterDao = new FighterDAO();
        final FighterResultWrapper fighterResults;
        if (cursor == null) {
            fighterResults = fighterDao.getFightersByGroup(group, pageSize, offset, security.isRoleOrGreater(UserRoles.CARD_MARSHAL));
        } else {
            final Cursor startCursor = Cursor.fromWebSafeString(cursor);
            fighterResults = fighterDao.getFightersByGroup(group, pageSize, startCursor, security.isRoleOrGreater(UserRoles.CARD_MARSHAL));
        }
        final String newCursor = fighterResults.getCursor() == null ? null : fighterResults.getCursor().toWebSafeString();

        final FighterListResultWrapper fighterListResults = new FighterListResultWrapper();
        fighterListResults.setFighters(convert(fighterResults.getFighters()));
        fighterListResults.setCursor(newCursor);
        fighterListResults.setPageSize(pageSize);
        fighterListResults.setCount(fighterDao.getFighterCountInGroup(group, security.isRoleOrGreater(UserRoles.CARD_MARSHAL)));
        return fighterListResults;
    }

    @Override
    public FighterListResultWrapper getFightersSortedByScaName(Integer pageSize) {
        final Security security = SecurityFactory.getSecurity();
        final FighterDAO fighterDao = new FighterDAO();
        FighterResultWrapper fighterResults = fighterDao.getFightersSortedByScaName(pageSize);
        final String newCursor = fighterResults.getCursor().toWebSafeString();

        final FighterListResultWrapper fighterListResults = new FighterListResultWrapper();
        fighterListResults.setFighters(convert(fighterResults.getFighters()));
        fighterListResults.setCursor(newCursor);
        fighterListResults.setPageSize(pageSize);
        fighterListResults.setCount(fighterDao.getTotalCount(security.isRoleOrGreater(UserRoles.CARD_MARSHAL)));
        return fighterListResults;
    }

    @Override
    public FighterListResultWrapper getFightersSortedByScaGroup(Integer pageSize) {
        final Security security = SecurityFactory.getSecurity();
        final FighterDAO fighterDao = new FighterDAO();
        FighterResultWrapper fighterResults = fighterDao.getFightersSortedByGroup(pageSize);
        final String newCursor = fighterResults.getCursor().toWebSafeString();

        final FighterListResultWrapper fighterListResults = new FighterListResultWrapper();
        fighterListResults.setFighters(convert(fighterResults.getFighters()));
        fighterListResults.setCursor(newCursor);
        fighterListResults.setPageSize(pageSize);
        fighterListResults.setCount(fighterDao.getTotalCount(security.isRoleOrGreater(UserRoles.CARD_MARSHAL)));
        return fighterListResults;
    }

    @Override
    public FighterListResultWrapper getFightersSortedByStatus(Integer pageSize) {
        final Security security = SecurityFactory.getSecurity();
        final FighterDAO fighterDao = new FighterDAO();
        FighterResultWrapper fighterResults = fighterDao.getFightersSortedByStatus(pageSize);
        final String newCursor = fighterResults.getCursor().toWebSafeString();

        final FighterListResultWrapper fighterListResults = new FighterListResultWrapper();
        fighterListResults.setFighters(convert(fighterResults.getFighters()));
        fighterListResults.setCursor(newCursor);
        fighterListResults.setPageSize(pageSize);
        fighterListResults.setCount(fighterDao.getTotalCount(security.isRoleOrGreater(UserRoles.CARD_MARSHAL)));
        return fighterListResults;
    }

    @Override
    public Long saveFighter(Fighter fighter) {
        FighterDAO fighterDao = new FighterDAO();
        try {
            return fighterDao.saveFighter(fighter, fighter.getFighterId(), false);
        } catch (ValidationException ex) {
            log.log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public FighterListInfo searchFighters(String searchString) {
        final Security security = SecurityFactory.getSecurity();
        if (!security.isRoleOrGreater(UserRoles.CARD_MARSHAL)) {
            searchString = "scaName = " + searchString;
        }
        final FighterDAO fighterDao = new FighterDAO();
        return convert(fighterDao.searchFighters(searchString));
    }

    @Override
    public List<AuthType> getAuthTypes() {
        AuthTypeDAO dao = new AuthTypeDAO();
        return dao.getAuthType();
    }

    @Override
    public List<ScaGroup> getGroups() {
        ScaGroupDAO dao = new ScaGroupDAO();
        return dao.getScaGroup();
    }

    @Override
    public Fighter getFighterByScaName(String scaName) {
        FighterDAO fighterDao = new FighterDAO();
        List<Fighter> fList = fighterDao.queryFightersByScaName(scaName);
        if (fList != null && !fList.isEmpty()) {
            return fList.get(0);
        }
        return null;
    }

    @Override
    public Map<String, Object> initialLookup() {
        log.log(Level.INFO, "Start Initial Lookup");
        Map<String, Object> iMap = new HashMap<>();
        // get application version
        iMap.put("appversion", "2.0.4");

        // get groups
        ScaGroupDAO groupDao = new ScaGroupDAO();
        List<ScaGroup> groups = groupDao.getScaGroup();
        iMap.put("groups", groups);

        // get authtypes
        AuthTypeDAO authTypeDao = new AuthTypeDAO();
        List<AuthType> authTypes = authTypeDao.getAuthType();
        iMap.put("authTypes", authTypes);

        return iMap;
    }

    @Override
    public Integer getMinorTotal(String group) {
        int ret = 0;
        FighterDAO fighterDao = new FighterDAO();
        ScaGroupDAO groupDao = new ScaGroupDAO();
        //ScaGroup scaGroup = groupDao.getScaGroupByName(group);
        List<Fighter> fList = fighterDao.getMinorCount();
        for (Fighter f : fList) {
            if (f.getScaGroup().getGroupName().equals(group)) {
                ++ret;
            }
        }
        return ret;
    }

    @Override
    public List<Fighter> getMinorFighters(String group) {
        FighterDAO fighterDao = new FighterDAO();
        List<Fighter> fList = fighterDao.getMinorCount();
        List<Fighter> retList = new ArrayList<>();
        for (Fighter f : fList) {
            if (f.getScaGroup().getGroupName().equals(group)) {
                retList.add(f);
            }
        }
        return retList;
    }

    @Override
    public void sendReportInfo(Map<String, Object> reportInfo) {
        log("send report");
        final ModulesService modulesApi = ModulesServiceFactory.getModulesService();

        try {
            final String version = modulesApi.getDefaultVersion("adminb");
            final URL url = new URL("http://"
                    + modulesApi.getVersionHostname("adminb", version)
                    + "/BuildReport.groovy");
            log("Sending report to " + url.toString());
            final HttpURLConnection connection
                    = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");

            final OutputStreamWriter writer
                    = new OutputStreamWriter(connection.getOutputStream());
            boolean first = true;
            for (String key : reportInfo.keySet()) {
                if (reportInfo.get(key) instanceof Collection) {
                } else {
                    if (first) {
                        first = false;
                    } else {
                        writer.write("&");
                    }
                    String value = URLEncoder.encode(reportInfo.get(key).toString(), "UTF-8");
                    writer.write(key + "=" + value);
                }
            }
            writer.close();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                log("Connection ok");
            } else {
                log("Connection " + connection.getResponseCode());
            }
        } catch (MalformedURLException mue) {
            log(mue.getLocalizedMessage(), mue);
        } catch (IOException ioe) {
            log(ioe.getLocalizedMessage(), ioe);
        }
    }

    @Override
    public List<Report> getAllReports() {
        String namespace = NamespaceManager.get();
        try {
            NamespaceManager.set("calontir");
            ReportDAO dao = new ReportDAO();
            return dao.select();
        } finally {
            NamespaceManager.set(namespace);
        }
    }

    @Override
    public void deleteReport(Report report) {
        String namespace = NamespaceManager.get();
        try {
            NamespaceManager.set("calontir");
            ReportDAO dao = new ReportDAO();
            dao.delete(report);
        } finally {
            NamespaceManager.set(namespace);
        }
    }

    @Override
    public List<Report> getReports(Integer days) {
        final Security security = SecurityFactory.getSecurity();
        log(String.format("Getting reports for the last %d days", days));
        List<Report> reports = null;
        log.log(Level.INFO, security.getUser().toString());
        if (security.isRoleOrGreater(UserRoles.DEPUTY_EARL_MARSHAL)) {
            log.log(Level.INFO, "Card Marshal");
            final String namespace = NamespaceManager.get();
            try {
                NamespaceManager.set("calontir");
                ReportDAO dao = new ReportDAO();
                reports = dao.getForDays(days);
            } finally {
                NamespaceManager.set(namespace);
            }
        } else {
            log.log(Level.INFO, "No reports allowed");
        }

        return reports;
    }

    @Override
    public Integer countFightersInGroup(String group) {
        final Security security = SecurityFactory.getSecurity();
        final FighterDAO fighterDao = new FighterDAO();
        final ScaGroupDAO groupDao = new ScaGroupDAO();
        final ScaGroup scaGroup = groupDao.getScaGroupByName(group);
        return fighterDao.getFighterCountInGroup(scaGroup, security.isRoleOrGreater(UserRoles.CARD_MARSHAL));
    }

    @Override
    public Integer countMinorsInGroup(String group) {
        final FighterDAO fighterDao = new FighterDAO();
        final ScaGroupDAO groupDao = new ScaGroupDAO();
        final ScaGroup scaGroup = groupDao.getScaGroupByName(group);
        return fighterDao.getMinorCountInGroup(scaGroup);
    }

}
