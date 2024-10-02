package com.is4tech.base.controller;

import com.is4tech.base.domain.Tokens;
import com.is4tech.base.domain.User;
import com.is4tech.base.dto.*;
import com.is4tech.base.repository.UserRepository;
import com.is4tech.base.service.*;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    private final String entidad = "user";
    @Autowired
    private final AuditService auditService;
    @Autowired
    private final UserService userService;
    @Autowired
    private final EmailService emailService;

    @PostMapping("/recover-password")
    public ResponseEntity<String> sendEmail(HttpServletRequest request, HttpServletResponse response, @RequestBody EmailDto email) {
        try {
            Optional<User> userOptional = userRepository.findByEmail(email.getEmail());
            if (userOptional.isEmpty()) {
                return ResponseEntity.badRequest().body("No se encontró un usuario con ese correo electrónico.");
            }

            User user = userOptional.get();
            String requestObject = user.getEmail();
            auditService.createAudit(request,response,entidad, requestObject, user.getId(), "Email enviado exitosamente");

            Tokens token = tokenRecoverPasswordService.generarTokenForRecoverPassword();
            emailService.sendEmail(email,token.getToken());
            return ResponseEntity.ok("Se ha enviado el código de recuperación de contraseña");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocurrió un error al enviar el email.");
        }
    }

    @PutMapping("/update-password")
    public ResponseEntity<String> updatePasswowrd(HttpServletRequest request, HttpServletResponse response, @RequestBody PasswordDto passwordDto) {
        try {
            boolean ok = tokenRecoverPasswordService.validarToken(passwordDto.getToken());
            if (ok){
                userService.updatePasswordByTokenMail(passwordDto);
            }else{
                return ResponseEntity.badRequest().body("Token inválido o ha expirado.");
            }
            auditService.createAudit(request,response,entidad, passwordDto.getNewPassword(), null, "Actualizacion correcta");
            return ResponseEntity.ok("Contraseña actualizada exitosamente.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Token inválido o ha expirado.");
        }
    }

    @PatchMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordDto request, Principal connectedUser) {

        userService.changePassword(request, connectedUser);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/getUsers")
    public List<User> getActivateUser(){
        return this.userService.getUserStatusTrue();
    }

    @GetMapping("/{userId}")
    public Optional<User> getUserById(@PathVariable("userId") Integer userId){
        return this.userService.getById(userId);
    }

    @PutMapping("/{userId}")
    public User updateUserById(HttpServletRequest request, HttpServletResponse response,@RequestBody User user, @PathVariable("userId") Integer userId){

        try{
            String requestObject = user.getSurname();
            auditService.createAudit(request,response,entidad, requestObject, user.getId(), "Actualizacion correcta");

            return this.userService.updateById(user, userId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<String> deleteById(@PathVariable("userId") Integer userId, HttpServletRequest request, HttpServletResponse response) {
        try {
            boolean ok = userService.deleteUserById(userId);
            String responseMessage;

            if (ok) {
                responseMessage = "User with id: " + userId + " Deleted";
                auditService.createAudit(request, response, entidad, "Eliminación de usuario con id: " + userId, userId, responseMessage);
            } else {
                responseMessage = "Error, we have a problem deleting the user: " + userId;
                return ResponseEntity.badRequest().body(responseMessage);
            }

            return ResponseEntity.ok(responseMessage);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocurrió un error al eliminar el usuario.");
        }
    }
}
