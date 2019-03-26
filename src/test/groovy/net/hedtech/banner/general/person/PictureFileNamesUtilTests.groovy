/*******************************************************************************
 Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.person

import grails.test.GrailsUnitTestCase
import org.junit.Test


class PictureFileNamesUtilTests extends GrailsUnitTestCase{
    public void setUp() {
        super.setUp()
    }

    public void tearDown() {
        super.tearDown()
    }

    @Test
    void testImgFileNames() {
        def names = PictureFileNamesUtil.getImgFileNames('JFROST789')

        assert names.contains('JFROST789.bmp')
        assert names.contains('IJFROST789.bmp')
        assert names.contains('IROST789.bmp')

        assert names.contains('JFROST789.BMP')
        assert names.contains('IJFROST789.BMP')
        assert names.contains('IROST789.BMP')

        assert names.contains('JFROST789.jpg')
        assert names.contains('IJFROST789.jpg')
        assert names.contains('IROST789.jpg')

        assert names.contains('JFROST789.JPG')
        assert names.contains('IJFROST789.JPG')
        assert names.contains('IROST789.JPG')

        assert names.contains('JFROST789.png')
        assert names.contains('IJFROST789.png')
        assert names.contains('IROST789.png')

        assert names.contains('JFROST789.PNG')
        assert names.contains('IJFROST789.PNG')
        assert names.contains('IROST789.PNG')

        assert names.contains('JFROST789.gif')
        assert names.contains('IJFROST789.gif')
        assert names.contains('IROST789.gif')

        assert names.contains('JFROST789.GIF')
        assert names.contains('IJFROST789.GIF')
        assert names.contains('IROST789.GIF')

        names = PictureFileNamesUtil.getImgFileNames('JFROST')
        assert names.contains('IROST.bmp')
        assert names.contains('IROST.JPG')
        assert names.contains('IROST.png')

        names = PictureFileNamesUtil.getImgFileNames('JFRO')
        assert names.contains('JFRO.bmp')
        assert names.contains('IJFRO.bmp')
        assert !names.contains('IRO.bmp')

        assert names.contains('JFRO.BMP')
        assert names.contains('IJFRO.BMP')
        assert !names.contains('IRO.BMP')

        assert names.contains('JFRO.jpg')
        assert names.contains('IJFRO.jpg')
        assert !names.contains('IRO.jpg')

        assert names.contains('JFRO.JPG')
        assert names.contains('IJFRO.JPG')
        assert !names.contains('IRO.JPG')

        assert names.contains('JFRO.png')
        assert names.contains('IJFRO.png')
        assert !names.contains('IRO.png')

        assert names.contains('JFRO.PNG')
        assert names.contains('IJFRO.PNG')
        assert !names.contains('IRO.PNG')

        assert names.contains('JFRO.gif')
        assert names.contains('IJFRO.gif')
        assert !names.contains('IRO.gif')

        assert names.contains('JFRO.GIF')
        assert names.contains('IJFRO.GIF')
        assert !names.contains('IRO.GIF')
    }

    @Test
    void testGetExtensionFromFilename() {
        assertEquals( "bmp", PictureFileNamesUtil.getExtensionFromFilenameLowercase("JFROST789.bmp"))
        assertEquals( "bmp", PictureFileNamesUtil.getExtensionFromFilenameLowercase("JFROST789.BMP"))

        // No extension
        assertEquals("", PictureFileNamesUtil.getExtensionFromFilenameLowercase("JFROST789"))

        // 1 char file name
        assertEquals("png", PictureFileNamesUtil.getExtensionFromFilenameLowercase("1.png"))
    }
}
