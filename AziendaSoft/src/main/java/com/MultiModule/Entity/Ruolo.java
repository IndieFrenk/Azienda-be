package com.MultiModule.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Ruolo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;

    @ManyToOne
    @JoinColumn(name = "unita_organizzativa_id")
    private UnitaOrganizzativa unitaOrganizzativa;
}
