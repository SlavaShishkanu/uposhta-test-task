package com.opinta.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.opinta.dto.ParcelDto;
import com.opinta.entity.Parcel;

@Mapper(componentModel = "spring", uses = ParcelItemMapper.class)
public interface ParcelMapper extends BaseMapper<ParcelDto, Parcel> {
    @Override
    @Mapping(source = "shipmentId", target = "shipment.id")
    Parcel toEntity(ParcelDto dto);

    @Override
    @Mapping(source = "shipment.id", target = "shipmentId")
    ParcelDto toDto(Parcel entity);
}
