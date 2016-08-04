package com.example;

import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.communication.PushMode;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import io.keen.client.java.KeenClient;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

@SpringUI
@Theme(ValoTheme.THEME_NAME)
@Push(value = PushMode.MANUAL)
public class WebUI extends UI {

    private VerticalLayout layout = new VerticalLayout();

    @Autowired
    private Dashboard dashboard;

    @Autowired
    private KeenClient keenClient;

    @Override
    protected void init(VaadinRequest request) {
        layout.setMargin(true);
        layout.setSpacing(true);
        setContent(layout);

        Label title = new Label("Your order is ready!");
        title.addStyleName(ValoTheme.LABEL_H1);
        layout.addComponent(title);

        Label instructions = new Label("We want to thank you for your purchase by giving you a gift. Please select one option:");
        instructions.setWidth("270px");
        layout.addComponent(instructions);

        addGiftOption("Prepaid card", FontAwesome.CREDIT_CARD, ValoTheme.BUTTON_PRIMARY, 12);
        addGiftOption("Voucher", FontAwesome.TICKET, ValoTheme.BUTTON_FRIENDLY, 14);
        addGiftOption("Surprise", FontAwesome.GIFT, ValoTheme.BUTTON_DANGER, 16);

        Button viewDashboard = new Button("View dashboard");
        viewDashboard.addStyleName(ValoTheme.BUTTON_LINK);
        layout.addComponent(viewDashboard);

        viewDashboard.addClickListener(event -> showDashboard());
    }

    private void addGiftOption(String name, FontAwesome icon, String styleName, int value) {
        Button button = new Button("$" + value + " " + name, icon);
        button.setWidth("270px");
        button.addStyleName(ValoTheme.BUTTON_LARGE);
        button.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_TOP);
        button.addStyleName(styleName);
        layout.addComponent(button);

        button.addClickListener(event -> giftSelected(name, value));
    }

    private void giftSelected(String name, int value) {
        Notification.show("Thank you! Your gift will be sent soon!");

        Map<String, Object> event = new HashMap<>();
        event.put("name", name);
        event.put("value", value);
        keenClient.addEventAsync("gifts", event);
    }

    private void showDashboard() {
        setContent(dashboard);
        dashboard.update();
    }

}
