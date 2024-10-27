package com.MultiModule.Entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class Ruolo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String nome;

    // Relazione molti-a-uno con UnitaOrganizzativa
    @ManyToMany
    @JsonManagedReference
    @JoinTable(
            name = "ruolo_unita_organizzativa",
            joinColumns = @JoinColumn(name = "ruolo_id"),
            inverseJoinColumns = @JoinColumn(name = "unita_organizzativa_id")
    )
    private List<UnitaOrganizzativa> unitaOrganizzative;

    // Relazione molti-a-molti con Dipendente
    @ManyToMany(mappedBy = "ruoli")
    private List<Dipendente> dipendenti;

    public Ruolo(String nome, List<UnitaOrganizzativa> unitaOrganizzativa) {
        this.nome = nome;
        this.unitaOrganizzative = unitaOrganizzativa;
    }

    public Ruolo() {
    }
}
