package bank.repository;


import bank.domain.TraceRecord;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface TraceRecordRepository extends MongoRepository<TraceRecord, Long> {
	@Transactional(propagation=Propagation.REQUIRES_NEW)
    public default void storeTraceRecord(TraceRecord traceRecord) {
		this.save(traceRecord);
    	
    }

}




