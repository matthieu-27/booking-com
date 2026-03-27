package fr.fms.booking_com.utils;

import fr.fms.booking_com.entities.Booking;

import org.springframework.stereotype.Component;
import javax.validation.Validator;

import java.util.Scanner;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;

@Component
public class UserBookingInputValidator {

    private final Validator validator;

    public UserBookingInputValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }

    public void validate(Booking userInput) {
        Set<ConstraintViolation<Booking>> violations = validator.validate(userInput);
        if (!violations.isEmpty()) {
            violations.forEach(violation -> System.err.println(violation.getMessage()));
            throw new IllegalArgumentException("Les données saisies sont invalides.");
        }
    }

    public static String readInput(String prompt) {
        System.out.print(prompt);
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }
}