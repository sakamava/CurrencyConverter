package com.satish.currency.converter.web.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * IndexController
 */
@RestController
public class IndexController {

    /**
     * Index endpoint to show the index page
     *
     * @param model Spring's view model
     * @return view name
     */
    @GetMapping({"/", "/index"})
    public String index(Model model) {
        model.addAttribute("title", "Check24 Task");
        model.addAttribute("welcome", "Welcome to Check24");
        model.addAttribute("applicationTitle", "Check24 Currency Converter");
        return "index";
    }
}
