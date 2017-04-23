package com.pitchedapps.theme.tools;

import com.pitchedapps.theme.tools.utils.ColorData;
import com.pitchedapps.theme.tools.utils.FileUtils;
import com.pitchedapps.theme.tools.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class PortColors {
    static String packageName = "com.android.vending",
            solution = "Google",
            project = "Google Play Store_7.6.08";

    static String writeDir = Config.defaultWrite + "values", //values directory to write new color file
            baseDir = Config.getVTSDir(solution, project), //values source directory
            themeDir = Config.getThemedDir(packageName); //values themed directory
    static final String COLORS_XML = "colors.xml";

    public static void main(String[] args) {

        //initialize variables
        Map<String, ColorData> refs = new HashMap<>(); //holds all name data relations
        Set<String> sourceNames = new LinkedHashSet<>(); //holds all the color names in the source
        Set<ColorData> source = new LinkedHashSet<>(); //holds source colors to compare
        Set<ColorData> themed = new LinkedHashSet<>(); //holds all themed colors (read only)
        Set<ColorData> missing = new LinkedHashSet<>(); //holds colors in source that aren't in themed
        Set<ColorData> removed = new LinkedHashSet<>(); //holds colors in themed that aren't in source
        Set<ColorData> stillThemed = new LinkedHashSet<>(); //holds colors in themed that are in source

        //Add titles
        missing.add(new ColorData().verbatim(Utils.title("missing")));
        removed.add(new ColorData().verbatim(Utils.title("removed")));


        //Get source colors
        FileUtils.readValueFiles(baseDir, COLORS_XML, s -> new ColorData(s).addTo(source).addTo(refs).addTo(sourceNames));

        //Get themed colors
        FileUtils.readValueFiles(themeDir, COLORS_XML, s -> new ColorData(s).addToRegardless(themed).addTo(refs));

        //Hardcode new colors
        source.forEach(c -> c.hardcode(refs));

        //Sift through themed vals
        themed.forEach(c -> {
            if (c.name == null)
                stillThemed.add(c);
            else if (sourceNames.contains(c.name)) {
                stillThemed.add(c);
                sourceNames.remove(c.name);
            } else
                removed.add(c);
        });

        //Sift through remaining source vals
        sourceNames.forEach(s -> missing.add(refs.get(s)));

        //Create file
        FileUtils.writeFile(writeDir, "colors.xml", stillThemed, removed, missing);

    }

}
