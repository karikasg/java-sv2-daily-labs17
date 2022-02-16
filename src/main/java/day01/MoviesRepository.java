package day01;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MoviesRepository {

    private DataSource dataSource;

    public MoviesRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void saveMovie(String title, LocalDate releaseDate) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement stmt = connection.prepareStatement("insert into movies (title, release_date) values (?,?)")) {
            stmt.setString(1, title);
            stmt.setDate(2, Date.valueOf(releaseDate));
            stmt.executeUpdate();
        }
        catch (SQLException throwables) {
            throw new IllegalStateException("Cannot add", throwables);
        }
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
}
