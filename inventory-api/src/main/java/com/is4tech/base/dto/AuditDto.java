package com.is4tech.base.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AuditDto {
    private String entity;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime dateTime;
    private String operation;
    private String request;
    private String response;
    private Integer httpStatusCode;
    private String url;
    private Integer userId;
}
