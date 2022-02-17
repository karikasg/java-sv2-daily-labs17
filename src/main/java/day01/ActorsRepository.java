package day01;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ActorsRepository {
    private DataSource dataSource;

    public ActorsRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public long saveActor(String name) {
        long id;
        try (Connection connection = dataSource.getConnection();
        PreparedStatement stmt = connection.prepareStatement("insert into actors (actor_name) values (?)", Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, name);
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                id = rs.getLong(1);
            } else {
                throw new SQLException("No key has generated");
            }
        }
        catch (SQLException throwables) {
            throw new IllegalStateException("Cannot save: " + name, throwables);
        }
        return id;
    }

    public List<String> findActorsWithPrefix(String prefix) {
        List<String> result = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement stmt = connection.prepareStatement("SELECT actor_name FROM actors WHERE actor_name LIKE ?")){
            stmt.setString(1, prefix+"%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String actorName = rs.getString("actor_name");
                    result.add(actorName);
                }
            }

        }
        catch (SQLException throwables) {
            throw new IllegalStateException("Cannot query", throwables);
        }
        return result;
    }

    public Optional<Actor> findActorByName(String name) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement stmt = connection.prepareStatement("SELECT * FROM actors WHERE actor_name = ?")){
            stmt.setString(1, name);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    long id = rs.getLong("id");
                    String actorName = rs.getString("actor_name");
                    return Optional.of(new Actor(id, actorName));
                }
            }
        }
        catch (SQLException throwables) {
            throw new IllegalStateException("Cannot query", throwables);
        }
        return Optional.empty();
    }
}
