package com.hp.maas.usecases.dev2prod;

import com.hp.maas.apis.Server;
import com.hp.maas.apis.model.rms.RMSInstance;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by sharir on 22/01/2015.
 */
public class Dev2Prod {
    public static void run(Server server) {

        List<RMSInstance> dev2ProdImportExportState = server.getRmsReaderAPI().readResources("Dev2ProdImportExportState");



        Collections.sort(dev2ProdImportExportState, new Comparator<RMSInstance>() {
            @Override
            public int compare(RMSInstance o1, RMSInstance o2) {
                return new Long(o1.getContent().getLong("StartTime")).compareTo(o2.getContent().getLong("StartTime"));
            }
        });

        for (RMSInstance instance : dev2ProdImportExportState) {

            SimpleDateFormat dateFormatGmt = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            dateFormatGmt.setTimeZone(TimeZone.getTimeZone("UTC"));


            System.out.println(dateFormatGmt.format(new Date(instance.getContent().getLong("StartTime"))) + " / "+instance.getContent().getString("Status"));
            System.out.println("=======================================================");
        }

    }
}
