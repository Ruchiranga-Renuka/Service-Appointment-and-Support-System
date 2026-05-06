package com.example.sas.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DbRunner implements CommandLineRunner {

    private final JdbcTemplate jdbcTemplate;

    public DbRunner(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(String... args) {
        try {
            jdbcTemplate.execute("ALTER TABLE jobs MODIFY COLUMN status VARCHAR(30) NOT NULL DEFAULT 'PENDING'");
            System.out.println("DEBUG: Successfully patched 'jobs' table status constraint.");
        } catch (Exception e) {
            System.out.println("DEBUG: Note: Could not patch 'jobs' table (may already be patched). Error: " + e.getMessage());
        }
    }
}
