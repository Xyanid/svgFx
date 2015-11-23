package de.saxsys.svgfx.core.elements;

import de.saxsys.svgfx.core.SVGDataProvider;
import de.saxsys.svgfx.core.SVGElementBase;
import de.saxsys.svgfx.core.SVGElementMapping;
import de.saxsys.svgfx.core.SVGException;
import de.saxsys.svgfx.core.SVGNodeBase;
import de.saxsys.svgfx.xml.elements.ElementBase;
import javafx.scene.Node;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * This class represents the style element from svg
 * Created by Xyanid on 27.10.2015.
 */
@SVGElementMapping("g") public class Group extends SVGNodeBase<javafx.scene.Group> {

    //region Constructor

    /**
     * Creates a new instance of he element using the given attributes and the parent.
     *
     * @param name         value of the element
     * @param attributes   attributes of the element
     * @param parent       parent of the element
     * @param dataProvider dataprovider to be used
     */
    public Group(final String name, final Attributes attributes, final SVGElementBase<SVGDataProvider> parent, final SVGDataProvider dataProvider) {
        super(name, attributes, parent, dataProvider);
    }

    //endregion

    //region SVGElementBase

    @Override protected final javafx.scene.Group createResultInternal() throws SVGException {
        javafx.scene.Group result = new javafx.scene.Group();

        result.setOpacity(1.0d);

        for (ElementBase child : getChildren()) {
            try {

                Object childResult = child.getResult();

                if (childResult instanceof Node) {
                    result.getChildren().add((Node) childResult);
                }
            } catch (SAXException e) {
                throw new SVGException(e);
            }
        }

        return result;
    }

    //endregion
}
