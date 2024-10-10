package com.MultiModule.feedback.Entity;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Getter @Setter

@RequiredArgsConstructor
public class FeedbackEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(length=1500)
    private String contenuto;
    private String titolo;
    private long userid;
    @Column(name = "stato", nullable = false)
    private boolean stato; //true, thread aperto, false, thread chiuso
    @JsonManagedReference
    @OneToMany(mappedBy = "feedback", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<RispostaEntity> risposte = new ArrayList<>();
    @ManyToMany
    @JoinTable(
            name = "feedback_contesto",
            joinColumns = @JoinColumn(name= "feedback_id"),
            inverseJoinColumns = @JoinColumn(name = "contesto_id")
    )

    private List<ContestoEntity> contesto;
    private  String Email;
    private LocalDateTime dataSottomissione;

    public void addContesto(Optional<ContestoEntity> c){
        contesto.add(c.get());
    }
    public void addRisposte(RispostaEntity risposta){
        risposte.add(risposta);
        risposta.setFeedback(this);
    }
    public boolean changeStatus(){
        this.stato = !this.stato;
        return stato;
    }
}
