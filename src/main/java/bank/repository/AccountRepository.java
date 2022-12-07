package bank.repository;

import bank.domain.Account;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AccountRepository extends MongoRepository<Account, Long> {
   Account findByAccountNumber(long accountNumber);

}
