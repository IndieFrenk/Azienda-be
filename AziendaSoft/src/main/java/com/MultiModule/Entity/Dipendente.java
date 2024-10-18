package com.MultiModule.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class Dipendente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;

    @ManyToMany
    @JoinTable(name = "dipendente_ruolo",
            joinColumns = @JoinColumn(name = "dipendente_id"),
            inverseJoinColumns = @JoinColumn(name = "ruolo_id"))
    private List<Ruolo> ruoli;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unita_organizzativa_id")  // Relazione con UnitaOrganizzativa
    private UnitaOrganizzativa unitaOrganizzativa;
}
