package ru.gordeychik.lab5.service1.service;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import org.junit.jupiter.api.Test;
import ru.gordeychik.lab5.service1.model.Positions;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class AnnualBonusServiceImplTest {
    private final AnnualBonusService service = new AnnualBonusServiceImpl(
            Clock.fixed(Instant.parse("2026-01-01T12:00:00Z"), ZoneOffset.UTC));

    @Test
    void calculate() {
        // given
        Positions position = Positions.HR;
        double bonus = 2.0;
        int workDays = 243;
        double salary = 100000.00;

        // when
        double result = service.calculate(position, salary, bonus, workDays);

        // then
        double expected = 360493.8271604938;
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void calculateInLeapYear() {
        AnnualBonusService leapYearService = new AnnualBonusServiceImpl(
                Clock.fixed(Instant.parse("2024-01-01T12:00:00Z"), ZoneOffset.UTC));

        double result = leapYearService.calculate(Positions.HR, 100000.00, 2.0, 243);

        assertThat(result).isEqualTo(361481.48148148146);
    }

    @Test
    void calculateQuarterlyForManager() {
        double result = service.calculateQuarterly(Positions.TL, 100000.00, 2.0, 243);

        assertThat(result).isEqualTo(195267.48971193415);
    }

    @Test
    void calculateQuarterlyForNonManager() {
        double result = service.calculateQuarterly(Positions.HR, 100000.00, 2.0, 243);

        assertThat(result).isEqualTo(0.0);
    }
}
