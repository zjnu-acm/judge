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
    @JsonIgnore
    private String password;
    private String nick;
    private String school;
    private Long submit;
    private Long solved;
    private Instant accesstime;
    private String ip;
    private Instant createdTime;
    private Instant modifiedTime;
    private Boolean disabled;

    @JsonIgnore
    public double getRatio() {
        try {
            return Math.round(solved * 1000.0 / submit) / 10.0;
        } catch (NullPointerException ex) {
            return 0;
        }
    }

}
