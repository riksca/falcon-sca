package org.calontir.marshallate.falcon.common;

import java.io.Serializable;

/**
 *
 * @author rikscarborough
 */
public enum ReportingMarshalType implements Serializable {

    ARMORED_COMBAT("AC", "Armored Combat"), CALON_STEEL("CS", "Calon Steel"), EQUESTRIAN("EQ", "Equestrian");

    private final String code;
    private final String value;

    ReportingMarshalType(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public String getCode() {
        return code;
    }

    public static ReportingMarshalType getByCode(String code) {
        for (ReportingMarshalType rmt : ReportingMarshalType.values()) {
            if (rmt.getCode().equals(code)) {
                return rmt;
            }
        }
        return null;
    }

}
