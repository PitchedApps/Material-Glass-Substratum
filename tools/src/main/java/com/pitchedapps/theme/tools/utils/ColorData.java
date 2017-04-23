package com.pitchedapps.theme.tools.utils;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Allan Wang on 2017-04-22.
 */

public class ColorData {

    public String name, hex, ref, verbatim, comment, pseudoComment;
    public boolean newLine = false, skip = false;
    static final String regex = "<color name=\"([^/]+?)\">([^<]+?)</color>(.*)";
    static final Pattern colorPattern = Pattern.compile(regex);
    static final String elementWrapper = "\t<color name=\"%s\">%s</color>";
    static final String refPrefix = "@color/";

    public ColorData(String parse) {
        Matcher m = colorPattern.matcher(parse);
        if (m.find()) {
            if (skip(m.group(1))) {
                skip = true;
                return;
            }
            name = m.group(1);
            if (m.group(2).startsWith("#"))
                hex = m.group(2);
            else ref = m.group(2);
            if (m.groupCount() >= 3 && !m.group(3).trim().isEmpty()) comment = m.group(3).trim();
        } else if (parse.trim().isEmpty())
            newLine = true;
        else if (!parse.startsWith("<resources")
                && !parse.startsWith("</resources")
                && !parse.startsWith("<?xml"))
            verbatim = parse;
        else
            skip = true;
    }

    public ColorData() {

    }

    public ColorData verbatim(String s) {
        this.verbatim = s;
        return this;
    }

    public ColorData(String name, String inner) {
        this.name = name;
        if (inner.startsWith("#"))
            hex = inner;
        else ref = inner;
    }

    @Override
    public String toString() {
        if (verbatim != null) return "\t" + verbatim;
        if (newLine) return "";
        if (skip) return null;
        String color = String.format(elementWrapper, name, colorRef());
        if (comment != null) color += " " + comment;
        if (pseudoComment != null) color += " " + pseudoComment;
        return color;
    }

    String colorRef() {
        if (hex != null) return hex;
        return ref;
    }

    boolean skip(String name) {
        return (ColorUtils.isAppcompatColor(name)
                || name.startsWith("quantum_")
                || name.startsWith("common_"));
    }

    public ColorData hardcode(Map<String, ColorData> data) {
        if (hex != null || ref == null || !ref.startsWith(refPrefix)) return this;
        ColorData color = data.get(ref.substring(refPrefix.length()));
        if (color == null || color.hex == null) return this;
        pseudoComment(ref);
        hex = color.hex;
        return this;
    }

    public ColorData addTo(Map<String, ColorData> map) {
        if (name != null)
            map.put(name, this);
        return this;
    }

    public ColorData addTo(Set<String> set) {
        if (name != null) set.add(name);
        return this;
    }

    public ColorData addTo(Collection<ColorData> list) {
        if (name != null)
            list.add(this);
        return this;
    }

    public ColorData addToRegardless(Collection<ColorData> list) {
        list.add(this);
        return this;
    }

    String pseudoComment(String s) {
        pseudoComment = String.format("<!--# %s -->", s);
        return pseudoComment;
    }

    @Override
    public boolean equals(Object o) {
        return o == this || o instanceof ColorData && this.name.equals(((ColorData) o).name) && this.hex.equals(((ColorData) o).hex);
    }

    @Override
    public int hashCode() {
        if (this.name == null) return super.hashCode();
        if (this.hex != null) return this.name.hashCode() + this.hex.hashCode();
        if (this.ref != null) return this.name.hashCode() + this.ref.hashCode();
        return this.name.hashCode();
    }
}
