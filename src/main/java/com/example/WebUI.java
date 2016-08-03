package com.example;

import com.vaadin.annotations.Theme;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

@SpringUI
@Theme(ValoTheme.THEME_NAME)
public class WebUI extends UI {

    private VerticalLayout layout = new VerticalLayout();

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

        addGiftOption("Voucher", FontAwesome.TICKET, ValoTheme.BUTTON_FRIENDLY, 12);
        addGiftOption("Prepaid card", FontAwesome.CREDIT_CARD, ValoTheme.BUTTON_PRIMARY, 14);
        addGiftOption("Surprise", FontAwesome.GIFT, ValoTheme.BUTTON_DANGER, 14);
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
    }

}
