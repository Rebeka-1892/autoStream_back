package com.projet_voiture.projet_voiture.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projet_voiture.projet_voiture.modele.Transmission;
import com.projet_voiture.projet_voiture.repository.TransmissionRepository;

@Service
public class TransmissionService {
    @Autowired
    private TransmissionRepository repository;

    public List<Transmission> list() {
        return repository.findAll();
    }

    public Transmission insert(Transmission Transmission) {
        return repository.save(Transmission);
    }

    public Optional<Transmission> findById(int idTransmission) {
        return repository.findById(idTransmission);
    }

    public void deleteById(int idTransmission) {
        repository.deleteById(idTransmission);
    }
}
