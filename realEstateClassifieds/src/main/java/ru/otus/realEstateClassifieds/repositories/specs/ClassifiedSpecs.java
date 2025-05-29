package ru.otus.realEstateClassifieds.repositories.specs;

import org.springframework.data.jpa.domain.Specification;
import ru.otus.realEstateClassifieds.models.Classified;

import java.util.List;
import java.util.Objects;

public class ClassifiedSpecs {
    public static Specification<Classified> settlementExactly(long settlementId) {
        if (settlementId == 0) {
            return null;
        }

        return ((root, query, criteriaBuilder) -> {
            var settlement = root.get("object")
                    .get("address")
                    .get("street")
                    .get("settlement")
                    .get("id");
            return criteriaBuilder.equal(
                    settlement,
                    settlementId
            );
        });
    }

    public static Specification<Classified> regionExactly(long regionId) {
        if (regionId == 0) {
            return null;
        }

        return ((root, query, criteriaBuilder) -> {
            var region = root.get("object")
                    .get("address")
                    .get("street")
                    .get("settlement")
                    .get("region")
                    .get("id");
            return criteriaBuilder.equal(
                    region,
                    regionId
            );
        });
    }

    public static Specification<Classified> countryExactly(long countryId) {
        if (countryId == 0) {
            return null;
        }

        return ((root, query, criteriaBuilder) -> {
            var country = root.get("object")
                    .get("address")
                    .get("street")
                    .get("settlement")
                    .get("region")
                    .get("country")
                    .get("id");
            return criteriaBuilder.equal(
                    country,
                    countryId
            );
        });
    }

    public static Specification<Classified> priceBetween(long from, long to) {
        if (to == 0) {
            return null;
        }

        return ((root, query, criteriaBuilder) -> {
            var price = root.get("object")
                    .get("price");
            return criteriaBuilder.between(price.as(Long.class), from, to);
        });
    }

    public static Specification<Classified> totalAreaBetween(float from, float to) {
        if (to == 0) {
            return null;
        }

        return ((root, query, criteriaBuilder) -> {
            var totalArea = root.get("object")
                    .get("totalArea");
            return criteriaBuilder.between(totalArea.as(Float.class), from, to);
        });
    }

    public static Specification<Classified> kitchenAreaBetween(float from, float to) {
        if (to == 0) {
            return null;
        }

        return ((root, query, criteriaBuilder) -> {
            var kitchenArea = root.get("object")
                    .get("kitchenArea");
            return criteriaBuilder.between(kitchenArea.as(Float.class), from, to);
        });
    }

    public static Specification<Classified> livingAreaBetween(float from, float to) {
        if (to == 0) {
            return null;
        }

        return ((root, query, criteriaBuilder) -> {
            var livingArea = root.get("object")
                    .get("livingArea");
            return criteriaBuilder.between(livingArea.as(Float.class), from, to);
        });
    }

    public static Specification<Classified> residentialFloorAreaBetween(float from, float to) {
        if (to == 0) {
            return null;
        }

        return ((root, query, criteriaBuilder) -> {
            var residentialFloorArea = root.get("object")
                    .get("residentialFloorArea");
            return criteriaBuilder.between(residentialFloorArea.as(Float.class), from, to);
        });
    }

    public static Specification<Classified> ceilingHeightBetween(float from, float to) {
        if (to == 0) {
            return null;
        }

        return ((root, query, criteriaBuilder) -> {
            var ceilingHeight = root.get("object")
                    .get("ceilingHeight");
            return criteriaBuilder.between(ceilingHeight.as(Float.class), from, to);
        });
    }

    public static Specification<Classified> floorNumberBetween(int from, int to) {
        if (to == 0) {
            return null;
        }

        return ((root, query, criteriaBuilder) -> {
            var floorNumber = root.get("object")
                    .get("floorNumber");
            return criteriaBuilder.between(floorNumber.as(Integer.class), from, to);
        });
    }

    public static Specification<Classified> constructionMethodAnyOf(List<Long> constructionMethodIds) {
        if (Objects.isNull(constructionMethodIds) || constructionMethodIds.isEmpty()) {
            return null;
        }

        return ((root, query, criteriaBuilder) -> root.get("object")
                .get("constructionMethod")
                .get("id")
                .in(constructionMethodIds)
        );
    }

    public static Specification<Classified> roomNumberAnyOf(List<Integer> roomNumbers) {
        if (Objects.isNull(roomNumbers) || roomNumbers.isEmpty()) {
            return null;
        }

        return ((root, query, criteriaBuilder) -> root
                .get("object")
                .get("roomNumber")
                .in(roomNumbers)
        );
    }

    public static Specification<Classified> balconyTypeAnyOf(List<Long> balconyTypes) {
        if (Objects.isNull(balconyTypes) || balconyTypes.isEmpty()) {
            return null;
        }

        return ((root, query, criteriaBuilder) -> root
                .get("object")
                .get("balconyType")
                .get("id")
                .in(balconyTypes)
        );
    }
}