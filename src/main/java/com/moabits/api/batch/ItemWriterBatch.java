package com.moabits.api.batch;

import com.moabits.api.entities.Movement;
import com.moabits.api.repositories.MovementRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class ItemWriterBatch implements ItemWriter<Movement> {
    private final MovementRepository movementRepository;

    @Override
    public void write(List<? extends Movement> list) throws Exception {
        log.info("Data saved for movement: {}", list);
        movementRepository.saveAll(list);
    }
}
