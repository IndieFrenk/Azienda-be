package com.MultiModule.Entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
public class Ruolo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String nome;


    @ManyToMany
    @JsonManagedReference
    @JoinTable(
            name = "ruolo_unita_organizzativa",
            joinColumns = @JoinColumn(name = "ruolo_id"),
            inverseJoinColumns = @JoinColumn(name = "unita_organizzativa_id")
    )
    private Set<UnitaOrganizzativa> unitaOrganizzative = new HashSet<>();


    @ManyToMany(mappedBy = "ruoli")
    private List<Dipendente> dipendenti;

    public Ruolo(String nome, Set<UnitaOrganizzativa> unitaOrganizzativa) {
        this.nome = nome;
        this.unitaOrganizzative = unitaOrganizzativa;
    }

    public Ruolo() {
    }
}
