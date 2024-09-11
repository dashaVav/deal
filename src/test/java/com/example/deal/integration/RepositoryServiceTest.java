package com.example.deal.integration;

import com.example.deal.dto.*;
import com.example.deal.dto.enums.EmploymentPosition;
import com.example.deal.dto.enums.EmploymentStatus;
import com.example.deal.dto.enums.Gender;
import com.example.deal.dto.enums.MaritalStatus;
import com.example.deal.exception.ApplicationNotFoundException;
import com.example.deal.exception.OfferDoesNotExistException;
import com.example.deal.mapper.MergeClientService;
import com.example.deal.model.Application;
import com.example.deal.model.Credit;
import com.example.deal.model.enums.ApplicationStatus;
import com.example.deal.model.enums.CreditStatus;
import com.example.deal.repository.JpaApplicationRepository;
import com.example.deal.repository.JpaClientRepository;
import com.example.deal.service.RepositoryService;
import com.example.deal.service.impl.RepositoryServiceImpl;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.convert.ConversionService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
public class RepositoryServiceTest extends IntegrationEnvironment {
    private RepositoryService repositoryService;

    @Autowired
    private JpaClientRepository clientRepository;

    @Autowired
    private JpaApplicationRepository applicationRepository;

    @Autowired
    private ConversionService conversionService;

    @Autowired
    private MergeClientService mergeClientService;

    private Application application;

    @BeforeEach
    void setUp() {
        repositoryService = new RepositoryServiceImpl(clientRepository, applicationRepository, conversionService, mergeClientService);

        //save 1 application in db
        application = repositoryService.getApplicationById(
                repositoryService.createApplicationWithClient(getTestLoanApplicationRequestDTO())
        );
    }

    private LoanApplicationRequestDTO getTestLoanApplicationRequestDTO() {
        return new LoanApplicationRequestDTO(BigDecimal.valueOf(100000),
                6, "User", "Test", "Test", "user@mail.com",
                LocalDate.of(2000, 1, 1), "0000", "000000"
        );
    }

    @Transactional
    @Test
    void testCreateApplicationWithClient() {
        Long id = repositoryService.createApplicationWithClient(getTestLoanApplicationRequestDTO());
        assertTrue(applicationRepository.findAll().stream().anyMatch(app -> app.getApplicationId().equals(id)));
    }

    @Transactional
    @Test
    void testValidateOfferDoesNotThrow() {
        List<LoanOfferDTO> offers = getTestLoanOffers(application.getApplicationId());
        application.setLoanOffers(offers);
        applicationRepository.save(application);

        assertDoesNotThrow(() -> repositoryService.validateOffer(offers.getFirst()));
    }

