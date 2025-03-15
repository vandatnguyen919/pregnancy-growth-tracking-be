package com.pregnancy.edu.system.utils.generators;

import com.pregnancy.edu.blog.tag.Tag;

import java.util.ArrayList;
import java.util.List;

public class TagGenerator {

    public static List<Tag> generateSampleTags() {
        List<Tag> tags = new ArrayList<>();

        String[] tagNames = {
                "Pregnancy", "First Trimester", "Second Trimester", "Third Trimester",
                "Nutrition", "Exercise", "Mental Health", "Childbirth", "Postpartum",
                "Breastfeeding", "Baby Development", "Sleep", "Work-Life Balance",
                "Health Concerns", "Safety", "Parenting"
        };

        for (String tagName : tagNames) {
            Tag tag = new Tag();
            tag.setName(tagName);
//            tag.setDisplayName(tagNames[i]);
            tags.add(tag);
        }

        return tags;
    }
}
