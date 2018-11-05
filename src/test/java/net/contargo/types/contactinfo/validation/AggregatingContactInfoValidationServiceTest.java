package net.contargo.types.contactinfo.validation;

import net.contargo.types.contactinfo.ContactInformation;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AggregatingContactInfoValidationServiceTest {

    @Mock
    private CompletenessValidator completenessValidator;

    @Mock
    private UniquenessValidator uniquenessValidator;

    @Test
    public void ensureThatCompletenessAndUniquenessValidatorsAreCalled() {
        ContactInformation contactInformation = new ContactInformation("userUUID", "mobile", "phone", "email", "communicationEmail");
        AggregatingContactInfoValidationService sut = new AggregatingContactInfoValidationService(uniquenessValidator, completenessValidator);
        sut.validate(contactInformation);
        verify(completenessValidator).checkCompleteness(isA(ContactInformation.class));
        verify(uniquenessValidator).checkUniqueness(isA(ContactInformation.class));
    }

    @Test
    public void ensureThatValidationResultsAreAggregated() {
        ContactInformation contactInformation = new ContactInformation("userUUID", "mobile", "phone", "email", "communicationEmail");
        AggregatingContactInfoValidationService sut = new AggregatingContactInfoValidationService(uniquenessValidator, completenessValidator);
        List<ValidationResult> validationResults1 = new ArrayList<>();
        validationResults1.add(ValidationResult.MISSING_MOBILE);
        validationResults1.add(ValidationResult.MISSING_EMAIL);

        List<ValidationResult> validationResults2 = new ArrayList<>();
        validationResults2.add(ValidationResult.NON_UNIQUE_MOBILE);
        validationResults2.add(ValidationResult.NON_UNIQUE_EMAIL);
        validationResults2.add(ValidationResult.NON_UNIQUE_PHONE);

        when(completenessValidator.checkCompleteness(any())).thenReturn(validationResults1);
        when(uniquenessValidator.checkUniqueness(any())).thenReturn(validationResults2);

        List<ValidationResult> aggregatedResults = sut.validate(contactInformation);
        verify(completenessValidator).checkCompleteness(isA(ContactInformation.class));
        verify(uniquenessValidator).checkUniqueness(isA(ContactInformation.class));

        Assert.assertEquals("wrong number of aggregated results", 5, aggregatedResults.size());
    }
}