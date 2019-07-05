/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.calontir.marshallate.falcon.client.ui.fighterform;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.datepicker.client.DateBox;
import java.util.Date;
import org.calontir.marshallate.falcon.client.ui.Shout;
import org.calontir.marshallate.falcon.dto.Fighter;

/**
 *
 * @author rikscarborough
 */
public class MembershipExpiresField extends AbstractFieldWidget {

	public MembershipExpiresField(final Fighter fighter, final boolean edit) {
		init(fighter, edit, false);
	}

	public MembershipExpiresField(final Fighter fighter, final boolean edit, final boolean required) {
		init(fighter, edit, required);
	}

	private void init(Fighter fighter, boolean edit, boolean required) {
		setRequired(required);
		if (edit) {
			final DateBox membershipExpires = new DateBox();
			membershipExpires.getTextBox().getElement().setId("membershipExpires");
			membershipExpires.getTextBox().setName("membershipExpires");
			membershipExpires.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getFormat("MM/dd/yyyy")));
			membershipExpires.setStyleName("membershipExpires");
			if (fighter.getMembershipExpires() != null) {
				membershipExpires.setValue(DateTimeFormat.getFormat("MM/dd/yyyy").parse(fighter.getMembershipExpires()));
			}
			membershipExpires.addValueChangeHandler(new ValueChangeHandler<Date>() {
				@Override
				public void onValueChange(ValueChangeEvent<Date> event) {
					if (membershipExpires.getTextBox().getValue().isEmpty()) {
						membershipExpires.setFocus(true);
						Shout.getInstance().tell("Membership Expiration Date is required");
						fighter.setMembershipExpires(null);
						if (isRequired()) {
							setValid(false);
						}
					} else {
						fighter.setMembershipExpires(membershipExpires.getTextBox().getValue());
						setValid(true);
					}
				}
			});
			membershipExpires.getTextBox().addBlurHandler(new BlurHandler() {
				@Override
				public void onBlur(BlurEvent event) {
					if (membershipExpires.getTextBox().getValue().isEmpty() && fighter.getMembershipExpires() != null) {
						membershipExpires.setFocus(true);
						Shout.getInstance().tell("Membership Expiration Date is required");
						fighter.setMembershipExpires(null);
						membershipExpires.setValue(null, false);
						fighter.setMembershipExpires(null);
						if (isRequired()) {
							setValid(false);
						}
					} else {
						setValid(true);
					}
				}
			});
			initWidget(membershipExpires);
		} else if (fighter.getMembershipExpires() != null) {
			initWidget(new Label(fighter.getMembershipExpires()));
		} else {
			initWidget(new Label(""));
		}
	}

}
