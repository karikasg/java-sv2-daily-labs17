package day01;

import org.flywaydb.core.Flyway;
import org.mariadb.jdbc.MariaDbDataSource;
import java.sql.SQLException;
import java.time.LocalDate;

public class Main {

    public static void main(String[] args) {


        MariaDbDataSource dataSource = new MariaDbDataSource();
        try {
            dataSource.setUrl("jdbc:mariadb://localhost:3306/movies-actors?useUnicode=true");
            dataSource.setUser("root");
            dataSource.setPassword("root");
        }
        catch (SQLException throwables) {
            throw new IllegalStateException("Cannot reach database", throwables);
        }

        Flyway flyway = Flyway.configure().dataSource(dataSource).load();
        flyway.migrate();

//        try (Connection connection = dataSource.getConnection(); Statement stmt = connection.createStatement()) {
//
//            stmt.executeUpdate("insert into actors (actor_name) values ('John Doe')");
//        }
//        catch (SQLException sqle) {
//            throw new IllegalStateException("Cannot connect", sqle);
//        }

//        ActorsRepository actorsRepository = new ActorsRepository(dataSource);
//        actorsRepository.saveActor("Jack Doe");

//        System.out.println(actorsRepository.findActorsWithPrefix("J"));

        MoviesRepository moviesRepository = new MoviesRepository(dataSource);
        moviesRepository.saveMovie("Titanic", LocalDate.of(1997, 12, 11));
        moviesRepository.saveMovie("Jaws", LocalDate.of(1979, 12, 11));
        moviesRepository.saveMovie("Star Wars", LocalDate.of(1977, 12, 11));

        System.out.println(moviesRepository.findAllMovies());

    }
}
