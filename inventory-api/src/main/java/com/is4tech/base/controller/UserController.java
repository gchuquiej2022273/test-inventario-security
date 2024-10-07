package com.is4tech.base.controller;

import com.is4tech.base.domain.Tokens;
import com.is4tech.base.domain.User;
import com.is4tech.base.dto.*;
import com.is4tech.base.repository.UserRepository;
import com.is4tech.base.service.*;
import com.is4tech.base.util.Utilities;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenRecoverPasswordService tokenRecoverPasswordService;
    private static final String ENTIDAD = "user";
    private final AuditService auditService;
    private final UserService userService;
    private final EmailService emailService;

    @PostMapping("/recover-password")
    public ResponseEntity<ApiResponse> sendEmail(HttpServletRequest request, HttpServletResponse response, @RequestBody EmailDto email) {
        try {
            Optional<User> userOptional = userRepository.findByEmail(email.getEmail());
            if (userOptional.isEmpty()) {
                return ResponseEntity.badRequest().body(new ApiResponse("User not found with email : ",email.getEmail()));
            }
            User user = userOptional.get();
            String requestObject = user.getEmail();
            auditService.createAudit(request,response,ENTIDAD, requestObject, "Email enviado exitosamente");
            Tokens token = tokenRecoverPasswordService.generarTokenForRecoverPassword();
            emailService.sendEmail(email,token.getToken());
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ApiResponse("El token fue enviado con extito",user.getEmail()));
        } catch (Exception e) {
            auditService.createAudit(request,response,ENTIDAD, "sendEmail", "Error al enviar correo de recuperacion!");
            Utilities.errorLog(request, HttpStatus.INTERNAL_SERVER_ERROR, "Error al tratar de recuperar contraseña", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Error al tratar de recuperar su contraseña", e.getMessage()));
        }
    }

    @PutMapping("/update-password")
    public ResponseEntity<ApiResponse> updatePasswowrd(HttpServletRequest request, HttpServletResponse response, @RequestBody PasswordDto passwordDto) {
        try {
            boolean ok = tokenRecoverPasswordService.validarToken(passwordDto.getToken());
            if (ok){
                userService.updatePasswordByTokenMail(passwordDto);
            }else{
                return ResponseEntity.badRequest().body(new ApiResponse("Token no valio o ha expirado",request.getMethod()));
            }
            auditService.createAudit(request,response,ENTIDAD, passwordDto.getNewPassword(), "Actualizacion correcta");
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ApiResponse("Contraseña actualizada exitosamente.", passwordDto.getEmail()));
        } catch (Exception e) {
            auditService.createAudit(request,response,ENTIDAD, passwordDto.getEmail(), "Error al cambiar contraseña");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Error al actualizar la contraseña", e.getMessage()));
        }
    }

    @PatchMapping("/change-password")
    public ResponseEntity<ApiResponse> changePassword(@RequestBody ChangePasswordDto request, Principal principal, HttpServletRequest servletRequest, HttpServletResponse response) {
        try {
            auditService.createAudit(servletRequest, response, ENTIDAD, principal.getName(), "Contraseña actualizada exitosamente");
            userService.changePassword(request);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ApiResponse("Contraseña actualizada exitosamente", principal.getName()));
        } catch (Exception e) {
            auditService.createAudit(servletRequest, response, ENTIDAD, principal.getName(), "Error al cambiar la contraseña");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Ocurrió un error al tratar de actualizar la contraseña", e.getMessage()));
        }
    }


    @GetMapping("/getUsers")
    public ResponseEntity<ApiResponse> getActivateUser(HttpServletRequest request, HttpServletResponse response) {
        try {
            List<User> userList = userService.getUserStatusTrue();
            auditService.createAudit(request, response, ENTIDAD, null, "Lista de usuarios activos obtenida correctamente");
            Utilities.infoLog(request, HttpStatus.OK, "Fetched users list");
            return ResponseEntity.ok(new ApiResponse("Fetched users list", userList));
        } catch (Exception e) {
            auditService.createAudit(request, response, ENTIDAD, null, "Error al obtener la lista de usuarios activos");
            Utilities.errorLog(request, HttpStatus.INTERNAL_SERVER_ERROR, "Error getting users", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Error getting users", e.getMessage()));
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse> getUserById(HttpServletRequest request, HttpServletResponse response, @PathVariable("userId") Integer userId) {
        try {
            User user = userService.getById(userId);
            Utilities.infoLog(request, HttpStatus.OK, "User found with ID: " + userId);
            return ResponseEntity.ok(new ApiResponse("User successfully obtained", user));
        } catch (Exception e) {
            auditService.createAudit(request, response, ENTIDAD, userId.toString(), "Error al obtener el usuario");
            Utilities.errorLog(request, HttpStatus.INTERNAL_SERVER_ERROR, "Error getting user", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Error getting user", e.getMessage()));
        }
    }

    @PutMapping("/{userId}")
    public ResponseEntity<ApiResponse> updateUserById(HttpServletRequest request, HttpServletResponse response,@RequestBody UserDto userDto, @PathVariable("userId") Integer userId){
        try{
            auditService.createAudit(request,response,ENTIDAD, String.valueOf(userDto),"Actualizacion correcta");

            User user = userService.updateById(userDto, userId);

            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ApiResponse("update successful", user));
        } catch (Exception e) {
            auditService.createAudit(request,response,ENTIDAD, String.valueOf(userDto),"Error updating user by id");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Error updating user", e.getMessage()));
        }
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<ApiResponse> deleteById(@PathVariable("userId") Integer userId, HttpServletRequest request, HttpServletResponse response) {
        try {
            boolean ok = userService.deleteUserById(userId);
            String responseMessage;

            if (ok) {
                responseMessage = "User with id: " + userId + " Deleted";
                auditService.createAudit(request, response, ENTIDAD, "Eliminación de usuario con id: " + userId,responseMessage);
            } else {
                responseMessage = "Error, we have a problem deleting the user: " + userId;
                return ResponseEntity.badRequest().body(new ApiResponse(responseMessage, null));
            }

            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(responseMessage, userId));
        } catch (Exception e) {
            auditService.createAudit(request, response, ENTIDAD, "Eliminación de usuario con id: " + userId,"Error trying to delete user");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Ocurrió un error al eliminar el usuario", e.getMessage()));
        }
    }
}
