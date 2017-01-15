package org.calontir.marshallate.falcon.client.ui.fighterform;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Label;
import org.calontir.marshallate.falcon.common.UserRoles;
import org.calontir.marshallate.falcon.dto.Fighter;

/**
 *
 * @author rik
 */
public class SupportField extends AbstractFieldWidget {

    public SupportField(final Fighter fighter, final boolean edit) {
        if (edit && security.isRoleOrGreater(UserRoles.CARD_MARSHAL)) {
            final CheckBox support = new CheckBox("Support");
            support.setName("support");
            support.setValue(fighter.getSupport() == null ? false : fighter.getSupport());
            support.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
                @Override
                public void onValueChange(ValueChangeEvent<Boolean> event) {
                    fighter.setSupport(event.getValue());
                }
            });
            initWidget(support);
        } else if (fighter.getSupport() == null ? false : fighter.getSupport()) {
            initWidget(new Label("Support Yes"));
        } else {
            initWidget(new Label("Support No"));
        }
    }

}
