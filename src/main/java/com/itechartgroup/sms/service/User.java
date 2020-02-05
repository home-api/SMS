package com.itechartgroup.sms.service;

import lombok.Data;

import java.util.Set;

@Data
public class User {

    private final String name;
    private final Set<String> interests;

}
