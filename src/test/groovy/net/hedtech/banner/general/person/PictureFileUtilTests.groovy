/*******************************************************************************
 Copyright 2009-2019 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.person

import org.grails.testing.GrailsUnitTest
import org.junit.Test
import spock.lang.Specification
import static org.junit.Assert.*

class PictureFileUtilTests {
    private static def imageLocation = System.getProperty('base.dir') + File.separator + "test" + File.separator + "resources" + File.separator + "images"

    public void setup() {
    }


    public void tearDown() {
        super.tearDown()
    }

    @Test
    void testDontFind() {
        assertNull PictureFileUtils.getImgFile("images", ['dontfindme.png'])
        assertNull PictureFileUtils.getImgFile(imageLocation, ['dontfindme.png'])
    }

    @Test
    void testFindPng() {
        File imageFile = PictureFileUtils.getImgFile(imageLocation, ['JFROST789.png'])
        assertNotNull imageFile
        assertTrue imageFile.exists()

        imageFile = PictureFileUtils.getImgFile(imageLocation, ['JFROST.PNG'])
        assertNotNull imageFile
        assertTrue imageFile.exists()
    }

    @Test
    void testFindJpg() {
        File imageFile = PictureFileUtils.getImgFile(imageLocation, ['JFROST789.jpg'])
        assertNotNull imageFile
        assertTrue imageFile.exists()

        imageFile = PictureFileUtils.getImgFile(imageLocation, ['JFROST.JPG'])
        assertNotNull imageFile
        assertTrue imageFile.exists()
    }

    @Test
    void testFindOrder() {
        File imageFile = PictureFileUtils.getImgFile(imageLocation, ['JFROST789.jpg', 'JFROST789.png'])
        assertNotNull imageFile
        assertTrue imageFile.exists()
        assertTrue imageFile.getName() == "JFROST789.jpg"

        imageFile = PictureFileUtils.getImgFile(imageLocation, ['JFROST.PNG','JFROST.JPG'])
        assertNotNull imageFile
        assertTrue imageFile.exists()
        assertTrue imageFile.getName() == "JFROST.PNG"
    }

    @Test
    void testBmpToJpg() {
        File imageFile = PictureFileUtils.getImgFile(imageLocation, ['JFROST789.bmp'])
        byte[] jpgFile = PictureFileUtils.bmpToJpg(imageFile)
        assertNotNull jpgFile

        // Test that our JPG file will be smaller than out BMP file
        assertTrue jpgFile.size() < imageFile.size()
    }
}
