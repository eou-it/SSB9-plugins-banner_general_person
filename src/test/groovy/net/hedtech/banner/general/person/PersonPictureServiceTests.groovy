/*******************************************************************************
 Copyright 2013-2019 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.person

import org.grails.testing.GrailsUnitTest
import grails.test.mixin.TestFor
import grails.util.Holders
import org.junit.Test

@TestFor(PersonPictureService)
class PersonPictureServiceTests {

    def personPictureService
    def testResourcePath = System.getProperty('base.dir') + File.separator + "test" + File.separator + "resources" +
            File.separator + "images" + File.separator + "resources"

    public void setUp() {
        personPictureService = service
        // Setup our configuration path to our test location
        Holders.config.banner.picturesPath = System.getProperty('base.dir') + File.separator + "test" + File.separator +
                "resources" + File.separator + "images"
    }

    public void tearDown() {
    }

    @Test
    void testGetPictureFileForUserWith() {
        File pictureFile = personPictureService.getPictureFileForUserWith("JFROST789")

        assertNotNull pictureFile
        assertTrue(pictureFile.getName().endsWith(".jpg"))
    }

    @Test
    void testBadBannerId() {
           assertNull (personPictureService.getPictureFileForUserWith("/file/JFROST789"))
    }

    @Test
    void testGetDefaultPictureFileNoProperty() {
        File pictureFile = personPictureService.getDefaultPictureFile(testResourcePath)
        assertNotNull(pictureFile)
        assertEquals(PersonPictureService.NO_PHOTO_AVAILABLE, pictureFile.getName())
    }

    @Test
    void testGetDefaultPictureFileWithProperty() {
        Holders.config.banner.defaultPhoto = System.getProperty('base.dir') + File.separator + "test" + File.separator +
                "resources" + File.separator + "images" + File.separator + "default.png"

        File pictureFile = personPictureService.getDefaultPictureFile(testResourcePath)
        assertNotNull(pictureFile)
        assertEquals("default.png", pictureFile.getName())

        Holders.config.banner.remove("defaultPhoto")
    }

    @Test
    void testGetDefaultPictureFileNotFound() {
        Holders.config.banner.defaultPhoto = System.getProperty('base.dir') + File.separator + "test" + File.separator +
                "resources" + File.separator + "images" + File.separator + "defaultbad.png"

        File pictureFile = personPictureService.getDefaultPictureFile(testResourcePath)
        assertNull pictureFile

        Holders.config.banner.remove("defaultPhoto")
    }
}
