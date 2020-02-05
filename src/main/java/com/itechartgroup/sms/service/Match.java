package com.itechartgroup.sms.service;

import lombok.Data;

import java.util.Collection;

@Data
public class Match {

    private final String left;
    private final String right;
    private final Collection<String> interests;

}
