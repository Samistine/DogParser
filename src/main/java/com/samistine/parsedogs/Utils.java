/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.samistine.parsedogs;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 *
 * @author Samuel
 */
public class Utils {

    public static XStream getXStream() {
        XStream xstream = new XStream(new DomDriver());
        xstream.autodetectAnnotations(true);
        return xstream;
    }
}
