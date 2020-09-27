package com.opinta.dto;

import java.math.BigDecimal;
import java.util.List;
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
public class ParcelDto {
    
    private long id;
    private float weight;
    private float length;
    private float width;
    private float height;
    private BigDecimal declaredPrice;
    private BigDecimal price;
    
    private long shipmentId;
    
//    TODO
    private List<ParcelItemDto> parcelItems;
    
}
