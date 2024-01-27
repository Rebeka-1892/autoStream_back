package com.projet_voiture.projet_voiture.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.projet_voiture.projet_voiture.modele.Drivetype;

@Repository
public interface DrivetypeRepository extends JpaRepository<Drivetype, Integer>{

    
}
