package com.learn.annotationprocessing.advices;

import com.learn.annotationprocessing.builder.AdviceBuilder;
import com.learn.annotationprocessing.builder.TraceAdviceBuilder;

public class AdviceFactory {
    private static AdviceFactory adviceFactory;

    private AdviceFactory() {

    }

    public static AdviceFactory getInstance() {
        if(adviceFactory == null)
            adviceFactory = new AdviceFactory();
        return adviceFactory;
    }

    public AdviceBuilder getAdviceBuilder(String annotation, String annotationProcessingBasePackage, String methodFullPackage) {
        if(annotation.equals("Trace")) return TraceAdviceBuilder.getInstance(annotationProcessingBasePackage, methodFullPackage);
        return null;
    }
}
