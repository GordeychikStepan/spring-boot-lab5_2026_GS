package ru.gordeychik.lab5.service1.service;

import org.springframework.validation.BindingResult;
import ru.gordeychik.lab5.service1.model.Request;

public interface ValidationService {
    void isValid(BindingResult bindingResult, Request request);
}
