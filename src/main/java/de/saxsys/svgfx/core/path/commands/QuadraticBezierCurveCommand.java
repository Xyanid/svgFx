/*
 * Copyright 2015 - 2017 Xyanid
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

package de.saxsys.svgfx.core.path.commands;

import de.saxsys.svgfx.core.path.PathException;

/**
 * This represents a quadratic bezier curve command in a svg path. This class is immutable, so each instance represents a separate position.
 *
 * @author Xyanid on 01.04.2017.
 */
public final class QuadraticBezierCurveCommand extends BezierCurveCommand {

    // region Constants

    /**
     * The absolute name of a quadratic bezier curve command.
     */
    public static final char ABSOLUTE_NAME = 'Q';

    /**
     * The relative name of a quadratic bezier curve command.
     */
    public static final char RELATIVE_NAME = Character.toLowerCase(ABSOLUTE_NAME);

    // endregion

    // region Field

    /**
     * Creates a new instance and expects a {@link String} that contains desired curve data.
     *
     * @param data the data to be used.
     *
     * @throws PathException if the string does not contain two numeric values separated by a whitespaces.
     */
    QuadraticBezierCurveCommand(final String data) throws PathException {
        super(data);
    }

    // endregion

    // region Implement PathCommand

    @Override
    public char getAbsoluteName() {
        return ABSOLUTE_NAME;
    }

    @Override
    public char getRelativeName() {
        return RELATIVE_NAME;
    }

    // endregion
}
