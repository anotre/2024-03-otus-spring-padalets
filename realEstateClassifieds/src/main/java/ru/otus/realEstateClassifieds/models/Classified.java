package ru.otus.realEstateClassifieds.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.NamedSubgraph;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "classifieds")
@NamedEntityGraph(
        name = "classified-entity-graph",
        attributeNodes = {
                @NamedAttributeNode(value = "seller", subgraph = "seller-entity-graph"),
                @NamedAttributeNode(value = "object", subgraph = "property-object-entity-graph"),
        },
        subgraphs = {
                @NamedSubgraph(name = "seller-entity-graph", attributeNodes = {
                        @NamedAttributeNode(value = "email"),
                        @NamedAttributeNode(value = "phoneNumber"),
                }),
                @NamedSubgraph(name = "property-object-entity-graph", attributeNodes = {
                        @NamedAttributeNode(value = "address", subgraph = "address-entity-graph")
                }),
                @NamedSubgraph(name = "address-entity-graph", attributeNodes = {
                        @NamedAttributeNode(value = "street", subgraph = "street-entity-graph")
                }),
                @NamedSubgraph(name = "street-entity-graph", attributeNodes = {
                        @NamedAttributeNode(value = "settlement", subgraph = "region-entity-graph")
                }),
                @NamedSubgraph(name = "region-entity-graph", attributeNodes = {
                        @NamedAttributeNode(value = "region", subgraph = "country-entity-graph")
                }),
                @NamedSubgraph(name = "country-entity-graph", attributeNodes = {
                        @NamedAttributeNode(value = "country")
                }),
        }
)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Classified {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "title", nullable = false)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private User seller;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_object_id", nullable = false)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private PropertyObject object;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private long price;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
