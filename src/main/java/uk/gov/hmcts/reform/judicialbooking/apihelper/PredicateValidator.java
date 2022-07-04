package uk.gov.hmcts.reform.judicialbooking.apihelper;

import java.time.LocalDate;
import java.util.function.Predicate;

import org.springframework.util.ObjectUtils;

public class PredicateValidator {

    private PredicateValidator() {
    }

    public static final Predicate<LocalDate> datePredicates = ObjectUtils::isEmpty;
    public static final Predicate<String> valuePredicates = ObjectUtils::isEmpty;
}
