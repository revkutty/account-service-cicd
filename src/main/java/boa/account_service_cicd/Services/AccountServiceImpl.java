package boa.account_service_cicd.Services;

import boa.account_service_cicd.Models.Accounts;
import boa.account_service_cicd.Repositories.AccountRepository;
import boa.account_service_cicd.Services.Interfaces.AccountService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {

    private static final Logger logger = LogManager.getLogger(AccountServiceImpl.class);


    private final AccountRepository accountRepository;


    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public List<Accounts> getAllAccounts() {
        return accountRepository.findAll();
    }


    @Override
    public List<Accounts> getAccountsByUserId(Long userId) {
        return accountRepository.findByUserId(userId);
    }

    @Override
    public Optional<Accounts> getAccountById(Long accountId) {
        return accountRepository.findById(accountId);
    }


    @Override
    public Accounts createAccount(Accounts account) {
        if (accountRepository.existsByAccountNumber(account.getAccountNumber())) {
            throw new IllegalArgumentException("Account number already exists");
        }
        return accountRepository.save(account);
    }

    @Override
    public Optional<Accounts> updateAccount(Long accountId, Accounts updatedAccount) {
        return accountRepository.findById(accountId).map(account -> {
            account.setAccountType(updatedAccount.getAccountType());
            account.setBalance(updatedAccount.getBalance() != null ? updatedAccount.getBalance() : BigDecimal.valueOf(0.0));
            account.setCurrency(updatedAccount.getCurrency());
            account.setCreatedAt(LocalDateTime.now());
            return accountRepository.save(account);
        });
    }

    @Override
    public boolean deleteAccount(Long accountId) {
        if (accountRepository.existsById(accountId)) {
            accountRepository.deleteById(accountId);
            return true;
        }
        return false;
    }

    @Override
    public Optional<Accounts> getAccountByAccountNumber(String accountNumber) {
        return Optional.ofNullable(accountRepository.findByAccountNumber(accountNumber));
    }

    @Override
    public void updateAccountBalance(Long accountId, BigDecimal balance) {
        // Fetch the account by ID
        Accounts accounts = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found with ID: " + accountId));

        // Update the balance
        accounts.setBalance(balance);

        // Save the updated account
        accountRepository.save(accounts);
    }

     /*
    @Override
    public Optional<Double> getAccountBalance(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber).map(Accounts::getBalance);

    }

    @Override
    public Optional<Double> getAccountBalanceByAccountId(Long accountId) {
        return accountRepository.findById(accountId).map(Accounts::getBalance);
    }

*/

}
