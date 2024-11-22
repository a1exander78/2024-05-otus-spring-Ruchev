package ru.otus.project.model;

import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Builder
@Getter
@EqualsAndHashCode(exclude = "authorities")
@ToString(exclude = "authorities")
@AllArgsConstructor
@NoArgsConstructor
@NamedEntityGraph(name = "user-authority-entity-graph",
        attributeNodes = @NamedAttributeNode("authorities"))
@Table(name = "users")
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "login", nullable = false)
    private String login;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "name")
    private String userName;

    @NotNull
    @AttributeOverrides({
            @AttributeOverride(name = "district",
                    column = @Column(name = "district")),
            @AttributeOverride(name = "street",
                    column = @Column(name = "street")),
            @AttributeOverride(name = "streetNumber",
                    column = @Column(name = "street_number")),
            @AttributeOverride(name = "flatNumber",
                    column = @Column(name = "flat_number"))
    })
    @Embedded
    private Address address;

    @NotNull
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_authority",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "authority_id"))
    private List<Authority> authorities;
}
