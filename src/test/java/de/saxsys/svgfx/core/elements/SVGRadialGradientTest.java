/*
 * Copyright 2015 - 2016 Xyanid
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package de.saxsys.svgfx.core.elements;

import de.saxsys.svgfx.core.SVGDocumentDataProvider;
import de.saxsys.svgfx.core.SVGException;
import de.saxsys.svgfx.core.attributes.CoreAttributeMapper;
import de.saxsys.svgfx.core.attributes.PresentationAttributeMapper;
import de.saxsys.svgfx.core.attributes.XLinkAttributeMapper;
import de.saxsys.svgfx.core.attributes.type.SVGAttributeTypeRectangle;
import de.saxsys.svgfx.core.definitions.Enumerations;
import javafx.scene.paint.RadialGradient;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static de.saxsys.svgfx.core.utils.TestUtils.assertResultFails;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * This test will ensure that svg radial gradient elements is fully supported.
 *
 * @author Xyanid on 05.10.2015.
 */
@SuppressWarnings ("unchecked")
public final class SVGRadialGradientTest {

    /**
     * Ensures that the attributes are parsed correctly.
     */
    @Test
    public void allAttributesAreParsedCorrectly() throws SAXException {

        final Attributes attributes = Mockito.mock(Attributes.class);

        when(attributes.getLength()).thenReturn(2);
        when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.OFFSET.getName());
        when(attributes.getValue(0)).thenReturn("0.1");
        when(attributes.getQName(1)).thenReturn(PresentationAttributeMapper.COLOR.getName());
        when(attributes.getValue(1)).thenReturn("red");

        final SVGDocumentDataProvider dataProvider = new SVGDocumentDataProvider();

        final SVGElementBase elementBase = mock(SVGElementBase.class);

        ((Map<String, SVGElementBase>) Whitebox.getInternalState(dataProvider, "data")).put("test", elementBase);

        final List<SVGElementBase> stops = new ArrayList<>();

        stops.add(new SVGStop(SVGStop.ELEMENT_NAME, attributes, null, dataProvider));

        when(attributes.getValue(0)).thenReturn("0.2");
        when(attributes.getValue(1)).thenReturn("blue");

        stops.add(new SVGStop(SVGStop.ELEMENT_NAME, attributes, null, dataProvider));

        when(elementBase.getUnmodifiableChildren()).thenReturn(stops);

