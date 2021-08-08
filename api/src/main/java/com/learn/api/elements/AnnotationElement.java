package com.learn.api.elements;

import java.util.HashMap;
import java.util.Map;

public class AnnotationElement extends Element {

    private Map<String, Object> members;

    public AnnotationElement(String type) {
        this.key = type;
        members = new HashMap<>();
    }

    public void addMember(String key, String value) {
        members.put(key, value);
    }

    public void addMembers(Map<String, Object> members) {
        this.members.putAll(members);
    }
}
