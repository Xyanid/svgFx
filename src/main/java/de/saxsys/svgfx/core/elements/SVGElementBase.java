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
import de.saxsys.svgfx.core.css.SVGCssContentTypeString;
import de.saxsys.svgfx.core.css.SVGCssStyle;
import de.saxsys.svgfx.core.utils.SVGUtils;
import de.saxsys.svgfx.core.utils.StringUtils;
import de.saxsys.svgfx.css.definitions.Constants;
import de.saxsys.svgfx.xml.elements.ElementBase;
import javafx.scene.Node;
import javafx.scene.transform.Transform;
import org.xml.sax.Attributes;

import java.util.EnumSet;

/**
 * This class represents a basic scg element, which provides some basic functionality to get the style of the class.
 *
 * @param <TResult> The type of the result this element will provide @author Xyanid on 28.10.2015.
 */
public abstract class SVGElementBase<TResult> extends ElementBase<SVGDataProvider, TResult, SVGElementBase<?>> {

    // region Enumerations

    /**
     * Contains the core attributes each svg element may have.
     */
    public enum CoreAttribute {

        /**
         * The id for an element, needed in case an element is referenced by another element.
         */
        ID("id"),
        /**
         * Represents the transformation to be applied to an element.
         */
        TRANSFORM("transform"),
        /**
         * Represents the style of an element, the style need to follow the css text restrictions to be used.
         */
        STYLE("style"),
        /**
         * Represents a class link to an existing style, in this case the element will use this link to style itself.
         */
        CLASS("class"),
        /**
         * Represents x component of a center position, this element is used for {@link SVGCircle}s and {@link SVGEllipse}s.
         */
        CENTER_X("cx"),
        /**
         * Represents y component of a center position, this element is used for {@link SVGCircle}s and {@link SVGEllipse}s.
         */
        CENTER_Y("cy"),
        /**
         * Represents a radius.
         */
        RADIUS("r"),
        /**
         * Represents a radius which is used in the x direction.
         */
        RADIUS_X("rx"),
        /**
         * Represents a radius which is used in the y direction.
         */
        RADIUS_Y("ry"),
        /**
         * Represents the focus in x direction, this attribute is used by a radial gradient.
         */
        FOCUS_X("fx"),
        /**
         * Represents the focus in y direction, this attribute is used by a radial gradient.
         */
        FOCUS_Y("fy"),
        /**
         * Represents a comma separated list of points.
         */
        POINTS("points"),
        /**
         * Represents the start x component of a line.
         */
        START_X("x1"),
        /**
         * Represents the start y component of a line.
         */
        START_Y("y1"),
        /**
         * Represents the end x component of a line.
         */
        END_X("x2"),
        /**
         * Represents the end y component of a line.
         */
        END_Y("y2"),
        /**
         * Represents a series of path descriptions.
         */
        PATH_DESCRIPTION("d"),
        /**
         * Represents the x component of a position, how this is used depends on the element it is used in.
         */
        POSITION_X("x"),
        /**
         * Represents the y component of a position, how this is used depends on the element it is used in.
         */
        POSITION_Y("y"),
        /**
         * Represents the width of an element.
         */
        WIDTH("width"),
        /**
         * Represents the height of an element.
         */
        HEIGHT("height"),
        /**
         * Represents the offset from a start position.
         */
        OFFSET("offset"),
        /**
         * Represents the type of the element.
         */
        TYPE("type");

        // region Fields

        /**
         * The name of the attribute within the svg element.
         */
        private final String name;

        // endregion

        // region Constructor

        /**
         * Creates a new instance.
         *
         * @param name the name of the attribute within the svg element
         */
        CoreAttribute(final String name) {
            this.name = name;
        }

        // endregion

        // region Getter

        /**
         * Returns the {@link CoreAttribute#name}.
         *
         * @return the {@link CoreAttribute#name}
         */
        public final String getName() {
            return name;
        }

        // endregion
    }

    /**
     * Contains the xlink attributes each svg element may have.
     */
    public enum XLinkAttribute {

        /**
         * Meaning this element has a reference to an existing element.
         */
        XLINK_HREF("xlink:href");

        // region Fields

        /**
         * The name of the attribute within the svg element.
         */
        private final String name;

        // endregion

        // region Constructor

        /**
         * Creates a new instance.
         *
         * @param name the name of the attribute within the svg element
         */
        XLinkAttribute(final String name) {
            this.name = name;
        }

        // endregion

        // region Getter

