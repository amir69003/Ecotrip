package fr.ecotrip.backend;

import fr.ecotrip.backend.service.Co2Service;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements ApplicationRunner {

    private final Co2Service co2Service;

    public DataInitializer(Co2Service co2Service) {
        this.co2Service = co2Service;
    }

    @Override
    public void run(ApplicationArguments args) {
        // Initialize the database with CO2 data
        co2Service.initCo2();
        // You can add more initialization logic here if needed
    }
}
