package com.example.desafioguiabolso.handler;

import com.example.desafioguiabolso.exception.ExcecaoDetalhes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.Set;

@ControllerAdvice
@Slf4j
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException excecao) {

        ExcecaoDetalhes excecaoDetalhes = ExcecaoDetalhes.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .titulo("Erro de validação de campo(s)")
                .detalhes(excecao.getClass().getName())
                .mensagem(messageViolacoes(excecao))
                .build();

        return ResponseEntity.badRequest().body(excecaoDetalhes);
    }

    private String messageViolacoes(ConstraintViolationException excecao) {
        StringBuilder mensagem = new StringBuilder();
        Set<ConstraintViolation<?>> violacoesConjunto = excecao.getConstraintViolations();

        for (ConstraintViolation<?> violation : violacoesConjunto) {
            mensagem.append(violation.getMessage().concat(";"));
        }

        return mensagem.toString();
    }
}