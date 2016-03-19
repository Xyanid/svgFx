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

package de.saxsys.svgfx.core.content;

import de.saxsys.svgfx.core.SVGDataProvider;
import de.saxsys.svgfx.core.css.SVGCssStyle;
import javafx.util.Pair;

/**
 * This class represents a svg transform content type. This means it will contains matrix transformation.
 *
 * @author Xyanid on 29.10.2015.
 */
public class SVGContentTypeStyle extends SVGContentTypeBase<SVGCssStyle, Void> {

    // region Static

    /**
     * Determines the default value for this {@link SVGContentTypeBase}.
     */
    public static final SVGCssStyle DEFAULT_VALUE = null;

    // endregion

    //region Constructor

    /**
     * Creates new instance.
     *
     * @param dataProvider the {@link SVGDataProvider} to use when data is needed.
     */
    public SVGContentTypeStyle(final SVGDataProvider dataProvider) {
        super(DEFAULT_VALUE, dataProvider);
    }

    //endregion

    //region Override SVGContentTypeBase

    /**
     * @throws de.saxsys.svgfx.core.SVGException when any value inside the array is not a valid {@link SVGContentTypeStyle}
     */
    @Override
    protected Pair<SVGCssStyle, Void> getValueAndUnit(final String text) {
        SVGCssStyle ownStyle = new SVGCssStyle(getDataProvider());
        ownStyle.parseCssText(text);
        return new Pair<>(ownStyle, null);
    }

    //endregion
}