        /**
         * Returns the {@link CoreAttribute#name}.
         *
         * @return the {@link CoreAttribute#name}
         */
        public final String getName() {
            return name;
        }

        // endregion
    }

    /**
     * Determines which keyword in a transform attribute of a matrix map to their corresponding javafx classes.
     */
    public enum Matrix {

        NONE(""),
        MATRIX("matrix"),
        TRANSLATE("translate"),
        SCALE("scale"),
        ROTATE("rotate"),
        SKEW_X("skewX"),
        SKEW_Y("skewY");

        // region Fields

        /**
         * The of the transformation within svg.
         */
        private final String name;


        // endregion

        // region Constructor

        /**
         * Creates a new instance.
         *
         * @param name the name of the attribute within the svg element.
         */
        Matrix(final String name) {
            this.name = name;
        }

        // endregion

        // region Getter

        /**
         * Returns the {@link Matrix#name}.
         *
         * @return the {@link Matrix#name}
         */
        public final String getName() {
            return name;
        }

        // endregion
    }

    // endregion

    // region Fields

    /**
     * The result represented by this element.
     */
    private TResult result;

    // endregion

    // region Constructor

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
    public SVGElementBase(final String name, final Attributes attributes, final SVGElementBase<?> parent, final SVGDataProvider dataProvider) throws IllegalArgumentException {
        super(name, attributes, parent, dataProvider);
    }

    // endregion

    // region Public

    //    /**
    //     * Encapsulate a call to {@link #getCssStyleAndResolveInheritance(SVGElementBase)} using the elements parent as the inheritanceResolver.
    //     *
    //     * @return the {@link SVGCssStyle} of this element or null if no style can be determined.
    //     */
    //    public final SVGCssStyle getCssStyleAndResolveInheritance() {
    //        return getCssStyleAndResolveInheritance(getParent());
    //    }

    //    /**
    //     * Encapsulate a call to {@link #getCssStyle()} and {@link SVGUtils#resolveInheritance(SVGCssStyle, SVGElementBase)}, making sure that the inheritance of the style is resolved
    //     *
    //     * @param inheritanceResolver the {@link SVGElementBase} which is used when inherited style attributes are to be resolved
    //     *
    //     * @return the {@link SVGCssStyle} of this element.
    //     */

    /**
     * Gets the elements own {@link SVGCssStyle} and combines it with the {@link SVGCssStyle} or the parent if there one.
     */
    public final SVGCssStyle getCssStyleAndResolveInheritance() {
        SVGCssStyle style = getCssStyle();

        if (getParent() != null) {
            SVGUtils.combineStylesAndResolveInheritance(style, getParent().getCssStyleAndResolveInheritance());
        }

        return style;
    }

    /**
     * Returns the {@link SVGCssStyle} of this element. Since an element can contain a {@link SVGCssStyle.PresentationAttribute}s, an own {@link SVGCssStyle} or a
     * reference to an existing {@link SVGCssStyle} there need to be a rule how the {@link SVGCssStyle} is build. The rule is as follows:
     * {@link SVGCssStyle.PresentationAttribute}s are preferred if they are present and will overwrite existing attribute of an own
     * {@link SVGCssStyle} or a referenced {@link SVGCssStyle}. The following example shows an element which has two
     * {@link SVGCssStyle.PresentationAttribute}s and an own {@link SVGCssStyle}.
     * <pre>
     *     e.G.
     *     circle fill="none" stroke="#808080" style="fill:#111111; stroke:#001122 fill-rule:odd"
     * </pre>
     * this will result in fill = none, stroke = #808080 and fill-rule = odd. The same behavior is to be expected if the {@link SVGCssStyle} would be a reference e.g.
     * <pre>
     *     e.G.
     *     .st1{fill:#111111; stroke:#001122 fill-rule:odd}
     *     circle fill="none" stroke="#808080" class="st1"
     * </pre>
     * An own {@link SVGCssStyle} is always preferred before a referenced {@link SVGCssStyle} and will overwrite existing attributes just as a
     * {@link SVGCssStyle.PresentationAttribute} would. The following example shows an element which has an own
     * {@link SVGCssStyle} and a reference to a {@link SVGCssStyle}.
     * <pre>
     *     e.G.
     *     .st1{fill:none; stroke:#808080 fill-rule:odd}
     *     circle style="fill:#111111; stroke:#001122" class="st1"
     * </pre>
     * this will result in fill = 111111, stroke = #001122 and fill-rule = odd.
     *
     * @return the {@link SVGCssStyle} of this element or null if no style can be determined.
     */
    public final SVGCssStyle getCssStyle() {

        // first we get a referenced style class if any
        SVGCssStyle style = getPresentationCssStyle();

        // if an own style is present it will be used overwriting other attributes in the process
        SVGCssStyle ownStyle = getOwnStyle();
        if (ownStyle != null) {
            if (style == null) {
                style = ownStyle;
            } else {
                style.combineWithStyle(ownStyle);
            }
        }

        // if a referenced style is present it will be used overwriting other attributes in the process
        SVGCssStyle referencedStyle = getReferencedStyle();
        if (referencedStyle != null) {

            if (style == null) {
                style = referencedStyle;
            } else {
                style.combineWithStyle(referencedStyle);
            }
        }

        if (style == null) {
            style = new SVGCssStyle(getDataProvider());
        }

        return style;
    }

