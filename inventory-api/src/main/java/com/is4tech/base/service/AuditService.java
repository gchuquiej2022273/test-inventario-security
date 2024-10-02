package com.is4tech.base.service;

import com.is4tech.base.domain.Audit;
import com.is4tech.base.repository.AuditRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AuditService {

    @Autowired
    private AuditRepository auditRepository;


    public void createAudit(HttpServletRequest request, HttpServletResponse response, String entity, String requestObject, Integer userId, String setResponse) {
        try {

            Audit audit = new Audit();
            audit.setDateTime(LocalDateTime.now());
            audit.setEntity(entity);
            audit.setHttpStatusCode(response.getStatus());
            audit.setOperation(request.getMethod());
            audit.setRequest(requestObject);
            audit.setResponse(setResponse);
            audit.setUrl(request.getRequestURI());
            audit.setUserId(userId);
            System.out.println("Inserting audit: " + audit);

            auditRepository.save(audit);
        } catch (Exception e) {
            throw new RuntimeException("Error al crear la auditoría", e);
        }
    }

    public List<Audit> getfindAudit(){
        try{
            return (List<Audit>) auditRepository.findAll();
        }catch (Exception e){
            throw new RuntimeException("Error al tratar de obtener la auditoria");
        }
    }
}
