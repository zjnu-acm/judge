package cn.edu.zjnu.acm.judge.support.ckfinder;

import cn.edu.zjnu.acm.judge.config.JudgeConfiguration;
import com.github.zhanhb.ckfinder.connector.configuration.IBasePathBuilder;
import java.nio.file.Path;
import javax.servlet.ServletContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ConfigurationPathBuilder implements IBasePathBuilder {

    private final JudgeConfiguration judgeConfiguration;
    private final ServletContext servletContext;

    @Override
    public String getBaseUrl() {
        return servletContext.getContextPath().concat("/support/ckfinder.action?path=");
    }

    @Override
    public String getBasePath() {
        Path resource = judgeConfiguration.getUploadDirectory();
        return resource.toString();
    }

}
