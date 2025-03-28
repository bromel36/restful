package vn.bromel.jobhunter.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.bromel.jobhunter.domain.Permission;
import vn.bromel.jobhunter.domain.Role;
import vn.bromel.jobhunter.domain.response.PaginationResponseDTO;
import vn.bromel.jobhunter.repository.RoleRepository;
import vn.bromel.jobhunter.util.error.IdInvalidException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleService {

    private final RoleRepository roleRepository;
    private final PermissionService permissionService;



    public RoleService(RoleRepository roleRepository, PermissionService permissionService) {
        this.roleRepository = roleRepository;
        this.permissionService = permissionService;
    }
    public Role handleCreateRole(Role role) {
        if(isExistsName(role.getName())){
            throw new IdInvalidException("Name already exists");
        }

        if (role.getPermissions()!= null && !role.getPermissions().isEmpty()) {
            List<Permission> permissions = role.getPermissions();

            List<Long> permissionIds = permissions.stream().map(Permission::getId).collect(Collectors.toList());

            List<Permission> permissionDB = this.permissionService.handleFetchPermissionByIds(permissionIds);

            role.setPermissions(permissionDB);
        }

        return roleRepository.save(role);

    }


    public Role handleUpdateRole(Role role) {
        Role roleDB = this.roleRepository.findById(role.getId())
                .orElseThrow(() -> new IdInvalidException("Role id not found"));

        if(!roleDB.getName().equals(role.getName()) && isExistsName(role.getName())){
            throw new IdInvalidException("Role Name already exists");
        }

        if (role.getPermissions()!= null && !role.getPermissions().isEmpty()) {
            List<Permission> permissions = role.getPermissions();

            List<Long> permissionIds = permissions.stream().map(Permission::getId).collect(Collectors.toList());

            List<Permission> permissionDB = this.permissionService.handleFetchPermissionByIds(permissionIds);

            roleDB.setPermissions(permissionDB);
        }

        roleDB.setName(role.getName());
        roleDB.setDescription(role.getDescription());
        roleDB.setActive(role.getActive());

        return roleRepository.save(roleDB);

    }


    public boolean isExistsName(String name) {
        return this.roleRepository.existsByName(name);
    }

    public Role handleFetchRoleById(Long id){
        Role role = this.roleRepository.findById(id)
                .orElseThrow(()->  new IdInvalidException("Role not found with id = "+ id));
        return role;
    }

    public PaginationResponseDTO handleGetAllPermission(Specification<Role> spec, Pageable pageable) {
        Page<Role> rolePage = roleRepository.findAll(spec, pageable);

        PaginationResponseDTO paginationResponseDTO = new PaginationResponseDTO();
        PaginationResponseDTO.Meta meta = new PaginationResponseDTO.Meta();

        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());

        meta.setPages(rolePage.getTotalPages());
        meta.setTotal(rolePage.getTotalElements());
        meta.setTotalOfCurrentPage(rolePage.getNumberOfElements());

        List<Role> result = rolePage.getContent();

        paginationResponseDTO.setResult(result);
        paginationResponseDTO.setMeta(meta);

        return paginationResponseDTO;

    }

    public void handleDeleteRole(Long id) {
        if(this.roleRepository.existsById(id)){
            this.roleRepository.deleteById(id);
            return;
        }
        throw new IdInvalidException("Role not found with id = "+ id);
    }
}
