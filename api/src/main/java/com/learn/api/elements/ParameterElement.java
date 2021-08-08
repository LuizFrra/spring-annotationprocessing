package com.learn.api.elements;

import java.lang.annotation.Annotation;
import java.util.LinkedHashSet;
import java.util.Set;

public class ParameterElement extends Element {

    private Set<AnnotationElement> annotations;

    private String name;

    private String type;

    public ParameterElement(String name, String type) {
        this.key = type + "#" + name;
        this.name = name;
        this.type = type;
        annotations = new LinkedHashSet<>();
    }

    public void setAnnotations(Set<AnnotationElement> annotations) {
        this.annotations.addAll(annotations);
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public boolean containsAnnotation(Class<? extends Annotation> annotation) {
        return annotations.stream().anyMatch(a -> a.getKey().equals(annotation.toString()));
    }
}
