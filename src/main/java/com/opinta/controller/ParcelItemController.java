package com.opinta.controller;

import static java.lang.String.format;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.opinta.dto.ParcelItemDto;
import com.opinta.service.ParcelItemService;

@RestController
@RequestMapping("/parcelitems")
public class ParcelItemController {
    private static final String NO_PARCEL_ITEM_FOUND_FOR_ID_D = "No ParcelItem found for ID %d";
    private ParcelItemService parcelItemService;
    
    @Autowired
    public ParcelItemController(ParcelItemService parcelItemService) {
        this.parcelItemService = parcelItemService;
    }
    
    @GetMapping
    @ResponseStatus(OK)
    public List<ParcelItemDto> getParcelItems() {
        return parcelItemService.getAll();
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getParcelItem(@PathVariable("id") long id) {
        ParcelItemDto parcelItemDto = parcelItemService.getById(id);
        if (parcelItemDto == null) {
            return new ResponseEntity<>(format("No ParcelItem item found for ID %d", id), NOT_FOUND);
        }
        return new ResponseEntity<>(parcelItemDto, OK);
    }

    @PostMapping
    @ResponseStatus(OK)
    public ParcelItemDto createParcelItem(@RequestBody ParcelItemDto parcelItemDto) {
        return parcelItemService.save(parcelItemDto);
    }

    @PutMapping("{id}")
    public ResponseEntity<?> updateParcelItem(@PathVariable long id, @RequestBody ParcelItemDto parcelItemDto) {
        parcelItemDto = parcelItemService.update(id, parcelItemDto);
        if (parcelItemDto == null) {
            return new ResponseEntity<>(format(NO_PARCEL_ITEM_FOUND_FOR_ID_D, id), NOT_FOUND);
        }
        return new ResponseEntity<>(parcelItemDto, OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteParcelItem(@PathVariable long id) {
        if (!parcelItemService.delete(id)) {
            return new ResponseEntity<>(format(NO_PARCEL_ITEM_FOUND_FOR_ID_D, id), NOT_FOUND);
        }
        return new ResponseEntity<>(OK);
    }

}
