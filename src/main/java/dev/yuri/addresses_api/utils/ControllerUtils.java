package dev.yuri.addresses_api.utils;

import java.util.*;

public final class ControllerUtils {
    private ControllerUtils() {
        throw new UnsupportedOperationException("ControllerUtils é uma classe utilitária e não pode ser instanciada.");
    }

    public static boolean isUniqueResponse(Object... filters) {
        return Arrays.stream(filters).anyMatch(Objects::nonNull);
    }

    public static boolean isValidParams(List<String> expectedParams, Map<String, String> params) {
        return new HashSet<>(expectedParams).containsAll(params.keySet());
    }
}