        when(attributes.getLength()).thenReturn(6);
        when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.CENTER_X.getName());
        when(attributes.getValue(0)).thenReturn("0.1");
        when(attributes.getQName(1)).thenReturn(CoreAttributeMapper.CENTER_Y.getName());
        when(attributes.getValue(1)).thenReturn("0.15");
        when(attributes.getQName(2)).thenReturn(CoreAttributeMapper.FOCUS_X.getName());
        when(attributes.getValue(2)).thenReturn("0.9");
        when(attributes.getQName(3)).thenReturn(CoreAttributeMapper.FOCUS_Y.getName());
        when(attributes.getValue(3)).thenReturn("0.95");
        when(attributes.getQName(4)).thenReturn(CoreAttributeMapper.RADIUS.getName());
        when(attributes.getValue(4)).thenReturn("0.5");
        when(attributes.getQName(5)).thenReturn(XLinkAttributeMapper.XLINK_HREF.getName());
        when(attributes.getValue(5)).thenReturn("#test");

        final SVGRadialGradient gradient = new SVGRadialGradient(SVGRadialGradient.ELEMENT_NAME, attributes, null, new SVGDocumentDataProvider());

        Assert.assertEquals(0.1d, gradient.getResult().getCenterX(), 0.01d);
        Assert.assertEquals(0.15d, gradient.getResult().getCenterY(), 0.01d);
        Assert.assertEquals(Math.hypot(0.9d - 0.1d, 0.95d - 0.15d), gradient.getResult().getFocusDistance(), 0.01d);
        Assert.assertEquals(Math.atan2(0.95d - 0.15d, 0.9d - 0.1d), gradient.getResult().getFocusAngle(), 0.01d);
    }

    /**
     * Ensures that the an {@link SAXException} is thrown if there are no stops elements.
     */
    @Test
    public void whenStopsAreMissingAnSVGExceptionIsThrown() {

        final Attributes attributes = Mockito.mock(Attributes.class);

        when(attributes.getLength()).thenReturn(0);

        assertResultFails(SVGRadialGradient::new, SVGRadialGradient.ELEMENT_NAME, attributes, null, new SVGDocumentDataProvider(), exception -> {
            assertThat(exception.getCause(), instanceOf(SVGException.class));
            assertEquals(SVGException.Reason.MISSING_STOPS, ((SVGException) exception.getCause()).getReason());
        });
    }

    /**
     * Ensures that the an {@link SVGException} is thrown if there are no stops elements.
     */
    @Test
    public void whenTheGradientIsInUserSpaceButNoShapeHasBeenProvidedAnSVGExceptionIsThrown() {

        final Attributes attributes = Mockito.mock(Attributes.class);

        when(attributes.getLength()).thenReturn(2);
        when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.OFFSET.getName());
        when(attributes.getValue(0)).thenReturn("0.1");
        when(attributes.getQName(1)).thenReturn(PresentationAttributeMapper.COLOR.getName());
        when(attributes.getValue(1)).thenReturn("red");

        final SVGDocumentDataProvider dataProvider = new SVGDocumentDataProvider();

        final SVGElementBase elementBase = mock(SVGElementBase.class);

        ((Map<String, SVGElementBase>) Whitebox.getInternalState(dataProvider, "data")).put("test", elementBase);

        final List<SVGElementBase> stops = new ArrayList<>();

        stops.add(new SVGStop(SVGStop.ELEMENT_NAME, attributes, null, dataProvider));

        when(attributes.getValue(0)).thenReturn("0.2");
        when(attributes.getValue(1)).thenReturn("blue");

        stops.add(new SVGStop(SVGStop.ELEMENT_NAME, attributes, null, dataProvider));

        when(elementBase.getUnmodifiableChildren()).thenReturn(stops);

        when(attributes.getLength()).thenReturn(7);
        when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.CENTER_X.getName());
        when(attributes.getValue(0)).thenReturn("0.1");
        when(attributes.getQName(1)).thenReturn(CoreAttributeMapper.CENTER_Y.getName());
        when(attributes.getValue(1)).thenReturn("0.15");
        when(attributes.getQName(2)).thenReturn(CoreAttributeMapper.FOCUS_X.getName());
        when(attributes.getValue(2)).thenReturn("0.9");
        when(attributes.getQName(3)).thenReturn(CoreAttributeMapper.FOCUS_Y.getName());
        when(attributes.getValue(3)).thenReturn("0.95");
        when(attributes.getQName(4)).thenReturn(CoreAttributeMapper.RADIUS.getName());
        when(attributes.getValue(4)).thenReturn("0.5");
        when(attributes.getQName(5)).thenReturn(CoreAttributeMapper.GRADIENT_UNITS.getName());
        when(attributes.getValue(5)).thenReturn(Enumerations.GradientUnit.USER_SPACE_ON_USE.getName());
        when(attributes.getQName(6)).thenReturn(XLinkAttributeMapper.XLINK_HREF.getName());
        when(attributes.getValue(6)).thenReturn("#test");

        assertResultFails(SVGRadialGradient::new, SVGRadialGradient.ELEMENT_NAME, attributes, null, new SVGDocumentDataProvider(), exception -> {
            assertThat(exception.getCause(), instanceOf(SVGException.class));
            assertEquals(SVGException.Reason.MISSING_ELEMENT, ((SVGException) exception.getCause()).getReason());
        });
    }

    /**
     * Ensures that the an {@link SVGException} is thrown if there are no stops elements.
     */
    @Test
    public void whenGradientUnitsAreProvidedUserSpaceOnUseTheValuesOfTheGradientAreAdjustedAccordingly() throws SVGException, SAXException {

        final Attributes attributes = Mockito.mock(Attributes.class);

        when(attributes.getLength()).thenReturn(2);
        when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.OFFSET.getName());
        when(attributes.getValue(0)).thenReturn("0.1");
        when(attributes.getQName(1)).thenReturn(PresentationAttributeMapper.COLOR.getName());
        when(attributes.getValue(1)).thenReturn("red");

        final SVGDocumentDataProvider dataProvider = new SVGDocumentDataProvider();

        final SVGElementBase elementBase = mock(SVGElementBase.class);

        ((Map<String, SVGElementBase>) Whitebox.getInternalState(dataProvider, "data")).put("test", elementBase);

        final List<SVGElementBase> stops = new ArrayList<>();

        stops.add(new SVGStop(SVGStop.ELEMENT_NAME, attributes, null, dataProvider));

        when(attributes.getValue(0)).thenReturn("0.2");
        when(attributes.getValue(1)).thenReturn("blue");

        stops.add(new SVGStop(SVGStop.ELEMENT_NAME, attributes, null, dataProvider));

        when(elementBase.getUnmodifiableChildren()).thenReturn(stops);

        when(attributes.getLength()).thenReturn(7);
        when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.CENTER_X.getName());
        when(attributes.getValue(0)).thenReturn("0.1");
        when(attributes.getQName(1)).thenReturn(CoreAttributeMapper.CENTER_Y.getName());
        when(attributes.getValue(1)).thenReturn("0.15");
        when(attributes.getQName(2)).thenReturn(CoreAttributeMapper.FOCUS_X.getName());
        when(attributes.getValue(2)).thenReturn("0.9");
        when(attributes.getQName(3)).thenReturn(CoreAttributeMapper.FOCUS_Y.getName());
        when(attributes.getValue(3)).thenReturn("0.95");
        when(attributes.getQName(4)).thenReturn(CoreAttributeMapper.RADIUS.getName());
        when(attributes.getValue(4)).thenReturn("0.5");
        when(attributes.getQName(5)).thenReturn(CoreAttributeMapper.GRADIENT_UNITS.getName());
        when(attributes.getValue(5)).thenReturn(Enumerations.GradientUnit.USER_SPACE_ON_USE.getName());
        when(attributes.getQName(6)).thenReturn(XLinkAttributeMapper.XLINK_HREF.getName());
        when(attributes.getValue(6)).thenReturn("#test");

        final SVGAttributeTypeRectangle.SVGTypeRectangle boundingBox = new SVGAttributeTypeRectangle.SVGTypeRectangle(new SVGDocumentDataProvider());
        boundingBox.getMinX().setText("50");
        boundingBox.getMaxX().setText("100");
        boundingBox.getMinY().setText("100");
        boundingBox.getMaxY().setText("150");

        final SVGShapeBase<?> shape = mock(SVGShapeBase.class);
        when(shape.createBoundingBox()).thenReturn(boundingBox);

        final RadialGradient gradient = new SVGRadialGradient(SVGRadialGradient.ELEMENT_NAME, attributes, null, new SVGDocumentDataProvider()).createResult(shape);

        throw new UnsupportedOperationException("implement");
    }
}
