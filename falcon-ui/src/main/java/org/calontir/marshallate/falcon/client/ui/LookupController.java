package org.calontir.marshallate.falcon.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.view.client.ListDataProvider;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.calontir.marshallate.falcon.client.FighterInfo;
import org.calontir.marshallate.falcon.client.FighterListInfo;
import org.calontir.marshallate.falcon.client.FighterService;
import org.calontir.marshallate.falcon.client.FighterServiceAsync;
import org.calontir.marshallate.falcon.dto.AuthType;
import org.calontir.marshallate.falcon.dto.Report;
import org.calontir.marshallate.falcon.dto.ScaGroup;

/**
 * Gets the data from local storage and the server.
 *
 * @author Rik Scarborough
 */
public class LookupController {

    private static final Logger log = Logger.getLogger(LookupController.class.getName());
    private static final LookupController _instance = new LookupController();
    private List<AuthType> authTypes = null;
    private List<ScaGroup> scaGroups = null;
    private boolean fighterDLComplete = false;
    private FighterServiceAsync fighterService = GWT.create(FighterService.class);
    public String versionId;

    private LookupController() {
        try {
            buildTables();
        } catch (Exception e) {
            log.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public static LookupController getInstance() {
        return _instance;
    }

    public boolean isDLComplete() {
        return authTypes != null && scaGroups != null && fighterDLComplete;
    }

    public List<AuthType> getAuthType() {
        return authTypes;
    }

    public List<ScaGroup> getScaGroups() {
        return scaGroups;
    }

    public ScaGroup getScaGroup(String name) {
        if (name == null || name.isEmpty()) {
            log.log(Level.SEVERE, "searching for empty group");
            return null;
        }
        for (ScaGroup group : scaGroups) {
            if (group == null || group.getGroupName() == null) {
                log.log(Level.SEVERE, "group in list is null");
                continue;
            }
            if (name.equals(group.getGroupName())) {
                return group;
            }
        }
        return null;
    }

    void searchFighters(final String searchName, final CellTable<FighterInfo> table, final ListDataProvider<FighterInfo> dataProvider) {
        fighterService.searchFighters(searchName, new AsyncCallback<FighterListInfo>() {

            @Override
            public void onFailure(Throwable caught) {
                log.log(Level.SEVERE, "searchFighters:", caught);
            }

            @Override
            public void onSuccess(FighterListInfo result) {
                List<FighterInfo> fil = result.getFighterInfo();
                table.setRowCount(fil.size());
                List<FighterInfo> data = dataProvider.getList();
                data.clear();
                for (FighterInfo fi : fil) {
                    data.add(fi);
                }
            }
        });
    }

    private void buildTables() {
        fighterService.initialLookup(new AsyncCallback<Map<String, Object>>() {
            @Override
            public void onFailure(Throwable caught) {
            }

            @Override
            @SuppressWarnings("unchecked")
            public void onSuccess(Map<String, Object> result) {
                versionId = (String) result.get("appversion");
                authTypes = (List<AuthType>) result.get("authTypes");
                scaGroups = (List<ScaGroup>) result.get("groups");
                fighterDLComplete = true;
            }
        });
    }

    public void retrieveReports() {
        fighterService.getAllReports(new AsyncCallback<List<Report>>() {
            @Override
            public void onFailure(Throwable caught) {
                log.severe("getAllReports " + caught.getMessage());
            }

            @Override
            public void onSuccess(List<Report> result) {
                String s = "";
                for (Report r : result) {
                    s += r.getMarshalName() + "<br";
                    for (String k : r.getReportParams().keySet()) {
                        s += k + ":" + r.getReportParams().get(k) + "<br>";
                    }
                }
                Window.alert(s);
            }
        });
    }
}
