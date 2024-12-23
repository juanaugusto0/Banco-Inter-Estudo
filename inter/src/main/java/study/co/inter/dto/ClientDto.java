package study.co.inter.dto;

import lombok.Getter;
import lombok.Setter;
import study.co.inter.enums.MembershipTier;

@Getter
@Setter
public class ClientDto {
    private String name;
    private MembershipTier membershipTier;
    private String email;
    private String cpf;

    public ClientDto(String name, MembershipTier membershipTier, String email, String cpf) {
        this.name = name;
        this.membershipTier = membershipTier;
        this.email = email;
        this.cpf = cpf;
    }
    
}
