package com.MultiModule.Service;


import com.MultiModule.DAO.DipendenteDAO;
import com.MultiModule.DAO.RuoloDAO;
import com.MultiModule.DAO.UnitaOrganizzativaDAO;
import com.MultiModule.DTO.DipendenteDTO;
import com.MultiModule.DTO.UnitaOrganizzativaDTO;
import com.MultiModule.Entity.Dipendente;
import com.MultiModule.Entity.Ruolo;
import com.MultiModule.Entity.UnitaOrganizzativa;
import com.MultiModule.Utility.GestioneRuoloStrategy;
import org.elasticsearch.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UnitaOrganizzativaService {
    @Autowired
    private UnitaOrganizzativaDAO unitaOrganizzativaRepository;
    private GestioneRuoloStrategy gestioneRuoloStrategy;
    @Autowired
    private RuoloDAO ruoloDAO;
    @Autowired
    private DipendenteDAO dipendenteDAO;

    public UnitaOrganizzativa createUnitaOrganizzativa(UnitaOrganizzativaDTO unitaOrganizzativaDTO) {
        System.out.println(unitaOrganizzativaDTO.getUnitaSuperiore());

        if (unitaOrganizzativaRepository.findByNome(unitaOrganizzativaDTO.getNome()) != null) {
            return null;
        }
        UnitaOrganizzativa unitaOrganizzativa = new UnitaOrganizzativa();
        unitaOrganizzativa.setNome(unitaOrganizzativaDTO.getNome());
        //metodo per sistemarli
        List<String> ruoli =  unitaOrganizzativaDTO.getRuoli();
        List<Ruolo> ruoloList =  new ArrayList<>();
        for (String i : ruoli ) { ruoloList.add(ruoloDAO.findByNome(i));}
        unitaOrganizzativa.setRuoli(ruoloList);

        if (unitaOrganizzativaDTO.getUnitaSuperiore() != null) {
            Optional<UnitaOrganizzativa> unitaSuperiore = unitaOrganizzativaRepository.findById(Long.valueOf(unitaOrganizzativaDTO.getUnitaSuperiore()));
            unitaOrganizzativa.setUnitaSuperiore(unitaSuperiore.get());
        }

        List<String> unita =  unitaOrganizzativaDTO.getUnitaSottostanti();
        List<UnitaOrganizzativa> unitaList =  new ArrayList<>();
        for (String i : unita ) { unitaList.add(unitaOrganizzativaRepository.findByNome(i));}
        unitaOrganizzativa.setUnitaSottostanti(unitaList);

        List<String> dipendente =  unitaOrganizzativaDTO.getDipendenti();
        List<Dipendente> dipendenteList =  new ArrayList<>();
        for (String i : dipendente ) { dipendenteList.add(dipendenteDAO.findByNome(i));}
        unitaOrganizzativa.setDipendenti(dipendenteList);
        return unitaOrganizzativaRepository.save(unitaOrganizzativa);
    }


    public List<UnitaOrganizzativa> getAllUnitaOrganizzative() {
        return unitaOrganizzativaRepository.findAll();
    }

    public Optional<UnitaOrganizzativa> getUnitaOrganizzativaById(Long id) {
        return unitaOrganizzativaRepository.findById(id);
    }

    public UnitaOrganizzativaDTO convertToDTO(UnitaOrganizzativa unita) {
        UnitaOrganizzativaDTO dto = new UnitaOrganizzativaDTO();
        dto.setId(unita.getId());
        dto.setNome(unita.getNome());

        // Converti i ruoli in una lista di stringhe (nomi dei ruoli)
        List<String> ruoli = unita.getRuoli().stream()
                .map(Ruolo::getNome)
                .collect(Collectors.toList());
        dto.setRuoli(ruoli);

        // Converti i dipendenti in una lista di stringhe (nomi dei dipendenti)
        List<String> dipendenti = unita.getDipendenti().stream()
                .map(Dipendente::getNome)
                .collect(Collectors.toList());
        dto.setDipendenti(dipendenti);

        return dto;
    }

    public UnitaOrganizzativa updateUnitaOrganizzativa(Long id, UnitaOrganizzativaDTO unitaOrganizzativaDTO) {
        Optional<UnitaOrganizzativa> unitaEsistente = unitaOrganizzativaRepository.findById(id);
        if (unitaEsistente.isPresent()) {
            UnitaOrganizzativa unitaAggiornata = unitaEsistente.get();
            unitaAggiornata.setNome(unitaOrganizzativaDTO.getNome());
            unitaAggiornata.setRuoli(this.getRuoli(unitaOrganizzativaDTO.getRuoli()));
            unitaAggiornata.setDipendenti(this.getDipendenti(unitaOrganizzativaDTO.getDipendenti()));
            unitaAggiornata.setUnitaSuperiore(unitaOrganizzativaRepository.findByNome(unitaOrganizzativaDTO.getUnitaSuperiore()));
            return unitaOrganizzativaRepository.save(unitaAggiornata);
        } else {
            throw new IllegalArgumentException("Unità organizzativa non trovata.");
        }
    }
    private List<Ruolo> getRuoli(List<String> ruoli) {
        List<Ruolo> ruoloList =  new ArrayList<>();
        for (String  i : ruoli) {
            ruoloList.add(ruoloDAO.findByNome(i));
        }
        return ruoloList;
    }
    private List<Dipendente> getDipendenti(List<String> dipendenti) {
        List<Dipendente> dipendenteList =  new ArrayList<>();
        for (String  i : dipendenti) {
            dipendenteList.add(dipendenteDAO.findByNome(i));
        }
        return dipendenteList;
    }

    public ResponseEntity<Object> deleteUnitaOrganizzativa(Long id) {
        UnitaOrganizzativa unitaOrganizzativa = unitaOrganizzativaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Unità organizzativa non trovata"));

        List<Ruolo> ruoli = unitaOrganizzativa.getRuoli();
        for (Ruolo ruolo : ruoli) {
            ruolo.getUnitaOrganizzative().remove(unitaOrganizzativa);
            ruoloDAO.save(ruolo);
        }
        unitaOrganizzativa.getRuoli().clear();
        unitaOrganizzativaRepository.save(unitaOrganizzativa);

        // 2. Rimuovi tutti i dipendenti associati all'unità organizzativa
        List<Dipendente> dipendenti = unitaOrganizzativa.getDipendenti();
        for (Dipendente dipendente : dipendenti) {
            dipendente.getUnitaOrganizzative().remove(unitaOrganizzativa);
            dipendenteDAO.save(dipendente);
        }
        unitaOrganizzativa.getDipendenti().clear();
        unitaOrganizzativaRepository.save(unitaOrganizzativa);

        // 3. Elimina l'unità organizzativa
        unitaOrganizzativaRepository.delete(unitaOrganizzativa);

        return ResponseEntity.noContent().build();
    }

    public Ruolo createRuoloPerUnita(String tipoRuolo, UnitaOrganizzativa unitaOrganizzativa) {
        Ruolo ruolo = new Ruolo();
        ruolo.setNome(tipoRuolo);
        Set<UnitaOrganizzativa> unita= new HashSet<>();
        unita.add(unitaOrganizzativa);

        ruolo.setUnitaOrganizzative(unita);
        return ruoloDAO.save(ruolo);
    }

    public void setGestioneRuoloStrategy(GestioneRuoloStrategy gestioneRuoloStrategy) {
        this.gestioneRuoloStrategy = gestioneRuoloStrategy;
    }

    public void gestisciUnitaOrganizzativa(UnitaOrganizzativa unita) {
        gestioneRuoloStrategy.gestisciUnita(unita);
    }



    //Servizi dipendente


    public UnitaOrganizzativa aggiungiDipendente(Long unitaId, Long dipendenteId) {
        Optional<UnitaOrganizzativa> unitaOpt = unitaOrganizzativaRepository.findById(unitaId);
        Optional<Dipendente> dipendenteOpt = dipendenteDAO.findById(dipendenteId);

        if (unitaOpt.isPresent() && dipendenteOpt.isPresent()) {
            UnitaOrganizzativa unita = unitaOpt.get();
            Dipendente dipendente = dipendenteOpt.get();
            unita.getDipendenti().add(dipendente);  // Aggiungi il dipendente alla lista
            return unitaOrganizzativaRepository.save(unita);  // Salva l'unità organizzativa aggiornata
        } else {
            throw new IllegalArgumentException("Unità organizzativa o dipendente non trovati.");
        }
    }

    public UnitaOrganizzativa rimuoviDipendente(Long unitaId, Long dipendenteId) {
        Optional<UnitaOrganizzativa> unitaOpt = unitaOrganizzativaRepository.findById(unitaId);
        Optional<Dipendente> dipendenteOpt = dipendenteDAO.findById(dipendenteId);

        if (unitaOpt.isPresent() && dipendenteOpt.isPresent()) {
            UnitaOrganizzativa unita = unitaOpt.get();
            Dipendente dipendente = dipendenteOpt.get();
            unita.getDipendenti().remove(dipendente);  // Rimuovi il dipendente dalla lista
            return unitaOrganizzativaRepository.save(unita);  // Salva l'unità organizzativa aggiornata
        } else {
            throw new IllegalArgumentException("Unità organizzativa o dipendente non trovati.");
        }
    }

    // 8. Trasferisci un dipendente da un'unità organizzativa a un'altra
    public void trasferisciDipendente(Long dipendenteId, Long unitaDaId, Long unitaAId) {
        Optional<Dipendente> dipendenteOpt = dipendenteDAO.findById(dipendenteId);
        Optional<UnitaOrganizzativa> unitaDaOpt = unitaOrganizzativaRepository.findById(unitaDaId);
        Optional<UnitaOrganizzativa> unitaAOpt = unitaOrganizzativaRepository.findById(unitaAId);

        if (dipendenteOpt.isPresent() && unitaDaOpt.isPresent() && unitaAOpt.isPresent()) {
            Dipendente dipendente = dipendenteOpt.get();
            UnitaOrganizzativa unitaDa = unitaDaOpt.get();
            UnitaOrganizzativa unitaA = unitaAOpt.get();

            unitaDa.getDipendenti().remove(dipendente);

            unitaA.getDipendenti().add(dipendente);

            unitaOrganizzativaRepository.save(unitaDa);
            unitaOrganizzativaRepository.save(unitaA);
        } else {
            throw new IllegalArgumentException("Dipendente o unità organizzative non trovati.");
        }
    }

    public List<Dipendente> getDipendentiUnita(Long unitaId, Long ruoloId) {
        Optional<UnitaOrganizzativa> unitaOpt = unitaOrganizzativaRepository.findById(unitaId);
        if (unitaOpt.isPresent()) {
            UnitaOrganizzativa unita = unitaOpt.get();

            // Mappa per associare i nomi dei ruoli con i nomi dei dipendenti
            List<Dipendente> ruoloDipendenti = new ArrayList<>() ;

            // Iterare attraverso ogni ruolo dell'unità
            for (Dipendente dipendente : unita.getDipendenti()) {
               if (dipendente.getRuoli().contains(ruoloDAO.findById(ruoloId).get())) {
                   ruoloDipendenti.add(dipendente);
               }
            }
            return ruoloDipendenti;
        } else {
            throw new IllegalArgumentException("Unità organizzativa non trovata.");
        }
    }

    //Servizi ruolo
    // 1. Aggiungi un ruolo a una unità organizzativa
    public Ruolo aggiungiRuolo(Long unitaId, String nomeRuolo) {
        Optional<UnitaOrganizzativa> unitaOpt = unitaOrganizzativaRepository.findById(unitaId);
        Optional<Ruolo> ruolo = Optional.ofNullable(ruoloDAO.findByNome(nomeRuolo));
        if (unitaOpt.isPresent()) {
            UnitaOrganizzativa unita = unitaOpt.get();

            if (ruolo.isPresent()){
                ruolo.get().getUnitaOrganizzative().add(unita);
                return ruoloDAO.save(ruolo.get());
            }
            Set<UnitaOrganizzativa> listUnita = new HashSet<>();
            listUnita.add(unita);
            Ruolo nuovoRuolo = new Ruolo(nomeRuolo, listUnita);  // Crea il nuovo ruolo connesso all'unità organizzativa
            return ruoloDAO.save(nuovoRuolo);  // Salva il ruolo nel repository
        } else {
            throw new IllegalArgumentException("Unità organizzativa non trovata.");
        }
    }

    // 2. Rimuovi un ruolo da una unità organizzativa
    public void rimuoviRuolo(Long unitaId, Long ruoloId) {
        Optional<UnitaOrganizzativa> unitaOpt = unitaOrganizzativaRepository.findById(unitaId);
        Optional<Ruolo> ruoloOpt = ruoloDAO.findById(ruoloId);

        if (unitaOpt.isPresent() && ruoloOpt.isPresent()) {
            Ruolo ruolo = ruoloOpt.get();
            UnitaOrganizzativa unita = unitaOpt.get();

            // Check if the role is indeed associated with the unit
            if (ruolo.getUnitaOrganizzative().contains(unita)) {
                // Remove the relationship
                ruolo.getUnitaOrganizzative().remove(unita);
                unita.getRuoli().remove(ruolo);

                // Save both entities to update the relationship in the database
                ruoloDAO.save(ruolo);
                unitaOrganizzativaRepository.save(unita);
            } else {
                throw new IllegalArgumentException("Il ruolo non appartiene all'unità specificata.");
            }
        } else {
            throw new IllegalArgumentException("Unità organizzativa o ruolo non trovati.");
        }
    }
    public Dipendente creaDipendenteConRuoliEUnita(DipendenteDTO dipendenteDTO) {

        List<Long> ruoliIds = dipendenteDTO.getRuoli();
        Long unitaId = dipendenteDTO.getUnita();
        System.out.println(dipendenteDTO.getUnita());
        System.out.println(dipendenteDTO.getRuoli());
        List<Ruolo> ruoli = new ArrayList<>();
        if (ruoliIds != null && !ruoliIds.isEmpty()) {
            ruoli = ruoloDAO.findAllById(ruoliIds);
        }

        UnitaOrganizzativa unita = new UnitaOrganizzativa();
        if (unitaId != null) {
            unita = unitaOrganizzativaRepository.findById(unitaId).get();

        }
        Dipendente dipendente = new Dipendente();

        if (dipendenteDAO.findByNome(dipendenteDTO.getNome()) == null || dipendenteDTO.getId() == null) {

            dipendente.setNome(dipendenteDTO.getNome());
        }
        else{
        dipendente = dipendenteDAO.findById(dipendenteDTO.getId()).get();}

        dipendente.setRuoli(new ArrayList<>());
        dipendente.setUnitaOrganizzative(new ArrayList<>());

        // Aggiungere i ruoli recuperati
        if (!ruoli.isEmpty()) {
            dipendente.getRuoli().addAll(ruoli);
        }

        // Aggiungere l'unità recuperata
        if (unita != null) {
            dipendente.getUnitaOrganizzative().add(unita);
        }
        // Gestire i ruoli
        if (ruoliIds != null && !ruoliIds.isEmpty()) {
            for (Long ruoloId : ruoliIds) {
                Optional<Ruolo> ruoloOpt = ruoloDAO.findById(ruoloId);
                if (ruoloOpt.isPresent()) {
                    Ruolo ruolo = ruoloOpt.get();

                    // Aggiungere il ruolo al dipendente
                    dipendente.getRuoli().add(ruolo);

                    // Aggiungere il dipendente al ruolo
                    if (ruolo.getDipendenti() == null) {
                        ruolo.setDipendenti(new ArrayList<>());
                    }
                    ruolo.getDipendenti().add(dipendente);

                    // Per ogni unità organizzativa associata al ruolo
                    if (ruolo.getUnitaOrganizzative() != null) {
                        for (UnitaOrganizzativa unitaRuolo : ruolo.getUnitaOrganizzative()) {
                            // Aggiungere il ruolo all'unità se non è già presente
                            if (!unitaRuolo.getRuoli().contains(ruolo)) {
                                unitaRuolo.getRuoli().add(ruolo);
                            }
                            // Salvare l'unità organizzativa aggiornata
                            unitaOrganizzativaRepository.save(unitaRuolo);
                        }
                    }

                    // Salvare il ruolo aggiornato
                    ruoloDAO.save(ruolo);
                }
            }
        }

        // Gestire l'unità organizzativa
        if (unitaId != null) {
            Optional<UnitaOrganizzativa> unitaOpt = unitaOrganizzativaRepository.findById(unitaId);
            if (unitaOpt.isPresent()) {
                 unita = unitaOpt.get();

                // Aggiungere l'unità al dipendente
                if (!dipendente.getUnitaOrganizzative().contains(unita)) {
                    dipendente.getUnitaOrganizzative().add(unita);
                }

                // Aggiungere il dipendente all'unità
                if (unita.getDipendenti() == null) {
                    unita.setDipendenti(new ArrayList<>());
                }
                if (!unita.getDipendenti().contains(dipendente)) {
                    unita.getDipendenti().add(dipendente);
                }

                // Gestire la relazione con i ruoli dell'unità
                if (unita.getRuoli() != null) {
                    for (Ruolo ruoloUnita : unita.getRuoli()) {
                        // Aggiungere l'unità al ruolo se non è già presente
                        if (!ruoloUnita.getUnitaOrganizzative().contains(unita)) {
                            ruoloUnita.getUnitaOrganizzative().add(unita);
                            ruoloDAO.save(ruoloUnita);
                        }
                    }
                }

                // Salvare l'unità organizzativa aggiornata
                unitaOrganizzativaRepository.save(unita);
            }
        }

        // Salvare e ritornare il dipendente finale
        return dipendenteDAO.save(dipendente);
    }
    // 3. Aggiorna un ruolo esistente
    public Ruolo aggiornaRuolo(Long ruoloId, String nuovoNome) {
        Optional<Ruolo> ruoloOpt = ruoloDAO.findById(ruoloId);

        if (ruoloOpt.isPresent()) {
            Ruolo ruolo = ruoloOpt.get();
            ruolo.setNome(nuovoNome);  // Aggiorna il nome del ruolo
            return ruoloDAO.save(ruolo);  // Salva il ruolo aggiornato
        } else {
            throw new IllegalArgumentException("Ruolo non trovato.");
        }
    }

    // 4. Recupera tutti i ruoli di una specifica unità organizzativa
    public List<Ruolo> getRuoliUnita(Long unitaId) {
        Optional<UnitaOrganizzativa> unitaOpt = unitaOrganizzativaRepository.findById(unitaId);

        if (unitaOpt.isPresent()) {
            return unitaOpt.get().getRuoli();  // Restituisce la lista dei ruoli collegati all'unità
        } else {
            throw new IllegalArgumentException("Unità organizzativa non trovata.");
        }
    }

    // 5. Assegna un ruolo a un dipendente
    public Ruolo assegnaRuoloADipendente(Long ruoloId, Long dipendenteId) {
        Optional<Ruolo> ruoloOpt = ruoloDAO.findById(ruoloId);
        Optional<Dipendente> dipendenteOpt = dipendenteDAO.findById(dipendenteId);

        if (ruoloOpt.isPresent() && dipendenteOpt.isPresent()) {
            Ruolo ruolo = ruoloOpt.get();
            Dipendente dipendente = dipendenteOpt.get();

            ruolo.getDipendenti().add(dipendente);  // Assegna il dipendente al ruolo
            dipendente.getRuoli().add(ruolo);  // Aggiungi il ruolo al dipendente

            ruoloDAO.save(ruolo);  // Salva il ruolo aggiornato
            dipendenteDAO.save(dipendente);  // Salva il dipendente aggiornato
            return ruolo;
        } else {
            throw new IllegalArgumentException("Ruolo o dipendente non trovati.");
        }
    }

    public List<Ruolo> getRuoli() {
        return ruoloDAO.findAll();
    }

    public List<Dipendente> getDipendentiNonAssegnati(Long unitaId) {
        List<Dipendente> listaTot = dipendenteDAO.findAll();
        listaTot.removeIf(i -> i.getUnitaOrganizzative().contains(unitaOrganizzativaRepository.findById(unitaId).get()));
        return listaTot;
    }

    public List<Dipendente> getAllDipendenti() {
        return dipendenteDAO.findAll();
    }

    /*public Dipendente creaDipendente(DipendenteDTO dipendente) {
        Dipendente dipendente1 = new Dipendente();
        dipendente1.setNome(dipendente.getNome());
        dipendente1.setRuoli(dipendente.getRuoli());
        dipendente1.setUnitaOrganizzative(dipendente.getUnitaOrganizzative());
        dipendenteDAO.save(dipendente1);
        return dipendente1;
    }*/
}
