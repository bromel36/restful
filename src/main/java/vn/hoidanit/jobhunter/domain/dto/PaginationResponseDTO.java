package vn.hoidanit.jobhunter.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaginationResponseDTO {
    Meta meta;
    Object result;
}
