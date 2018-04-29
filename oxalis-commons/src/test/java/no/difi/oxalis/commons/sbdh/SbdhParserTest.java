/*
 * Copyright 2010-2018 Norwegian Agency for Public Management and eGovernment (Difi)
 *
 * Licensed under the EUPL, Version 1.1 or – as soon they
 * will be approved by the European Commission - subsequent
 * versions of the EUPL (the "Licence");
 *
 * You may not use this work except in compliance with the Licence.
 *
 * You may obtain a copy of the Licence at:
 *
 * https://joinup.ec.europa.eu/community/eupl/og_page/eupl
 *
 * Unless required by applicable law or agreed to in
 * writing, software distributed under the Licence is
 * distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied.
 * See the Licence for the specific language governing
 * permissions and limitations under the Licence.
 */

package no.difi.oxalis.commons.sbdh;

import no.difi.oxalis.test.datagenerator.FileGenerator;
import no.difi.vefa.peppol.common.model.Header;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

/**
 * @author steinar
 *         Date: 24.06.15
 *         Time: 15.58
 */
public class SbdhParserTest {

    public static final Logger log = LoggerFactory.getLogger(SbdhParserTest.class);

    public static final String EHF_INVOICE_NO_SBDH_XML = "/ehf-invoice-no-sbdh.xml";

    private File xmlSampleFile;

    @BeforeMethod
    public void setUp() {
        xmlSampleFile = FileGenerator.generate(FileGenerator.MB * 100L);
    }

    @AfterMethod
    public void tearDown() {
        if (xmlSampleFile.exists() && xmlSampleFile.isFile()) {
            xmlSampleFile.delete();
        }
    }

    @Test
    public void simpleConstructor() {
        new SbdhParser();
    }

    /**
     * Parses a rather large xml document with SBDH.
     *
     * @throws Exception
     */
    @Test
    public void parseMediumSizedFile() throws Exception {
        FileInputStream fileInputStream = new FileInputStream(xmlSampleFile);
        BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);

        Header sbdh = SbdhParser.parse(fileInputStream);

        assertTrue(sbdh != null, "SBDH was not detected");
        assertTrue(bufferedInputStream.available() > 0);
    }


    @Test(expectedExceptions = IllegalStateException.class)
    public void parseXmlFileWithoutSBDH() {
        InputStream resourceAsStream = getClass().getResourceAsStream(EHF_INVOICE_NO_SBDH_XML);

        Header sbdh = SbdhParser.parse(resourceAsStream);
        assertNull(sbdh);
    }
}
