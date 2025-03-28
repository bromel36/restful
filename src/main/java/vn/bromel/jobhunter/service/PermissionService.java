package vn.bromel.jobhunter.service;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.bromel.jobhunter.domain.Permission;
import vn.bromel.jobhunter.domain.Role;
import vn.bromel.jobhunter.domain.response.PaginationResponseDTO;
import vn.bromel.jobhunter.repository.PermissionRepository;
import vn.bromel.jobhunter.util.error.IdInvalidException;

import java.util.List;

@Service
public class PermissionService {

    private PermissionRepository permissionRepository;

    public PermissionService(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    public Permission handldeCreatePermission(Permission permission) {
        if(isExistPermission(permission)){
            throw new IdInvalidException("Permission already exists");
        }
        return permissionRepository.save(permission);
    }

    public Permission handldeUpdatePermission(@Valid Permission permission) {
        Permission currentPermission = permissionRepository.findById(permission.getId())
                .orElseThrow(() -> new IdInvalidException("Permission not found with id= " + permission.getId()));

        if(isExistPermission(permission)){
            throw new IdInvalidException("Permission already exists");
        }

        currentPermission.setName(permission.getName());
        currentPermission.setMethod(permission.getMethod());
        currentPermission.setApiPath(permission.getApiPath());
        currentPermission.setModule(permission.getModule());


        return permissionRepository.save(currentPermission);

    }


    public boolean isExistPermission(Permission permission) {
        return this.permissionRepository.existsPermissionByApiPathAndAndModuleAndMethod(permission.getApiPath(), permission.getModule(), permission.getMethod());
    }


    public PaginationResponseDTO handleGetAllPermission(Specification<Permission> spec, Pageable pageable) {
        Page<Permission> permissionPage = permissionRepository.findAll(spec, pageable);

        PaginationResponseDTO paginationResponseDTO = new PaginationResponseDTO();
        PaginationResponseDTO.Meta meta = new PaginationResponseDTO.Meta();

        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());

        meta.setPages(permissionPage.getTotalPages());
        meta.setTotal(permissionPage.getTotalElements());
        meta.setTotalOfCurrentPage(permissionPage.getNumberOfElements());

        List<Permission> result = permissionPage.getContent();

        paginationResponseDTO.setResult(result);
        paginationResponseDTO.setMeta(meta);

        return paginationResponseDTO;
    }


    public List<Permission> handleFetchPermissionByIds(List<Long> permissionIds) {
        return this.permissionRepository.findByIdIn(permissionIds);
    }

    public void handleDeletePermission(Long id) {
        Permission permissionDB = permissionRepository.findById(id).orElseThrow(
                () -> new IdInvalidException("Permission not found with id= " + id)
        );

        List<Role> roles = permissionDB.getRoles();

        if(roles!= null){
            roles.forEach(role -> {
                role.getPermissions().remove(permissionDB);
            });
        }

        permissionRepository.delete(permissionDB);
    }

    public boolean isExistByApiPathAndMethod(String apiPath, String method){
        return this.permissionRepository.existsPermissionByApiPathAndMethod(apiPath,method);
    }
}
