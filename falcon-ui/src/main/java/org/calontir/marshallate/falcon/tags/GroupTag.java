package org.calontir.marshallate.falcon.tags;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import javax.servlet.jsp.JspWriter;
import org.calontir.marshallate.falcon.dto.ScaGroup;
import org.calontir.marshallate.falcon.db.ScaGroupDAO;

/**
 *
 * @author rik
 */
public class GroupTag extends CMPExtendedTagSupport {

    private String mode;
    private String groupName;
    private ScaGroupDAO groupDao = null;

    @Override
    protected void init() {
        groupDao = new ScaGroupDAO();

    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    protected void doAdd(JspWriter out) throws IOException {
        List<ScaGroup> groups = groupDao.getScaGroup();
        Collections.sort(groups);

        out.println("<select name=\"scaGroup\">");
        out.println("<option value=\"SELECTGROUP\">Select a group</option>");
        for (ScaGroup group : groups) {
            out.println("<option value=\"" + group.getGroupName() + "\">" + group.getGroupName() + "</option>");
        }
        out.println("</select>");
    }

    protected void doView(JspWriter out) throws IOException {
        if (groupName != null) {
            out.println("<input type=\"hidden\" name=\"scaGroup\" value=\"" + groupName + "\"/>");
            out.print(groupName);
        }
    }

    protected void doEdit(JspWriter out) throws IOException {
        List<ScaGroup> groups = groupDao.getScaGroup();
        Collections.sort(groups);

        out.println("<select name=\"scaGroup\">");
        for (ScaGroup group : groups) {
            out.print("<option ");
            if (groupName != null && group.getGroupName() != null) {
                if (group.getGroupName().equals(groupName)) {
                    out.print(" selected ");
                }
            }
            out.print(" value=\"" + group.getGroupName() + "\">" + group.getGroupName() + "</option>");
        }
        out.println("</select>");
    }
}
