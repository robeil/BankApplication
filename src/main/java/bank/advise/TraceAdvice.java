package bank.advise;


import bank.domain.Account;
import bank.domain.Customer;
import bank.logging.ILogger;
import bank.logging.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Aspect
@Configuration
public class TraceAdvice {

    @Autowired
    private ILogger logger;

    @After("execution(* bank.repository.AccountRepository.*(..))")
    public void traceAfterMethod(JoinPoint joinpoint) {
        Object object = joinpoint.getTarget();
//        AccountDAO account = (AccountDAO) object;
        LocalTime localTime = LocalTime.now();

        System.out.println( LocalDateTime.now().getMonth() + " " +
                LocalDateTime.now().getDayOfMonth() + ", " +
                localTime + " PM " +
                object.getClass());
//        System.out.println("Info : " + joinpoint.getSignature().getName() + " with parameters accountNumber " + accountNumber + " ," +
//                " customerName = " );

        logger.log(LocalDateTime.now() +" PM " + joinpoint.getSignature().getName() + object.getClass());
    }

    @Around("execution(* bank.service.impl.AccountService.*(..)) && args(accountNumber, amount)")
    public Object invoke(ProceedingJoinPoint call, String accountNumber, double amount) throws Throwable {

        Object object = call.getTarget();
        long startTime = System.currentTimeMillis();
        Object requiredTime = call.proceed();
        long endTime = System.currentTimeMillis();
        long totalTimeRequired = endTime - startTime;

        logger.log("ClassName: " + call.getSignature().getDeclaringTypeName()+" " +
                " MethodName: " +call.getSignature().getName() +
                ": time taken for Execution is : ===> " + totalTimeRequired + " milliseconds.");
        return requiredTime;
    }

    @After("execution(* bank.integration.EmailSender.sendEmail(..)) && args(message)")
    public void traceAfterEmailSent(JoinPoint joinpoint, String message) {

        logger.log("ClassName = " + joinpoint.getSignature().getDeclaringTypeName() +
                " methodName = " + joinpoint.getSignature().getName() +
                " messageSent = " + message );
    }

    @Bean
    public Logger getLogger(){
        return new Logger();
    }
}
