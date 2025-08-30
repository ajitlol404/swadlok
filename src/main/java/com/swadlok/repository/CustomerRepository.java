package com.swadlok.repository;

import com.swadlok.entity.Customer;
import com.swadlok.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByUser(User user);

    Optional<Customer> findByUserEmail(String email);

}
