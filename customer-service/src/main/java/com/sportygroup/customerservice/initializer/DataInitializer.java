package com.sportygroup.customerservice.initializer;

import com.github.javafaker.Faker;
import com.sportygroup.customerservice.model.Address;
import com.sportygroup.customerservice.model.AddressType;
import com.sportygroup.customerservice.model.Customer;
import com.sportygroup.customerservice.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
@Slf4j
@Profile("dev")
public class DataInitializer implements CommandLineRunner {

    private final CustomerRepository customerRepository;
    private final Faker faker = new Faker();
    private final Random random = new Random();

    @Override
    public void run(String... args) throws Exception {
        if (customerRepository.count() == 0) {
            log.info("Initializing 10 sample customers...");
            List<Customer> customers = IntStream.range(0, 10)
                    .mapToObj(i -> createCustomer())
                    .toList();
            customerRepository.saveAll(customers);
            log.info("10 sample customers added.");
        }
    }


    private Customer createCustomer() {
        return new Customer(
                null,
                faker.name().firstName(),
                faker.name().lastName(),
                faker.internet().emailAddress(),
                generateLoyaltyPoints(),
                List.of(
                        new Address(
                                faker.address().streetAddress(),
                                faker.address().city(),
                                faker.address().country(),
                                faker.address().zipCode(),
                                randomEnum(AddressType.class)
                        )
                )
        );
    }

    private int generateLoyaltyPoints() {
        return random.nextInt(100) < 30 ? random.nextInt(11, 100) : random.nextInt(0, 10);
    }

    private <T extends Enum<?>> T randomEnum(Class<T> clazz) {
        T[] values = clazz.getEnumConstants();
        return values[random.nextInt(values.length)];
    }


}
