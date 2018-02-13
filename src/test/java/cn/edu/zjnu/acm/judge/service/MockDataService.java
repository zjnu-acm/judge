/*
 * Copyright 2017 ZJNU ACM.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.edu.zjnu.acm.judge.service;

import cn.edu.zjnu.acm.judge.data.form.SubmissionQueryForm;
import cn.edu.zjnu.acm.judge.domain.Contest;
import cn.edu.zjnu.acm.judge.domain.Problem;
import cn.edu.zjnu.acm.judge.domain.Submission;
import cn.edu.zjnu.acm.judge.domain.User;
import cn.edu.zjnu.acm.judge.mapper.SubmissionMapper;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import javax.annotation.Nonnull;
import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static org.junit.Assume.assumeTrue;

/**
 *
 * @author zhanhb
 */
@Service
public class MockDataService {

    private final Function<String, String> idGenerator;

    @Autowired
    private AccountService accountService;
    @Autowired
    private ProblemService problemService;
    @Autowired
    private ContestService contestService;
    @Autowired
    private SubmissionService submissionService;
    @Autowired
    private SubmissionMapper submissionMapper;

    public MockDataService() {
        final AtomicLong seed = new AtomicLong(System.currentTimeMillis());
        idGenerator = type -> type + seed.incrementAndGet();
    }

    @Nonnull
    public User user() {
        return user(true);
    }

    @Nonnull
    public User user(boolean create) {
        String userId = idGenerator.apply("user");
        User user = User.builder().id(userId).password(userId).school("").nick(userId).build();
        if (create) {
            accountService.save(user);
        }
        return user;
    }

    @Nonnull
    public Problem problem() {
        return problem(true);
    }

    @Nonnull
    public Problem problem(Function<Problem.Builder, Problem.Builder> function) {
        return problem(function, true);
    }

    @Nonnull
    public Problem problem(boolean create) {
        return problem(Function.identity(), create);
    }

    @Nonnull
    public Problem problem(Function<Problem.Builder, Problem.Builder> function, boolean create) {
        Objects.requireNonNull(function);
        Problem problem = function.apply(Problem.builder()
                .title("")
                .description("")
                .input("")
                .output("")
                .sampleInput("")
                .sampleOutput("")
                .hint("")
                .source("")
                .timeLimit(1000L)
                .memoryLimit(65536 * 1024L)
                .contests(new long[]{0}))
                .build();
        if (create) {
            problemService.save(problem);
        }
        return problem;
    }

    @Nonnull
    public Contest contest() {
        return contest(true);
    }

    @Nonnull
    public Contest contest(boolean create) {
        Instant now = Instant.now();
        Contest contest = Contest.builder()
                .startTime(now.minus(1, ChronoUnit.HOURS))
                .endTime(now.plus(1, ChronoUnit.HOURS))
                .title("test title")
                .description("test description")
                .build();
        if (create) {
            contestService.save(contest);
        }
        return contest;
    }

    public Submission submission(int languageId, String source, String userId, String ip, long problemId) {
        assumeTrue("not windows", Platform.isWindows());
        submissionService.submit(languageId, source, userId, ip, problemId);
        return submissionMapper.findAllByCriteria(SubmissionQueryForm.builder().user(userId).size(1).build()).iterator().next();
    }

    public Submission submission() throws IOException {
        String userId = user().getId();
        long problemId = problem().getId();
        String source = Lazy.SAMPLE_SOURCE;
        return submission(0, source, userId, "::1", problemId);
    }

    @SuppressWarnings("UtilityClassWithoutPrivateConstructor")
    private static class Lazy {

        static String SAMPLE_SOURCE;

        static {
            try {
                SAMPLE_SOURCE = new String(IOUtils.toByteArray(MockDataService.class.getResourceAsStream("/sample/program/ac/accept.cpp")), StandardCharsets.UTF_8);
            } catch (IOException ex) {
                throw new ExceptionInInitializerError(ex);
            }
        }
    }

}
