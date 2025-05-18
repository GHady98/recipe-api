package com.ghadynazha.recipeapi.service;

import com.ghadynazha.recipeapi.model.Counter;
import com.ghadynazha.recipeapi.repository.CounterRepository;
import org.springframework.stereotype.Service;

/**
 * Service class for generating auto-incrementing sequence numbers.
 *
 * This service fetches and updates a {@link Counter} document in the database
 * to produce unique, incremental IDs for entities like users or recipes.
 *
 * Author: Ghady Nazha
 */
@Service
public class CounterService {

    private final CounterRepository counterRepo;

    /**
     * Constructs the CounterService with the required repository.
     *
     * @param counterRepo the repository for managing {@link Counter} documents
     */
    public CounterService(CounterRepository counterRepo) {
        this.counterRepo = counterRepo;
    }

    /**
     * Returns the next sequence number for the given counter ID.
     * If the counter doesn't exist, initializes it with 0.
     *
     * @param id the unique identifier for the counter (e.g., "user", "recipe")
     * @return the next incremented sequence value
     */
    public int getNextSequence(String id) {
        Counter counter = counterRepo.findById(id).orElse(new Counter(id, 0));
        counter.setSeq(counter.getSeq() + 1);
        counterRepo.save(counter);
        return counter.getSeq();
    }
}


