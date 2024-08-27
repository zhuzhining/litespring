package org.litespring.test.v2;

import org.junit.Test;
import org.litespring.beans.propertyeditors.CustomBooleanEditor;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CustomBooleanEditorTest {

    @Test
    public void testConvertValue() {
        CustomBooleanEditor editor = new CustomBooleanEditor(false);
        editor.setAsText("true");
        assertTrue(editor.getValue() instanceof Boolean);
        assertTrue((Boolean) editor.getValue());

        editor.setAsText("false");
        assertTrue(editor.getValue() instanceof Boolean);
        assertFalse((Boolean) editor.getValue());

        editor.setAsText("on");
        assertTrue(editor.getValue() instanceof Boolean);
        assertTrue((Boolean) editor.getValue());

        editor.setAsText("off");
        assertTrue(editor.getValue() instanceof Boolean);
        assertFalse((Boolean) editor.getValue());

        editor.setAsText("yes");
        assertTrue(editor.getValue() instanceof Boolean);
        assertTrue((Boolean) editor.getValue());

        editor.setAsText("no");
        assertTrue(editor.getValue() instanceof Boolean);
        assertFalse((Boolean) editor.getValue());

        editor.setAsText("1");
        assertTrue(editor.getValue() instanceof Boolean);
        assertTrue((Boolean) editor.getValue());

        editor.setAsText("0");
        assertTrue(editor.getValue() instanceof Boolean);
        assertFalse((Boolean) editor.getValue());
    }
}
