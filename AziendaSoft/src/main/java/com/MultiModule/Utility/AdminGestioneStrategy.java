package com.MultiModule.Utility;

import com.MultiModule.Entity.UnitaOrganizzativa;

public class AdminGestioneStrategy implements GestioneRuoloStrategy {

    @Override
    public void gestisciUnita(UnitaOrganizzativa unita) {
        System.out.println("Operazione consentita per l'amministratore.");
    }
}

