package com.pregnancy.edu.system.utils.generators;

import com.pregnancy.edu.fetusinfo.fetus.Fetus;
import com.pregnancy.edu.pregnancy.Pregnancy;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FetusGenerator {

    public static List<Fetus> generateSampleFetus(List<Pregnancy> pregnancies) {
        List<Fetus> fetuses = new ArrayList<>();
        for (Pregnancy pregnancy : pregnancies) {
            Random random = new Random();
            // Determine number of fetuses (twins chance ~3%)
            int numFetuses = random.nextDouble() < 0.03 ? 2 : 1;

            // Calculate current week of pregnancy
            int currentWeek = pregnancy.getCurrentWeek();

            // Add fetuses
            for (int i = 1; i <= numFetuses; i++) {
                Fetus fetus = new Fetus();
                fetus.setUser(pregnancy.getUser());
                fetus.setFetusNumber(i);
                fetus.setPregnancy(pregnancy);

                // Set nickname
                String[] nicknames = {"Bean", "Peanut", "Nugget", "Sprout", "Bump", "Button", "Blueberry", "Jellybean"};
                fetus.setNickName(nicknames[random.nextInt(nicknames.length)]);

                // Gender may not be known in early pregnancy
                if (currentWeek < 20) { // Before ~20 weeks
                    fetus.setGender("UNKNOWN");
                } else {
                    fetus.setGender(random.nextBoolean() ? "MALE" : "FEMALE");
                }

                // Add fetus to pregnancy
                pregnancy.addFetus(fetus);

                fetuses.add(fetus);
            }
        }
        return fetuses;
    }
}
