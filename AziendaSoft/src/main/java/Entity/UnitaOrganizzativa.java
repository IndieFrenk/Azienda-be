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

    @OneToMany(mappedBy = "unitaOrganizzativa", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Ruolo> ruoli;

    @OneToMany(mappedBy = "unitaOrganizzativa", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Dipendente> dipendenti;
}
