package ru.gordeychik.lab5.service1.service;

import java.time.Clock;
import java.time.Year;
import org.springframework.stereotype.Service;
import ru.gordeychik.lab5.service1.model.Positions;

@Service
public class AnnualBonusServiceImpl implements AnnualBonusService {
    private final Clock clock;

    public AnnualBonusServiceImpl() {
        this(Clock.systemDefaultZone());
    }

    AnnualBonusServiceImpl(Clock clock) {
        this.clock = clock;
    }

    @Override
    public double calculate(Positions positions, double salary, double bonus, int workDays) {
        int daysInYear = Year.now(clock).length();
        return salary * bonus * daysInYear * positions.getPositionCoefficient() / workDays;
    }

    @Override
    public double calculateQuarterly(Positions positions, double salary, double bonus, int workDays) {
        if (!positions.isManager()) {
            return 0.0;
        }
        return calculate(positions, salary, bonus, workDays) / 4;
    }
}
