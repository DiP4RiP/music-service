package ru.dip4rip.musicservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import ru.dip4rip.musicservice.models.Role;
import ru.dip4rip.musicservice.models.User;
import ru.dip4rip.musicservice.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class RoleBasedAccessTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void testUserRoleCreation() {
        // Создаем пользователя с ролью USER
        User user = new User();
        user.setLogin("testuser");
        user.setPassword(passwordEncoder.encode("password"));
        user.setFullName("Test User");
        user.setRole(Role.USER);

        User savedUser = userRepository.save(user);
        assertNotNull(savedUser.getId());
        assertEquals(Role.USER, savedUser.getRole());
    }

    @Test
    public void testAdminRoleCreation() {
        // Создаем пользователя с ролью ADMIN
        User admin = new User();
        admin.setLogin("admin");
        admin.setPassword(passwordEncoder.encode("admin"));
        admin.setFullName("Admin User");
        admin.setRole(Role.ADMIN);

        User savedAdmin = userRepository.save(admin);
        assertNotNull(savedAdmin.getId());
        assertEquals(Role.ADMIN, savedAdmin.getRole());
    }
}
