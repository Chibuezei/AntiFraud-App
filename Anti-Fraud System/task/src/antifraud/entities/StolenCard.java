package antifraud.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

@Entity
@Table(name = "stolen_card")
public class StolenCard {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


    @Column
    private String number;

    public StolenCard() {
    }

    public StolenCard(String cardNumber) {
        this.number = cardNumber;
    }

    public long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }
}
