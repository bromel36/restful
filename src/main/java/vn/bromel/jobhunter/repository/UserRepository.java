package vn.bromel.jobhunter.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import vn.bromel.jobhunter.domain.User;

/**
 * UserRepository
 */
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    User findByEmail(String email);
    Boolean existsByEmail(String email);

    User findByRefreshTokenAndEmail(String token, String email);
}