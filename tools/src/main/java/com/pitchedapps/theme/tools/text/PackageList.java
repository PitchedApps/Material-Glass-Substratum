package com.pitchedapps.theme.tools.text;

import com.pitchedapps.theme.tools.Config;
import com.pitchedapps.theme.tools.utils.FileUtils;
import com.pitchedapps.theme.tools.utils.Utils;

import java.io.File;
import java.util.Collection;
import java.util.TreeSet;

/**
 * Created by Allan Wang on 2017-04-23.
 * <p>
 * Generates list of packages in overlay
 */

public class PackageList {

    public static void main(String[] args) {
        printPackageList(Config.substratumSrc, "- [ ] ", null, "main", "full");
    }

    public static void printPackageList(String baseSrc, String prefix, String suffix, String... subfolders) {
        Collection<String> packages = generatePackageList(baseSrc, subfolders);
        Utils.p("Size %d", packages.size());
        for (String s : packages) {
            if (prefix != null) System.out.print(prefix);
            System.out.print(s);
            if (suffix != null) System.out.print(suffix);
            System.out.println();
        }
    }

    public static Collection<String> generatePackageList(String baseSrc, String... subfolders) {
        if (subfolders == null || subfolders.length == 0) subfolders = new String[]{"main"};
        TreeSet<String> set = new TreeSet<>();
        for (String subfolder : subfolders) {
            File overlays = new File(FileUtils.filename(baseSrc, subfolder, "assets/overlays"));
            if (!overlays.exists()) continue;
            for (File file : overlays.listFiles(File::isDirectory))
                set.add(file.getName());
        }
        return set;
    }
}
