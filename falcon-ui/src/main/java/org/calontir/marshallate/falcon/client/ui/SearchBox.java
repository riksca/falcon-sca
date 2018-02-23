package org.calontir.marshallate.falcon.client.ui;

import com.google.gwt.user.client.ui.TextBox;
import org.calontir.marshallate.falcon.client.ui.fighterform.AbstractFieldWidget;

/**
 *
 * @author rikscarborough
 */
public class SearchBox extends AbstractFieldWidget {

    final TextBox search = new TextBox();

    public SearchBox() {
        search.getElement().setAttribute("id", "search");
        search.getElement().setAttribute("autocomplete", "off");
        initWidget(search);
    }

    public String getText() {
        return search.getText();
    }

}
