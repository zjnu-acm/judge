package cn.edu.zjnu.acm.judge.support.ckfinder;

import cn.edu.zjnu.acm.judge.config.JudgeConfiguration;
import com.ckfinder.connector.configuration.IBasePathBuilder;
import javax.servlet.http.HttpServletRequest;

public class ConfigurationPathBuilder implements IBasePathBuilder {

    @Override
    public String getBaseUrl(HttpServletRequest request) {
        return request.getContextPath().concat("/support/ckfinder.action?path=");
    }

    @Override
    public String getBaseDir(HttpServletRequest request) {
        return JudgeConfiguration.getUploadDirectory().toString();
    }

}
