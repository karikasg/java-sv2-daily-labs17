package day01;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class MoviesRatingService {
    private MoviesRepository moviesRepository;
    private RatingRepository ratingsRepository;

    public MoviesRatingService(MoviesRepository moviesRepository, RatingRepository ratingsRepository) {
        this.moviesRepository = moviesRepository;
        this.ratingsRepository = ratingsRepository;
    }

    public void addRatings(String title, Integer... ratings) {
        Optional<Movie> actual = moviesRepository.findMovieByTitle(title);
        if (actual.isPresent()) {
            ratingsRepository.insertRating(actual.get().getId(), Arrays.asList(ratings));
        } else {
            throw new IllegalArgumentException("Cannot find movie: "+title);
        }
    }
}
