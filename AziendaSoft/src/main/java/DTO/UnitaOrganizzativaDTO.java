package DTO;


import lombok.Data;

import java.util.List;

@Data
public class UnitaOrganizzativaDTO {
    private Long id;
    private String nome;
    private List<String> ruoli;
    private List<String> dipendenti;
    private String unitaSuperiore;
    private List<String> unitaSottostanti;

}
