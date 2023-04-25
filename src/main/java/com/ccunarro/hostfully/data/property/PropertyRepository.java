package com.ccunarro.hostfully.data.property;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PropertyRepository extends JpaRepository<Property, UUID> {

    @Query(" SELECT count(p) = 1 FROM Property p " +
            " WHERE p.id = :propertyId and p.userId = :userId")
    boolean existsPropertyForUser(UUID propertyId, UUID userId);
}
