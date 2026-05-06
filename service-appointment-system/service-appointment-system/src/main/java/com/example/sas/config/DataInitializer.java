package com.example.sas.config;

import com.example.sas.entity.User;
import com.example.sas.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        seedAdmin();
    }

    private void seedAdmin() {
        if (!userRepository.existsByEmail("admin@system.com")) {
            User admin = new User();
            admin.setFullName("System Administrator");
            admin.setEmail("admin@system.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setPhone("0777000000");
            admin.setRole(User.Role.ADMIN);
            admin.setEnabled(true);
            userRepository.save(admin);
            log.info("✅ Admin user created: admin@system.com / admin123");
        }

        if (!userRepository.existsByEmail("staff@system.com")) {
            User staff = new User();
            staff.setFullName("Demo Staff");
            staff.setEmail("staff@system.com");
            staff.setPassword(passwordEncoder.encode("staff123"));
            staff.setPhone("0777000001");
            staff.setRole(User.Role.STAFF);
            staff.setEnabled(true);
            userRepository.save(staff);
            log.info("✅ Staff user created: staff@system.com / staff123");
        }

        if (!userRepository.existsByEmail("customer@system.com")) {
            User customer = new User();
            customer.setFullName("Demo Customer");
            customer.setEmail("customer@system.com");
            customer.setPassword(passwordEncoder.encode("customer123"));
            customer.setPhone("0777000002");
            customer.setRole(User.Role.CUSTOMER);
            customer.setEnabled(true);
            userRepository.save(customer);
            log.info("✅ Customer user created: customer@system.com / customer123");
        }
    }

}
