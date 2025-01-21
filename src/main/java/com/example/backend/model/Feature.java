package com.example.backend.model;

import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Getter
@Setter
@Table(name = "features")
public class Feature implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    @Enumerated(EnumType.STRING)
    private FeatureType feature;

    @Override
    public String getAuthority() {
        return feature.getDisplayName();
    }

    public enum FeatureType {
        ELEVATOR("Elevator"),
        STORAGE_ROOM("Storage room"),
        PASSIVE_VENTILATION_SYSTEM("Passive ventilation system"),
        PARKING("Parking"),
        BALCONY("Balcony"),
        BACK_GARDEN("Back garden"),
        FLAT_ROOF("Flat roof"),
        PARTLY_FURNISHED("Partly furnished"),
        DETACHED_BRICK_STORAGE("Detached brick storage"),
        PAID_PARKING("Paid parking"),
        CH_BOILER("CH boiler"),
        STORAGE_BOX("Storage box"),
        ROOF_INSULATION("Roof insulation"),
        MECHANICAL_VENTILATION("Mechanical ventilation"),
        GROUND_LEASE("Ground lease"),
        COMMUNAL_CENTRAL_HEATING("Communal central heating"),
        DISHWASHER("Dishwasher"),
        AIR_CONDITIONING("Air conditioning"),
        TV_VIA_CABLE("TV via cable"),
        FREE_PARKING("Free parking"),
        OUTDOOR_AWNING("Outdoor awning"),
        PUBLIC_PARKING("Public parking"),
        HIP_ROOF("Hip roof"),
        ATTACHED_BRICK_STORAGE("Attached brick storage"),
        GABLE_ROOF("Gable roof"),
        OPTICAL_FIBRE("Optical fibre"),
        DISTRICT_HEATING("District heating"),
        ROOF_TERRACE("Roof terrace"),
        GARAGE("Garage"),
        UNDERGROUND_PARKING("Underground parking"),
        FRENCH_BALCONY("French balcony"),
        PARKING_GARAGE("Parking garage");

        private final String displayName;

        FeatureType(String displayName) {
            this.displayName = displayName;
        }

        @JsonValue
        public String getDisplayName() {
            return displayName;
        }

        public static Feature.FeatureType fromValue(String value) {
            for (Feature.FeatureType type : values()) {
                if (type.displayName.equalsIgnoreCase(value)) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Unknown feature type: " + value);
        }
    }
}
