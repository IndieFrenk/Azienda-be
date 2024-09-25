package com.MultiModule.User.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;

@Entity
public class Privilege {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    @ManyToMany(mappedBy = "privileges")
    private Collection<RoleEntity> roles;
    public Privilege() {
        super();
    }
    public Privilege(String name) {
        super();
        this.name = name;
    }

}