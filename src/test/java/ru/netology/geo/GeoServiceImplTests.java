package ru.netology.geo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import ru.netology.entity.Country;
import ru.netology.entity.Location;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class GeoServiceImplTests {
    @ParameterizedTest
            //@ValueSource(strings = { "127.0.0.1", "172.0.32.11", "96.44.183.149", "172.", "96."})
    @CsvSource({
            "172.0.32.11, Moscow, RUSSIA, Lenina, 15",
            "96.44.183.149, New York, USA, 10th Avenue, 32",
            "172., Moscow, RUSSIA, null, 0",
            "96., New York, USA, null, 0"
    })

    public void TestByIp (String ip, String city, Country country, String street, int builing) {
        // given:
        GeoServiceImpl geoService = new GeoServiceImpl();
        // when:
            Location actual = geoService.byIp(ip);
        // then:
            Assertions.assertEquals( city, actual.getCity() );
            Assertions.assertEquals( country, actual.getCountry() );
            Assertions.assertEquals( street, String.valueOf(actual.getStreet()) );
            Assertions.assertEquals( builing, actual.getBuiling() );
    }

    @Test
    public void TestByIP_localhost() {
        // given:
        String ip = "127.0.0.1";
        GeoServiceImpl geoService = new GeoServiceImpl();
        // when:
        Location actual = geoService.byIp(ip);
        // then:
        Assertions.assertEquals( null, actual.getCity() );
        Assertions.assertEquals( null, actual.getCountry() );
        Assertions.assertEquals( null, actual.getStreet() );
        Assertions.assertEquals( 0, actual.getBuiling() );
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

}
