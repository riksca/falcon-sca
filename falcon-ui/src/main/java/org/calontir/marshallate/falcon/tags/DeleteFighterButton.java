package org.calontir.marshallate.falcon.tags;

import java.io.IOException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

/**
 *
 * @author rik
 */
public class DeleteFighterButton extends CMPExtendedTagSupport {
    
    @Override
    public void doTag() throws JspException {
        JspWriter out = getJspContext().getOut();

        try {
            init();

            if (mode != null && mode.equals("add")) {
                doAdd(out);
            } else if (mode.startsWith("edit")) {
                doEdit(out);
            } else {
                doView(out);
            }
        } catch (java.io.IOException ex) {
            throw new JspException("Error in DeleteFighterButton tag", ex);
        }
    }

    protected void doView(JspWriter out) throws IOException {
        out.print("<span class=\"deleteButton\">"
                + "<a href=\"#\" id=\"Delete\" class=\"Delete\""
                + " onClick=\"$( '#dialog-confirm' ).dialog('open')\">Delete</a>"
                + "</span>");
    }

    protected void doEdit(JspWriter out) throws IOException {
        out.println();
    }

    protected void doAdd(JspWriter out) throws IOException {
        out.println();
    }


}
