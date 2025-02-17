package com.message.server.data.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "messages")
@Builder
public class MessageModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "message_from", referencedColumnName = "username", nullable = false) // ✅ Explicitly reference UserModel.id
    private UserModel from;

    @ManyToOne
    @JoinColumn(name = "message_to", referencedColumnName = "username", nullable = true) // ✅ Reference UserModel.id for private messages
    private UserModel to;

    @Column(nullable = false)
    private String text;

    private Boolean seen;

    private Date created;

    private String type;
}
