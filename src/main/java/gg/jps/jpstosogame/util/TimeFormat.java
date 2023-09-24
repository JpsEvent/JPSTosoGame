package gg.jps.jpstosogame.util;

import org.jetbrains.annotations.NotNull;

public final class TimeFormat {

    public static @NotNull String format(int seconds) {
        int secs;
        int mins = 0;

        if (seconds < 60) {
            secs = seconds;
        } else {
            mins = seconds / 60;
            secs = seconds % 60;
        }

        if (mins > 0) {
            return mins + "分 " + secs + "秒";
        } else {
            if (secs < 0) {
                return "終了";
            }

            return secs + "秒";
        }
    }
}