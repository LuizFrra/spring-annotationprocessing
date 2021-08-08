package com.learn.api;

import com.learn.annotationprocessing.annotations.Proxy;
import com.learn.annotationprocessing.annotations.Trace;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @Trace
    @GetMapping
    public String hello() {
        return "hello";
    }

    @Proxy(key="teste", host = "a")
    @GetMapping("/{id}")
    public String test(@PathVariable Long id, int ida) {
        return "test " + id;
    }
}
