package study.co.inter.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import lombok.Getter;
import lombok.Setter;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TransactionNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleTransactionNotFoundException(TransactionNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    
    @ExceptionHandler(ForbiddenTransactionAccessException.class)
    public ResponseEntity<ErrorResponse> handleForbiddenTransactionAccessException(ForbiddenTransactionAccessException ex) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.FORBIDDEN.value(), ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }
    
    @ExceptionHandler(InsufficientFundsException.class)
    public ResponseEntity<ErrorResponse> handleInsufficientFundsException(InsufficientFundsException ex) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.PAYMENT_REQUIRED.value(), ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.PAYMENT_REQUIRED);
    }
    
    @ExceptionHandler(ClientIdNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleClientIdNotFoundException(ClientIdNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
    
    @ExceptionHandler(ClientCpfNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleClientCpfNotFoundException(ClientCpfNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    
    @ExceptionHandler(InvalidValueException.class)
    public ResponseEntity<ErrorResponse> handleInvalidValueException(InvalidValueException ex) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY.value(), ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.UNPROCESSABLE_ENTITY);
    }
    
    @ExceptionHandler(AccountTierLimitException.class)
    public ResponseEntity<ErrorResponse> handleAccountTierLimitException(AccountTierLimitException ex) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.FORBIDDEN.value(), ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }
    
    @Getter
    @Setter
    public static class ErrorResponse {
        private int status;
        private String message;

        public ErrorResponse(int status, String message) {
            this.status = status;
            this.message = message;
        }
    }
}
