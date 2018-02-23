package org.calontir.marshallate.falcon.client;

import java.io.Serializable;
import org.calontir.marshallate.falcon.common.UserRoles;

/**
 *
 * @author rikscarborough
 */
public class LoginInfo implements Serializable {

    private String emailAddress;
    private long fighterId;
    private String group;
    private boolean loggedIn = false;
    private String loginUrl;
    private String logoutUrl;
    private String nickname;
    private String scaName;
    private Boolean support;
    private UserRoles userRole;

    public String getEmailAddress() {
        return emailAddress;
    }

    public long getFighterId() {
        return fighterId;
    }

    public String getGroup() {
        return group;
    }

    public String getLoginUrl() {
        return loginUrl;
    }

    public String getLogoutUrl() {
        return logoutUrl;
    }

    public String getNickname() {
        return nickname;
    }

    public String getScaName() {
        return scaName;
    }

    public Boolean getSupport() {
        return support;
    }

    public UserRoles getUserRole() {
        return userRole;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public boolean isUser() {
        return isLoggedIn() && getScaName() != null && !getScaName().trim().isEmpty();
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public void setFighterId(long fighterId) {
        this.fighterId = fighterId;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }

    public void setLogoutUrl(String logoutUrl) {
        this.logoutUrl = logoutUrl;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setScaName(String scaName) {
        this.scaName = scaName;
    }

    public void setSupport(Boolean support) {
        this.support = support;
    }

    public void setUserRole(UserRoles userRole) {
        this.userRole = userRole;
    }

}
