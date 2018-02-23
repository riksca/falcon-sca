/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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

	protected String cleanString(String target) {
		if (target == null) {
			return "";
		}
		String changed = target.trim();
		changed = changed.replaceAll(",$", "");
		changed = changed.trim();

		return changed;
	}
	
}
