package org.calontir.marshallate.falcon.client.ui.qtrlyreport;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.google.gwt.user.datepicker.client.DateBox;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 *
 * @author rikscarborough
 */
public abstract class BaseReportPage extends SimplePanel {

    private static final Logger log = Logger.getLogger(BaseReportPage.class.getName());
    public static final String REPORT_BUTTON_PANEL = "reportButtonPanel";
    public static final String REPORTBG = "reportbg";
    public static final String REPORT_TITLE = "reportTitle";
    public static final String REPORT_INSTRUCTIONS = "reportInstructions";
    public static final String REPORT_TEXT_BOX = "reportTextBox";
    public static final String PERSONAL_INFO = "personalInfo";
    protected Map<String, Object> reportInfo;
    protected List<String> required;
    protected FocusWidget submitButton;
    protected FocusWidget nextButton;
    protected DeckPanel deck;

    public void init(Map<String, Object> reportInfo, List<String> required, FocusWidget submitButton, FocusWidget nextButton) {
        this.reportInfo = reportInfo;
        this.required = required;
        this.submitButton = submitButton;
        this.nextButton = nextButton;
        buildPage();
    }

    public abstract void buildPage();

    public abstract void onDisplay();

    public abstract void onLeavePage();

    public boolean enableNext() {
        return true;
    }

    protected void addCheckBox(Panel infoPanel, String labelText, String reportName) {
        CheckBox cb = new CheckBox(labelText);
        cb.setValue(true);

        cb.addClickHandler((ClickEvent event) -> {
            boolean checked = ((CheckBox) event.getSource()).getValue();
            addReportInfo(reportName, checked);
        });

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

    public boolean addReportInfo(String key, Object value, boolean required) {
        boolean retVal = addReportInfo(key, value);
        if (required) {
            addRequired(key);
        }
        return retVal;
    }

    public boolean addReportInfo(String key, Object value) {
        boolean removeValue = false;
        if (value == null) {
            removeValue = true;
        } else if (value instanceof String) {
            String strValue = (String) value;
            if (strValue.trim().isEmpty()) {
                removeValue = true;
            }
        }

        if (removeValue) {
            if (reportInfo.containsKey(key)) {
                reportInfo.remove(key);
            }
        } else {
            reportInfo.put(key, value);
            if (enableNext()) {
                nextButton.setEnabled(true);
            }
        }

        if (allRequired()) {
            submitButton.setEnabled(true);
        } else {
            submitButton.setEnabled(false);
        }
        return !removeValue;
    }

    protected boolean allRequired() {
        int count = required.size();
        String logStr = "";
        logStr += "Required count " + count + "\n";
        for (String test : required) {
            logStr += test;
            if (reportInfo.containsKey(test)) {
                --count;
                logStr += " found";
            }
            logStr += "\n";
        }
        logStr += "returning count " + count;
        log.fine(logStr);
        return count == 0;
    }

    public void addRequired(String newRequired) {
        required.add(newRequired);
        if (!allRequired()) {
            submitButton.setEnabled(false);
        }
    }

    public Map<String, Object> getReportInfo() {
        return reportInfo;
    }

    public Object getReportInfo(String key) {
        return reportInfo.get(key);
    }

    public List<String> getRequired() {
        return required;
    }

    public DeckPanel getDeck() {
        return deck;
    }

    public void setDeck(DeckPanel deck) {
        this.deck = deck;
    }

    public class RequiredFieldKeyPressHandler implements KeyPressHandler {

        private final String requiredField;

        public RequiredFieldKeyPressHandler(String requiredField) {
            this.requiredField = requiredField;
        }

        @Override
        public void onKeyPress(KeyPressEvent event) {
            if (event.getSource() instanceof RichTextArea) {
                RichTextArea textBox = (RichTextArea) event.getSource();
                addReportInfo(requiredField, textBox.getHTML());
                if (textBox.getText().isEmpty()) {
                    nextButton.setEnabled(false);
                } else {
                    if (enableNext()) {
                        nextButton.setEnabled(true);
                    }
                }
            } else {
                TextBoxBase textBox = (TextBoxBase) event.getSource();
                if (textBox.getValue().isEmpty()) {
                    nextButton.setEnabled(false);
                } else {
                    if (enableNext()) {
                        nextButton.setEnabled(true);
                    }
                }
            }
        }
    }
}
