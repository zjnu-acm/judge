package cn.edu.zjnu.acm.judge.mapper;

import cn.edu.zjnu.acm.judge.data.dto.Standing;
import cn.edu.zjnu.acm.judge.domain.Problem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

@Mapper
public interface ContestProblemMapper {

    List<Problem> getProblems(
            @Param("contest") long contestId,
            @Nullable @Param("userId") String userId,
            @Nullable @Param("lang") String lang);

    List<Standing> standing(@Param("id") long contestId);

    @Nullable
    Problem getProblem(
            @Param("contest") long contestId,
            @Param("problem") long problemNum,
            @Nullable @Param("lang") String lang);

    long addProblem(@Param("id") long contestId, @Param("problem") long problem,
                    @Nullable @Param("title") String title);

    long addProblems(@Param("contestId") long contestId, @Param("base") int base, @Nonnull @Param("problems") long[] problems);

    long deleteByContest(@Param("contestId") long contestId);

}
