package dev.yuri.addresses_api.initializer;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    @Autowired
    private Flyway flyway;

    @Override
    public void run(String... args) throws Exception {
        flyway.clean();
        flyway.migrate();
    }
}

