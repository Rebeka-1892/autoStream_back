package com.projet_voiture.projet_voiture.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.projet_voiture.projet_voiture.modele.Marque;
import com.projet_voiture.projet_voiture.modele.Modele;
import java.util.List;


@Repository
public interface ModeleRepository extends JpaRepository<Modele, Integer>{
    List<Modele> findByMarque(Marque marque);

    @Query(
        value = "SELECT * FROM modele WHERE idcategorie = ?1",
        nativeQuery = true
    )
    List<Modele> findByIdCategorie(int idCategorie);

    @Query(
        value = "SELECT * FROM modele WHERE idmarque = ?1",
        nativeQuery = true
    )
    List<Modele> findByIdMarque(int idMarque);
}
