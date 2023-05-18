package com.example.security.test;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/test")
public class TestController {
    @GetMapping
    public ResponseEntity<String> testfunc(){ // @RequestHeader(value="authorization") String auth
        /*Base64.Decoder decoder = Base64.getUrlDecoder();
        String[] chunks = auth.split("\\.");
        String payload = new String(decoder.decode(chunks[1]));
        System.out.println(payload);*/
        return ResponseEntity.ok("test success.");
    }
}
