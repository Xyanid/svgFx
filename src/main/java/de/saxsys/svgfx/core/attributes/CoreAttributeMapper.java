package de.saxsys.svgfx.core.attributes;

import de.saxsys.svgfx.core.SVGDataProvider;
import de.saxsys.svgfx.core.content.SVGAttributeType;
import de.saxsys.svgfx.core.content.SVGAttributeTypeGradientUnits;
import de.saxsys.svgfx.core.content.SVGAttributeTypeLength;
import de.saxsys.svgfx.core.content.SVGAttributeTypePoints;
import de.saxsys.svgfx.core.content.SVGAttributeTypeString;
import de.saxsys.svgfx.core.content.SVGAttributeTypeTransform;
import de.saxsys.svgfx.core.elements.SVGCircle;
import de.saxsys.svgfx.core.elements.SVGEllipse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Function;

/**
 * This class determines which svg core attributes are mapped to the desired {@link SVGAttributeType}.
 *
 * @author Xyanid on 09.03.2016.
 */
public class CoreAttributeMapper extends BaseAttributeMapper<SVGDataProvider> {

    // region Constants

    /**
     * The id for an element, needed in case an element is referenced by another element.
     */
    public static final CoreAttributeMapper ID = new CoreAttributeMapper("id", SVGAttributeTypeString::new);
    /**
     * Determines the gradient units of a gradient.
     */
    public static final CoreAttributeMapper GRADIENT_UNITS = new CoreAttributeMapper("gradientUnits", SVGAttributeTypeGradientUnits::new);
    /**
     * Determines the gradient transformation of a gradient.
     */
    public static final CoreAttributeMapper GRADIENT_TRANSFORM = new CoreAttributeMapper("gradientUnits", SVGAttributeTypeTransform::new);
    /**
     * Represents the transformation to be applied to an element.
     */
    public static final CoreAttributeMapper TRANSFORM = new CoreAttributeMapper("transform", SVGAttributeTypeTransform::new);
    /**
     * Represents the style of an element, the style need to follow the css text restrictions to be used.
     */
    public static final CoreAttributeMapper STYLE = new CoreAttributeMapper("style", SVGAttributeTypeString::new);
    /**
     * Represents a class link to an existing style, in this case the element will use this link to style itself.
     */
    public static final CoreAttributeMapper CLASS = new CoreAttributeMapper("class", SVGAttributeTypeString::new);
    /**
     * Represents x component of a center position, this element is used for {@link SVGCircle}s and {@link SVGEllipse}s.
     */
    public static final CoreAttributeMapper CENTER_X = new CoreAttributeMapper("cx", SVGAttributeTypeLength::new);
    /**
     * Represents y component of a center position, this element is used for {@link SVGCircle}s and {@link SVGEllipse}s.
     */
    public static final CoreAttributeMapper CENTER_Y = new CoreAttributeMapper("cy", SVGAttributeTypeLength::new);
    /**
     * Represents a radius.
     */
    public static final CoreAttributeMapper RADIUS = new CoreAttributeMapper("r", SVGAttributeTypeLength::new);
    /**
     * Represents a radius which is used in the x direction.
     */
    public static final CoreAttributeMapper RADIUS_X = new CoreAttributeMapper("rx", SVGAttributeTypeLength::new);
    /**
     * Represents a radius which is used in the y direction.
     */
    public static final CoreAttributeMapper RADIUS_Y = new CoreAttributeMapper("ry", SVGAttributeTypeLength::new);
    /**
     * Represents the focus in x direction, this attribute is used by a radial gradient.
     */
    public static final CoreAttributeMapper FOCUS_X = new CoreAttributeMapper("fx", SVGAttributeTypeLength::new);
    /**
     * Represents the focus in y direction, this attribute is used by a radial gradient.
     */
    public static final CoreAttributeMapper FOCUS_Y = new CoreAttributeMapper("fy", SVGAttributeTypeLength::new);
    /**
     * Represents a comma separated list of points.
     */
    public static final CoreAttributeMapper POINTS = new CoreAttributeMapper("points", SVGAttributeTypePoints::new);
    /**
     * Represents the start x component of a line.
     */
    public static final CoreAttributeMapper START_X = new CoreAttributeMapper("x1", SVGAttributeTypeLength::new);
    /**
     * Represents the start y component of a line.
     */
    public static final CoreAttributeMapper START_Y = new CoreAttributeMapper("y1", SVGAttributeTypeLength::new);
    /**
     * Represents the end x component of a line.
     */
    public static final CoreAttributeMapper END_X = new CoreAttributeMapper("x2", SVGAttributeTypeLength::new);
    /**
     * Represents the end y component of a line.
     */
    public static final CoreAttributeMapper END_Y = new CoreAttributeMapper("y2", SVGAttributeTypeLength::new);
    /**
     * Represents a series of path descriptions.
     */
    public static final CoreAttributeMapper PATH_DESCRIPTION = new CoreAttributeMapper("d", SVGAttributeTypeString::new);
    /**
     * Represents the x component of a position, how this is used depends on the element it is used in.
     */
    public static final CoreAttributeMapper POSITION_X = new CoreAttributeMapper("x", SVGAttributeTypeLength::new);
    /**
     * Represents the y component of a position, how this is used depends on the element it is used in.
     */
    public static final CoreAttributeMapper POSITION_Y = new CoreAttributeMapper("y", SVGAttributeTypeLength::new);
    /**
     * Represents the width of an element.
     */
    public static final CoreAttributeMapper WIDTH = new CoreAttributeMapper("width", SVGAttributeTypeLength::new);
    /**
     * Represents the height of an element.
     */
    public static final CoreAttributeMapper HEIGHT = new CoreAttributeMapper("height", SVGAttributeTypeLength::new);
    /**
     * Represents the offset from a start position.
     */
    public static final CoreAttributeMapper OFFSET = new CoreAttributeMapper("offset", SVGAttributeTypeLength::new);
    /**
     * Represents the type of the element.
     */
    public static final CoreAttributeMapper TYPE = new CoreAttributeMapper("type", SVGAttributeTypeString::new);

    /**
     * Contains all the values that are available for this attribute class.
     */
    public static final ArrayList<CoreAttributeMapper> VALUES = new ArrayList<>(Arrays.asList(ID,
                                                                                              GRADIENT_UNITS,
                                                                                              GRADIENT_TRANSFORM,
                                                                                              TRANSFORM,
                                                                                              STYLE,
                                                                                              CLASS,
                                                                                              CENTER_X,
                                                                                              CENTER_Y,
                                                                                              RADIUS,
                                                                                              RADIUS_X,
                                                                                              RADIUS_Y,
                                                                                              FOCUS_X,
                                                                                              FOCUS_Y,
                                                                                              POINTS,
                                                                                              START_X,
                                                                                              START_Y,
                                                                                              END_X,
                                                                                              END_Y,
                                                                                              PATH_DESCRIPTION,
                                                                                              POSITION_X,
                                                                                              POSITION_Y,
                                                                                              WIDTH,
                                                                                              HEIGHT,
                                                                                              OFFSET,
                                                                                              TYPE));

    // endregion

    //region Constructor

    /**
     * {@inheritDoc}
     */
    private CoreAttributeMapper(final String name, final Function<SVGDataProvider, ? extends SVGAttributeType> contentTypeCreator) {
        super(name, contentTypeCreator);
    }

    //endregion
}