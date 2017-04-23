package com.pitchedapps.theme.tools;

import com.pitchedapps.theme.tools.utils.Private;

/**
 * Created by Allan Wang on 2016-08-24.
 */
public class Config {

    public static String defaultWrite = "substratum/src/factory/res/",
            substratumSrc = "substratum/src";

    public static String getVTSDir(String solution, String project) {
        return String.format(Private.VTS, solution, project);
    }

    public static String getThemedDir(String packageName) {
        return String.format("substratum/src/main/assets/overlays/%s/res/", packageName);
    }
}
