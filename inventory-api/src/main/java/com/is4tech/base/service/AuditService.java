package com.is4tech.base.service;

import com.is4tech.base.domain.Audit;
import com.is4tech.base.domain.User;
import com.is4tech.base.dto.NewUserDto;
import com.is4tech.base.dto.RegisterUserDto;
import com.is4tech.base.exception.Exceptions;
import com.is4tech.base.repository.AuditRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuditService {

    private final AuditRepository auditRepository;


    //Para todos los usuarios que estas autenticados
    public void createAudit(HttpServletRequest request, HttpServletResponse response, String entity, String requestObject,String setResponse) {
        try {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                User user = (User) authentication.getPrincipal();

                Audit audit = new Audit();
                audit.setDateTime(LocalDateTime.now());
                audit.setEntity(entity);
                audit.setHttpStatusCode(response.getStatus());
                audit.setOperation(request.getMethod());
                audit.setRequest(requestObject);
                audit.setResponse(setResponse);
                audit.setUrl(request.getRequestURI());
                audit.setUsername(user.getUsername());

                auditRepository.save(audit);

        } catch (Exception e) {
            throw new RuntimeException("Authentication error", e);
        }
    }

    //para agregar un nuevo usuario
    public void createNewUserAudit (HttpServletRequest request, HttpServletResponse response, String entity, RegisterUserDto userRequest, String setResponse) {
        try {
            NewUserDto newUser = new NewUserDto();
            newUser.setName(userRequest.getName());
            newUser.setSurname(userRequest.getSurname());
            newUser.setEmail(userRequest.getEmail());
            newUser.setUsername(userRequest.getUsername());

            Audit audit = new Audit();
            audit.setDateTime(LocalDateTime.now());
            audit.setEntity(entity);
            audit.setHttpStatusCode(response.getStatus());
            audit.setOperation(request.getMethod());
            audit.setRequest(String.valueOf(newUser));
            audit.setResponse(setResponse);
            audit.setUrl(request.getRequestURI());
            audit.setUsername("new user");

            auditRepository.save(audit);

        } catch (Exception e) {
            throw new RuntimeException("Error al crear la auditoría new user", e);
        }
    }

    public List<Audit> getfindAudit(){
            List<Audit> auditList = auditRepository.findAll();
            if (auditList.isEmpty()){
                throw new Exceptions("Audit not found");
            }
            return auditList;
    }
}
