package Controller;


import Entity.UnitaOrganizzativa;
import Service.UnitaOrganizzativaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/control")
public class ControlloAzienda {

    @Autowired
    private UnitaOrganizzativaService unitaOrganizzativaService;

    @GetMapping
    public ResponseEntity<List<UnitaOrganizzativa>> getAllUnitaOrganizzative() {
        return ResponseEntity.ok(unitaOrganizzativaService.getAllUnitaOrganizzative());
    }

    @PostMapping
    public ResponseEntity<UnitaOrganizzativa> createUnitaOrganizzativa(@RequestBody UnitaOrganizzativa unitaOrganizzativa) {
        return ResponseEntity.ok(unitaOrganizzativaService.createUnitaOrganizzativa(unitaOrganizzativa));
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
}
