package de.saxsys.svgfx.core.attributes;

import de.saxsys.svgfx.content.ContentTypeBase;
import de.saxsys.svgfx.core.content.SVGContentTypeBase;
import de.saxsys.svgfx.xml.core.IDataProvider;

import java.util.function.Function;

/**
 * This class represents a base attributes, it is intended to be used
 *
 * @author Xyanid on 09.03.2016.
 */
public abstract class BaseAttributeMapper<TDataProvider extends IDataProvider> {

    // region Fields

    /**
     * The name of the attribute within the svg element.
     */
    private final String name;

    /**
     * This function is used to create a new instance of the underlying {@link ContentTypeBase} of the attribute using the given {@link TDataProvider}.
     */
    private final Function<TDataProvider, ? extends SVGContentTypeBase> contentTypeCreator;

    // endregion

    // region Constructor

    /**
     * Creates a new instance.
     *
     * @param name               the name of the attribute within the svg element
     * @param contentTypeCreator the {@link Function} to use when a {@link ContentTypeBase} is needed.
     */
    public BaseAttributeMapper(final String name, final Function<TDataProvider, ? extends SVGContentTypeBase> contentTypeCreator) {
        this.name = name;
        this.contentTypeCreator = contentTypeCreator;
    }

    // endregion

    // region Getter

    /**
     * Returns the {@link #name}.
     *
     * @return the {@link #name}
     */
    public final String getName() {
        return name;
    }

    /**
     * Returns the {@link #contentTypeCreator}.
     *
     * @return the {@link #contentTypeCreator}.
     */
    public final Function<TDataProvider, ? extends SVGContentTypeBase> getContentTypeCreator() {
        return contentTypeCreator;
    }

    // endregion
}