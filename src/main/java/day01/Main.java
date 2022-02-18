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
        flyway.clean();
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

        ActorsRepository actorsRepository = new ActorsRepository(dataSource);
        MoviesRepository moviesRepository = new MoviesRepository(dataSource);
        ActorsMoviesRepository actorsMoviesRepository = new ActorsMoviesRepository(dataSource);
        RatingRepository ratingRepository = new RatingRepository(dataSource);
        ActorsMoviesService actorsMoviesService = new ActorsMoviesService(actorsRepository, moviesRepository, actorsMoviesRepository);
        MoviesRatingService moviesRatingService = new MoviesRatingService(moviesRepository, ratingRepository);

//        moviesRepository.saveMovie("Titanic", LocalDate.of(1997, 12, 11));
//        moviesRepository.saveMovie("Jaws", LocalDate.of(1979, 12, 11));
//        moviesRepository.saveMovie("Star Wars", LocalDate.of(1977, 12, 11));

        actorsMoviesService.insertMovieWithActors("Titanic", LocalDate.of(1997, 11, 13), List.of("Leonardo DiCaprio", "Kate Winslet"));
        actorsMoviesService.insertMovieWithActors("Jaws", LocalDate.of(1975, 06, 20), List.of("Roy Scheider", "Richard Dreyfuss"));
        actorsMoviesService.insertMovieWithActors("Great Gatsby", LocalDate.of(2012, 12, 11), List.of("Leonardo DiCaprio", "Tobey Maguire"));
        actorsMoviesService.insertMovieWithActors("Star Wars", LocalDate.of(1977, 05, 25), List.of("Mark Hamill", "Harrison Ford", "Carrie Fisher"));

        moviesRatingService.addRatings("Titanic", 5, 3, 2);
        moviesRatingService.addRatings("Great Gatsby", 1, 3, 2, 5);
        moviesRatingService.addRatings("Titanic", 4, 5, 4);
        moviesRatingService.addRatings("Jaws", 5, 5, 5);

    }
}
