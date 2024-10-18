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
        Ruolo nuovoRuolo = RuoloFactory.createRuolo(tipoRuolo);
        nuovoRuolo.setUnitaOrganizzativa(unitaOrganizzativa);
        return ruoloDAO.save(nuovoRuolo);
    }

    public void setGestioneRuoloStrategy(GestioneRuoloStrategy gestioneRuoloStrategy) {
        this.gestioneRuoloStrategy = gestioneRuoloStrategy;
    }

    public void gestisciUnitaOrganizzativa(UnitaOrganizzativa unita) {
        gestioneRuoloStrategy.gestisciUnita(unita);
    }
}
