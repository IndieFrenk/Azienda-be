package com.MultiModule.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class UnitaOrganizzativa {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String nome;

    @ManyToOne
    @Nullable
    @JoinColumn(name = "unita_superiore_id")
    @JsonManagedReference
    private UnitaOrganizzativa unitaSuperiore;


    @OneToMany(mappedBy = "unitaSuperiore", cascade = CascadeType.ALL)
    @JsonBackReference
    private List<UnitaOrganizzativa> unitaSottostanti;


    @ManyToMany(mappedBy = "unitaOrganizzative")
    @JsonBackReference
    private List<Ruolo> ruoli;

    @ManyToMany(mappedBy = "unitaOrganizzative")
    private List<Dipendente> dipendenti;

}
