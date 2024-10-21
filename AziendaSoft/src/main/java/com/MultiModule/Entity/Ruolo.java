package com.MultiModule.Entity;

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
    @ManyToOne
    @JoinColumn(name = "unita_organizzativa_id")
    private UnitaOrganizzativa unitaOrganizzativa;

    // Relazione molti-a-molti con Dipendente
    @ManyToMany(mappedBy = "ruoli")
    private List<Dipendente> dipendenti;

    public Ruolo(String nome, UnitaOrganizzativa unitaOrganizzativa) {
        this.nome = nome;
        this.unitaOrganizzativa = unitaOrganizzativa;
    }

    public Ruolo() {
    }
}