    /**
     * Gets the elements own {@link SVGCssStyle}, which will only be available if the element has the {@link CoreAttribute#STYLE}.
     *
     * @return the {@link SVGCssStyle} of this element or null if there is none.
     */
    public final SVGCssStyle getOwnStyle() {

        if (StringUtils.isNullOrEmpty(getAttribute(CoreAttribute.STYLE.getName()))) {
            return null;
        }

        String attribute = getAttribute(CoreAttribute.STYLE.getName());

        SVGCssStyle ownStyle = new SVGCssStyle(getDataProvider());

        ownStyle.parseCssText(String.format("ownStyle%s%s%s%s",
                                            Constants.DECLARATION_BLOCK_START,
                                            attribute,
                                            attribute.endsWith(Constants.PROPERTY_END_STRING) ? "" : Constants.PROPERTY_END,
                                            Constants.DECLARATION_BLOCK_END));

        return ownStyle;
    }

    /**
     * Gets the elements referenced {@link SVGCssStyle}, which will only be available if the element has the {@link CoreAttribute#CLASS}.
     *
     * @return the {@link SVGCssStyle} referenced by this element or null if there is none.
     *
     * @throws SVGException if the element uses a style reference but the style was not found in the {@link #dataProvider}.
     */
    public final SVGCssStyle getReferencedStyle() throws SVGException {
        if (StringUtils.isNullOrEmpty(getAttribute(CoreAttribute.CLASS.getName()))) {
            return null;
        }

        String reference = getAttribute(CoreAttribute.CLASS.getName());

        try {
            return getDataProvider().getStyles().stream().filter(data -> data.getName().endsWith(reference)).findFirst().get();
        } catch (Exception e) {
            throw new SVGException(String.format("Given style reference %s was not found", reference), e);
        }
    }

    /**
     * This method attempts to create a {@link SVGCssStyle} by looking up all the supported {@link SVGCssStyle.PresentationAttribute}. If any attribute is present a
     * valid
     * cssString is returned.
     *
     * @return a {@link SVGCssStyle} containing the {@link SVGCssStyle.PresentationAttribute}s of this element if any or null if not attributes are submitted.
     * {@link SVGCssStyle.PresentationAttribute} exists.
     */
    public final SVGCssStyle getPresentationCssStyle() {

        SVGCssStyle result = null;

        StringBuilder cssText = new StringBuilder();

        for (SVGCssStyle.PresentationAttribute attribute : EnumSet.allOf(SVGCssStyle.PresentationAttribute.class)) {

            String data = getAttribute(attribute.getName());

            if (StringUtils.isNotNullOrEmpty(data)) {
                if (cssText.length() == 0) {
                    cssText.append("presentationStyle" + Constants.DECLARATION_BLOCK_START);
                }

                cssText.append(String.format("%s%s%s%s", attribute.getName(), Constants.PROPERTY_SEPARATOR, data, Constants.PROPERTY_END));
            }
        }

        if (cssText.length() > 0) {
            cssText.append(Constants.DECLARATION_BLOCK_END);
            result = new SVGCssStyle(getDataProvider());
            result.parseCssText(cssText.toString());
        }

        return result;
    }

    /**
     * @return the transformation to be applied to this element if the {@link CoreAttribute#TRANSFORM} is present.
     * otherwise null.
     *
     * @throws SVGException if there is a transformation which has invalid data for its matrix.
     */
    public final Transform getTransformation() throws SVGException {
        if (StringUtils.isNotNullOrEmpty(getAttribute(CoreAttribute.TRANSFORM.getName()))) {
            return SVGUtils.getTransform(getAttribute(CoreAttribute.TRANSFORM.getName()));
        }

        return null;
    }

