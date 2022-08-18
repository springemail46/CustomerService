package com.spring.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerControllerException extends RuntimeException{
    private static final long serialVersionId =1L;

    private String errorCode;
    private String errorMessage;

}
