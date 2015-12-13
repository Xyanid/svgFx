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
import de.saxsys.svgfx.core.elements.LinearGradient;
import de.saxsys.svgfx.core.elements.SVGElementBase;
import de.saxsys.svgfx.xml.elements.ElementBase;
import javafx.scene.paint.Paint;
import javafx.scene.paint.Stop;
import org.xml.sax.Attributes;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Contains basic functionality to handle gradients of svg.
 *
 * @param <TPaint> the type of the paint need
 *                 @author Xyanid on 06.11.2015.
 */
public abstract class SVGGradientBase<TPaint extends Paint> extends SVGElementBase<TPaint> {

    //region Constructor

    /**
     * Creates a new instance of he element using the given attributes, parent and dataProvider.
     *
     * @param name         value of the element
     * @param attributes   attributes of the element
     * @param parent       parent of the element
     * @param dataProvider dataprovider to be used
     *
     * @throws IllegalArgumentException if either value or dataProvider are null
     */
    public SVGGradientBase(final String name, final Attributes attributes, final SVGElementBase<SVGDataProvider> parent, final SVGDataProvider dataProvider) throws IllegalArgumentException {
        super(name, attributes, parent, dataProvider);
    }

    //endregion

    //region Public

    /**
     * Gets the stops related to this gradient.
     *
     * @return the stops which this gradient needs
     */
    public final List<Stop> getStops() {
        List<javafx.scene.paint.Stop> stops = new ArrayList<>();

        // in this case we are referencing a color and need to ask the data provider to give use the color
        if (getAttributes().containsKey(XLinkAttribute.XLINK_HREF.getName())) {

            String reference = getAttributes().get(XLinkAttribute.XLINK_HREF.getName());

            LinearGradient gradient = getDataProvider().getData(LinearGradient.class, reference.replaceFirst("#", ""));

            if (gradient == null) {
                throw new IllegalArgumentException(String.format("given reference %s to a linear gradient does not exist", reference));
            }

            stops = gradient.getChildren().stream().filter(child -> child instanceof de.saxsys.svgfx.core.elements.Stop).map(child -> ((de.saxsys.svgfx.core.elements.Stop) child).getResult()).collect(
                    Collectors.toList());
        } else {
            for (ElementBase element : getChildren()) {
                if (element instanceof de.saxsys.svgfx.core.elements.Stop) {
                    stops.add(((de.saxsys.svgfx.core.elements.Stop) element).getResult());
                }
            }
        }

        return stops;
    }

    //endregion

    // region Override SVGElementBase

    @Override protected final void initializeResult(TPaint paint) throws SVGException {

        // TODO figure out how to apply transformation to a paint if that is possible
    }

    // endregion
}