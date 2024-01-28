package com.projet_voiture.projet_voiture.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.projet_voiture.projet_voiture.modele.Annonce;
import com.projet_voiture.projet_voiture.modele.HistoriqueValidation;
import com.projet_voiture.projet_voiture.modele.Tresorerie;
import com.projet_voiture.projet_voiture.modele.Validation;
import com.projet_voiture.projet_voiture.modele.Voiture;
import com.projet_voiture.projet_voiture.modele.Modele;
// import com.projet_voiture.projet_voiture.modele.HistoriqueAnnonce;
import com.projet_voiture.projet_voiture.repository.AnnonceRepository;
import com.projet_voiture.projet_voiture.repository.ValidationRepository;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AnnonceService {
    @Autowired
    private AnnonceRepository repository;
    @Autowired
    private ValidationService validationService;
    @Autowired
    private ValidationRepository validationRepository;
    @Autowired
    private CommissionService commissionService;
    @Autowired
    private TresorerieService tresorerieService;
    @Autowired
    private HistoriqueValidationService historiqueValidationService;
    // @Autowired
    // private HistoriqueAnnonceService historiqueAnnonceService;

    public Annonce insert(Annonce Annonce) {
        Annonce.setIdannonce(UUID.randomUUID().toString().split("-")[0]);
        return repository.save(Annonce);
    }

    @Transactional
    public Validation updateValidation(Validation ValidationRequest){
        Validation existingValidation = validationRepository.findByIdannonce(ValidationRequest.getIdannonce()).get();

        HistoriqueValidation historiqueValidation = new HistoriqueValidation();
        historiqueValidation.setIdvalidation(existingValidation.getIdvalidation());
        historiqueValidation.setIdannonce(existingValidation.getIdannonce());
        historiqueValidation.setEtat(existingValidation.getEtat());
        historiqueValidation.setDatemodif(existingValidation.getDatemodif());

        historiqueValidationService.insertHistoriqueValidation(historiqueValidation);

        existingValidation.setEtat(ValidationRequest.getEtat());
        existingValidation.setIdannonce(ValidationRequest.getIdannonce());
        existingValidation.setDatemodif(LocalDateTime.now());
        

        if(existingValidation.getEtat() == 3) {
            double pourcentage = commissionService.getLastCommission().getValeur();
            double prixVoiture = repository.findById(existingValidation.getIdannonce()).get().getPrix();

            double gainSite = prixVoiture *(pourcentage/100);

            Tresorerie tresorerie = new Tresorerie();
            tresorerie.setIdannonce(existingValidation.getIdannonce());
            tresorerie.setEntre(gainSite);
            tresorerie.setSortie(0);
            LocalDateTime currentDate = LocalDateTime.now();
            Instant instant = currentDate.toInstant(ZoneOffset.UTC);
            tresorerie.setDatemouvement(Timestamp.from(instant));

            tresorerieService.insertTresorerie(tresorerie);
        }
        return validationRepository.save(existingValidation);
    }

    public List<Annonce> getUnvalidatedAnnonces() {
        return repository.findAllByIdannonceIn(validationService.getAllValidationIds());
    }

    public List<Annonce> getHistorique(String idutilisateur) {
        return repository.findByIduutilisateurAndIdannonceIn(idutilisateur, validationService.getHistoriqueValidation());
    }

    public Annonce findById(String AnnonceId){
        return repository.findById(AnnonceId).get();
    }
     
    // public Annonce modifAnnonce(Annonce annonce)
    // {
    //     Annonce annonceTemp = findById(annonce.getIdannonce());

    //     HistoriqueAnnonce historiqueAnnonce = new HistoriqueAnnonce();
    //     historiqueAnnonce.setIdAnnonce(annonceTemp.getIdannonce());
    //     historiqueAnnonce.setDescri(annonceTemp.getDescri());
    //     historiqueAnnonce.setPrix(annonceTemp.getPrix());
    //     historiqueAnnonce.setDatepub(annonceTemp.getDatepub());
    //     historiqueAnnonce.setIdVoiture(annonceTemp.getIdvoiture());
    //     historiqueAnnonce.setIdUtilisateur(annonceTemp.getIdutilisateur());

    //     historiqueAnnonceService.insertHistoriqueAnnonce(historiqueAnnonce);

    //     return repository.save(annonce);
    // }
    // public List<Annonce> findAll() {
    //     return repository.findAll();
    // }
    // public Annonce updateAnnonce(Annonce AnnonceRequest){
    //     Annonce existingAnnonce = repository.findById(AnnonceRequest.getIdannonce()).get();
    //     existingAnnonce.setDatepub(AnnonceRequest.getDatepub());
    //     existingAnnonce.setDescri(AnnonceRequest.getDescri());
    //     existingAnnonce.setIdutilisateur(AnnonceRequest.getIdutilisateur());
    //     existingAnnonce.setIdvoiture(AnnonceRequest.getIdvoiture());
    //     existingAnnonce.setPrix(AnnonceRequest.getPrix());
    //     return repository.save(existingAnnonce);
    // }
    // public String deleteAnnonce(String AnnonceId){
    //     repository.deleteById(AnnonceId);
    //     return AnnonceId+" Annonce deleted from dashboard ";
    // }

    public Page<Annonce> searchByPrixBetween(double prixMin, double prixMax, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "prix"));
        return repository.findByPrixBetween(prixMin, prixMax, pageRequest);
    }


    public Page<Annonce> getAnnoncesByIdModele(int idModele, int page, int size) {
        List<Voiture> voitures = voitureService.getVoituresByModeleId(idModele);
        List<String> idVoitures = voitures.stream().map(Voiture::getIdvoiture).collect(Collectors.toList());
        PageRequest pageRequest = PageRequest.of(page, size);
        return repository.findByIdvoitureIn(idVoitures, pageRequest);
    }

    public Page<Annonce> getAnnonceByIdCategorie(int idCategorie, int page, int size) {
        List<Modele> getModeles = modeleService.findByIdCategorie(idCategorie);
        List<Integer> listIdModele = getModeles.stream().map(Modele::getIdmodele).collect(Collectors.toList());
        List<Voiture> getVoitures = voitureService.getVoitureByIdModeleIn(listIdModele);
        List<String> idVoitures = getVoitures.stream().map(Voiture::getIdvoiture).collect(Collectors.toList());
        PageRequest pageRequest = PageRequest.of(page, size);
        return repository.findByIdvoitureIn(idVoitures, pageRequest);
    }

    public Page<Annonce> getAnnonceByIdMarque(int idMarque, int page, int size) {
        List<Modele> getModeles = modeleService.findByIdMarque(idMarque);
        List<Integer> listIdModele = getModeles.stream().map(Modele::getIdmodele).collect(Collectors.toList());
        List<Voiture> getVoitures = voitureService.getVoitureByIdModeleIn(listIdModele);
        List<String> idVoitures = getVoitures.stream().map(Voiture::getIdvoiture).collect(Collectors.toList());
        PageRequest pageRequest = PageRequest.of(page, size);
        return repository.findByIdvoitureIn(idVoitures, pageRequest);
    }

    public Page<Annonce> getAnnoncesByDateSortie(String dateDebut, String dateFin, int page, int size) throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = dateFormat.parse(dateDebut);
        Date date2 = dateFormat.parse(dateFin);

        List<Voiture> voitures = voitureService.findByDatesortieBetween(date1, date2);
        List<String> idVoitures = voitures.stream().map(Voiture::getIdvoiture).collect(Collectors.toList());
        PageRequest pageRequest = PageRequest.of(page, size);
        return repository.findByIdvoitureIn(idVoitures, pageRequest);
    }

    public Page<Annonce> getAnnoncesByKeyWord(String motCle, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return repository.findByKeyword(motCle, pageRequest);
    }
}
