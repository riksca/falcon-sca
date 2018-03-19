package org.calontir.marshallate.falcon.client.ui.qtrlyreport;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.dom.client.ParagraphElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Panel;
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

        if (getReportInfo().containsKey("Marshal Type")) {
            buildOne("Marshal Type: ", getReportInfo().get("Marshal Type").toString(), bk);
        }
        if (getReportInfo().containsKey("Equestrian Marshal in Charge")) {
            buildOne("Equestrian Marshal in Charge: ",
                    getReportInfo().get("Equestrian Marshal in Charge").toString(), bk);
        }
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

    private void buildTwo(String title, String body, Panel bk) {
        HeadingElement hthree = Document.get().createHElement(3);
        hthree.setInnerText(title);
        ParagraphElement para = Document.get().createPElement();
        if (body != null && !body.trim().isEmpty()) {
            para.setInnerHTML(body);
        }
        bk.getElement().insertAfter(hthree, null);
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
