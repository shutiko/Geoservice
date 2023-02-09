package ru.netology.sender;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import ru.netology.entity.Country;
import ru.netology.entity.Location;
import ru.netology.geo.GeoServiceImpl;
import ru.netology.i18n.LocalizationServiceImpl;

import java.util.HashMap;
import java.util.Map;

public class MessageSenderImplTest {
    @ParameterizedTest
    @CsvSource({
            "172.0.32.11, Moscow, RUSSIA, Lenina, 15, Добро пожаловать",
            "96.44.183.149, New York, USA, 10th Avenue, 32, Welcome"
    })
    public void TestMessageSenderImpl(String ip, String city, Country country, String street, int builing, String message) {

            GeoServiceImpl geoService1 = Mockito.mock(GeoServiceImpl.class);
            Mockito.when(geoService1.byIp(ip))
                    .thenReturn(new Location(city, country, street, builing));
            LocalizationServiceImpl localizationService1 = Mockito.mock(LocalizationServiceImpl.class);
            Mockito.when(localizationService1.locale(country))
                    .thenReturn(message);
            MessageSenderImpl messageSender = new MessageSenderImpl(geoService1, localizationService1);
            Map<String, String> headers = new HashMap<String, String>();
            headers.put(messageSender.IP_ADDRESS_HEADER, ip);
            String preferences = messageSender.send(headers);
            String expected = message;

            Assertions.assertEquals(expected, preferences);
    }
}
