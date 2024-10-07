package com.is4tech.base.controller;

import com.is4tech.base.domain.Winery;
import com.is4tech.base.dto.ApiResponse;
import com.is4tech.base.dto.WineryDto;
import com.is4tech.base.service.AuditService;
import com.is4tech.base.service.WineryService;
import com.is4tech.base.util.Utilities;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/winery")
@RequiredArgsConstructor
public class WineryController {

    private final WineryService wineryService;
    private final AuditService auditService;
    private static final String ENTIDAD = "Winery";

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createWinery(HttpServletRequest servletRequest, HttpServletResponse response, @RequestBody WineryDto wineryDto) {
        try {
            auditService.createAudit(servletRequest, response, ENTIDAD, String.valueOf(wineryDto), "Winery created successfully");
            Winery createWinery = wineryService.saveWinery(wineryDto);
            Utilities.infoLog(servletRequest, HttpStatus.CREATED, "Winery created");
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("Winery added successfully", createWinery));
        } catch (Exception e) {
            auditService.createAudit(servletRequest, response, ENTIDAD, String.valueOf(wineryDto), "Error creating winery");
            Utilities.errorLog(servletRequest, HttpStatus.INTERNAL_SERVER_ERROR, "Error creating winery", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Error adding winery", e.getMessage()));
        }
    }

    @PutMapping("/update/{wineryId}")
    public ResponseEntity<ApiResponse> updateWinery(HttpServletRequest servletRequest, HttpServletResponse response, @PathVariable("wineryId") Integer id, @RequestBody WineryDto wineryDto) {
        try {
            auditService.createAudit(servletRequest, response, ENTIDAD, String.valueOf(wineryDto), "Winery updated");
            Winery updateWinery = wineryService.updateWinery(wineryDto, id);
            Utilities.infoLog(servletRequest, HttpStatus.OK, "Winery updated");
            return ResponseEntity.ok(new ApiResponse("Winery updated successfully", updateWinery));
        } catch (Exception e) {
            auditService.createAudit(servletRequest, response, ENTIDAD, String.valueOf(wineryDto), "Error updating winery by id");
            Utilities.errorLog(servletRequest, HttpStatus.INTERNAL_SERVER_ERROR, "Error updating winery", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Error updating winery", e.getMessage()));
        }
    }

    @DeleteMapping("/delete/{wineryId}")
    public ResponseEntity<ApiResponse> deleteWinery(HttpServletRequest servletRequest, HttpServletResponse response, @PathVariable("wineryId") Integer id) {
        try {
            auditService.createAudit(servletRequest, response, ENTIDAD, String.valueOf(id), "Winery deleted");
            wineryService.deleteWinery(id);
            Utilities.infoLog(servletRequest, HttpStatus.OK, "Winery deleted");
            return ResponseEntity.ok(new ApiResponse("Winery deleted successfully", null));
        } catch (Exception e) {
            auditService.createAudit(servletRequest, response, ENTIDAD, String.valueOf(id), "Error deleting winery by id");
            Utilities.errorLog(servletRequest, HttpStatus.INTERNAL_SERVER_ERROR, "Error deleting winery", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Error deleting winery", e.getMessage()));
        }
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponse> getWineries(HttpServletRequest servletRequest, HttpServletResponse response) {
        try {
            List<Winery> wineries = wineryService.getWineries();
            Utilities.infoLog(servletRequest, HttpStatus.OK, "Wineries fetched");
            return ResponseEntity.ok(new ApiResponse("Wineries list fetched successfully", wineries));
        } catch (Exception e) {
            auditService.createAudit(servletRequest, response, ENTIDAD, null, "Error fetching wineries list");
            Utilities.errorLog(servletRequest, HttpStatus.INTERNAL_SERVER_ERROR, "Error fetching wineries", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Error fetching wineries", e.getMessage()));
        }
    }

    @GetMapping("/listId/{wineryId}")
    public ResponseEntity<ApiResponse> getWineryById(HttpServletRequest servletRequest, HttpServletResponse response, @PathVariable("wineryId") Integer id) {
        try {
            Winery winery = wineryService.getWineryId(id);
            Utilities.infoLog(servletRequest, HttpStatus.OK, "Winery fetched with ID: " + id);
            return ResponseEntity.ok(new ApiResponse("Winery successfully obtained", winery));
        } catch (Exception e) {
            auditService.createAudit(servletRequest, response, ENTIDAD, String.valueOf(id), "Error fetching winery by ID");
            Utilities.errorLog(servletRequest, HttpStatus.INTERNAL_SERVER_ERROR, "Error fetching winery", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Error fetching winery", e.getMessage()));
        }
    }
}
