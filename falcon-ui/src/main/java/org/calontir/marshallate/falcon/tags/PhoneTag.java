package org.calontir.marshallate.falcon.tags;

import java.io.IOException;
import java.util.List;
import javax.servlet.jsp.JspWriter;
import org.apache.commons.lang3.StringUtils;
import org.calontir.marshallate.falcon.dto.Phone;

/**
 *
 * @author rik
 */
public class PhoneTag extends CMPExtendedTagSupport {

    private List numbers;
    // local
    private Phone number;

    @Override
    protected void init() {
        @SuppressWarnings("unchecked")
        List<Phone> numberList = (List<Phone>) numbers;
        number = (numberList == null || numberList.isEmpty()) ? new Phone() : numberList.get(0);
    }

    @Override
    protected void doView(JspWriter out) throws IOException {
        out.print(StringUtils.trimToEmpty(number.getPhoneNumber()));
    }

    @Override
    protected void doEdit(JspWriter out) throws IOException {
        out.print("<input type=\"text\" name=\"phoneNumber\"");
        out.print(" value=\"" + StringUtils.trimToEmpty(number.getPhoneNumber()) + "\"");
        out.print(" size=\"40\"");
        out.println("<br>");
    }

    @Override
    protected void doAdd(JspWriter out) throws IOException {
        out.print("<input type=\"text\" name=\"phoneNumber\"");
        out.print(" value=\"" + StringUtils.trimToEmpty(number.getPhoneNumber()) + "\"");
        out.print(" size=\"40\"");
        out.print(" onfocus=\"if (this.value==this.defaultValue) this.value='';"
                + " else this.select()\" onblur=\"if (!this.value) this.value=this.defaultValue)\"");
        out.print(" />");
        out.println("<br>");
    }

    public void setNumbers(List numbers) {
        this.numbers = numbers;
    }
}
