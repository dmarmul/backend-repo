package com.example.backend.specification;

import com.example.backend.model.Feature;
import com.example.backend.model.House;
import jakarta.persistence.criteria.Join;
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
            Join<Object, Feature> featureJoin = root.join("features");
            return featureJoin.get("feature").in(featureTypesEnum);
        };
    }
}
