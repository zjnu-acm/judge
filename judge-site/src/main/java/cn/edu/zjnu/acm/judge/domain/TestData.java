package cn.edu.zjnu.acm.judge.domain;

import lombok.*;

@AllArgsConstructor
@Builder(builderClassName = "Builder", toBuilder = true)
@Data
@NoArgsConstructor
public class TestData {

    private String testIn;
    private String testOut;

}
