package org.calontir.marshallate.falcon.user;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import org.calontir.marshallate.falcon.db.FighterDAO;
import org.calontir.marshallate.falcon.dto.Fighter;

/**
 *
 * @author rik
 */
public class SecurityFactory {

    private SecurityFactory() {
    }

    public static Security getSecurity() {
        Security security = new Security();
        UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();
        if (user != null) {
            security.setAeUser(user);

            FighterDAO fighterDao = new FighterDAO();
            Fighter fighter = fighterDao.getFighterByGoogleId(user.getEmail());
            security.setUser(fighter);
        }

        return security;
    }
}
