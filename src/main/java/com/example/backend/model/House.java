package com.example.backend.model;

import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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
    @Column(nullable = false)
    private String street;
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
            name = "houses_features",
            joinColumns = @JoinColumn(name = "house_id"),
            inverseJoinColumns = @JoinColumn(name = "feature_id")
    )
    private Set<Feature> features = new HashSet<>();

    public enum HouseType {
        APARTMENT("Apartment"),
        HOUSE("House");

        private final String displayName;

        HouseType(String displayName) {
            this.displayName = displayName;
        }

        @JsonValue
        public String getDisplayName() {
            return displayName;
        }

        public static HouseType fromValue(String value) {
            for (HouseType type : values()) {
                if (type.displayName.equalsIgnoreCase(value)) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Unknown house type: " + value);
        }
    }
}
