package bank.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TraceRecordRepository extends MongoRepository<TraceRecord, Long> {
}
