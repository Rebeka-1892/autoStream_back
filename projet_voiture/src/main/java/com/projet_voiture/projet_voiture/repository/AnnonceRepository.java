package com.projet_voiture.projet_voiture.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.projet_voiture.projet_voiture.modele.Annonce;
import java.util.List;
import org.springframework.data.mongodb.repository.Query;

public interface AnnonceRepository extends MongoRepository<Annonce, String>{

    List<Annonce> findAllByIdannonceIn(List<String> allValidationIds);

    public List<Annonce> findByIdannonceIn(List<String> idAnnonces);

    @Query("{ 'idutilisateur': ?0, 'idannonce': { $in: ?1 } }")
    List<Annonce> findByIduutilisateurAndIdannonceIn(String idutilisateur, List<String> allValidationIds);

    @Query("{ 'prix' : { $gte : ?0, $lte : ?1 } }")
    Page<Annonce> findByPrixBetween(double prixMin, double prixMax, PageRequest pageRequest);

    Page<Annonce> findByIdvoitureIn(List<String> idVoitures, PageRequest pageRequest);

    @Query("{'descri': {$regex: ?0, $options: 'i'}}")
    Page<Annonce> findByKeyword(String keyword, PageRequest pageRequest);
}
