package com.pitchedapps.theme.tools.utils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

/**
 * Created by Allan Wang on 2017-04-22.
 */

public class ColorDataTest {

    @Test
    public void standardHexColor() {
        ColorData data = new ColorData(" \t<color name=\"colorPrimary\">#3F51B5</color> ");
        assertNull("verbatim == null", data.verbatim);
        assertNull("pseudoComment == null", data.pseudoComment);
        assertNull("ref == null", data.ref);
        assertFalse("skip == false", data.skip);
        assertEquals("name == colorPrimary", "colorPrimary", data.name);
        assertEquals("hex == #3F51B5", "#3F51B5", data.hex);

    }
}
