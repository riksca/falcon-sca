package org.calontir.marshallate.falcon.tags;

import java.io.IOException;
import javax.servlet.jsp.JspWriter;
import org.calontir.marshallate.falcon.user.Security;
import org.calontir.marshallate.falcon.user.SecurityFactory;

/**
 *
 * @author rik
 */
public class PrintButton extends CMPExtendedTagSupport {

    private Long fighterId;

    @Override
    protected void doView(JspWriter out) throws IOException {
        Security security = SecurityFactory.getSecurity();

        if (security.canPrintFighter(fighterId)) {
            out.print("<span class=\"printButton\">"
                    + "<a href=\"#\" id=\"BPrint\" class=\"BPrint\""
                    + " onClick=\"printThis(document.fighterInfoForm);\">Print</a>"
                        + "</span>");
        } else {
            out.println();
        }
    }

    @Override
    protected void doEdit(JspWriter out) throws IOException {
        out.print("");
    }

    @Override
    protected void doAdd(JspWriter out) throws IOException {
        out.print("");
    }

    public void setFighterId(Long fighterId) {
        this.fighterId = fighterId;
    }
}
