package com.hp.maas.utils.executers.multiTenant;

import com.hp.maas.apis.Server;
import com.hp.maas.apis.model.tenatManagment.Tenant;
import com.hp.maas.utils.executers.reporters.LogLevel;
import com.hp.maas.utils.executers.reporters.Reporter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sharir on 06/01/2015.
 */
public class MultiTenantExecutor {
    private String operatorTenantId;
    private TenantFilter filter;
    private Reporter reporter;

    private Server masterServer;

    private ArrayList<Tenant> tenants;

    public MultiTenantExecutor(String hostUrl, String operatorUser, String operatorPwd , String operatorTenantId,TenantFilter filter, Reporter reporter) {
        this.operatorTenantId = operatorTenantId;
        this.filter = filter;
        this.reporter = reporter;

        masterServer = new Server(hostUrl, operatorUser, operatorPwd,operatorTenantId);
        masterServer.authenticate();
        createTenantList(filter);
    }

    private void createTenantList(TenantFilter tenantsFilter) {

        List<Tenant> allTenants = masterServer.getTenantManagementAPI().getAllTenants();

        tenants = new ArrayList<Tenant>();

        for (Tenant t : allTenants) {
            if (!t.getId().equals(operatorTenantId) && "Active".equals(t.getState())) {
                if (tenantsFilter == null || tenantsFilter.shouldRun(t)) {
                    tenants.add(t);
                }
            }
        }
        reporter.report(LogLevel.INFO, "About to run over "+tenants.size()+" tenants (out of "+allTenants.size()+" ).");
    }

    public void run(TenantCommand cmd){
        int total = tenants.size();
        int current = 0;

        for (Tenant t : tenants) {
            current++;
            reporter.report(LogLevel.INFO, "["+current+"/"+total+"]"+"Running tenant ["+t.getType()+"/"+t.getId()+"/"+t.getTenantName()+"]...");
            reporter.report(LogLevel.INFO, "-----------------------------------------------------------------------------------------");
            try {
                runTenant(t, cmd);
            }catch(Throwable e){
                reporter.report(LogLevel.ERROR, "Error while executing tenant "+t);
                reporter.report(LogLevel.ERROR,e.toString());
            }
            reporter.report(LogLevel.INFO, "-----------------------------------------------------------------------------------------");;
        }

    }

    private void runTenant(Tenant tenant, TenantCommand cmd) {
        Server fork = masterServer.fork(tenant.getId());
        fork.authenticate();
        cmd.execute(fork, tenant, reporter);
    }
}
