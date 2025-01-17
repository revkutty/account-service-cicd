package boa.account_service_cicd.Controller;

import boa.account_service_cicd.DTO.UserDTO;
import boa.account_service_cicd.Models.Accounts;
import boa.account_service_cicd.Services.AccountServiceImpl;
import boa.account_service_cicd.Services.Interfaces.AccountService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/account")
public class AccountServiceController {

    private static final Logger logger = LogManager.getLogger(AccountServiceController.class);

    private final RestTemplate restTemplate;

    private final AccountService accountService;

    @Value("${user.service.url}")
    private String userServiceUrl;

    public AccountServiceController(RestTemplate restTemplate, AccountService accountService) {
        this.restTemplate = restTemplate;
        this.accountService = accountService;
    }
    /*
     @Autowired
    public AccountController(AccountServiceImpl accServiceImpl,
                             @Value("${user.microservice.url}") String userMicroserviceUrl) {
        this.accServiceImpl = accServiceImpl;
       // this.userDto = userDto;
        this.USER_MICROSERVICE_URL = userMicroserviceUrl;
    }
     */

    @GetMapping
    public List<Accounts> getAllAccounts() {
        return accountService.getAllAccounts();
    }

    @GetMapping("/user/{userId}")
    public List<Accounts> getAccountsByUserId(@PathVariable Long userId) {
        return accountService.getAccountsByUserId(userId);
    }


    @PostMapping
    public ResponseEntity<Accounts> createAccount(@RequestBody Accounts account) {
        try {
            // Construct the full user service URL
            String userUrl = userServiceUrl + account.getUserId();

            // Fetch user details from the user service
            UserDTO userDetails = restTemplate.getForObject(userUrl, UserDTO.class);

            // Check if the user's KYC status is approved
            if (userDetails == null || !"approved".equalsIgnoreCase(userDetails.getKycStatus())) {
                return ResponseEntity.badRequest().body(null);
            }

            // Create and save the account
            Accounts savedAccount = accountService.createAccount(account);
            return ResponseEntity.ok(savedAccount);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<Accounts> getAccountById(@PathVariable Long accountId) {
        logger.info("Received request for get account ID: {}", accountId);
        return accountService.getAccountById(accountId).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{accountId}")
    public ResponseEntity<Accounts> updateAccount(@PathVariable Long accountId, @RequestBody Accounts updatedAccount) {
        logger.info("Received request for update account for account ID: {}", accountId);
        return accountService.updateAccount(accountId, updatedAccount).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{accountId}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long accountId) {
        if (accountService.deleteAccount(accountId)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }


    @GetMapping("/balance/{accountNumber}")
    public ResponseEntity<BigDecimal> getAccountBalance(@PathVariable String accountNumber) {
        return accountService.getAccountByAccountNumber(accountNumber)
                .map(account -> ResponseEntity.ok(account.getBalance()))
                .orElse(ResponseEntity.notFound().build());
    }


    @PutMapping("/{accountId}/update-balance")
    public ResponseEntity<Void> updateAccountBalance(@PathVariable Long accountId, @RequestBody Accounts accounts) {
        // Call the service to update the balance
        accountService.updateAccountBalance(accountId, accounts.getBalance());
        return ResponseEntity.ok().build();
    }


    @GetMapping("/user-details/{userId}")
    public ResponseEntity<UserDTO> getUserDetails(@PathVariable Long userId) {
        try {
            // Construct the full URL using the injected property
            String userUrl = userServiceUrl + userId;

            // Fetch user details from the user service
            UserDTO userDetails = restTemplate.getForObject(userUrl, UserDTO.class);

            // Return the user details in the response
            return ResponseEntity.ok(userDetails);
        } catch (Exception e) {
            // catch (HttpClientErrorException | HttpServerErrorException e)
            // Handle errors (e.g., user not found)
            return ResponseEntity.notFound().build();
        }
    }



    @ExceptionHandler
    public ResponseEntity<?> respondWithError(Exception e){
        return new ResponseEntity<>("Exception Occurred:" + e.getMessage(), HttpStatus.BAD_REQUEST);
    }

}
