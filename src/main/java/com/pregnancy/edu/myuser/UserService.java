package com.pregnancy.edu.myuser;

import com.pregnancy.edu.system.common.base.BaseCrudService;
import com.pregnancy.edu.system.exception.ObjectNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class UserService implements BaseCrudService<MyUser, Long>, UserDetailsService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Page<MyUser> findAll(Pageable pageable) {
        return this.userRepository.findAll(pageable);
    }

    @Override
    public List<MyUser> findAll() {
        return this.userRepository.findAll();
    }

    public MyUser findById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new ObjectNotFoundException("user", userId));
    }

    public MyUser save(MyUser newUser) {
        newUser.setPassword(this.passwordEncoder.encode(newUser.getPassword()));
        return this.userRepository.save(newUser);
    }

    public MyUser update(Long userId, MyUser user) {
        return this.userRepository.findById(userId)
                .map(oldUser -> {
                    oldUser.setAvatarUrl(user.getAvatarUrl());
                    oldUser.setFullName(user.getFullName());
                    oldUser.setUsername(user.getUsername());
                    oldUser.setPhoneNumber(user.getPhoneNumber());
                    oldUser.setDateOfBirth(user.getDateOfBirth());
                    oldUser.setGender(user.getGender());
                    oldUser.setBloodType(user.getBloodType());
                    oldUser.setSymptoms(user.getSymptoms());
                    oldUser.setNationality(user.getNationality());
                    return this.userRepository.save(oldUser);
                })
                .orElseThrow(() -> new ObjectNotFoundException("user", userId));
    }

    public void delete(Long userId) {
        this.userRepository.findById(userId).orElseThrow(() -> new ObjectNotFoundException("user", userId));
        this.userRepository.deleteById(userId);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.userRepository.findByEmail(username) // First, we need to find this user from database.
                .map(MyUserPrincipal::new)  // If found, wrap the returned user instance in a MyUserPrinciple instance.
                .orElseThrow(() -> new UsernameNotFoundException("email " + username + " is not found.")); // Otherwise, throw an exception.
    }
}
