package com.pregnancy.edu.system;

import com.pregnancy.edu.preguser.MyUser;
import com.pregnancy.edu.preguser.UserService;
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

        // Create a user and assign the role
        MyUser myUser = new MyUser();
        myUser.setUsername("admin");
        myUser.setPassword("admin");
        myUser.setEnabled(true);
        myUser.setRole(Role.ADMIN.getDisplayName());
        userService.save(myUser);
    }
}
