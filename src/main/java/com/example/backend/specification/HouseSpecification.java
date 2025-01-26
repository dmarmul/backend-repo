package com.example.backend.specification;

import com.example.backend.model.Feature;
import com.example.backend.model.House;
import com.example.backend.model.Neighborhood;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.data.jpa.domain.Specification;

public class HouseSpecification {
    public static Specification<House> houseTypeEquals(String houseType) {
        House.HouseType houseTypeEnum = House.HouseType.fromValue(houseType);

        return (root, query, criteriaBuilder)
                -> criteriaBuilder.equal(root.get("houseType"), houseTypeEnum);
    }

    public static Specification<House> priceLessThan(BigDecimal price) {
        return (root, query, criteriaBuilder)
                -> criteriaBuilder.lessThanOrEqualTo(root.get("price"), price);
    }

    public static Specification<House> priceGreaterThan(BigDecimal price) {
        return (root, query, criteriaBuilder)
                -> criteriaBuilder.greaterThan(root.get("price"), price);
    }

    public static Specification<House> roomsEquals(Integer rooms) {
        return (root, query, criteriaBuilder)
                -> criteriaBuilder.equal(root.get("rooms"), rooms);
    }

    public static Specification<House> bedroomsEquals(Integer bedrooms) {
        return (root, query, criteriaBuilder)
                -> criteriaBuilder.equal(root.get("bedrooms"), bedrooms);
    }

    public static Specification<House> featuresIn(Set<String> featureTypes) {
        Set<Feature.FeatureType> featureTypesEnum = featureTypes.stream()
                .map(Feature.FeatureType::fromValue)
                .collect(Collectors.toSet());

        return (root, query, criteriaBuilder) -> {
            Subquery<Long> subquery = query.subquery(Long.class);
            Root<House> subRoot = subquery.from(House.class);

            Join<House, Feature> subFeatureJoin = subRoot.join("features");
            subquery.select(subRoot.get("id"))
                    .where(subFeatureJoin.get("feature").in(featureTypesEnum))
                    .groupBy(subRoot.get("id"))
                    .having(criteriaBuilder.equal(
                            criteriaBuilder.countDistinct(subFeatureJoin.get("feature")),
                            featureTypesEnum.size()
                    ));
            return criteriaBuilder.in(root.get("id")).value(subquery);
        };
    }

    public static Specification<House> isNotRentHouse() {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("isRentHouse"), false);
    }

    public static Specification<House> isRentHouse() {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("isRentHouse"), true);
    }

    public static Specification<House> neighborhoodsIn(Set<String> neighborhoods) {
        Set<Neighborhood.NeighborhoodName> neighborhoodNameEnums = neighborhoods.stream()
                .map(Neighborhood.NeighborhoodName::fromValue)
                .collect(Collectors.toSet());

        return (root, query, criteriaBuilder) -> {
            Join<House, Neighborhood> neighborhoodJoin = root.join("neighborhood");
            return neighborhoodJoin.get("neighborhoodName").in(neighborhoodNameEnums);
        };
    }
}
