package boa.account_service_cicd.Services.Interfaces;

import boa.account_service_cicd.Models.Accounts;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface AccountService {

    List<Accounts> getAllAccounts();
    List<Accounts> getAccountsByUserId(Long userId);
    Optional<Accounts> getAccountById(Long accountId);
    Accounts createAccount(Accounts account);
    Optional<Accounts> updateAccount(Long accountId, Accounts updatedAccount);
    boolean deleteAccount(Long accountId);
    Optional<Accounts> getAccountByAccountNumber(String accountNumber);


    void updateAccountBalance(Long accountId, BigDecimal balance);
}
