package students.students_demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class LoadDatabase {

  private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

  @Bean
  CommandLineRunner initDatabase(StudentRepository repository) {

    return args -> {
      log.info("Preloading " + repository.save(new Student("Kelvin", "Chuks", "SOE", 100)));
      log.info("Preloading " + repository.save(new Student("Marcus", "David", "SOP", 200)));
    };
  }
}