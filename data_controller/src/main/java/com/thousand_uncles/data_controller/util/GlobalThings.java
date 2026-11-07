package com.thousand_uncles.data_controller.util;

import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Component
public class GlobalThings {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static ObjectMapper getObjectMapper() {return objectMapper;}
}
