/*********************************************************************************
Copyright 2012 Ellucian Company L.P. and its affiliates.
**********************************************************************************/
 package net.hedtech.banner.general.person

import grails.util.Holders

/**
 * Provides access to people's pictures on the file system
 *
 * Note: This service does not extend BaseService as it doesn't access the database.
 */
class PersonPictureService {
    static transactional = false

    public static final String NO_PHOTO_AVAILABLE = "no_photo_available.png"


    private getImgPath() {
        Holders.config.banner.picturesPath ? Holders.config.banner.picturesPath : System.getProperty('base.dir') + File.separator + 'images'
    }


    private String getDefaultImgPath() {
        Holders.config.banner.defaultPhoto ? Holders.config.banner.defaultPhoto : ""
    }


    /**
     * Returns the picture file for the given Banner ID. It utilized the configuration property
     * banner.picturesPath to determine the location of the picture files. If the configuration property is not set,
     * it defaults to ${basedir} + '/imagees'
     *
     * @param bannerId The given Banner ID
     *
     * @param resourcePath The path of the caller's image resources. Typically retrieved from the application context.
     *
     * @return The picture file or null if no picture is found for the given Banner ID.
     *
     * @throws RuntimeException if the banner id contains path characters.
     */
    File getPictureFileForUserWith(bannerId) {
        // don't let users do directory tree traversal in bannerId, and ensure it's not empty
        if (!(bannerId ==~ '[^/\\\\]+'))
            return null // let the default silhouette load

        // Get a list of possibe file names
        def possibleFiles = PictureFileNamesUtil.getImgFileNames(bannerId)
        def imageFile =  PictureFileUtils.getImgFile(getImgPath(), possibleFiles)
        return imageFile
    }


    /**
     * This method will return the default picture file. The default picture file is determined by:
     * 1. A property called banner.defaultPhoto, which is the fully qualified file name of the default photo.
     * 2. If such a thing doesn't exist, then it will use the passed in resource path and return the file
     * identified above with NO_PHOTO_AVAILABLE. So this file would be ${resourcePath}/NO_PHOTO_AVAILABLE
     *
     * @param resourcePath The path of the resources
     *
     * @return The default file. If the file does not exist, null is returned.
     */
    File getDefaultPictureFile(resourcePath)
    {
        String defaultFilePath = getDefaultImgPath()
        File defaultFile

        if (defaultFilePath != "")
            defaultFile = new File(defaultFilePath)
        else
            defaultFile = new File( resourcePath, NO_PHOTO_AVAILABLE)

        if (!defaultFile.exists())
            return null
        else
            return defaultFile
    }
}
