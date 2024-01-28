package com.projet_voiture.projet_voiture.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.projet_voiture.projet_voiture.modele.Voiture;
// import org.springframework.data.mongodb.repository.Query;

public interface VoitureRepository extends MongoRepository<Voiture, String>{
    
    List<Voiture> findByIdmodele(int idModele);

    List<Voiture> findByIdmodeleIn(List<Integer> listIdModele);

    List<Voiture> findByDatesortieBetween(Date dateDebut, Date dateFin);
}
