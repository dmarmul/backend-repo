package com.example.backend.model;

import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Getter
@Setter
@Table(name = "house_types")
public class HouseType implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    @Enumerated(EnumType.STRING)
    private HouseTypeName houseType;

    @Override
    public String getAuthority() {
        return houseType.name();
    }

    public enum HouseTypeName {
        ELEVATOR ("Elevator"),
        STORAGE_ROOM("Storage room"),
        PASSIVE_VENTILATION_SYSTEM("Passive ventilation system"),
        PARKING("Parking"),
        BALCONY("Balcony");

        private final String displayName;

        HouseTypeName(String displayName) {
            this.displayName = displayName;
        }

        @JsonValue
        public String getDisplayName() {
            return displayName;
        }
    }
}
