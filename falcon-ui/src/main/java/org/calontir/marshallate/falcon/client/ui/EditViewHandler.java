package org.calontir.marshallate.falcon.client.ui;

import com.google.gwt.event.shared.EventHandler;
import org.calontir.marshallate.falcon.dto.Fighter;

/**
 *
 * @author rikscarborough
 */
public interface EditViewHandler extends EventHandler {
//    public void buildEdit();
    
    public void buildView();
    
    public void buildAdd();

    public void setFighter(Fighter fighter);
}
