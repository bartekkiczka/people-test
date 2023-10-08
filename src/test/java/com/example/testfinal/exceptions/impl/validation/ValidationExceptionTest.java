package com.example.testfinal.exceptions.impl.validation;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ValidationExceptionTest {

    @Test
    public void testConstructorAndMessage() {
        //given
        List<String> errors = Arrays.asList("Error 1", "Error 2", "Error 3");
        ValidationException exception = new ValidationException(errors);

        //when
        assertNotNull(exception.getErrors());

        //then
        String expectedMessage = "Error 1, Error 2, Error 3, ";
        assertEquals(expectedMessage, exception.getMessage());
    }

}