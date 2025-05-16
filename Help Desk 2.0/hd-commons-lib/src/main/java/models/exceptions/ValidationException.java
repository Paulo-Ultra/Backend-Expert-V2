package models.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder
public class ValidationException extends StandardError {

    @Getter
    private List<FieldError> fieldErrors;

    @Getter
    @AllArgsConstructor
    private static class FieldError{

        private String fieldName;
        private String message;
    }

    public void addError(final String fieldName, final String message){
        this.fieldErrors.add(new FieldError(fieldName, message));
    }
}
