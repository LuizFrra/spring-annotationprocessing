package com.learn.api.elements;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class MethodElement extends Element {

    Set<AnnotationElement> annotations;

    Set<ParameterElement> parameters;

    private String returnType;

    public MethodElement(Method method) {
        this.key = buildKey(method);
        annotations = new LinkedHashSet<>();
        parameters = new LinkedHashSet<>();
        returnType = method.getReturnType().getName();
    }

    private String buildKey(Method method) {
        return Integer.toString(method.hashCode());
    }

    public void setAnnotations(Set<AnnotationElement> annotations) {
        this.annotations.addAll(annotations);
    }

    public void setParameters(Set<ParameterElement> parameters) { this.parameters.addAll(parameters); }

    public boolean containsAnnotation(Class<? extends Annotation> annotation) {
        return annotations.stream().anyMatch(a -> a.getKey().equals(annotation.toString()));
    }

    public boolean containsParameter(String parameterName) {
        return parameters.stream().anyMatch(p -> p.getName().equals(parameterName));
    }

    public boolean containsParameter(String parameterName, Class<?> type) {
        return parameters.stream().anyMatch(p -> p.getName().equals(parameterName) && p.getType().equals(type.getName()));
    }

    public Set<ParameterElement> getParametersWithAnnotation(Class<? extends Annotation> annotation) {
        return parameters.stream().filter(p -> p.containsAnnotation(annotation)).collect(Collectors.toSet());
    }

    public String getReturnType() {
        return returnType;
    }
}
