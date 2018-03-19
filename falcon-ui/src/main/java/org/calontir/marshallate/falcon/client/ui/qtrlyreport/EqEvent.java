package org.calontir.marshallate.falcon.client.ui.qtrlyreport;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.datepicker.client.DateBox;
import java.util.Date;
import java.util.logging.Logger;

/**
 *
 * @author richardscarborough
 */
public class EqEvent extends BaseReportPage {
    private static final Logger log = Logger.getLogger(BaseReportPage.class.getName());

    @Override
    public void buildPage() {
        clear();

        final Panel bk = new FlowPanel();
        bk.setStylePrimaryName(REPORTBG);

        Panel infoPanel = new FlowPanel();

        addField(infoPanel, "Equestrian Marshal in Charge", "eqMic", "Equestrian Marshal in Charge", true);
        addField(infoPanel, "Additional Marshals (if applicable - specifically, those In Training)", "addMarshals",
                "Additional Marshals", true);
        addField(infoPanel, "Hosting SCA Group", "hostingGroup", "Hosting SCA Group", true);
        addField(infoPanel, "Name of Event", "eventName", "Name of Event", true);
        addDateField(infoPanel, "Date of Event", "eventDate", "Date of Event", true);

        bk.add(infoPanel);
        add(bk);

    }

    protected void addDateField(Panel infoPanel, String labelText, String elementName, String reportName, boolean required) {
        Label label = new Label();
        label.setText(labelText);
        DateBox eventDate = new DateBox();
        eventDate.getTextBox().getElement().setId(elementName);
        eventDate.getTextBox().setName(elementName);
        if (getReportInfo().containsKey(reportName)) {
            eventDate.setValue(DateTimeFormat.getFormat("MM/dd/yyyy").parse((String) getReportInfo().get(reportName)));
        }
        eventDate.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getFormat("MM/dd/yyyy")));
        if (required) {
            addRequired(reportName);
        }
        eventDate.getTextBox().addKeyPressHandler(new RequiredFieldKeyPressHandler(reportName));
        eventDate.addValueChangeHandler((ValueChangeEvent<Date> event) -> {
            addReportInfo(reportName, event.getValue());
        });
        infoPanel.add(label);
        infoPanel.add(eventDate);
    }

    protected void addField(Panel infoPanel, String labelText, String elementName, String reportName, boolean required) {
        Label label = new Label();
        label.setText(labelText);
        TextBox textBox = new TextBox();
        textBox.getElement().setId(elementName);
        textBox.setValue((String) getReportInfo().get(reportName), false);
        if (required) {
            addRequired(reportName);
        }
        textBox.addValueChangeHandler((ValueChangeEvent<String> event) -> {
            addReportInfo(reportName, event.getValue());
        });
        infoPanel.add(label);
        infoPanel.add(textBox);
    }

    @Override
    public boolean enableNext() {
        return allRequired();
    }

    @Override
    public void onDisplay() {
        nextButton.setEnabled(enableNext());
    }

    @Override
    public void onLeavePage() {
    }

}
