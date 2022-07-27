package fr.esgi.chat.domain.model;

import fr.esgi.chat.data.entity.UserEntity;
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
    public class GroupModel {
        private Long id;
        private String name;
        private Set<UserModel> members = new HashSet<>();
    }


