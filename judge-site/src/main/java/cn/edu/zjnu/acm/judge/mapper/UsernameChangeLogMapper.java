package cn.edu.zjnu.acm.judge.mapper;

import cn.edu.zjnu.acm.judge.domain.UsernameChangeLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author zhanhb
 */
@Mapper
public interface UsernameChangeLogMapper {

    long save(UsernameChangeLog usernameChangeLog);

}
