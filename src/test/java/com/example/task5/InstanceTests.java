package com.example.task5;

import com.example.task5.entities.AgreementEntity;
import com.example.task5.entities.TppProductEntity;
import com.example.task5.enums.ProductType;
import com.example.task5.instance.dto.InstanceDto;
import com.example.task5.repo.AgreementRepo;
import com.example.task5.repo.TppProductRegisterRepo;
import com.example.task5.repo.TppProductRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class InstanceTests {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    TppProductRepo tppProductRepo;
    @Autowired
    TppProductRegisterRepo tppProductRegisterRepo;
    @Autowired
    AgreementRepo agreementRepo;
    MvcResult result;

    String asJsonString(Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeEach
    void beforeSetup() {
        agreementRepo.deleteAll();
        tppProductRepo.deleteAll();
        tppProductRegisterRepo.deleteAll();
    }

    @Test
    void testCreateOkRequestWithNullInstanceID() throws Exception {
        AgreementEntity agreement = new AgreementEntity();
        agreement.setNumber("1");
        agreement.setOpeningDate(Date.valueOf(LocalDate.now()));
        List<AgreementEntity> agreements = new ArrayList<>();
        agreements.add(agreement);

        InstanceDto instanceDto = InstanceDto.builder()
                .productType(ProductType.DBDS)
                .productCode("03.012.002")
                .contractNumber("1")
                .contractDate(Date.valueOf(LocalDate.now()))
                .contractId(1)
                .urgencyCode("1")
                .referenceCode(1)
                .branchCode("0022")
                .isoCurrencyCode("800")
                .mdmCode("15")
                .urgencyCode("00")
                .priority(1L)
                .registerType("03.012.002_47533_ComSoLd")
                .agreements(agreements)
                .build();

        result = mockMvc.perform(post("/corporate-settlement-instance/create")
                        .content(asJsonString(instanceDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(1, tppProductRepo.count());
        assertEquals(1, tppProductRegisterRepo.count());
    }

    @Test
    void testCreateOkRequestWithNotNullInstanceID() throws Exception {
        TppProductEntity tppProduct = new TppProductEntity();
        tppProductRepo.save(tppProduct);

        AgreementEntity agreement = new AgreementEntity();
        agreement.setNumber("1");
        agreement.setOpeningDate(Date.valueOf(LocalDate.now()));
        List<AgreementEntity> agreements = new ArrayList<>();
        agreements.add(agreement);

        InstanceDto instanceDto = InstanceDto.builder()
                .instanceId(tppProduct.getId())
                .productType(ProductType.DBDS)
                .productCode("03.012.002")
                .contractNumber("1")
                .contractDate(Date.valueOf(LocalDate.now()))
                .contractId(1)
                .urgencyCode("1")
                .referenceCode(1)
                .branchCode("0022")
                .isoCurrencyCode("800")
                .mdmCode("15")
                .urgencyCode("00")
                .priority(1L)
                .registerType("03.012.002_47533_ComSoLd")
                .agreements(agreements)
                .build();

        result = mockMvc.perform(post("/corporate-settlement-instance/create")
                        .content(asJsonString(instanceDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(1, tppProductRepo.count());
        assertEquals(1, agreementRepo.count());
    }

    @Test
    void testCreateBadRequestWithNullRequiredParams() throws Exception {

        InstanceDto instanceDto = InstanceDto.builder()
                .instanceId(null)
                .build();

        result = mockMvc.perform(post("/corporate-settlement-instance/create")
                        .content(asJsonString(instanceDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void testCreateBadRequestWithExistingProductAndNullInstanceID() throws Exception {
        TppProductEntity tppProduct = new TppProductEntity();
        tppProduct.setNumber("contract_number_tst");
        tppProductRepo.save(tppProduct);

        AgreementEntity agreement = new AgreementEntity();
        agreement.setNumber("1");
        agreement.setOpeningDate(Date.valueOf(LocalDate.now()));
        List<AgreementEntity> agreements = new ArrayList<>();
        agreements.add(agreement);

        InstanceDto instanceDto = InstanceDto.builder()
                .productType(ProductType.DBDS)
                .productCode("03.012.002")
                .contractNumber("contract_number_tst")
                .contractDate(Date.valueOf(LocalDate.now()))
                .contractId(1)
                .urgencyCode("1")
                .referenceCode(1)
                .branchCode("0022")
                .isoCurrencyCode("800")
                .mdmCode("15")
                .urgencyCode("00")
                .priority(1L)
                .registerType("03.012.002_47533_ComSoLd")
                .agreements(agreements)
                .build();

        result = mockMvc.perform(post("/corporate-settlement-instance/create")
                        .content(asJsonString(instanceDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void testCreateBadRequestWithExistingAgreementAndNullInstanceID() throws Exception {
        AgreementEntity agreement = new AgreementEntity();
        agreement.setNumber("agreement_num_tst");
        agreement.setOpeningDate(Date.valueOf(LocalDate.now()));
        List<AgreementEntity> agreements = new ArrayList<>();
        agreements.add(agreement);
        agreementRepo.save(agreement);

        InstanceDto instanceDto = InstanceDto.builder()
                .productType(ProductType.DBDS)
                .productCode("03.012.002")
                .contractNumber("contract_number_tst")
                .contractDate(Date.valueOf(LocalDate.now()))
                .contractId(1)
                .urgencyCode("1")
                .referenceCode(1)
                .branchCode("0022")
                .isoCurrencyCode("800")
                .mdmCode("15")
                .urgencyCode("00")
                .priority(1L)
                .registerType("03.012.002_47533_ComSoLd")
                .agreements(agreements)
                .build();

        result = mockMvc.perform(post("/corporate-settlement-instance/create")
                        .content(asJsonString(instanceDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void testCreateBadRequestWithExistingAgreementAndNotNullInstanceID() throws Exception {
        AgreementEntity agreement = new AgreementEntity();
        agreement.setNumber("1");
        agreement.setOpeningDate(Date.valueOf(LocalDate.now()));
        List<AgreementEntity> agreements = new ArrayList<>();
        agreements.add(agreement);

        InstanceDto instanceDto = InstanceDto.builder()
                .instanceId(-1L)
                .productType(ProductType.DBDS)
                .productCode("03.012.002")
                .contractNumber("contract_number_tst")
                .contractDate(Date.valueOf(LocalDate.now()))
                .contractId(1)
                .urgencyCode("1")
                .referenceCode(1)
                .branchCode("0022")
                .isoCurrencyCode("800")
                .mdmCode("15")
                .urgencyCode("00")
                .priority(1L)
                .registerType("03.012.002_47533_ComSoLd")
                .agreements(agreements)
                .build();

        result = mockMvc.perform(post("/corporate-settlement-instance/create")
                        .content(asJsonString(instanceDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void testCreateNotFoundRequestWithNotExistingProductAndNotNullInstanceID() throws Exception {
        TppProductEntity tppProduct = new TppProductEntity();
        tppProductRepo.save(tppProduct);

        AgreementEntity agreement = new AgreementEntity();
        agreement.setNumber("1");
        agreement.setOpeningDate(Date.valueOf(LocalDate.now()));
        List<AgreementEntity> agreements = new ArrayList<>();
        agreements.add(agreement);
        agreementRepo.save(agreement);

        InstanceDto instanceDto = InstanceDto.builder()
                .instanceId(tppProduct.getId())
                .productType(ProductType.DBDS)
                .productCode("03.012.002")
                .contractNumber("contract_number_tst")
                .contractDate(Date.valueOf(LocalDate.now()))
                .contractId(1)
                .urgencyCode("1")
                .referenceCode(1)
                .branchCode("0022")
                .isoCurrencyCode("800")
                .mdmCode("15")
                .urgencyCode("00")
                .priority(1L)
                .registerType("03.012.002_47533_ComSoLd")
                .agreements(agreements)
                .build();

        result = mockMvc.perform(post("/corporate-settlement-instance/create")
                        .content(asJsonString(instanceDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }
}
