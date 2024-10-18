package com.MultiModule.DAO;

import com.MultiModule.Entity.Ruolo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RuoloDAO extends JpaRepository<Ruolo, Long> {
    Ruolo findByNome(String nome);
}
