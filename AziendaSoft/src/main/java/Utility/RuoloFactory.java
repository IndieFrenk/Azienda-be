package Utility;

import Entity.Ruolo;

public class RuoloFactory {
    public static Ruolo createRuolo(String tipoRuolo) {
        Ruolo ruolo = new Ruolo();
        switch (tipoRuolo) {
            case "Manager":
                ruolo.setNome("Manager");
                return ruolo;
            case "Dipendente":
                ruolo.setNome("Dipendente");
                return ruolo;
            default:
                throw new IllegalArgumentException("Tipo ruolo non supportato.");
        }
    }
}
