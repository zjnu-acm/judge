package cn.edu.zjnu.acm.judge.domain;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
@SuppressWarnings("FinalClass")
public class UsernameChangeLog {
    String old;
    String newName;
}
