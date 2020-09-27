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
import com.opinta.dto.ParcelDto;
import com.opinta.service.ParcelService;

@RestController
@RequestMapping("/parcels")
public class ParcelController {
    private ParcelService parcelService;
    
    @Autowired
    public ParcelController(ParcelService parcelService) {
        this.parcelService = parcelService;
    }
    
    @GetMapping
    @ResponseStatus(OK)
    public List<ParcelDto> getParcel() {
        return parcelService.getAll();
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getParcel(@PathVariable("id") long id) {
        ParcelDto parcelDto = parcelService.getById(id);
        if (parcelDto == null) {
            return new ResponseEntity<>(format("No Parcel found for ID %d", id), NOT_FOUND);
        }
        return new ResponseEntity<>(parcelDto, OK);
    }

    @PostMapping
    @ResponseStatus(OK)
    public ParcelDto createParcel(@RequestBody ParcelDto parcelDto) {
        return parcelService.save(parcelDto);
    }

    @PutMapping("{id}")
    public ResponseEntity<?> updateParcel(@PathVariable long id, @RequestBody ParcelDto parcelDto) {
        parcelDto = parcelService.update(id, parcelDto);
        if (parcelDto == null) {
            return new ResponseEntity<>(format("No Parcel found for ID %d", id), NOT_FOUND);
        }
        return new ResponseEntity<>(parcelDto, OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteParcel(@PathVariable long id) {
        if (!parcelService.delete(id)) {
            return new ResponseEntity<>(format("No Parcel found for ID %d", id), NOT_FOUND);
        }
        return new ResponseEntity<>(OK);
    }
    
}
