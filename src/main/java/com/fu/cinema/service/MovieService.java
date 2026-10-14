package com.fu.cinema.service;

import com.fu.cinema.entity.Movie;
import com.fu.cinema.enums.MovieStatus;
import org.springframework.data.domain.Page;

import java.util.List;

public interface MovieService {

    Page<Movie> searchForAdmin(String keyword, Long genreId, MovieStatus status, int page, int size, String sort);

    Page<Movie> searchForCustomer(String keyword, Long genreId, int page, int size);

    Movie getById(Long movieId);

    List<Movie> findSchedulable();

    long countShowing();
}
