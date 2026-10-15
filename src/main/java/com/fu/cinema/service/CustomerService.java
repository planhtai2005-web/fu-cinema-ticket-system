package com.fu.cinema.service;

import com.fu.cinema.entity.Customer;
import org.springframework.data.domain.Page;

public interface CustomerService {

    Page<Customer> search(String keyword, int page, int size);

    long count();
}
