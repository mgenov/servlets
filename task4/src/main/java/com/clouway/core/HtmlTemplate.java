package com.clouway.core;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Borislav Gadjev <gadjevb@gmail.com>
 */
public class HtmlTemplate implements Template {
    private final Map<String, String> placeHolderToValue = new LinkedHashMap<>();
    private String templateValue;

    public HtmlTemplate(String templateValue) {
        this.templateValue = templateValue;
    }

    public void put(String placeHolder, String value) {
        placeHolderToValue.put(placeHolder, value);
    }

    public String evaluate() {
        String evaluationResult = templateValue;
        for (String placeHolder : placeHolderToValue.keySet()) {
            evaluationResult = evaluationResult.replaceAll("\\$\\{" + placeHolder + "\\}", placeHolderToValue.get(placeHolder));
        }

        return evaluationResult;
    }
}