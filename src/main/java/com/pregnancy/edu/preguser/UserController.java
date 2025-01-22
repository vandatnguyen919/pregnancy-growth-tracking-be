package com.pregnancy.edu.preguser;

import com.pregnancy.edu.preguser.converter.UserDtoToUserConverter;
import com.pregnancy.edu.preguser.converter.UserToUserDtoConverter;
import com.pregnancy.edu.preguser.dto.UserDto;
import com.pregnancy.edu.system.Result;
import com.pregnancy.edu.system.StatusCode;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public Result getAllUsers() {
        List<MyUser> hogwartsUsers = userService.findAll();
        List<UserDto> userDtos = hogwartsUsers.stream().map(userToUserDtoConverter::convert).toList();
        return new Result(true, StatusCode.SUCCESS, "Find All Success", userDtos);
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

    @DeleteMapping("/{userId}")
    public Result deleteUser(@PathVariable Long userId) {
        this.userService.delete(userId);
        return new Result(true, StatusCode.SUCCESS, "Delete Success");
    }
}
