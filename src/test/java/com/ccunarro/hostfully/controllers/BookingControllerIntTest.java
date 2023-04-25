package com.ccunarro.hostfully.controllers;

import com.ccunarro.hostfully.BookingTestUtils;
import com.ccunarro.hostfully.data.reservation.Reservation;
import com.ccunarro.hostfully.data.reservation.forms.CreateReservationForm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Disclaimer: For simplicity while implementing the tests, it was used the sample data located in data.sql.
 * On a real world scenario probably the data will be prepared carefully, and it would require
 * different data for each test.
 */
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext
class BookingControllerIntTest {

    @Autowired
    private MockMvc mockMvc;
    private static ObjectMapper mapper;

    @BeforeAll
    public static void init() {
        mapper = BookingTestUtils.getMapper();
    }

    @Test
    void contextLoads() {
    }

    @Test
    public void createBookingWithNoAuthentication() throws Exception {
        CreateReservationForm form = BookingTestUtils.buildReservationFormOk();

        System.out.println("Given an unauthenticated user, it should return unauthorized while attempting to create a booking");
        mockMvc.perform(post("/booking/")
                .contentType(MediaType.APPLICATION_JSON)
                .content((mapper.writeValueAsString(form))))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails("foo@gmail.com")
    public void createBookingWithUserSuccess() throws Exception {

        LocalDate start = LocalDate.now().plusDays(145);
        LocalDate end = LocalDate.now().plusDays(147);

        CreateReservationForm form = BookingTestUtils.buildReservationFormWithDates(start, end);

        System.out.println("Given an authenticated user it should successfully create the booking");
        mockMvc.perform(post("/booking/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content((mapper.writeValueAsString(form))))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("foo@gmail.com")
    public void getBookingWithUserSuccess() throws Exception {

        LocalDate start = LocalDate.now().plusDays(150);
        LocalDate end = LocalDate.now().plusDays(157);

        CreateReservationForm form = BookingTestUtils.buildReservationFormWithDates(start, end);

        //create a new booking
        MvcResult result = mockMvc.perform(post("/booking/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content((mapper.writeValueAsString(form))))
                .andExpect(status().isOk())
                .andReturn();

        UUID uuid = UUID.fromString(JsonPath.read(result.getResponse().getContentAsString(), "$.id"));

        // fetch the booking
        System.out.println("Given an authenticated user it should successfully get an own booking");
        mockMvc.perform(get(String.format("/booking/%s/", uuid))
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.id").value(uuid.toString()))
                .andExpect(jsonPath("$.userId").value(BookingTestUtils.USER_ID_FOO))
                .andExpect(jsonPath("$.startDate").value(start.toString()))
                .andExpect(jsonPath("$.endDate").value(end.toString()))
                .andExpect(jsonPath("$.type").value(Reservation.Type.BOOKING.toString()));
    }

    @Test
    @WithUserDetails("foo@gmail.com")
    public void createBookingsWithCloseDatesSuccess() throws Exception {

        LocalDate firstStart = LocalDate.now().plusDays(160);
        LocalDate firstEnd = LocalDate.now().plusDays(162);

        CreateReservationForm firstForm = BookingTestUtils.buildReservationFormWithDates(firstStart, firstEnd);

        mockMvc.perform(post("/booking/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content((mapper.writeValueAsString(firstForm))))
                .andExpect(status().isOk());

        LocalDate secondStart = firstEnd;
        LocalDate secondEnd = secondStart.plusDays(2);

        CreateReservationForm secondForm = BookingTestUtils.buildReservationFormWithDates(secondStart, secondEnd);

        System.out.println("Given a new booking which date starts at the end " +
                "of another one it should successfully be created");
        mockMvc.perform(post("/booking/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content((mapper.writeValueAsString(secondForm))))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("foo@gmail.com")
    public void createReservationWithStartDateBeforeThanToday() throws Exception {
        CreateReservationForm form = BookingTestUtils.buildReservationFormStartDateBeforeToday();

        System.out.println("Given a start date that is before than today it should return bad request when attempting to create a booking");
        mockMvc.perform(post("/booking/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content((mapper.writeValueAsString(form))))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithUserDetails("foo@gmail.com")
    public void createBookingWithStartDateAfterEndDate() throws Exception {
        CreateReservationForm form = BookingTestUtils.buildReservationFormStartDateAfterEndDate();

        System.out.println("Given a start date that is greater than the end date, it should return bad request when attempting to create a booking");
        mockMvc.perform(post("/booking/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content((mapper.writeValueAsString(form))))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithUserDetails("foo@gmail.com")
    public void createBookingWithStartDateSameAsEndDate() throws Exception {
        LocalDate start = LocalDate.now().plusDays(20);
        LocalDate end = start;
        CreateReservationForm form = BookingTestUtils.buildReservationFormWithDates(start, end);

        System.out.println("Given a start date that is the same as the end date, " +
                "it should return bad request when attempting to create a booking");
        mockMvc.perform(post("/booking/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content((mapper.writeValueAsString(form))))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void cancelBookingWithNoAuthentication() throws Exception {
        System.out.println("Given an unauthenticated user, it should return unauthorized while attempting to cancel a booking");
        mockMvc.perform(delete(String.format("/booking/%s/", BookingTestUtils.RESERVATION_BOOKING_ID_HOUSE_2)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails("johndoe@gmail.com")
    public void cancelBookingWithWithAnotherUser() throws Exception {
        System.out.println("Given an authenticated user that has not made the booking, " +
                "it should return forbidden while attempting to cancel it");
        mockMvc.perform(delete(String.format("/booking/%s/", BookingTestUtils.RESERVATION_BOOKING_ID_HOUSE_2)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails("foo@gmail.com")
    public void cancelBookingWithWithUserSuccess() throws Exception {
        System.out.println("Given an authenticated user that has made the booking, " +
                "it should successfully cancel it");
        mockMvc.perform(delete(String.format("/booking/%s/", BookingTestUtils.RESERVATION_BOOKING_ID_HOUSE_2)))
                .andExpect(status().isOk());
    }
}