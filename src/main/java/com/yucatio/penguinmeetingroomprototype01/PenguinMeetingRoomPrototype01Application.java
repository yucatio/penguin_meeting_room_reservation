package com.yucatio.penguinmeetingroomprototype01;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.hibernate.validator.HibernateValidator;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.beanvalidation.SpringConstraintValidatorFactory;

@SpringBootApplication
public class PenguinMeetingRoomPrototype01Application {

  public static void main(String[] args) {
    SpringApplication.run(PenguinMeetingRoomPrototype01Application.class, args);
  }

  @Bean
  public Validator localValidatorFactoryBean(final AutowireCapableBeanFactory autowireCapableBeanFactory) {
    ValidatorFactory validatorFactory = Validation.byProvider(HibernateValidator.class)
        .configure().constraintValidatorFactory(new SpringConstraintValidatorFactory(autowireCapableBeanFactory))
        .buildValidatorFactory();
    Validator validator = validatorFactory.getValidator();

    return validator;
  }

}
