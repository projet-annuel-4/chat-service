package fr.esgi.chat.data.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Entity
    @Table(name = "groups")
    public class GroupEntity {
        @Id
        private Long id;
        private String name;
        @OneToMany(mappedBy = "id", cascade = CascadeType.ALL)
        private Set<UserEntity> members = new HashSet<>();
    }


