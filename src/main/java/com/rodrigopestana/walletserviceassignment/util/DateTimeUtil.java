package com.rodrigopestana.walletserviceassignment.util;

import com.rodrigopestana.walletserviceassignment.exception.WalletBusinessException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateTimeUtil {

    public static LocalDateTime convertStringToDateTime(String dateTime) {
        try {
            LocalDateTime localDateTime = LocalDateTime.parse(dateTime);

            return localDateTime;

        } catch (DateTimeParseException e) {
            throw new WalletBusinessException("Invalid DateTime format: " + dateTime, e.getMessage());
        }
    }

    public static String formatDateTime(LocalDateTime zonedDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH:mm:ss");
        return zonedDateTime.format(formatter);
    }
}
