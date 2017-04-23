package com.pitchedapps.theme.tools.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Allan Wang on 2017-04-22.
 */
public class ColorUtils {

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
