package com.projet_voiture.projet_voiture.modele;

import java.sql.Timestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Tresorerie {

    @Id
    @GeneratedValue(
        strategy = GenerationType.IDENTITY
    )
    int idtresorerie;
    String idannonce;
    double entre;
    double sortie;
    Timestamp datemouvement;
}