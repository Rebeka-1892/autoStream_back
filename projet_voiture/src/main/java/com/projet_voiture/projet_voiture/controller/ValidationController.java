package com.projet_voiture.projet_voiture.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
 

import com.projet_voiture.projet_voiture.modele.Validation;
import com.projet_voiture.projet_voiture.service.ValidationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;



@RestController
@RequestMapping("/validation")
public class ValidationController {
    @Autowired
    private ValidationService service;

    @GetMapping("/list")
    public List<String> findAllValidationIds() {
        return service.getAllValidationIds();
    }  

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Validation insert(@RequestBody Validation Validation) {
        return service.insert(Validation);
    }

    @GetMapping
    public List<Validation> findAll() {
        return service.findAll();
    }

    @GetMapping("/etat/{etat}")
    public List<Validation> findByEtat(@PathVariable int etat) {
        return service.findByEtat(etat);
    }

    @GetMapping("/{ValidationId}")
    public Validation findById(@PathVariable String ValidationId) {
        return service.findById(ValidationId);
    }

    @PutMapping
    public Validation updateValidation(@RequestBody Validation Validation) {
        return service.updateValidation(Validation);
    }

    @DeleteMapping("/{ValidationId}")
    public String deleteValidation(@PathVariable String ValidationId) {
        return service.deleteValidation(ValidationId);
    }
}
