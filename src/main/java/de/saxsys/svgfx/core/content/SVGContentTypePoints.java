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
import de.saxsys.svgfx.core.SVGException;
import de.saxsys.svgfx.core.definitions.Constants;
import de.saxsys.svgfx.core.utils.SVGUtils;
import de.saxsys.svgfx.core.utils.StringUtils;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class represents a svg transform content type. This means it will contains matrix transformation.
 *
 * @author Xyanid on 29.10.2015.
 */
public class SVGContentTypePoints extends SVGContentTypeBase<List<SVGContentTypePoint>, Void> {

    // region Static

    /**
     * Determines the default value for this {@link SVGContentTypeBase}.
     */
    public static final List<SVGContentTypePoint> DEFAULT_VALUE = new ArrayList<>();

    // endregion

    //region Constructor

    /**
     * Creates new instance.
     *
     * @param dataProvider the {@link SVGDataProvider} to use when data is needed.
     */
    public SVGContentTypePoints(final SVGDataProvider dataProvider) {
        super(DEFAULT_VALUE, dataProvider);
    }

    //endregion

    //region Override SVGContentTypeBase

    /**
     * @throws de.saxsys.svgfx.core.SVGException when any value inside the array is not a valid {@link SVGContentTypePoint}.
     */
    @Override
    protected Pair<List<SVGContentTypePoint>, Void> getValueAndUnit(final String text) {
        List<SVGContentTypePoint> actualPoints = new ArrayList<>();

        if (StringUtils.isNotNullOrEmpty(text)) {
            List<String> values = SVGUtils.split(text, Collections.singletonList(Constants.POINTS_DELIMITER), (currentData, index) -> {

                // check if the required delimiter is present and that the last character is not a delimiter so the string can be split
                boolean containsDelimiter = currentData.contains(Constants.POSITION_DELIMITER_STRING);
                if (containsDelimiter && currentData.charAt(currentData.length() - 1) != Constants.POSITION_DELIMITER) {
                    return true;
                }
                // in this special case we have two non delimiters characters separated by a split delimiter which is invalid e.G. "1,2 3 4,5"
                else if (index == text.length() - 1 || text.charAt(index + 1) != Constants.POINTS_DELIMITER) {
                    throw new SVGException("Invalid points format");
                }

                return false;
            });

            for (String pointsSplit : values) {
                SVGContentTypePoint actualPoint = new SVGContentTypePoint(getDataProvider());
                actualPoint.consumeText(pointsSplit);
                actualPoints.add(actualPoint);
            }
        }

        return new Pair<>(actualPoints, null);
    }

    //endregion
}