package com.opinta.dao;

import java.util.List;
import com.opinta.entity.Parcel;
import com.opinta.entity.ParcelItem;

public interface ParcelItemDao {

    List<ParcelItem> getAll();

    ParcelItem getById(long id);

    ParcelItem save(ParcelItem parcelItem);

    void update(ParcelItem parcelItem);

    void delete(ParcelItem parcelItem);

    List<ParcelItem> getByParcel(Parcel parcel);

}
