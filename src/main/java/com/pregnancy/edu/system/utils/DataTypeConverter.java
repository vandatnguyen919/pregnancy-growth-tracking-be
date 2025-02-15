package com.pregnancy.edu.system.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DataTypeConverter {
    public String formatDateTime(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return dateTime.format(formatter);
    }
}
