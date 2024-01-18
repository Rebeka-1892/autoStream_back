package com.projet_voiture.projet_voiture.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;

import com.projet_voiture.projet_voiture.modele.Favori;
import com.projet_voiture.projet_voiture.service.FavoriService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Favori")
public class FavoriController {
    @Autowired
    private FavoriService service;

    @Transactional
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Favori insert(@RequestBody Favori Favori) {
        return service.insert(Favori);
    }

    @GetMapping
    public List<Favori> findAll() {
        return service.findAll();
    }

    @GetMapping("/{FavoriId}")
    public Favori findById(@PathVariable String FavoriId) {
        return service.findById(FavoriId);
    }

    @DeleteMapping("/{FavoriId}")
    public String deleteFavori(@PathVariable String FavoriId) {
        return service.deleteFavori(FavoriId);
    }
}