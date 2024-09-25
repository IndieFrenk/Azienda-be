package com.MultiModule.feedback.DTO;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RispostaDTO {
    private String contenuto;
    private LocalDateTime dataSottomissione;
    private long user_id;
    private long feedback_id;

}