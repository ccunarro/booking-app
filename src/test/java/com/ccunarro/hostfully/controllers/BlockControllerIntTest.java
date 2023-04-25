package com.ccunarro.hostfully.controllers;

import com.ccunarro.hostfully.BookingTestUtils;
import com.ccunarro.hostfully.data.reservation.forms.CreateReservationForm;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Disclaimer: For simplicity while implementing the tests, it was used the sample data located in data.sql.
 * On a real world scenario probably the data will be prepared carefully, and it would require
 * different data for each test.
 */
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext
class BlockControllerIntTest {

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
    public void createBlockWithNoAuthentication() throws Exception {
        CreateReservationForm form = BookingTestUtils.buildReservationFormOk();

        System.out.println("Given an unauthenticated user, it should return unauthorized while attempting to create a block");
        mockMvc.perform(post("/block/")
                .contentType(MediaType.APPLICATION_JSON)
                .content((mapper.writeValueAsString(form))))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails("johndoe@gmail.com")
    public void createBlockWithAnotherUser() throws Exception {
        CreateReservationForm form = BookingTestUtils.buildReservationFormOk();

        System.out.println("Given an authenticated user that is not owner of the property, it should return forbidden while attempting to create a block");
        mockMvc.perform(post("/block/")
                .contentType(MediaType.APPLICATION_JSON)
                .content((mapper.writeValueAsString(form))))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails("foo@gmail.com")
    public void createBlockWithUserSuccess() throws Exception {
        LocalDate start = LocalDate.now().plusDays(140);
        LocalDate end = LocalDate.now().plusDays(141);
        CreateReservationForm form = BookingTestUtils.buildReservationFormWithDates(start, end);

        System.out.println("Given an authenticated user that is the owner of the property, it should successfully create the block");
        mockMvc.perform(post("/block/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content((mapper.writeValueAsString(form))))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("foo@gmail.com")
    public void createBlockWithStartDateBeforeThanToday() throws Exception {
        CreateReservationForm form = BookingTestUtils.buildReservationFormStartDateBeforeToday();

        System.out.println("Given a start date that is before than today it should return bad request when attempting to create a block");
        mockMvc.perform(post("/block/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content((mapper.writeValueAsString(form))))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithUserDetails("foo@gmail.com")
    public void createBlockWithStartDateAfterEndDate() throws Exception {
        CreateReservationForm form = BookingTestUtils.buildReservationFormStartDateAfterEndDate();

        System.out.println("Given a start date that is greater than the end date, it should return bad request when attempting to create a block");
        mockMvc.perform(post("/block/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content((mapper.writeValueAsString(form))))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void deleteBlockWithNoAuthentication() throws Exception {
        System.out.println("Given an unauthenticated user, it should return unauthorized while attempting to delete a block");
        mockMvc.perform(delete(String.format("/block/%s/", BookingTestUtils.RESERVATION_BLOCK_ID_HOUSE_1)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails("johndoe@gmail.com")
    public void deleteBlockWithWithAnotherUser() throws Exception {
        System.out.println("Given an authenticated user that is not owner of the property, " +
                "it should return forbidden while attempting to delete a block from it");
        mockMvc.perform(delete(String.format("/block/%s/", BookingTestUtils.RESERVATION_BLOCK_ID_HOUSE_1)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails("foo@gmail.com")
    public void deleteBlockWithWithUserSuccess() throws Exception {
        System.out.println("Given a user authenticated that is owner of the property, " +
                "it should successfully delete the block");
        mockMvc.perform(delete(String.format("/block/%s/", BookingTestUtils.RESERVATION_BLOCK_ID_HOUSE_1)))
                .andExpect(status().isOk());
    }
}