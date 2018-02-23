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

    public FighterListInfo getListItems(Date targetDate);

    public Fighter getFighter(Long id);

    public FighterListInfo searchFighters(String searchString);

    public Fighter getFighterByScaName(String scaName);

    public FighterListResultWrapper getFighters(String cursor, Integer pageSize, Integer offset);

    public FighterListResultWrapper getFightersByGroup(ScaGroup group, String cursor, Integer pageSize, Integer offset);

    public Long saveFighter(Fighter fighter);

    public List<AuthType> getAuthTypes();

    public List<ScaGroup> getGroups();

    public Map<String, Object> initialLookup();

    public Integer getMinorTotal(String group);

    public List<Fighter> getMinorFighters(String group);

    public void sendReportInfo(Map<String, Object> reportInfo);

    public List<Report> getAllReports();

    public List<Report> getReports(Integer days);

    public void deleteReport(Report report);

    public FighterListResultWrapper getFightersSortedByScaName(Integer pageSize);

    public FighterListResultWrapper getFightersSortedByScaGroup(Integer pageSize);

    public FighterListResultWrapper getFightersSortedByStatus(Integer pageSize);
}
