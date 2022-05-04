package cn.edu.zjnu.acm.judge.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class FileUtils {

    public static String readFile(String src) throws IOException {
        try (InputStream is = Files.newInputStream(Paths.get(src));
             BufferedReader in = new BufferedReader(new InputStreamReader(is))) {
            StringBuilder buffer = new StringBuilder();
            String line = "";
            if ((line = in.readLine()) != null) {
                buffer.append(line);
            }
            while ((line = in.readLine()) != null) {
                buffer.append("\n").append(line);
            }
            return buffer.toString();
        }
    }

    public static List<String> readMultipartFile(MultipartFile file) throws IOException {
        List<String> stringList = new ArrayList<>();
        try (InputStream inputStream = file.getInputStream();
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringList.add(line);
            }
        }
        return stringList;
    }

    public static void writeIOFile(String dirPath, int idx, String str, boolean isIn) throws IOException {
        Path dir = Paths.get(dirPath);
        Files.createDirectories(dir);
        Path path = dir.resolve( idx + (isIn ? ".in" : ".out"));
        Files.write(path, str.getBytes(StandardCharsets.UTF_8));
    }

    public static boolean writeImgFile(String dirPath, String base64, String imgName) throws IOException {
        Path dir = Paths.get(dirPath);
        Files.createDirectories(dir);
        try {
            byte[] bytes = Base64.getDecoder().decode(base64);
            try (OutputStream out = Files.newOutputStream(dir.resolve(imgName))) {
                out.write(bytes);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static String writeTmpFile(String dirPath, MultipartFile multipartFile) throws IOException {
        Path dir = Paths.get(dirPath);
        Files.createDirectories(dir);
        String originalFilename = multipartFile.getOriginalFilename();
        System.out.println(originalFilename);
        Path path = dir.resolve(originalFilename);
        multipartFile.transferTo(path.toFile());
        return path.toRealPath().toString();
    }

    public static boolean deleteTmpFile(String filePath) {
        File file = new File(filePath);
        if(file.delete()){
            System.out.println("删除成功");
            return true;
        } else {
            System.out.println("删除失败");
            return false;
        }
    }

}
