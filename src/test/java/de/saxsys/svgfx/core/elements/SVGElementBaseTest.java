/*
 *
 * ******************************************************************************
 *  * Copyright 2015 - 2016 Xyanid
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
import de.saxsys.svgfx.core.attributes.CoreAttributeMapper;
import de.saxsys.svgfx.core.attributes.PresentationAttributeMapper;
import de.saxsys.svgfx.core.content.SVGAttributeTypeDouble;
import de.saxsys.svgfx.core.content.SVGAttributeTypeFillRule;
import de.saxsys.svgfx.core.content.SVGAttributeTypeLength;
import de.saxsys.svgfx.core.content.SVGAttributeTypePaint;
import de.saxsys.svgfx.core.content.SVGAttributeTypeStrokeLineCap;
import de.saxsys.svgfx.core.css.SVGCssStyle;
import de.saxsys.svgfx.core.definitions.Enumerations;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.FillRule;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.shape.StrokeType;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;
import org.xml.sax.Attributes;

import java.util.Map;

/**
 * Test to ensure the base functionality of {@link SVGElementBase}. Note that this test does not use mocks for the {@link SVGElementBase} since it would be
 * too much effort to train them.
 *
 * @author Xyanid on 28.11.2015.
 */
public class SVGElementBaseTest {

    /**
     * Ensure that presentation attributes create a valid css style.
     */
    @Test
    public void ensureThatPresentationAttributesCreateValidCssStyle() {

        Attributes attributes = Mockito.mock(Attributes.class);

        Mockito.when(attributes.getLength()).thenReturn(PresentationAttributeMapper.VALUES.size());

        int counter = 0;

        for (PresentationAttributeMapper attribute : PresentationAttributeMapper.VALUES) {

            String value = "1.0";

            if (attribute == PresentationAttributeMapper.FILL_RULE) {
                value = Enumerations.FillRuleMapping.EVEN_ODD.getName();
            } else if (attribute == PresentationAttributeMapper.FILL ||
                       attribute == PresentationAttributeMapper.STROKE ||
                       attribute == PresentationAttributeMapper.STOP_COLOR ||
                       attribute == PresentationAttributeMapper.COLOR) {
                value = "#808080";
            } else if (attribute == PresentationAttributeMapper.STROKE_DASHARRAY) {
                value = "10,5";
            } else if (attribute == PresentationAttributeMapper.STROKE_LINECAP) {
                value = StrokeLineCap.BUTT.name().toLowerCase();
            } else if (attribute == PresentationAttributeMapper.STROKE_LINEJOIN) {
                value = StrokeLineJoin.BEVEL.name().toLowerCase();
            } else if (attribute == PresentationAttributeMapper.STROKE_TYPE) {
                value = StrokeType.OUTSIDE.name().toLowerCase();
            }

            Mockito.when(attributes.getQName(counter)).thenReturn(attribute.getName());
            Mockito.when(attributes.getValue(counter++)).thenReturn(value);
        }

        SVGCssStyle style = new SVGCircle("circle", attributes, null, new SVGDataProvider()).getPresentationCssStyle();

        Assert.assertNotNull(style);

        for (PresentationAttributeMapper attribute : PresentationAttributeMapper.VALUES) {

            Object value = 1.0d;

            if (attribute == PresentationAttributeMapper.CLIP_PATH || attribute == PresentationAttributeMapper.CLIP_RULE) {
                value = "1.0";
            } else if (attribute == PresentationAttributeMapper.FILL_RULE) {
                value = Enumerations.FillRuleMapping.EVEN_ODD.getRule();
            } else if (attribute == PresentationAttributeMapper.FILL ||
                       attribute == PresentationAttributeMapper.STROKE ||
                       attribute == PresentationAttributeMapper.STOP_COLOR ||
                       attribute == PresentationAttributeMapper.COLOR) {
                value = Color.web("#808080");
            } else if (attribute == PresentationAttributeMapper.STROKE_DASHARRAY) {
                SVGAttributeTypeLength[] result = (SVGAttributeTypeLength[]) style.getAttributeTypeHolder().getAttribute(attribute.getName()).getValue();
                Assert.assertEquals(2, result.length);
                Assert.assertEquals(10.0d, result[0].getValue(), 0.01d);
                Assert.assertEquals(5.0d, result[1].getValue(), 0.01d);
                continue;
            } else if (attribute == PresentationAttributeMapper.STROKE_LINECAP) {
                value = StrokeLineCap.BUTT;
            } else if (attribute == PresentationAttributeMapper.STROKE_LINEJOIN) {
                value = StrokeLineJoin.BEVEL;
            } else if (attribute == PresentationAttributeMapper.STROKE_TYPE) {
                value = StrokeType.OUTSIDE;
            }

            Assert.assertEquals(value, style.getAttributeTypeHolder().getAttribute(attribute.getName()).getValue());
        }
    }

