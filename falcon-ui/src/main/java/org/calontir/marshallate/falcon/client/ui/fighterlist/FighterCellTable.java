package org.calontir.marshallate.falcon.client.ui.fighterlist;

import com.google.gwt.cell.client.ButtonCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.ImageCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.calontir.marshallate.falcon.client.FighterInfo;
import org.calontir.marshallate.falcon.client.FighterListInfo;
import org.calontir.marshallate.falcon.client.FighterListResultWrapper;
import org.calontir.marshallate.falcon.client.FighterService;
import org.calontir.marshallate.falcon.client.FighterServiceAsync;
import org.calontir.marshallate.falcon.client.ui.EditViewEvent;
import org.calontir.marshallate.falcon.client.ui.Mode;
import org.calontir.marshallate.falcon.client.ui.Shout;
import org.calontir.marshallate.falcon.client.user.Security;
import org.calontir.marshallate.falcon.client.user.SecurityFactory;
import org.calontir.marshallate.falcon.common.UserRoles;
import org.calontir.marshallate.falcon.dto.Fighter;
import org.calontir.marshallate.falcon.dto.ScaGroup;

/**
 *
 * @author rik
 */
public class FighterCellTable extends CellTable<FighterInfo> {

    private static final Logger log = Logger.getLogger(FighterCellTable.class.getName());
    private TextColumn<FighterInfo> authorizationColumn;
    private TextColumn<FighterInfo> groupColumn;
    private Column<FighterInfo, String> imageColumn;
    private SimplePager pager;
    private TextColumn<FighterInfo> scaNameColumn;
    private final Security security = SecurityFactory.getSecurity();
    private Column<FighterInfo, String> selectColumn;
    private final Shout shout = Shout.getInstance();
    private TextColumn<FighterInfo> statusColumn;
    private AsyncDataProviderImpl aDataProvider;

    protected FighterCellTable() {
        super();
    }

    public SimplePager getPager() {
        return pager;
    }

    public void loadAllFighters() {
        aDataProvider.setDataProviderType(DataProviderType.FIGHTER);
        pager.firstPage();
        aDataProvider.onRangeChanged(this);
    }

    public void loadSelectedGroup(ScaGroup group) {
        aDataProvider.setDataProviderType(DataProviderType.GROUP);
        aDataProvider.setGroup(group);
        pager.firstPage();
        aDataProvider.onRangeChanged(this);
    }

    public void searchFighters(String searchName) {
        aDataProvider.setDataProviderType(DataProviderType.SEARCH);
        aDataProvider.setSearchName(searchName);
        pager.firstPage();
        aDataProvider.onRangeChanged(this);
    }

    public void setPager(SimplePager pager) {
        this.pager = pager;
    }

    protected void addColumns() {
        if (selectColumn != null) {
            addColumn(selectColumn, "");
        }
        if (imageColumn != null) {
            addColumn(imageColumn);
        }
        if (scaNameColumn != null) {
            addColumn(scaNameColumn, "SCA Name");
        }

        if (authorizationColumn != null) {
            addColumn(authorizationColumn, "Authorizations");
        }
        if (groupColumn != null) {
            addColumn(groupColumn, "Group");
        }
        if (statusColumn != null) {
            addColumn(statusColumn, "Status");
        }
    }

    protected TextColumn<FighterInfo> createAuthorizationColumn() {
        authorizationColumn = new TextColumn<FighterInfo>() {
            @Override
            public String getValue(FighterInfo fli) {
                return fli.getAuthorizations();
            }
        };
        return authorizationColumn;
    }

    protected void createDataProvider() {
        aDataProvider = new AsyncDataProviderImpl();
        aDataProvider.addDataDisplay(this);
    }

    protected TextColumn<FighterInfo> createGroupColumn() {
        groupColumn = new TextColumn<FighterInfo>() {
            @Override
            public String getValue(FighterInfo fli) {
                return fli.getGroup();
            }
        };
        groupColumn.setSortable(false);
        return groupColumn;
    }

