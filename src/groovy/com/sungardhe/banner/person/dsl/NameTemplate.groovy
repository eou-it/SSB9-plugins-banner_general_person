package com.sungardhe.banner.person.dsl

import groovy.text.SimpleTemplateEngine

/**
 * Created by IntelliJ IDEA.
 * User: mhitrik
 * Date: 4/19/12
 * Time: 2:38 PM
 * To change this template use File | Settings | File Templates.
 */
class NameTemplate {

    def m = [:]

    /**
     * This method accepts a closure which is essentially the DSL. Delegate the closure methods to
     * the DSL class so the calls can be processed
     */
    def static format(closure) {
        NameTemplate nameDsl = new NameTemplate()
        closure.delegate = nameDsl
        closure()
    }

    /**
     * Store the parameter as a variable and use it later to output a formatted name
     */
    def lastName(def lastName) {
        m.lastName = lastName ?:""
    }

    def firstName(def firstName) {
        m.firstName = firstName ?:""
    }

    def mi(def mi) {
        m.mi = mi ?:""
    }

    def surnamePrefix(def surnamePrefix) {
        m.surnamePrefix = surnamePrefix ?:""
    }

    def nameSuffix(def nameSuffix) {
        m.nameSuffix = nameSuffix ?:""
    }

    def namePrefix(def namePrefix) {
        m.namePrefix = namePrefix ?:""
    }

    def formatTemplate(def formatTemplate) {
        m.formatTemplate = formatTemplate
    }

    def getText() {
        doText(this)
    }

    /**
     * Use markupBuilder to create a text output
     */
    private static doText(NameTemplate dsl) {
        SimpleTemplateEngine templateEngine = new SimpleTemplateEngine()
        def template = templateEngine.createTemplate(dsl.m.formatTemplate)
        Writable writable = template.make(dsl.m)

        //removes all double spaces and empty <,,> placeholders
        def str = writable.toString().replaceAll("\\s+", " ").replaceAll(", ,",",").trim()
        def finalStr

        //removes <,> from the ends of the string if they are first or last
         if (str[-1]== ','){
            return str[0..-2]
         }else if(str[0]== ','){
            return str[1..-1]
         }

        return str
    }
}
