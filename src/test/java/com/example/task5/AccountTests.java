package com.example.task5;

import com.example.task5.account.dto.AccountDto;
import com.example.task5.entities.TppProductRegisterEntity;
import com.example.task5.repo.TppProductRegisterRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AccountTests {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    TppProductRegisterRepo tppProductRegisterRepo;
    MvcResult result;

    @BeforeEach
    void beforeSetup() {
        tppProductRegisterRepo.deleteAll();
    }

    String asJsonString(Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testCreateOkRequest() throws Exception {
        AccountDto accountDto = AccountDto.builder()
                .instanceId(3L)
                .branchCode("0021")
                .currencyCode("500")
                .mdmCode("13")
                .priorityCode("00")
                .registryTypeCode("02.001.005_45343_CoDowFF")
                .build();

        result = mockMvc.perform(post("/corporate-settlement-account/create")
                        .content(asJsonString(accountDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(1, tppProductRegisterRepo.count());
    }

    @Test
    void testCreateBadRequestWithNullRequiredParams() throws Exception {

        AccountDto accountDTO = AccountDto.builder()
                .instanceId(null)
                .build();

        result = mockMvc.perform(post("/corporate-settlement-account/create")
                        .content(asJsonString(accountDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void testCreateBadRequestWithExistingProductRegister() throws Exception {
        TppProductRegisterEntity tppProductRegister = new TppProductRegisterEntity();
        tppProductRegister.setProductId(25L);
        tppProductRegister.setType("03.012.002_47533_ComSoLd");
        tppProductRegisterRepo.save(tppProductRegister);

        AccountDto accountDTO = AccountDto.builder()
                .instanceId(25L)
                .registryTypeCode("03.012.002_47533_ComSoLd")
                .build();

        result = mockMvc.perform(post("/corporate-settlement-account/create")
                        .content(asJsonString(accountDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void testCreateNotFoundRequestWithNotExistingProductRegisterType() throws Exception {

        AccountDto accountDTO = AccountDto.builder()
                .instanceId(3L)
                .registryTypeCode("register_type_code_not_found")
                .build();

        result = mockMvc.perform(post("/corporate-settlement-account/create")
                        .content(asJsonString(accountDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();
    }
}
