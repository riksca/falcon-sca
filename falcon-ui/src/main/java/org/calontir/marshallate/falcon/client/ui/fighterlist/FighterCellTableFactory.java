package org.calontir.marshallate.falcon.client.ui.fighterlist;

/**
 *
 * @author rik
 */
public class FighterCellTableFactory {

    public static FighterCellTable create() {
        FighterCellTable table = new FighterCellTable();
        table.createPager();
        table.createSelectColumn();
        table.createImageColumn();
        table.createScaNameColumn();
        table.createAuthorizationColumn();
        table.createGroupColumn();
        table.createStatusColumn();

        table.addColumns();

        table.createSelectionModel();

        table.createDataProvider();

        return table;
    }

}
