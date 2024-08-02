package com.habit.app.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EntryController {

    @GetMapping("/")
    public String entryPage()
    {
            return "entry_page";
    }

}
