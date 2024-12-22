package com.library.userservice.Security.Repositories;


import com.library.userservice.Models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/*
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    @Query("SELECT r FROM Role r WHERE id = :clientId")
    List<Role> findRolesByClientId(@Param("clientId") String clientId);
}*/
