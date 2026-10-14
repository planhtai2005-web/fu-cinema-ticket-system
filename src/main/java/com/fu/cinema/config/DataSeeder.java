package com.fu.cinema.config;

import com.fu.cinema.entity.Account;
import com.fu.cinema.entity.Customer;
import com.fu.cinema.entity.Genre;
import com.fu.cinema.entity.Movie;
import com.fu.cinema.enums.AccountStatus;
import com.fu.cinema.enums.AgeRating;
import com.fu.cinema.enums.MovieStatus;
import com.fu.cinema.enums.Role;
import com.fu.cinema.repository.AccountRepository;
import com.fu.cinema.repository.CustomerRepository;
import com.fu.cinema.repository.GenreRepository;
import com.fu.cinema.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    private final GenreRepository genreRepository;
    private final MovieRepository movieRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        // Avoid inserting duplicate sample data.
        if (accountRepository.count() > 0) {
            return;
        }

        log.info(">>> Seeding accounts, customers, genres and movies...");

        // Accounts and customers
        accountRepository.save(new Account(
                "admin@fucinema.vn",
                passwordEncoder.encode("Admin@123"),
                Role.ADMIN,
                AccountStatus.ACTIVE
        ));

        customer(
                "Nguyen Van An",
                "an.nv@gmail.com",
                "0901234567",
                LocalDate.of(2000, 5, 12),
                "079200001234"
        );

        customer(
                "Tran Thi Binh",
                "binh.tt@gmail.com",
                "0912345678",
                LocalDate.now().minusYears(15),
                "079209005678"
        );

        // Genres
        Genre action = genreRepository.save(
                new Genre("Hành động", "Phim hành động, kịch tính")
        );

        Genre anime = genreRepository.save(
                new Genre("Hoạt hình", "Phim hoạt hình cho mọi lứa tuổi")
        );

        Genre horror = genreRepository.save(
                new Genre("Kinh dị", "Phim kinh dị, giật gân")
        );

        Genre romance = genreRepository.save(
                new Genre("Tình cảm", "Phim tình cảm, lãng mạn")
        );

        genreRepository.save(
                new Genre("Khoa học viễn tưởng", "Phim khoa học viễn tưởng")
        );

        // Movies
        movie(
                "Biệt Đội Siêu Tốc", "Lê Minh", 120,
                AgeRating.T13, action, MovieStatus.SHOWING, -10
        );

        movie(
                "Chú Mèo Phiêu Lưu", "Trần Hà", 95,
                AgeRating.P, anime, MovieStatus.SHOWING, -20
        );

        movie(
                "Ngôi Nhà Bí Ẩn", "Phạm Quân", 105,
                AgeRating.T18, horror, MovieStatus.SHOWING, -5
        );

        movie(
                "Mùa Hè Năm Ấy", "Đỗ Lan", 110,
                AgeRating.T16, romance, MovieStatus.COMING_SOON, 14
        );

        movie(
                "Vũ Trụ Song Song", "Ngô Tùng", 140,
                AgeRating.T13, action, MovieStatus.COMING_SOON, 30
        );

        movie(
                "Huyền Thoại Cũ", "Vũ Bình", 100,
                AgeRating.P, anime, MovieStatus.STOPPED, -120
        );

        log.info(">>> Seed completed");
    }

    private Customer customer(
            String name,
            String email,
            String mobile,
            LocalDate birthday,
            String cccd
    ) {
        Account account = accountRepository.save(new Account(
                email,
                passwordEncoder.encode("Customer@123"),
                Role.CUSTOMER,
                AccountStatus.ACTIVE
        ));

        Customer customer = new Customer();
        customer.setFullName(name);
        customer.setEmail(email);
        customer.setMobile(mobile);
        customer.setBirthday(birthday);
        customer.setIdentityCard(cccd);
        customer.setAccount(account);

        return customerRepository.save(customer);
    }

    private Movie movie(
            String title,
            String director,
            int duration,
            AgeRating rating,
            Genre genre,
            MovieStatus status,
            int releaseOffsetDays
    ) {
        Movie movie = new Movie();
        movie.setTitle(title);
        movie.setDirector(director);
        movie.setDuration(duration);
        movie.setAgeRating(rating);
        movie.setGenre(genre);
        movie.setStatus(status);
        movie.setReleaseDate(
                LocalDate.now().plusDays(releaseOffsetDays)
        );
        movie.setDescription("Nội dung phim " + title + ".");
        movie.setPosterUrl(
                "https://placehold.co/300x450?text="
                        + title.replace(' ', '+')
        );

        return movieRepository.save(movie);
    }
}
