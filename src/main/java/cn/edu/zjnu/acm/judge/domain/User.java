package cn.edu.zjnu.acm.judge.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(builderClassName = "Builder", toBuilder = true)
@Data
@Setter(AccessLevel.PRIVATE)
@ToString(exclude = "password")
@NoArgsConstructor
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String email;
    private String vcode;
    @JsonIgnore
    private String password;
    private String nick;
    private String school;
    private long submit;
    private long solved;
    private Instant accesstime;
    private String ip;
    private Instant expireTime;
    private Instant createdTime;
    private Instant modifiedTime;

    @JsonIgnore
    public int getRatio() {
        return submit == 0 ? 0 : (int) Math.round(solved * 100.0 / submit);
    }

}
