package com.ozan.currency.conversion.system.exception.handler;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.ozan.currency.conversion.system.dto.response.Response;
import com.ozan.currency.conversion.system.util.type.ErrorType;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Order(Ordered.LOWEST_PRECEDENCE)
@ControllerAdvice
@AllArgsConstructor
public class GenericExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public final ResponseEntity handleGenericException(Exception e) {

    	log.error("Unexpected error occured: ", e);
    	
        Response response = new Response().notOk(HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.addErrorMsg(ErrorType.GENERIC_INTERNAL_ERROR.getCode(),
                ErrorType.GENERIC_INTERNAL_ERROR.getDescription());

        return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}