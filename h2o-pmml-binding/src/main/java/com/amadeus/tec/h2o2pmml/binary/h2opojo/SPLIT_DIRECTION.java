package com.amadeus.tec.h2o2pmml.binary.h2opojo;

public enum SPLIT_DIRECTION {
	
	SPLIT_ON_NAN_TEST_NAN_RIGHT("splitOnNaNTestNaNRight"),
    SPLIT_ON_OPERATION_TEST_NAN_LEFT("splitOnOperationTestNaNLeft"),
	SPLIT_ON_OPERATION_TEST_NAN_RIGHT("splitOnOperationTestNaNRight");
    
	private final String value;

	SPLIT_DIRECTION(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static SPLIT_DIRECTION fromValue(String v) {
        for (SPLIT_DIRECTION c: SPLIT_DIRECTION.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
