package com.crud.adventuretravel.form;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import org.springframework.stereotype.Component;

@Component
public class ErrorNotification {

    public void showNotification(String text) {
        Notification notification = Notification.show(text);
        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        notification.setPosition(com.vaadin.flow.component.notification.Notification.Position.MIDDLE);
        notification.setDuration(3000);
    }
}