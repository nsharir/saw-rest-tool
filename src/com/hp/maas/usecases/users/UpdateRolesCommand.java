package com.hp.maas.usecases.users;

import com.hp.maas.apis.Server;
import com.hp.maas.apis.model.query.FilterBuilder;
import com.hp.maas.apis.model.query.SimpleFilterElement;
import com.hp.maas.apis.model.rms.RMSInstance;
import com.hp.maas.apis.model.tenatManagment.Tenant;
import com.hp.maas.tools.FSOutput.FileSystemOutput;
import com.hp.maas.utils.executers.multiTenant.TenantCommand;
import com.hp.maas.utils.executers.reporters.LogLevel;
import com.hp.maas.utils.executers.reporters.Reporter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sharir on 28/04/2015.
 */
public class UpdateRolesCommand implements TenantCommand {

    private boolean simulation;
    private List<String> upns;
    private String[] roles;
    private boolean append;
    private FileSystemOutput out;

    public UpdateRolesCommand(boolean simulation, String folder, List<String> upns, boolean append , String ... roles) {
        this.simulation = simulation;
        this.upns = upns;
        this.roles = roles;
        this.append = append;
        out = new FileSystemOutput(folder);
    }

    @Override
    public void execute(Server server, Tenant tenant, Reporter reporter) {

        List<String> newRoles = new ArrayList<String>();
        for (String role : roles) {
            newRoles.add(role);
        }

        for (String upn : upns) {
            List<RMSInstance> rmsInstances = server.getRmsReaderAPI().readResources("AuthorizationPrincipalResourceJSON", new FilterBuilder(new SimpleFilterElement("UserId", "=", upn)));
            if (rmsInstances.isEmpty()){
                reporter.report(LogLevel.WARN, "Upn: "+upn+""+" - NO Permissions defined");
                continue;
            }
            else{
                RMSInstance instance = rmsInstances.get(0);
                AuthorizationPrincipalResourceJSON permissions = new AuthorizationPrincipalResourceJSON(instance.getContent());


                List<String> currentRoles = permissions.getRoles();
                if (currentRoles.size() != 1 || !currentRoles.get(0).equals("Self-Service Portal User")){
                    continue;
                }

                reporter.report(LogLevel.INFO, "Upn: "+upn+""+" - adding permissions...");

                try {
                    out.dump("", upn.replace('@', '_') + ".json",permissions.getJson().toString(1) );

                    List<String> userRoles = permissions.getRoles();
                    if (append){
                        for (String newRole : newRoles) {
                            if (!userRoles.contains(newRole)) {
                                userRoles.add(newRole);
                            }
                        }
                    }else{
                        userRoles.clear();
                        userRoles.addAll(newRoles);
                    }
                    permissions.setRoles(userRoles);
                    out.dump("", upn.replace('@', '_') + "_fixed.json",permissions.getJson().toString(1) );

                    if (!simulation){

                        server.getRmsWriterAPI().updateResource(new RMSInstance("AuthorizationPrincipalResourceJSON",permissions.getJson()));
                        reporter.report(LogLevel.INFO, "Upn: "+upn+""+" - Done!");
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
