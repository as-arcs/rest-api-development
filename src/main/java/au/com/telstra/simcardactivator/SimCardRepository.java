package au.com.telstra.simcardactivator;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for performing database operations on SIM card activation records.
 */
@Repository
public interface SimCardRepository extends JpaRepository<SimCard, Long> {
}