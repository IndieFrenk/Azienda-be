package com.MultiModule.DAO;

import com.MultiModule.Entity.Dipendente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DipendenteDAO extends JpaRepository<Dipendente, Long> {
    Dipendente findByNome(String nome);
}
