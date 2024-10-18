package com.MultiModule.Controller;


import com.MultiModule.DTO.UnitaOrganizzativaDTO;
import com.MultiModule.Entity.UnitaOrganizzativa;
import com.MultiModule.Service.UnitaOrganizzativaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/unita")
public class ControlloAzienda {

    @Autowired
    private UnitaOrganizzativaService unitaOrganizzativaService;

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
    public ResponseEntity<UnitaOrganizzativa> updateUnitaOrganizzativa(@PathVariable Long id, @RequestBody UnitaOrganizzativa unitaOrganizzativa) {
        return ResponseEntity.ok(unitaOrganizzativaService.updateUnitaOrganizzativa(id, unitaOrganizzativa));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUnitaOrganizzativa(@PathVariable Long id) {
        unitaOrganizzativaService.deleteUnitaOrganizzativa(id);
        return ResponseEntity.ok().build();
    }
    //creazione dipendenti
    //creazione ruolo
}
