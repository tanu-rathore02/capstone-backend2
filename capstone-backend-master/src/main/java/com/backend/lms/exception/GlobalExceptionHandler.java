package com.backend.lms.exception;

import com.backend.lms.dto.response.ErrorResponseDto;
import com.backend.lms.dto.response.ResponseDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@ControllerAdvice
//public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
//
//    // Handle validation errors
//    @Override
//    protected ResponseEntity<Object> handleMethodArgumentNotValid(
//            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
//
//        Map<String, String> validationErrors = new HashMap<>();
//        List<ObjectError> validationErrorList = ex.getBindingResult().getAllErrors();
//
//        validationErrorList.forEach((error) -> {
//            String fieldName = ((FieldError) error).getField();
//            String validationMsg = error.getDefaultMessage();
//            validationErrors.put(fieldName, validationMsg);
//        });
//
//        ErrorResponseDto errorResponseDto = new ErrorResponseDto(
//                request.getDescription(false),
//                HttpStatus.BAD_REQUEST,
//                "Validation Error: " + validationErrors.toString(),
//                LocalDateTime.now()
//        );
//
//        return new ResponseEntity<>(errorResponseDto, HttpStatus.BAD_REQUEST);
//    }
//
//    // Global exception handler (catch-all)
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ErrorResponseDto> handleGlobalException(Exception exception, WebRequest webRequest) {
//        ErrorResponseDto errorResponseDto = new ErrorResponseDto(
//                webRequest.getDescription(false),
//                HttpStatus.INTERNAL_SERVER_ERROR,
//                "Internal Server Error: " + exception.getMessage(),
//                LocalDateTime.now()
//        );
//
//        // Log the exception for debugging
//        exception.printStackTrace();
//
//        return new ResponseEntity<>(errorResponseDto, HttpStatus.INTERNAL_SERVER_ERROR);
//    }
//
//
//
//    // Handle ResourceNotFoundException
//    @ExceptionHandler(ResourceNotFoundException.class)
//    public ResponseEntity<ErrorResponseDto> handleResourceNotFoundException(ResourceNotFoundException exception,
//                                                                            WebRequest webRequest) {
//        ErrorResponseDto errorResponseDto = new ErrorResponseDto(
//                webRequest.getDescription(false),
//                HttpStatus.NOT_FOUND,
//                "Resource Not Found: " + exception.getMessage(),
//                LocalDateTime.now()
//        );
//
//        return new ResponseEntity<>(errorResponseDto, HttpStatus.NOT_FOUND);
//    }
//
//    // Handle ResourceAlreadyExistsException
//    @ExceptionHandler(ResourceAlreadyExistsException.class)
//    public ResponseEntity<ErrorResponseDto> handleResourceAlreadyExistsException(ResourceAlreadyExistsException exception,
//                                                                                 WebRequest webRequest) {
//        ErrorResponseDto errorResponseDto = new ErrorResponseDto(
//                webRequest.getDescription(false),
//                HttpStatus.CONFLICT,
//                "Resource Conflict: " + exception.getMessage(),
//                LocalDateTime.now()
//        );
//
//        return new ResponseEntity<>(errorResponseDto, HttpStatus.CONFLICT);
//    }
//
//    // Handle EntityConstraintViolationException
//    @ExceptionHandler(EntityConstraintViolationException.class)
//    public ResponseEntity<ErrorResponseDto> handleEntityConstraintViolationException(EntityConstraintViolationException exception,
//                                                                                     WebRequest webRequest) {
//        ErrorResponseDto errorResponseDto = new ErrorResponseDto(
//                webRequest.getDescription(false),
//                HttpStatus.CONFLICT,
//                "Entity Constraint Violation: " + exception.getMessage(),
//                LocalDateTime.now()
//        );
//
//        return new ResponseEntity<>(errorResponseDto, HttpStatus.CONFLICT);
//    }
//
//    // Handle AccessDeniedException
//    @ExceptionHandler(AccessDeniedException.class)
//    public ResponseEntity<ErrorResponseDto> handleAccessDeniedException(AccessDeniedException exception,
//                                                                        WebRequest webRequest) {
//        ErrorResponseDto errorResponseDto = new ErrorResponseDto(
//                webRequest.getDescription(false),
//                HttpStatus.FORBIDDEN,
//                "Access Denied: " + exception.getMessage(),
//                LocalDateTime.now()
//        );
//
//        return new ResponseEntity<>(errorResponseDto, HttpStatus.FORBIDDEN);
//    }
//}


@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Map<String, String> validationErrors = new HashMap<>();
        List<ObjectError> validationErrorList = ex.getBindingResult().getAllErrors();

        validationErrorList.forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String validationMsg = error.getDefaultMessage();
            validationErrors.put(fieldName, validationMsg);
        });

        return new ResponseEntity<>(validationErrors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDto> handleGlobalException(Exception exception,
                                                             WebRequest webRequest) {
        ResponseDto errorResponseDto = new ResponseDto(
                HttpStatus.INTERNAL_SERVER_ERROR.toString(),
                exception.getMessage()
        );

        return new ResponseEntity<>(errorResponseDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ResponseDto> handleResourceNotFoundException(ResourceNotFoundException exception,
                                                                       WebRequest webRequest) {
        ResponseDto errorResponseDto = new ResponseDto(
                HttpStatus.NOT_FOUND.toString(),
                exception.getMessage()
        );

        return new ResponseEntity<>(errorResponseDto, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<ResponseDto> handleResourceAlreadyExistsException(ResourceAlreadyExistsException exception,
                                                                            WebRequest webRequest) {
        ResponseDto errorResponseDto = new ResponseDto(
                HttpStatus.CONFLICT.toString(),
                exception.getMessage()
        );

        return new ResponseEntity<>(errorResponseDto, HttpStatus.CONFLICT);
    }



    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ResponseDto> handleBadCredentialsException(BadCredentialsException exception,
                                                                     WebRequest webRequest) {
        ResponseDto errorResponseDto = new ResponseDto(
                HttpStatus.BAD_REQUEST.toString(),
                exception.getMessage()
        );

        return new ResponseEntity<>(errorResponseDto, HttpStatus.BAD_REQUEST);
    }


    // Handle EntityConstraintViolationException
    @ExceptionHandler(EntityConstraintViolationException.class)
    public ResponseEntity<ErrorResponseDto> handleEntityConstraintViolationException(EntityConstraintViolationException exception,
                                                                                     WebRequest webRequest) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                webRequest.getDescription(false),
                HttpStatus.CONFLICT,
                "Entity Constraint Violation: " + exception.getMessage(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(errorResponseDto, HttpStatus.CONFLICT);
    }


    @ExceptionHandler(MethodNotAllowedException.class)
    public ResponseEntity<ResponseDto> handleMethodNotAllowedException(MethodNotAllowedException exception, WebRequest webRequest) {
        ResponseDto errorResponseDto = new ResponseDto(
                HttpStatus.METHOD_NOT_ALLOWED.toString(),
                exception.getMessage()
        );

        return new ResponseEntity<>(errorResponseDto, HttpStatus.METHOD_NOT_ALLOWED);
    }
}
