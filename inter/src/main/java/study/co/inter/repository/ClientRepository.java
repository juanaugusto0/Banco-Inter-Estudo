package study.co.inter.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import study.co.inter.model.Client;

public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findByCpf(Long cpf);
}
