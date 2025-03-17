package com.pregnancy.edu.fetusinfo.fetus.dto;

public record FetusDto(
        Long id,
        Long userId,
        Long pregnancyId,
        String nickName,
        String gender
) {
}