package com.instagl.aspect;

import java.util.List;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import com.instagl.entity.Location;

@Component
@Aspect
public class ShareAspect {

	@AfterReturning(pointcut = "execution(* com.instagl.service.ShareService.getLocations(..))", returning = "locations")
	public void afterReturningFindAccountsAdvice(JoinPoint joinPoint, List<Location> locations) {
		System.out.println("Aspect(@AfterReturning) :: afterReturningFindAccountsAdvice is executed after "
				+ joinPoint.getSignature().toShortString());
		System.out.println(locations.toString());
	}
}
