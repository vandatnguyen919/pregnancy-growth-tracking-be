package com.pregnancy.edu.system;

import com.pregnancy.edu.blog.blogpost.BlogPost;
import com.pregnancy.edu.blog.blogpost.BlogPostService;
import com.pregnancy.edu.blog.blogpostcomment.BlogPostComment;
import com.pregnancy.edu.blog.blogpostcomment.BlogPostCommentService;
import com.pregnancy.edu.blog.blogpostlike.BlogPostLikeService;
import com.pregnancy.edu.blog.tag.Tag;
import com.pregnancy.edu.blog.tag.TagService;
import com.pregnancy.edu.fetusinfo.fetus.Fetus;
import com.pregnancy.edu.fetusinfo.fetus.FetusService;
import com.pregnancy.edu.fetusinfo.fetusmetric.FetusMetric;
import com.pregnancy.edu.fetusinfo.fetusmetric.FetusMetricService;
import com.pregnancy.edu.fetusinfo.metric.Metric;
import com.pregnancy.edu.fetusinfo.metric.MetricService;
import com.pregnancy.edu.fetusinfo.standard.Standard;
import com.pregnancy.edu.fetusinfo.standard.StandardService;
import com.pregnancy.edu.membershippackages.membership.MembershipPlan;
import com.pregnancy.edu.membershippackages.membership.MembershipPlanService;
import com.pregnancy.edu.myuser.MyUser;
import com.pregnancy.edu.myuser.UserService;
import com.pregnancy.edu.pregnancy.Pregnancy;
import com.pregnancy.edu.pregnancy.PregnancyService;
import com.pregnancy.edu.system.utils.BlogPostGenerator;
import com.pregnancy.edu.system.utils.MyUserGenerator;
import com.pregnancy.edu.system.utils.TagGenerator;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class DBDataInitializer implements CommandLineRunner {

    private final UserService userService;
    private final BlogPostService blogPostService;
    private final BlogPostCommentService blogPostCommentService;
    private final TagService tagService;
    private final PregnancyService pregnancyService;
    private final FetusService fetusService;
    private final MetricService metricService;
    private final StandardService standardService;
    private final FetusMetricService fetusMetricService;
    private final MembershipPlanService membershipPlanService;

    public DBDataInitializer(UserService userService, BlogPostService blogPostService,
                             BlogPostCommentService blogPostCommentService,
                             TagService tagService, PregnancyService pregnancyService, FetusService fetusService, MetricService metricService, StandardService standardService, FetusMetricService fetusMetricService, FetusMetricService fetusMetricService1, MembershipPlanService membershipPlanService) {
        this.userService = userService;
        this.blogPostService = blogPostService;
        this.blogPostCommentService = blogPostCommentService;
        this.tagService = tagService;
        this.pregnancyService = pregnancyService;
        this.fetusService = fetusService;
        this.metricService = metricService;
        this.standardService = standardService;
        this.fetusMetricService = fetusMetricService1;
        this.membershipPlanService = membershipPlanService;
    }

    @Override
    public void run(String... args) throws Exception {
        // Create user data
        MyUser u1 = MyUserGenerator.createJohn();
        MyUser u2 = MyUserGenerator.createEric();
        MyUser u3 = MyUserGenerator.createTom();

        List<MyUser> randomUsers = MyUserGenerator.generateRandomUsers(10);
        randomUsers.add(u1);
        randomUsers.add(u2);
        randomUsers.add(u3);

        for (MyUser user : randomUsers) {
            userService.save(user);
        }

        // Create tags
        List<Tag> tags = TagGenerator.generateSampleTags();
        for (Tag tag : tags) {
            tagService.save(tag);
        }

        // Create blog posts
        List<BlogPost> blogPosts = BlogPostGenerator.generateSampleBlogPosts(randomUsers, tags);
        for (BlogPost blogPost : blogPosts) {
            blogPostService.save(blogPost);
        }

        // Create and save comments
        BlogPostComment bpComment1 = new BlogPostComment();
        bpComment1.setContent("comment1");
        BlogPostComment bpComment2 = new BlogPostComment();
        bpComment2.setContent("comment2");
        BlogPostComment bpComment3 = new BlogPostComment();
        bpComment3.setContent("comment3");

        blogPostCommentService.save(bpComment1);
        blogPostCommentService.save(bpComment2);
        blogPostCommentService.save(bpComment3);

        // Create metrics
        Metric bpdMetric = createMetric("BPD", "NUMERIC", "mm");
        Metric crlMetric = createMetric("CRL", "NUMERIC", "mm");
        Metric acMetric = createMetric("AC", "NUMERIC", "mm");
        Metric fhrMetric = createMetric("Fetal Heart Rate", "NUMERIC", "bpm");
        Metric weightMetric = createMetric("Weight", "NUMERIC", "g");
        Metric flMetric = createMetric("FL", "NUMERIC", "mm");

        // Save metrics
        metricService.save(bpdMetric);
        metricService.save(crlMetric);
        metricService.save(acMetric);
        metricService.save(fhrMetric);
        metricService.save(weightMetric);
        metricService.save(flMetric);

        // Create and save standards for each metric
        createAndSaveBPDStandards(bpdMetric);
        createAndSaveCRLStandards(crlMetric);
        createAndSaveACStandards(acMetric);
        createAndSaveFHRStandards(fhrMetric);
        createAndSaveWeightStandards(weightMetric);
        createAndSaveFLStandards(flMetric);


        // Create pregnancies for users
        Pregnancy pregnancy1 = createPregnancy(u1, LocalDate.now().minusWeeks(20));
        Pregnancy pregnancy2 = createPregnancy(u2, LocalDate.now().minusWeeks(15));

        if (pregnancy1.getFetuses() == null) {
            pregnancy1.setFetuses(new ArrayList<>());
        }
        if (pregnancy2.getFetuses() == null) {
            pregnancy2.setFetuses(new ArrayList<>());
        }

        pregnancyService.save(pregnancy1);
        pregnancyService.save(pregnancy2);

        // Create fetuses
        Fetus fetus1 = createFetus(u1, pregnancy1, "Baby Boy", "MALE", 1);
        Fetus fetus2 = createFetus(u1, pregnancy1, "Baby Girl", "FEMALE", 2); // Twins for user1
        Fetus fetus3 = createFetus(u2, pregnancy2, "Little One", "UNKNOWN", 1);

        fetusService.save(fetus1);
        fetusService.save(fetus2);
        fetusService.save(fetus3);

        createAndSaveFetusMetrics();
        createAndSaveFetusMetrics();
        createAndSaveFetusMetrics();

        MembershipPlan m1 = new MembershipPlan();
        m1.setName("Each Month");
        m1.setPrice(400000D);
        m1.setDurationMonths(1);
        m1.setActive(true);

        MembershipPlan m2 = new MembershipPlan();
        m2.setName("Lifetime Package");
        m2.setPrice(3600000D);
        m2.setDurationMonths(10);
        m2.setActive(true);

        membershipPlanService.save(m1);
        membershipPlanService.save(m2);
    }

    private Pregnancy createPregnancy(MyUser user, LocalDate dueDate) {
        Pregnancy pregnancy = new Pregnancy();
        pregnancy.setUser(user);
        pregnancy.setEstimatedDueDate(dueDate);
        return pregnancy;
    }

    private Metric createMetric(String name, String dataType, String unit) {
        Metric metric = new Metric();
        metric.setName(name);
        metric.setDataType(dataType);
        metric.setUnit(unit);
        return metric;
    }

    private Standard createStandard(Metric metric, Integer week, Double min, Double max) {
        Standard standard = new Standard();
        standard.setMetric(metric);
        standard.setWeek(week);
        standard.setMin(min);
        standard.setMax(max);
        return standard;
    }

    private Fetus createFetus(MyUser user, Pregnancy pregnancy, String nickname, String gender, Integer fetusNumber) {
        Fetus fetus = new Fetus();
        fetus.setUser(user);
        fetus.setPregnancy(pregnancy);
        fetus.setNickName(nickname);
        fetus.setGender(gender);
        fetus.setFetusNumber(fetusNumber);
        return fetus;
    }

    private void createAndSaveFetusMetrics() {
        List<Fetus> allFetuses = fetusService.findAll();
        List<Metric> allMetrics = metricService.findAll();

        for (Fetus fetus : allFetuses) {
            int currentWeek = calculateCurrentWeek(fetus.getPregnancy().getEstimatedDueDate());

            for (Metric metric : allMetrics) {
                // For each metric, find the appropriate weeks to generate data
                int startWeek;
                int endWeek = Math.min(currentWeek, 40); // Don't go beyond 40 weeks

                // Different metrics start at different weeks
                if (metric.getName().equals("CRL")) {
                    startWeek = Math.max(5, currentWeek - 8);
                    endWeek = Math.min(currentWeek, 16); // CRL only goes up to week 16
                } else if (metric.getName().equals("BPD")) {
                    startWeek = Math.max(11, currentWeek - 8);
                } else if (metric.getName().equals("FL")) {
                    startWeek = Math.max(13, currentWeek - 8);
                } else if (metric.getName().equals("AC")) {
                    startWeek = Math.max(16, currentWeek - 8);
                } else if (metric.getName().equals("Fetal Heart Rate")) {
                    startWeek = Math.max(6, currentWeek - 8);
                } else { // Weight or other metrics
                    startWeek = Math.max(12, currentWeek - 8);
                }

                // Generate data for every 4 weeks
                for (int week = startWeek; week <= endWeek; week += 4) {
                    Standard standard = standardService.findByMetricAndWeek(metric.getId(), week);

                    if (standard != null) {
                        double range = standard.getMax() - standard.getMin();
                        double value = standard.getMin() + (Math.random() * range * 0.8) + (range * 0.1);

                        FetusMetric fetusMetric = new FetusMetric();
                        fetusMetric.setFetus(fetus);
                        fetusMetric.setMetric(metric);
                        fetusMetric.setValue(value);
                        fetusMetric.setWeek(week);

                        fetusMetricService.save(fetusMetric);
                    }
                }
            }
            fetusService.save(fetus);
        }
    }

    private int calculateCurrentWeek(LocalDate dueDate) {
        LocalDate today = LocalDate.now();
        long weeksUntilDue = java.time.temporal.ChronoUnit.WEEKS.between(today, dueDate);
        if (weeksUntilDue < 0) {
            return 40;
        }
        return (int) (40 - weeksUntilDue);
    }

    private void createAndSaveBPDStandards(Metric metric) {
        List<Standard> standards = new ArrayList<>();

        // BPD standards from JSON
        standards.add(createStandard(metric, 11, 13.0, 21.0));
        standards.add(createStandard(metric, 12, 17.0, 24.0));
        standards.add(createStandard(metric, 13, 20.0, 28.0));
        standards.add(createStandard(metric, 14, 23.0, 31.0));
        standards.add(createStandard(metric, 15, 26.0, 34.0));
        standards.add(createStandard(metric, 16, 29.0, 37.0));
        standards.add(createStandard(metric, 17, 32.0, 40.0));
        standards.add(createStandard(metric, 18, 35.0, 44.0));
        standards.add(createStandard(metric, 19, 38.0, 47.0));
        standards.add(createStandard(metric, 20, 41.0, 50.0));
        standards.add(createStandard(metric, 21, 44.0, 53.0));
        standards.add(createStandard(metric, 22, 47.0, 56.0));
        standards.add(createStandard(metric, 23, 50.0, 59.0));
        standards.add(createStandard(metric, 24, 53.0, 62.0));
        standards.add(createStandard(metric, 25, 56.0, 65.0));
        standards.add(createStandard(metric, 26, 59.0, 68.0));
        standards.add(createStandard(metric, 27, 62.0, 71.0));
        standards.add(createStandard(metric, 28, 65.0, 74.0));
        standards.add(createStandard(metric, 29, 68.0, 77.0));
        standards.add(createStandard(metric, 30, 71.0, 80.0));
        standards.add(createStandard(metric, 31, 74.0, 83.0));
        standards.add(createStandard(metric, 32, 77.0, 86.0));
        standards.add(createStandard(metric, 33, 80.0, 89.0));
        standards.add(createStandard(metric, 34, 83.0, 92.0));
        standards.add(createStandard(metric, 35, 86.0, 95.0));
        standards.add(createStandard(metric, 36, 89.0, 98.0));
        standards.add(createStandard(metric, 37, 92.0, 101.0));
        standards.add(createStandard(metric, 38, 95.0, 104.0));
        standards.add(createStandard(metric, 39, 98.0, 107.0));
        standards.add(createStandard(metric, 40, 100.0, 110.0));

        saveStandards(standards, metric);
    }

    private void createAndSaveCRLStandards(Metric metric) {
        List<Standard> standards = new ArrayList<>();

        // CRL standards from JSON
        standards.add(createStandard(metric, 5, 2.0, 5.0));
        standards.add(createStandard(metric, 6, 4.0, 6.0));
        standards.add(createStandard(metric, 7, 9.0, 14.0));
        standards.add(createStandard(metric, 8, 16.0, 22.0));
        standards.add(createStandard(metric, 9, 23.0, 31.0));
        standards.add(createStandard(metric, 10, 31.0, 41.0));
        standards.add(createStandard(metric, 11, 41.0, 52.0));
        standards.add(createStandard(metric, 12, 52.0, 67.0));
        standards.add(createStandard(metric, 13, 67.0, 80.0));
        standards.add(createStandard(metric, 14, 80.0, 95.0));
        standards.add(createStandard(metric, 15, 95.0, 110.0));
        standards.add(createStandard(metric, 16, 110.0, 120.0));

        saveStandards(standards, metric);
    }

    private void createAndSaveACStandards(Metric metric) {
        List<Standard> standards = new ArrayList<>();

        // AC standards from JSON
        standards.add(createStandard(metric, 16, 88.0, 116.0));
        standards.add(createStandard(metric, 17, 93.0, 122.0));
        standards.add(createStandard(metric, 18, 98.0, 128.0));
        standards.add(createStandard(metric, 19, 104.0, 136.0));
        standards.add(createStandard(metric, 20, 110.0, 144.0));
        standards.add(createStandard(metric, 21, 116.0, 152.0));
        standards.add(createStandard(metric, 22, 122.0, 160.0));
        standards.add(createStandard(metric, 23, 128.0, 168.0));
        standards.add(createStandard(metric, 24, 134.0, 176.0));
        standards.add(createStandard(metric, 25, 140.0, 184.0));
        standards.add(createStandard(metric, 26, 146.0, 192.0));
        standards.add(createStandard(metric, 27, 152.0, 200.0));
        standards.add(createStandard(metric, 28, 158.0, 208.0));
        standards.add(createStandard(metric, 29, 164.0, 216.0));
        standards.add(createStandard(metric, 30, 170.0, 224.0));
        standards.add(createStandard(metric, 31, 176.0, 232.0));
        standards.add(createStandard(metric, 32, 182.0, 240.0));
        standards.add(createStandard(metric, 33, 188.0, 248.0));
        standards.add(createStandard(metric, 34, 194.0, 256.0));
        standards.add(createStandard(metric, 35, 200.0, 264.0));
        standards.add(createStandard(metric, 36, 206.0, 272.0));
        standards.add(createStandard(metric, 37, 212.0, 280.0));
        standards.add(createStandard(metric, 38, 218.0, 288.0));
        standards.add(createStandard(metric, 39, 224.0, 296.0));
        standards.add(createStandard(metric, 40, 230.0, 304.0));

        saveStandards(standards, metric);
    }

    private void createAndSaveFHRStandards(Metric metric) {
        List<Standard> standards = new ArrayList<>();

        // Fetal Heart Rate standards from JSON
        standards.add(createStandard(metric, 6, 90.0, 110.0));
        standards.add(createStandard(metric, 7, 110.0, 130.0));
        standards.add(createStandard(metric, 8, 120.0, 150.0));
        standards.add(createStandard(metric, 9, 120.0, 160.0));
        standards.add(createStandard(metric, 10, 130.0, 170.0));

        // Weeks 11-27 have the same range
        for (int week = 11; week <= 27; week++) {
            standards.add(createStandard(metric, week, 140.0, 170.0));
        }

        // Weeks 28-35 have the same range
        for (int week = 28; week <= 35; week++) {
            standards.add(createStandard(metric, week, 120.0, 160.0));
        }

        // Weeks 36-40 have the same range
        for (int week = 36; week <= 40; week++) {
            standards.add(createStandard(metric, week, 110.0, 160.0));
        }

        saveStandards(standards, metric);
    }

    private void createAndSaveWeightStandards(Metric metric) {
        List<Standard> standards = new ArrayList<>();

        // Weight standards from JSON
        standards.add(createStandard(metric, 12, 10.0, 20.0));
        standards.add(createStandard(metric, 13, 23.0, 30.0));
        standards.add(createStandard(metric, 14, 40.0, 45.0));
        standards.add(createStandard(metric, 15, 70.0, 75.0));
        standards.add(createStandard(metric, 16, 100.0, 110.0));
        standards.add(createStandard(metric, 17, 140.0, 150.0));
        standards.add(createStandard(metric, 18, 190.0, 210.0));
        standards.add(createStandard(metric, 19, 240.0, 270.0));
        standards.add(createStandard(metric, 20, 290.0, 320.0));
        standards.add(createStandard(metric, 21, 350.0, 380.0));
        standards.add(createStandard(metric, 22, 430.0, 460.0));
        standards.add(createStandard(metric, 23, 500.0, 550.0));
        standards.add(createStandard(metric, 24, 600.0, 650.0));
        standards.add(createStandard(metric, 25, 700.0, 750.0));
        standards.add(createStandard(metric, 26, 800.0, 850.0));
        standards.add(createStandard(metric, 27, 900.0, 1000.0));
        standards.add(createStandard(metric, 28, 1000.0, 1100.0));
        standards.add(createStandard(metric, 29, 1150.0, 1250.0));
        standards.add(createStandard(metric, 30, 1300.0, 1400.0));
        standards.add(createStandard(metric, 31, 1500.0, 1600.0));
        standards.add(createStandard(metric, 32, 1700.0, 1800.0));
        standards.add(createStandard(metric, 33, 1900.0, 2000.0));
        standards.add(createStandard(metric, 34, 2100.0, 2200.0));
        standards.add(createStandard(metric, 35, 2300.0, 2400.0));
        standards.add(createStandard(metric, 36, 2600.0, 2800.0));
        standards.add(createStandard(metric, 37, 2800.0, 3000.0));
        standards.add(createStandard(metric, 38, 3000.0, 3200.0));
        standards.add(createStandard(metric, 39, 3200.0, 3400.0));
        standards.add(createStandard(metric, 40, 3400.0, 3600.0));

        saveStandards(standards, metric);
    }
    private void createAndSaveFLStandards(Metric metric) {
        List<Standard> standards = new ArrayList<>();

        // FL (Femur Length) standards from JSON
        standards.add(createStandard(metric, 13, 7.0, 9.0));
        standards.add(createStandard(metric, 14, 10.0, 14.0));
        standards.add(createStandard(metric, 15, 12.0, 17.0));
        standards.add(createStandard(metric, 16, 15.0, 20.0));
        standards.add(createStandard(metric, 17, 17.0, 23.0));
        standards.add(createStandard(metric, 18, 20.0, 26.0));
        standards.add(createStandard(metric, 19, 23.0, 30.0));
        standards.add(createStandard(metric, 20, 26.0, 34.0));
        standards.add(createStandard(metric, 21, 29.0, 37.0));
        standards.add(createStandard(metric, 22, 32.0, 40.0));
        standards.add(createStandard(metric, 23, 35.0, 44.0));
        standards.add(createStandard(metric, 24, 38.0, 47.0));
        standards.add(createStandard(metric, 25, 41.0, 50.0));
        standards.add(createStandard(metric, 26, 44.0, 53.0));
        standards.add(createStandard(metric, 27, 47.0, 56.0));
        standards.add(createStandard(metric, 28, 50.0, 59.0));
        standards.add(createStandard(metric, 29, 53.0, 62.0));
        standards.add(createStandard(metric, 30, 56.0, 65.0));
        standards.add(createStandard(metric, 31, 59.0, 68.0));
        standards.add(createStandard(metric, 32, 62.0, 71.0));
        standards.add(createStandard(metric, 33, 65.0, 74.0));
        standards.add(createStandard(metric, 34, 68.0, 77.0));
        standards.add(createStandard(metric, 35, 71.0, 80.0));
        standards.add(createStandard(metric, 36, 74.0, 83.0));
        standards.add(createStandard(metric, 37, 77.0, 86.0));
        standards.add(createStandard(metric, 38, 80.0, 89.0));
        standards.add(createStandard(metric, 39, 83.0, 92.0));
        standards.add(createStandard(metric, 40, 86.0, 95.0));

        saveStandards(standards, metric);
    }

    private void saveStandards(List<Standard> standards, Metric metric) {
        for (Standard standard : standards) {
            standardService.save(standard);
        }

        if (metric.getStandards() == null) {
            metric.setStandards(new ArrayList<>());
        }
        metric.getStandards().addAll(standards);
        metricService.save(metric);
    }

}