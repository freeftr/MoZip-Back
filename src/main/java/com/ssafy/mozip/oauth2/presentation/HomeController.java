package com.ssafy.mozip.oauth2.presentation;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "googleLogin"; // googleLogin.html 템플릿 반환
    }
}
