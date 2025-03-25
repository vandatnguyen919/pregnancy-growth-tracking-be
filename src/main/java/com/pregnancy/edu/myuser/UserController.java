package com.pregnancy.edu.myuser;

import com.pregnancy.edu.myuser.converter.UserDtoToUserConverter;
import com.pregnancy.edu.myuser.converter.UserToUserDtoConverter;
import com.pregnancy.edu.myuser.dto.UserDto;
import com.pregnancy.edu.system.Result;
import com.pregnancy.edu.system.StatusCode;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    private final UserToUserDtoConverter userToUserDtoConverter;

    private final UserDtoToUserConverter userDtoToUserConverter;

    public UserController(UserService userService, UserToUserDtoConverter userToUserDtoConverter, UserDtoToUserConverter userDtoToUserConverter) {
        this.userService = userService;
        this.userToUserDtoConverter = userToUserDtoConverter;
        this.userDtoToUserConverter = userDtoToUserConverter;
    }

    @GetMapping
    public Result getAllUsers(Pageable pageable) {
        Page<MyUser> myUserPage = userService.findAll(pageable);
        Page<UserDto> userDtoPage = myUserPage.map(userToUserDtoConverter::convert);
        return new Result(true, StatusCode.SUCCESS, "Find All Success", userDtoPage);
    }

    @GetMapping("/{userId}")
    public Result getUserById(@PathVariable Long userId) {
        MyUser hogwartsUser = userService.findById(userId);
        UserDto userDto = userToUserDtoConverter.convert(hogwartsUser);
        return new Result(true, StatusCode.SUCCESS, "Find One Success", userDto);
    }

    @PostMapping
    public Result addUser(@Valid @RequestBody MyUser newUser) {
        MyUser savedUser = userService.save(newUser);
        UserDto savedUserDto = userToUserDtoConverter.convert(savedUser);
        return new Result(true, StatusCode.SUCCESS, "Add Success", savedUserDto);
    }

    @PutMapping("/{userId}")
    public Result updateUser(@PathVariable Long userId, @Valid @RequestBody UserDto userDto) {
        MyUser update = userDtoToUserConverter.convert(userDto);
        MyUser updatedUser = userService.update(userId, update);
        UserDto updatedUserDto = userToUserDtoConverter.convert(updatedUser);
        return new Result(true, StatusCode.SUCCESS, "Update Success", updatedUserDto);
    }

//    @DeleteMapping("/{userId}")
//    public Result deleteUser(@PathVariable Long userId) {
//        this.userService.delete(userId);
//        return new Result(true, StatusCode.SUCCESS, "Delete Success");
//    }
}