    private List<LoanOfferDTO> getTestLoanOffers(Long applicationId) {
        List<LoanOfferDTO> loanOfferDTOList = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            loanOfferDTOList.add(
                    new LoanOfferDTO(applicationId, BigDecimal.valueOf(100000),
                            BigDecimal.valueOf(100000), 6, BigDecimal.valueOf(10000),
                            BigDecimal.valueOf(10), true, true)
            );
        }
        return loanOfferDTOList;
    }

    @Transactional
    @Test
    void testValidateOfferThrow() {
        List<LoanOfferDTO> offers = getTestLoanOffers(application.getApplicationId());
        application.setLoanOffers(offers);
        applicationRepository.save(application);
        LoanOfferDTO loanOfferDTODoesNotExist = new LoanOfferDTO(application.getApplicationId(), BigDecimal.valueOf(100000), BigDecimal.valueOf(100000),
                0, BigDecimal.valueOf(10000), BigDecimal.valueOf(10), true, true);

        assertThrows(OfferDoesNotExistException.class, () -> repositoryService.validateOffer(loanOfferDTODoesNotExist));
        assertEquals(applicationRepository.findById(application.getApplicationId()).get().getApplicationStatus(), ApplicationStatus.CLIENT_DENIED);
    }

    @Transactional
    @Test
    void testGetEmailAddressByApplicationId() {
        String expectedEmailAddress = getTestLoanApplicationRequestDTO().getEmail();

        assertEquals(expectedEmailAddress, repositoryService.getEmailAddressByApplicationId(application.getApplicationId()));
    }

    private FinishRegistrationRequestDTO getTestFinishRegistrationRequestDTO() {
        return new FinishRegistrationRequestDTO(
                Gender.FEMALE, MaritalStatus.MARRIED, 1, LocalDate.now().plusDays(10),
                "branch", new EmploymentDTO(EmploymentStatus.EMPLOYED, "1234567890",
                BigDecimal.valueOf(10000), EmploymentPosition.WORKER, 12, 10),
                "1234567890");
    }

    @Transactional
    @Test
    void testSaveClientAdditionalInfo() {
        FinishRegistrationRequestDTO finishRegistration = getTestFinishRegistrationRequestDTO();

        repositoryService.saveClientAdditionalInfo(finishRegistration, application.getApplicationId());

        Application savedApplication = applicationRepository.findById(application.getApplicationId()).get();
        assertEquals(savedApplication.getClient().getEmail(), application.getClient().getEmail());
        assertEquals(savedApplication.getClient().getGender(), finishRegistration.getGender());
        assertEquals(savedApplication.getClient().getMaritalStatus(), finishRegistration.getMaritalStatus());
        assertEquals(savedApplication.getClient().getDependentAmount(), finishRegistration.getDependentAmount());
        assertEquals(savedApplication.getClient().getEmployment(), finishRegistration.getEmployment());
        assertEquals(savedApplication.getClient().getAccount(), finishRegistration.getAccount());
        assertEquals(savedApplication.getClient().getPassport().getIssueBranch(), finishRegistration.getPassportIssueBranch());
        assertEquals(savedApplication.getClient().getPassport().getIssueDate(), finishRegistration.getPassportIssueDate());
    }

    @Transactional
    @Test
    void testCalculate() {
        CreditDTO creditDTO = getTestCreditDTO();

        repositoryService.calculate(application.getApplicationId(), creditDTO);

        Application savedApplication = applicationRepository.findById(application.getApplicationId()).get();
        Credit credit = savedApplication.getCredit();
        assertEquals(savedApplication.getApplicationStatus(), ApplicationStatus.CC_APPROVED);
        assertEquals(credit.getCreditStatus(), CreditStatus.CALCULATED);
        assertEquals(credit.getAmount(), creditDTO.getAmount());
        assertEquals(credit.getTerm(), creditDTO.getTerm());
    }

    @Transactional
    @Test
    void testOffer() {
        LoanOfferDTO loanOffer = getTestLoanOffers(application.getApplicationId()).getFirst();

        repositoryService.offer(loanOffer);

        Application savedApplication = applicationRepository.findById(application.getApplicationId()).get();
        assertEquals(savedApplication.getApplicationStatus(), ApplicationStatus.APPROVED);
        assertEquals(savedApplication.getAppliedOffer(), loanOffer);
    }

    @Transactional
    @Test
    void testGetApplicationByIdThrowException() {
        assertThrows(ApplicationNotFoundException.class,
                () -> repositoryService.getApplicationById(application.getApplicationId() + 1L));
    }

    @Transactional
    @Test
    void testGetApplicationById() {
        assertEquals(repositoryService.getApplicationById(application.getApplicationId()), application);
    }

    @Transactional
    @Test
    void testSetApplicationStatus() {
        ApplicationStatus applicationStatus = ApplicationStatus.APPROVED;

        repositoryService.setApplicationStatus(application.getApplicationId(), applicationStatus);

        assertEquals(applicationRepository.findById(application.getApplicationId()).get().getApplicationStatus(),
                applicationStatus);
    }

    @Transactional
    @Test
    void testGetAllApplications() {
        List<Application> applications = repositoryService.getAllApplications();
        assertEquals(applications.size(), 1);
        assertEquals(applications.getFirst(), application);
    }

    @Transactional
    @Test
    void testGetScoringData() {
        FinishRegistrationRequestDTO dto = getTestFinishRegistrationRequestDTO();
        repositoryService.saveClientAdditionalInfo(dto, application.getApplicationId());
        repositoryService.offer(getTestLoanOffers(application.getApplicationId()).getFirst());

        ScoringDataDTO expectedScoringData = new ScoringDataDTO(
                BigDecimal.valueOf(100000), 6, "User", "Test", "Test",
                Gender.FEMALE, LocalDate.of(2000, 1, 1), "0000",
                "000000", LocalDate.now().plusDays(10), "branch",
                MaritalStatus.MARRIED, 1,
                new EmploymentDTO(EmploymentStatus.EMPLOYED, "1234567890", BigDecimal.valueOf(10000), EmploymentPosition.WORKER, 12, 10),
                "1234567890", true, true
        );

        assertEquals(repositoryService.getScoringData(dto, application.getApplicationId()), expectedScoringData);
    }

    @Transactional
    @Test
    void setSesCode() {
        String sesCode = UUID.randomUUID().toString();

        repositoryService.setSesCode(application.getApplicationId(), sesCode);

        assertEquals(sesCode, applicationRepository.findById(application.getApplicationId()).get().getSesCode());
    }

    @Transactional
    @Test
    void getSesCode() {
        String expectedSesCode = UUID.randomUUID().toString();
        application.setSesCode(expectedSesCode);
        applicationRepository.save(application);

        String sesCode = repositoryService.getSesCode(application.getApplicationId());

        assertEquals(sesCode, expectedSesCode);
    }

    @Transactional
    @Test
    void testSaveLoanOffers() {
        List<LoanOfferDTO> loanOffers = getTestLoanOffers(application.getApplicationId());

        repositoryService.saveLoanOffers(application.getApplicationId(), loanOffers);

        assertEquals(applicationRepository.findById(application.getApplicationId()).get().getLoanOffers(), loanOffers);
    }

    @Transactional
    @Test
    void testGetApplicationStatus() {
        ApplicationStatus applicationStatus = repositoryService.getApplicationStatus(application.getApplicationId());

        assertEquals(applicationStatus, application.getApplicationStatus());
    }

    @Transactional
    @Test
    void testSetSignDate() {
        repositoryService.setSignDate(application.getApplicationId());

        assertNotNull(application.getSignDate());
    }

    private CreditDTO getTestCreditDTO() {
        return new CreditDTO(
                BigDecimal.valueOf(10000), 10, BigDecimal.valueOf(1000), BigDecimal.valueOf(10),
                BigDecimal.valueOf(10000), true, true, new ArrayList<>()
        );
    }

    @Transactional
    @Test
    void testSetCreditStatus() {
        repositoryService.calculate(application.getApplicationId(), getTestCreditDTO());
        repositoryService.setCreditStatus(application.getApplicationId(), CreditStatus.ISSUED);

        assertEquals(applicationRepository.findById(application.getApplicationId()).get().getCredit().getCreditStatus(),
                CreditStatus.ISSUED);
    }
}
