/*
 * Copyright 2015 zhanhb.
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
package a;

import cn.edu.zjnu.acm.judge.util.CopyHelper;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author zhanhb
 * @data 2015-3-16 1:27:16
 */
public class NewClass {

    private static final int[] problemIds = {
        1001, 1002, 1003, 1011, 1012, 1020, 1022, 1029, 1032,
        1033, 1035, 1038, 1039, 1041, 1042, 1043, 1051, 1053,
        1054, 1055, 1056, 1057,
        1064, 1071,
        1076, 1077, 1080,
        1081, 1082, 1084, 1090, 1091, 1092, 1095,
        1098, 1099, 1103, 1109, 1111, 1112, 1114, 1152, 1168, 1169, 1434, 1435, 1436
    };

    static {
        int last = 0;
        for (int problemid : problemIds) {
            if (last > problemid) {
                throw new IllegalArgumentException();
            }
            last = problemid;
        }
    }

    private static void copy(Connection conn, Connection conn2, int oldId) throws SQLException {
        int id;
        try (PreparedStatement ps = conn2.prepareStatement("select max(problem_id) as id from problem");
                ResultSet rs = ps.executeQuery()) {
            rs.next();
            id = Math.max(999, rs.getInt("id"));
        }

        try (PreparedStatement ps = conn.prepareStatement("select * from problem where problem_id=? order by problem_id")) {
            ps.setLong(1, oldId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String problem_id = rs.getString("problem_id");
                    String title = rs.getString("title");
                    String description = rs.getString("description");
                    String input = rs.getString("input");
                    String output = rs.getString("output");
//                        String input_path = rs.getString("input_path");
//                        String output_path = rs.getString("output_path");
                    String sample_input = rs.getString("sample_input");
                    String sample_output = rs.getString("sample_output");
                    String hint = rs.getString("hint");
                    String source = rs.getString("source");
                    String sample_Program = rs.getString("sample_Program");
                    String in_date = rs.getString("in_date");
                    String time_limit = rs.getString("time_limit");
                    String memory_limit = rs.getString("memory_limit");
                    String defunct = rs.getString("defunct");
                    String contest_id = rs.getString("contest_id");
                    String accepted = rs.getString("accepted");
                    String submit = rs.getString("submit");
                    String ratio = rs.getString("ratio");
                    String error = rs.getString("error");
                    String difficulty = rs.getString("difficulty");
                    String submit_user = rs.getString("submit_user");
                    String solved = rs.getString("solved");

                    try (PreparedStatement ps2 = conn2.prepareStatement("insert into problem("
                            + "problem_id,"
                            + "title,description,input,output,"
                            //                                + "input_path,output_path,"
                            + "sample_input,sample_output,hint,source,sample_Program,in_date,time_limit,memory_limit,defunct,contest_id,accepted,submit,ratio,error,difficulty,submit_user,solved) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?"
                            //                                + ",?,?"
                            + ")")) {
                        int i = 0;
                        ps2.setInt(++i, ++id);
                        ps2.setString(++i, title);
                        ps2.setString(++i, description);
                        ps2.setString(++i, input);
                        ps2.setString(++i, output);
//                            ps2.setString(++i, input_path);
//                            ps2.setString(++i, output_path);
                        ps2.setString(++i, sample_input);
                        ps2.setString(++i, sample_output);
                        ps2.setString(++i, hint);
                        ps2.setString(++i, source);
                        ps2.setString(++i, sample_Program);
                        ps2.setString(++i, in_date);
                        ps2.setString(++i, time_limit);
                        ps2.setString(++i, memory_limit);
                        ps2.setString(++i, defunct);
                        ps2.setString(++i, contest_id);
                        ps2.setString(++i, accepted);
                        ps2.setString(++i, submit);
                        ps2.setString(++i, ratio);
                        ps2.setString(++i, error);
                        ps2.setString(++i, difficulty);
                        ps2.setString(++i, submit_user);
                        ps2.setString(++i, solved);

                        ps2.executeUpdate();
                    }
                }
            }
        }
    }

    public static void main1(String[] args) throws SQLException, ClassNotFoundException {
        // log.info(Arrays.toString(problemids));
        for (int problemid : problemIds) {
            try (Connection src = DriverManager.getConnection("jdbc:mariadb://localhost/clanguage?characterEncoding=utf8", "root", "root");
                    Connection dest = DriverManager.getConnection("jdbc:mariadb://localhost/java?characterEncoding=utf8", "root", "root")) {
                copy(src, dest, problemid);
            }
        }
    }

    private static void doCopy(Path srcPath, Path destPath) throws IOException {
        CopyHelper.copy(srcPath, destPath, StandardCopyOption.COPY_ATTRIBUTES, StandardCopyOption.REPLACE_EXISTING);
    }

    public static void main2(String[] args) throws IOException {
        Path src = Paths.get("D:\\CLanguageData");
        Path dest = Paths.get("D:\\javaData");
        int newId = 1000;
        for (int problemId : problemIds) {
            Path a = src.resolve(problemId + "");
            Path b = dest.resolve("" + newId++);
            doCopy(a, b);
        }
    }

    public static void main(String[] args)
            throws SQLException, ClassNotFoundException, IOException {
        main1(args);
        main2(args);
    }
}
