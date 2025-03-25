package com.pregnancy.edu.system.utils.generators;

import com.pregnancy.edu.myuser.MyUser;
import com.pregnancy.edu.pregnancy.Pregnancy;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PregnancyGenerator {

    public static List<Pregnancy> generateSamplePregnancy(List<MyUser> users) {
        List<Pregnancy> pregnancies = new ArrayList<>();
        for (MyUser user : users) {
            // Only add pregnancy data for females (gender = false)
            if (user.getGender() != null && !user.getGender()) {

                Random random = new Random();
                // 90% chance of having a pregnancy for female users
                if (random.nextDouble() < 1) {
                    Pregnancy pregnancy = new Pregnancy();

                    // Calculate age from DOB
                    int maternalAge = 0;
                    if (user.getDateOfBirth() != null) {
                        maternalAge = LocalDate.now().getYear() - user.getDateOfBirth().getYear();
                    } else {
                        maternalAge = random.nextInt(22) + 18; // 18-40 years
                    }
                    pregnancy.setMaternalAge(maternalAge);

                    // Randomize pregnancy start date - within last 10 months
                    LocalDate now = LocalDate.now();
                    int daysBack = random.nextInt(300); // up to ~10 months ago
                    LocalDate pregnancyStartDate = now.minusDays(daysBack);
                    pregnancy.setPregnancyStartDate(pregnancyStartDate);

                    // Set estimated due date (~40 weeks/280 days from start)
                    pregnancy.setEstimatedDueDate(pregnancyStartDate.plusDays(266));
                    pregnancy.setDeliveryDate(pregnancyStartDate.plusDays(280));

                    // If already delivered
                    if (daysBack > 280) {
                        pregnancy.setDeliveryDate(pregnancyStartDate.plusDays(random.nextInt(30) + 265)); // Between 38-42 weeks
                        pregnancy.setStatus("COMPLETED");
                    } else {
                        pregnancy.setStatus("ONGOING");
                    }

                    // Set user
                    pregnancy.setUser(user);

                    pregnancies.add(pregnancy);
                }
            }
        }
        return pregnancies;
    }
}
