package com.api.reflection.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class MakeException {

    @GetMapping("makeWarn/{message}")
    public String makeWarn(@PathVariable("message") String message) {
        log.warn("I will make a warn"+message);
        return "success";
    }
}
