package com.fu.cinema.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class MovieRevenueDTO {

    private final String title;
    private final Long ticketCount;
    private final BigDecimal revenue;
}