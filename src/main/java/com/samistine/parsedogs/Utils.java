package com.samistine.parsedogs;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 *
 * @author Samuel Seidel
 */
public class Utils {

    public static XStream getXStream()
    {
        XStream xstream = new XStream(new DomDriver());
        xstream.autodetectAnnotations(true);
        return xstream;
    }
}
