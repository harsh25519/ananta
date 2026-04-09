package dev.hkb.ananta.exceptionHandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> userNotAuthenticated(UserNotAuthenticated exc){
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(exc.getMessage());
        errorResponse.setStatus(HttpStatus.NOT_FOUND.value());
        errorResponse.setTimestamp(System.currentTimeMillis());

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> userNotFound(UserNotFound exc){
        ErrorResponse err = new ErrorResponse();
        err.setMessage(exc.getMessage());
        err.setStatus(HttpStatus.NOT_FOUND.value());
        err.setTimestamp(System.currentTimeMillis());

        return new ResponseEntity<>(err, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> addressNotFound(AddressNotFound exc){
        ErrorResponse err = new ErrorResponse();
        err.setMessage(exc.getMessage());
        err.setStatus(HttpStatus.NOT_FOUND.value());
        err.setTimestamp(System.currentTimeMillis());

        return new ResponseEntity<>(err, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> cartNotFound(CartNotFound exc){
        ErrorResponse err = new ErrorResponse();
        err.setMessage(exc.getMessage());
        err.setStatus(HttpStatus.NOT_FOUND.value());
        err.setTimestamp(System.currentTimeMillis());

        return new ResponseEntity<>(err, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> categoryNotFound(CategoryNotFound exc){
        ErrorResponse err = new ErrorResponse();
        err.setMessage(exc.getMessage());
        err.setStatus(HttpStatus.NOT_FOUND.value());
        err.setTimestamp(System.currentTimeMillis());

        return new ResponseEntity<>(err, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> ImageNotFound(ImageNotFound exc){
        ErrorResponse err = new ErrorResponse();
        err.setMessage(exc.getMessage());
        err.setStatus(HttpStatus.NOT_FOUND.value());
        err.setTimestamp(System.currentTimeMillis());

        return new ResponseEntity<>(err, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> insufficientStock(InsufficientStock exc){
        ErrorResponse err = new ErrorResponse();
        err.setMessage(exc.getMessage());
        err.setStatus(HttpStatus.INSUFFICIENT_STORAGE.value());
        err.setTimestamp(System.currentTimeMillis());

        return new ResponseEntity<>(err, HttpStatus.INSUFFICIENT_STORAGE);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> manufacturerNotFound(ManufacturerNotFound exc){
        ErrorResponse err = new ErrorResponse();
        err.setMessage(exc.getMessage());
        err.setStatus(HttpStatus.NOT_FOUND.value());
        err.setTimestamp(System.currentTimeMillis());

        return new ResponseEntity<>(err, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> orderNotFound(OrderNotFound exc){
        ErrorResponse err = new ErrorResponse();
        err.setMessage(exc.getMessage());
        err.setStatus(HttpStatus.NOT_FOUND.value());
        err.setTimestamp(System.currentTimeMillis());

        return new ResponseEntity<>(err, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> paymentNotFound(PaymentNotFound exc){
        ErrorResponse err = new ErrorResponse();
        err.setMessage(exc.getMessage());
        err.setStatus(HttpStatus.NOT_FOUND.value());
        err.setTimestamp(System.currentTimeMillis());

        return new ResponseEntity<>(err, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> productNotFound(ProductNotFound exc){
        ErrorResponse err = new ErrorResponse();
        err.setMessage(exc.getMessage());
        err.setStatus(HttpStatus.NOT_FOUND.value());
        err.setTimestamp(System.currentTimeMillis());

        return new ResponseEntity<>(err, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> reviewNotFound(ReviewNotFound exc){
        ErrorResponse err = new ErrorResponse();
        err.setMessage(exc.getMessage());
        err.setStatus(HttpStatus.NOT_FOUND.value());
        err.setTimestamp(System.currentTimeMillis());

        return new ResponseEntity<>(err, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> sellerNotFound(SellerNotFound exc){
        ErrorResponse err = new ErrorResponse();
        err.setMessage(exc.getMessage());
        err.setStatus(HttpStatus.NOT_FOUND.value());
        err.setTimestamp(System.currentTimeMillis());

        return new ResponseEntity<>(err, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> sellerProductNotFound(SellerProductNotFound exc){
        ErrorResponse err = new ErrorResponse();
        err.setMessage(exc.getMessage());
        err.setStatus(HttpStatus.NOT_FOUND.value());
        err.setTimestamp(System.currentTimeMillis());

        return new ResponseEntity<>(err, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> tagNotFound(TagNotFound exc){
        ErrorResponse err = new ErrorResponse();
        err.setMessage(exc.getMessage());
        err.setStatus(HttpStatus.NOT_FOUND.value());
        err.setTimestamp(System.currentTimeMillis());

        return new ResponseEntity<>(err, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> userDoesNotExist(UserDoesNotExist exc){
        ErrorResponse err = new ErrorResponse();
        err.setMessage(exc.getMessage());
        err.setStatus(HttpStatus.NO_CONTENT.value());
        err.setTimestamp(System.currentTimeMillis());

        return new ResponseEntity<>(err, HttpStatus.NO_CONTENT);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> userNotAuthorized(UserNotAuthorized exc){
        ErrorResponse err = new ErrorResponse();
        err.setMessage(exc.getMessage());
        err.setStatus(HttpStatus.UNAUTHORIZED.value());
        err.setTimestamp(System.currentTimeMillis());

        return new ResponseEntity<>(err, HttpStatus.UNAUTHORIZED);
    }
}
