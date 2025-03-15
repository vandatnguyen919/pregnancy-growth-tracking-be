package com.pregnancy.edu.myuser;

import com.pregnancy.edu.blog.blogpost.BlogPost;
import com.pregnancy.edu.blog.blogpostcomment.BlogPostComment;
import com.pregnancy.edu.blog.blogpostlike.BlogPostLike;
import com.pregnancy.edu.fetusinfo.fetus.Fetus;
import com.pregnancy.edu.fetusinfo.standard.Standard;
import com.pregnancy.edu.membershippackages.order.Order;
import com.pregnancy.edu.pregnancy.Pregnancy;
import com.pregnancy.edu.reminder.Reminder;
import com.pregnancy.edu.system.common.BloodType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Data
@Entity
public class MyUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "email is required.")
    private String email;

    @NotEmpty(message = "username is required.")
    @Column(unique = true)
    private String username;

    @NotEmpty(message = "password is required.")
    private String password;

    @Column(unique = true)
    private String phoneNumber;

    private String fullName;

    private LocalDateTime dateOfBirth;

    private String avatarUrl;

    private Boolean gender;

    @Enumerated(EnumType.STRING)
    private BloodType bloodType;

    private String symptoms;

    private String nationality;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private Boolean verified = false;

    private Boolean enabled = false;

    @NotEmpty(message = "role is required.")
    private String role;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "user")
    private List<Reminder> reminders = new ArrayList<>();

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "user")
    private List<BlogPost> blogPosts = new ArrayList<>();

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "user")
    private List<BlogPostComment> blogPostComments = new ArrayList<>();

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "user")
    private List<BlogPostLike> blogPostLikes = new ArrayList<>();

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "user")
    private List<Order> orders = new ArrayList<>();

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "user")
    private List<Pregnancy> pregnancies = new ArrayList<>();

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "user")
    private List<Fetus> fetuses = new ArrayList<>();
}

