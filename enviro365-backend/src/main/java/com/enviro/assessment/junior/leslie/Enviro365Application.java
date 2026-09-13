package com.enviro.assessment.junior.leslie;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point for the Enviro365 Investments Withdrawal System.
 *
 * This application exposes a REST API that allows investors to view their
 * portfolios, submit withdrawal notices (subject to business rule
 * validation), view withdrawal history and export withdrawal statements
 * to CSV.
 */
@SpringBootApplication
public class Enviro365Application {

    public static void main(String[] args) {
        SpringApplication.run(Enviro365Application.class, args);
    }
}
