/*******************************************************************************
 Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.person

/**
 * User: John Quinley
 * Date: 8/12/13
 *
 * This class provides a series of supported file name/extension pairs that are supported for a given banner id. It
 * provides additional support for determining information about a given picture file.
 */

class PictureFileNamesUtil {
    static extensions = ['jpg','png','gif','bmp']
    static allExtensions = extensions.collect { it.toLowerCase() } + extensions.collect { it.toUpperCase() }

    /**
     * Returns a collection of potential image file names for a given banner id. No check is done on the validity of the
     * id.
     *
     * @param id The banner id.
     *
     * @return A collection of potential file names for the banner id.
     */
    static getImgFileNames(id) {
        //def names = [];
        allExtensions.collect( { ext ->
            def partialId = id.replaceFirst(/..(.{3,7}).*/, /$1/)
            [ "${id}.${ext}", "I${id}.${ext}", "I${partialId}.${ext}"]
        }).flatten().collect( { it.toString() })
    }

    /**
     * Returns the extension, in lower case, for the given file name. This file name doesn't necessarily need to be
     * an image file.
     *
     * @param filename The file name in question.
     *
     * @return The extension of te file name in all lower case
     */
    static String getExtensionFromFilenameLowercase(filename) {
        def returned_value = ""
        def m = (filename =~ /(\.[^\.]*)$/)
        if (m.size() > 0) returned_value = ((m[0][0].size() > 0) ? m[0][0].substring(1).trim().toLowerCase() : "");
        return returned_value
    }
}
