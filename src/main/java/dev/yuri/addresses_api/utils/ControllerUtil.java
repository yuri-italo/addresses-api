package dev.yuri.addresses_api.utils;

import dev.yuri.addresses_api.exception.InvalidFilterException;
import org.springframework.context.MessageSource;

import java.util.*;

public final class ControllerUtil {
    public static final Locale LOCALE_PT_BR = new Locale("pt", "BR");

    private ControllerUtil() {
        throw new UnsupportedOperationException("ControllerUtils é uma classe utilitária e não pode ser instanciada.");
    }

    public static void validateFilters(List<String> expectedFilters, Map<String, String> filters, MessageSource messageSource) {
        var invalidFilters = ControllerUtil.getInvalidFilters(expectedFilters, filters);
        if (!invalidFilters.isEmpty()) {
            throw new InvalidFilterException(
                    messageSource.getMessage("error.invalid.filters", new Object[]{invalidFilters}, LOCALE_PT_BR)
            );
        }
    }

    public static boolean isFiltersApplied(Object... filters) {
        return Arrays.stream(filters).anyMatch(Objects::nonNull);
    }

    public static List<String> getInvalidFilters(List<String> expectedFilters, Map<String, String> filters) {
        Set<String> expectedFilterSet = new HashSet<>(expectedFilters);

        return filters.keySet().stream()
                .filter(filter -> !expectedFilterSet.contains(filter))
                .toList();
    }
}
