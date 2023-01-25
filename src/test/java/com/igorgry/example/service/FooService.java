package com.igorgry.example.service;

import com.igorgry.annotation.Autowired;
import com.igorgry.annotation.Bean;

@Bean
public class FooService {
    @Autowired
    private BarService mService;

    public String foo() {
        return mService.bar();
    }
}
