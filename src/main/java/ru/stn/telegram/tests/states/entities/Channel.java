package ru.stn.telegram.tests.states.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
@NoArgsConstructor
public class Channel {
    @Id
    private long chatId;
    private String currencySingular;
    private String currencyDual;
    private String currencyPlural;

    public Channel(long chatId) {
        this.chatId = chatId;
    }
    public Channel(String currencySingular, String currencyDual, String currencyPlural) {
        this.currencySingular = currencySingular;
        this.currencyDual = currencyDual;
        this.currencyPlural = currencyPlural;
    }
}
