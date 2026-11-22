package ru.gordeychik.lab5.service1.service;

import org.springframework.stereotype.Service;
import ru.gordeychik.lab5.service1.model.Positions;

@Service
public interface AnnualBonusService {
    double calculate(Positions positions, double salary, double bonus, int workDays);

    double calculateQuarterly(Positions positions, double salary, double bonus, int workDays);
}
