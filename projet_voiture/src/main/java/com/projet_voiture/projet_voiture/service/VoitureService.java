package com.projet_voiture.projet_voiture.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projet_voiture.projet_voiture.modele.Voiture;
import com.projet_voiture.projet_voiture.repository.VoitureRepository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class VoitureService {
    @Autowired
    private VoitureRepository repository;

    public Voiture insert(Voiture Voiture) {
        Voiture.setIdvoiture(UUID.randomUUID().toString().split("-")[0]);
        return repository.save(Voiture);
    }

    public List<Voiture> findAll() {
        return repository.findAll();
    }

    public Voiture findById(String VoitureId){
        return repository.findById(VoitureId).get();
    }

    public Voiture updateVoiture(Voiture VoitureRequest){
        Voiture existingVoiture = repository.findById(VoitureRequest.getIdvoiture()).get();
        existingVoiture.setNbplace(VoitureRequest.getNbplace());
        existingVoiture.setNbporte(VoitureRequest.getNbporte());
        existingVoiture.setKilometrage(VoitureRequest.getKilometrage());
        existingVoiture.setCylindre(VoitureRequest.getCylindre());
        existingVoiture.setPuissance(VoitureRequest.getPuissance());
        existingVoiture.setFumeur(VoitureRequest.getFumeur());
        existingVoiture.setIddrivetype(VoitureRequest.getIddrivetype());
        existingVoiture.setDatesortie(VoitureRequest.getDatesortie());
        existingVoiture.setIdconduite(VoitureRequest.getIdconduite());
        existingVoiture.setIdmodele(VoitureRequest.getIdmodele());
        existingVoiture.setIdtransmission(VoitureRequest.getIdtransmission());
        existingVoiture.setIdenergie(VoitureRequest.getIdenergie());
        return repository.save(existingVoiture);
    }

    public String deleteVoiture(String VoitureId){
        repository.deleteById(VoitureId);
        return VoitureId+" Voiture deleted from dashboard ";
    }

    public List<Voiture> getVoituresByModeleId(int idModele) {
        return repository.findByIdmodele(idModele);
    }

    public List<Voiture> getVoitureByIdModeleIn(List<Integer> idModele) {
        return repository.findByIdmodeleIn(idModele);
    }

    public List<Voiture> findByDatesortieBetween(Date dateDebut, Date dateFin) {
        return repository.findByDatesortieBetween(dateDebut, dateFin);
    }
}
