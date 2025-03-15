package com.pregnancy.edu.system;

import com.pregnancy.edu.blog.blogpost.BlogPost;
import com.pregnancy.edu.blog.blogpost.BlogPostService;
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
import com.pregnancy.edu.system.utils.generators.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
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

        // Metrics & Standards
        List<Metric> metrics = MetricGenerator.generateSampleMetrics();
        for (Metric metric : metrics) {
            metricService.save(metric);
        }

        // Create user data
        List<MyUser> users = new ArrayList<>();
        MyUser u1 = MyUserGenerator.createJohn();
        MyUser u2 = MyUserGenerator.createEric();
        MyUser u3 = MyUserGenerator.createTom();

        users.add(u1);
        users.add(u2);
        users.add(u3);
        users.addAll(MyUserGenerator.generateRandomUsers(10));

        List<MyUser> savedUsers = new ArrayList<>();
        for (MyUser user : users) {
            MyUser savedUser = userService.save(user);
            savedUsers.add(savedUser);
        }

        // Create pregnancies
        List<Pregnancy> savedPregnancies = new ArrayList<>();
        for (Pregnancy pregnancy : PregnancyGenerator.generateSamplePregnancy(savedUsers)) {
            Pregnancy savedPregnancy = pregnancyService.save(pregnancy);
            savedPregnancies.add(savedPregnancy);
        }

        for (Fetus fetus : FetusGenerator.generateSampleFetus(savedPregnancies)) {
            Fetus savedFetus = fetusService.save(fetus);
            List<FetusMetric> fetusMetrics = FetusMetricGenerator.generateSampleFetusMetrics(savedFetus, metrics);
            for (FetusMetric fetusMetric : fetusMetrics) {
                fetusMetricService.save(fetusMetric);
            }
        }

        // Create tags
        List<Tag> tags = TagGenerator.generateSampleTags();
        for (Tag tag : tags) {
            tagService.save(tag);
        }

        // Create blog posts
        List<BlogPost> blogPosts = BlogPostGenerator.generateSampleBlogPosts(users, tags);
        for (BlogPost blogPost : blogPosts) {
            blogPostService.save(blogPost);
        }

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
}