package com.habit.app.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class EntryController {

    @GetMapping("/")
    public ModelAndView entryPage()
    {
        ModelAndView modelAndView = new ModelAndView("home");
            return modelAndView;
    }

}
