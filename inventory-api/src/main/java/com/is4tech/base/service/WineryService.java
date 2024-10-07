package com.is4tech.base.service;

import com.is4tech.base.domain.Winery;
import com.is4tech.base.dto.WineryDto;
import com.is4tech.base.exception.Exceptions;
import com.is4tech.base.repository.WineryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WineryService {

    private final WineryRepository wineryRepository;

    public Winery saveWinery(WineryDto input){

        validateWineryInput(input);

        Winery winery = new Winery();
        winery.setName(input.getName());
        winery.setAddress(input.getAddress());
        winery.setPhone(input.getPhone());
        winery.setMaxCapacity(input.getMaxCapacity());
        winery.setAvailableQuantity(input.getAvailableQuantity());
        winery.setUserId(input.getUserId());
        winery.setStatus(true);

        return wineryRepository.save(winery);
    }

    public Winery updateWinery(WineryDto input, Integer id) {
        Winery existinrWinery = wineryRepository.findById(id)
                .orElseThrow(() -> new Exceptions("Winery not found"));

        validateWineryInput(input);

        existinrWinery.setName(input.getName());
        existinrWinery.setAddress(input.getAddress());
        existinrWinery.setPhone(input.getPhone());
        existinrWinery.setMaxCapacity(input.getMaxCapacity());
        existinrWinery.setAvailableQuantity(input.getAvailableQuantity());
        existinrWinery.setUserId(input.getUserId());
        if (input.getStatus() != null) {
            existinrWinery.setStatus(input.getStatus());
        }
        return wineryRepository.save(existinrWinery);
    }

    public void deleteWinery(Integer id){
        Winery existingWinery= wineryRepository.findById(id)
                .orElseThrow(() -> new Exceptions("Winery not found"));

        wineryRepository.delete(existingWinery);
    }

    public List<Winery> getWineries() {
        List<Winery> wineries = wineryRepository.findAll();
        if (wineries.isEmpty()) {
            throw new Exceptions("No wineries found");
        }
        return wineries;
    }

    public Winery getWineryId(Integer id) {
        return wineryRepository.findById(id)
                .orElseThrow(() -> new Exceptions("Winery not found with id: " + id));
    }

    public void validateWineryInput(WineryDto input){
        if (input.getName() == null || input.getName().trim().isEmpty()){
            throw new Exceptions("Name cannot be empty");
        }
        if (input.getAddress() == null || input.getAddress().trim().isEmpty()){
            throw new Exceptions("Address cannot be empty");
        }
        if (input.getPhone().length() < 8 || input.getPhone().length() > 8) {
            throw new Exceptions("Phone number must be between 8 and 8 characters");
        }
        if (input.getMaxCapacity() == null){
            throw new Exceptions("Max capacity cannot be empty");
        }
        if (input.getAvailableQuantity() == null ){
            throw new Exceptions("Available quantity cannot be empty");
        }
        if (input.getUserId() == null ){
            throw new Exceptions("UserId cannot be empty");
        }
    }

}
