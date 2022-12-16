package bank.advise;

import bank.logging.LoggerService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Aspect
@Configuration
public class DaoLoggingAdvice {
	@Autowired
	private LoggerService logger;



	@After("execution(* bank.repository.*.*(..))")
	public void log(JoinPoint joinpoint) {
		logger.log("Call was made to:" + joinpoint.getSignature().getName()
				+ " on " + joinpoint.getTarget().getClass());
	}
}
