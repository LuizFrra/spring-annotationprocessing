package com.learn.annotationprocessing.builder;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class PointcutsBuilder {
    private static PointcutsBuilder pointcutsBuilder;

    private final Set<String> annotationsPointcut = new LinkedHashSet<>();

    private final TypeSpec.Builder classBuilder;

    private String basePackage;

    private final String className = "CommonPointcuts";

    private final Map<String, String> annotationToPointcut = new HashMap<>();

    private boolean isBuilt = false;

    private PointcutsBuilder() {
        classBuilder = TypeSpec
                .classBuilder(className)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Aspect.class);
    }

    public static PointcutsBuilder getInstance(String annotationProcessingBasePackage) {
        if(pointcutsBuilder == null) {
            pointcutsBuilder = new PointcutsBuilder();
            pointcutsBuilder.basePackage = annotationProcessingBasePackage;
        }
        return pointcutsBuilder;
    }

    public void addPointcut(String annotationCanonicalName) {
        if(annotationsPointcut.contains(annotationCanonicalName)) return;
        classBuilder.addMethod(buildMethodPointcut(annotationCanonicalName));
        annotationsPointcut.add(annotationCanonicalName);
    }

    private MethodSpec buildMethodPointcut(String annotationCanonicalName) {
        String annotationName = Arrays.stream(annotationCanonicalName.split("\\.")).reduce((first, second) -> second).orElseThrow();
        String methodName = annotationName.toLowerCase(Locale.ROOT);
        String methodFullPackage = basePackage + "." + className + "." + methodName + "()";
        annotationToPointcut.putIfAbsent(annotationName, methodFullPackage);
        return MethodSpec
                .methodBuilder(methodName)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(buildPointcutAnnotation(annotationCanonicalName))
                .build();
    }

    private AnnotationSpec buildPointcutAnnotation(String annotationCanonicalName) {
        String value = "\"@annotation(" + annotationCanonicalName + ")\"";
        return AnnotationSpec.builder(Pointcut.class)
                .addMember("value", value)
                .build();
    }

    public void build(Filer filer) {
        if(!isBuilt) {
            TypeSpec commonPointcut = classBuilder.build();
            JavaFile javaFile = JavaFile.builder(basePackage, commonPointcut).build();
            try {
                javaFile.writeTo(filer);
            } catch (IOException e) {
                e.printStackTrace();
            }
            isBuilt = true;
        }
    }

    public Map<String, String> getAnnotationToPointcut() {
        return annotationToPointcut;
    }
}
