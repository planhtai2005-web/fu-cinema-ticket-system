package com.fu.cinema.service.impl;

import com.fu.cinema.entity.Customer;
import com.fu.cinema.repository.CustomerRepository;
import com.fu.cinema.service.CustomerService;
import com.fu.cinema.util.ValidationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Override
    public Page<Customer> search(String keyword, int page, int size) {
        return customerRepository.search(ValidationUtils.blankToNull(keyword),
                PageRequest.of(Math.max(page, 0), size, Sort.by("fullName")));
    }

    @Override
    public long count() {
        return customerRepository.count();
    }
}
