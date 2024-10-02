package com.is4tech.base.controller;
import com.is4tech.base.domain.User;
import com.is4tech.base.dto.LoginUserDto;
import com.is4tech.base.dto.ProfileDto;
import com.is4tech.base.dto.RegisterUserDto;
import com.is4tech.base.domain.Login;
import com.is4tech.base.service.AuditService;
import com.is4tech.base.service.AuthenticationService;
import com.is4tech.base.service.JwtService;
import com.is4tech.base.service.ProfileService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api/auth")
@RestController
public class AuthenticationController {
    private final JwtService jwtService;
    private final String entidad="signup";
    private final AuditService auditService;

    @Autowired
    private ProfileService profileService;
    private final AuthenticationService authenticationService;

    public AuthenticationController(JwtService jwtService, AuditService auditService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.auditService = auditService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signup")
    public ResponseEntity<User> register(HttpServletRequest request, HttpServletResponse response, @RequestBody RegisterUserDto registerUserDto) {
        User registeredUser = authenticationService.signup(registerUserDto);
        auditService.createAudit(request,response,entidad, String.valueOf(registeredUser), null, "Registro user exitoso");
        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<Login> authenticate(@RequestBody LoginUserDto loginUserDto) {
        User authenticatedUser = authenticationService.authenticate(loginUserDto);

        //Obtenemos el perfil para extraer los roles
        ProfileDto profile = profileService.getProfileById(authenticatedUser.getProfileId());
        List<String> roles = profile.getResource(); //aca es donde vienen los roles

        //Genermaos el token e incluye los roles
        String jwtToken = jwtService.generateToken(authenticatedUser, roles);

        // creamos el objeto de respuesta para el login
        Login login = new Login();
        login.setToken(jwtToken);
        login.setExpiresIn(jwtService.getExpirationTime());

        return ResponseEntity.ok(login);
    }
}