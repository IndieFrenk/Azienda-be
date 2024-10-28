package com.MultiModule.Controller;


import com.MultiModule.DAO.RuoloDAO;
import com.MultiModule.DTO.DipendenteDTO;
import com.MultiModule.DTO.RuoloDTO;
import com.MultiModule.DTO.UnitaOrganizzativaDTO;
import com.MultiModule.Entity.Dipendente;
import com.MultiModule.Entity.Ruolo;
import com.MultiModule.Entity.UnitaOrganizzativa;
import com.MultiModule.Service.UnitaOrganizzativaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/azienda")
public class ControlloAzienda {

    @Autowired
    private UnitaOrganizzativaService unitaOrganizzativaService;
    @Autowired
    private RuoloDAO ruoloDAO;

    @GetMapping
    public ResponseEntity<List<UnitaOrganizzativaDTO>> getAllUnitaOrganizzative() {
        List<UnitaOrganizzativa> unitaOrganizzative = unitaOrganizzativaService.getAllUnitaOrganizzative();
        List<UnitaOrganizzativaDTO> dtos = unitaOrganizzative.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    private UnitaOrganizzativaDTO convertToDTO(UnitaOrganizzativa unita) {
        return unitaOrganizzativaService.convertToDTO(unita);
    }

    @PostMapping
    public ResponseEntity<UnitaOrganizzativa> createUnitaOrganizzativa(@RequestBody UnitaOrganizzativaDTO unitaOrganizzativaDTO) {
        return ResponseEntity.ok(unitaOrganizzativaService.createUnitaOrganizzativa(unitaOrganizzativaDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UnitaOrganizzativa> getUnitaOrganizzativaById(@PathVariable Long id) {
        return unitaOrganizzativaService.getUnitaOrganizzativaById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<UnitaOrganizzativa> updateUnitaOrganizzativa(@PathVariable Long id, @RequestBody UnitaOrganizzativaDTO unitaOrganizzativaDTO) {
        return ResponseEntity.ok(unitaOrganizzativaService.updateUnitaOrganizzativa(id, unitaOrganizzativaDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUnitaOrganizzativa(@PathVariable Long id) {
        unitaOrganizzativaService.deleteUnitaOrganizzativa(id);
        return ResponseEntity.ok().build();
    }




    // Dipendenti management endpoints
   /* @PostMapping("/dipendenti/create")
    public ResponseEntity<Dipendente> aggiungiDipendente(@RequestBody DipendenteDTO dipendente) {
        return ResponseEntity.ok(unitaOrganizzativaService.creaDipendente(dipendente));
    }*/

    @PostMapping("/dipendenti/create-with-roles-and-unit")
    public ResponseEntity<Dipendente> creaDipendenteConRuoliEUnita(@RequestBody DipendenteDTO dipendenteDTO) {
        Dipendente dipendente = unitaOrganizzativaService.creaDipendenteConRuoliEUnita(dipendenteDTO);
        return ResponseEntity.ok(dipendente);
    }
    @PostMapping("/dipendenti/{unitaId}/{dipendenteId}")
    public ResponseEntity<UnitaOrganizzativa> aggiungiDipendente(@PathVariable Long unitaId, @PathVariable Long dipendenteId) {
        return ResponseEntity.ok(unitaOrganizzativaService.aggiungiDipendente(unitaId, dipendenteId));
    }

    @DeleteMapping("/dipendenti/{unitaId}/{dipendenteId}")
    public ResponseEntity<UnitaOrganizzativa> rimuoviDipendente(@PathVariable Long unitaId, @PathVariable Long dipendenteId) {
        return ResponseEntity.ok(unitaOrganizzativaService.rimuoviDipendente(unitaId, dipendenteId));
    }

    @PostMapping("/dipendenti/trasferisci/{dipendenteId}/da/{unitaDaId}/a/{unitaAId}")
    public ResponseEntity<Void> trasferisciDipendente(
            @PathVariable Long dipendenteId, @PathVariable Long unitaDaId, @PathVariable Long unitaAId) {
        unitaOrganizzativaService.trasferisciDipendente(dipendenteId, unitaDaId, unitaAId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{unitaId}/{ruoloId}/dipendenti")
    public ResponseEntity<List<Dipendente>> getDipendentiUnita(@PathVariable Long unitaId, @PathVariable Long ruoloId) {
        return ResponseEntity.ok(unitaOrganizzativaService.getDipendentiUnita(unitaId, ruoloId));
    }


    // Ruoli management endpoints
    @PostMapping("/{unitaId}/ruoli")
    public ResponseEntity<Ruolo> aggiungiRuolo(@PathVariable Long unitaId, @RequestBody String nomeRuolo) {
        return ResponseEntity.ok(unitaOrganizzativaService.aggiungiRuolo(unitaId, nomeRuolo));
    }

    @DeleteMapping("/{unitaId}/ruoli/{ruoloId}")
    public ResponseEntity<Void> rimuoviRuolo(@PathVariable Long unitaId, @PathVariable Long ruoloId) {
        unitaOrganizzativaService.rimuoviRuolo(unitaId, ruoloId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/ruoli/{ruoloId}")
    public ResponseEntity<Ruolo> aggiornaRuolo(@PathVariable Long ruoloId, @RequestBody String nuovoNome) {
        return ResponseEntity.ok(unitaOrganizzativaService.aggiornaRuolo(ruoloId, nuovoNome));
    }

    @GetMapping("/{unitaId}/ruoli")
    public ResponseEntity<List<Ruolo>> getRuoliUnita(@PathVariable Long unitaId) {
        return ResponseEntity.ok(unitaOrganizzativaService.getRuoliUnita(unitaId));
    }

    @GetMapping("/ruolo")
    public ResponseEntity<List<Ruolo>> getRuoli() {
        return ResponseEntity.ok(unitaOrganizzativaService.getRuoli());
    }

    @PostMapping("/ruoli/{ruoloId}/dipendenti/{dipendenteId}")
    public ResponseEntity<Ruolo> assegnaRuoloADipendente(
            @PathVariable Long ruoloId, @PathVariable Long dipendenteId) {
        return ResponseEntity.ok(unitaOrganizzativaService.assegnaRuoloADipendente(ruoloId, dipendenteId));
    }

    @PostMapping("/{unitaId}/ruoli/create")
    public ResponseEntity<Ruolo> createRuoloPerUnita(
            @PathVariable Long unitaId,
            @RequestBody String tipoRuolo) {
        Optional<UnitaOrganizzativa> unitaOpt = unitaOrganizzativaService.getUnitaOrganizzativaById(unitaId);
        if (unitaOpt.isPresent()) {
            Ruolo nuovoRuolo = unitaOrganizzativaService.createRuoloPerUnita(tipoRuolo, unitaOpt.get());
            return ResponseEntity.ok(nuovoRuolo);
        }
        return ResponseEntity.notFound().build();
    }
    //crea ruolo
    @PostMapping("/ruoli/create")
    public ResponseEntity<Ruolo> createRuoloPerUnita(@RequestBody RuoloDTO ruolo) {
        Ruolo ruolo1 = new Ruolo();
        ruolo1.setNome(ruolo.getNome());
        ruoloDAO.save(ruolo1);
        return ResponseEntity.ok(ruolo1);
    }

}
