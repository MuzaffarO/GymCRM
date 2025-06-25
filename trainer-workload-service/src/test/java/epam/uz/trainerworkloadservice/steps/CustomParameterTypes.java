package epam.uz.trainerworkloadservice.steps;

import io.cucumber.java.ParameterType;
import java.time.LocalDate;

public class CustomParameterTypes {
    @ParameterType("20\\d{2}-\\d{2}-\\d{2}")
    public LocalDate date(String date) {
        return LocalDate.parse(date);
    }
}
