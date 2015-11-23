package de.saxsys.svgfx.core;

import org.junit.Assert;
import org.junit.Test;

import java.net.URL;

/**
 * Created by rico.hentschel on 05.10.2015.
 */
public final class SVGParserTest {

    /**
     *
     */
    @Test public void parse() {

        SVGParser parser;

        parser = new SVGParser();

        Assert.assertNull(parser.getResult());

        URL url = getClass().getClassLoader().getResource("de/saxsys/svgfx/core/complex.svg");

        Assert.assertNotNull(url);

        try {
            parser.parse(url.getFile());
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }

        Assert.assertNotNull(parser.getResult());
    }
}
