package com.example.backend.model;

import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.*;
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
        return feature.name();
    }

    public enum FeatureType {
        ELEVATOR ("Elevator"),
        STORAGE_ROOM("Storage room"),
        PASSIVE_VENTILATION_SYSTEM("Passive ventilation system"),
        PARKING("Parking"),
        BALCONY("Balcony");

        private final String displayName;

        FeatureType(String displayName) {
            this.displayName = displayName;
        }

        @JsonValue
        public String getDisplayName() {
            return displayName;
        }
    }
}
