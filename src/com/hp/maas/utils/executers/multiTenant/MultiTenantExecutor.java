package com.hp.maas.utils.executers.multiTenant;

import com.hp.maas.apis.Server;
import com.hp.maas.apis.model.tenatManagment.Tenant;
import com.hp.maas.utils.executers.reporters.ConsoleReporter;
import com.hp.maas.utils.executers.reporters.LogLevel;
import com.hp.maas.utils.executers.reporters.Reporter;
import org.apache.commons.lang3.exception.ExceptionUtils;

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

    public MultiTenantExecutor(Server masterServer,TenantFilter filter) {
        this(masterServer, filter,new ConsoleReporter(LogLevel.INFO));
    }
    public MultiTenantExecutor(Server masterServer,TenantFilter filter, Reporter reporter) {
        this.masterServer = masterServer;
        masterServer.authenticate();
        this.operatorTenantId = masterServer.getTenantId();
        this.filter = filter;
        this.reporter = reporter;
        createTenantList(filter);
    }

    public MultiTenantExecutor(String hostUrl, String operatorUser, String operatorPwd , String operatorTenantId,TenantFilter filter, Reporter reporter) {
        this (new Server(hostUrl, operatorUser, operatorPwd,operatorTenantId),filter, reporter);
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
            reporter.report(LogLevel.INFO, "["+current+"/"+total+"]"+"Running tenant ["+t.getType()+"/"+t.getId()+"/"+t.getTenantName()+"/"+t.getVersion()+"]...");
            reporter.report(LogLevel.INFO, "-----------------------------------------------------------------------------------------");
            try {
                runTenant(t, cmd);
                reporter.report(LogLevel.INFO, "Done executing tenant "+t);
            }catch(Throwable e){
                reporter.report(LogLevel.ERROR, "Error while executing tenant "+t);
                reporter.report(LogLevel.ERROR, ExceptionUtils.getStackTrace(e));
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
