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
import de.saxsys.svgfx.core.definitions.Enumerations;
import javafx.scene.shape.FillRule;
import javafx.util.Pair;

/**
 * Represents a {@link FillRule}, the default value is {@link FillRule#EVEN_ODD}.
 *
 * @author Xyanid on 29.10.2015.
 */
public class SVGContentTypeFillRule extends SVGContentTypeBase<FillRule, Void> {

    // region Static

    /**
     * Determines the default value for this {@link SVGContentTypeBase}.
     */
    public static final FillRule DEFAULT_VALUE = FillRule.EVEN_ODD;

    // endregion

    //region Constructor

    /**
     * Creates new instance with a default value of {@link FillRule#EVEN_ODD}.
     *
     * @param dataProvider the {@link SVGDataProvider} to use when data is needed.
     */
    public SVGContentTypeFillRule(final SVGDataProvider dataProvider) {
        super(DEFAULT_VALUE, dataProvider);
    }

    //endregion

    //region Override ContentTypeBase

    @Override
    protected Pair<FillRule, Void> getValueAndUnit(final String cssText) {

        FillRule rule = DEFAULT_VALUE;

        for (Enumerations.FillRuleMapping mapping : Enumerations.FillRuleMapping.values()) {
            if (mapping.getName().equals(cssText)) {
                rule = mapping.getRule();
                break;
            }
        }

        return new Pair<>(rule, null);
    }

    //endregion
}