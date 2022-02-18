package day01;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class RatingRepository {
    private DataSource dataSource;

    public RatingRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void insertRating(long movieId, List<Integer> ratings) {
        try (Connection conn = dataSource.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement stmt = conn.prepareStatement("insert into ratings (movie_id, rating) values (?, ?)")) {
                for (Integer act : ratings) {
                    if (act < 1 || act > 5) {
                        throw new IllegalArgumentException("Invalid rating!");
                    }
                    stmt.setLong(1, movieId);
                    stmt.setLong(2, act);
                    stmt.executeUpdate();
                }
                conn.commit();
                updateRating(movieId);
            }
            catch (IllegalArgumentException iae) {
                conn.rollback();
            }
        }
        catch (SQLException sqle) {
            throw new IllegalStateException("Cannot connect to ratings!", sqle);
        }
    }

    private void updateRating(long movieId) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement("select avg(rating) from ratings where movie_id = ?")) {
            stmt.setLong(1, movieId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    double avg = rs.getDouble(1);
                    PreparedStatement stmt2 = conn.prepareStatement("UPDATE movies SET avg_rate=? WHERE id=?;");
                    stmt2.setDouble(1, avg);
                    stmt2.setLong(2, movieId);
                    stmt2.executeUpdate();
                }
            }
            catch (SQLException sqle) {
                throw new IllegalStateException("Film not found", sqle);
            }
        }
        catch (SQLException sqle) {
            throw new IllegalStateException("Cannot connect to ratings!", sqle);
        }
    }

}


