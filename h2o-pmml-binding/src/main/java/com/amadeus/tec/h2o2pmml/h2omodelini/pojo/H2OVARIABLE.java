package com.amadeus.tec.h2o2pmml.h2omodelini.pojo;

public enum H2OVARIABLE {

	CATEGORICAL_VARIABLE("categoricalVariable"),
    CONTINUOUS_VARIABLE("continuousVariable");
    
	private final String value;

	H2OVARIABLE(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static H2OVARIABLE fromValue(String v) {
        for (H2OVARIABLE c: H2OVARIABLE.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
}
