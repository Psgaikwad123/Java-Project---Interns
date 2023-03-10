package com.infy.infyinterns.utility;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.infy.infyinterns.exception.InfyInternException;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect
{

    private static final Log LOGGER = LogFactory.getLog(LoggingAspect.class);

    @AfterThrowing(pointcut = "execution(* com.infy.infyinterns.service.ProjectAllocationImpl.*(..))", throwing = "exception")
    public void logServiceException(InfyInternException exception)
    {
        LOGGER.error(exception.getMessage(),exception);
	// code
    }

}
