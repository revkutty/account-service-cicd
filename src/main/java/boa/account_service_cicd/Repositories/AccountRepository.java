package boa.account_service_cicd.Repositories;

import boa.account_service_cicd.Models.Accounts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Accounts, Long> {
    List<Accounts> findByUserId(Long userId);

    boolean existsByAccountNumber(String accountNumber);
    Accounts findByAccountNumber(String accountNumber);

    //  Remapper findByAccountNumber(String accountNumber);
}


