package Service;


import DAO.RuoloDAO;
import DAO.UnitaOrganizzativaDAO;
import Entity.Ruolo;
import Entity.UnitaOrganizzativa;
import Utility.GestioneRuoloStrategy;
import Utility.RuoloFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UnitaOrganizzativaService {
    @Autowired
    private UnitaOrganizzativaDAO unitaOrganizzativaRepository;
    private GestioneRuoloStrategy gestioneRuoloStrategy;
    @Autowired
    private RuoloDAO ruoloDAO;

    public UnitaOrganizzativa createUnitaOrganizzativa(UnitaOrganizzativa unitaOrganizzativa) {
        return unitaOrganizzativaRepository.save(unitaOrganizzativa);
    }

    public List<UnitaOrganizzativa> getAllUnitaOrganizzative() {
        return unitaOrganizzativaRepository.findAll();
    }

    public Optional<UnitaOrganizzativa> getUnitaOrganizzativaById(Long id) {
        return unitaOrganizzativaRepository.findById(id);
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
