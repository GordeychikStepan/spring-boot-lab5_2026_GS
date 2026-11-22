package ru.gordeychik.lab5.service1.service;

import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import ru.gordeychik.lab5.service1.exception.ValidationFailedException;
import ru.gordeychik.lab5.service1.model.Request;

@Slf4j
@Service
public class RequestValidationService implements ValidationService {
    @Override
    public void isValid(BindingResult bindingResult, Request request) {
        if (bindingResult.hasErrors()) {
            bindingResult.getFieldErrors().forEach(error ->
                    log.error("bindingResult: поле {}: {}",
                            error.getField(), error.getDefaultMessage()));
            String details = bindingResult.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .sorted()
                    .collect(Collectors.joining("; "));
            log.error("Выбрасывается ValidationFailedException: {}",
                    details);
            throw new ValidationFailedException(details);
        }
        validateBonusData(request);
        log.info("Валидация request пройдена");
    }

    private void validateBonusData(Request request) {
        boolean hasBonusData = request.getPosition() != null || request.getSalary() != null
                || request.getBonus() != null || request.getWorkDays() != null;
        if (hasBonusData && (request.getPosition() == null || request.getSalary() == null
                || request.getBonus() == null || request.getWorkDays() == null
                || request.getWorkDays() <= 0)) {
            throw new ValidationFailedException(
                    "Для премии укажите position, salary, bonus и workDays > 0");
        }
    }
}
