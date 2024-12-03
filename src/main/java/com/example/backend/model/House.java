package com.example.backend.model;

import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
@Setter
@ToString
@SQLDelete(sql = "UPDATE houses SET is_deleted = true WHERE id=?")
@SQLRestriction("is_deleted=false")
@Table(name = "houses")
public class House {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private HouseType houseType;
    @Column(nullable = false)
    private int rooms;
    @Column(nullable = false)
    private int bedrooms;
    @Column(nullable = false)
    private int houseSquare;
    @Column(nullable = false)
    private BigDecimal price;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private boolean isDeleted = false;
    @OneToMany(mappedBy = "house", cascade = CascadeType.ALL,
            orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<PhotoLink> photoLinks = new HashSet<>();
    @ManyToMany
    @JoinTable(
            name = "house_features",
            joinColumns = @JoinColumn(name = "house_id"),
            inverseJoinColumns = @JoinColumn(name = "features_id")
    )
    private Set<Feature> features = new HashSet<>();

    private enum HouseType {
        ELEVATOR ("Elevator"),
        STORAGE_ROOM("Storage room"),
        PASSIVE_VENTILATION_SYSTEM("Passive ventilation system"),
        PARKING("Parking"),
        BALCONY("Balcony");

        private final String displayName;

        HouseType(String displayName) {
            this.displayName = displayName;
        }

        @JsonValue
        public String getDisplayName() {
            return displayName;
        }
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
