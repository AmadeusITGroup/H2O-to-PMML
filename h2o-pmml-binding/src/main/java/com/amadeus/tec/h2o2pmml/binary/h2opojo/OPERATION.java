package com.amadeus.tec.h2o2pmml.binary.h2opojo;

public enum OPERATION {
	
    FLOAT_GREATER_OR_EQUAL("floatGreaterOrEqual"),
    INCLUSION_SMALL_SET("inclusionSmallSet"),
	INCLUSION_BIG_SET("inclusionBigSet");
    
	private final String value;

	OPERATION(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static OPERATION fromValue(String v) {
        for (OPERATION c: OPERATION.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
