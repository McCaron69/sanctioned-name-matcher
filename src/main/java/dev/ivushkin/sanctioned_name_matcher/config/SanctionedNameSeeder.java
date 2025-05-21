package dev.ivushkin.sanctioned_name_matcher.config;

import dev.ivushkin.sanctioned_name_matcher.service.SanctionedNameService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SanctionedNameSeeder {

    @Bean
    public CommandLineRunner commandLineRunner(SanctionedNameService sanctionedNameService) {
        return args -> {
            sanctionedNameService.addSanctionedName("Osama Bin Laden");
            sanctionedNameService.addSanctionedName("Ali Baba");
            sanctionedNameService.addSanctionedName("Joe Smith");
            sanctionedNameService.addSanctionedName("Ben Ladin");
            sanctionedNameService.addSanctionedName("Robert Johnson");
        };
    }
}
