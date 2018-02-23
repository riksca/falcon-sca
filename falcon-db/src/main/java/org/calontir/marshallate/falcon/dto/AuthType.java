package org.calontir.marshallate.falcon.dto;

import java.io.Serializable;

/**
 *
 * @author rik
 */
public class AuthType implements Serializable {

    private Long authTypeId;
    private String code;
    private String description;
    private Long orderValue;

    public Long getAuthTypeId() {
        return authTypeId;
    }

    public void setAuthTypeId(Long authTypeId) {
        this.authTypeId = authTypeId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getOrderValue() {
        return orderValue;
    }

    public void setOrderValue(Long orderValue) {
        this.orderValue = orderValue;
    }
}
