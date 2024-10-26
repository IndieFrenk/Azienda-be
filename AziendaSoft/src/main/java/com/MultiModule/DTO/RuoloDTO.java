package com.MultiModule.DTO;

import lombok.Data;

import java.util.List;

@Data
public class RuoloDTO {
        private Long id;
        private String nome;
        private Long unitaOrganizzativaId;
        private List<Long> dipendentiId;
}
