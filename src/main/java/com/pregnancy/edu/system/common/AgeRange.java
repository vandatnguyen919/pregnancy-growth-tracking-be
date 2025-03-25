package com.pregnancy.edu.system.common;
import lombok.Getter;

@Getter
public enum AgeRange {
    AGE_18_24("18-24"),
    AGE_25_34("25-34"),
    AGE_35_44("35-44"),
    AGE_45_54("45-54"),
    AGE_55_64("55-64"),
    AGE_65_PLUS("65+");

    private final String displayName;

    AgeRange(String displayName) {
        this.displayName = displayName;
    }
}
