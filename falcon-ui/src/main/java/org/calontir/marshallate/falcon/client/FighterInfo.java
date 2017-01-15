package org.calontir.marshallate.falcon.client;

import java.io.Serializable;

/**
 *
 * @author rikscarborough
 */
public class FighterInfo implements Serializable {

    private Long fighterId;
    private String scaName;
    private String authorizations;
    private String group;
    private String status;
    private Boolean minor;
    private String role;
    private Boolean support;

    public String getScaName() {
        return scaName;
    }

    public Boolean getSupport() {
        return support;
    }

    public void setScaName(String scaName) {
        this.scaName = scaName;
    }

    public String getAuthorizations() {
        return authorizations;
    }

    public void setAuthorizations(String authorizations) {
        this.authorizations = authorizations;
    }

    public Long getFighterId() {
        return fighterId;
    }

    public void setFighterId(Long fighterId) {
        this.fighterId = fighterId;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isMinor() {
        return minor.booleanValue();
    }

    public Boolean getMinor() {
        return minor;
    }

    public void setMinor(Boolean minor) {
        this.minor = minor;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setSupport(Boolean support) {
        this.support = support;
    }

    @Override
    public String toString() {
        return "FighterInfo{" + "fighterId=" + fighterId + ", scaName=" + scaName + ", authorizations=" + authorizations + ", group=" + group + ", status=" + status + ", minor=" + minor + ", role=" + role + ", support=" + support + '}';
    }

}
