package fr.esgi.chat.data.entity;


import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "files", indexes = {@Index(name = "i_url", columnList = "url")})
public class FileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "files_id_seq")
    @SequenceGenerator(name = "files_id_seq", sequenceName = "files_id_seq", initialValue = 1, allocationSize = 1)
    private Long id;
    private String url;
    private String type;
    @ManyToOne
    @JoinColumn(
            name = "message_id",
            foreignKey = @ForeignKey(name = "fk_files_message"),
            nullable = false
    )
    private MessageEntity message;
}
