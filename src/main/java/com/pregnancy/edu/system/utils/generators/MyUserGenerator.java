package com.pregnancy.edu.system.utils.generators;

import com.pregnancy.edu.fetusinfo.fetus.Fetus;
import com.pregnancy.edu.fetusinfo.fetusmetric.FetusMetric;
import com.pregnancy.edu.fetusinfo.metric.Metric;
import com.pregnancy.edu.fetusinfo.standard.Standard;
import com.pregnancy.edu.myuser.MyUser;
import com.pregnancy.edu.pregnancy.Pregnancy;
import com.pregnancy.edu.system.common.BloodType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class MyUserGenerator {

    public static MyUser createJohn() {
        MyUser u1 = new MyUser();

        // Keeping original values
        u1.setEmail("john@example.com");
        u1.setUsername("john");
        u1.setPassword("123456");
        u1.setVerified(true);
        u1.setEnabled(true);
        u1.setRole("admin");

        // Adding more information
        u1.setPhoneNumber("+14155552671");
        u1.setFullName("John Anderson Smith");
        u1.setDateOfBirth(LocalDateTime.of(1980, 5, 15, 0, 0));
        u1.setAvatarUrl("https://storage.vivago.ai/image/j_da373917-4d47-4807-97d7-4ffc45c0ced4.jpg?width=512");
        u1.setGender(true); // Male
        u1.setBloodType(BloodType.O_POSITIVE);
        u1.setSymptoms("None");
        u1.setNationality("American");
        u1.setCreatedAt(LocalDateTime.of(2023, 1, 10, 9, 30, 0));
        u1.setUpdatedAt(LocalDateTime.now());

        return u1;
    }

    public static MyUser createEric() {
        MyUser u2 = new MyUser();

        // Keeping original values
        u2.setEmail("eric@example.com");
        u2.setUsername("eric");
        u2.setPassword("654321");
        u2.setVerified(true);
        u2.setEnabled(true);
        u2.setRole("member");

        // Adding more information
        u2.setPhoneNumber("+44789123456");
        u2.setFullName("Eric James Williams");
        u2.setDateOfBirth(LocalDateTime.of(1992, 9, 23, 0, 0));
        u2.setAvatarUrl("https://storage.vivago.ai/image/j_da373917-4d47-4807-97d7-4ffc45c0ced4.jpg?width=512");
        u2.setGender(true); // Male
        u2.setBloodType(BloodType.A_NEGATIVE);
        u2.setSymptoms("Occasional headaches, mild hypertension");
        u2.setNationality("British");
        u2.setCreatedAt(LocalDateTime.of(2023, 3, 22, 14, 15, 0));
        u2.setUpdatedAt(LocalDateTime.of(2024, 2, 5, 11, 30, 0));

        return u2;
    }

    public static MyUser createTom() {
        MyUser u3 = new MyUser();

        // Keeping original values
        u3.setEmail("tom@example.com");
        u3.setUsername("tom");
        u3.setPassword("qwerty");
        u3.setVerified(false);
        u3.setEnabled(true);
        u3.setRole("user");

        // Adding more information
        u3.setPhoneNumber("+61412345678");
        u3.setFullName("Thomas Robert Chen");
        u3.setDateOfBirth(LocalDateTime.of(1988, 12, 7, 0, 0));
        u3.setAvatarUrl("https://storage.vivago.ai/image/j_da373917-4d47-4807-97d7-4ffc45c0ced4.jpg?width=512");
        u3.setGender(true); // Male
        u3.setBloodType(BloodType.B_POSITIVE);
        u3.setSymptoms("Seasonal allergies, lower back pain");
        u3.setNationality("Australian");
        u3.setCreatedAt(LocalDateTime.of(2024, 1, 3, 8, 45, 0));
        u3.setUpdatedAt(LocalDateTime.of(2024, 1, 3, 8, 45, 0));

        return u3;
    }

    public static List<MyUser> generateRandomUsers(int count) {
        List<MyUser> users = new ArrayList<>();
        Random random = new Random();
        
        String[] domains = {"gmail.com", "yahoo.com", "outlook.com", "hotmail.com", "company.com"};
        String[] firstNames = {"Emma", "Sarah", "Sophia", "Olivia", "Ava", "Lily", "Zoe", "Isabella", "Charlotte", "Mia"};
        String[] lastNames = {"Smith", "Johnson", "Williams", "Brown", "Jones", "Garcia", "Miller", "Davis", "Rodriguez", "Martinez"};
        String[] nationalities = {"American", "British", "Canadian", "Australian", "French", "German", "Spanish", "Italian", "Japanese", "Chinese"};
        String[] symptoms = {"Headache", "Fever", "Nausea", "Fatigue", "Cough", "Dizziness", "None", "Chest pain", "Shortness of breath", "Muscle ache"};
        String[] roles = {"user", "member"};
        
        for (int i = 0; i < count; i++) {
            MyUser user = new MyUser();
            
            String firstName = firstNames[random.nextInt(firstNames.length)];
            String lastName = lastNames[random.nextInt(lastNames.length)];
            
            user.setUsername(firstName.toLowerCase() + lastName.toLowerCase() + random.nextInt(100));
            user.setEmail(firstName.toLowerCase() + "." + lastName.toLowerCase() + "@" + domains[random.nextInt(domains.length)]);
            user.setPassword("Password" + random.nextInt(1000) + "!");
            user.setPhoneNumber(generatePhoneNumber(random));
            user.setFullName(firstName + " " + lastName);
            
            // Generate date of birth (18-80 years ago)
            LocalDate now = LocalDate.now();
            int years = random.nextInt(22) + 18; // 18-40 years
            int months = random.nextInt(12);
            int days = random.nextInt(28); // Simplified for safety
            user.setDateOfBirth(now.minusYears(years).minusMonths(months).minusDays(days).atStartOfDay());
            
            user.setAvatarUrl("https://s3.envato.com/files/437230449/63f8b7060d73aa1e07faf27f_withmeta.jpg");

            // Make 95% of users female (gender=false)
            // true=male (5%), false=female (95%)
            user.setGender(random.nextDouble() >= 0.95);
            user.setBloodType(BloodType.values()[random.nextInt(BloodType.values().length)]);
            user.setSymptoms(symptoms[random.nextInt(symptoms.length)]);
            user.setNationality(nationalities[random.nextInt(nationalities.length)]);

            user.setVerified(random.nextBoolean());
            user.setEnabled(random.nextDouble() > 0.2); // 80% likely to be enabled
            
            user.setRole(roles[random.nextInt(roles.length)]);

            users.add(user);
        }
        
        return users;
    }

    private static String generatePhoneNumber(Random random) {
        StringBuilder phoneNumber = new StringBuilder("+1");
        for (int i = 0; i < 10; i++) {
            phoneNumber.append(random.nextInt(10));
        }
        return phoneNumber.toString();
    }

    public static void main(String[] args) {
        List<MyUser> users = generateRandomUsers(10);

        // Print users
        for (MyUser user : users) {
            System.out.println(user);
        }
    }
}