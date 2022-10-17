package com.moabits.api.batch;

import com.moabits.api.entities.Movement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
@Slf4j
public class ItemProcessorBatch implements ItemProcessor<Movement, Movement> {
    @Override
    public Movement process(Movement movement) throws Exception {
        log.info("Movement: {}", movement);
        movement.setCreatedAt(new Date());
        return movement;
    }
}
