package com.learn.annotationprocessing.builder;

import javax.annotation.processing.Filer;

public interface AdviceBuilder {
    void build(Filer filer);
}
