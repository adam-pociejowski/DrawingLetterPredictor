package com.valverde.letterpainter.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

    @RequestMapping("/home")
    public String showIndex() {
        return "index";
    }

    @RequestMapping("/test")
    public String test() {
        return "test";
    }
}
