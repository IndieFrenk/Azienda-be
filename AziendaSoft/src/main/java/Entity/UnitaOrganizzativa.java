package Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class UnitaOrganizzativa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;

    @ManyToOne
    @JoinColumn(name = "unita_superiore_id")  // Foreign key che rappresenta l'unità padre
    private UnitaOrganizzativa unitaSuperiore;

    // Relazione uno-a-molti: Un'unità organizzativa può avere più unità figlie
    @OneToMany(mappedBy = "unitaSuperiore", cascade = CascadeType.ALL)
    private List<UnitaOrganizzativa> unitaSottostanti;


    @OneToMany(mappedBy = "unitaOrganizzativa", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Ruolo> ruoli;

    @OneToMany(mappedBy = "unitaOrganizzativa", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Dipendente> dipendenti;

}
