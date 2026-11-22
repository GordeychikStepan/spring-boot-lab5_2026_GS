package ru.gordeychik.lab5.service1.service;

import org.springframework.stereotype.Service;
import ru.gordeychik.lab5.service1.model.Response;

@Service
public interface ModifyResponseService {
    Response modify(Response response);
}
