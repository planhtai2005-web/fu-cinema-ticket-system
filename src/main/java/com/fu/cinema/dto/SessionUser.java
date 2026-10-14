
package com.fu.cinema.dto;

import com.fu.cinema.enums.Role;
import lombok.Value;

import java.io.Serializable;

@Value
public class SessionUser implements Serializable {

    private static final long serialVersionUID = 1L;

    Long accountId;
    Long customerId;
    String displayName;
    Role role;

    public boolean isAdmin() {
        return role == Role.ADMIN;
    }

    public boolean isCustomer() {
        return role == Role.CUSTOMER;
    }

    public boolean isStaff() {
        return role == Role.STAFF;
    }
}
