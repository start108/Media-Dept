package com.jy.hessed.media.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("main")
    public String mainView(Model model) {
        model.addAttribute("data", "중 입니다.");
        return "main";
    }
}
