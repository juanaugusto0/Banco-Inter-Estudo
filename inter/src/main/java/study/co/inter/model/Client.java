package study.co.inter.model;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import study.co.inter.enums.MembershipTier;

@Entity
@Getter
@Setter
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NonNull
    private String email;

    @NonNull
    private MembershipTier membershipTier;

    @NonNull
    private String name;

    @OneToMany(mappedBy = "client")
    private Set<Transaction> transactions;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal balance;

    @NonNull
    private Long cpf;

    public Client() {
        transactions = new HashSet<>();
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", membershipTier=" + membershipTier +
                ", name='" + name + '\'' +
                ", transactions=" + transactions.toString() +
                ", balance=" + balance +
                ", cpf=" + cpf +
                '}';
    }

}
