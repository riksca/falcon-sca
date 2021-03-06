/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.calontir.marshallate.falcon.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.Panel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.calontir.marshallate.falcon.client.DisplayUtils;
import org.calontir.marshallate.falcon.client.FighterService;
import org.calontir.marshallate.falcon.client.FighterServiceAsync;
import org.calontir.marshallate.falcon.client.ui.qtrlyreport.BaseReportPage;
import org.calontir.marshallate.falcon.client.ui.qtrlyreport.Welcome;
import org.calontir.marshallate.falcon.client.user.Security;
import org.calontir.marshallate.falcon.client.user.SecurityFactory;

/**
 *
 * @author rikscarborough
 */
public class ReportGen extends Composite {

    private static final Logger log = Logger.getLogger(ReportGen.class.getName());
    final private Security security = SecurityFactory.getSecurity();
    Map<String, Object> reportInfo = new HashMap<>();

    public void init() {
        final DeckPanel deck = new DeckPanel();
        deck.setAnimationEnabled(true);
        History.newItem("qrtlyreport:Welcome");

        final FocusWidget next = buildNextLink(deck);
        next.setEnabled(true);

        final Button submit = new Button("Submit Report");
        submit.setEnabled(false);
        submit.getElement().getStyle().setTextAlign(Style.TextAlign.RIGHT);
        submit.getElement().getStyle().setDisplay(Style.Display.NONE);
        submit.addClickHandler((ClickEvent event) -> {
            if (submit.isEnabled()) {
                final Shout shout = Shout.getInstance();
                shout.tell("Submitting report, please wait");
                submit.setEnabled(false);
                FighterServiceAsync fighterService = GWT.create(FighterService.class);
                fighterService.sendReportInfo(reportInfo, new AsyncCallback<Void>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        shout.hide();
                        shout.tell("Error on submitting report", false);
                        log.log(Level.INFO, "Error on submitting report", caught);
                    }

                    @Override
                    public void onSuccess(Void result) {
                        shout.hide();
                        shout.tell("Thank you for submitting your report");
                        DisplayUtils.resetDisplay();
                    }
                });
            }
        });

        //reportInfo.put("Email Cc", user.);
        reportInfo.put("user.googleid", security.getLoginInfo().getEmailAddress());
        List<String> required = new ArrayList<>();

        Welcome welcome = new Welcome();
        welcome.setDeck(deck);
        welcome.init(reportInfo, required, submit, next);
        deck.add(welcome);

        Panel background = new FlowPanel();

        background.add(deck);
        deck.showWidget(0);

        background.add(buildPrevLink(deck));
        background.add(next);
        background.add(submit);

        initWidget(background);
    }

    private FocusWidget buildNextLink(final DeckPanel deck) {
        final Button nextLink = new Button("Next >>");
        nextLink.addClickHandler((ClickEvent event) -> {
            if (nextLink.isEnabled()) {
                int index = deck.getVisibleWidget();
                if (index < deck.getWidgetCount() - 1) {
                    if (index >= 0) {
                        BaseReportPage prevPage = (BaseReportPage) deck.getWidget(index);
                        prevPage.onLeavePage();
                    }
                    index++;
                    BaseReportPage nextPage = (BaseReportPage) deck.getWidget(index);
                    nextPage.onDisplay();
                    deck.showWidget(index);
                }
            }
        });
        nextLink.setWidth("90px");
//		nextLink.setHeight(".90em");
//		nextLink.getElement().getStyle().setFontSize(0.75, Style.Unit.EM);

        return nextLink;
    }

    private FocusWidget buildPrevLink(final DeckPanel deck) {
        final Button prevLink = new Button("<< Prev");
        prevLink.addClickHandler((ClickEvent event) -> {
            if (prevLink.isEnabled()) {
                int index = deck.getVisibleWidget();
                if (index == 1) {
                    Shout.getInstance().tell(
                            "To reselect the report type, select the Report link from the menu and start again.");
                } else if (index > 1) {
                    if (index < deck.getWidgetCount()) {
                        BaseReportPage prevPage = (BaseReportPage) deck.getWidget(index);
                        prevPage.onLeavePage();
                    }
                    --index;
                    BaseReportPage nextPage = (BaseReportPage) deck.getWidget(index);
                    nextPage.onDisplay();
                    deck.showWidget(index);
                }
            }
        });
        prevLink.setWidth("90px");
//		nextLink.setHeight(".90em");
//		nextLink.getElement().getStyle().setFontSize(0.75, Style.Unit.EM);

        return prevLink;
    }
}
