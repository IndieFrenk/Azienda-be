package com.MultiModule.DAO;

import com.MultiModule.Entity.UnitaOrganizzativa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UnitaOrganizzativaDAO extends JpaRepository<UnitaOrganizzativa, Long> {
    UnitaOrganizzativa findByNome(String nome);
}
