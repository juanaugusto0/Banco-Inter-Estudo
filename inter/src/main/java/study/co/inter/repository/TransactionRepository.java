package study.co.inter.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import study.co.inter.model.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
