package com.fu.cinema.service.impl;

import com.fu.cinema.entity.Movie;
import com.fu.cinema.enums.MovieStatus;
import com.fu.cinema.exception.ResourceNotFoundException;
import com.fu.cinema.repository.MovieRepository;
import com.fu.cinema.service.MovieService;
import com.fu.cinema.util.ValidationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MovieServiceImpl implements MovieService {

    private static final Set<String> SORTABLE = Set.of("title", "releaseDate", "duration");

    private final MovieRepository movieRepository;

    @Override
    public Page<Movie> searchForAdmin(String keyword, Long genreId, MovieStatus status, int page, int size, String sort) {
        String field = SORTABLE.contains(sort) ? sort : "releaseDate";
        Sort order = "title".equals(field) ? Sort.by(field).ascending() : Sort.by(field).descending();
        Set<MovieStatus> statuses = status == null ? EnumSet.allOf(MovieStatus.class) : EnumSet.of(status);
        return movieRepository.search(ValidationUtils.blankToNull(keyword), genreId, statuses,
                PageRequest.of(Math.max(page, 0), size, order));
    }

    @Override
    public Page<Movie> searchForCustomer(String keyword, Long genreId, int page, int size) {
        return movieRepository.search(ValidationUtils.blankToNull(keyword), genreId,
                EnumSet.of(MovieStatus.SHOWING, MovieStatus.COMING_SOON),
                PageRequest.of(Math.max(page, 0), size, Sort.by("releaseDate").descending()));
    }

    @Override
    public Movie getById(Long movieId) {
        return movieRepository.findWithGenreById(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("Phim", movieId));
    }

    @Override
    public List<Movie> findSchedulable() {
        return movieRepository.findByStatusNotOrderByTitleAsc(MovieStatus.STOPPED);
    }

    @Override
    public long countShowing() {
        return movieRepository.countByStatus(MovieStatus.SHOWING);
    }
}
