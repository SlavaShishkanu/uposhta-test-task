package com.opinta.entity;

import java.math.BigDecimal;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ParcelItem {

    @Id
    @GeneratedValue
    private long id;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH,
            CascadeType.DETACH})
    @JoinColumn(name = "parcel_id")
    private Parcel parcel;

    private String name;
    private int quantity;
    private float weight;
    private BigDecimal price;

    public ParcelItem(String name, int quantity, float weight, BigDecimal price) {
        this.name = name;
        this.quantity = quantity;
        this.weight = weight;
        this.price = price;
    }

}
