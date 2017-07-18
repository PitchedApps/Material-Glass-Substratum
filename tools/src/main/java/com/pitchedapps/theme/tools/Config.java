package com.pitchedapps.theme.tools;

import com.pitchedapps.theme.tools.utils.Private;

/**
 * Created by Allan Wang on 2016-08-24.
 */
public class Config {

    public static String defaultWrite = "overlays-wip",
            substratumSrc = "overlays/delta";

    public static String getVTSDir(String solution, String project) {
        return String.format(Private.VTS, solution, project);
    }

    public static String getThemedDir(String packageName) {
        return String.format("overlays/delta/%s/res/", packageName);
    }
}
