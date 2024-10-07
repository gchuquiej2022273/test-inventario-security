package com.is4tech.base.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Table(name = "audit")
@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Audit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "audit_id", nullable = false)
    private Integer id;

    @Column(name = "entity", nullable = false, length = 130)
    private String entity;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm")
    @Column(name = "date_time")
    private LocalDateTime dateTime;

    @Column(name = "operation", length = 70)
    private String operation;

    @Column(name = "request")
    private String  request;

    @Column(name = "response")
    private String response;

    @Column(name = "http_status_code")
    private Integer httpStatusCode;

    @Column(name = "url", length = 255)
    private String url;

    @Column(name = "username")
    private String username;
}