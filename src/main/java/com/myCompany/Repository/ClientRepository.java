package com.myCompany.Repository;

import com.myCompany.models.entities.Client;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class ClientRepository implements PanacheRepository<Client> {

    public Optional<Client> findByEmail(String email) {
        return find("correoElectronico = ?1 and deletedAt is null", email)
                .firstResultOptional();
    }

    public boolean canUpdateEmail(UUID uuid, String email) {
        return !find("correoElectronico = ?1 and id != ?2", email, uuid)
                .firstResultOptional().isPresent();
    }

    public Optional<Client> findById(UUID uuid) {
        return find("id = ?1 and deletedAt is null", uuid)
                .firstResultOptional();
    }

    public List<Client> listAllClients() {
        return list("deletedAt is null order by updatedAt desc");
    }

    public void save(Client client) {
        persistAndFlush(client);
    }

}