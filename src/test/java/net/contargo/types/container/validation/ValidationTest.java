package net.contargo.types.container.validation;

import org.junit.Before;

import org.junit.runner.RunWith;

import org.mockito.Mock;

import org.mockito.runners.MockitoJUnitRunner;

import java.util.Set;

import javax.validation.Configuration;
import javax.validation.ConstraintViolation;
import javax.validation.MessageInterpolator;
import javax.validation.MessageInterpolator.Context;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import static org.hamcrest.core.Is.is;

import static org.junit.Assert.assertThat;

import static org.mockito.AdditionalAnswers.returnsFirstArg;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;

import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public abstract class ValidationTest {

    @Mock
    private MessageInterpolator messageInterpolator;

    private Validator validator;

    @Before
    public void setup() {

        Configuration<?> config = Validation.byDefaultProvider().configure();
        config.messageInterpolator(messageInterpolator);

        ValidatorFactory factory = config.buildValidatorFactory();
        validator = factory.getValidator();

        when(messageInterpolator.interpolate(anyString(), any(Context.class))).then(returnsFirstArg());
    }


    public void assertPropertyHasNoErrors(Object object, String property) {

        Set<ConstraintViolation<Object>> validationResult = validator.validateProperty(object, property);
        assertThat(validationResult.size(), is(0));
    }


    public void assertPropertyHasErrors(Object object, String property, String annotation) {

        Set<ConstraintViolation<Object>> validationResult = validator.validateProperty(object, property);

        assertThat(validationResult.size(), is(1));
        assertThat(validationResult.iterator().next().getMessage(), is(annotation));
    }
}
