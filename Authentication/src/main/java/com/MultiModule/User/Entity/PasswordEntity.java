package com.MultiModule.User.Entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Data
@RequiredArgsConstructor
public class PasswordEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String password;
    private LocalDateTime dataCreazione;
    private long userId;

    public PasswordEntity(String password, LocalDateTime dataCreazione, long UserId){
        this.password = password;
        this.dataCreazione = dataCreazione;
        this.userId = UserId;
    }
}
