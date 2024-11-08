package dev.yuri.addresses_api.utils;

import java.util.*;

public final class ControllerUtil {
    private ControllerUtil() {
        throw new UnsupportedOperationException("ControllerUtils é uma classe utilitária e não pode ser instanciada.");
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
