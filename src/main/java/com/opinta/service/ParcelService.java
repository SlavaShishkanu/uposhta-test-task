package com.opinta.service;

import java.math.BigDecimal;
import java.util.List;
import com.opinta.dto.ParcelDto;
import com.opinta.entity.Parcel;
import com.opinta.entity.Shipment;

public interface ParcelService {

    List<Parcel> getEntityByShipment(Shipment shipment);

    List<Parcel> getAllEntities();

    Parcel getEntityById(long id);

    Parcel saveEntity(Parcel parcel);

    List<ParcelDto> getAll();

    ParcelDto getById(long id);

    ParcelDto save(ParcelDto parcelDto);

    ParcelDto update(long id, ParcelDto parcelDto);

    boolean delete(long id);

    BigDecimal calculatePrice(Parcel parcel);

    void delete(Parcel parcel);

}
