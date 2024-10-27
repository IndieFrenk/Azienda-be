package com.MultiModule.DTO;

import com.MultiModule.Entity.Ruolo;
import com.MultiModule.Entity.UnitaOrganizzativa;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
@Data
public class DipendenteDTO {
    private Long id;
    private String nome;
    private List<Long> ruoli;
    private Long unita;
}
