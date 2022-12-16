package bank.service.impl;



import bank.repository.TraceRecordRepository;
import bank.service.TraceRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TraceRecordServiceImpl implements TraceRecordService {
    @Autowired
    private TraceRecordRepository traceRecordRepository;


    @Override
    public void save(TraceRecord traceRecord) {
        traceRecordRepository.save(traceRecord);
    }
}
