package com.MultiModule.feedback.Entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Entity
@Data
@RequiredArgsConstructor
public class ContestoEntity {

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private long id;
        private String definizione;


}
