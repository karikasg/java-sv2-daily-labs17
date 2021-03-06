package day01;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MoviesRepository {

    private DataSource dataSource;

    public MoviesRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public long saveMovie(String title, LocalDate releaseDate) {
        long id;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement stmt = connection.prepareStatement("insert into movies (title, release_date) values (?,?)", Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, title);
            stmt.setDate(2, Date.valueOf(releaseDate));
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                id = rs.getLong(1);
            } else {
                throw new SQLException("No key has generated");
            }
        }
        catch (SQLException throwables) {
            throw new IllegalStateException("Cannot add", throwables);
        }
        return id;
    }

    public List<Movie> findAllMovies() {
        List<Movie> movies = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
//             PreparedStatement stmt = connection.prepareStatement("select "))
             PreparedStatement statement = connection.prepareStatement("select * from movies");
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                long id = rs.getLong("id");
                String title = rs.getString("title");
                LocalDate releaseDate = rs.getDate("release_date").toLocalDate();
                movies.add(new Movie(id, title, releaseDate));
            }
        }
        catch (SQLException throwables) {
            throw new IllegalStateException("Cannot add", throwables);
        }
        return movies;
    }

    public Optional<Movie> findMovieByTitle(String title) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement stmt = connection.prepareStatement("select * from movies where title = ?")) {
            stmt.setString(1, title);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new Movie(rs.getLong("id"), rs.getString("title"), rs.getDate("release_date").toLocalDate()));
                }
                return Optional.empty();
            }
        }
        catch (SQLException sqle) {
            throw new IllegalStateException("Cannot connect to movies", sqle);
        }
    }
}

