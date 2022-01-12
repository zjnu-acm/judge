/*
 * Copyright 2019 ZJNU ACM.
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
package cn.edu.zjnu.acm.judge.support;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author zhanhb
 */
public class JudgeData {

    private final Path[][] data;

    public static JudgeData parse(Path dataDirectory) throws IOException {
        if (!Files.isDirectory(dataDirectory)) {
            throw new NoDataException("data directory not exists");
        }
        List<Path[]> files = new ArrayList<>(20);
        try (DirectoryStream<Path> listFiles = Files.newDirectoryStream(dataDirectory)) {
            for (Path inFile : listFiles) {
                String inFileName = inFile.getFileName().toString();
                if (!inFileName.toLowerCase().endsWith(".in")) {
                    continue;
                }
                Path outFile = dataDirectory.resolve(inFileName.substring(0, inFileName.length() - 3) + ".out");
                if (!Files.exists(outFile)) {
                    continue;
                }
                files.add(new Path[]{inFile, outFile});//统计输入,输出文件
            }
        }
        int caseNum = files.size();
        if (caseNum == 0) {
            throw new NoDataException("No test cases found in specified directory");
        }
        return new JudgeData(files.toArray(new Path[caseNum][]));
    }

    private JudgeData(Path[][] data) {
        this.data = data;
    }

    public int getCaseCount() {
        return data.length;
    }

    public Path[] get(int index) {
        return data[index].clone();
    }

    @Override
    public String toString() {
        return "JudgeData{data=" + Arrays.deepToString(data) + '}';
    }

}
