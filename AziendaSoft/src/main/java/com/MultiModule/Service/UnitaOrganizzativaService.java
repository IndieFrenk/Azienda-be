package com.MultiModule.Service;


import com.MultiModule.DAO.DipendenteDAO;
import com.MultiModule.DAO.RuoloDAO;
import com.MultiModule.DAO.UnitaOrganizzativaDAO;
import com.MultiModule.DTO.UnitaOrganizzativaDTO;
import com.MultiModule.Entity.Dipendente;
import com.MultiModule.Entity.Ruolo;
import com.MultiModule.Entity.UnitaOrganizzativa;
import com.MultiModule.Utility.GestioneRuoloStrategy;
import com.MultiModule.Utility.RuoloFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
        UnitaOrganizzativa unitaOrganizzativa = new UnitaOrganizzativa();
        unitaOrganizzativa.setNome(unitaOrganizzativaDTO.getNome());
        //metodo per sistemarli
        List<String> ruoli =  unitaOrganizzativaDTO.getRuoli();
        List<Ruolo> ruoloList =  new ArrayList<>();
        for (String i : ruoli ) { ruoloList.add(ruoloDAO.findByNome(i));}
        unitaOrganizzativa.setRuoli(ruoloList);

        unitaOrganizzativa.setUnitaSuperiore(unitaOrganizzativaRepository.findByNome(unitaOrganizzativaDTO.getUnitaSuperiore()));

        List<String> unita =  unitaOrganizzativaDTO.getUnitaSottostanti();
        List<UnitaOrganizzativa> unitaList =  new ArrayList<>();
        for (String i : unita ) { unitaList.add(unitaOrganizzativaRepository.findByNome(i));}
        unitaOrganizzativa.setUnitaSottostanti(unitaList);

        List<String> dipendente =  unitaOrganizzativaDTO.getRuoli();
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

    public UnitaOrganizzativa updateUnitaOrganizzativa(Long id, UnitaOrganizzativa unitaOrganizzativa) {
        Optional<UnitaOrganizzativa> unitaEsistente = unitaOrganizzativaRepository.findById(id);
        if (unitaEsistente.isPresent()) {
            UnitaOrganizzativa unitaAggiornata = unitaEsistente.get();
            unitaAggiornata.setNome(unitaOrganizzativa.getNome());
            unitaAggiornata.setRuoli(unitaOrganizzativa.getRuoli());
            unitaAggiornata.setDipendenti(unitaOrganizzativa.getDipendenti());
            return unitaOrganizzativaRepository.save(unitaAggiornata);
        } else {
            throw new IllegalArgumentException("Unità organizzativa non trovata.");
        }
    }

    public void deleteUnitaOrganizzativa(Long id) {
        unitaOrganizzativaRepository.deleteById(id);
    }

    public Ruolo createRuoloPerUnita(String tipoRuolo, UnitaOrganizzativa unitaOrganizzativa) {
        Ruolo nuovoRuolo = RuoloFactory.createRuolo(tipoRuolo, unitaOrganizzativa);
        nuovoRuolo.setUnitaOrganizzativa(unitaOrganizzativa);
        return ruoloDAO.save(nuovoRuolo);
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

    public List<Dipendente> getDipendentiUnita(Long unitaId) {
        Optional<UnitaOrganizzativa> unitaOpt = unitaOrganizzativaRepository.findById(unitaId);

        if (unitaOpt.isPresent()) {
            return unitaOpt.get().getDipendenti();
        } else {
            throw new IllegalArgumentException("Unità organizzativa non trovata.");
        }
    }

    //Servizi ruolo
    // 1. Aggiungi un ruolo a una unità organizzativa
    public Ruolo aggiungiRuolo(Long unitaId, String nomeRuolo) {
        Optional<UnitaOrganizzativa> unitaOpt = unitaOrganizzativaRepository.findById(unitaId);

        if (unitaOpt.isPresent()) {
            UnitaOrganizzativa unita = unitaOpt.get();
            Ruolo nuovoRuolo = new Ruolo(nomeRuolo, unita);  // Crea il nuovo ruolo connesso all'unità organizzativa
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

            if (ruolo.getUnitaOrganizzativa().equals(unita)) {
                ruoloDAO.delete(ruolo);  // Elimina il ruolo solo se appartiene a questa unità
            } else {
                throw new IllegalArgumentException("Il ruolo non appartiene all'unità specificata.");
            }
        } else {
            throw new IllegalArgumentException("Unità organizzativa o ruolo non trovati.");
        }
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
}
