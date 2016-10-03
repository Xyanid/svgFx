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
import javafx.scene.paint.Color;
import org.junit.Test;
import org.mockito.Mockito;
import org.xml.sax.Attributes;

import static de.saxsys.svgfx.core.elements.utils.TestUtils.assertCreationFails;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * This test will ensure that svg stop elements is fully supported.
 *
 * @author Xyanid on 05.10.2015.
 */
public final class SVGStopTest {

    /**
     * Ensures that the attributes are parsed correctly.
     */
    @Test
    public void ensureAttributesAreParsedCorrectly() {

        final Attributes attributes = Mockito.mock(Attributes.class);

        when(attributes.getLength()).thenReturn(3);

        when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.OFFSET.getName());
        when(attributes.getValue(0)).thenReturn("0.1");
        when(attributes.getQName(1)).thenReturn(PresentationAttributeMapper.COLOR.getName());
        when(attributes.getValue(1)).thenReturn("red");
        when(attributes.getQName(2)).thenReturn(PresentationAttributeMapper.STOP_OPACITY.getName());
        when(attributes.getValue(2)).thenReturn("0.5");

        final SVGStop stop = new SVGStop(SVGStop.ELEMENT_NAME, attributes, null, new SVGDocumentDataProvider());

        assertEquals(0.1d, stop.getResult().getOffset(), 0.01d);
        assertEquals(new Color(1.0d, 0.0d, 0.0d, 0.5d), stop.getResult().getColor());
        assertEquals(0.5d, stop.getResult().getColor().getOpacity(), 0.01d);
    }

    /**
     * Ensures that the {@link PresentationAttributeMapper#STOP_COLOR} is preferred.
     */
    @Test
    public void ensureThatStopColorIsPreferred() {

        final Attributes attributes = Mockito.mock(Attributes.class);

        when(attributes.getLength()).thenReturn(3);

        when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.OFFSET.getName());
        when(attributes.getValue(0)).thenReturn("0.1");
        when(attributes.getQName(1)).thenReturn(PresentationAttributeMapper.COLOR.getName());
        when(attributes.getValue(1)).thenReturn("red");
        when(attributes.getQName(2)).thenReturn(PresentationAttributeMapper.STOP_COLOR.getName());
        when(attributes.getValue(2)).thenReturn("blue");

        final SVGStop stop = new SVGStop(SVGStop.ELEMENT_NAME, attributes, null, new SVGDocumentDataProvider());

        assertEquals(0.1d, stop.getResult().getOffset(), 0.01d);
        assertEquals(Color.BLUE, stop.getResult().getColor());
    }

    /**
     * Ensures that a {@link SVGException} is thrown of one of the attributes is invalid.
     */
    @Test
    public void ensureSVGExceptionIsThrownWhenAttributesAreInvalid() {

        final Attributes attributes = Mockito.mock(Attributes.class);

        when(attributes.getLength()).thenReturn(6);

        when(attributes.getQName(0)).thenReturn(CoreAttributeMapper.OFFSET.getName());
        when(attributes.getQName(1)).thenReturn(PresentationAttributeMapper.STOP_OPACITY.getName());
        when(attributes.getQName(2)).thenReturn(PresentationAttributeMapper.STOP_COLOR.getName());

        when(attributes.getValue(0)).thenReturn("A");
        when(attributes.getValue(1)).thenReturn("1.0");
        when(attributes.getValue(2)).thenReturn("#000000");

        assertCreationFails(SVGStop::new, SVGStop.ELEMENT_NAME, attributes, null, new SVGDocumentDataProvider(), SVGStop.class, NumberFormatException.class);

        when(attributes.getValue(0)).thenReturn("1.0");
        when(attributes.getValue(1)).thenReturn("A");
        when(attributes.getValue(2)).thenReturn("#000000");

        assertCreationFails(SVGStop::new, SVGStop.ELEMENT_NAME, attributes, null, new SVGDocumentDataProvider(), SVGStop.class, NumberFormatException.class);

        when(attributes.getValue(0)).thenReturn("1.0");
        when(attributes.getValue(1)).thenReturn("1.0");
        when(attributes.getValue(2)).thenReturn("asdsagfa");

        assertCreationFails(SVGStop::new, SVGStop.ELEMENT_NAME, attributes, null, new SVGDocumentDataProvider(), SVGStop.class, IllegalArgumentException.class);

        when(attributes.getQName(2)).thenReturn(PresentationAttributeMapper.COLOR.getName());

        assertCreationFails(SVGStop::new, SVGStop.ELEMENT_NAME, attributes, null, new SVGDocumentDataProvider(), SVGStop.class, IllegalArgumentException.class);
    }

    /**
     * Ensures that a {@link SVGException} is thrown if one of the required attributes is missing.
     */
    @Test
    public void ensureSVGExceptionIsThrownWhenAttributesAreMissing() {

        final Attributes attributes = Mockito.mock(Attributes.class);

        when(attributes.getLength()).thenReturn(1);
        when(attributes.getValue(0)).thenReturn("50.0");

        when(attributes.getQName(0)).thenReturn(PresentationAttributeMapper.STOP_COLOR.getName());

        assertCreationFails(SVGStop::new, SVGStop.ELEMENT_NAME, attributes, null, new SVGDocumentDataProvider(), SVGStop.class, IllegalArgumentException.class);
    }
}