package org.g9project4.global.exceptions;

import org.g9project4.global.rests.JSONData;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.Map;

public interface RestExceptionProcessor<O> {
    @ExceptionHandler(Exception.class)
    default ResponseEntity<JSONData<O>> errorHandler(Exception e) {

        Object message = e.getMessage();

        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR; // 500
        if (e instanceof CommonException commonException) {//커맨드 객체 오류
            status = commonException.getStatus();

            Map<String, List<String>> errorMessages = commonException.getErrorMessages();
            if (errorMessages != null) message = errorMessages;
        }

        JSONData<O> data = new JSONData<O>();
        data.setSuccess(false);
        data.setMessage(message);
        data.setStatus(status);

        e.printStackTrace();

        return ResponseEntity.status(status).body(data);
    }
}
