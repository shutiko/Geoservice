import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import ru.netology.entity.Country;
import ru.netology.entity.Location;
import ru.netology.geo.GeoService;
import ru.netology.geo.GeoServiceImpl;
import ru.netology.i18n.LocalizationService;
import ru.netology.i18n.LocalizationServiceImpl;
import ru.netology.sender.MessageSenderImpl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class MainTest {

    @ParameterizedTest
    @ValueSource(strings = { "127.0.0.1", "172.0.32.11", "96.44.183.149", "172.", "96.", "New York"})

    public void TestByIp (String ip) {
        // given:
        GeoServiceImpl geoService = new GeoServiceImpl();
        // when:
        if (ip.equals("127.0.0.1")) {
            Location expected = geoService.byIp(ip);
            Assertions.assertEquals( null, expected.getCity() );
            Assertions.assertEquals( null, expected.getCountry() );
            Assertions.assertEquals( null, expected.getStreet() );
            Assertions.assertEquals( 0, expected.getBuiling() );
        } else if (ip.equals("172.0.32.11")) {
            Location expected = geoService.byIp(ip);
            Assertions.assertEquals( "Moscow", expected.getCity() );
            Assertions.assertEquals( Country.RUSSIA, expected.getCountry() );
            Assertions.assertEquals( "Lenina", expected.getStreet() );
            Assertions.assertEquals( 15, expected.getBuiling() );
        } else if (ip.equals("96.44.183.149")) {
            Location expected = geoService.byIp(ip);
            Assertions.assertEquals( "New York", expected.getCity() );
            Assertions.assertEquals( Country.USA, expected.getCountry() );
            Assertions.assertEquals( " 10th Avenue", expected.getStreet() );
            Assertions.assertEquals( 32, expected.getBuiling() );
        } else if (ip.equals("172.")) {
            Location expected = geoService.byIp(ip);
            Assertions.assertEquals( "Moscow", expected.getCity() );
            Assertions.assertEquals( Country.RUSSIA, expected.getCountry() );
            Assertions.assertEquals( null, expected.getStreet() );
            Assertions.assertEquals( 0, expected.getBuiling() );
        } else if (ip.equals("96.")) {
            Location expected = geoService.byIp(ip);
            Assertions.assertEquals( "New York", expected.getCity() );
            Assertions.assertEquals( Country.USA, expected.getCountry() );
            Assertions.assertEquals( null, expected.getStreet() );
            Assertions.assertEquals( 0, expected.getBuiling() );
        }

        // then:

    }

    @Test
    public void TestByCoordinates() {
        GeoServiceImpl geoService = new GeoServiceImpl();
        // given:
        double latitude = 5.3;
        double longi = 4.3;
        // when:
        Throwable exception = assertThrows(RuntimeException.class, () -> geoService.byCoordinates(latitude, longi));
        // then:
        Assertions.assertEquals("Not implemented", exception.getMessage());
    }

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
    @ParameterizedTest
    @ValueSource(strings = {"172.0.32.11", "96.44.183.149"})
    public void TestMessageSenderImpl(String ip) {
        if (ip.equals("172.0.32.11")) {
        GeoServiceImpl geoService1 = Mockito.mock(GeoServiceImpl.class);
        Mockito.when(geoService1.byIp(ip))
                .thenReturn(new Location("Moscow", Country.RUSSIA, "Lenina", 15));
        LocalizationServiceImpl localizationService1 = Mockito.mock(LocalizationServiceImpl.class);
            Mockito.when(localizationService1.locale(Country.RUSSIA))
                    .thenReturn("Добро пожаловать");
        MessageSenderImpl messageSender = new MessageSenderImpl(geoService1, localizationService1);
        Map<String, String> headers = new HashMap<String, String>();
        headers.put(messageSender.IP_ADDRESS_HEADER, ip);
        String preferences = messageSender.send(headers);
        String expected = "Добро пожаловать";

        Assertions.assertEquals(expected, preferences);
    } else if (ip.equals("96.44.183.149")) {
            GeoServiceImpl geoService1 = Mockito.mock(GeoServiceImpl.class);
            Mockito.when(geoService1.byIp(ip))
                    .thenReturn(new Location("New York", Country.USA, " 10th Avenue", 32));
            LocalizationServiceImpl localizationService1 = Mockito.mock(LocalizationServiceImpl.class);
            Mockito.when(localizationService1.locale(Country.USA))
                    .thenReturn("Welcome");
            MessageSenderImpl messageSender = new MessageSenderImpl(geoService1, localizationService1);
            Map<String, String> headers = new HashMap<String, String>();
            headers.put(messageSender.IP_ADDRESS_HEADER, ip);
            String preferences = messageSender.send(headers);
            String expected = "Welcome";

            Assertions.assertEquals(expected, preferences);
        }

    }

}
