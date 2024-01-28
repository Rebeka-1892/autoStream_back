package com.projet_voiture.projet_voiture.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projet_voiture.projet_voiture.modele.Marque;
import com.projet_voiture.projet_voiture.modele.Modele;
import com.projet_voiture.projet_voiture.repository.ModeleRepository;

@Service
public class ModeleService {
    @Autowired
    private ModeleRepository repository;

    public List<Modele> list() {
        return repository.findAll();
    }

    public List<Modele> findByMarque(Marque marque){
        return repository.findByMarque(marque);
    }

    public Modele insert(Modele Modele) {
        return repository.save(Modele);
    }

    public Optional<Modele> findById(int idModele) {
        return repository.findById(idModele);
    }

    public void deleteById(int idModele) {
        repository.deleteById(idModele);
    }

    public List<Modele> findByIdCategorie(int idCategorie)
    {
        return repository.findByIdCategorie(idCategorie);
    }

    public List<Modele> findByIdMarque(int idMarque)
    {
        return repository.findByIdMarque(idMarque);
    }
}
