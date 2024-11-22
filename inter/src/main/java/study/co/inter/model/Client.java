package study.co.inter.model;

import java.util.HashSet;
import java.util.Set;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private String email;

    @NonNull
    private MembershipTier membershipTier;

    @NonNull
    private String name;

    @OneToMany(mappedBy = "client")
    private Set<Transaction> transactions;

    private double balance;

    @NonNull
    private Long cpf;

    public Client() {
        transactions = new HashSet<>();
    }

    
}


