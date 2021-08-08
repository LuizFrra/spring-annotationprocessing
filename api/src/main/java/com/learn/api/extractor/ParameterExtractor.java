package com.learn.api.extractor;

import com.learn.api.elements.AnnotationElement;
import com.learn.api.elements.ParameterElement;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

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
