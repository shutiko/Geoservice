package ru.netology.i18n;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import ru.netology.entity.Country;

public class LocalizationServiceImplTest {
    @ParameterizedTest
    @EnumSource(Country.class)
    public void TestSenderMessages(Country country) {
        // given:
        LocalizationServiceImpl localizationService = new LocalizationServiceImpl();
        // when:
        String result = localizationService.locale(country);

        // then:
        if (country == Country.RUSSIA) {
            Assertions.assertEquals("Добро пожаловать", result);
        } else {Assertions.assertEquals("Welcome", result);}
    }
}
