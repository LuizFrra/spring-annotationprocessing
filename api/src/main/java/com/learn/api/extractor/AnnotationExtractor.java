package com.learn.api.extractor;

import com.learn.api.elements.AnnotationElement;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class AnnotationExtractor {
    private AnnotationExtractor() {

    }

    public static AnnotationElement extract(Annotation annotation) {
        AnnotationElement annotationElement = new AnnotationElement(annotation.annotationType().toString());
        annotationElement.addMembers(extractMembers(annotation));
        return annotationElement;
    }

    public static Set<AnnotationElement> extractAnnotations(Annotation[] annotations) {
        return Arrays.stream(annotations).map(AnnotationExtractor::extract).collect(Collectors.toSet());
    }

    private static Map<String, Object> extractMembers(Annotation annotation) {
        Map<String, Object> members = new HashMap<>();
        for(Method method : annotation.annotationType().getDeclaredMethods()) {
            String methodName = method.getName();
            try {
                Object value = method.invoke(annotation, (Object[]) null);
                members.put(methodName, value);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return members;
    }


}
