/*******************************************************************************
 Copyright 2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.person

import grails.test.mixin.TestFor
import grails.util.Holders
import org.codehaus.groovy.grails.support.MockApplicationContext
import org.codehaus.groovy.grails.web.context.ServletContextHolder
import org.codehaus.groovy.grails.web.servlet.GrailsApplicationAttributes
import org.springframework.mock.web.MockServletContext
/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(PersonPictureBaseController)
class PersonPictureBaseControllerTests {

    void setUp() {

        // Setup our configuration path to our test location
        Holders.config.banner.picturesPath = System.getProperty('base.dir') + File.separator + "test" + File.separator +
                "resources" + File.separator + "images"

        Holders.config.grails.mime.types["jpg"] = "image/jpeg"
        Holders.config.grails.mime.types["png"] = "image/png"
        controller.personPictureService = new PersonPictureService()
    }


    void testDoViewValidUserJpg() {
        controller.params.bannerId = "JFROST789"
        controller.picture()

        assertEquals(300, controller.response.contentLength)
        assertEquals("image/jpeg", controller.response.contentType)
    }


    void testDoViewValidUserBmp() {
        controller.params.bannerId = "JFROST123"
        controller.picture()

        assertTrue(controller.response.contentLength < 32000) // 32K is the size of the BMP. The JPG should be smaller
        assertEquals("image/jpeg", controller.response.contentType)
    }


    void testDoViewInvalidUserWithDefault() {
        Holders.config.banner.defaultPhoto = System.getProperty('base.dir') + File.separator + "test" + File.separator +
            "resources" + File.separator + "images" + File.separator + "default.png"

        // We are going to need a resource file object for our images resources. Path doesn't matter since we are relying
        // on our defaultPhoto property
        MockApplicationContext.MockResource.metaClass.getFile = {
            return new File(System.getProperty('base.dir') + "dontmatter")
        }

        MockServletContext mockServletContext = new MockServletContext()
        MockApplicationContext applicationContext = new MockApplicationContext()
        mockServletContext.setAttribute(GrailsApplicationAttributes.APPLICATION_CONTEXT, applicationContext)

        applicationContext.registerMockResource("images", "doesnotmatter")
        ServletContextHolder.servletContext = mockServletContext

        controller.params.bannerId = "NOTME"
        controller.picture()

        assertEquals(22238, controller.response.contentLength) // The length of our default image
        assertEquals("image/png", controller.response.contentType)

        Holders.config.banner.remove("defaultPhoto")
    }


    void testDoViewInvalidUserWithoutDefault() {
        Holders.config.banner.remove("defaultPhoto")

        // We are going to need a resource file object for our images resources. Path doesn't matter since we are relying
        // on our defaultPhoto property
        MockApplicationContext.MockResource.metaClass.getFile = {
            return new File(System.getProperty('base.dir') + File.separator + "test" + File.separator +
                    "resources" + File.separator + "images" + File.separator + "resources")
        }

        MockServletContext mockServletContext = new MockServletContext()
        MockApplicationContext applicationContext = new MockApplicationContext()
        mockServletContext.setAttribute(GrailsApplicationAttributes.APPLICATION_CONTEXT, applicationContext)

        applicationContext.registerMockResource("images", "doesnotmatter")
        ServletContextHolder.servletContext = mockServletContext

        controller.params.bannerId = "NOTME"
        controller.picture()

        assertEquals(300, controller.response.contentLength) // The length of our default image
        assertEquals("image/png", controller.response.contentType)
    }

    void testDoViewInvalidUserWithNoDefault() {
        Holders.config.banner.remove("defaultPhoto")

        // We are going to need a resource file object for our images resources. Path doesn't matter since we are relying
        // on our defaultPhoto property
        MockApplicationContext.MockResource.metaClass.getFile = {
            return new File(System.getProperty('base.dir') + "badpath")
        }

        MockServletContext mockServletContext = new MockServletContext()
        MockApplicationContext applicationContext = new MockApplicationContext()
        mockServletContext.setAttribute(GrailsApplicationAttributes.APPLICATION_CONTEXT, applicationContext)

        applicationContext.registerMockResource("images", "doesnotmatter")
        ServletContextHolder.servletContext = mockServletContext

        controller.params.bannerId = "NOTME"
        controller.picture()

        assertEquals 404, controller.response.status
    }

    void testHasNoAccess() {
        def failingController = new FailingController()
        failingController.params.bannerId = "NOTME"
        failingController.picture()

        assertEquals 403, controller.response.status
    }

}

public class FailingController extends PersonPictureBaseController {
    boolean hasAccess(params)
    {
        return false
    }
}


