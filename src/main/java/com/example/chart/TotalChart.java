package com.example.chart;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import io.keen.client.java.AbsoluteTimeframe;
import io.keen.client.java.KeenQueryClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Component
@Scope("prototype")
public class TotalChart extends VerticalLayout {

    @Autowired
    private KeenQueryClient keenQueryClient;

    private Label total = new Label("...");

    @PostConstruct
    public void init() {
        Label title = new Label("Total");
        addComponent(title);
        setComponentAlignment(title, Alignment.MIDDLE_CENTER);

        total.addStyleName(ValoTheme.LABEL_H1);
        total.addStyleName(ValoTheme.LABEL_BOLD);
        total.addStyleName(ValoTheme.LABEL_COLORED);

        addComponent(total);
        setComponentAlignment(total, Alignment.MIDDLE_CENTER);
        setSizeFull();
    }

    public void update() {
        try {
            double sum = keenQueryClient.sum("gifts", "value", new AbsoluteTimeframe("2016-01-01", "2020-12-31"));
            total.setValue("$" + sum);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
