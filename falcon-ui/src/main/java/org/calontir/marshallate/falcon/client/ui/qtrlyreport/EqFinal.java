package org.calontir.marshallate.falcon.client.ui.qtrlyreport;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.dom.client.ParagraphElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Panel;
import java.util.Date;
import org.calontir.marshallate.falcon.common.ReportingMarshalType;

/**
 *
 * @author rikscarborough
 */
public class EqFinal extends BaseReportPage {

    final Panel bk = new FlowPanel();
    Panel report;

    @Override
    public void buildPage() {
        bk.setStylePrimaryName(REPORTBG);

        String p1 = "Press submit to send your report.  When you submit your report, you should recieve a copy of your report by email.";
        HTML para1 = new HTML(p1);
        para1.setStylePrimaryName(REPORT_INSTRUCTIONS);
        bk.add(para1);

        add(bk);
    }

    private void buildReport(Panel bk) {
        HeadingElement header = Document.get().createHElement(1);
        header.setInnerText("Equestrian Report");
        bk.getElement().insertAfter(header, null);

        String rmType = (String) getReportInfo().get("Reporting Marshal Type");
        ReportingMarshalType rmt = ReportingMarshalType.getByCode(rmType);

        buildOne("Reporting Marshal Type: ", rmt.getValue(), bk);

        buildOne("Marshal Type: ", (String) getReportInfo().get("Marshal Type"), bk);
        buildOne("Equestrian Marshal in Charge: ", (String) getReportInfo().get("Equestrian Marshal in Charge"), bk);
        buildOne("Additional Marshals: ", (String) getReportInfo().get("Additional Marshals"), bk);
        buildOne("Hosting SCA Group: ", (String) getReportInfo().get("Hosting SCA Group"), bk);
        buildOne("Name of Event: ", (String) getReportInfo().get("Name of Event"), bk);
        buildOne("Date of Event: ", ((Date) getReportInfo().get("Date of Event")).toString(), bk);
    }

    private void buildOne(String title, String body, Panel bk) {
        ParagraphElement para = Document.get().createPElement();
        HeadingElement hthree = Document.get().createHElement(3);
        hthree.getStyle().setDisplay(Style.Display.INLINE);
        hthree.setInnerText(title);
        SpanElement span = Document.get().createSpanElement();
        span.setInnerText(body);
        para.insertFirst(hthree);
        para.insertAfter(span, null);

        bk.getElement().insertAfter(para, null);
    }

    @Override
    public void onDisplay() {
        report = new FlowPanel();
        buildReport(report);
        bk.add(report);
        nextButton.getElement().getStyle().setDisplay(Style.Display.NONE);
        submitButton.getElement().getStyle().setDisplay(Style.Display.INLINE_BLOCK);
    }

    @Override
    public void onLeavePage() {
        bk.remove(report);
        nextButton.getElement().getStyle().setDisplay(Style.Display.INLINE_BLOCK);
        submitButton.getElement().getStyle().setDisplay(Style.Display.NONE);
    }
}
