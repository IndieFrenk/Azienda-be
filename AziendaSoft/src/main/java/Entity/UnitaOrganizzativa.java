package Entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

public class UnitaOrganizzativa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;

    @OneToMany(mappedBy = "unitaOrganizzativa")
    private List<Ruolo> ruoli;

    @OneToMany(mappedBy = "unitaOrganizzativa")
    private List<Dipendente> dipendenti;
}
