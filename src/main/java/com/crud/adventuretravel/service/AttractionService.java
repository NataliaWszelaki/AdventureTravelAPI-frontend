package com.crud.adventuretravel.service;

import com.crud.adventuretravel.backend.BackendClient;
import com.crud.adventuretravel.backend.BackendRequestException;
import com.crud.adventuretravel.domain.AttractionDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AttractionService {

    private static BackendClient backendClient;
    private static AttractionService attractionService;

    public AttractionService(BackendClient backendClient) {

        AttractionService.backendClient = backendClient;
    }

    public static AttractionService getInstance() {

        if (attractionService == null) {
            attractionService = new AttractionService(backendClient);
        }
        return attractionService;
    }

    public List<AttractionDto> getAllAttractions() {

        return backendClient.getAttractionsList();
    }

    public void saveAttractionDto(AttractionDto AttractionDto) {

        backendClient.post(AttractionDto);
    }

    public List<AttractionDto> findByName(String name) {

        List<AttractionDto> AttractionDtoList = getAllAttractions();
        return AttractionDtoList.stream()
                .filter(c -> c.getName().contains(name))
                .collect(Collectors.toList());
    }

    public void deleteAttractionDto(AttractionDto AttractionDto) {

        backendClient.delete(AttractionDto);
    }

    public void updateAttractionDto(AttractionDto AttractionDto) throws BackendRequestException {

        backendClient.put(AttractionDto);
    }

    public AttractionDto getAttractionById(Long id) {

        return backendClient.getAttractionById(id);
    }
}
