package boa.account_service_cicd.Models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "accounts")
@Data
@NoArgsConstructor
public class Accounts {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false, unique = true, length = 20)
    private String accountNumber;

    @Column(nullable = false, length = 20)
    private String accountType;

    @Column(nullable = false, columnDefinition = "DECIMAL(15, 2) DEFAULT 0.0")
    private BigDecimal balance = BigDecimal.valueOf(0.0);

    @Column(nullable = false, columnDefinition = "VARCHAR(10) DEFAULT 'USD'")
    private String currency = "USD";

    @Column(nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt = LocalDateTime.now();
}