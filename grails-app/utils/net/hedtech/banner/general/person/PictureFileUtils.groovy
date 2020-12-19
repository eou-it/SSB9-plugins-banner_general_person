/*******************************************************************************
 Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.person

import javax.imageio.ImageIO
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

/**
 * This class provides basic utilities for processing person pictures.
 */
class PictureFileUtils {

    /**
     * Returns an image file based on the base path and a list of possible file names.
     * It will return the first one found or null if none are found.
     *
     *
     * @param basePath The base path where the images are stored
     *
     * @param fromThese The list of possible file names. The first one is returned.
     *
     * @return The first file in the list to exist or null if none exist.
     */
    static File getImgFile(basePath, fromThese) {
        fromThese.findResult { name ->
            def file = new File(basePath, name)
            (file.exists() && Files.isReadable(Paths.get(file.getPath()))) ? file : null
        }
    }


    /**
     * Converts a BMP file to a JPG file.
     *
     * @param imgFile The BMP file.
     *
     * @return The BMP file as a JPG file.
     */
    static byte[] bmpToJpg(File imgFile){
        def outputStream = new ByteArrayOutputStream()
        ImageIO.write(ImageIO.read(imgFile), 'jpg', outputStream)
        outputStream.toByteArray()
    }

}
