package com.sportygroup.bookservice.dto;

import com.sportygroup.bookservice.model.BookType;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookDto {

    private Long id;

    @NotBlank(message = "Title cannot be empty")
    private String title;

    @NotBlank(message = "Author cannot be empty")
    private String author;

    @NotBlank(message = "ISBN cannot be empty")
    @Pattern(regexp = "\\d{10}|\\d{13}", message = "ISBN must be 10 or 13 digits long")
    private String isbn;

    @NotNull(message = "Book type cannot be empty")
    private BookType type;

    @NotNull(message = "Price cannot be empty")
    @Positive(message = "Price must be greater than 0")
    private BigDecimal price;

    @NotNull(message = "Stock quantity cannot be empty")
    @Min(value = 0, message = "Stock quantity cannot be negative")
    private Integer stockQuantity;
}
