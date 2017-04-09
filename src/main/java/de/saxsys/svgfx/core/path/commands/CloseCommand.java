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
import javafx.geometry.Point2D;
import javafx.scene.shape.Rectangle;

/**
 * This represents a position command in a svg path which will either be a {@link MoveCommand} or a {@link LineCommand}. This class is immutable, so each instance represents a separate position.
 *
 * @author Xyanid on 01.04.2017.
 */
public class CloseCommand extends PathCommand {

    // region Constants

    /**
     * The absolute name of a close command.
     */
    public static final char ABSOLUTE_NAME = 'Z';

    /**
     * The relative name of a close command.
     */
    public static final char RELATIVE_NAME = Character.toLowerCase(ABSOLUTE_NAME);

    // endregion

    // region Fields

    /**
     * Contains the position which will be done by this command.
     */
    private final Point2D startPoint;

    // endregion

    // region Field

    /**
     * Creates a new instance and expects a {@link Point2D}, which is the first position that has been moved to in a path command.
     *
     * @param startPoint the start point.
     *
     * @throws PathException if the string does not contain two numeric values separated by a whitespaces.
     */
    CloseCommand(final Point2D startPoint) throws PathException {
        this.startPoint = startPoint;
    }

    // endregion

    // region Implement PathCommand

    @Override
    public final Point2D getNextPosition(final Point2D position) {
        return startPoint;
    }

    @Override
    public final Rectangle getBoundingBox(final Point2D position) {
        return new Rectangle(Math.min(position.getX(), startPoint.getX()),
                             Math.min(position.getY(), startPoint.getY()),
                             Math.abs(position.getX() - startPoint.getX()),
                             Math.abs(position.getY() - startPoint.getY()));
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