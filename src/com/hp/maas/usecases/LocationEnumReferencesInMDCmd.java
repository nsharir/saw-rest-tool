package com.hp.maas.usecases;

import com.hp.maas.apis.Server;
import com.hp.maas.apis.model.metadata.EntityTypeDescriptor;
import com.hp.maas.apis.model.metadata.FieldDescriptor;
import com.hp.maas.apis.model.tenatManagment.Tenant;
import com.hp.maas.tools.FSOutput.FileSystemOutput;
import com.hp.maas.utils.executers.multiTenant.TenantCommand;
import com.hp.maas.utils.executers.reporters.Reporter;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;

/**
 * Created by sharir on 15/03/2015.
 */
public class LocationEnumReferencesInMDCmd implements TenantCommand {
    private FileSystemOutput out;

    public LocationEnumReferencesInMDCmd(FileSystemOutput out) {
        this.out = out;
    }

    @Override
    public void execute(Server server, Tenant tenant, Reporter reporter)  {
        List<EntityTypeDescriptor> entityTypeDescriptors = server.getMetadataAPI().loadAllFromServer();

        HashSet<String> refs = new HashSet<String>();
        for (EntityTypeDescriptor type : entityTypeDescriptors) {
            for (FieldDescriptor fieldDescriptor : type.getFields()) {
                if (fieldDescriptor.getLogicalType().equals("ENUM") && fieldDescriptor.getReferenceName().equals("LocationType")){
                    refs.add(type.getName()+"."+fieldDescriptor.getName());
                }
            }
        }



        try {
            for (String ref : refs) {
                out.dump("","all.log",ref);
            }
            if (refs.size() > 1){
                for (String ref : refs) {
                    out.dump("","location_customization.log",ref);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }



    }
}
