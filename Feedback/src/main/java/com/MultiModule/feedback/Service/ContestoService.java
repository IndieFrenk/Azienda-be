package com.MultiModule.feedback.Service;

import com.MultiModule.feedback.DAO.ContestoDAO;
import com.MultiModule.feedback.DAO.RispostaDAO;
import com.MultiModule.feedback.DTO.ContestoDTO;
import com.MultiModule.feedback.Entity.ContestoEntity;
import com.MultiModule.feedback.Entity.RispostaEntity;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContestoService {
    @Autowired
    private ContestoDAO contestoDAO;

    public ContestoEntity invioContesto(ContestoDTO contestoDTO) {
        val contesto = new ContestoEntity();
        contesto.setDefinizione(contestoDTO.getDefinizione());
        return contestoDAO.save(contesto);
    }
}
