package com.moabits.api.services;

import com.moabits.api.repositories.MovementRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class MovementService {
    private final MovementRepository movementRepository;

    public Double getCurrentBalance(Long clientId) {
        return movementRepository.findCurrentBalanceByClientId(clientId);
    }

    public Double getCurrentBalanceByDates(Long clientId, String fromDate, String toDate) {
        try {
            Date fDate = new SimpleDateFormat("yyyy-MM-dd").parse(fromDate);
            Date tDate = new SimpleDateFormat("yyyy-MM-dd").parse(toDate);
            return movementRepository.findCurrentBalanceByClientIdAndDates(clientId, fDate, tDate);
        } catch (ParseException parseException) {
            log.error(parseException.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Field fromDate or toDate is required on this format YYYY-MM-DD");
        }
    }

    public List<Map<String, ?>> listAllByClientIdAndPaginate(Long clientId, Long limit, Long page) {
        page = page == 1 ? 0 : page;
        page = page == 0 ? page : page + 1;
        return movementRepository.findAllByClientIdAndPaginate(clientId, limit, page);
    }

    public List<Map<String, ?>> listAllByClientIdAndDatesAndPaginate(Long clientId, String fromDate, String toDate, Long limit, Long page) {
        try {
            Date fDate = new SimpleDateFormat("yyyy-MM-dd").parse(fromDate);
            Date tDate = new SimpleDateFormat("yyyy-MM-dd").parse(toDate);
            page = page == 1 ? 0 : page;
            page = page == 0 ? page : page + 1;
            return movementRepository.findAllByClientIdAndDatesAndPaginate(clientId, fDate, tDate, limit, page);
        } catch (ParseException parseException) {
            log.error(parseException.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Field fromDate or toDate is required on this format YYYY-MM-DD");
        }
    }

    public Long getTotalByClientId(Long clientId) {
        return movementRepository.getTotalByClientId(clientId);
    }

    public Long getTotalByClientIdAndDates(Long clientId, String fromDate, String toDate) {
        try {
            Date fDate = new SimpleDateFormat("yyyy-MM-dd").parse(fromDate);
            Date tDate = new SimpleDateFormat("yyyy-MM-dd").parse(toDate);
            return movementRepository.getTotalByClientIdAndDates(clientId, fDate, tDate);
        } catch (ParseException parseException) {
            log.error(parseException.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Field fromDate or toDate is required on this format YYYY-MM-DD");
        }
    }
}
