package ru.stn.telegram.tests.states.localization;

import org.springframework.stereotype.Component;

import java.util.ResourceBundle;

@Component
public class Localizer {
    public String localize(Entry entry, ResourceBundle resourceBundle) {
        return resourceBundle.getString(entry.getName());
    }
}
