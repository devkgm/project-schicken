package com.groups.schicken;

import com.groups.schicken.weather.WeatherService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("dev")
public class WeatherTest {
    @Autowired
    WeatherService weatherService;

    @Test
    public void test() throws Exception {
        weatherService.syncWeatherData();
    }
}
