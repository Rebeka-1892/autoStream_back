package com.projet_voiture.projet_voiture.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;

import com.projet_voiture.projet_voiture.modele.Annonce;
import com.projet_voiture.projet_voiture.modele.Utilisateur;
import com.projet_voiture.projet_voiture.modele.Validation;
import com.projet_voiture.projet_voiture.repository.UtilisateurRepository;
import com.projet_voiture.projet_voiture.service.AnnonceService;
import com.projet_voiture.projet_voiture.service.ValidationService;

import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/annonce")
public class AnnonceController {
    @Autowired
    private AnnonceService service;
    @Autowired
    private UtilisateurRepository repository;
    @Autowired
    private ValidationService servicevalidation;

    @PreAuthorize("hasRole('USER') || hasRole('ADMIN')")
    @GetMapping("/historique")
    public List<Annonce> getHistorique() {
        String login = String.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
        Optional<Utilisateur> utilisateurOptional = repository.findByEmail(login);
        Utilisateur utilisateur = new Utilisateur();
        if (utilisateurOptional.isPresent()) {
            utilisateur = utilisateurOptional.get();
            return service.getHistorique(utilisateur.getIdutilisateur());
        }
        return null;
    }

    @PutMapping()
    public Validation updateValidation(@RequestBody Validation Validation) {
        return service.updateValidation(Validation);
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/unvalid")
    public List<Annonce> getUnvalidatedAnnonces() throws IOException {
        return service.getUnvalidatedAnnonces();
    }
    
    @PreAuthorize("hasRole('USER') || hasRole('ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Annonce insert(@RequestBody Annonce Annonce) {
        String login = String.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());

        Optional<Utilisateur> utilisateurOptional = repository.findByEmail(login);
        Utilisateur utilisateur = new Utilisateur();
        if (utilisateurOptional.isPresent()) {

            utilisateur = utilisateurOptional.get();

            Annonce.setIdutilisateur(utilisateur.getIdutilisateur());
            Annonce.setDatepub(LocalDateTime.now());
            Annonce = service.insert(Annonce);
        
            Validation Validation = new com.projet_voiture.projet_voiture.modele.Validation();
            Validation.setEtat(0);
            Validation.setIdannonce(Annonce.getIdannonce());
            servicevalidation.insert(Validation);
            return Annonce;
        }
        return null;
    }

    @GetMapping("/{AnnonceId}")
    public Annonce findById(@PathVariable String AnnonceId) {
        return service.findById(AnnonceId);
    }

    @GetMapping("/parPrix")
    public Page<Annonce> searchByPrixBetween( @RequestParam("prixMin") double prixMin, @RequestParam("prixMax") double prixMax, @RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "size", defaultValue = "10") int size) {
        return service.searchByPrixBetween(prixMin, prixMax, page, size);
    }

    @GetMapping("/modele/{idModele}")
    public Page<Annonce> getAnnoncesByModeleId( @PathVariable int idModele, @RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "size", defaultValue = "10") int size) {
        return service.getAnnoncesByIdModele(idModele, page, size);
    }

    @GetMapping("/categorie/{idCategorie}")
    public Page<Annonce> getAnnonceByIdCategorie( @PathVariable int idCategorie, @RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "size", defaultValue = "10") int size) {
        return service.getAnnonceByIdCategorie(idCategorie, page, size);
    }

    @GetMapping("/marque/{idMarque}")
    public Page<Annonce> getAnnonceByIdMarque( @PathVariable int idMarque, @RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "size", defaultValue = "10") int size) {
        return service.getAnnonceByIdMarque(idMarque, page, size);
    }
    
    @GetMapping("/dateSortie")
    public Page<Annonce> getAnnoncesByDateSortie( @RequestParam String dateDebut, @RequestParam String dateFin, @RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "size", defaultValue = "10") int size) {
        try {
            return service.getAnnoncesByDateSortie(dateDebut, dateFin, page, size);
        } catch (Exception e) {
            return null;
        }
    }

    @GetMapping("/motCle")
    public Page<Annonce> getAnnoncesByKeyWord( @RequestParam String motCle, @RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "size", defaultValue = "10") int size) {
        return service.getAnnoncesByKeyWord(motCle, page, size);       
    }

}
