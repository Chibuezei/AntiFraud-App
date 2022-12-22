package antifraud.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "Ip")
public class Ip {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    @NotEmpty
    private String ip;

    public Ip() {
    }

    public Ip(String ip) {
        this.ip = ip;
    }

    public long getId() {
        return id;
    }

    public String getIp() {
        return ip;
    }

}
