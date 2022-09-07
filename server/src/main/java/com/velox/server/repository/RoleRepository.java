package com.velox.server.repository;

import java.util.Optional;

import com.velox.server.models.ERole;
import com.velox.server.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
  Optional<Role> findByName(ERole name);
}