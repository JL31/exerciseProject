package com.example.demo.tools;

import java.util.Map;

public class RequestValidation {
    public boolean requestValidity;
    public Map<String, String> invalidParameters;

    public RequestValidation(boolean requestValidity, Map<String, String> invalidParameters) {
        this.requestValidity = requestValidity;
        this.invalidParameters = invalidParameters;
    }

    public boolean getRequestValidity() {
        return requestValidity;
    }

    public Map<String, String> getInvalidParameters() {
        return invalidParameters;
    }
}
