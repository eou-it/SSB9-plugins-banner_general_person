/*******************************************************************************
 Copyright 2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.person

import grails.util.Holders
import org.codehaus.groovy.grails.web.context.ServletContextHolder
import org.codehaus.groovy.grails.web.servlet.GrailsApplicationAttributes
import org.springframework.context.ApplicationContext

/**
 * User: John Quinley
 * Date: 8/12/13
 *
 * This is the base controller used to provide pictures for Banner IDs. It maps Banner ID to image files according to
 * the rules provided by PersonPictureService.
 *
 * @see net.hedtech.banner.general.person.PersonPictureService
 *
 */
class PersonPictureBaseController {

    static defaultAction = "picture"

    def personPictureService


    /**
     * Renders the image without applying security. To apply security, extend this controller and implement the method
     * hasAccess(params)
     *
     * This view takes the following parameters:
     *   -- bannerId The banner id of the user to render the image for.
     */
    def picture() {
        if (hasAccess(params))
        {
            def pictureBytes
            def contentType

            def imageFile = personPictureService.getPictureFileForUserWith(params.bannerId)
            if (null == imageFile)
            {
                // we didn't find a file for our user, so send back the default
                imageFile = getDefaultPhotoFile()
            }

            // We didn't find a default either
            if (null == imageFile)
            {
                response.sendError(403)
            }
            else
            {
                def extension = PictureFileNamesUtil.getExtensionFromFilenameLowercase(imageFile.getName())

                if (extension == "bmp") {
                    pictureBytes = PictureFileUtils.bmpToJpg(imageFile)
                    contentType = 'image/jpeg'
                }
                else
                {
                    pictureBytes = imageFile.readBytes()
                    contentType = Holders.config.grails.mime.types[extension]
                }

                response.setHeader('Content-length', pictureBytes.length.toString())
                response.contentType = contentType
                response.outputStream << pictureBytes
                response.outputStream.flush()
            }
        }
        else
        {
            response.sendError(403)
        }

    }


    /**
     * Override this method in order to provide additional security before providing access to the photo.
     * @param params The params that are accessible to the controller.
     *  -- bannerId The Banner ID of the user whose photo is being requrested.
     *
     * @return True if the currently logged in user has access to the user's photo
     */
    boolean hasAccess(params)
    {
        return true
    }


    /**
     * This method returns the default photo file. It uses the PersonPictureService, passing in the controllers
     * images resource path.
     *
     * You can override this method to provide a different default photo file.
     *
     * @return The default file. Null is returned if no default photo file was found.
     */
    File getDefaultPhotoFile()
    {
        return personPictureService.getDefaultPictureFile(getImagesResourcePath())
    }


    private def getImagesResourcePath()
    {
        return getApplicationContext().getResource('images').file.absolutePath
    }


    private ApplicationContext getApplicationContext() {
        return (ApplicationContext) ServletContextHolder.getServletContext().getAttribute(GrailsApplicationAttributes.APPLICATION_CONTEXT)
    }
}
