package org.calontir.marshallate.falcon.user;

import com.google.appengine.api.users.User;
import org.calontir.marshallate.falcon.common.UserRoles;
import org.calontir.marshallate.falcon.db.FighterDAO;
import org.calontir.marshallate.falcon.dto.Fighter;

/**
 *
 * @author rik
 */
public class Security {

    private User aeUser;
    private Fighter user;

    protected Security() {
    }

    public boolean isRole(UserRoles userRole) {
        return user == null ? false : user.getRole().equals(userRole);
    }

    public boolean isRoleOrGreater(UserRoles userRole) {
        if (user == null) {
            return false;
        }
        if (user.getSupport() != null && user.getSupport()) {
            return true; // support has full rights.
        }
        return user.getRole().ordinal() >= userRole.ordinal();
    }

    public UserRoles getUserRole() {
        return user == null ? null : user.getRole();
    }

    public User getAeUser() {
        return aeUser;
    }

    protected void setAeUser(User aeUser) {
        this.aeUser = aeUser;
    }

    public Fighter getUser() {
        return user;
    }

    protected void setUser(Fighter user) {
        this.user = user;
    }

    public boolean canEdit(Long fighterId, String target) {
        if ("Authorizations".equals(target)) {
            return canEditAuthorizations(fighterId);
        }

        return canEditFighter(fighterId);
    }

    public boolean canEditAuthorizations(Long fighterId) {
        if (user == null) {
            return false;
        }

        if (isRoleOrGreater(UserRoles.CARD_MARSHAL)) {
            return true;
        }

        return false;
    }

    public boolean canEditFighter(Long fighterId) {
        if (user == null) {
            return false;
        }

        if (isRoleOrGreater(UserRoles.CARD_MARSHAL)) {
            return true;
        }

        if (user.getFighterId() != null && user.getFighterId().longValue() == fighterId.longValue()) {
            return true;
        }

        FighterDAO fighterDao = new FighterDAO();
        Fighter fighter = fighterDao.getFighter(fighterId);

        if (isRole(UserRoles.KNIGHTS_MARSHAL)) {
            if (user.getScaGroup() != null && fighter.getScaGroup() != null) {
                if (user.getScaGroup().getGroupName().equals(fighter.getScaGroup().getGroupName())) {
                    return true;
                }
            }
        }

        if (isRole(UserRoles.DEPUTY_EARL_MARSHAL)) {
            if (user.getScaGroup() != null && fighter.getScaGroup() != null) {
                if (user.getScaGroup().getGroupLocation().equals(fighter.getScaGroup().getGroupLocation())) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean canPrintFighter(Long fighterId) {
        if (user == null) {
            return false;
        }

        if (isRoleOrGreater(UserRoles.CARD_MARSHAL)) {
            return true;
        }

        if (user.getFighterId() != null && user.getFighterId().longValue() == fighterId.longValue()) {
            return true;
        }

        FighterDAO fighterDao = new FighterDAO();
        Fighter fighter = fighterDao.getFighter(fighterId);

        if (isRole(UserRoles.KNIGHTS_MARSHAL)) {
            if (user.getScaGroup() != null && fighter.getScaGroup() != null) {
                if (user.getScaGroup().getGroupName().equals(fighter.getScaGroup().getGroupName())) {
                    return true;
                }
            }
        }

        if (isRole(UserRoles.DEPUTY_EARL_MARSHAL)) {
            if (user.getScaGroup() != null && fighter.getScaGroup() != null) {
                if (user.getScaGroup().getGroupLocation().equals(fighter.getScaGroup().getGroupLocation())) {
                    return true;
                }
            }
        }

        return false;
    }
}
