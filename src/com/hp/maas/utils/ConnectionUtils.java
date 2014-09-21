package com.hp.maas.utils;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

/**
 * Created with IntelliJ IDEA.
 * User: Nadav
 * Date: 9/9/14
 * Time: 10:21 AM
 * To change this template use File | Settings | File Templates.
 */
public class ConnectionUtils {
    public static String connectAndGetResponse(HttpURLConnection con) throws IOException {

        Log.log("Invoked URL: "+con.getURL().toString());

        InputStream in = con.getInputStream();
        String encoding = con.getContentEncoding();
        encoding = encoding == null ? "UTF-8" : encoding;
        return IOUtils.toString(in, encoding);

    }
}
