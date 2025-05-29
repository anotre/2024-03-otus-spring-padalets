package ru.otus.realEstateClassifieds.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "property_objects")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PropertyObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", nullable = false, unique = true)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Address address;

    @Column(name = "total_area", nullable = false)
    private float totalArea;

    @Column(name = "kitchen_area", nullable = false)
    private float kitchenArea;

    @Column(name = "living_area", nullable = false)
    private float livingArea;

    @Column(name = "residential_floor_area")
    private float residentialFloorArea;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "construction_method_id", nullable = false)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private ConstructionMethod constructionMethod;

    @Column(name = "room_number", nullable = false)
    private int roomNumber;

    @Column(name = "bathroom_number", nullable = false)
    private int bathroomNumber;

    @Column(name = "ceiling_height", nullable = false)
    private float ceilingHeight;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "balcony_type_id", nullable = false)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private BalconyType balconyType;

    @Column(name = "floor_number", nullable = false)
    private int floorNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "furnished_type_id", nullable = false)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private FurnishedType furnishedType;
}
