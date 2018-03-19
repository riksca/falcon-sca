package org.calontir.marshallate.falcon.client.ui.qtrlyreport;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TextBox;
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

        Label eqMicLabel = new Label();
        eqMicLabel.setText("Equestrian Marshal in Charge");
        infoPanel.add(eqMicLabel);
        TextBox eqMic = new TextBox();
        eqMic.getElement().setId("eqMic");
        eqMic.setValue((String) getReportInfo().get("Equestrian Marshal in Charge"), false);
        addRequired("Equestrian Marshal in Charge");
        eqMic.addValueChangeHandler((ValueChangeEvent<String> event) -> {
            addReportInfo("Equestrian Marshal in Charge", event.getValue());
        });
        infoPanel.add(eqMicLabel);
        infoPanel.add(eqMic);

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
