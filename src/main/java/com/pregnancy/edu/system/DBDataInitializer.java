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
    private final BlogPostLikeService blogPostLikeService;
    private final BlogPostCommentService blogPostCommentService;
    private final TagService tagService;
    private final PregnancyService pregnancyService;
    private final FetusService fetusService;
    private final MetricService metricService;
    private final StandardService standardService;
    private final FetusMetricService fetusMetricService;
    private final MembershipPlanService membershipPlanService;

    public DBDataInitializer(UserService userService, BlogPostService blogPostService,
                             BlogPostLikeService blogPostLikeService,
                             BlogPostCommentService blogPostCommentService,
                             TagService tagService, PregnancyService pregnancyService, FetusService fetusService, MetricService metricService, StandardService standardService, FetusMetricService fetusMetricService, FetusMetricService fetusMetricService1, MembershipPlanService membershipPlanService) {
        this.userService = userService;
        this.blogPostService = blogPostService;
        this.blogPostLikeService = blogPostLikeService;
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
        // Create and save users first
        MyUser u1 = createUser("john@example.com", "john", "123456", true, true,"admin");
        MyUser u2 = createUser("eric@example.com", "eric", "654321", true, true, "user");
        MyUser u3 = createUser("tom@example.com", "tom", "qwerty", false, true, "user");

        userService.save(u1);
        userService.save(u2);
        userService.save(u3);

        // First create and save all tags
        Tag t1 = new Tag();
        t1.setName("Pregnancy");
        t1 = tagService.save(t1);

        Tag t2 = new Tag();
        t2.setName("Nutrition");
        t2 = tagService.save(t2);

        Tag t3 = new Tag();
        t3.setName("Sleep");
        t3 = tagService.save(t3);

        BlogPost p1 = createBlogPost(
                "Top 10 Foods Every Pregnant Mom Should Include in Her Diet",
                "Content 1",
                "https://images.squarespace-cdn.com/content/v1/57c93870b3db2b6e16992e6c/1512434151171-3RNNOUGZXAIRBBRU342M/pregnant-mom-baby",
                "By Texas Health and Human Services: If you experience any of these symptoms during or after",
                new ArrayList<>(Arrays.asList(t1, t2, t3))
        );

        BlogPost p2 = createBlogPost(
                "The Importance of Sleep for Moms and Babies: Tips to Rest Better",
                "Content 2",
                "https://images.squarespace-cdn.com/content/v1/57c93870b3db2b6e16992e6c/1512434151171-3RNNOUGZXAIRBBRU342M/pregnant-mom-baby",
                "By Texas Health and Human Services: If you experience any of these symptoms during or after",
                new ArrayList<>(Arrays.asList(t1, t2))
        );

        BlogPost p3 = createBlogPost(
                "5 Simple Prenatal Yoga Poses to Reduce Stress and Boost Energy",
                "Content 3",
                "https://images.squarespace-cdn.com/content/v1/57c93870b3db2b6e16992e6c/1512434151171-3RNNOUGZXAIRBBRU342M/pregnant-mom-baby",
                "By Texas Health and Human Services: If you experience any of these symptoms during or after",
                new ArrayList<>(Arrays.asList(t1, t2, t3))
        );

        BlogPost p4 = createBlogPost(
                "Pregnancy Warning Signs You Should Never Ignore",
                "Content 4",
                "https://images.squarespace-cdn.com/content/v1/57c93870b3db2b6e16992e6c/1512434151171-3RNNOUGZXAIRBBRU342M/pregnant-mom-baby",
                "By Texas Health and Human Services: If you experience any of these symptoms during or after",
                new ArrayList<>(Arrays.asList(t1, t3))
        );

        BlogPost p5 = createBlogPost(
                "Fun and Educational Games to Boost Your Baby's Brain Development",
                "Content 5",
                "https://images.squarespace-cdn.com/content/v1/57c93870b3db2b6e16992e6c/1512434151171-3RNNOUGZXAIRBBRU342M/pregnant-mom-baby",
                "For children, play is not just about having fun—it's crucial for developing critical",
                new ArrayList<>(Arrays.asList(t1, t2))
        );

        BlogPost p6 = createBlogPost(
                "Common Newborn Health Issues and How to Handle Them Like a Pro",
                "Content 6",
                "https://images.squarespace-cdn.com/content/v1/57c93870b3db2b6e16992e6c/1512434151171-3RNNOUGZXAIRBBRU342M/pregnant-mom-baby",
                "If you're a first-time parent, and even if you've already been through it before",
                new ArrayList<>(Arrays.asList(t1, t2))
        );

        // Save blog posts
        blogPostService.save(p1);
        blogPostService.save(p2);
        blogPostService.save(p3);
        blogPostService.save(p4);
        blogPostService.save(p5);
        blogPostService.save(p6);

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

        // Create metrics with their standards
        Metric weightMetric = createMetric("Weight", "NUMERIC", "grams");
        Metric lengthMetric = createMetric("Length", "NUMERIC", "cm");
        Metric headCircumferenceMetric = createMetric("Head Circumference", "NUMERIC", "cm");

        metricService.save(weightMetric);
        metricService.save(lengthMetric);
        metricService.save(headCircumferenceMetric);

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

        createAndSaveStandards(weightMetric);
        createAndSaveStandards(lengthMetric);
        createAndSaveStandards(headCircumferenceMetric);

        createAndSaveFetusMetrics();
        createAndSaveFetusMetrics();
        createAndSaveFetusMetrics();

        MembershipPlan m1 = new MembershipPlan();
        m1.setName("1 month");
        m1.setPrice(400000D);
        m1.setDurationMonths(1);
        m1.setActive(true);

        MembershipPlan m2 = new MembershipPlan();
        m2.setName("1 year");
        m2.setPrice(40000000D);
        m2.setDurationMonths(12);
        m2.setActive(true);

        MembershipPlan m3 = new MembershipPlan();
        m3.setName("6 months");
        m3.setPrice(2000000D);
        m3.setDurationMonths(6);
        m3.setActive(false);

        MembershipPlan m4 = new MembershipPlan();
        m4.setName("7 months");
        m4.setPrice(2000000D);
        m4.setDurationMonths(7);
        m4.setActive(true);

        membershipPlanService.save(m1);
        membershipPlanService.save(m2);
        membershipPlanService.save(m3);
        membershipPlanService.save(m4);
    }

    private void createAndSaveStandards(Metric metric) {
        List<Standard> standards = new ArrayList<>();
        if (metric.getName().equals("Weight")) {
            standards.add(createStandard(metric, 12, 16.0, 45.0));
            standards.add(createStandard(metric, 16, 100.0, 200.0));
            standards.add(createStandard(metric, 20, 250.0, 350.0));
            standards.add(createStandard(metric, 28, 1000.0, 1300.0));
            standards.add(createStandard(metric, 32, 1700.0, 2100.0));
            standards.add(createStandard(metric, 36, 2500.0, 3000.0));
            standards.add(createStandard(metric, 40, 3200.0, 3700.0));
        } else if (metric.getName().equals("Length")) {
            standards.add(createStandard(metric, 12, 5.0, 8.0));
            standards.add(createStandard(metric, 16, 11.0, 14.0));
            standards.add(createStandard(metric, 20, 16.0, 19.0));
            standards.add(createStandard(metric, 24, 21.0, 24.0));
            standards.add(createStandard(metric, 28, 25.0, 28.0));
            standards.add(createStandard(metric, 32, 29.0, 32.0));
            standards.add(createStandard(metric, 36, 33.0, 37.0));
        } else if (metric.getName().equals("Head Circumference")) {
            standards.add(createStandard(metric, 12, 5.0, 8.0));
            standards.add(createStandard(metric, 16, 10.0, 13.0));
            standards.add(createStandard(metric, 20, 15.0, 18.0));
            standards.add(createStandard(metric, 24, 19.0, 22.0));
            standards.add(createStandard(metric, 28, 23.0, 26.0));
            standards.add(createStandard(metric, 32, 27.0, 30.0));
            standards.add(createStandard(metric, 36, 31.0, 34.0));
            standards.add(createStandard(metric, 40, 35.0, 38.0));
        }

        for (Standard standard : standards) {
            standardService.save(standard);
            if (metric.getStandards() == null) {
                metric.setStandards(new ArrayList<>());
            }
        }

        metricService.save(metric);
    }

    private MyUser createUser(String email, String username, String password, boolean enabled, boolean verified, String role) {
        MyUser user = new MyUser();
        user.setEmail(email);
        user.setUsername(username);
        user.setPassword(password);
        user.setEnabled(enabled);
        user.setVerified(verified);
        user.setRole(role);
        return user;
    }

    private BlogPost createBlogPost(String title, String content, String imageUrl, String description, List<Tag> tags) {
        BlogPost post = new BlogPost();
        post.setPageTitle(title);
        post.setContent(content);
        post.setFeaturedImageUrl(imageUrl);
        post.setShortDescription(description);
        post.setUser(userService.findById(1L));
        tags.forEach(post::addTag);
        return post;
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
                for (int week = Math.max(12, currentWeek - 8); week <= currentWeek; week += 4) {
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


}