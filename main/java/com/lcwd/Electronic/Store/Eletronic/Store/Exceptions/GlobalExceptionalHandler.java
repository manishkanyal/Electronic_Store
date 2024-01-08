package com.lcwd.Electronic.Store.Eletronic.Store.Exceptions;

import com.lcwd.Electronic.Store.Eletronic.Store.DTOS.ApiResponseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionalHandler {

    Logger logger= LoggerFactory.getLogger(GlobalExceptionalHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponseMessage> ResourceNotFoundExceptionalHandler(ResourceNotFoundException ex)
    {
        logger.info(ex.getMessage());
        ApiResponseMessage apiMessage= ApiResponseMessage.builder().message(ex.getMessage()).success(false).status(HttpStatus.NOT_FOUND).build();
        return new ResponseEntity<ApiResponseMessage>(apiMessage,HttpStatus.NOT_FOUND);
    }

    //Handling BadApiRequest Exception
    @ExceptionHandler(BadApiRequest.class)
    public ResponseEntity<ApiResponseMessage> BadApiRequestHandler(BadApiRequest ex)
    {
        logger.info(ex.getMessage());
        ApiResponseMessage apiMessage= ApiResponseMessage.builder().message(ex.getMessage()).success(false).status(HttpStatus.NOT_FOUND).build();
        return new ResponseEntity<ApiResponseMessage>(apiMessage,HttpStatus.BAD_REQUEST);
    }

    //This exception is being created for
    //MethodArgumentNotValidException is a java class exception
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String , Object>> methodArgumentNotValidExceptionhandler(MethodArgumentNotValidException ex)
    {
        List<ObjectError> allErrors=ex.getBindingResult().getAllErrors(); //This method return all error in a method as a list.
        Map<String,Object> response=new HashMap<>();
        allErrors.stream().forEach(objectError->{
            String message=objectError.getDefaultMessage();
            String field=((FieldError) objectError).getField();
            response.put(field,message);
        });

        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }




}
