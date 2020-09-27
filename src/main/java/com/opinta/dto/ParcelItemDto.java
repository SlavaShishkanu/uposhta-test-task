package com.opinta.dto;

import java.math.BigDecimal;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class ParcelItemDto {

    private long id;
    private String name;
    private int quantity;
    private float weight;
    private BigDecimal price;
    private long parcelId;
}
