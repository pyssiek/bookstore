package com.sportygroup.customerservice.repository;

import com.sportygroup.customerservice.model.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends MongoRepository<Customer, String> {

    @Query("{ 'id': ?0 }")
    @Update("{ '$inc': { 'loyaltyPoints': ?1 } }")
    void updateLoyaltyPoints(String customerId, int points);
}

