package study.co.inter.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import study.co.inter.enums.TransactionType;

@Getter
@Setter
@Entity
public class Transaction {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private LocalDateTime timestamp;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    private TransactionType type;

    private double amount;
    
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", timestamp=" + timestamp +
                ", client=" + client.getName() +
                ", type=" + type +
                ", amount=" + amount +
                '}';
    }
}
