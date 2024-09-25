package com.MultiModule.feedback.DTO;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FeedbackDTO {
    public String titolo;
    private String contenuto;
    private String email;
    private String contesti ;
    private long risposta_id;
    private LocalDateTime dataSottomissione;
    private String user;
}