/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package org.calontir.marshallate.falcon.tags;

import java.io.IOException;
import javax.servlet.jsp.JspWriter;
import org.calontir.marshallate.falcon.common.UserRoles;
import org.calontir.marshallate.falcon.dto.Treaty;
import org.calontir.marshallate.falcon.user.Security;
import org.calontir.marshallate.falcon.user.SecurityFactory;

/**
 *
 * @author rik
 */
public class TreatyTag extends CMPExtendedTagSupport {

    private Treaty treaty;
    private Security security = SecurityFactory.getSecurity();

    @Override
    protected void doView(JspWriter out) throws IOException {
        if (security.isRoleOrGreater(UserRoles.CARD_MARSHAL)) {
            if (treaty != null) {
                out.println("Treaty");
            }
        } else {
            out.println("");
        }
    }

    @Override
    protected void doEdit(JspWriter out) throws IOException {
        if (security.isRoleOrGreater(UserRoles.CARD_MARSHAL)) {
            out.print("<input type=\"checkbox\" name=\"treaty\" value=\"treaty\"");
            if (treaty != null) {
                out.print(" checked ");
            }
            out.print(" />");
            out.println("Treaty");
        } else {
            doView(out);
        }
    }

    @Override
    protected void doAdd(JspWriter out) throws IOException {
        if (security.isRoleOrGreater(UserRoles.CARD_MARSHAL)) {
            out.print("<input type=\"checkbox\" name=\"treaty\" value=\"treaty\"");
            if (treaty != null) {
                out.print(" checked ");
            }
            out.print(" />");
            out.println("Treaty");
        }
    }

    public void setTreaty(Treaty treaty) {
        this.treaty = treaty;
    }
}
