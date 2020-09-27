package com.opinta.service;

import static org.apache.commons.beanutils.BeanUtils.copyProperties;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.opinta.dao.ClientDao;
import com.opinta.dao.ShipmentDao;
import com.opinta.dto.ShipmentDto;
import com.opinta.entity.BarcodeInnerNumber;
import com.opinta.entity.Client;
import com.opinta.entity.Counterparty;
import com.opinta.entity.Parcel;
import com.opinta.entity.PostcodePool;
import com.opinta.entity.Shipment;
import com.opinta.mapper.ShipmentMapper;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional
public class ShipmentServiceImpl implements ShipmentService {
    private final ShipmentDao shipmentDao;
    private final ClientDao clientDao;
    private final ShipmentMapper shipmentMapper;
    private final BarcodeInnerNumberService barcodeInnerNumberService;
    private ParcelService parcelService;

    @Autowired
    public ShipmentServiceImpl(ShipmentDao shipmentDao, ClientDao clientDao,
            ShipmentMapper shipmentMapper, BarcodeInnerNumberService barcodeInnerNumberService,
            ParcelService parcelService) {
        this.shipmentDao = shipmentDao;
        this.clientDao = clientDao;
        this.shipmentMapper = shipmentMapper;
        this.barcodeInnerNumberService = barcodeInnerNumberService;
        this.parcelService = parcelService;
    }

    @Override
    public List<Shipment> getAllEntities() {
        log.info("Getting all shipments");
        return shipmentDao.getAll();
    }

    @Override
    public Shipment getEntityById(long id) {
        log.info("Getting postcodePool by id {}", id);
        return shipmentDao.getById(id);
    }

    @Override
    public Shipment saveEntity(Shipment shipment) {
        log.info("Saving shipment {}", shipment);
        setParcelsAndParcelItems(shipment);
        Shipment savedShipment = shipmentDao.save(shipment);
        savedShipment.setPrice(calculatePrice(savedShipment));
        return savedShipment;
    }

    @Override
    public List<ShipmentDto> getAll() {
        return shipmentMapper.toDto(getAllEntities());
    }

    @Override
    public List<ShipmentDto> getAllByClientId(long clientId) {
        Client client = clientDao.getById(clientId);
        if (client == null) {
            log.debug("Can't get shipment list by client. Client {} doesn't exist", clientId);
            return new ArrayList<>();
        }
        log.info("Getting all shipments by client {}", client);
        return shipmentMapper.toDto(shipmentDao.getAllByClient(client));
    }

    @Override
    public ShipmentDto getById(long id) {
        return shipmentMapper.toDto(getEntityById(id));
    }

    @Override
    public ShipmentDto save(ShipmentDto shipmentDto) {

        Client existingClient = clientDao.getById(shipmentDto.getSenderId());
        Counterparty counterparty = existingClient.getCounterparty();
        PostcodePool postcodePool = counterparty.getPostcodePool();
        BarcodeInnerNumber newBarcode =
                barcodeInnerNumberService.generateBarcodeInnerNumber(postcodePool);
        postcodePool.getBarcodeInnerNumbers().add(newBarcode);

        Shipment shipment = shipmentMapper.toEntity(shipmentDto);
        shipment.setBarcode(newBarcode);
        log.info("Saving shipment with assigned barcode", shipmentMapper.toDto(shipment));

        shipment.setSender(clientDao.getById(shipment.getSender().getId()));
        shipment.setRecipient(clientDao.getById(shipment.getRecipient().getId()));

        setParcelsAndParcelItems(shipment);

        Shipment savedShipment = shipmentDao.save(shipment);
        savedShipment.setPrice(calculatePrice(savedShipment));

        return shipmentMapper.toDto(savedShipment);
    }

    private void setParcelsAndParcelItems(Shipment shipment) {
        if (shipment.getParcels() != null) {
            shipment.getParcels().forEach(parcel -> {
                parcel.setShipment(shipment);
                if (parcel.getParcelItems() != null) {
                    parcel.getParcelItems().forEach(item -> item.setParcel(parcel));
                }
            });
        }
    }

    @Override
    public ShipmentDto update(long id, ShipmentDto shipmentDto) {
        Shipment source = shipmentMapper.toEntity(shipmentDto);
        Shipment target = shipmentDao.getById(id);

        if (target == null) {
            log.debug("Can't update shipment. Shipment doesn't exist {}", id);
            return null;
        }

        try {
            copyProperties(target, source);
        } catch (Exception e) {
            log.error("Can't get properties from object to updatable object for shipment", e);
        }
        target.setId(id);
        setParcelsAndParcelItems(target);
        shipmentDao.update(target);

        target.setPrice(calculatePrice(target));

        return shipmentMapper.toDto(target);
    }

    @Override
    public boolean delete(long id) {
        Shipment shipment = shipmentDao.getById(id);
        if (shipment == null) {
            log.debug("Can't delete shipment. Shipment doesn't exist {}", id);
            return false;
        }
        shipment.setId(id);
        if (shipment.getParcels() != null) {
            shipment.getParcels().forEach(parcel -> parcelService.delete(parcel.getId()));
        }
        log.info("Deleting shipment {}", shipment);
        shipmentDao.delete(shipment);
        return true;
    }

    private BigDecimal calculatePrice(Shipment savedShipment) {
        if (savedShipment.getParcels() != null) {
            savedShipment.getParcels()
                    .forEach(parcel -> parcel.setPrice(parcelService.calculatePrice(parcel)

                    ));
            return savedShipment.getParcels().stream().map(Parcel::getPrice).reduce(BigDecimal.ZERO,
                    BigDecimal::add);
        } else {
            return BigDecimal.ZERO;
        }
    }

}
