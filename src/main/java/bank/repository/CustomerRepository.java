package bank.repository;


import bank.domain.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface CustomerRepository extends MongoRepository<Customer, Long> {
    default void saveCustomer(Customer customer) {
    //  throw new RuntimeException("could not save customer");
        save(customer);
    }
}




