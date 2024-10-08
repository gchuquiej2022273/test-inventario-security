package com.is4tech.base.service;

import com.is4tech.base.dto.LoginUserDto;
import com.is4tech.base.dto.RegisterUserDto;
import com.is4tech.base.domain.User;
import com.is4tech.base.exception.Exceptions;
import com.is4tech.base.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    public User signup(RegisterUserDto input) {
            validateRegisterInput(input);

            var passwordRandom = (input.getPassword() != null && !input.getPassword().isEmpty())
                    ? input.getPassword()
                    : userService.passswordRandomAndSendEmail(input);
            try {
                User user = new User();
                user.setName(input.getName());
                user.setSurname(input.getSurname());
                user.setEmail(input.getEmail());
                user.setUsername(input.getUsername());
                user.setPassword(passwordEncoder.encode(passwordRandom));
                user.setAge(input.getAge());
                user.setPhone(input.getPhone());

                if (input.getProfileId() == null || input.getProfileId() == 0) {
                    user.setProfileId(1);
                } else {
                    user.setProfileId(input.getProfileId());
                }
                user.setStatus(true);
                return userRepository.save(user);
            }catch (Exception e){
                throw new RuntimeException(e.getMessage());
            }

    }

    public User authenticate(LoginUserDto input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()
                )
        );

        return userRepository.findByEmail(input.getEmail())
                .orElseThrow();
    }

    public void validateRegisterInput(RegisterUserDto input){
        if (input.getName() == null || input.getName().trim().isEmpty()) {
            throw new Exceptions("Name cannot be empty");
        }
        if (input.getSurname() == null || input.getSurname().trim().isEmpty()){
            throw new Exceptions("Surname cannot be empty");
        }
        if (input.getEmail() == null || input.getEmail().trim().isEmpty()) {
            throw new Exceptions("Email cannot be empty");
        }
        if (!input.getEmail().endsWith("@gmail.com")) {
            throw new Exceptions("Email must be @gmail.com");
        }
        if (input.getUsername() == null || input.getUsername().trim().isEmpty()) {
            throw new Exceptions("Username cannot be empty");
        }
        if (input.getAge() == null ) {
            throw new Exceptions("Age cannot be empty");
        }
        if (input.getPhone() == null || input.getPhone().trim().isEmpty()) {
            throw new Exceptions("Phone cannot be empty");
        }
        if (input.getPhone().length() < 8 || input.getPhone().length() > 8) {
            throw new Exceptions("Phone number must be between 8 and 8 characters");
        }

        //verificamos si el correo ya existe
        if (userRepository.existsByEmail(input.getEmail())){
            throw new Exceptions("The email is already exists");
        }

        if (userRepository.existsByPhone(input.getPhone())){
            throw  new Exceptions("The phone number already exists");
        }

        if (userRepository.existsByUsername(input.getUsername())){
            throw  new Exceptions("The username already exists");
        }
    }
}
