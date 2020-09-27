package com.opinta.entity;

import java.math.BigDecimal;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Shipment {
    @Id
    @GeneratedValue
    private long id;
    @ManyToOne
    @JoinColumn(name = "sender_id")
    private Client sender;
    @ManyToOne
    @JoinColumn(name = "recipient_id")
    private Client recipient;
    @OneToOne
    private BarcodeInnerNumber barcode;
    @Enumerated(EnumType.STRING)
    private DeliveryType deliveryType;

    private BigDecimal price;

    @OneToMany(mappedBy = "shipment", cascade = CascadeType.ALL)
    private List<Parcel> parcels;

    private BigDecimal postPay;
    private String description;

    public Shipment(Client sender, Client recipient, DeliveryType deliveryType, BigDecimal price,
            BigDecimal postPay) {
        this.sender = sender;
        this.recipient = recipient;
        this.deliveryType = deliveryType;
        this.price = price;
        this.postPay = postPay;
    }

    @Override
    public String toString() {
        return "Shipment [id=" + id + ", sender=" + sender + ", recipient=" + recipient
                + ", barcode=" + barcode + ", deliveryType=" + deliveryType + ", price=" + price
                + ", postPay=" + postPay + ", description=" + description + "]";
    }

}
