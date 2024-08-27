package org.litespring.test.v2;

import org.junit.Assert;
import org.junit.Test;
import org.litespring.beans.propertyeditors.CustomNumberEditor;

import static org.junit.Assert.assertTrue;

public class CustomNumberEditorTest {

    @Test
    public void convertValue() {
        CustomNumberEditor editor = new CustomNumberEditor(Integer.class, true);
        editor.setAsText("3");
        assertTrue(editor.getValue() instanceof Integer);
        assertTrue(((Integer) editor.getValue()).intValue() == 3);

        editor.setAsText("");
        assertTrue(editor.getValue() == null);

        try {
            editor.setAsText("3.1");
        } catch (IllegalArgumentException e) {
            return;
        }
        Assert.fail();
    }
}
