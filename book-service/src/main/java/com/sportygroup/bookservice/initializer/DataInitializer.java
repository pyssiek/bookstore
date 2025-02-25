package com.sportygroup.bookservice.initializer;

import com.github.javafaker.Faker;
import com.sportygroup.bookservice.model.Book;
import com.sportygroup.bookservice.model.BookType;
import com.sportygroup.bookservice.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
@Slf4j
@Profile("dev")
public class DataInitializer implements CommandLineRunner {

    private final BookRepository bookRepository;
    private final Faker faker = new Faker();
    private final Random random = new Random();

    @Override
    public void run(String... args) {
        if (bookRepository.count() == 0) {
            log.info("Initializing 50 sample books...");

            List<Book> books = IntStream.range(0, 50)
                    .mapToObj(i -> createBook())
                    .toList();

            bookRepository.saveAll(books);
            log.info("50 sample books added.");
        }
    }


    private Book createBook() {
        return new Book(
                null,
                faker.book().title(),
                faker.book().author(),
                faker.code().isbn10(),
                randomBookType(),
                randomPrice(),
                random.nextInt(1, 20)
        );
    }

    private BookType randomBookType() {
        BookType[] types = BookType.values();
        return types[random.nextInt(types.length)];
    }

    private BigDecimal randomPrice() {
        return BigDecimal.valueOf(20.0 + (random.nextDouble() * 80.0))
                .setScale(2, RoundingMode.HALF_UP);
    }
}
