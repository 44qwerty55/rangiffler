package org.rangiffler.service;

import org.rangiffler.model.EqualPasswords;
import org.rangiffler.model.RegistrationModel;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EqualPasswordsValidator implements ConstraintValidator<EqualPasswords, RegistrationModel> {
    @Override
    public boolean isValid(RegistrationModel form, ConstraintValidatorContext context) {
        boolean isValid = form.getPassword().equals(form.getPasswordSubmit());
        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                    .addPropertyNode("password")
                    .addConstraintViolation();
        }
        return isValid;
    }
}
