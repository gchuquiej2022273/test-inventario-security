package com.is4tech.base.service;

import com.is4tech.base.domain.Tokens;
import com.is4tech.base.repository.TokensRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TokenRecoverPasswordService {

    private final TokensRepository tokensRepository;

    public Tokens generarTokenForRecoverPassword(){
        SecureRandom random = new SecureRandom();
        StringBuilder token = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            token.append((char) (random.nextInt(26) + 'a'));
            token.append((char) (random.nextInt(10) + '0'));
        }
        Duration duration = Duration.ofMinutes(1);
        LocalDateTime expiracion = LocalDateTime.now().plus(duration);

        Tokens nuevoToken = new Tokens();
        nuevoToken.setToken(token.toString());
        nuevoToken.setExpiracion(expiracion);

        tokensRepository.save(nuevoToken);

        return nuevoToken;
    }

    public boolean validarToken(String token) {
        Optional<Tokens> optionalToken = tokensRepository.findByToken(token);
        if (optionalToken.isPresent()) {
            Tokens tokenDB = optionalToken.get();
            if (LocalDateTime.now().isBefore(tokenDB.getExpiracion())) {
                return true;
            } else {
                tokensRepository.delete(tokenDB);
            }
        }
        return false;
    }
}
