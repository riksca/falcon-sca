package org.calontir.marshallate.falcon.client.ui.qtrlyreport;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RadioButton;
import java.util.Date;
import org.calontir.marshallate.falcon.client.user.Security;
import org.calontir.marshallate.falcon.client.user.SecurityFactory;
import org.calontir.marshallate.falcon.common.ReportingMarshalType;
import org.calontir.marshallate.falcon.common.UserRoles;

/**
 *
 * @author rikscarborough
 */
public class Welcome extends BaseReportPage {

    final private Security security = SecurityFactory.getSecurity();

    protected RadioButton acButton;

    @Override
    public void buildPage() {
        clear();

        Panel bk = new FlowPanel();
        bk.setStylePrimaryName(REPORTBG);
        String p1 = "<h1>Marshal Report Form</h1>";
        HTML para1 = new HTML(p1);
        para1.setStylePrimaryName(REPORT_TITLE);
        bk.add(para1);

        String p2;
        if (security.isRole(UserRoles.DEPUTY_EARL_MARSHAL)) {
            p2 = "To complete Deputy Earl Marshal Report, press the Next>> button then follow the instructions.";
            addReportInfo("Marshal Type", "Deputy Earl Marshal");
        } else if (security.isRole(UserRoles.KNIGHTS_MARSHAL)) {
            p2 = "To complete Group Marshal Report, press the Next>> button then follow the instructions.";
            addReportInfo("Marshal Type", "Group Marshal");
        } else if (security.isRole(UserRoles.CUT_AND_THRUST_MARSHAL)) {
            p2 = "To complete Cut and Thrust Marshal Report, press the Next>> button then follow the instructions.";
            addReportInfo("Marshal Type", "Cut and Thrust Marshal");
        } else {
            p2 = "To complete Marshal of the Field Report, press the Next>> button then follow the instructions.";
            addReportInfo("Marshal Type", "Marshal of the Field");
        }
        p2 = p2 + " Please note, the correct quarter should be selected; do not change unless you need a different quarter.";
        HTML para2 = new HTML(p2);
        para2.setStylePrimaryName(REPORT_INSTRUCTIONS);
        bk.add(para2);

        Panel marshalTypePanel = new HorizontalPanel();
        marshalTypePanel.setStylePrimaryName(REPORT_BUTTON_PANEL);
        HTML mTypeInst = new HTML("Please select a Marshal Type: ");
        marshalTypePanel.add(mTypeInst);
        // Marshal Type buttons
        acButton = armoredCombatButton(marshalTypePanel);
        calonSteelButton(marshalTypePanel);
        equestrianButton(marshalTypePanel);
        bk.add(marshalTypePanel);

        int quarter = getQuarter();

        Panel qtrButtonPanel = new HorizontalPanel();
        qtrButtonPanel.setStylePrimaryName(REPORT_BUTTON_PANEL);
        RadioButton qtr1Button = new RadioButton("reportType", "1st Quarter (April 15th)");
        qtr1Button.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
            @Override
            public void onValueChange(ValueChangeEvent<Boolean> event) {
                if (event.getValue()) {
                    addReportInfo("Report Type", "1st Quarter");
                }
            }
        });
        qtrButtonPanel.add(qtr1Button);

        RadioButton qtr2Button = new RadioButton("reportType", "2nd Quarter (July 15th)");
        qtr2Button.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
            @Override
            public void onValueChange(ValueChangeEvent<Boolean> event) {
                if (event.getValue()) {
                    addReportInfo("Report Type", "2nd Quarter");
                }
            }
        });
        qtrButtonPanel.add(qtr2Button);

        RadioButton qtr3Button = new RadioButton("reportType", "3rd Quarter (October 15th)");
        qtr3Button.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
            @Override
            public void onValueChange(ValueChangeEvent<Boolean> event) {
                if (event.getValue()) {
                    addReportInfo("Report Type", "3rd Quarter");
                }
            }
        });
        qtrButtonPanel.add(qtr3Button);

        RadioButton qtr4Button = new RadioButton("reportType", "4th Quarter (January 15th)");
        qtr4Button.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
            @Override
            public void onValueChange(ValueChangeEvent<Boolean> event) {
                if (event.getValue()) {
                    addReportInfo("Report Type", "4th Quarter");
                }
            }
        });
        qtrButtonPanel.add(qtr4Button);

        switch (quarter) {
            case 0:
                qtr1Button.setValue(Boolean.TRUE, true);
                break;
            case 1:
                qtr2Button.setValue(Boolean.TRUE, true);
                break;
            case 2:
                qtr3Button.setValue(Boolean.TRUE, true);
                break;
            case 3:
                qtr4Button.setValue(Boolean.TRUE, true);
                break;
            default:
                qtr4Button.setValue(Boolean.TRUE, true);
        }

        bk.add(qtrButtonPanel);

        Panel eventButtonPanel = new HorizontalPanel();
        eventButtonPanel.setStylePrimaryName(REPORT_BUTTON_PANEL);
        RadioButton eventButton = new RadioButton("reportType", "Event Report");
        eventButton.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
            @Override
            public void onValueChange(ValueChangeEvent<Boolean> event) {
                if (event.getValue()) {
                    getRequired().clear();
                    addReportInfo("Report Type", "Event");
                    for (int i = getDeck().getWidgetCount(); i > 1; --i) {
                        getDeck().remove(i - 1);
                    }

                    PersonalInfo pi = new PersonalInfo();
                    pi.init(reportInfo, required, submitButton, nextButton);
                    pi.getElement().setId("personalinfo");
                    getDeck().add(pi);

                    ReportingMarshalType marshalType = ReportingMarshalType.getByCode((String) getReportInfo(
                            "Reporting Marshal Type"));

                    if (marshalType.equals(ReportingMarshalType.EQUESTRIAN)) {
                        EqEvent eqEvent = new EqEvent();
                        eqEvent.init(reportInfo, required, submitButton, nextButton);
                        getDeck().add(eqEvent);

//                        EqEventDetail eqEventDetail = new EqEventDetail();
//                        eqEventDetail.init(reportInfo, required, submitButton, nextButton);
//                        getDeck().add(eqEventDetail);

                        EqFinal finalPage = new EqFinal();
                        finalPage.init(reportInfo, required, submitButton, nextButton);
                        getDeck().add(finalPage);
                    } else {
                        Activities activities = new Activities();
                        activities.init(reportInfo, required, submitButton, nextButton);
                        getDeck().add(activities);

                        InjuryReport injuryReport = new InjuryReport();
                        injuryReport.init(reportInfo, required, submitButton, nextButton);
                        getDeck().add(injuryReport);

                        Summary summary = new Summary();
                        summary.init(reportInfo, required, submitButton, nextButton);
                        getDeck().add(summary);

                        Final finalPage = new Final();
                        finalPage.init(reportInfo, required, submitButton, nextButton);
                        getDeck().add(finalPage);
                    }

                }
            }
        });
        eventButtonPanel.add(eventButton);
        bk.add(eventButtonPanel);

        add(bk);

	onDisplay();
    }

    private void equestrianButton(Panel marshalTypePanel) {
        final RadioButton button = new RadioButton("marshalType", ReportingMarshalType.EQUESTRIAN.getValue());
        button.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
            @Override
            public void onValueChange(ValueChangeEvent<Boolean> event) {
                if (event.getValue()) {
                    getRequired().clear();
		    nextButton.setEnabled(true);
		    nextButton.setTitle("Next");
                    addReportInfo("Reporting Marshal Type", ReportingMarshalType.EQUESTRIAN.getCode());
                    for (int i = getDeck().getWidgetCount(); i > 1; --i) {
                        getDeck().remove(i - 1);
                    }

                    PersonalInfo pi = new PersonalInfo();
                    pi.init(reportInfo, required, submitButton, nextButton);
                    pi.getElement().setId("personalinfo");
                    getDeck().add(pi);

                    Summary summary = new Summary();
                    summary.init(reportInfo, required, submitButton, nextButton);
                    getDeck().add(summary);

                    Final finalPage = new Final();
                    finalPage.init(reportInfo, required, submitButton, nextButton);
                    getDeck().add(finalPage);
                }
            }
        });
        button.setValue(Boolean.FALSE, true);
        marshalTypePanel.add(button);
    }

    private void calonSteelButton(Panel marshalTypePanel) {
        final RadioButton calonSteelButton = new RadioButton("marshalType", ReportingMarshalType.CALON_STEEL.getValue());
        calonSteelButton.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
            @Override
            public void onValueChange(ValueChangeEvent<Boolean> event) {
                if (event.getValue()) {
                    getRequired().clear();
		    nextButton.setEnabled(true);
		    nextButton.setTitle("Next");
                    addReportInfo("Reporting Marshal Type", ReportingMarshalType.CALON_STEEL.getCode());
                    for (int i = getDeck().getWidgetCount(); i > 1; --i) {
                        getDeck().remove(i - 1);
                    }

                    PersonalInfo pi = new PersonalInfo();
                    pi.init(reportInfo, required, submitButton, nextButton);
                    pi.getElement().setId("personalinfo");
                    getDeck().add(pi);

                    Activities activities = new Activities();
                    activities.init(reportInfo, required, submitButton, nextButton);
                    getDeck().add(activities);

                    Summary summary = new Summary();
                    summary.init(reportInfo, required, submitButton, nextButton);
                    getDeck().add(summary);

                    Final finalPage = new Final();
                    finalPage.init(reportInfo, required, submitButton, nextButton);
                    getDeck().add(finalPage);
                }
            }
        });
        calonSteelButton.setValue(Boolean.FALSE, true);
        marshalTypePanel.add(calonSteelButton);
    }

    private RadioButton armoredCombatButton(Panel marshalTypePanel) {
        final RadioButton armoredCombatButton = new RadioButton("marshalType", ReportingMarshalType.ARMORED_COMBAT.getValue());
        armoredCombatButton.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
            @Override
            public void onValueChange(ValueChangeEvent<Boolean> event) {
                if (event.getValue()) {
                    getRequired().clear();
		    nextButton.setEnabled(true);
		    nextButton.setTitle("Next");
                    addReportInfo("Reporting Marshal Type", ReportingMarshalType.ARMORED_COMBAT.getCode());
                    for (int i = getDeck().getWidgetCount(); i > 1; --i) {
                        getDeck().remove(i - 1);
                    }

                    PersonalInfo pi = new PersonalInfo();
                    pi.init(reportInfo, required, submitButton, nextButton);
                    pi.getElement().setId("personalinfo");
                    getDeck().add(pi);

                    Activities activities = new Activities();
                    activities.init(reportInfo, required, submitButton, nextButton);
                    getDeck().add(activities);

                    if (security.isRole(UserRoles.KNIGHTS_MARSHAL) || security.isRole(UserRoles.DEPUTY_EARL_MARSHAL)) {
                        InjuryReport injuryReport = new InjuryReport();
                        injuryReport.init(reportInfo, required, submitButton, nextButton);
                        getDeck().add(injuryReport);

                        FighterComment fc = new FighterComment();
                        fc.init(reportInfo, required, submitButton, nextButton);
                        getDeck().add(fc);
                    }

                    Summary summary = new Summary();
                    summary.init(reportInfo, required, submitButton, nextButton);
                    getDeck().add(summary);

                    Final finalPage = new Final();
                    finalPage.init(reportInfo, required, submitButton, nextButton);
                    getDeck().add(finalPage);
                }
            }
        });
        marshalTypePanel.add(armoredCombatButton);
        return armoredCombatButton;
    }

    @SuppressWarnings("deprecation")
    // allow date deprecation because this is converted to JavaScript and GWT does not support other ways.
    private int getQuarter() {
        Date now = new Date();
        if (now.getMonth() < 2 || now.getMonth() == 11) {
            return 3;
        }

        if (now.getMonth() < 5) {
            return 0;
        }

        if (now.getMonth() < 7) {
            return 1;
        }

        if (now.getMonth() < 11) {
            return 2;
        }
        return 0;
    }

    @Override
    public void onDisplay() {
        nextButton.setEnabled(false);
	nextButton.setTitle("Set the Report Marshal Type to enable.");
    }

    @Override
    public void onLeavePage() {
    }

}
