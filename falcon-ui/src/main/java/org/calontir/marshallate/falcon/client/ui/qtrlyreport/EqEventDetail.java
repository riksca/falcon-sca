package org.calontir.marshallate.falcon.client.ui.qtrlyreport;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import java.util.logging.Logger;

/**
 *
 * @author richardscarborough
 */
public class EqEventDetail extends BaseReportPage {
    private static final Logger log = Logger.getLogger(BaseReportPage.class.getName());

    @Override
    public void buildPage() {
        clear();

        final Panel bk = new FlowPanel();
        bk.setStylePrimaryName(REPORTBG);

        Panel infoPanel = new FlowPanel();

        addField(infoPanel, "Number of Equines", "eqNumEquines", "Number of Equines", true);
        addField(infoPanel, "Number of Rentals (if applicable)", "numRentals", "Number of Rentals", true);
        addField(infoPanel, "Number of Adult Riders", "numAdultRiders", "Number of Adult Riders", true);
        addField(infoPanel, "Number of Youth Riders", "numYouthRiders", "Number of Youth Riders", true);
        Label label = new Label();
        label.setText("Activities Held (select all that apply)");
        addCheckBox(infoPanel, "General Riding", "General Riding");
        addCheckBox(infoPanel, "Mounted Games", "Mounted Games");
        addCheckBox(infoPanel, "Mounted Combat", "Mounted Combat");
        addCheckBox(infoPanel, "Crest Combat", "Crest Combat");
        addCheckBox(infoPanel, "Foam Jousting", "Foam Jousting");
        addCheckBox(infoPanel, "Mounted Archery", "Mounted Archery");
        addCheckBox(infoPanel, "Experimental-Wood Jousting", "Experimental-Wood Jousting");
        addCheckBox(infoPanel, "Experimental-Mounted Combat with Add'l Simulators",
                "Experimental-Mounted Combat with Add'l Simulators");
        addCheckBox(infoPanel, "Experimental-Mounted Combat with Thrusting Tips",
                "Experimental-Mounted Combat with Thrusting Tips");
        addField(infoPanel, "Other", "otherAct", "Other Activity", false);

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
