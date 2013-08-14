/*******************************************************************************
 Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.person

import grails.test.GrailsUnitTestCase

class PictureFileUtilTests extends GrailsUnitTestCase{
    private static def imageLocation = System.getProperty('base.dir') + File.separator + "test" + File.separator + "resources" + File.separator + "images"

    protected void setUp() {
        super.setUp()
    }


    protected void tearDown() {
        super.tearDown()
    }


    void testDontFine() {
        assertNull PictureFileUtils.getImgFile("images", ['dontfindme.png'])
        assertNull PictureFileUtils.getImgFile(imageLocation, ['dontfindme.png'])
    }


    void testFindPng() {
        File imageFile = PictureFileUtils.getImgFile(imageLocation, ['JFROST789.png'])
        assertNotNull imageFile
        assertTrue imageFile.exists()

        imageFile = PictureFileUtils.getImgFile(imageLocation, ['JFROST.PNG'])
        assertNotNull imageFile
        assertTrue imageFile.exists()
    }


    void testFindJpg() {
        File imageFile = PictureFileUtils.getImgFile(imageLocation, ['JFROST789.jpg'])
        assertNotNull imageFile
        assertTrue imageFile.exists()

        imageFile = PictureFileUtils.getImgFile(imageLocation, ['JFROST.JPG'])
        assertNotNull imageFile
        assertTrue imageFile.exists()
    }


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


    void testBmpToJpg() {
        File imageFile = PictureFileUtils.getImgFile(imageLocation, ['JFROST789.bmp'])
        byte[] jpgFile = PictureFileUtils.bmpToJpg(imageFile)
        assertNotNull jpgFile

        // Test that our JPG file will be smaller than out BMP file
        assertTrue jpgFile.size() < imageFile.size()
    }
}