    /**
     * Ensures that {@link PresentationAttributeMapper}s will be preferred and own {@link SVGCssStyle} attributes are kept.
     */
    @Test
    public void ensureThatPresentationStyleIsPreferredAndOwnCssStyleAttributesAreKept() {

        Attributes attributes = Mockito.mock(Attributes.class);

        Mockito.when(attributes.getLength()).thenReturn(4);

        Mockito.when(attributes.getQName(0)).thenReturn(PresentationAttributeMapper.FILL.getName());
        Mockito.when(attributes.getValue(0)).thenReturn("none");
        Mockito.when(attributes.getQName(1)).thenReturn(PresentationAttributeMapper.STROKE.getName());
        Mockito.when(attributes.getValue(1)).thenReturn("#808080");
        Mockito.when(attributes.getQName(2)).thenReturn(PresentationAttributeMapper.FILL_RULE.getName());
        Mockito.when(attributes.getValue(2)).thenReturn("evenodd");
        Mockito.when(attributes.getQName(3)).thenReturn(CoreAttributeMapper.STYLE.getName());
        Mockito.when(attributes.getValue(3)).thenReturn("fill:#101010;stroke:#080808;stroke-width:50;");

        SVGCircle circle = new SVGCircle("circle", attributes, null, new SVGDataProvider());

        SVGCssStyle style = circle.getCssStyleAndResolveInheritance();

        Assert.assertNotNull(style);

        Assert.assertTrue(style.getAttributeTypeHolder().getAttribute(PresentationAttributeMapper.FILL.getName(), SVGAttributeTypePaint.class).getIsNone());
        Assert.assertEquals(style.getAttributeTypeHolder().getAttribute(PresentationAttributeMapper.STROKE.getName(), SVGAttributeTypePaint.class).getValue(),
                            Color.web("#808080"));
        Assert.assertEquals(style.getAttributeTypeHolder()
                                 .getAttribute(PresentationAttributeMapper.FILL_RULE.getName(), SVGAttributeTypeFillRule.class)
                                 .getValue(), FillRule.EVEN_ODD);
        Assert.assertEquals(style.getAttributeTypeHolder()
                                 .getAttribute(PresentationAttributeMapper.STROKE_WIDTH.getName(), SVGAttributeTypeLength.class)
                                 .getValue(), 50.0d, 0.01d);
    }

    /**
     * Ensures that {@link PresentationAttributeMapper}s will be preferred and own {@link SVGCssStyle} attributes are kept.
     */
    @Test
    public void ensureThatOwnStyleIsPreferredAndReferencedCssStyleAttributesAreKept() {

        Attributes attributes = Mockito.mock(Attributes.class);

        Mockito.when(attributes.getLength()).thenReturn(2);

        Mockito.when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.STYLE.getName());
        Mockito.when(attributes.getValue(0)).thenReturn("fill:#101010;stroke:#080808;stroke-width:50;");
        Mockito.when(attributes.getQName(1)).thenReturn(CoreAttributeMapper.CLASS.getName());
        Mockito.when(attributes.getValue(1)).thenReturn("st1");

        SVGCssStyle referencedStyle = new SVGCssStyle(new SVGDataProvider());

        referencedStyle.parseCssText("st1{fill:none;stroke:#001122;fill-rule:odd;}");

        SVGDataProvider dataProvider = new SVGDataProvider();

        dataProvider.getStyles().add(referencedStyle);

        SVGCircle circle = new SVGCircle("circle", attributes, null, dataProvider);

        SVGCssStyle style = circle.getCssStyleAndResolveInheritance();

        Assert.assertNotNull(style);

