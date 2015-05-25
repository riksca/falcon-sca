/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.calontir.marshallate.falcon.client.ui;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Panel;
import java.util.logging.Logger;
import org.calontir.marshallate.falcon.client.DisplayUtils;
import org.calontir.marshallate.falcon.client.ui.SearchEvent.SearchType;
import org.calontir.marshallate.falcon.client.ui.fighterlist.FighterCellTable;
import org.calontir.marshallate.falcon.client.ui.fighterlist.FighterCellTableFactory;
import org.calontir.marshallate.falcon.dto.ScaGroup;

/**
 *
 * @author rikscarborough
 */
public class FighterListBox extends Composite implements SearchEventHandler {

    private static final Logger log = Logger.getLogger(FighterListBox.class.getName());
    private final Shout shout = Shout.getInstance();
    private final FighterCellTable table = FighterCellTableFactory.create();

    public FighterListBox() {
        Panel listBackground = new FlowPanel();
        listBackground.getElement().setId(DisplayUtils.Displays.ListBox.toString());
        listBackground.getElement().getStyle().setDisplay(Style.Display.NONE);

        Panel listPanel = new FlowPanel();
        listPanel.addStyleName("top");
        listPanel.addStyleName("inline_table");

        listPanel.add(table);
        listPanel.add(table.getPager());
        listBackground.add(listPanel);
        final Image legendImage = new Image();
        legendImage.setUrl("/images/falcon_icon_legend.png");
        Panel legendPanel = new FlowPanel();
        legendPanel.addStyleName("inline");
        legendPanel.getElement().getStyle().setMargin(2.5, Style.Unit.EM);
        legendPanel.add(legendImage);
        listBackground.add(legendPanel);

        initWidget(listBackground);

    }

    @Override
    public void find(String searchName) {
        table.searchFighters(searchName);
        DisplayUtils.changeDisplay(DisplayUtils.Displays.ListBox, true);
    }

    public FighterCellTable getTable() {
        return table;
    }

    @Override
    public void loadAll() {
        table.loadAllFighters();
        DisplayUtils.changeDisplay(DisplayUtils.Displays.ListBox, true);
    }

    @Override
    public void loadGroup(ScaGroup group) {
        table.loadSelectedGroup(group);
        DisplayUtils.changeDisplay(DisplayUtils.Displays.ListBox, true);
    }

    @Override
    public void switchSearchType(SearchType searchType) {
    }
}
