package bank.repository;

import bank.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
   @Query("select a from Account a where a.accountNumber = :accountNumber")
   Account findByAccountNumber(long accountNumber);

}
