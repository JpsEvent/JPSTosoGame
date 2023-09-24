package pakira.util;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class Validate {


    @Nonnull
    public static <T> T notNull(@Nullable T object) {
        if (object == null)
            throw new NullPointerException();
        return object;
    }

    @Nonnull
    public static <T> T notNull(@Nullable T object, @Nonnull String message) {
        if (object == null)
            throw new NullPointerException(message);
        return object;
    }

    public static void isTrue(boolean condition) {
        if (condition)
            throw new IllegalArgumentException();
    }

    public static void isTrue(boolean condition, @Nonnull String message) {
        if (condition)
            throw new IllegalArgumentException(message);
    }

    public static void isFalse(boolean condition) {
        Validate.isTrue(!condition);
    }

    public static void isFalse(boolean condition, @Nonnull String message) {
        Validate.isTrue(!condition, message);
    }
}