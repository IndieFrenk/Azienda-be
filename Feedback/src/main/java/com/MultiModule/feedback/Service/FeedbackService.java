package com.MultiModule.feedback.Service;

import com.MultiModule.User.DAO.UserDAO;
import com.MultiModule.feedback.DAO.ContestoDAO;
import com.MultiModule.feedback.DAO.FeedbackDAO;
import com.MultiModule.feedback.DTO.FeedbackDTO;
import com.MultiModule.feedback.DTO.SearchDTO;
import com.MultiModule.feedback.Entity.ContestoEntity;
import com.MultiModule.feedback.Entity.FeedbackEntity;
import com.MultiModule.feedback.Specs.FeedbackSpecs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static com.MultiModule.feedback.Specs.FeedbackSpecs.contenutoContains;

@Service
@Slf4j
public class FeedbackService {


    @Autowired
    private FeedbackDAO feedbackDAO;

    @Autowired
    private ContestoDAO contestoDAO;

    @Autowired
    private UserDAO userDAO;
    public FeedbackEntity invioFeedback(FeedbackDTO feedbackDTO) {
        //pattern builder
        FeedbackEntity feedback = new FeedbackEntity();
        feedback.setContenuto(feedbackDTO.getContenuto());
        feedback.setEmail(feedbackDTO.getEmail());
        feedback.setDataSottomissione(LocalDateTime.now());
        feedback.setRisposte(new ArrayList<>());
        feedback.setTitolo(feedbackDTO.getTitolo());
        feedback.setUserid(userDAO.findByEmail(feedbackDTO.getEmail()).getId());
        feedback.setStato(true);
        ContestoEntity contesto = contestoDAO.findByDefinizione(feedbackDTO.getContesti());
        List<ContestoEntity> contesti = new ArrayList<>();
        contesti.add(contesto);
        feedback.setContesto(contesti);
        feedbackDAO.save(feedback);
        return feedback;
    }

    public void cancellaFeedback(long id) {
        feedbackDAO.deleteById(id);
    }

    public List<FeedbackEntity> searchFeedback(SearchDTO searchDTO){
        OffsetDateTime parsedData = null;
        if (searchDTO.getData() != null) {
            parsedData = OffsetDateTime.parse(searchDTO.getData(), DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        }
        Specification<FeedbackEntity> spec = Specification.where(contenutoContains(searchDTO.getContenuto())).and(FeedbackSpecs.dataSottomissioneContains(parsedData.toLocalDateTime()));
        if (searchDTO.getId() != 0) {
            spec = spec.and(FeedbackSpecs.idEquals(searchDTO.getId()));
        }
       return feedbackDAO.findAll(spec);
    }

    public List<FeedbackEntity> findByUser(Long id) {
        return feedbackDAO.findByUserid(id);

    }

    public void cancellaContesto(long id) {
        contestoDAO.deleteById(id);

    }
}
