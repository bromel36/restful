package vn.bromel.jobhunter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import vn.bromel.jobhunter.domain.Permission;

import java.util.List;

public interface PermissionRepository extends JpaRepository<Permission, Long>, JpaSpecificationExecutor<Permission> {
    boolean existsPermissionByApiPathAndAndModuleAndMethod(String apiPath, String module, String method);

    List<Permission> findByIdIn(List<Long> permissionIds);

    boolean existsPermissionByApiPathAndMethod(String apiPath, String method);
}
