package com.opinta.dao;

import java.util.List;
import com.opinta.entity.Parcel;
import com.opinta.entity.Shipment;

public interface ParcelDao {

    List<Parcel> getAll();

    Parcel getById(long id);

    Parcel save(Parcel parcel);

    void update(Parcel parcel);

    void delete(Parcel parcel);

    List<Parcel> getByShipment(Shipment shipment);

}
