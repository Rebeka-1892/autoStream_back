package com.projet_voiture.projet_voiture.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projet_voiture.projet_voiture.modele.Drivetype;
import com.projet_voiture.projet_voiture.repository.DrivetypeRepository;

@Service
public class DrivetypeService {
    @Autowired
    private DrivetypeRepository repository;

    public List<Drivetype> list() {
        return repository.findAll();
    }

    public Drivetype insert(Drivetype Drivetype) {
        return repository.save(Drivetype);
    }

    public Optional<Drivetype> findById(int idDrivetype) {
        return repository.findById(idDrivetype);
    }

    public void deleteById(int idDrivetype) {
        repository.deleteById(idDrivetype);
    }
}
