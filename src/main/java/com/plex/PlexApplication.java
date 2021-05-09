package com.plex;

import com.plex.model.mysql.Role;
import com.plex.model.mysql.User;
import com.plex.repository.mysql.RoleRepository;
import com.plex.repository.mysql.UserRepository;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.Set;

@EnableEncryptableProperties
@SpringBootApplication
public class PlexApplication {

    @Autowired
    private UserRepository userService;
    @Autowired
    private RoleRepository repository;

    public static void main(String[] args) {
        SpringApplication.run(PlexApplication.class, args);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @PostConstruct
    public void execute() {
        Role role = Role.builder()
                .authority("ADMIN")
                .build();

        repository.save(role);

        userService.save(User.builder()
                .username("dzhu")
                .password(passwordEncoder().encode("pass"))
                .createdAt(LocalDateTime.now())
                .email("djuliqn.bg@gmail.com")
                .roles(Set.of(repository.findById(1L).get()))
                .build());
    }
}
