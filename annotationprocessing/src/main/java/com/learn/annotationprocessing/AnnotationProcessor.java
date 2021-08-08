package com.learn.annotationprocessing;

import com.google.auto.service.AutoService;
import com.learn.annotationprocessing.advices.AdviceFactory;
import com.learn.annotationprocessing.annotations.Proxy;
import com.learn.annotationprocessing.annotations.Trace;
import com.learn.annotationprocessing.builder.AdviceBuilder;
import com.learn.annotationprocessing.builder.PointcutsBuilder;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.util.Set;

@AutoService(Processor.class)
public class AnnotationProcessor extends AbstractProcessor {

    private static String annotationProcessingBasePackage = "";

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnvironment) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.WARNING, "Executing Annotation Processing");
        buildAnnotationProcessingRootPackage(roundEnvironment);
        PointcutsBuilder pointcutsBuilder = PointcutsBuilder.getInstance(annotationProcessingBasePackage);

        for ( TypeElement annotation : annotations ) {
            for ( Element element : roundEnvironment.getElementsAnnotatedWith(annotation) ) {
                if(element.getKind().equals(ElementKind.METHOD)) {
                    String annotationCanonicalName = annotation.getQualifiedName().toString();
                    pointcutsBuilder.addPointcut(annotationCanonicalName);
                }
            }
        }

        if(annotations.size() > 0) {
            pointcutsBuilder.build(processingEnv.getFiler());
            pointcutsBuilder.getAnnotationToPointcut().forEach((key, value) -> {
                AdviceBuilder adviceBuilder = AdviceFactory.getInstance().getAdviceBuilder(key, annotationProcessingBasePackage, value);
                if(adviceBuilder != null)
                    adviceBuilder.build(processingEnv.getFiler());
            });
        }

        return true;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Set.of(Trace.class.getCanonicalName(), Proxy.class.getCanonicalName());
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_11;
    }

    private void buildAnnotationProcessingRootPackage(RoundEnvironment roundEnvironment) {
        if(annotationProcessingBasePackage.equals("")) {
            annotationProcessingBasePackage = extractRootPackage(roundEnvironment) + "." + "annotationProcessing";
        }
    }

    private String extractRootPackage(RoundEnvironment roundEnvironment) {
        Set<? extends Element> rootElements = roundEnvironment.getRootElements();
        if(rootElements.size() == 0) return "";
        String rootElement = rootElements.stream().findFirst().orElseThrow().toString();
        int lastIndexOfDot = rootElement.lastIndexOf('.');
        return rootElement.substring(0, lastIndexOfDot);
    }
}
