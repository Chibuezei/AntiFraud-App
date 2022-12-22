package antifraud.entities;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

public class TransactionDTO {
    @Min(1)
    private long amount;
    @NotEmpty
    private String ip;
    @NotEmpty
    private String number;

    public TransactionDTO(long amount, String ip, String number) {
        this.amount = amount;
        this.ip = ip;
        this.number = number;
    }

    public TransactionDTO() {
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}