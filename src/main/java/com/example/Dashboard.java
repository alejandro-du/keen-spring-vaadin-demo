package com.example;

import com.example.chart.TotalChart;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;

@SpringComponent
@Scope("prototype")
public class Dashboard extends VerticalLayout {

    @Autowired
    private TotalChart totalChart;

    @PostConstruct
    public void init() {
        setMargin(true);
        setSpacing(true);

        Label title = new Label("Dashboard");
        title.addStyleName(ValoTheme.LABEL_H1);
        addComponent(title);

        GridLayout chartsLayout = new GridLayout(2, 2, totalChart);
        addComponent(chartsLayout);
    }

    public void update() {
        new Thread(() -> {
            UI.getCurrent().access(() -> {
                totalChart.update();
            });
        }).start();
    }

}
