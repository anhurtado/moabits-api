package com.moabits.api.controllers;

import com.moabits.api.services.ClientService;
import com.moabits.api.services.MovementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
public class MovementController {
    private final MovementService movementService;
    private final ClientService clientService;

    @GetMapping("/movement/balance")
    public ResponseEntity<Map<String, String>> getBalance(
        @RequestParam("clientId") final Long clientId,
        @RequestParam(defaultValue = "") final String fromDate,
        @RequestParam(defaultValue = "") final String toDate) {

        this.assertClientExists(clientId);

        Double currentBalance = 0.0;
        if (fromDate.isEmpty() && toDate.isEmpty()) {
            currentBalance = movementService.getCurrentBalance(clientId);
        } else {
            currentBalance = movementService.getCurrentBalanceByDates(clientId, fromDate, toDate);
            currentBalance = Objects.isNull(currentBalance) ? 0.0 : currentBalance;
        }

        Map<String, String> response = new HashMap<>();
        response.put("balance", currentBalance.toString());
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/movement/list")
    public ResponseEntity getListByClientId(
        @RequestParam("clientId") final Long clientId,
        @RequestParam(defaultValue = "") final String fromDate,
        @RequestParam(defaultValue = "") final String toDate,
        @RequestParam(defaultValue = "10") final Long limit,
        @RequestParam(defaultValue = "1") final Long page) {

        this.assertClientExists(clientId);

        List<Map<String, ?>> movementList = null;
        if (fromDate.isEmpty() && toDate.isEmpty()) {
            movementList = movementService.listAllByClientIdAndPaginate(clientId, limit, page);
        } else {
            movementList = movementService.listAllByClientIdAndDatesAndPaginate(clientId, fromDate, toDate, limit, page);
        }
        return ResponseEntity.ok().body(movementList);
    }

    public void assertClientExists(Long clientId) {
        if (Objects.isNull(clientService.getById(clientId))) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Client not found");
        }
    }
}