    protected Column<FighterInfo, String> createImageColumn() {
        ImageCell imageCell = new ImageCell();

        imageColumn = new Column<FighterInfo, String>(imageCell) {
            @Override
            public String getValue(FighterInfo fighter) {
                if (fighter != null && fighter.getRole() != null) {
                    if (fighter.getRole().equals(UserRoles.USER.toString())) {
                        return "/images/authorizedFighter.png";
                    } else if (fighter.getRole().equals(UserRoles.MARSHAL_OF_THE_FIELD.toString())) {
                        return "/images/warrantedMarshal.png";
                    } else if (fighter.getRole().equals(UserRoles.CUT_AND_THRUST_MARSHAL.toString())) {
                        return "/images/falconCutAndThrustMarshal.png";
                    } else if (fighter.getRole().equals(UserRoles.KNIGHTS_MARSHAL.toString())) {
                        return "/images/knightsMarshal.png";
                    } else if (fighter.getRole().equals(UserRoles.DEPUTY_EARL_MARSHAL.toString())) {
                        return "/images/regionalDeputy.png";
                    } else if (fighter.getRole().equals(UserRoles.CARD_MARSHAL.toString())) {
                        return "/images/warrantedMarshal.png";
                    } else if (fighter.getRole().equals(UserRoles.EARL_MARSHAL.toString())) {
                        return "/images/earlMarshal.png";
                    }
                }
                return "/images/authorizedFighter.png";
            }
        };
        imageColumn.setSortable(false);
        return imageColumn;
    }

    protected void createPager() {
        final SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
        pager = new SimplePager(SimplePager.TextLocation.CENTER, pagerResources, false, 0, true);
        pager.setDisplay(this);
    }

    protected TextColumn<FighterInfo> createScaNameColumn() {
        scaNameColumn = new TextColumn<FighterInfo>() {
            @Override
            public String getValue(FighterInfo fli) {
                return fli.getScaName();
            }
        };
        scaNameColumn.setSortable(false);
        return scaNameColumn;
    }

    protected Column<FighterInfo, String> createSelectColumn() {
        ButtonCell selectButton = new ButtonCell();
        selectColumn = new Column<FighterInfo, String>(selectButton) {
            @Override
            public String getValue(FighterInfo fighter) {
                if (security.canView(fighter)) {
                    return "Select";
                } else {
                    return "...";
                }
            }
        };
        selectColumn.setSortable(false);
        selectColumn.setFieldUpdater(new FieldUpdater<FighterInfo, String>() {
            @Override
            public void update(int index, FighterInfo fighter, String value) {
                if (!security.canView(fighter)) {
                    Shout.getInstance().tell("You do not have rights to update this record");
                }
            }
        });
        return selectColumn;
    }

