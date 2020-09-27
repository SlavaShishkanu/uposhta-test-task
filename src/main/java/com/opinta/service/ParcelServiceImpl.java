package com.opinta.service;

import static org.apache.commons.beanutils.BeanUtils.copyProperties;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.opinta.dao.ParcelDao;
import com.opinta.dao.ShipmentDao;
import com.opinta.dao.TariffGridDao;
import com.opinta.dto.ParcelDto;
import com.opinta.entity.Address;
import com.opinta.entity.DeliveryType;
import com.opinta.entity.Parcel;
import com.opinta.entity.Shipment;
import com.opinta.entity.TariffGrid;
import com.opinta.entity.W2wVariation;
import com.opinta.mapper.ParcelMapper;
import com.opinta.util.AddressUtil;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional
public class ParcelServiceImpl implements ParcelService {

    private static final String CALCULATING_PRICE_FOR_PARCEL = "Calculating price for parcel {}";
    private ParcelDao parcelDao;
    private ParcelMapper parcelMapper;
    private ShipmentDao shipmentDao;
    private TariffGridDao tariffGridDao;
    private ParcelItemService parcelItemService;

    @Autowired
    public ParcelServiceImpl(ParcelDao parcelDao, ParcelMapper parcelMapper,
            ShipmentDao shipmentDao, TariffGridDao tariffGridDao,
            ParcelItemService parcelItemService) {
        this.parcelDao = parcelDao;
        this.parcelMapper = parcelMapper;
        this.shipmentDao = shipmentDao;
        this.tariffGridDao = tariffGridDao;
        this.parcelItemService = parcelItemService;

    }

    @Override
    public List<Parcel> getEntityByShipment(Shipment shipment) {
        return parcelDao.getByShipment(shipment);
    }

    @Override
    public List<Parcel> getAllEntities() {
        return parcelDao.getAll();

    }

    @Override
    public Parcel getEntityById(long id) {
        return parcelDao.getById(id);
    }

    @Override
    public Parcel saveEntity(Parcel parcel) {
        parcel.setPrice(calculatePrice(parcel)); // not working ??? why
        return parcelDao.save(parcel);
    }

    @Override
    public List<ParcelDto> getAll() {
        return parcelMapper.toDto(getAllEntities());
    }

    List<ParcelDto> getAllByShipmentId(long shipmentId) {
        Shipment shipment = shipmentDao.getById(shipmentId);
        if (shipment == null) {
            log.debug("Can't get parcels list by shipment. Shipment {} doesn't exist", shipmentId);
            return new ArrayList<>();
        }
        log.info("Getting all parcels by shipment {}", shipment);

        return parcelMapper.toDto(parcelDao.getByShipment(shipment));
    }

    @Override
    public ParcelDto getById(long id) {
        return parcelMapper.toDto(getEntityById(id));
    }

    @Override
    public ParcelDto save(ParcelDto parcelDto) {
        Parcel parcel = parcelMapper.toEntity(parcelDto);
        if (parcel.getParcelItems() != null) {
            parcel.getParcelItems().forEach(item -> item.setParcel(parcel));
        }

        parcel.setPrice(calculatePrice(parcel));
        return parcelMapper.toDto(parcelDao.save(parcel));
    }

    @Override
    public ParcelDto update(long id, ParcelDto parcelDto) {
        Parcel source = parcelMapper.toEntity(parcelDto);
        Parcel target = parcelDao.getById(id);

        try {
            copyProperties(target, source);
        } catch (Exception e) {
            log.error("Can't get properties from object to updatable object for shipment", e);
        }

        target.setId(id);
        target.setPrice(calculatePrice(target));
        if (target.getParcelItems() != null) {
            target.getParcelItems().forEach(item -> item.setParcel(target));
        }
        log.info("Updating parcel {}", target);
        parcelDao.update(target);
        return parcelMapper.toDto(target);

    }

    @Override
    public boolean delete(long id) {
        Parcel parcel = parcelDao.getById(id);
        if (parcel == null) {
            log.debug("Can't delete parcel. Parcel doesn't exist {}", id);
            return false;
        }
        parcel.setId(id);
        if (parcel.getParcelItems() != null) {
            parcel.getParcelItems().forEach(item -> parcelItemService.delete(item.getId()));
        }

        log.info("Deleting parcel {}", parcel);
        parcelDao.delete(parcel);
        return true;
    }

    @Override
    public void delete(Parcel parcel) {
        parcelDao.delete(parcel);
    }

    @Override
    public BigDecimal calculatePrice(Parcel parcel) {
        log.info(CALCULATING_PRICE_FOR_PARCEL, parcel);
        Shipment shipment = shipmentDao.getById(parcel.getShipment().getId());

        Address senderAdress = shipment.getSender().getAddress();
        Address recipientAdress = shipment.getRecipient().getAddress();
        DeliveryType deliveryType = shipment.getDeliveryType();

        log.info(CALCULATING_PRICE_FOR_PARCEL, parcel);

        W2wVariation w2wVariation = W2wVariation.COUNTRY;
        if (AddressUtil.isSameTown(senderAdress, recipientAdress)) {
            w2wVariation = W2wVariation.TOWN;
        } else if (AddressUtil.isSameRegion(senderAdress, recipientAdress)) {
            w2wVariation = W2wVariation.REGION;
        }

        TariffGrid tariffGrid = tariffGridDao.getLast(w2wVariation);
        if (parcel.getWeight() < tariffGrid.getWeight()
                && parcel.getLength() < tariffGrid.getLength()) {
            tariffGrid = tariffGridDao.getByDimension(parcel.getWeight(), parcel.getLength(),
                    w2wVariation);
        }

        log.info("TariffGrid for weight {} per length {} and type {}: {}", parcel.getWeight(),
                parcel.getLength(), w2wVariation, tariffGrid);

        if (tariffGrid == null) {
            return BigDecimal.ZERO;
        }

        float price = tariffGrid.getPrice() + getSurcharges(deliveryType);
        return new BigDecimal(Float.toString(price));
    }

    private float getSurcharges(DeliveryType deliveryType) {
        float surcharges = 0;
        if (deliveryType.equals(DeliveryType.D2W) || deliveryType.equals(DeliveryType.W2D)) {
            surcharges += 9;
        } else if (deliveryType.equals(DeliveryType.D2D)) {
            surcharges += 12;
        }
        return surcharges;
    }

}
