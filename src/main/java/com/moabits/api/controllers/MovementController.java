package com.moabits.api.controllers;

import com.moabits.api.services.ClientService;
import com.moabits.api.services.MovementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
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
@Slf4j
public class MovementController {
    private final MovementService movementService;
    private final ClientService clientService;
    private final JobLauncher jobLauncher;
    private final Job job;

    @GetMapping("/movement/list")
    public ResponseEntity getListByClientId(
        @RequestParam("clientId") final Long clientId,
        @RequestParam(defaultValue = "") final String fromDate,
        @RequestParam(defaultValue = "") final String toDate,
        @RequestParam(defaultValue = "10") final Long limit,
        @RequestParam(defaultValue = "1") final Long page) {

        // Validate client exists
        this.assertClientExists(clientId);

        // Movements
        List<Map<String, ?>> movementList = null;
        if (fromDate.isEmpty() && toDate.isEmpty()) {
            movementList = movementService.listAllByClientIdAndPaginate(clientId, limit, page);
        } else {
            movementList = movementService.listAllByClientIdAndDatesAndPaginate(clientId, fromDate, toDate, limit, page);
        }

        // Balance
        Double currentBalance = 0.0;
        if (fromDate.isEmpty() && toDate.isEmpty()) {
            currentBalance = movementService.getCurrentBalance(clientId);
        } else {
            currentBalance = movementService.getCurrentBalanceByDates(clientId, fromDate, toDate);
        }
        currentBalance = Objects.isNull(currentBalance) ? 0.0 : currentBalance;

        // Total
        Long total = 0L;
        if (fromDate.isEmpty() && toDate.isEmpty()) {
            total = movementService.getTotalByClientId(clientId);
        } else {
            total = movementService.getTotalByClientIdAndDates(clientId, fromDate, toDate);
        }

        // Response
        Map<String, Object> response = new HashMap<>();
        response.put("items", movementList);
        response.put("balance", currentBalance);
        response.put("total", total);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/movement/batch")
    public BatchStatus load() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
        Map<String, JobParameter> maps = new HashMap<>();
        maps.put("time", new JobParameter(System.currentTimeMillis()));

        JobParameters parameters = new JobParameters(maps);
        JobExecution jobExecution = jobLauncher.run(job, parameters);

        log.info("JobExecution: {}", jobExecution.getStatus());
        log.info("Batch is running...");
        while (jobExecution.isRunning()) {
            log.info("...");
        }
        return jobExecution.getStatus();
    }

    public void assertClientExists(Long clientId) {
        if (Objects.isNull(clientService.getById(clientId))) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Client not found");
        }
    }
}
