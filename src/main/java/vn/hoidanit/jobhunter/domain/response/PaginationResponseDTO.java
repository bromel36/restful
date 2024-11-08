package vn.hoidanit.jobhunter.domain.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaginationResponseDTO {
    Meta meta;
    Object result;

    @Getter
    @Setter
    public static class Meta{
        private int page;
        private int pageSize;
        private long total;
        private int totalOfCurrentPage;
        private int pages;
    }
}
