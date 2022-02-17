package day01;

import org.flywaydb.core.Flyway;
import org.mariadb.jdbc.MariaDbDataSource;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

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
        ActorsRepository actorsRepository = new ActorsRepository(dataSource);
        ActorsMoviesRepository actorsMoviesRepository = new ActorsMoviesRepository(dataSource);
        ActorsMoviesService service = new ActorsMoviesService(actorsRepository, moviesRepository, actorsMoviesRepository);

//        moviesRepository.saveMovie("Titanic", LocalDate.of(1997, 12, 11));
//        moviesRepository.saveMovie("Jaws", LocalDate.of(1979, 12, 11));
//        moviesRepository.saveMovie("Star Wars", LocalDate.of(1977, 12, 11));

        service.insertMovieWithActors("Titanic", LocalDate.of(1997, 11, 13), List.of("Leonardo DiCaprio", "Kate Winslet"));
        service.insertMovieWithActors("Great Gatsby", LocalDate.of(2012, 12, 11), List.of("Leonardo DiCaprio", "Tobey Maguire"));


    }
}
