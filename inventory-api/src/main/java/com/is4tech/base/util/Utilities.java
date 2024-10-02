package com.is4tech.base.util;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

public class Utilities {

    private static final Logger logger = LoggerFactory.getLogger(Utilities.class);

    //metodod para registar informacion
    public static void infoLog(HttpServletRequest request, HttpStatus status, String message) {
        String clientIP = request.getRemoteAddr();
        logger.info("IP: {}, Status: {}, Message: {}", clientIP, status.value(), message);
    }

    //metdodo para las advertencias
    public static void warnLog(HttpServletRequest request, HttpStatus status, String message) {
        String clientIP = request.getRemoteAddr();
        logger.warn("IP: {}, Status: {}, Message: {}", clientIP, status.value(), message);
    }

    //metodo para los errores
    public static void errorLog(HttpServletRequest request, HttpStatus status, String message, Exception e) {
        String clientIP = request.getRemoteAddr();
        logger.error("IP: {}, Status: {}, Message: {}, Exception: {}", clientIP, status.value(), message, e.getMessage());
    }
}
