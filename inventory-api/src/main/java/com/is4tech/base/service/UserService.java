package com.is4tech.base.service;


import com.is4tech.base.domain.User;
import com.is4tech.base.dto.ChangePasswordDto;
import com.is4tech.base.dto.PasswordDto;
import com.is4tech.base.dto.RegisterUserDto;
import com.is4tech.base.dto.UserDto;
import com.is4tech.base.exception.Exceptions;
import com.is4tech.base.repository.UserRepository;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    public void changePassword(ChangePasswordDto request){
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User user = (User) authentication.getPrincipal();

            User user1 = userRepository.findById(user.getId())
                    .orElseThrow(() -> new Exceptions("User not found with id: " + user.getId()));

            if (!passwordEncoder.matches(request.getCurrentPassword(), user1.getPassword())){
                throw new RuntimeException("La contraseña actual no es correcta");
            }

            user1.setPassword(passwordEncoder.encode(request.getNewPassword()));
            userRepository.save(user1);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public List<User> getUserStatusTrue(){
        return (List<User>) userRepository.findAll();

    }

    public User getById(Integer userId){

        return userRepository.findById(userId)
                .orElseThrow(() -> new Exceptions("User not found with id: " + userId));
    }

    public User updateById(UserDto userDto, Integer userId){

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new Exceptions("user not found"));

        user.setEmail(userDto.getEmail());
        user.setSurname(userDto.getSurname());
        user.setName(userDto.getName());
        user.setAge(userDto.getAge());
        user.setPhone(userDto.getPhone());

        return userRepository.save(user);
    }

    public Boolean deleteUserById(Integer userId){
        try {
            userRepository.deleteById(userId);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public String updatePasswordByTokenMail(PasswordDto passwordDto){
        try{
            User user = userRepository.findByEmail(passwordDto.getEmail())
                    .orElseThrow(() -> new Exceptions("user not found"));

            user.setPassword(passwordEncoder.encode(passwordDto.getNewPassword()));

            userRepository.save(user);

            return "Password actualizado correctamente!!";
        }catch (Exception e){
            throw new RuntimeException("Error al actualizar su contraseña");
        }
    }

    public String passswordRandomAndSendEmail(RegisterUserDto registerUserDto) throws MessagingException {
        final String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        final int defecto = 20;
        Random random = new Random();
        StringBuilder sb = new StringBuilder(defecto);
        for(int i=0; i<defecto; i++){
            int index = random.nextInt(caracteres.length());
            sb.append(caracteres.charAt(index));
        }
        String cadenaAleatoria = sb.toString();

        emailService.sendEmail(registerUserDto.getEmail(), registerUserDto.getName(), registerUserDto.getSurname(), cadenaAleatoria);

        return cadenaAleatoria;

    }

    private void validateNewPassword(String newPassword) {
        if (newPassword.length() < 8) {
            throw new RuntimeException("La nueva contraseña debe tener al menos 8 caracteres");
        }
    }
}
