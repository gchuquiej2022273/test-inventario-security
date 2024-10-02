package com.is4tech.base.controller;

import com.is4tech.base.domain.Winery;
import com.is4tech.base.dto.ApiResponse;
import com.is4tech.base.dto.WineryDto;
import com.is4tech.base.service.AuditService;
import com.is4tech.base.service.WineryService;
import com.is4tech.base.util.Utilities;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/winery")
public class WineryController {

    @Autowired
    private WineryService wineryService;

    @Autowired
    private AuditService auditService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createWinery(HttpServletRequest servletRequest, HttpServletResponse response, @RequestBody WineryDto wineryDto) {
        try {
            Winery createWinery = wineryService.saveWinery(wineryDto);
            Utilities.infoLog(servletRequest, HttpStatus.CREATED, "Winery created successfully");
            auditService.createAudit(servletRequest, response, "Winery", wineryDto.getName(), createWinery.getUserId(), "Winery created successfully"); // Auditoría
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("Winery added successfully", createWinery));
        } catch (Exception e) {
            Utilities.errorLog(servletRequest, HttpStatus.INTERNAL_SERVER_ERROR, "Error adding winery", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Error adding winery", e.getMessage()));
        }
    }

    @PutMapping("/update/{wineryId}")
    public ResponseEntity<ApiResponse> updateWinery(HttpServletRequest servletRequest, HttpServletResponse response,@PathVariable("wineryId") Integer id, @RequestBody WineryDto wineryDto) {
        try {
            Winery updateWinery = wineryService.updateWinery(wineryDto, id);
            Utilities.infoLog(servletRequest, HttpStatus.OK, "Winery updated successfully");
            auditService.createAudit(servletRequest, response, "Winery", wineryDto.getName(), updateWinery.getUserId(), "Winery updated successfully"); // Auditoría
            return ResponseEntity.ok(new ApiResponse("Winery updated successfully", updateWinery));
        } catch (Exception e) {
            Utilities.errorLog(servletRequest, HttpStatus.INTERNAL_SERVER_ERROR, "Error updating winery", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Error updating winery", e.getMessage()));
        }
    }

    @DeleteMapping("/delete/{wineryId}")
    public ResponseEntity<ApiResponse> deleteWinery(HttpServletRequest servletRequest, HttpServletResponse response,@PathVariable("wineryId") Integer id) {
        try {
            wineryService.deleteWinery(id);
            Utilities.infoLog(servletRequest, HttpStatus.OK, "Winery deleted successfully");
            auditService.createAudit(servletRequest, response, "Winery", null, id, "Winery deleted successfully");
            return ResponseEntity.ok(new ApiResponse("Winery deleted successfully", null));
        } catch (Exception e) {
            Utilities.errorLog(servletRequest, HttpStatus.INTERNAL_SERVER_ERROR, "Error deleting winery", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Error deleting winery", e.getMessage()));
        }
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponse> getWineries(HttpServletRequest servletRequest) {
        try {
            List<Winery> wineries = wineryService.getWineries();
            Utilities.infoLog(servletRequest, HttpStatus.OK, "Fetched wineries list");
            return ResponseEntity.ok(new ApiResponse("Fetched wineries list:", wineries));
        }catch (Exception e){
            Utilities.errorLog(servletRequest, HttpStatus.INTERNAL_SERVER_ERROR, "Error getting wineries", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Error getting wineries", e.getMessage()));
        }
    }

    @GetMapping("/listId/{wineryId}")
    public ResponseEntity<ApiResponse> getWineryId(HttpServletRequest servletRequest, @PathVariable("wineryId") Integer id) {
        try {
            Winery winery = wineryService.getWineryId(id);
            Utilities.infoLog(servletRequest, HttpStatus.OK, "Winery found with ID:" + id);
            return ResponseEntity.ok(new ApiResponse("Winery successfully obtained", winery));
        } catch (Exception e) {
            Utilities.errorLog(servletRequest, HttpStatus.INTERNAL_SERVER_ERROR, "Error getting winery", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Error getting winery", e.getMessage()));
        }
    }
}
