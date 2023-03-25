package com.api.assemblyvoter.dto.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class ResponseHandler {
    public static ResponseEntity<Object> generateResponse(String message, HttpStatus status) {
        Map<String, Object> map = new HashMap<>();
        map.put("message", message);

        return new ResponseEntity<>(map,status);
    }

    public static ResponseEntity<Object> generateResponse(Object responseObj, HttpStatus status) {
        Map<String, Object> map = new HashMap<>();
        map.put("data", responseObj);

        return new ResponseEntity<>(map,status);
    }
}
