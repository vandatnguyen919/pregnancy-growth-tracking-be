package com.pregnancy.edu.blog.tag;

import com.pregnancy.edu.blog.tag.converter.TagDtoToTagConverter;
import com.pregnancy.edu.blog.tag.converter.TagToTagDtoConverter;
import com.pregnancy.edu.blog.tag.dto.TagDto;
import com.pregnancy.edu.system.Result;
import com.pregnancy.edu.system.StatusCode;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tags")
public class TagController {

    private final TagService tagService;
    private final TagToTagDtoConverter toTagDtoConverter;
    private final TagDtoToTagConverter toTagConverter;

    public TagController(TagService tagService,
                         TagToTagDtoConverter toTagDtoConverter,
                         TagDtoToTagConverter toTagConverter) {
        this.tagService = tagService;
        this.toTagDtoConverter = toTagDtoConverter;
        this.toTagConverter = toTagConverter;
    }

    @GetMapping
    public Result getAllTags() {
        List<Tag> tags = tagService.findAll();
        List<TagDto> tagDtos = tags.stream()
                .map(toTagDtoConverter::convert)
                .toList();
        return new Result(true, StatusCode.SUCCESS, "Find All Success", tagDtos);
    }

    @GetMapping("/{tagId}")
    public Result getTagById(@PathVariable Long tagId) {
        Tag tag = tagService.findById(tagId);
        TagDto tagDto = toTagDtoConverter.convert(tag);
        return new Result(true, StatusCode.SUCCESS, "Find One Success", tagDto);
    }

    @PostMapping
    public Result addTag(@Valid @RequestBody TagDto tagDto) {
        Tag tag = toTagConverter.convert(tagDto);
        Tag savedTag = tagService.save(tag);
        TagDto savedTagDto = toTagDtoConverter.convert(savedTag);
        return new Result(true, StatusCode.SUCCESS, "Add Success", savedTagDto);
    }

    @PutMapping("/{tagId}")
    public Result updateTag(@PathVariable Long tagId, @Valid @RequestBody TagDto tagDto) {
        Tag tag = toTagConverter.convert(tagDto);
        Tag updatedTag = tagService.update(tagId, tag);
        TagDto updatedTagDto = toTagDtoConverter.convert(updatedTag);
        return new Result(true, StatusCode.SUCCESS, "Update Success", updatedTagDto);
    }

    @DeleteMapping("/{tagId}")
    public Result deleteTag(@PathVariable Long tagId) {
        tagService.delete(tagId);
        return new Result(true, StatusCode.SUCCESS, "Delete Success");
    }
}