package com.pitchedapps.theme.tools;

import java.util.TreeMap;
import java.util.TreeSet;

public class PortColors {
    static String dir = Config.getSubTest();
    static String itemPattern = "(<color name=\"([^\\/]+?)\">[^<]+?<\\/color>)";

    public static void main(String[] args) {

        //initialize variables
        TreeMap<String, String> newMap = new TreeMap<>();
        TreeSet<String> missing = new TreeSet<>();
        TreeSet<String> removed = new TreeSet<>();
        TreeMap<String, String> map = new TreeMap<>();

        ///get currently themed colors
        Utils.getMatches(itemPattern, Utils.readFile(dir, "colors.xml"), m -> {
            if (!Utils.isAppcompatColor(m.group(2))) {
                map.put(m.group(2), m.group(1));
            }
        });
        //get new stock file
        Utils.getMatches(itemPattern, Utils.readFile(Config.VTS, "colors.xml"), m -> {
            if (!Utils.isAppcompatColor(m.group(2))) {
                newMap.put(m.group(2), m.group(1));
            }
        });
        //get removed
        for (String s : map.keySet()) {
            if (!newMap.containsKey(s)) {
                removed.add(s);
            }
        }
//        //remove from map
//        for (String s : removed) {
//            map.remove(s);
//        }
        //get missing
        for (String s : newMap.keySet()) {
            if (!map.containsKey(s)) {
                if (s.startsWith("quantum_")) continue;
                if (s.startsWith("common_")) continue;
                missing.add(s);
            }
        }


        StringBuilder result = new StringBuilder();
        result.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<resources>");
        for (String s : map.keySet()) {
            if (!removed.contains(s))
                result.append("\n\t").append(map.get(s));
        }
        result.append("\n\n\n\t<!--Missing-->\n\n");
        for (String s : missing) {
            result.append("\n\t").append(newMap.get(s));
        }
        result.append("\n\n\n\t<!--Removed-->\n\n");
        for (String s : removed) {
            result.append("\n\t").append(map.get(s));
        }
        result.append("\n</resources>");
        Utils.writeFile(dir, "colors_new.xml", result);
//        Utils.printSet("Missing", missing);
//        Utils.printSet("Removed", removed);

    }

}
