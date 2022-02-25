package in.cts.budgetanalysis.profiles.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import in.cts.budgetanalysis.profiles.exceptions.BadProfileException;

@RestControllerAdvice
public class GlobalExceptionControllerAdvice {

	Logger logger;
	
	public GlobalExceptionControllerAdvice() {
		logger = LoggerFactory.getLogger(this.getClass());
	}
	
	@ExceptionHandler(BadProfileException.class)
	public ResponseEntity<String> handleBadProfileException(BadProfileException exp){
		logger.info("sending bad request as response",exp);
		return new ResponseEntity<String>(exp.getMessage(), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> handleUnhandledExceptions(Exception exp){
		logger.error("sending internal server error as resposne",exp);
		return new ResponseEntity<String>("Regeret Inconvinience! Please try again later!", 
				HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
