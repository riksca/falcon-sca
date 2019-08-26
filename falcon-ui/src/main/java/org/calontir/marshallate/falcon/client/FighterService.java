package org.calontir.marshallate.falcon.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.calontir.marshallate.falcon.dto.AuthType;
import org.calontir.marshallate.falcon.dto.Fighter;
import org.calontir.marshallate.falcon.dto.Report;
import org.calontir.marshallate.falcon.dto.ScaGroup;

@RemoteServiceRelativePath("fighter")
public interface FighterService extends RemoteService {

    public Integer countFightersInGroup(String group);
    public Integer countMinorsInGroup(String group);
    public void deleteReport(Report report);

    public List<Report> getAllReports();

    public List<AuthType> getAuthTypes();

    public Fighter getFighter(Long id);

    public Fighter getFighterByScaName(String scaName);

    public FighterListResultWrapper getFighters(String cursor, Integer pageSize, Integer offset);

    public FighterListResultWrapper getFightersByGroup(ScaGroup group, String cursor, Integer pageSize, Integer offset);

    public FighterListResultWrapper getFightersSortedByScaGroup(Integer pageSize);

    public FighterListResultWrapper getFightersSortedByScaName(Integer pageSize);

    public FighterListResultWrapper getFightersSortedByStatus(Integer pageSize);

    public List<ScaGroup> getGroups();

    public FighterListInfo getListItems(Date targetDate);

    public List<Fighter> getMinorFighters(String group);
    public Integer getMinorTotal(String group);
    public List<Report> getReports(Integer days);
    public List<Report> getReports(String quarter);

    public Map<String, Object> initialLookup();

    public Long saveFighter(Fighter fighter);

    public FighterListInfo searchFighters(String searchString);

    public void sendReportInfo(Map<String, Object> reportInfo);
}
