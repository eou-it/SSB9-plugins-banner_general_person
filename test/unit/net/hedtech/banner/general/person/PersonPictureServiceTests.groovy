/*******************************************************************************
 Copyright 2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.person

import grails.test.GrailsUnitTestCase
import grails.util.Holders

class PersonPictureServiceTests  extends GrailsUnitTestCase{

    def personPictureService = new PersonPictureService()
    def testResourcePath = System.getProperty('base.dir') + File.separator + "test" + File.separator + "resources" +
            File.separator + "images" + File.separator + "resources"

    protected void setUp() {
        super.setUp()

        // Setup our configuration path to our test location
        Holders.config.banner.picturesPath = System.getProperty('base.dir') + File.separator + "test" + File.separator +
                "resources" + File.separator + "images"
    }

    protected void tearDown() {
        super.tearDown()
    }


    void testGetPictureFileForUserWith() {
        File pictureFile = personPictureService.getPictureFileForUserWith("JFROST789")

        assertNotNull pictureFile
        assertTrue(pictureFile.getName().endsWith(".jpg"))
    }

    void testBadBannerId() {
        shouldFail(RuntimeException) {
           personPictureService.getPictureFileForUserWith("/file/JFROST789")
        }
    }


    void testGetDefaultPictureFileNoProperty() {
        File pictureFile = personPictureService.getDefaultPictureFile(testResourcePath)
        assertNotNull(pictureFile)
        assertEquals(PersonPictureService.NO_PHOTO_AVAILABLE, pictureFile.getName())
    }

    void testGetDefaultPictureFileWithProperty() {
        Holders.config.banner.defaultPhoto = System.getProperty('base.dir') + File.separator + "test" + File.separator +
                "resources" + File.separator + "images" + File.separator + "default.png"

        File pictureFile = personPictureService.getDefaultPictureFile(testResourcePath)
        assertNotNull(pictureFile)
        assertEquals("default.png", pictureFile.getName())

        Holders.config.banner.remove("defaultPhoto")
    }

    void testGetDefaultPictureFileNotFound() {
        Holders.config.banner.defaultPhoto = System.getProperty('base.dir') + File.separator + "test" + File.separator +
                "resources" + File.separator + "images" + File.separator + "defaultbad.png"

        File pictureFile = personPictureService.getDefaultPictureFile(testResourcePath)
        assertNull pictureFile

        Holders.config.banner.remove("defaultPhoto")
    }
}
