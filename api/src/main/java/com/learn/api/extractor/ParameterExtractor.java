package com.learn.api.extractor;

import com.learn.api.elements.ParameterElement;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;

public class ParameterExtractor {

    private ParameterExtractor() {

    }

    public static ParameterElement extract(Parameter parameter, Annotation[] annotations) {
        ParameterElement parameterElement = new ParameterElement(parameter.getName(), parameter.getType().getName());
        if(annotations != null && annotations.length > 0)
            parameterElement.setAnnotations(AnnotationExtractor.extractAnnotations(annotations));
        return parameterElement;
    }
}
