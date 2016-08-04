package com.example.chart;

import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.ChartType;
import com.vaadin.addon.charts.model.DataSeries;
import com.vaadin.addon.charts.model.DataSeriesItem;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.VerticalLayout;
import io.keen.client.java.AbsoluteTimeframe;
import io.keen.client.java.KeenQueryClient;
import io.keen.client.java.Query;
import io.keen.client.java.QueryType;
import io.keen.client.java.result.QueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Component
@Scope("prototype")
public class ValueChart extends VerticalLayout {

    @Autowired
    private KeenQueryClient keenQueryClient;

    private ProgressBar spinner = new ProgressBar();

    private Chart chart = new Chart(ChartType.AREA);

    @PostConstruct
    public void init() {
        spinner.setIndeterminate(true);
        addComponents(spinner, chart);
        setComponentAlignment(spinner, Alignment.MIDDLE_CENTER);

        chart.getConfiguration().setTitle("Last Hour Delivered Value");
        chart.setVisible(false);
    }

    public void update() {
        try {
            Instant now = Instant.now();
            ZoneId zone = ZoneId.systemDefault();
            String start = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH':00'").withZone(zone).format(now);
            String end = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH':59'").withZone(zone).format(now);

            Query query = new Query.Builder(QueryType.SUM)
                    .withEventCollection("gifts")
                    .withTargetProperty("value")
                    .withTimeframe(new AbsoluteTimeframe(start, end))
                    .withInterval("minutely")
                    .withTimezone("" + zone.getRules().getOffset(now).getTotalSeconds())
                    .build();

            QueryResult queryResult = keenQueryClient.execute(query);
            DataSeries series = new DataSeries("Delivered value per minute");

            queryResult.getIntervalResults().forEach((t, r) -> {
                series.add(new DataSeriesItem(t.getEnd(), r.longValue()));
            });

            chart.getConfiguration().setSeries(series);
            chart.drawChart();
            chart.setVisible(true);
            spinner.setVisible(false);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
