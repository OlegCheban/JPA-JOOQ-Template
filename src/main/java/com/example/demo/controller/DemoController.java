package com.example.demo.controller;

import com.example.demo.service.PersonService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/demo")
public class DemoController {
    private PersonService personService;

    public DemoController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping(path = "/test")
    public ResponseEntity<?> demo(@RequestParam("name") String name) {
        personService.addPerson(name);
        return ResponseEntity.ok().build();
    }
}
