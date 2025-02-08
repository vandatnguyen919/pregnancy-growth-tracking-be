package com.pregnancy.edu.system;

import com.pregnancy.edu.myuser.MyUser;
import com.pregnancy.edu.myuser.UserService;
import com.pregnancy.edu.system.consts.Role;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DBDataInitializer implements CommandLineRunner {

    private final UserService userService;

    public DBDataInitializer(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void run(String... args) throws Exception {

        // Create some users.
        MyUser u1 = new MyUser();
//        u1.setId(1L);
        u1.setEmail("john@example.com");
        u1.setUsername("john");
        u1.setPassword("123456");
        u1.setEnabled(true);
        u1.setRole("admin");

        MyUser u2 = new MyUser();
//        u2.setId(2L);
        u2.setEmail("eric@example.com");
        u2.setUsername("eric");
        u2.setPassword("654321");
        u2.setEnabled(true);
        u2.setRole("user");

        MyUser u3 = new MyUser();
//        u3.setId(3L);
        u3.setEmail("tom@example.com");
        u3.setUsername("tom");
        u3.setPassword("qwerty");
        u3.setEnabled(false);
        u3.setRole("user");

        this.userService.save(u1);
        this.userService.save(u2);
        this.userService.save(u3);
    }
}
