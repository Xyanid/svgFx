package de.saxsys.svgfx.core.elements;

import de.saxsys.svgfx.core.SVGParser;
import javafx.scene.Node;
import javafx.scene.shape.SVGPath;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Assert;
import org.junit.Test;

import java.net.URL;

/**
 * Created by Xyanid on 05.10.2015.
 */
public final class ClipPathTest {

    /**
     *
     */
    @Test public void parse() {

        SVGParser parser;

        parser = new SVGParser();

        Assert.assertNull(parser.getResult());

        URL url = getClass().getClassLoader().getResource("de/saxsys/svgfx/core/elements/clipPath.svg");

        Assert.assertNotNull(url);

        try {

            parser.parse(url.getFile());

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }

        Assert.assertNotNull(parser.getResult());

        Assert.assertEquals(parser.getResult().getChildren().size(), 5);

        for (Node element : parser.getResult().getChildren()) {
            Assert.assertThat(element, new IsInstanceOf(SVGPath.class));
        }
    }
}
