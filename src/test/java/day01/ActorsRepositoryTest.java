package day01;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mariadb.jdbc.MariaDbDataSource;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class ActorsRepositoryTest {

    ActorsRepository actorsRepository;
    Flyway flyway;

    @BeforeEach
    void init() {
        MariaDbDataSource dataSource = new MariaDbDataSource();
        try {
            dataSource.setUrl("jdbc:mariadb://localhost:3306/movies-actors-test?useUnicode=true");
            dataSource.setUser("root");
            dataSource.setPassword("root");
        }
        catch (SQLException throwables) {
            throw new IllegalStateException("Cannot reach database", throwables);
        }
        flyway = Flyway.configure().dataSource(dataSource).load();
//        flyway.clean();
        flyway.migrate();

        actorsRepository = new ActorsRepository(dataSource);
    }

    @Test
    void saveActor() {
        System.out.println(actorsRepository.saveActor("Jack Doe"));


    }

    @Test
    void findActorsWithPrefix() {
    }

    @Test
    void findActorByName() {
        System.out.println(actorsRepository.findActorByName("Jack Doe"));
    }
}