package cn.edu.zjnu.acm.judge.support;

import cn.edu.zjnu.acm.judge.domain.Language;
import java.io.Serializable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;

/**
 * @author zhanhb
 */
@Builder(builderClassName = "Builder", toBuilder = true)
@Data
@Setter(AccessLevel.PRIVATE)
public class RunRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    private Language language;
    private long timeLimit;
    private long memoryLimit;
    private String source;

}
