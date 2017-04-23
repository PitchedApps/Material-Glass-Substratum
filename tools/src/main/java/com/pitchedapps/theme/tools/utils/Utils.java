package com.pitchedapps.theme.tools.utils;

import java.util.Set;

/**
 * Created by Allan Wang on 2017-04-22.
 */

public class Utils {

    public static void p(String s, Object... o) {
        System.out.println(String.format(s, o));
    }

    public static void printSet(String title, Set<String> set) {
        if (set.isEmpty()) return;
        p(title);
        for (String s : set) {
            p(s);
        }
    }

    public static String title(String s) {
        return String.format("\n\n\n\t<!--# %s -->\n\n", s);
    }
}
