package com.is4tech.base.service;


import com.is4tech.base.domain.User;
import com.is4tech.base.dto.ChangePasswordDto;
import com.is4tech.base.dto.PasswordDto;
import com.is4tech.base.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    public void changePassword(ChangePasswordDto request, Principal connectedUser){
        try{

            var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

            if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())){
                throw new IllegalAccessException("Wrong password");
            }
            if (!request.getNewPassword().equals(request.getCurrentPassword())){
                throw new IllegalAccessException("password are not the same");
            }

            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
            userRepository.save(user);
        }catch (Exception e){
            throw new RuntimeException("fallo al tratar de actualizar el password");
        }
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public List<User> getUserStatusTrue(){
        return (List<User>) userRepository.findAll();

    }

    public Optional<User> getById(Integer userId){

        return userRepository.findById(userId);
    }

    public User updateById(User request, Integer userId){

        User user = userRepository.findById(userId).get();

        user.setEmail(request.getEmail());
        user.setSurname(request.getSurname());
        user.setName(request.getName());
        user.setAge(request.getAge());
        user.setPhone(request.getPhone());

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
            User user = userRepository.findByEmail(passwordDto.getEmail()).get();

            user.setPassword(passwordEncoder.encode(passwordDto.getNewPassword()));

            userRepository.save(user);

            return "Password actualizado correctamente!!";
        }catch (Exception e){
            throw new RuntimeException("Error al actualizar su contraseña");
        }
    }

    private void validateNewPassword(String newPassword) {
        if (newPassword.length() < 8) {
            throw new RuntimeException("La nueva contraseña debe tener al menos 8 caracteres");
        }
    }
}
