package com.projet_voiture.projet_voiture.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;

// import com.projet_voiture.projet_voiture.service.FavoriService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;

import com.projet_voiture.projet_voiture.modele.Annonce;
// import com.projet_voiture.projet_voiture.modele.Favori;
// import com.projet_voiture.projet_voiture.modele.HistoriqueAnnonce;
import com.projet_voiture.projet_voiture.modele.Utilisateur;
import com.projet_voiture.projet_voiture.modele.Validation;
import com.projet_voiture.projet_voiture.repository.UtilisateurRepository;
import com.projet_voiture.projet_voiture.service.AnnonceService;
// import com.projet_voiture.projet_voiture.service.HistoriqueAnnonceService;
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
    // @Autowired
    // private HistoriqueAnnonceService historiqueAnnonceService;

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

    // @GetMapping
    // public List<Annonce> findAll() {
    //     return service.findAll();
    // }
    // @PutMapping
    // public Annonce updateAnnonce(@RequestBody Annonce Annonce) {
    //     return service.updateAnnonce(Annonce);
    // }
    // @DeleteMapping("/{AnnonceId}")
    // public String deleteAnnonce(@PathVariable String AnnonceId) {
    //     return service.deleteAnnonce(AnnonceId);
    // }
    // @PostMapping("/modifAnnonce")
    // public ResponseEntity<Annonce> updateAnnonceHistorique(@RequestBody Annonce annonce)
    // {
    //     Annonce a = service.modifAnnonce(annonce);
    //     return ResponseEntity.ok().body(a);
    // }
    // @GetMapping("/histo/{idAnnonce}")
    // public List<HistoriqueAnnonce> getHistoriqueAnnonceByIdAnnonce(@PathVariable String idAnnonce)
    // {
    //     return historiqueAnnonceService.getAllHistoAnnonceByIdAnnonce(idAnnonce);
    // }

    @GetMapping("/annonces/parPrix")
    public Page<Annonce> searchByPrixBetween(
            @RequestParam("prixMin") double prixMin,
            @RequestParam("prixMax") double prixMax,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        return service.searchByPrixBetween(prixMin, prixMax, page, size);
    }

    @GetMapping("/annonces/modele/{idModele}")
    public Page<Annonce> getAnnoncesByModeleId(
            @PathVariable int idModele,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        return service.getAnnoncesByIdModele(idModele, page, size);
    }

    @GetMapping("/annonces/categorie/{idCategorie}")
    public Page<Annonce> getAnnonceByIdCategorie(
            @PathVariable int idCategorie,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        return service.getAnnonceByIdCategorie(idCategorie, page, size);
    }

    @GetMapping("/annonces/marque/{idMarque}")
    public Page<Annonce> getAnnonceByIdMarque(
            @PathVariable int idMarque,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        return service.getAnnonceByIdMarque(idMarque, page, size);
    }
    
    @GetMapping("/annonces/dateSortie")
    public Page<Annonce> getAnnoncesByDateSortie(
            @RequestParam String dateDebut,
            @RequestParam String dateFin,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        try {
            return service.getAnnoncesByDateSortie(dateDebut, dateFin, page, size);
        } catch (Exception e) {
            return null;
        }
    }

    @GetMapping("/annonces/motCle")
    public Page<Annonce> getAnnoncesByKeyWord(
            @RequestParam String motCle,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        return service.getAnnoncesByKeyWord(motCle, page, size);       
    }

}
