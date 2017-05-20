package cn.edu.zjnu.acm.judge.domain;

import java.io.Serializable;
import java.nio.file.Path;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;

@Builder(builderClassName = "Builder", toBuilder = true)
@Data
@Setter(AccessLevel.PRIVATE)
public class RunRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    private Language language;
    private long submissionId;
    private long problemId;
    private long timeLimit;
    private long memoryLimit;
    private String source;
    private Path dataPath;
    private String userId;
    @Setter(AccessLevel.PUBLIC)
    private Path workDirectory;

}
