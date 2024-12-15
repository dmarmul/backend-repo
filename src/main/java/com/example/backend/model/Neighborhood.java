package com.example.backend.model;

import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Getter
@Setter
@Table(name = "neighborhoods")
public class Neighborhood implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    @Enumerated(EnumType.STRING)
    private NeighborhoodName neighborhoodName;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private String topAttractions;
    @Column(nullable = false)
    private String photoLink;
    @OneToMany(mappedBy = "neighborhood", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TransportDetails> transportDetails;
    @OneToMany(mappedBy = "neighborhood", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ParkingPlace> parkingPlace;
    @OneToMany(mappedBy = "neighborhood", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL, orphanRemoval = true)
    private List<KeyDetails> keyDetails;

    @Override
    public String getAuthority() {
        return neighborhoodName.getDisplayName();
    }

    public enum NeighborhoodName {
        NIEUWMARKET("Niewmarket"),
        MUSEUMPLEIN("Museumplein"),
        DE_WALLEN("De Wallen"),
        JORDAAN("Jordaan"),
        SPUI("Spui"),
        SPIEGELBUURT("Spiegelbuurt"),
        DE_PLANTAGE("De Plantage"),
        DE_PIJP("De Pijp"),
        LEIDSEPLEIN("Leidseplein"),
        IJ_WATERFRONT("IJ Waterfront"),
        REMBRANDTPLEIN("Rembrandtplein"),
        OUD_WEST("Oud-West"),
        OOST("Oost"),
        SPAARNDAMMERBUURT("Spaarndammerbuurt"),
        RIVIERENBUURT("Rivierenbuurt"),
        NOORD("Noord"),
        WESTPOORT("Westpoort");

        private final String displayName;

        NeighborhoodName(String displayName) {
            this.displayName = displayName;
        }

        @JsonValue
        public String getDisplayName() {
            return displayName;
        }

        public static NeighborhoodName fromValue(String value) {
            for (NeighborhoodName name : values()) {
                if (name.displayName.equalsIgnoreCase(value)) {
                    return name;
                }
            }
            throw new IllegalArgumentException("Unknown neighborhood name: " + value);
        }
    }

    @Entity
    @Getter
    @Setter
    @Table(name = "key_details")
    public static class KeyDetails {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        @Column(nullable = false)
        private String name;
        @Column(nullable = false)
        private String description;
        @ManyToOne(fetch = FetchType.LAZY, optional = false)
        @JoinColumn(name = "neighborhood_id", nullable = false)
        private Neighborhood neighborhood;
    }

    @Entity
    @Getter
    @Setter
    @Table(name = "transport_details")
    public static class TransportDetails implements GrantedAuthority {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        private TransportType transportType;
        @Column(nullable = false)
        private String transportNumbers;
        @ManyToOne(fetch = FetchType.LAZY, optional = false)
        @JoinColumn(name = "neighborhood_id", nullable = false)
        private Neighborhood neighborhood;

        @Override
        public String getAuthority() {
            return transportType.getDisplayName();
        }

        public enum TransportType {
            BUS("Bus"),
            TRAM("Tram");

            private final String displayName;

            TransportType(String displayName) {
                this.displayName = displayName;
            }

            @JsonValue
            public String getDisplayName() {
                return displayName;
            }
        }
    }

    @Entity
    @Getter
    @Setter
    @Table(name = "parking_place")
    public static class ParkingPlace {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        @Column(nullable = false)
        private String parkingPlace;
        @ManyToOne(fetch = FetchType.LAZY, optional = false)
        @JoinColumn(name = "neighborhood_id", nullable = false)
        private Neighborhood neighborhood;
    }
}
