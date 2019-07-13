package br.com.markstudy.testAPI.service.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.PRECONDITION_FAILED, reason="Invalid data sent do persist")
public class InvalidDataPersistException extends RuntimeException{
}
