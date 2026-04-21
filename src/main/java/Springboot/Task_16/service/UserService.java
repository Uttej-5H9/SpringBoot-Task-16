package Springboot.Task_16.service;

import Springboot.Task_16.entity.User;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {


    private List<User> users = new ArrayList<>();

    public UserService() {
        users.add(new User(1L, "Alice", "alice@example.com"));
        users.add(new User(2L, "Bob", "bob@example.com"));
    }

    @Cacheable(value = "users")
    public List<User> getAllUsers() {
        System.out.println("Fetching users from 'DB' (simulated)...");
        try { Thread.sleep(2000); } catch (InterruptedException e) {}
        return users;
    }

    @Cacheable(value = "user", key = "#id")
    public User getUserById(Long id) {
        System.out.println("Fetching user from 'DB' (simulated)...");
        try { Thread.sleep(2000); } catch (InterruptedException e) {}
        return users.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @CacheEvict(value = "users", allEntries = true)
    public User updateUser(Long id, User dto) {
        User user = users.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setName(dto.getName());
        user.setEmail(dto.getEmail());

        return user;
    }

    @CacheEvict(value = "users", allEntries = true)
    public void deleteUser(Long id) {
        users.removeIf(u -> u.getId().equals(id));
    }
}
