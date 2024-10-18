package Utility;

import Entity.UnitaOrganizzativa;

public class DipendenteGestioneStrategy implements GestioneRuoloStrategy {

    @Override
    public void gestisciUnita(UnitaOrganizzativa unita) {
        System.out.println("Operazione non consentita per i dipendenti.");
    }
}