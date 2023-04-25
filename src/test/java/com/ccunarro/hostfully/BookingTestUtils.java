package com.ccunarro.hostfully;

import com.ccunarro.hostfully.data.common.DateRange;
import com.ccunarro.hostfully.data.reservation.forms.CreateReservationForm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;

import java.time.LocalDate;
import java.util.UUID;

public class BookingTestUtils {

    private final static ObjectMapper mapper;
    // for more details check data.sql
    public final static String USER_ID_FOO = "dbd76c0a-cb18-4d48-be34-9cc9c7825100";
    public final static String USER_ID_NEW_GUEST = "dbd76c0a-cb18-4d48-be34-9cc9c7825102";
    public final static String PROPERTY_ID_HOUSE_1 = "dbd76c0a-cb18-4d48-be34-9cc9c78251b1";
    public final static String PROPERTY_ID_HOUSE_3 = "dbd76c0a-cb18-4d48-be34-9cc9c78251b3";
    public final static String RESERVATION_BLOCK_ID_HOUSE_1 = "dbd76c0a-cb18-4d48-be34-9cc9c78251c1";
    public final static String RESERVATION_BOOKING_ID_HOUSE_2 = "dbd76c0a-cb18-4d48-be34-9cc9c78251c2";

    static {
        mapper = new ObjectMapper();
        mapper.registerModule(new JSR310Module());
    }

    public static ObjectMapper getMapper() {
        return mapper;
    }

    public static CreateReservationForm buildReservationFormOk() {
        CreateReservationForm form = new CreateReservationForm();
        DateRange date = new DateRange(LocalDate.now().plusDays(3), LocalDate.now().plusDays(6));
        form.setPropertyId(UUID.fromString(PROPERTY_ID_HOUSE_1));
        form.setDate(date);
        return form;
    }

    public static CreateReservationForm buildReservationFormWithDates(LocalDate start, LocalDate end) {
        return buildReservationForm(start, end, PROPERTY_ID_HOUSE_1);
    }

    public static CreateReservationForm buildReservationForm(LocalDate start, LocalDate end, String propertyId) {
        CreateReservationForm form = new CreateReservationForm();
        DateRange date = new DateRange(start, end);
        form.setPropertyId(UUID.fromString(propertyId));
        form.setDate(date);
        return form;
    }

    public static CreateReservationForm buildReservationFormStartDateBeforeToday() {
        CreateReservationForm form = new CreateReservationForm();
        DateRange date = new DateRange(LocalDate.now().minusDays(2), LocalDate.now().plusDays(6));
        form.setPropertyId(UUID.fromString(PROPERTY_ID_HOUSE_1));
        form.setDate(date);
        return form;
    }

    public static CreateReservationForm buildReservationFormStartDateAfterEndDate() {
        CreateReservationForm form = new CreateReservationForm();
        DateRange date = new DateRange(LocalDate.now().plusDays(20), LocalDate.now().plusDays(6));
        form.setPropertyId(UUID.fromString(PROPERTY_ID_HOUSE_1));
        form.setDate(date);
        return form;
    }
}
