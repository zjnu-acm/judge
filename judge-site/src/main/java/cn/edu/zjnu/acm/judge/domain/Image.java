package cn.edu.zjnu.acm.judge.domain;

import lombok.*;

@AllArgsConstructor
@Builder(builderClassName = "Builder", toBuilder = true)
@Data
@NoArgsConstructor
public class Image {

    private String imgSrc;
    private String base64Src;

}
