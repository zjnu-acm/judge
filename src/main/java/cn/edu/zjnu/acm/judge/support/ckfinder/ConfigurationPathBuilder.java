package cn.edu.zjnu.acm.judge.support.ckfinder;

import cn.edu.zjnu.acm.judge.config.JudgeConfiguration;
import com.ckfinder.connector.configuration.IBasePathBuilder;
import java.io.IOException;
import java.nio.file.Path;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ConfigurationPathBuilder implements IBasePathBuilder {

    private final JudgeConfiguration judgeConfiguration;

    @Override
    public String getBaseUrl(HttpServletRequest request) {
        return request.getContextPath().concat("/support/ckfinder.action?path=");
    }

    @Override
    public String getBaseDir(HttpServletRequest request) throws IOException {
        Path resource = judgeConfiguration.getUploadDirectory();
        return resource.toString();
    }

}