    /**
     * Encapsulate a call to {@link #getClipPath(SVGCssStyle)} using the {@link #getCssStyleAndResolveInheritance()} as the style.
     *
     * @return the clip path to use or null if this element does not have a clip path.
     *
     * @throws SVGException when there is a {@link SVGClipPath} referenced but the reference can not be found in the {@link #dataProvider}.
     */
    public final Node getClipPath() throws SVGException {

        return getClipPath(getCssStyleAndResolveInheritance());
    }

    /**
     * Must be overwritten in the actual implementation to create a new result for this element based on the
     * information available.
     *
     * @param style the element to use when data is needed.
     *
     * @return a new instance of the result or null of no result was created
     *
     * @throws SVGException will be thrown when an error during creation occurs
     */
    protected abstract TResult createResult(final SVGCssStyle style) throws SVGException;

    /**
     * This method will be called in the {@link #createAndInitializeResult(SVGCssStyle)} and allows to modify the result such as applying a style or transformations.
     * The given inheritanceResolver should be used to retrieve such data, this simply is needed because some elements can actually be referenced e.g. {@link SVGUse} and their actual parent is not
     * the one from which the inherited style attributes can be retrieved.
     *
     * @param result the result which should be modified.
     * @param style  the {@link SVGCssStyle} to use when data is needed.
     *
     * @throws SVGException will be thrown when an error during modification
     */
    protected abstract void initializeResult(final TResult result, final SVGCssStyle style) throws SVGException;

    /**
     * Returns a node which represents the clip path to be applied to this element.
     *
     * @param style the {@link SVGCssStyle} which is used when inherited style attributes are to be resolved
     *
     * @return the clip path to use or null if this element does not have a clip path.
     *
     * @throws SVGException             when there is a {@link SVGClipPath} referenced but the reference can not be found in the {@link #dataProvider}.
     * @throws IllegalArgumentException if the referenced {@link SVGClipPath} is an empty string.
     */
    public final Node getClipPath(final SVGCssStyle style) throws SVGException {

        SVGCssContentTypeString referenceIRI = style.getCssContentType(SVGCssStyle.PresentationAttribute.CLIP_PATH.getName(), SVGCssContentTypeString.class);
        if (referenceIRI != null && StringUtils.isNotNullOrEmpty(referenceIRI.getValue())) {
            // it might happen that we a clip path has a style that contains a clip path that is the SAME clip path, in this case we would get a stackoverflow here, so we prevent that
            SVGClipPath element = SVGUtils.resolveIRI(referenceIRI.getValue(), getDataProvider(), SVGClipPath.class);
            if (element != null && this != element) {

                return SVGUtils.resolveIRI(referenceIRI.getValue(), getDataProvider(), SVGClipPath.class).getResult();
            }
        }

        return null;
    }

    /**
     * Gets the result if its was not yet created using the given parentStyle to create the styling.
     *
     * @param style the parentStyle which will be used in order to get data for styling.
     *
     * @return the result of this element.
     *
     * @throws SVGException if something fails during the creation of the element
     */
    public final TResult getResult(final SVGCssStyle style) throws SVGException {

        if (result == null) {
            result = createAndInitializeResult(style);
        }

        return result;
    }


    //endregion

    //region Abstract

    /**
     * Creates a result represented by this element.
     *
     * @return the result produced by this element.
     *
     * @throws SVGException thrown when an exception during creation occurs.
     */
    public final TResult createAndInitializeResult() throws SVGException {

        return createAndInitializeResult(getCssStyleAndResolveInheritance());
    }

    /**
     * Creates a result represented by this element and uses the given supplier in order to fetch data needed to initialize the result
     *
     * @param parentStyle the style to use when data is needed.
     *
     * @return the result produced by this element.
     *
     * @throws SVGException thrown when an exception during creation occurs.
     */
    public final TResult createAndInitializeResult(final SVGCssStyle parentStyle) throws SVGException {

        TResult result;

        try {
            result = createResult(parentStyle);
            initializeResult(result, parentStyle);
        } catch (Exception e) {
            throw new SVGException(String.format("Creation of element %s failed.\nOriginal element is %s ", getClass().getName(), toString()), e);
        }

        return result;
    }

    //endregion

    // region Override ElementBase

    /**
     * {@inheritDoc}
     */
    @Override
    public final TResult getResult() throws SVGException {

        if (result == null) {
            result = createAndInitializeResult();
        }

        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void startProcessing() throws SVGException {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void processCharacterData(final char[] ch, final int start, final int length) throws SVGException {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void endProcessing() throws SVGException {
    }

    // endregion
}
