/*
 *
 * ******************************************************************************
 *  * Copyright 2015 - 2015 Xyanid
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *   http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *  *****************************************************************************
 */

package de.saxsys.svgfx.core.elements;

import de.saxsys.svgfx.core.SVGDataProvider;
import de.saxsys.svgfx.core.SVGException;
import de.saxsys.svgfx.core.SVGParser;
import de.saxsys.svgfx.core.css.SVGCssContentTypeFillRule;
import de.saxsys.svgfx.core.css.SVGCssContentTypeLength;
import de.saxsys.svgfx.core.css.SVGCssContentTypePaint;
import de.saxsys.svgfx.core.css.SVGCssStyle;
import javafx.scene.paint.Color;
import javafx.scene.shape.FillRule;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.xml.sax.Attributes;

import java.net.URL;

/**
 * This test will ensure that svg circle elements are fully supported.
 *
 * @author Xyanid on 05.10.2015.
 */
public final class SVGCircleTest {

    /**
     * Ensures that the attributes required for a circle are parse correctly.
     */
    @Test public void ensureAttributesAreParsedCorrectly() {

        Attributes attributes = Mockito.mock(Attributes.class);

        Mockito.when(attributes.getLength()).thenReturn(3);

        Mockito.when(attributes.getQName(0)).thenReturn(SVGElementBase.CoreAttribute.CENTER_X.getName());
        Mockito.when(attributes.getValue(0)).thenReturn("50.0");
        Mockito.when(attributes.getQName(1)).thenReturn(SVGElementBase.CoreAttribute.CENTER_Y.getName());
        Mockito.when(attributes.getValue(1)).thenReturn("100.0");
        Mockito.when(attributes.getQName(2)).thenReturn(SVGElementBase.CoreAttribute.RADIUS.getName());
        Mockito.when(attributes.getValue(2)).thenReturn("25");

        SVGCircle circle = new SVGCircle("circle", attributes, null, new SVGDataProvider());

        Assert.assertEquals(50.0d, circle.getResult().getCenterX(), 0.01d);
        Assert.assertEquals(100.0d, circle.getResult().getCenterY(), 0.01d);
        Assert.assertEquals(25.0d, circle.getResult().getRadius(), 0.01d);
    }

    /**
     * Ensures that a {@link de.saxsys.svgfx.core.SVGException} is thrown of one of the attributes is invalid.
     */
    @Test public void ensureSVGExceptionIsThrownWhenAttributesAreInvalid() {

        Attributes attributes = Mockito.mock(Attributes.class);

        Mockito.when(attributes.getLength()).thenReturn(3);

        Mockito.when(attributes.getQName(0)).thenReturn(SVGElementBase.CoreAttribute.CENTER_X.getName());
        Mockito.when(attributes.getQName(1)).thenReturn(SVGElementBase.CoreAttribute.CENTER_Y.getName());
        Mockito.when(attributes.getQName(2)).thenReturn(SVGElementBase.CoreAttribute.RADIUS.getName());

        Mockito.when(attributes.getValue(0)).thenReturn("A");
        Mockito.when(attributes.getValue(1)).thenReturn("100.0");
        Mockito.when(attributes.getValue(2)).thenReturn("25");

        SVGCircle circle = new SVGCircle("circle", attributes, null, new SVGDataProvider());

        try {
            circle.getResult();
            Assert.fail();
        } catch (SVGException e) {
            Assert.assertTrue(e.getMessage().contains(SVGCircle.class.getName()));
            Assert.assertEquals(NumberFormatException.class, e.getCause().getClass());
        }

        Mockito.when(attributes.getValue(0)).thenReturn("10.0");
        Mockito.when(attributes.getValue(1)).thenReturn("B");
        Mockito.when(attributes.getValue(2)).thenReturn("25");

        circle = new SVGCircle("circle", attributes, null, new SVGDataProvider());

        try {
            circle.getResult();
            Assert.fail();
        } catch (SVGException e) {
            Assert.assertTrue(e.getMessage().contains(SVGCircle.class.getName()));
            Assert.assertEquals(NumberFormatException.class, e.getCause().getClass());
        }

        Mockito.when(attributes.getValue(0)).thenReturn("10.0");
        Mockito.when(attributes.getValue(1)).thenReturn("10.0");
        Mockito.when(attributes.getValue(2)).thenReturn("A");

        circle = new SVGCircle("circle", attributes, null, new SVGDataProvider());

        try {
            circle.getResult();
            Assert.fail();
        } catch (SVGException e) {
            Assert.assertTrue(e.getMessage().contains(SVGCircle.class.getName()));
            Assert.assertEquals(NumberFormatException.class, e.getCause().getClass());
        }
    }

    /**
     * Ensures that a {@link de.saxsys.svgfx.core.SVGException} is thrown of one of the attributes is missing.
     */
    @Test public void ensureSVGExceptionIsThrownWhenAttributesAreMissing() {

        Attributes attributes = Mockito.mock(Attributes.class);

        Mockito.when(attributes.getLength()).thenReturn(1);
        Mockito.when(attributes.getValue(0)).thenReturn("50.0");

        Mockito.when(attributes.getQName(0)).thenReturn(SVGElementBase.CoreAttribute.CENTER_X.getName());

        SVGCircle circle = new SVGCircle("circle", attributes, null, new SVGDataProvider());

        try {
            circle.getResult();
            Assert.fail();
        } catch (SVGException e) {
            Assert.assertTrue(e.getMessage().contains(SVGCircle.class.getName()));
            Assert.assertEquals(NullPointerException.class, e.getCause().getClass());
        }

        Mockito.when(attributes.getQName(0)).thenReturn(SVGElementBase.CoreAttribute.CENTER_Y.getName());

        circle = new SVGCircle("circle", attributes, null, new SVGDataProvider());

        try {
            circle.getResult();
            Assert.fail();
        } catch (SVGException e) {
            Assert.assertTrue(e.getMessage().contains(SVGCircle.class.getName()));
            Assert.assertEquals(NullPointerException.class, e.getCause().getClass());
        }

        Mockito.when(attributes.getQName(0)).thenReturn(SVGElementBase.CoreAttribute.RADIUS.getName());

        circle = new SVGCircle("circle", attributes, null, new SVGDataProvider());

        try {
            circle.getResult();
            Assert.fail();
        } catch (SVGException e) {
            Assert.assertTrue(e.getMessage().contains(SVGCircle.class.getName()));
            Assert.assertEquals(NullPointerException.class, e.getCause().getClass());
        }
    }
}