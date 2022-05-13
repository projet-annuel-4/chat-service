package fr.esgi.chat.util;

import fr.esgi.chat.exception.BadRequestException;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateTimeUtil {
    public static LocalDateTime getDateFromString(String str){
        LocalDateTime date;
         try {
            date = LocalDateTime.from(ZonedDateTime.parse( str, DateTimeFormatter.ISO_DATE_TIME).toInstant());
        } catch (DateTimeParseException ex){
            throw new BadRequestException("Unable to parse date provided");
        }
        return date;
    }
}
