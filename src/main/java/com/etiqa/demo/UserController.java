package com.etiqa.demo;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api/users")
public class UserController {

    // Thread-safe ID generator
    private static final AtomicLong ID_GENERATOR = new AtomicLong(0);

    // In-memory store
    private static final Map<Long, User> store = new HashMap<>();

    static {
        // seed a couple of users
        seedUser("Alice", "alice@example.com");
        seedUser("Bob",   "bob@example.com");
    }

    private static void seedUser(String name, String email) {
        // Explicitly box the primitive long into a Long
        Long id = Long.valueOf(ID_GENERATOR.incrementAndGet());

        User u = new User();
        u.setId(id);
        u.setName(name);
        u.setEmail(email);
        store.put(id, u);
    }

    @GetMapping
    public List<User> list() {
        return new ArrayList<>(store.values());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getById(@PathVariable Long id) {
        User user = store.get(id);
        return (user != null)
                ? ResponseEntity.ok(user)
                : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<User> create(@RequestBody User incoming) {
        // Explicitly box the primitive long into a Long
        Long nextId = Long.valueOf(ID_GENERATOR.incrementAndGet());

        incoming.setId(nextId);
        store.put(nextId, incoming);
        return ResponseEntity.status(201).body(incoming);
    }

    // Simple DTO with only a default ctor + setters/getters
    public static class User {
        private Long id;
        private String name;
        private String email;

        public User() { /* default ctor only */ }

        public Long getId() {
            return id;
        }
        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }
        public void setEmail(String email) {
            this.email = email;
        }
    }
}
