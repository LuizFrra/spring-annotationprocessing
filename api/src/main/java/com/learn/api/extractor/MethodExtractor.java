package com.learn.api.extractor;

import com.learn.api.elements.MethodElement;
import com.learn.api.elements.ParameterElement;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.LinkedHashSet;
import java.util.Set;

public class MethodExtractor {
    private MethodExtractor() {

    }

    public static MethodElement extract(Method method) {
        MethodElement methodElement = new MethodElement(method);
        methodElement.setAnnotations(AnnotationExtractor.extractAnnotations(method.getDeclaredAnnotations()));
        methodElement.setParameters(extractParameters(method));
        return methodElement;
    }

    private static Set<ParameterElement> extractParameters(Method method) {
        Parameter[] parameters = method.getParameters();
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        Set<ParameterElement> annotationElements = new LinkedHashSet<>();
        if(parameterAnnotations.length == parameters.length) {
            for(int count = 0; count < parameters.length; count++) {
                Parameter parameter = parameters[count];
                Annotation[] parameterAnnotation = parameterAnnotations[count];
                annotationElements.add(ParameterExtractor.extract(parameter, parameterAnnotation));
            }
        }
        return annotationElements;
    }
}
