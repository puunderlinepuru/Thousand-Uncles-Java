package com.thousand_uncles.google_api_handler.data.util;

import com.thousand_uncles.google_api_handler.data.service.MapRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@SuppressWarnings("unused")
@Component
public class MapRecordUtil {

    @Autowired
    private MapRecordService service;
}
