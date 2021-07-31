package com.learn.annotationprocessing.builder;

import com.learn.annotationprocessing.advices.TraceAdviceModel;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import java.io.IOException;

public class TraceAdviceBuilder implements AdviceBuilder {
    private static TraceAdviceBuilder traceAdviceBuilder;

    private TypeSpec.Builder classBuilder;

    private String basePackage;

    private final String className = "TraceAdvice";

    private boolean isBuilt = false;

    private String methodFullPackage;

    private TraceAdviceBuilder() {
        classBuilder = TypeSpec
                .classBuilder(className)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Aspect.class)
                .addAnnotation(Component.class);
    }

    public static TraceAdviceBuilder getInstance(String annotationProcessingBasePackage, String methodFullPackage) {
        if(traceAdviceBuilder == null) {
            traceAdviceBuilder = new TraceAdviceBuilder();
            traceAdviceBuilder.basePackage = annotationProcessingBasePackage;
            traceAdviceBuilder.methodFullPackage = methodFullPackage;
        }
        return traceAdviceBuilder;
    }

    public void build(Filer filer) {
        if(!isBuilt) {
            classBuilder
                    .superclass(TraceAdviceModel.class)
                    .addMethod(overrideTrackMethod());
            TypeSpec traceAdvice = classBuilder.build();
            JavaFile javaFile = JavaFile.builder(basePackage, traceAdvice).build();
            try {
                javaFile.writeTo(filer);
            } catch (IOException e) {
                e.printStackTrace();
            }
            isBuilt = true;
        }
    }

    private MethodSpec overrideTrackMethod() {
        return MethodSpec
                .methodBuilder("track")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addAnnotation(buildAroundAnnotation())
                .addParameter(ProceedingJoinPoint.class, "pjp")
                .returns(Object.class)
                .addStatement("return super.track(pjp)")
                .build();

    }

    private AnnotationSpec buildAroundAnnotation() {
        return AnnotationSpec.builder(Around.class)
                .addMember("value", "\"" + methodFullPackage + "\"")
                .build();
    }
}