    protected void createSelectionModel() {
        final SingleSelectionModel<FighterInfo> selectionModel = new SingleSelectionModel<>();
        setSelectionModel(selectionModel);

        selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                FighterInfo selected = selectionModel.getSelectedObject();
                if (selected != null) {
                    if (security.canView(selected)) {
                        FighterServiceAsync fighterService = GWT.create(FighterService.class);

                        fighterService.getFighter(selected.getFighterId(), new AsyncCallback<Fighter>() {
                            @Override
                            public void onFailure(Throwable caught) {
                                log.log(Level.SEVERE, "getFighter {0}", caught);
                                shout.defaultError();
                            }

                            @Override
                            public void onSuccess(Fighter result) {
                                fireEvent(new EditViewEvent(Mode.VIEW, result));
                            }
                        });
                    }
                }
            }
        });
    }

    protected void createStatusColumn() {
        if (security.isRoleOrGreater(UserRoles.KNIGHTS_MARSHAL)) {
            statusColumn = new TextColumn<FighterInfo>() {
                @Override
                public String getValue(FighterInfo fli) {
                    return fli.getStatus();
                }
            };
            statusColumn.setSortable(false);

        }
    }

    private class AsyncDataProviderImpl extends AsyncDataProvider<FighterInfo> {

        private String cursor = null;
        private DataProviderType dataProviderType;
        private ScaGroup group;
        private int prevStart = 0;
        private String searchName;

        public AsyncDataProviderImpl() {
            this.dataProviderType = DataProviderType.FIGHTER;
            this.group = null;
        }

        @Override
        protected void onRangeChanged(final HasData<FighterInfo> display) {
            shout.hide();
            shout.tell("Please Wait, searching for records....", false);
            int dispStart = display.getVisibleRange().getStart();
            int dispLength = display.getVisibleRange().getLength();

            int prevPageStart = dispStart - dispLength;
            prevPageStart = prevPageStart < 0 ? 0 : dispStart;
            if (prevStart >= dispStart) {
                cursor = null;
            }
            if (dispStart >= getRowCount() - dispLength) {
                cursor = null;
                prevPageStart = getRowCount() - dispLength;
            }
            final FighterServiceAsync fighterService = GWT.create(FighterService.class);
            switch (dataProviderType) {
                case FIGHTER:
                    fighterService.getFighters(cursor, dispLength, prevPageStart,
                            new AsyncCallback<FighterListResultWrapper>() {

                        @Override
                        public void onFailure(Throwable caught) {
                            shout.hide();
                            log.log(Level.SEVERE, "loadAll:", caught);
                            shout.defaultError();
                        }

                        @Override
                        public void onSuccess(FighterListResultWrapper result) {
                            final Range range = display.getVisibleRange();
                            int start = range.getStart();
                            prevStart = start;
                            log.log(Level.INFO, "getFighters set count to " + result.getCount());
                            updateRowCount(result.getCount(), true);
                            updateRowData(start, result.getFighters().getFighterInfo());
                            cursor = result.getCursor();
                            shout.hide();
                        }

                    });
                    break;
                case GROUP:
                    fighterService.getFightersByGroup(group, cursor, dispLength, prevPageStart,
                            new AsyncCallback<FighterListResultWrapper>() {

                        @Override
                        public void onFailure(Throwable caught) {
                            shout.hide();
                            log.log(Level.SEVERE, "loadGroup:", caught);
                            shout.defaultError();
                        }

                        @Override
                        public void onSuccess(FighterListResultWrapper result) {
                            final Range range = display.getVisibleRange();
                            int start = range.getStart();
                            prevStart = start;
                            log.log(Level.INFO, "getFightersByGroup set count to " + result.getCount());
                            updateRowCount(result.getCount(), true);
                            updateRowData(start, result.getFighters().getFighterInfo());
                            cursor = result.getCursor();
                            shout.hide();
                        }

                    });
                    break;
                case SEARCH:
                    fighterService.searchFighters(searchName, new AsyncCallback<FighterListInfo>() {

                        @Override
                        public void onFailure(Throwable caught) {
                            shout.hide();
                            log.log(Level.SEVERE, "searchFighters:", caught);
                            shout.defaultError();
                        }

                        @Override
                        public void onSuccess(FighterListInfo result) {
                            List<FighterInfo> fil = result.getFighterInfo();
                            final Range range = display.getVisibleRange();
                            int start = range.getStart();
                            prevStart = start;
                            log.log(Level.INFO, "searchFighters set count to " + fil.size());
                            updateRowCount(fil.size(), true);
                            updateRowData(start, fil);
                        }
                    });
                    break;
                default:
            }
        }

        protected void setDataProviderType(DataProviderType dataProviderType) {
            this.dataProviderType = dataProviderType;
        }

        protected void setGroup(ScaGroup group) {
            this.group = group;
        }

        protected void setSearchName(String searchName) {
            this.searchName = searchName;
        }

    }

    protected enum DataProviderType {

        FIGHTER, GROUP, SEARCH
    }
}
