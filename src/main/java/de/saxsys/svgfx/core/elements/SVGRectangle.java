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
import de.saxsys.svgfx.core.css.SVGCssStyle;
import de.saxsys.svgfx.core.utils.StringUtils;
import javafx.scene.shape.Rectangle;
import org.xml.sax.Attributes;

/**
 * This class represents a line element from svg
 *
 * @author Xyanid on 25.10.2015.
 */
@SVGElementMapping("rect")
public class SVGRectangle extends SVGShapeBase<Rectangle> {

    //region Constructor

    /**
     * Creates a new instance of he element using the given attributes and the parent.
     *
     * @param name         value of the element
     * @param attributes   attributes of the element
     * @param parent       parent of the element
     * @param dataProvider dataprovider to be used
     */
    public SVGRectangle(final String name, final Attributes attributes, final SVGElementBase<?> parent, final SVGDataProvider dataProvider) {
        super(name, attributes, parent, dataProvider);
    }

    //endregion

    //region Override SVGElementBase

    @Override
    protected final javafx.scene.shape.Rectangle createResult(final SVGCssStyle style) {

        String positionX = getAttribute(CoreAttribute.POSITION_X.getName());
        String positionY = getAttribute(CoreAttribute.POSITION_Y.getName());

        return new javafx.scene.shape.Rectangle(StringUtils.isNullOrEmpty(positionX) ? 0.0d : Double.parseDouble(positionX),
                                                StringUtils.isNullOrEmpty(positionY) ? 0.0d : Double.parseDouble(positionY),
                                                Double.parseDouble(getAttribute(CoreAttribute.WIDTH.getName())),
                                                Double.parseDouble(getAttribute(CoreAttribute.HEIGHT.getName())));
    }

    /**
     * {@inheritDoc}
     * Applies the corner radius if any.
     */
    @Override
    protected void initializeResult(final Rectangle rect, final SVGCssStyle style) throws SVGException {
        super.initializeResult(rect, style);

        // note that we need to multiply the radius since the arc is a diameter for whatever reason

        if (StringUtils.isNotNullOrEmpty(getAttribute(CoreAttribute.RADIUS_X.getName()))) {
            rect.setArcWidth(Double.parseDouble(getAttribute(CoreAttribute.RADIUS_X.getName())) * 2.0d);
        }

        if (StringUtils.isNotNullOrEmpty(getAttribute(CoreAttribute.RADIUS_Y.getName()))) {
            rect.setArcHeight(Double.parseDouble(getAttribute(CoreAttribute.RADIUS_Y.getName())) * 2.0d);
        }
    }

    //endregion
}
