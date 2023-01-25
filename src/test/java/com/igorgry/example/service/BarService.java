package com.igorgry.example.service;

import com.igorgry.annotation.Bean;

@Bean
public class BarService {
    public String bar() {
        return "Hello";
    }
}
