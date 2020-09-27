package com.opinta.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import com.opinta.dto.ParcelItemDto;
import com.opinta.entity.ParcelItem;

@Mapper(componentModel = "spring")
public interface ParcelItemMapper extends BaseMapper<ParcelItemDto, ParcelItem> {

    @Override
    @Mappings({@Mapping(source = "parcel.id", target = "parcelId")})
    ParcelItemDto toDto(ParcelItem parcelItem);

    @Override
    @Mappings({@Mapping(source = "parcelId", target = "parcel.id")})
    ParcelItem toEntity(ParcelItemDto parcelItemDto);

}
