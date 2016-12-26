package com.pitchedapps.theme.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Allan Wang on 2016-08-16.
 */
public class Utils {

    public static StringBuilder readFile(String dir, String fileName) {
        StringBuilder s = new StringBuilder();
        String line = "";
        if (!dir.endsWith("\\") && !fileName.startsWith("\\")) dir = dir + "\\";
        try (BufferedReader br = new BufferedReader(new FileReader(dir + fileName))) {
            while ((line = br.readLine()) != null) {
                // process the line.
                s.append(line);
            }
        } catch (IOException e) {
            System.out.println("ERROR :" + e + "\n" + line);
        }
        return s;
    }

    public static StringBuilder readFileKeepLines(String dir, String fileName) {
        StringBuilder s = new StringBuilder();
        String line = "";
        if (!dir.endsWith("\\") && !fileName.startsWith("\\")) dir = dir + "\\";
        try (BufferedReader br = new BufferedReader(new FileReader(dir + fileName))) {
            while ((line = br.readLine()) != null) {
                // process the line.
                if (!line.startsWith("<?xml")) {
                    s.append("\n");
                }
                s.append(line);
            }
        } catch (IOException e) {
            System.out.println("ERROR :" + e + "\n" + line);
        }
        return s;
    }

    public static void writeFile(String dir, String fileName, StringBuilder result) {
        try {
            File newFile = new File(dir, fileName);
            OutputStreamWriter f = new OutputStreamWriter(new FileOutputStream(newFile), "UTF-8");
            f.append(result);
            f.close();
        } catch (Exception e) {
            // This should never happen
            e.printStackTrace();
        }
    }

    public static void writeFile(String dir, String fileName, String result) {
        try {
            File newFile = new File(dir, fileName);
            OutputStreamWriter f = new OutputStreamWriter(new FileOutputStream(newFile), "UTF-8");
            f.append(result);
            f.close();
        } catch (Exception e) {
            // This should never happen
            e.printStackTrace();
        }
    }

    public static void printSet(String title, Set<String> set) {
        if (set.isEmpty()) return;
        print(title);
        for (String s : set) {
            print(s);
        }
    }

    public interface MatcherInterface {
        void onMatch(Matcher m);
    }

    public static void getMatches(String pattern, StringBuilder text, MatcherInterface mi) {
        Pattern r = Pattern.compile(pattern, Pattern.DOTALL);
        Matcher m = r.matcher(text);
        if (m.find()) {
            do {
                mi.onMatch(m);
            } while (m.find());
        } else {
            System.out.println("NO MATCH");
        }
    }

    public static void print(String s) {
        System.out.println(s);
    }

    static String[] appcompatColorArray = new String[]{
            "abc_input_method_navigation_guard",
            "abc_search_url_text_normal",
            "abc_search_url_text_pressed",
            "abc_search_url_text_selected",
            "accent_material_dark",
            "accent_material_light",
            "background_floating_material_dark",
            "background_floating_material_light",
            "background_material_dark",
            "background_material_light",
            "bright_foreground_disabled_material_dark",
            "bright_foreground_disabled_material_light",
            "bright_foreground_inverse_material_dark",
            "bright_foreground_inverse_material_light",
            "bright_foreground_material_dark",
            "bright_foreground_material_light",
            "button_material_dark",
            "button_material_light",
            "design_fab_shadow_end_color",
            "design_fab_shadow_mid_color",
            "design_fab_shadow_start_color",
            "design_fab_stroke_end_inner_color",
            "design_fab_stroke_end_outer_color",
            "design_fab_stroke_top_inner_color",
            "design_fab_stroke_top_outer_color",
            "design_snackbar_background_color",
            "design_textinput_error_color_dark",
            "design_textinput_error_color_light",
            "dim_foreground_disabled_material_dark",
            "dim_foreground_disabled_material_light",
            "dim_foreground_material_dark",
            "dim_foreground_material_light",
            "foreground_material_dark",
            "foreground_material_light",
            "highlighted_text_material_dark",
            "highlighted_text_material_light",
            "hint_foreground_material_dark",
            "hint_foreground_material_light",
            "material_blue_grey_800",
            "material_blue_grey_900",
            "material_blue_grey_950",
            "material_deep_teal_200",
            "material_deep_teal_500",
            "material_grey_100",
            "material_grey_300",
            "material_grey_50",
            "material_grey_600",
            "material_grey_800",
            "material_grey_850",
            "material_grey_900",
            "primary_dark_material_dark",
            "primary_dark_material_light",
            "primary_material_dark",
            "primary_material_light",
            "primary_text_default_material_dark",
            "primary_text_default_material_light",
            "primary_text_disabled_material_dark",
            "primary_text_disabled_material_light",
            "ripple_material_dark",
            "ripple_material_light",
            "secondary_text_default_material_dark",
            "secondary_text_default_material_light",
            "secondary_text_disabled_material_dark",
            "secondary_text_disabled_material_light",
            "switch_thumb_disabled_material_dark",
            "switch_thumb_disabled_material_light",
            "switch_thumb_normal_material_dark",
            "switch_thumb_normal_material_light"
    };
    static HashSet<String> appcompatColorSet = new HashSet<>(Arrays.asList(appcompatColorArray));

    public static boolean isAppcompatColor(String s) {
        return appcompatColorSet.contains(s);
    }
}
