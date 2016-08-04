package com.example;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import io.keen.client.java.AbsoluteTimeframe;
import io.keen.client.java.Query;
import io.keen.client.java.QueryType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.vaadin.keen.charts.KeenChart;

import javax.annotation.PostConstruct;

@SpringComponent
@Scope("prototype")
public class Dashboard extends VerticalLayout {

    @Value("${keen.projectId}")
    private String projectId;

    @Value("${keen.readKey}")
    private String readKey;

    @PostConstruct
    public void init() {
        setSizeFull();
        setMargin(true);
        setSpacing(true);

        Label title = new Label("Dashboard");
        title.addStyleName(ValoTheme.LABEL_H1);
        addComponent(title);

        KeenChart total = new KeenChart(projectId, readKey, new Query.Builder(QueryType.SUM)
                .withEventCollection("gifts")
                .withTargetProperty("value")
                .withTimeframe(new AbsoluteTimeframe("2016-08-01T00:00", "2016-08-31T23:59"))
                .build());

        KeenChart distribution = new KeenChart(projectId, readKey, new Query.Builder(QueryType.SUM)
                .withEventCollection("gifts")
                .withTargetProperty("value")
                .withTimeframe(new AbsoluteTimeframe("2016-08-01T00:00", "2016-08-31T23:59"))
                .withGroupBy("name")
                .build());

        KeenChart value = new KeenChart(projectId, readKey, new Query.Builder(QueryType.SUM)
                .withEventCollection("gifts")
                .withTargetProperty("value")
                .withTimeframe(new AbsoluteTimeframe("2016-08-01T00:00", "2016-08-31T23:59"))
                .withGroupBy("name")
                .build());

        KeenChart count = new KeenChart(projectId, readKey, new Query.Builder(QueryType.COUNT)
                .withEventCollection("gifts")
                .withTimeframe(new AbsoluteTimeframe("2016-08-01T00:00", "2016-08-31T23:59"))
                .withInterval("daily")
                .build());

        GridLayout chartsLayout = new GridLayout(2, 2, total, distribution, value, count);
        chartsLayout.setSizeFull();
        chartsLayout.setSpacing(true);
        addComponent(chartsLayout);
        setExpandRatio(chartsLayout, 1);
    }

}