        Assert.assertEquals(style.getAttributeTypeHolder().getAttribute(PresentationAttributeMapper.FILL.getName(), SVGAttributeTypePaint.class).getValue(),
                            Color.web("#101010"));
        Assert.assertEquals(style.getAttributeTypeHolder().getAttribute(PresentationAttributeMapper.STROKE.getName(), SVGAttributeTypePaint.class).getValue(),
                            Color.web("#080808"));
        Assert.assertEquals(style.getAttributeTypeHolder()
                                 .getAttribute(PresentationAttributeMapper.FILL_RULE.getName(), SVGAttributeTypeFillRule.class)
                                 .getValue(), FillRule.EVEN_ODD);
        Assert.assertEquals(style.getAttributeTypeHolder()
                                 .getAttribute(PresentationAttributeMapper.STROKE_WIDTH.getName(), SVGAttributeTypeLength.class)
                                 .getValue(), 50.0d, 0.01d);
    }

    /**
     * Ensures that attributes of {@link SVGCssStyle} can be inherited and that values which are not present on the {@link SVGCssStyle} of the parent will
     * not return data but will not cause any
     * exceptions.
     */
    @Test
    public void ensureThatCssStyleAttributesAreInheritedFromParent() {

        Attributes attributes = Mockito.mock(Attributes.class);

        Mockito.when(attributes.getLength()).thenReturn(1);

        Mockito.when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.STYLE.getName());
        Mockito.when(attributes.getValue(0)).thenReturn("fill:#000000;stroke:#111111;fill-rule:evenodd;");

        SVGDataProvider dataProvider = new SVGDataProvider();

        SVGCssStyle referencedStyle = new SVGCssStyle(dataProvider);

        referencedStyle.parseCssText("st1{fill-rule:inherit;}");

        dataProvider.getStyles().add(referencedStyle);

        SVGGroup group = new SVGGroup("group", attributes, null, dataProvider);

        Mockito.when(attributes.getLength()).thenReturn(3);

        Mockito.when(attributes.getQName(0)).thenReturn(PresentationAttributeMapper.FILL.getName());
        Mockito.when(attributes.getValue(0)).thenReturn("inherit");
        Mockito.when(attributes.getQName(1)).thenReturn(CoreAttributeMapper.STYLE.getName());
        Mockito.when(attributes.getValue(1)).thenReturn("stroke:inherit;");
        Mockito.when(attributes.getQName(2)).thenReturn(CoreAttributeMapper.CLASS.getName());
        Mockito.when(attributes.getValue(2)).thenReturn("st1");

        SVGCircle circle = new SVGCircle("circle", attributes, group, dataProvider);

        SVGCssStyle style = circle.getCssStyleAndResolveInheritance();

        Assert.assertNotNull(style);

        Assert.assertEquals(style.getAttributeTypeHolder().getAttribute(PresentationAttributeMapper.FILL.getName(), SVGAttributeTypePaint.class).getValue(),
                            Color.web("#000000"));
        Assert.assertEquals(style.getAttributeTypeHolder().getAttribute(PresentationAttributeMapper.STROKE.getName(), SVGAttributeTypePaint.class).getValue(),
                            Color.web("#111111"));
        Assert.assertEquals(style.getAttributeTypeHolder()
                                 .getAttribute(PresentationAttributeMapper.FILL_RULE.getName(), SVGAttributeTypeFillRule.class)
                                 .getValue(), FillRule.EVEN_ODD);
    }

    /**
     * Ensures that attributes of {@link SVGCssStyle} can be inherited from the parent tree and that missing attributes have the default value.
     */
    @Test
    public void ensureThatCssStyleAttributesAreInheritedFromParentTreeAndDefaultValuesAreApplied() {

        Attributes attributes = Mockito.mock(Attributes.class);

        Mockito.when(attributes.getLength()).thenReturn(1);

        Mockito.when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.STYLE.getName());
        Mockito.when(attributes.getValue(0)).thenReturn("fill:#222222");

        SVGGroup group = new SVGGroup("group", attributes, null, new SVGDataProvider());

        Mockito.when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.STYLE.getName());
        Mockito.when(attributes.getValue(0)).thenReturn("stroke:#111111");

        SVGGroup group1 = new SVGGroup("group", attributes, group, new SVGDataProvider());

        Mockito.when(attributes.getLength()).thenReturn(3);

        Mockito.when(attributes.getQName(0)).thenReturn(PresentationAttributeMapper.FILL.getName());
        Mockito.when(attributes.getValue(0)).thenReturn("inherit");
        Mockito.when(attributes.getQName(1)).thenReturn(PresentationAttributeMapper.STROKE.getName());
        Mockito.when(attributes.getValue(1)).thenReturn("inherit");
        Mockito.when(attributes.getQName(2)).thenReturn(PresentationAttributeMapper.STROKE_LINECAP.getName());
        Mockito.when(attributes.getValue(2)).thenReturn("inherit");

        SVGCircle circle = new SVGCircle("circle", attributes, group1, new SVGDataProvider());

        SVGCssStyle style = circle.getCssStyleAndResolveInheritance();

        Assert.assertNotNull(style);

        Assert.assertEquals(Color.web("#222222"),
                            style.getAttributeTypeHolder().getAttribute(PresentationAttributeMapper.FILL.getName(), SVGAttributeTypePaint.class).getValue());
        Assert.assertEquals(Color.web("#111111"),
                            style.getAttributeTypeHolder().getAttribute(PresentationAttributeMapper.STROKE.getName(), SVGAttributeTypePaint.class).getValue());
        Assert.assertEquals(SVGAttributeTypeStrokeLineCap.DEFAULT_VALUE,
                            style.getAttributeTypeHolder()
                                 .getAttribute(PresentationAttributeMapper.STROKE_LINECAP.getName(), SVGAttributeTypeStrokeLineCap.class)
                                 .getValue());
    }

    /**
     * Ensure the {@link SVGElementBase#getCssStyle()} will always return a new {@link SVGCssStyle} and never null.
     */
    @Test
    public void ensureGetCssStyleWillAlwaysReturnANewStyleAndNeverNull() {

        Attributes attributes = Mockito.mock(Attributes.class);

        Mockito.when(attributes.getLength()).thenReturn(0);

        SVGCircle circle = new SVGCircle("circle", attributes, null, new SVGDataProvider());

        SVGCssStyle cssStyle = circle.getCssStyle();

        Assert.assertNotNull(cssStyle);

        SVGCssStyle cssStyle1 = circle.getCssStyle();

        Assert.assertNotNull(cssStyle1);

        Assert.assertTrue(cssStyle != cssStyle1);

        Mockito.when(attributes.getLength()).thenReturn(3);

        Mockito.when(attributes.getQName(0)).thenReturn(PresentationAttributeMapper.FILL.getName());
        Mockito.when(attributes.getValue(0)).thenReturn("#000000");
        Mockito.when(attributes.getQName(1)).thenReturn(PresentationAttributeMapper.STROKE.getName());
        Mockito.when(attributes.getValue(1)).thenReturn("#111111");
        Mockito.when(attributes.getQName(2)).thenReturn(PresentationAttributeMapper.OPACITY.getName());
        Mockito.when(attributes.getValue(2)).thenReturn("0.5");

        circle = new SVGCircle("circle", attributes, null, new SVGDataProvider());

        SVGCssStyle cssStyle2 = circle.getCssStyle();

        Assert.assertNotNull(cssStyle2);
    }

    /**
     * Ensure the {@link SVGElementBase#getCssStyleAndResolveInheritance()} will return the same result as {@link SVGElementBase#getCssStyle()}, if the
     * element does not have a parent.
     */
    @Test
    public void ensureGetCssStyleAndResolveInheritanceWillWorkIfThereIsNoParent() {

        Attributes attributes = Mockito.mock(Attributes.class);

        Mockito.when(attributes.getLength()).thenReturn(3);

        Mockito.when(attributes.getQName(0)).thenReturn(PresentationAttributeMapper.FILL.getName());
        Mockito.when(attributes.getValue(0)).thenReturn("#000000");
        Mockito.when(attributes.getQName(1)).thenReturn(PresentationAttributeMapper.STROKE.getName());
        Mockito.when(attributes.getValue(1)).thenReturn("#111111");
        Mockito.when(attributes.getQName(2)).thenReturn(PresentationAttributeMapper.OPACITY.getName());
        Mockito.when(attributes.getValue(2)).thenReturn("0.5");

        SVGCircle circle = new SVGCircle("circle", attributes, null, new SVGDataProvider());

        SVGCssStyle style = circle.getCssStyleAndResolveInheritance();

        Assert.assertNotNull(style);

        Assert.assertEquals(Color.web("#000000"),
                            style.getAttributeTypeHolder().getAttribute(PresentationAttributeMapper.FILL.getName(), SVGAttributeTypePaint.class).getValue());
        Assert.assertEquals(Color.web("#111111"),
                            style.getAttributeTypeHolder().getAttribute(PresentationAttributeMapper.STROKE.getName(), SVGAttributeTypePaint.class).getValue());
        Assert.assertEquals(0.5d,
                            style.getAttributeTypeHolder().getAttribute(PresentationAttributeMapper.OPACITY.getName(), SVGAttributeTypeDouble.class).getValue(),
                            0.01d);

        SVGCssStyle style1 = circle.getCssStyleAndResolveInheritance();

        Assert.assertNotNull(style1);

        Assert.assertTrue(style != style1);

        Assert.assertEquals(style.getAttributeTypeHolder().getAttribute(PresentationAttributeMapper.FILL.getName(), SVGAttributeTypePaint.class).getValue(),
                            style1.getAttributeTypeHolder().getAttribute(PresentationAttributeMapper.FILL.getName(), SVGAttributeTypePaint.class).getValue());
        Assert.assertEquals(style.getAttributeTypeHolder().getAttribute(PresentationAttributeMapper.STROKE.getName(), SVGAttributeTypePaint.class).getValue(),
                            style1.getAttributeTypeHolder().getAttribute(PresentationAttributeMapper.STROKE.getName(), SVGAttributeTypePaint.class).getValue());
        Assert.assertEquals(style.getAttributeTypeHolder().getAttribute(PresentationAttributeMapper.OPACITY.getName(), SVGAttributeTypeDouble.class).getValue(),
                            style1.getAttributeTypeHolder().getAttribute(PresentationAttributeMapper.OPACITY.getName(), SVGAttributeTypeDouble.class).getValue(),
                            0.01d);


    }

    /**
     * Ensures that {@link CoreAttributeMapper#TRANSFORM} attribute is correctly read.
     */
    @Test
    public void ensureThatTransformAttributeIsRead() {

        Attributes attributes = Mockito.mock(Attributes.class);

        Mockito.when(attributes.getLength()).thenReturn(1);

        Mockito.when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.TRANSFORM.getName());
        Mockito.when(attributes.getValue(0)).thenReturn("translate(30)");

        Transform transform = new SVGCircle("circle", attributes, null, new SVGDataProvider()).getTransformation();

        Assert.assertNotNull(transform);
        Assert.assertEquals(Translate.class, transform.getClass());

        Assert.assertEquals(30.0d, transform.getTx(), 0.01d);
    }

    /**
     * Ensure that no {@link SVGClipPath} will be created if the referenced {@link SVGClipPath} does not exist in the {@link SVGDataProvider}.
     */
    @Test
    public void ensureSVGExceptionIsThrownIfTheReferencedClipPathIsMissing() {

        Attributes attributes = Mockito.mock(Attributes.class);

        Mockito.when(attributes.getLength()).thenReturn(1);

        Mockito.when(attributes.getQName(0)).thenReturn(PresentationAttributeMapper.CLIP_PATH.getName());
        Mockito.when(attributes.getValue(0)).thenReturn("url(#path)");

        SVGCircle circle = new SVGCircle("circle", attributes, null, new SVGDataProvider());

        try {
            circle.getClipPath();
            Assert.fail();
        } catch (SVGException e) {
            Assert.assertTrue(e.getMessage().contains("path"));
        }
    }

    /**
     * Ensure that no {@link SVGClipPath} will be created if the element does not have {@link PresentationAttributeMapper#CLIP_PATH} attribute.
     */
    @Test
    public void ensureNoClipPathIsReturnedIfTheElementDoesNotHaveAClipPathAttribute() {

        Attributes attributes = Mockito.mock(Attributes.class);

        Mockito.when(attributes.getLength()).thenReturn(0);

        SVGCircle circle = new SVGCircle("circle", attributes, null, new SVGDataProvider());

        Assert.assertNull(circle.getClipPath());
    }

    /**
     * Ensure that an {@link SVGException} is thrown if the referenced {@link SVGClipPath} is not present in the {@link SVGDataProvider}.
     */
    @Test(expected = SVGException.class)
    public void ensureSVGExceptionIsThrownIfTheClipPathReferenceIsMissingInTheDataProvider() {

        Attributes attributes = Mockito.mock(Attributes.class);

        Mockito.when(attributes.getLength()).thenReturn(1);

        Mockito.when(attributes.getQName(0)).thenReturn(PresentationAttributeMapper.CLIP_PATH.getName());
        Mockito.when(attributes.getValue(0)).thenReturn("url(#path)");

        SVGCircle circle = new SVGCircle("circle", attributes, null, new SVGDataProvider());

        circle.getClipPath();
    }

    /**
     * Ensure that a {@link SVGClipPath} will be created if the element meets all the requirements.
     */
    @Test
    public void ensureClipPathIsReturnedIfThereIsClipPathReference() {

        Attributes attributes = Mockito.mock(Attributes.class);

        Mockito.when(attributes.getLength()).thenReturn(0);

        SVGDataProvider dataProvider = new SVGDataProvider();

        ((Map<String, SVGElementBase>) Whitebox.getInternalState(dataProvider, "data")).put("test",
                                                                                            new SVGClipPath("clipPath", attributes, null, dataProvider));

        Mockito.when(attributes.getLength()).thenReturn(1);

        Mockito.when(attributes.getQName(0)).thenReturn(PresentationAttributeMapper.CLIP_PATH.getName());
        Mockito.when(attributes.getValue(0)).thenReturn("url(#test)");

        SVGCircle circle = new SVGCircle("circle", attributes, null, dataProvider);

        Assert.assertNotNull(circle.getClipPath());
    }

    /**
     * Ensure that a {@link SVGClipPath} will always return a new instance.
     */
    @Test
    public void ensureClipPathIsAlwaysADifferentInstance() {

        Attributes attributes = Mockito.mock(Attributes.class);

        Mockito.when(attributes.getLength()).thenReturn(1);

        Mockito.when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.ID.getName());
        Mockito.when(attributes.getValue(0)).thenReturn("test");

        SVGDataProvider dataProvider = new SVGDataProvider();

        ((Map<String, SVGElementBase>) Whitebox.getInternalState(dataProvider, "data")).put("test",
                                                                                            new SVGClipPath("clipPath", attributes, null, dataProvider));

        Mockito.when(attributes.getLength()).thenReturn(1);

        Mockito.when(attributes.getQName(0)).thenReturn(PresentationAttributeMapper.CLIP_PATH.getName());
        Mockito.when(attributes.getValue(0)).thenReturn("url(#test)");

        SVGCircle circle = new SVGCircle("circle", attributes, null, dataProvider);

        Node clipPath = circle.getClipPath();

        Assert.assertNotNull(clipPath);

        Node clipPath1 = circle.getClipPath();

        Assert.assertNotNull(clipPath1);

        Assert.assertTrue(clipPath != clipPath1);
    }

    /**
     * Ensure that a {@link SVGClipPath} will not be able to cause a stack overflow in case the {@link SVGClipPath} reference it self.
     */
    @Test
    public void ensureClipPathWillNotCauseStackOverflow() {

        Attributes attributes = Mockito.mock(Attributes.class);

        Mockito.when(attributes.getLength()).thenReturn(2);

        Mockito.when(attributes.getQName(0)).thenReturn(PresentationAttributeMapper.CLIP_PATH.getName());
        Mockito.when(attributes.getValue(0)).thenReturn("url(#test)");
        Mockito.when(attributes.getQName(1)).thenReturn(CoreAttributeMapper.ID.getName());
        Mockito.when(attributes.getValue(1)).thenReturn("test");

        SVGDataProvider dataProvider = new SVGDataProvider();

        ((Map<String, SVGElementBase>) Whitebox.getInternalState(dataProvider, "data")).put("test",
                                                                                            new SVGClipPath("clipPath", attributes, null, dataProvider));

        Mockito.when(attributes.getLength()).thenReturn(1);

        Mockito.when(attributes.getQName(0)).thenReturn(PresentationAttributeMapper.CLIP_PATH.getName());
        Mockito.when(attributes.getValue(0)).thenReturn("url(#test)");

        SVGCircle circle = new SVGCircle("circle", attributes, null, dataProvider);

        Node clipPath = circle.getClipPath();

        Assert.assertNotNull(clipPath);
    }
}
