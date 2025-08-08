package com.roadmap.personalblog.web;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {
    @GetMapping("/")
    public String root() {
        return "forward:/index.html";
    }
    @GetMapping("/home")
    public String home() {
        return "forward:/index.html";
    }
}
