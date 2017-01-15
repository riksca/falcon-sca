package org.calontir.marshallate.falcon.dto;

import java.io.Serializable;
import java.util.List;
import org.calontir.marshallate.falcon.common.FighterStatus;
import org.calontir.marshallate.falcon.common.UserRoles;

/**
 *
 * @author rik
 */
public class Fighter implements Serializable {

    private List<Address> address;
    private List<Authorization> authorization;
    private String dateOfBirth;
    private List<Email> email;

    private Long fighterId;
    private String googleId;
    private String membershipExpires;
    private String modernName;
    private Note note;
    private List<Phone> phone;
    private UserRoles role;
    private ScaGroup scaGroup;
    private String scaMemberNo;
    private String scaName;
    private FighterStatus status = FighterStatus.ACTIVE;
    private Boolean support;

    private Treaty treaty;

    public List<Address> getAddress() {
        return address;
    }

    public List<Authorization> getAuthorization() {
        return authorization;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public List<Email> getEmail() {
        return email;
    }

    public Long getFighterId() {
        return fighterId;
    }

    public String getGoogleId() {
        return googleId;
    }

    public String getMembershipExpires() {
        return membershipExpires;
    }

    public String getModernName() {
        return modernName;
    }

    public Note getNote() {
        return note;
    }

    public List<Phone> getPhone() {
        return phone;
    }

    public Address getPrimeAddress() {
        if (address == null || address.isEmpty()) {
            return null;
        }
        return address.get(0);
    }

    public Email getPrimeEmail() {
        if (email == null || email.isEmpty()) {
            return null;
        }
        return email.get(0);
    }

    public Phone getPrimePhone() {
        if (phone == null || phone.isEmpty()) {
            return null;
        }
        return phone.get(0);
    }

    public UserRoles getRole() {
        return role;
    }

    public ScaGroup getScaGroup() {
        return scaGroup;
    }

    public String getScaMemberNo() {
        return scaMemberNo;
    }

    public String getScaName() {
        return scaName;
    }

    public FighterStatus getStatus() {
        return status;
    }

    public Boolean getSupport() {
        return support;
    }

    public Treaty getTreaty() {
        return treaty;
    }

    public void setAddress(List<Address> address) {
        this.address = address;
    }

    public void setAuthorization(List<Authorization> authorization) {
        this.authorization = authorization;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setEmail(List<Email> email) {
        this.email = email;
    }

    public void setFighterId(Long fighterId) {
        this.fighterId = fighterId;
    }

    public void setGoogleId(String googleId) {
        this.googleId = googleId;
    }

    public void setMembershipExpires(String membershipExpires) {
        this.membershipExpires = membershipExpires;
    }

    public void setModernName(String modernName) {
        this.modernName = modernName;
    }

    public void setNote(Note note) {
        this.note = note;
    }

    public void setPhone(List<Phone> phone) {
        this.phone = phone;
    }

    public void setRole(UserRoles role) {
        this.role = role;
    }

    public void setScaGroup(ScaGroup scaGroup) {
        this.scaGroup = scaGroup;
    }

    public void setScaMemberNo(String scaMemberNo) {
        this.scaMemberNo = scaMemberNo;
    }

    public void setScaName(String scaName) {
        this.scaName = scaName;
    }

    public void setStatus(FighterStatus status) {
        this.status = status;
    }

    public void setSupport(Boolean support) {
        this.support = support;
    }

    public void setTreaty(Treaty treaty) {
        this.treaty = treaty;
    }

    @Override
    public String toString() {
        return "Fighter{" + "address=" + address + ", authorization=" + authorization + ", dateOfBirth=" + dateOfBirth + ", email=" + email + ", fighterId=" + fighterId + ", googleId=" + googleId + ", membershipExpires=" + membershipExpires + ", modernName=" + modernName + ", note=" + note + ", phone=" + phone + ", role=" + role + ", scaGroup=" + scaGroup + ", scaMemberNo=" + scaMemberNo + ", scaName=" + scaName + ", status=" + status + ", support=" + support + ", treaty=" + treaty + '}';
    }

}
