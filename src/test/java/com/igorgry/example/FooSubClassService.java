package com.igorgry.example;

import com.igorgry.annotation.Bean;
import com.igorgry.example.service.FooService;

@Bean("customFooServiceName")
public class FooSubClassService extends FooService {
}
