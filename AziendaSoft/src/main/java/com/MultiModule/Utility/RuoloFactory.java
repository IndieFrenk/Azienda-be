package com.MultiModule.Utility;

import com.MultiModule.Entity.Ruolo;
import com.MultiModule.Entity.UnitaOrganizzativa;

import java.util.List;

public class RuoloFactory {

    public static Ruolo createRuolo(String tipoRuolo, List<UnitaOrganizzativa> unitaOrganizzativa) {
        Ruolo ruolo = new Ruolo();
        ruolo.setUnitaOrganizzative(unitaOrganizzativa);  // Associa il ruolo all'unità organizzativa

        switch (tipoRuolo) {
            case "Manager":
                ruolo.setNome("Manager");
                // Aggiungi altre caratteristiche specifiche del ruolo Manager se necessario
                return ruolo;
            case "Dipendente":
                ruolo.setNome("Dipendente");
                // Aggiungi altre caratteristiche specifiche del ruolo Dipendente se necessario
                return ruolo;
            default:
                throw new IllegalArgumentException("Tipo ruolo non supportato: " + tipoRuolo);
        }
    }
}
