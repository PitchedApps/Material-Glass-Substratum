package com.pitchedapps.theme.tools;

import java.io.File;

/**
 * Created by Allan Wang on 2016-08-24.
 */
public class Config {

    public static String defaultWrite = "substratum/src/sample/res/values/";

    public static String
            packageName = "com.android.vending",
            SOLUTION = "Google",
            PROJECT = "Google Play Store_7.6.08",
            VTS = "C:\\Users\\User7681\\PA\\VTS\\" + SOLUTION + "\\" + PROJECT + "\\Data"
                    + "\\res\\values";

    private static String values = "values",
            values_v21 = "values-v21";

    public static String getPZ() {
        String s = String.format(Private.PZ, packageName, values_v21);
        if (new File(s).isDirectory()) return s;
        return String.format(Private.PZ, packageName, values);
    }

    public static String getSubTest() {
        String s = String.format(Private.SUBTEST, packageName, values_v21);
        if (new File(s).isDirectory()) return s;
        return String.format(Private.SUBTEST, packageName, values);
    }

    public static String getPZres() {
        return String.format(Private.PZr, packageName);
    }

    public static String getSubTestRes() {
        return String.format(Private.STr, packageName);
    }

    public static String getCM() {
        String s = String.format(Private.CM, packageName, values_v21);
        if (new File(s).isDirectory()) return s;
        return String.format(Private.CM, packageName, values);
    }
}
