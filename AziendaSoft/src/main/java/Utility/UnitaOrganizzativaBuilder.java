package Utility;

import Entity.Dipendente;
import Entity.Ruolo;
import Entity.UnitaOrganizzativa;

import java.util.ArrayList;
import java.util.List;

public class UnitaOrganizzativaBuilder {
    private String nome;
    private List<Ruolo> ruoli = new ArrayList<>();
    private List<Dipendente> dipendenti = new ArrayList<>();

    public UnitaOrganizzativaBuilder setNome(String nome) {
        this.nome = nome;
        return this;
    }

    public UnitaOrganizzativaBuilder addRuolo(Ruolo ruolo) {
        this.ruoli.add(ruolo);
        return this;
    }

    public UnitaOrganizzativaBuilder addDipendente(Dipendente dipendente) {
        this.dipendenti.add(dipendente);
        return this;
    }

    public UnitaOrganizzativa build() {
        UnitaOrganizzativa unita = new UnitaOrganizzativa();
        unita.setNome(nome);
        unita.setRuoli(ruoli);
        unita.setDipendenti(dipendenti);
        return unita;
    }
}
