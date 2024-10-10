package com.MultiModule.feedback.Service;

import com.MultiModule.User.DAO.UserDAO;
import com.MultiModule.User.Service.EmailService;
import com.MultiModule.feedback.DAO.FeedbackDAO;
import com.MultiModule.feedback.DAO.RispostaDAO;
import com.MultiModule.feedback.DTO.RispostaDTO;
import com.MultiModule.feedback.Entity.FeedbackEntity;
import com.MultiModule.feedback.Entity.RispostaEntity;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
public class RispostaService {

    @Autowired
    private RispostaDAO rispostaDAO;
    @Autowired
    private FeedbackDAO feedbackDAO;
    private EmailService emailService;
    @Autowired
    private UserDAO userDAO;
    //usare i mapstruct
    public RispostaService(EmailService emailService
    ) {
        this.emailService = emailService;

    }
    public RispostaEntity invioRisposta(RispostaDTO rispostaDTO) throws MessagingException, IOException {
        //builder
        RispostaEntity risposta = new RispostaEntity();
        risposta.setContenuto(rispostaDTO.getContenuto());
        risposta.setDataSottomissione(LocalDateTime.now());
        risposta.setFeedback(feedbackDAO.findById(rispostaDTO.getFeedback_id()).get());
        risposta.setUser_id(rispostaDTO.getUser_id());

        risposta.setEmail(userDAO.findById(rispostaDTO.getUser_id()).get().getEmail());
        FeedbackEntity feedback = feedbackDAO.findById(rispostaDTO.getFeedback_id()).get();
        RispostaEntity newRisposta = rispostaDAO.save(risposta);
        feedback.addRisposte(newRisposta);
        feedbackDAO.save(feedback);

        emailService.sendAnswerEmail(feedback.getEmail(), feedback.getId());

        return newRisposta;
    }
    public void cancellaRisposta(long id) {rispostaDAO.deleteById(id);}
}
