package com.hp.maas.utils.executers.multiTenant;

import com.hp.maas.apis.Server;
import com.hp.maas.apis.model.tenatManagment.Tenant;
import com.hp.maas.utils.executers.reporters.ConsoleReporter;
import com.hp.maas.utils.executers.reporters.LogLevel;
import com.hp.maas.utils.executers.reporters.Reporter;

/**
 * Created by sharir on 26/01/2015.
 */
public class SingleTenantExecutor {

    private final Reporter reporter;
    private final Server server ;
    private final Tenant tenant;


    public SingleTenantExecutor(Server server, Reporter reporter) {
        this.server = server;
        this.reporter = reporter;
        this.tenant = new Tenant(server.getTenantId(),"unknown","unknown","unknown","unknown","unknown");
        server.authenticate();

    }

    public SingleTenantExecutor(String hostUrl, String user, String pwd , String tenant, Reporter reporter) {
        this(new Server(hostUrl, user, pwd,tenant),reporter);

    }

    public SingleTenantExecutor(Server server) {
        this(server,new ConsoleReporter(LogLevel.INFO));
    }

    public SingleTenantExecutor(String hostUrl, String user, String pwd , String tenant) {
        this(hostUrl, user, pwd, tenant, new ConsoleReporter(LogLevel.INFO));
    }

    public void run(TenantCommand cmd){
        try {
            cmd.execute(server,tenant,reporter );
        }catch(Throwable e){
            reporter.report(LogLevel.ERROR, "Error while executing tenant "+ tenant.getId());
            reporter.report(LogLevel.ERROR,e.toString());
            e.printStackTrace();
        }

    }
}
