/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.calontir.marshallate.falcon.client.ui;

import com.google.gwt.event.shared.EventHandler;
import org.calontir.marshallate.falcon.dto.Fighter;

/**
 *
 * @author rikscarborough
 */
public interface DataUpdatedEventHandler extends EventHandler {

	public void fighterUpdated(Fighter fighter);
	public void fighterAdded();
	
}
