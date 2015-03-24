package com.hp.maas.utils.chart;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.SubCategoryAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.GroupedStackedBarRenderer;
import org.jfree.data.KeyToGroupMap;
import org.jfree.data.category.DefaultCategoryDataset;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by sharir on 24/03/2015.
 */
public class ChartUtils {

    public static void openChartAsFile(JFreeChart chart) {
        try {
            File tsChart = File.createTempFile(System.currentTimeMillis() + "", ".png");
            tsChart.deleteOnExit();
            ChartUtilities.saveChartAsPNG(tsChart, chart, 1200, 500);


            Runtime.getRuntime().exec("mspaint " + tsChart.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static JFreeChart generateClusteredStackChart(Map<ClusteredChartEntry, Integer> counters , String title) {

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        List<ClusteredChartEntry> entries = new ArrayList<ClusteredChartEntry>(counters.keySet().size());
        entries.addAll(counters.keySet());

        Collections.sort(entries, new Comparator<ClusteredChartEntry>() {
            @Override
            public int compare(ClusteredChartEntry o1, ClusteredChartEntry o2) {
                int xCompare = o1.x.compareTo(o2.x);

                if (xCompare != 0) {
                    return xCompare;
                }
                return o1.yGroup.compareTo(o2.yGroup);
            }
        });

        for (ClusteredChartEntry entry : entries) {
            dataset.addValue(counters.get(entry), entry.y, entry.x);
        }

        JFreeChart chart = ChartFactory.createStackedBarChart(
                title,  // chart title
                "",                  // domain axis label
                "",                     // range axis label
                dataset,                     // data
                PlotOrientation.VERTICAL,    // the plot orientation
                true,                        // legend
                true,                        // tooltips
                false                        // urls
        );

        Map<String,String> yMap = new HashMap<String, String>();
        List<String> categories = new ArrayList<String>();

        for (ClusteredChartEntry tenantEntry : entries) {
            int catIndex = categories.indexOf(tenantEntry.yGroup) + 1;
            if (catIndex == 0){
                categories.add(tenantEntry.yGroup);
                catIndex = categories.indexOf(tenantEntry.yGroup) + 1;
            }
            yMap.put(tenantEntry.y,"G"+catIndex);
        }

        GroupedStackedBarRenderer renderer = new GroupedStackedBarRenderer();
        KeyToGroupMap map = new KeyToGroupMap("G1");
        List<Map.Entry<String,String>> orderedByYGroup = new ArrayList<Map.Entry<String, String>>();
        orderedByYGroup.addAll(yMap.entrySet());
        Collections.sort(orderedByYGroup, new Comparator<Map.Entry<String, String>>() {
            @Override
            public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
                return o1.getValue().compareTo(o2.getValue());
            }
        });
        for (Map.Entry<String, String> group : orderedByYGroup) {
            map.mapKeyToGroup(group.getKey(), group.getValue());
        }

        renderer.setSeriesToGroupMap(map);

        //margin between bar.
        renderer.setItemMargin(0.03);
        //end

        SubCategoryAxis dom_axis = new SubCategoryAxis("");
        //Margin between group
        dom_axis.setCategoryMargin(0.15);
        //end

        for (String cat : categories) {
            dom_axis.addSubCategory(cat);
        }

        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        plot.setDomainAxis(dom_axis);
        plot.setRenderer(renderer);
        return chart;
    }

    public static class ClusteredChartEntry implements Comparator<ClusteredChartEntry>{
        String x;
        String yGroup;
        String y;

        public ClusteredChartEntry(String x, String yGroup , String ySubGroup){
            this.x = x;
            this.yGroup =yGroup;
            this.y = yGroup +"/"+ySubGroup;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            ClusteredChartEntry that = (ClusteredChartEntry) o;

            if (x != null ? !x.equals(that.x) : that.x != null) return false;
            if (y != null ? !y.equals(that.y) : that.y != null) return false;
            if (yGroup != null ? !yGroup.equals(that.yGroup) : that.yGroup != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = x != null ? x.hashCode() : 0;
            result = 31 * result + (yGroup != null ? yGroup.hashCode() : 0);
            result = 31 * result + (y != null ? y.hashCode() : 0);
            return result;
        }

        @Override
        public int compare(ClusteredChartEntry o1, ClusteredChartEntry o2) {
            return o1.y.compareTo(o2.y);
        }
    }
}
