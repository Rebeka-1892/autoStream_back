package com.projet_voiture.projet_voiture.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
 

import com.projet_voiture.projet_voiture.modele.Transmission;
import com.projet_voiture.projet_voiture.service.TransmissionService;

@RequestMapping("/transmission")
@RestController
public class TransmissionController {
    @Autowired
    private TransmissionService service;

    @GetMapping
    public List<Transmission> list() {
        return service.list();
    }

    @GetMapping("/{id}")
    public Optional<Transmission> findById(@PathVariable("id") int id) {
        return service.findById(id);
    }

     
    @PostMapping
    public ResponseEntity<Transmission> insert(@RequestBody Transmission Transmission) {
        try {
            Transmission inserted = service.insert(Transmission);
            return new ResponseEntity<>(inserted, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Transmission> update(@PathVariable("id") int id, @RequestBody Transmission Transmission) {
        Optional<Transmission> to_update = service.findById(id);
        if (to_update.isPresent()) {
            Transmission updated = to_update.get();
            updated.setNomtransmission(Transmission.getNomtransmission());
            return new ResponseEntity<Transmission>(
                    service.insert(updated),
                    HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteById(@PathVariable("id") int id) {
        try {
            service.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
