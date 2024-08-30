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
import com.example.deal.model.Client;
import com.example.deal.model.Credit;
import com.example.deal.model.Passport;
import com.example.deal.model.enums.ApplicationStatus;
import com.example.deal.model.enums.CreditStatus;
import com.example.deal.repository.JpaApplicationRepository;
import com.example.deal.repository.JpaClientRepository;
import com.example.deal.service.RepositoryService;
import com.example.deal.service.impl.RepositoryServiceImpl;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
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

    @BeforeEach
    void setUp() {
        repositoryService = new RepositoryServiceImpl(clientRepository, applicationRepository, conversionService, mergeClientService);
    }

    @Transactional
    @Test
    void testCreateApplicationWithClient() {
        LoanApplicationRequestDTO loanApplicationRequest = new LoanApplicationRequestDTO(BigDecimal.valueOf(100000),
                6, "User", "Test", "Test", "user@mail.com",
                LocalDate.of(2000, 1, 1), "0000", "000000"
        );

        Long id = repositoryService.createApplicationWithClient(loanApplicationRequest);
        assertEquals(id, applicationRepository.findAll().stream().sorted().toList().getLast().getApplicationId());
    }

    @Transactional
    @Test
    void testValidateOfferDoesNotThrow() {
        Application application1 = new Application();
        application1.setApplicationStatus(ApplicationStatus.PREAPPROVAL);
        applicationRepository.save(application1);

        long i = applicationRepository.findAll().getFirst().getApplicationId();
        Application application = applicationRepository.findById(i).get();
        LoanOfferDTO loanOfferDTO = new LoanOfferDTO(i, BigDecimal.valueOf(100000), BigDecimal.valueOf(100000),
                10, BigDecimal.valueOf(10000), BigDecimal.valueOf(10), true, true);

        application.setLoanOffers(List.of(loanOfferDTO));
        applicationRepository.save(application);

        assertDoesNotThrow(() -> repositoryService.validateOffer(loanOfferDTO));
    }

    @Transactional
    @Test
    void testValidateOfferThrow() {
        Application application1 = new Application();
        application1.setApplicationStatus(ApplicationStatus.PREAPPROVAL);
        applicationRepository.save(application1);

        long i = applicationRepository.findAll().getFirst().getApplicationId();
        Application application = applicationRepository.findById(i).get();
        LoanOfferDTO loanOfferDTO = new LoanOfferDTO(i, BigDecimal.valueOf(100000), BigDecimal.valueOf(100000),
                10, BigDecimal.valueOf(10000), BigDecimal.valueOf(10), true, true);

        application.setLoanOffers(List.of(loanOfferDTO));
        applicationRepository.save(application);

        LoanOfferDTO loanOfferDTODoesNotExist = new LoanOfferDTO(i, BigDecimal.valueOf(100000), BigDecimal.valueOf(100000),
                1, BigDecimal.valueOf(10000), BigDecimal.valueOf(10), true, true);
        assertThrows(OfferDoesNotExistException.class, () -> repositoryService.validateOffer(loanOfferDTODoesNotExist));
        assertEquals(applicationRepository.findById(i).get().getApplicationStatus(), ApplicationStatus.CLIENT_DENIED);
    }

    @Transactional
    @Test
    void testGetEmailAddressByApplicationId() {
        String emailAddress = "test@test.com";

        Client client = clientRepository.save(new Client().setEmail(emailAddress));
        Application application = applicationRepository.save(new Application().setClient(client));

        assertEquals(emailAddress, repositoryService.getEmailAddressByApplicationId(application.getApplicationId()));
    }

    @Transactional
    @Test
    void testSaveClientAdditionalInfo() {
        FinishRegistrationRequestDTO dto = new FinishRegistrationRequestDTO(
                Gender.FEMALE, MaritalStatus.MARRIED, 1, LocalDate.now().plusDays(10), "branch",
                new EmploymentDTO(EmploymentStatus.EMPLOYED, "1234567890", BigDecimal.valueOf(10000), EmploymentPosition.WORKER, 12, 10), "1234567890");
        Client client = clientRepository.save(new Client().setLastName("Test").setFirstName("User")
                .setBirthdate(LocalDate.of(2000, 1, 1))
                .setPassport(new Passport("000000", "0000", null, null))
                .setEmail("test@mail.com"));
        Application application = applicationRepository.save(new Application().setClient(client));


        repositoryService.saveClientAdditionalInfo(dto, application.getApplicationId());

        Application savedApplication = applicationRepository.findById(application.getApplicationId()).get();
        assertEquals(savedApplication.getClient().getEmail(), client.getEmail());
        assertEquals(savedApplication.getClient().getGender(), dto.getGender());
        assertEquals(savedApplication.getClient().getMaritalStatus(), dto.getMaritalStatus());
        assertEquals(savedApplication.getClient().getDependentAmount(), dto.getDependentAmount());
        assertEquals(savedApplication.getClient().getEmployment(), dto.getEmployment());
        assertEquals(savedApplication.getClient().getAccount(), dto.getAccount());
        assertEquals(savedApplication.getClient().getPassport().getIssueBranch(), dto.getPassportIssueBranch());
        assertEquals(savedApplication.getClient().getPassport().getIssueDate(), dto.getPassportIssueDate());
    }

    @Transactional
    @Test
    void testCalculate() {
        Application application = applicationRepository.save(new Application());

        CreditDTO dto = new CreditDTO(
                BigDecimal.valueOf(10000), 10, BigDecimal.valueOf(1000), BigDecimal.valueOf(10), BigDecimal.valueOf(10000),
                true, true, new ArrayList<>()
        );

        repositoryService.calculate(application.getApplicationId(), dto);

        Application savedApplication = applicationRepository.findById(application.getApplicationId()).get();
        Credit credit = savedApplication.getCredit();
        assertEquals(savedApplication.getApplicationStatus(), ApplicationStatus.CC_APPROVED);
        assertEquals(credit.getCreditStatus(), CreditStatus.CALCULATED);
        assertEquals(credit.getAmount(), dto.getAmount());
        assertEquals(credit.getTerm(), dto.getTerm());
    }

    @Transactional
    @Test
    void testOffer() {
        Application application = applicationRepository.save(new Application());

        LoanOfferDTO loanOffer = new LoanOfferDTO(application.getApplicationId(), BigDecimal.valueOf(100000), BigDecimal.valueOf(100000),
                10, BigDecimal.valueOf(10000), BigDecimal.valueOf(10), true, true);

        repositoryService.offer(loanOffer);

        Application newApplication = applicationRepository.findById(application.getApplicationId()).get();
        assertEquals(newApplication.getApplicationStatus(), ApplicationStatus.APPROVED);
        assertEquals(newApplication.getAppliedOffer(), loanOffer);
    }

    @Transactional
    @Test
    void testGetApplicationByIdThrowException() {
        Application application = applicationRepository.save(new Application());
        applicationRepository.save(application);

        assertThrows(ApplicationNotFoundException.class, () -> repositoryService.getApplicationById(application.getApplicationId() + 1L));
    }

    @Transactional
    @Test
    void testGetApplicationById() {
        Application application = applicationRepository.save(new Application());
        applicationRepository.save(application);

        assertEquals(repositoryService.getApplicationById(application.getApplicationId()), application);
    }

    @Transactional
    @Test
    void testSetApplicationStatus() {
        Application application = applicationRepository.save(new Application());
        ApplicationStatus applicationStatus = ApplicationStatus.APPROVED;

        repositoryService.setApplicationStatus(application.getApplicationId(), applicationStatus);

        assertEquals(applicationRepository.findById(application.getApplicationId()).get().getApplicationStatus(),
                applicationStatus);
    }

    @Transactional
    @Test
    void testGetAllApplications() {
        Application application = applicationRepository.save(new Application());
        applicationRepository.save(application);

        List<Application> applications = repositoryService.getAllApplications();
        assertEquals(applications.size(), 1);
        assertEquals(applications.get(0), application);
    }

    @Transactional
    @Test
    void testGetScoringData() {
        FinishRegistrationRequestDTO dto = new FinishRegistrationRequestDTO(
                Gender.FEMALE, MaritalStatus.MARRIED, 1, LocalDate.now().plusDays(10), "branch",
                new EmploymentDTO(EmploymentStatus.EMPLOYED, "1234567890", BigDecimal.valueOf(10000), EmploymentPosition.WORKER, 12, 10), "1234567890");
        Client client = clientRepository.save(new Client().setLastName("Test").setFirstName("User")
                .setBirthdate(LocalDate.of(2000, 1, 1))
                .setPassport(new Passport("000000", "0000", null, null))
                .setEmail("test@mail.com"));
        Application application = applicationRepository.save(new Application().setClient(client));
        repositoryService.saveClientAdditionalInfo(dto, application.getApplicationId());
        LoanOfferDTO loanOffer = new LoanOfferDTO(application.getApplicationId(), BigDecimal.valueOf(100000), BigDecimal.valueOf(100000),
                10, BigDecimal.valueOf(10000), BigDecimal.valueOf(10), true, true);
        application.setAppliedOffer(loanOffer);
        applicationRepository.save(application);


        ScoringDataDTO excpectedScoringData = new ScoringDataDTO(
                BigDecimal.valueOf(100000), 10, "User", "Test", null, Gender.FEMALE, LocalDate.of(2000, 1, 1),
                "000000", "0000", LocalDate.now().plusDays(10), "branch", MaritalStatus.MARRIED, 1,
                new EmploymentDTO(EmploymentStatus.EMPLOYED, "1234567890", BigDecimal.valueOf(10000), EmploymentPosition.WORKER, 12, 10),
                "1234567890", true, true
        );


        ScoringDataDTO scoringDataDTO = repositoryService.getScoringData(dto, application.getApplicationId());

        assertEquals(scoringDataDTO, excpectedScoringData);
    }

    @Transactional
    @Test
    void setSesCode() {
        Application application = applicationRepository.save(new Application());
        String sesCode = UUID.randomUUID().toString();

        repositoryService.setSesCode(application.getApplicationId(), sesCode);

        assertEquals(sesCode, applicationRepository.findById(application.getApplicationId()).get().getSesCode());
    }

    @Transactional
    @Test
    void getSesCode() {
        Application application = applicationRepository.save(
                new Application().setSesCode(UUID.randomUUID().toString()));

        String sesCode = repositoryService.getSesCode(application.getApplicationId());

        assertEquals(sesCode, application.getSesCode());
    }

    @Transactional
    @Test
    void testSaveLoanOffers() {
        Application application = applicationRepository.save(new Application());
        List<LoanOfferDTO> loanOffers = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            loanOffers.add(
                    new LoanOfferDTO(application.getApplicationId(), BigDecimal.valueOf(100000),
                            BigDecimal.valueOf(100000), 10, BigDecimal.valueOf(10000),
                            BigDecimal.valueOf(10), true, true)
            );
        }

        repositoryService.saveLoanOffers(application.getApplicationId(), loanOffers);

        assertEquals(applicationRepository.findById(application.getApplicationId()).get().getLoanOffers(), loanOffers);
    }

    @Transactional
    @Test
    void testGetApplicationStatus() {
        Application application = applicationRepository.save(
                new Application().setApplicationStatus(ApplicationStatus.APPROVED));

        ApplicationStatus applicationStatus = repositoryService.getApplicationStatus(application.getApplicationId());

        assertEquals(applicationStatus, application.getApplicationStatus());
    }

    @Transactional
    @Test
    void testSetSignDate() {
        Application application = applicationRepository.save(new Application());

        repositoryService.setSignDate(application.getApplicationId());

        assertNotNull(application.getSignDate());
    }


    @AfterEach
    void tearDown() {
        applicationRepository.deleteAll();
    }
}
