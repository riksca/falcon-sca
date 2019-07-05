package org.calontir.marshallate.falcon.client.ui.fighterform;

import com.google.gwt.user.client.ui.Composite;
import org.calontir.marshallate.falcon.client.user.Security;
import org.calontir.marshallate.falcon.client.user.SecurityFactory;

/**
 *
 * @author rikscarborough
 */
public class AbstractFieldWidget extends Composite {
	final protected Security security = SecurityFactory.getSecurity();
	private boolean required = true;
	private boolean valid = true;

	protected String cleanString(String target) {
		if (target == null) {
			return "";
		}
		String changed = target.trim();
		changed = changed.replaceAll(",$", "");
		changed = changed.trim();

		return changed;
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}
}
