package com.MultiModule.feedback.Controller;

import com.MultiModule.User.DAO.UserDAO;
import com.MultiModule.User.Entity.UserEntity;
import com.MultiModule.feedback.DAO.ContestoDAO;
import com.MultiModule.feedback.DAO.FeedbackDAO;
import com.MultiModule.feedback.DAO.RispostaDAO;
import com.MultiModule.feedback.DTO.ContestoDTO;
import com.MultiModule.feedback.DTO.FeedbackDTO;
import com.MultiModule.feedback.DTO.RispostaDTO;
import com.MultiModule.feedback.DTO.SearchDTO;
import com.MultiModule.feedback.Entity.ContestoEntity;
import com.MultiModule.feedback.Entity.FeedbackEntity;
import com.MultiModule.feedback.Entity.RispostaEntity;
import com.MultiModule.feedback.Service.ContestoService;
import com.MultiModule.feedback.Service.FeedbackService;
import com.MultiModule.feedback.Service.RispostaService;
import jakarta.mail.MessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/feedback")
public class FeedbackController {

    private static final Logger log = LoggerFactory.getLogger(FeedbackController.class);
    @Autowired
    private FeedbackService feedbackService;
    @Autowired
    private ContestoService contestoService;
    @Autowired
    private RispostaService rispostaService;
    @Autowired
    private FeedbackDAO feedbackDAO;
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private ContestoDAO contestoDAO;
    @Autowired
    private RispostaDAO rispostaDAO;

    @PostMapping("/createContesto")
    public ResponseEntity<?>  creaContesto(@RequestBody ContestoDTO contesto) {
        var cont = contestoService.invioContesto(contesto);
        return ResponseEntity.ok(cont);
    }
    @PostMapping("/send")
    public ResponseEntity<?> invioFeedback(@RequestBody FeedbackDTO feedbackDTO){
        var feedback =  feedbackService.invioFeedback(feedbackDTO);
        return ResponseEntity.ok(feedback);
    }
    @DeleteMapping("contesto/delete/{id}")
    public ResponseEntity<?> cancellaContesto(@PathVariable long id){
        feedbackService.cancellaContesto(id);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> cancellaFeedback(@PathVariable long id){
        feedbackService.cancellaFeedback(id);
        return ResponseEntity.ok(id);
    }

    @PostMapping("/search")
    public ResponseEntity<?> cercaFeedback(@RequestBody SearchDTO searchDTO) {
        if(searchDTO.getData() == "") {
            searchDTO.setData( "1980-07-29T09:36:06.1728899Z");
        }
        List<FeedbackEntity> feedList = feedbackService.searchFeedback(searchDTO);
        return ResponseEntity.ok(feedList);
    }
    @PreAuthorize("hasRole('Admin')")
    @PostMapping("/status/{id}")
    public ResponseEntity<?> statusFeedback(@PathVariable long id) {
        FeedbackEntity feedback = feedbackDAO.findById(id).get();
        feedback.changeStatus();
        feedbackDAO.save(feedback);
        return ResponseEntity.ok(feedback);
    }
    @PreAuthorize("hasRole('Admin')")
    @GetMapping("/get")
    public ResponseEntity<?> getFeedback() {

        List<FeedbackEntity> feedList = feedbackDAO.findAll();
        return ResponseEntity.ok(feedList);
    }
    @PreAuthorize("hasRole('Admin')")
    @GetMapping("/users")
    public ResponseEntity<?> getUsers() {

        List<UserEntity> userList = userDAO.findAll();
        return ResponseEntity.ok(userList);
    }
    @GetMapping("/getByUser/{id}")
    public ResponseEntity<?> getFeedbackByUser(@PathVariable long id) {
        List<FeedbackEntity> feedList = feedbackService.findByUser(id);
        return ResponseEntity.ok(feedList);
    }

    @GetMapping("/getContesto")
    public List<ContestoEntity> findAll(){
        return (List<ContestoEntity>) contestoDAO.findAll();
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getFeedbackUser(@PathVariable Long id) {
        Optional<FeedbackEntity> feed = feedbackDAO.findById(id);
        return ResponseEntity.ok(feed);
    }

    @PostMapping("/sendRisposta")
    public ResponseEntity<?> invioRisposta(@RequestBody RispostaDTO rispostaDTO) throws MessagingException, IOException {
        var risposta =  rispostaService.invioRisposta(rispostaDTO);
        return ResponseEntity.ok(risposta);
    }
    @DeleteMapping("/deleteRisposta/{id}")
    public ResponseEntity<?> cancellaRisposta(@PathVariable long id){
        rispostaService.cancellaRisposta(id);
        return ResponseEntity.ok("cancellata");
    }
    @GetMapping("/getRisposte/{id}")
    public ResponseEntity<?> getRisposte(@PathVariable Long id) {
        List<RispostaEntity> risposte = rispostaDAO.findByFeedback(feedbackDAO.findById(id).get());
        return ResponseEntity.ok(risposte);
    }


}
