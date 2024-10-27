package com.MultiModule.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class Dipendente {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String nome;

    @ManyToMany
    @JsonIgnore
    @JoinTable(name = "dipendente_ruolo",
            joinColumns = @JoinColumn(name = "dipendente_id"),
            inverseJoinColumns = @JoinColumn(name = "ruolo_id"))
    private List<Ruolo> ruoli;

    @ManyToMany
    @JsonIgnore
    @JoinTable(
            name = "unita_dipendenti",
            joinColumns = @JoinColumn(name = "dipendente_id"),
            inverseJoinColumns = @JoinColumn(name = "unita_organizzativa_id")
    )
    private List<UnitaOrganizzativa> unitaOrganizzative;
}
