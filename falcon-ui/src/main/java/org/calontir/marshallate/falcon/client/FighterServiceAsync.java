/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.calontir.marshallate.falcon.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.calontir.marshallate.falcon.dto.AuthType;
import org.calontir.marshallate.falcon.dto.Fighter;
import org.calontir.marshallate.falcon.dto.Report;
import org.calontir.marshallate.falcon.dto.ScaGroup;

/**
 *
 * @author rikscarborough
 */
public interface FighterServiceAsync {

    public void countFightersInGroup(String group, AsyncCallback<Integer> async);

    public void countMinorsInGroup(String group, AsyncCallback<Integer> async);

    public void getListItems(Date targetDate, AsyncCallback<FighterListInfo> async);

    public void getFighter(Long id, AsyncCallback<Fighter> async);

    public void getFighters(String cursor, Integer pageSize, Integer offset, AsyncCallback<FighterListResultWrapper> async);

    public void getFightersByGroup(ScaGroup group, String cursor, Integer pageSize, Integer offset, AsyncCallback<FighterListResultWrapper> async);

    public void searchFighters(String searchString, AsyncCallback<FighterListInfo> async);

    public void getFighterByScaName(String scaName, AsyncCallback<Fighter> async);

    public void saveFighter(Fighter fighter, AsyncCallback<Long> async);

    public void getAuthTypes(AsyncCallback<List<AuthType>> async);

    public void getGroups(AsyncCallback<List<ScaGroup>> async);

    public void initialLookup(AsyncCallback<Map<String, Object>> async);

    public void getMinorTotal(String group, AsyncCallback<Integer> async);

    public void sendReportInfo(Map<String, Object> reportInfo, AsyncCallback<Void> async);

    public void getAllReports(AsyncCallback<List<Report>> async);

    public void getReports(Integer days, AsyncCallback<List<Report>> async);
    public void getReports(String quarter, AsyncCallback<List<Report>> async);

    public void deleteReport(Report report, AsyncCallback<Void> async);

    public void getMinorFighters(String group, AsyncCallback<List<Fighter>> asyncCallback);

    public void getFightersSortedByScaName(Integer pageSize, AsyncCallback<FighterListResultWrapper> fighterAsyncCallback);

    public void getFightersSortedByScaGroup(Integer pageSize, AsyncCallback<FighterListResultWrapper> fighterAsyncCallback);

    public void getFightersSortedByStatus(Integer pageSize, AsyncCallback<FighterListResultWrapper> fighterAsyncCallback);
}
