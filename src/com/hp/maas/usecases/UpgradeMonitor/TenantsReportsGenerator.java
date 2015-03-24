package com.hp.maas.usecases.UpgradeMonitor;

import com.hp.maas.apis.Server;
import com.hp.maas.apis.model.tenatManagment.Tenant;
import com.hp.maas.utils.chart.ChartUtils;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.SubCategoryAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.GroupedStackedBarRenderer;
import org.jfree.data.KeyToGroupMap;
import org.jfree.data.category.DefaultCategoryDataset;

import java.util.*;

/**
 * Created by sharir on 24/03/2015.
 */
public class TenantsReportsGenerator {

    public static void createVersionsChart(Server server){

        generateChart(server, new EntryCreator() {
            @Override
            public ChartUtils.ClusteredChartEntry createEntry(Tenant tenant) {
                return new ChartUtils.ClusteredChartEntry(tenant.getVersion(),tenant.getType(),tenant.getState());
            }
        });

    }


    private static void generateChart(Server server , EntryCreator entryCreator) {
        List<Tenant> tenants = server.getTenantManagementAPI().getAllTenants();

        Map<ChartUtils.ClusteredChartEntry, Integer> counters = new HashMap<ChartUtils.ClusteredChartEntry, Integer>();

        int active = 0;

        for (Tenant tenant : tenants) {
            ChartUtils.ClusteredChartEntry te = entryCreator.createEntry(tenant);
            Integer current = counters.get(te);
            if (current == null){
                current = 0;
            }
            current++;
            counters.put(te, current);
            if ("Active".equals(tenant.getState())){
                active++;
            }
        }

        JFreeChart chart = ChartUtils.generateClusteredStackChart(counters,"Tenants per version  - "+server.getServerUrl()+" ("+active+" active tenants)");

        ChartUtils.openChartAsFile(chart);
    }


    private static interface EntryCreator{
        public ChartUtils.ClusteredChartEntry createEntry(Tenant tenant);
    }


}