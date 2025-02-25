package com.sportygroup.bookservice.mapper;

import com.sportygroup.bookservice.dto.BookDto;
import com.sportygroup.bookservice.dto.BookPageResponseDto;
import com.sportygroup.bookservice.model.Book;
import com.sportygroup.bookservice.model.BookType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class BookMapperTest {

    private BookMapper bookMapper;

    @BeforeEach
    void setUp() {
        bookMapper = new BookMapper();
    }

    @Test
    void shouldMapDtoToEntity() {
        //given
        BookDto bookDto = getBookDto();
        Book expectedBook = getBook();
        expectedBook.setId(null);

        //when
        Book book = bookMapper.toEntity(bookDto);

        //then
        assertThat(book).isNotNull();
        assertThat(book).usingRecursiveComparison().isEqualTo(expectedBook);
    }

    @Test
    void shouldMapEntityToDto() {
        Book book = getBook();

        BookDto expectedBookDto = getBookDto();

        BookDto bookDto = bookMapper.toDto(book);

        assertThat(bookDto).isNotNull();
        assertThat(bookDto).usingRecursiveComparison().isEqualTo(expectedBookDto);

    }

    @Test
    void shouldUpdateEntity() {
        Book book = getBook();
        BookDto bookDto = new BookDto(1L, "Updated Title", "Updated Author",
                "987654321", BookType.REGULAR, BigDecimal.valueOf(99.99), 25);

        bookMapper.updateEntity(bookDto, book);

        assertThat(book.getTitle()).isEqualTo("Updated Title");
        assertThat(book.getAuthor()).isEqualTo("Updated Author");
        assertThat(book.getIsbn()).isEqualTo("987654321");
        assertThat(book.getType()).isEqualTo(BookType.REGULAR);
        assertThat(book.getPrice()).isEqualByComparingTo(BigDecimal.valueOf(99.99));
        assertThat(book.getStockQuantity()).isEqualTo(25);
    }

    @Test
    void shouldConvertPageToBookPageResponseDto() {

        //given
        List<Book> books = List.of(
                getBook(),
                getBook()
        );
        Page<Book> bookPage = new PageImpl<>(books, PageRequest.of(0, 2), 2);

        //when
        BookPageResponseDto responseDto = bookMapper.toBookPageResponseDto(bookPage);

        //then
        assertThat(responseDto.books()).hasSize(2);
        assertThat(responseDto.totalElements()).isEqualTo(2);
        assertThat(responseDto.totalPages()).isEqualTo(1);
        assertThat(responseDto.pageSize()).isEqualTo(2);
        assertThat(responseDto.currentPage()).isEqualTo(1);
    }

    private BookDto getBookDto() {
        return new BookDto(1L, "Title", "Author",
                "123456789",
                BookType.OLD_EDITION, BigDecimal.valueOf(100), 5);
    }

    private Book getBook() {
        return new Book(1L, "Title", "Author", "123456789",
                BookType.OLD_EDITION, BigDecimal.valueOf(100), 5);
    }
}