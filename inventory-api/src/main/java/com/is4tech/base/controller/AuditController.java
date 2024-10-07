package com.is4tech.base.controller;

import com.is4tech.base.domain.Audit;
import com.is4tech.base.dto.ApiResponse;
import com.is4tech.base.service.AuditService;
import com.is4tech.base.util.Utilities;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/audit")
@RequiredArgsConstructor
public class AuditController {

    private final AuditService auditService;

    @GetMapping("/list")
    public ResponseEntity<ApiResponse> getAuditorio(HttpServletRequest request){
        try{
            List<Audit> audits = auditService.getfindAudit();
            Utilities.infoLog(request, HttpStatus.OK, "Se obtuvieron las Auditorias");
            return ResponseEntity.ok(new ApiResponse("Se logro obter los datos :", audits));
        } catch (Exception e) {
            Utilities.errorLog(request, HttpStatus.INTERNAL_SERVER_ERROR, "Error getting profiles", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Error al obtener la Auditoria", e.getMessage()));
        }
    }
}
