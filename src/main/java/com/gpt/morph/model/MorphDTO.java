package com.gpt.morph.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class MorphDTO {
    private String pos;
    private String morph;
    private Integer start;
    private Integer end;

}
