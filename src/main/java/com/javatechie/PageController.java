package com.javatechie;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class PageController {

    @GetMapping
    public String index(Principal principal, Model model) {
        model.addAttribute("user", principal.getName());
        return "index";
    }

    @GetMapping("/ott/sent")
    public String ottSent() {
        return "ott-sent";
    }

}
