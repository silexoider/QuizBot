package ru.stn.telegram.quiz.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
@NoArgsConstructor
public class Channel {
    @Id
    private long id;
    private String currencySingular;
    private String currencyDual;
    private String currencyPlural;

    public Channel(long id) {
        this.id = id;
    }
    public Channel(String currencySingular, String currencyDual, String currencyPlural) {
        this.currencySingular = currencySingular;
        this.currencyDual = currencyDual;
        this.currencyPlural = currencyPlural;
    }
}
