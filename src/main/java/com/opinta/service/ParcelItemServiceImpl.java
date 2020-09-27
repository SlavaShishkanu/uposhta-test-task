package com.opinta.service;

import static org.apache.commons.beanutils.BeanUtils.copyProperties;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.opinta.dao.ParcelItemDao;
import com.opinta.dto.ParcelItemDto;
import com.opinta.entity.ParcelItem;
import com.opinta.mapper.ParcelItemMapper;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional
public class ParcelItemServiceImpl implements ParcelItemService {
    
    private ParcelItemDao parcelItemDao;
    private ParcelItemMapper parcelItemMapper;

    @Autowired
    public ParcelItemServiceImpl(ParcelItemDao parcelItemDao, ParcelItemMapper parcelItemMapper) {
        this.parcelItemDao = parcelItemDao;
        this.parcelItemMapper = parcelItemMapper;
    }
    
    @Override
    public List<ParcelItem> getAllEntities() {
        return parcelItemDao.getAll();

    }

    @Override
    public ParcelItem getEntityById(long id) {
        return parcelItemDao.getById(id);
    }

    @Override
    public ParcelItem saveEntity(ParcelItem parcelItem) {
        return parcelItemDao.save(parcelItem);
    }

    @Override
    public List<ParcelItemDto> getAll() {
        return parcelItemMapper.toDto(getAllEntities());
    }

    @Override
    public ParcelItemDto getById(long id) {
        return parcelItemMapper.toDto(getEntityById(id));
    }

    @Override
    public ParcelItemDto save(ParcelItemDto parcelItemDto) {
        ParcelItem parcelItem = parcelItemMapper.toEntity(parcelItemDto);
        return parcelItemMapper.toDto(parcelItemDao.save(parcelItem));
    }

    @Override
    public ParcelItemDto update(long id, ParcelItemDto parcelItemDto) {
        ParcelItem source = parcelItemMapper.toEntity(parcelItemDto);
        ParcelItem target = parcelItemDao.getById(id);

        try {
            copyProperties(target, source);
        } catch (Exception e) {
            log.error("Can't get properties from object to updatable object for shipment", e);
        }
        
        target.setId(id);
        
        log.info("Updating parcel {}", target);
        parcelItemDao.update(target);
        return parcelItemMapper.toDto(target);

    }

    @Override
    public boolean delete(long id) {
        ParcelItem parcelItem = parcelItemDao.getById(id);
        if (parcelItem == null) {
            log.debug("Can't delete parcel. Parcel doesn't exist {}", id);
            return false;
        }
        parcelItem.setId(id);
        log.info("Deleting parcel {}", parcelItem);
        parcelItemDao.delete(parcelItem);
        return true;
    }

}
