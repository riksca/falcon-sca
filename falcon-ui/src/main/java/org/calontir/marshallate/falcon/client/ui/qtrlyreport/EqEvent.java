package org.calontir.marshallate.falcon.client.ui.qtrlyreport;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Panel;
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
