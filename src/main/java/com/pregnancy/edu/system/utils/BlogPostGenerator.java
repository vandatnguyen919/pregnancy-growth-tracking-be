package com.pregnancy.edu.system.utils;

import com.pregnancy.edu.blog.blogpost.BlogPost;
import com.pregnancy.edu.blog.tag.Tag;
import com.pregnancy.edu.myuser.MyUser;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BlogPostGenerator {

    public static void main(String[] args) {
        // Create some users for association with blog posts
        List<MyUser> users = MyUserGenerator.generateRandomUsers(10);
        
        // Create some tags for association with blog posts
        List<Tag> tags = TagGenerator.generateSampleTags();
        
        // Generate 15 sample blog posts
        List<BlogPost> blogPosts = generateSampleBlogPosts(users, tags);
        
        // Print the blog posts
        for (BlogPost post : blogPosts) {
            System.out.println("Blog Post ID: " + post.getId());
            System.out.println("Heading: " + post.getHeading());
            System.out.println("Author: " + post.getUser().getFullName());
            System.out.println("Short Description: " + post.getShortDescription());
            System.out.println("Tags: " + post.getTags().stream().map(Tag::getName).toList());
            System.out.println("Published: " + post.getPublishedDate());
            System.out.println("Visible: " + post.isVisible());
            System.out.println("URL: " + post.getUrlHandle());
            System.out.println("--------------------------------------");
        }
    }
    
    public static List<BlogPost> generateSampleBlogPosts(List<MyUser> users, List<Tag> tags) {
        List<BlogPost> blogPosts = new ArrayList<>();
        Random random = new Random();
        
        // Sample blog post data
        String[] headings = {
            "Understanding Pregnancy Nutrition: What Every Mother Needs to Know",
            "The First Trimester: Navigating Early Pregnancy Challenges",
            "Exercise During Pregnancy: Safe Workouts for Each Trimester",
            "Managing Pregnancy Symptoms Naturally",
            "Preparing for Childbirth: A Comprehensive Guide",
            "Postpartum Recovery: What to Expect and How to Cope",
            "Baby Development: Month by Month Guide",
            "Breastfeeding Tips for New Mothers",
            "Sleep Training Methods for Infants",
            "Balancing Career and Motherhood",
            "Nutrition Guide for Nursing Mothers",
            "Common Health Concerns in Newborns",
            "Choosing the Right Pediatrician",
            "Creating a Safe Nursery Environment",
            "Developmental Milestones in the First Year"
        };
        
        String[] shortDescriptions = {
            "Discover the essential nutrients needed during pregnancy and how to incorporate them into your diet.",
            "Learn about the common challenges faced during the first trimester and effective ways to manage them.",
            "Find safe and effective workout routines tailored for each stage of pregnancy.",
            "Natural remedies and lifestyle changes to help manage common pregnancy symptoms without medication.",
            "Everything you need to know to prepare for labor and delivery, from birth plans to hospital bags.",
            "A guide to physical and emotional recovery after childbirth, including self-care strategies.",
            "Track your baby's development with this comprehensive month-by-month guide of milestones and changes.",
            "Practical advice and techniques to make breastfeeding successful and comfortable.",
            "Different approaches to helping your baby develop healthy sleep patterns and habits.",
            "Strategies for managing professional responsibilities while adjusting to motherhood.",
            "Essential dietary guidelines for nursing mothers to support milk production and baby's health.",
            "Identifying and addressing common health issues that affect newborns in the first months.",
            "Important factors to consider when selecting a pediatrician for your child's healthcare needs.",
            "Tips for creating a nursery that's both beautiful and safe for your new arrival.",
            "Understanding and supporting your baby's developmental progress throughout the first year."
        };
        
        String[] contents = {
            "During pregnancy, nutrition becomes more important than ever. Your body needs additional nutrients to support your growing baby while maintaining your own health. This comprehensive guide covers the essential vitamins and minerals needed during pregnancy, including folic acid, iron, calcium, and omega-3 fatty acids. We'll explore food sources for these nutrients and provide sample meal plans for each trimester. Additionally, we'll discuss common nutritional concerns like morning sickness and food aversions, offering practical solutions to ensure adequate nutrition despite these challenges.",
            "The first trimester is often the most challenging period of pregnancy for many women. This article explores the physical and emotional changes that occur during weeks 1-12, including morning sickness, fatigue, and mood swings. We provide evidence-based strategies for managing these symptoms and maintaining your wellbeing. The article also covers important first prenatal appointments, early pregnancy tests, and when to announce your pregnancy to family, friends, and colleagues. Finally, we address common concerns and anxieties during this critical developmental period.",
            "Regular exercise during pregnancy offers numerous benefits, including improved mood, better sleep, and preparation for childbirth. This guide outlines safe workout routines specifically designed for each trimester, taking into account your changing body and energy levels. We cover cardio, strength training, and flexibility exercises, with modifications to accommodate your growing belly. The article also discusses exercises to avoid, warning signs to stop activity, and how to maintain proper form to prevent injury. Includes testimonials from fitness experts specializing in prenatal exercise.",
            "Many women prefer to manage pregnancy symptoms naturally whenever possible. This comprehensive article explores evidence-based natural remedies for common pregnancy complaints including morning sickness, heartburn, back pain, swelling, and insomnia. We discuss dietary modifications, herbal remedies (with safety information), acupressure techniques, and lifestyle adjustments that can provide relief. Each recommendation includes scientific research when available and practical implementation tips. The article emphasizes consulting healthcare providers before trying any new remedy during pregnancy.",
            "Preparing for childbirth involves both physical and psychological readiness. This guide walks you through creating a birth plan, packing a hospital bag, and understanding the stages of labor. We explain different pain management options, from natural techniques to medical interventions, helping you make informed decisions that align with your preferences. The article also covers potential complications and interventions, reducing anxiety through knowledge. Finally, we provide partner-focused guidance to help your support person be maximally effective during labor and delivery."
        };
        
        String[] imageUrls = {
            "https://www.motherhoodcenter.com/wp-content/uploads/2015/09/Pregnant-Woman.jpg",
            "https://images.herzindagi.info/image/2024/May/dos-and-donts-for-pregnant-women-2.jpg",
            "https://media.istockphoto.com/id/873472660/photo/regnant-woman-drinking-milk-nd-touching-her-belly-with-care.jpg?s=612x612&w=0&k=20&c=38zQtf43AyK-GginTMjFYFboec7cpE70tJUihN36lQw=",
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSJbmhI333IKBQ6Z3NmJ76c8R0ullwBFaXXPg&s",
            "https://t4.ftcdn.net/jpg/08/51/43/93/360_F_851439355_tXPcfoKQkPAQh0yyf7xAYzthp0hVXUHI.jpg",
            "https://cdn.richmondmom.com/wp-content/uploads/2023/09/When-Does-Pregnancy-Start-To-Show-768x430.webp",
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQEF-uQB7GLxcUSTMYZAHdk8ymza0Ye6mheVwExLWap_6lSi6HF2EMwA2m1ROxoHzr0UuQ&usqp=CAU",
            "https://thumbs.dreamstime.com/b/lesbian-couple-waiting-baby-woman-posing-her-pregnant-girlfriend-lesbian-couple-waiting-baby-women-posing-her-202534462.jpg",
            "https://thumbs.dreamstime.com/b/pregnant-mother-daughter-resting-bed-young-woman-her-first-child-second-pregnancy-motherhood-parenting-210154666.jpg",
            "https://img.freepik.com/free-photo/young-good-looking-blonde-pregnant-woman-grey-home-clothes-lying-comfy-bed-looking-video-online-about-healthy-pregnancy-period-with-interest-concentrated-expression_176420-12764.jpg?ga=GA1.1.679745334.1739001825&semt=ais_hybrid",
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRiDPiAQj7iW8BazAFL4Ik5Tp4rAoeZlZd4KqQV3lEmAruTfiimqwbEjDWq-kB-I_f-LmM&usqp=CAU",
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS_kCiPpRnXuXrX_AL1D2_EWbhaZx_h78DBPb1RRVoN0agqwu8vnLAH5gkxIbGfC42jEmQ&usqp=CAU",
            "https://img.freepik.com/free-photo/front-view-family-staying-together-sofa_23-2148252539.jpg",
            "https://img.freepik.com/free-photo/pregnant-woman-lying-coach-home_1303-27119.jpg",
            "https://lirp.cdn-website.com/6668b3a2/dms3rep/multi/opt/shutterstock_2479444533-14817b98-640w.jpg"
        };
        
        // Generate blog posts
        for (int i = 0; i < headings.length; i++) {
            BlogPost post = new BlogPost();
            post.setHeading(headings[i]);
            post.setPageTitle(headings[i] + " | Pregnancy Education Blog");
            
            // Assign content - use predefined content if available, otherwise use the heading
            if (i < contents.length) {
                post.setContent(contents[i]);
            } else {
                post.setContent("Detailed content for the article about " + headings[i].toLowerCase() + 
                               ". This would typically be a long-form article with multiple sections, " +
                               "expert quotes, and evidence-based information to educate readers.");
            }
            
            post.setShortDescription(shortDescriptions[i]);
            post.setFeaturedImageUrl(imageUrls[i]);
            
            // Create URL handle from heading
            String urlHandle = headings[i].toLowerCase()
                    .replaceAll("[^a-z0-9\\s]", "")
                    .replaceAll("\\s+", "-");
            post.setUrlHandle(urlHandle);
            
            // 80% of posts are visible
            post.setVisible(random.nextDouble() > 0.2);
            
            // Set published and updated dates
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime publishedDate = now.minusDays(random.nextInt(180)); // Within last 6 months
            post.setPublishedDate(publishedDate);
            
            // Updated date is after published date
            int daysAfterPublish = random.nextInt(30); // Updated within 30 days of publishing
            post.setUpdatedDate(publishedDate.plusDays(daysAfterPublish));
            
            // Assign a random user as author
            post.setUser(users.get(random.nextInt(users.size())));
            
            // Assign 1-4 random tags
            int numTags = random.nextInt(4) + 1;
            List<Tag> shuffledTags = new ArrayList<>(tags);
            java.util.Collections.shuffle(shuffledTags);
            
            for (int t = 0; t < numTags && t < shuffledTags.size(); t++) {
                post.addTag(shuffledTags.get(t));
            }
            
            blogPosts.add(post);
        }
        
        return blogPosts;
    }
}