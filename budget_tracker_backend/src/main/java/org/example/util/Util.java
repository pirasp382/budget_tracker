package org.example.util;

import lombok.experimental.UtilityClass;
import org.example.entity.Account;
import org.example.entity.Transaction;
import org.example.enums.Type;

import java.lang.reflect.Field;
import java.util.Arrays;

@UtilityClass
public class Util {

    public static boolean typeMatches(final String inputType) {
        return Arrays.stream(Type.values()).anyMatch(type -> type.name().equals(inputType));
    }

    public static boolean tableContainsColumn(final String columnName, final boolean isAccount) {
        final Field[] columns =
                isAccount ? Account.class.getDeclaredFields() : Transaction.class.getDeclaredFields();
        return Arrays.stream(columns)
                .filter(item -> !item.getName().startsWith("$$_"))
                .anyMatch(item -> item.getName().equals(columnName));
    }

}
