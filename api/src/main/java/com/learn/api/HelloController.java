package com.learn.api;

import com.learn.annotationprocessing.annotations.Trace;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @Trace
    @GetMapping
    public String hello() {
        return "hello";
    }

    @GetMapping("/a")
    public String test() {
        return "test";
    }
}
