package cn.edu.zjnu.acm.judge.util;

import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {

    public static String readFile(String src) throws IOException {
        InputStream is = new FileInputStream(src);
        BufferedReader in = new BufferedReader(new InputStreamReader(is));
        StringBuffer buffer = new StringBuffer();
        String line = "";
        if ((line = in.readLine()) != null) {
            buffer.append(line);
        }
        while ((line = in.readLine()) != null){
            buffer.append("\n" + line);
        }
        return buffer.toString();
    }

    public static List<String> readMultipartFile(MultipartFile file) throws IOException {
        FileInputStream inputStream = null;
        BufferedReader bufferedReader = null;
        List<String> stringList = new ArrayList<>();
        try{
            inputStream = (FileInputStream) file.getInputStream();
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            String line = null;
            while((line = bufferedReader.readLine()) != null){
                stringList.add(line);
            }
        } finally {
            bufferedReader.close();
            inputStream.close();
        }
        return stringList;
    }

    public static void writeIOFile(String dirPath, int idx, String str, boolean isIn) throws IOException {
        File dir = new File(dirPath);
        if (!dir.exists()){
            dir.mkdirs();
        }
        File file = new File(dirPath + "/" + idx + (isIn?".in":".out"));
        if (file.exists()){
            file.delete();
        }
        file.createNewFile();
        byte bytes[] = new byte[512];
        bytes = str.getBytes();
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(bytes);
        fos.flush();
        fos.close();
    }

    public static boolean writeImgFile(String dirPath, String base64, String imgName) throws IOException {
        File dir = new File(dirPath);
        if (!dir.exists()){
            dir.mkdirs();
        }
        BASE64Decoder decoder = new BASE64Decoder();
        try{
            byte[] bytes = decoder.decodeBuffer(base64);
            for(int i = 0; i < bytes.length; i++){
                if (bytes[i] < 0){
                    bytes[i] += 256;
                } // 调整异常数据
            }
            OutputStream out = new FileOutputStream(dirPath + "/" + imgName);
            out.write(bytes);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static String writeTmpFile(String dirPath, MultipartFile multipartFile) throws IOException {
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        System.out.println(multipartFile.getOriginalFilename());
        File file = new File(dirPath + "/" + multipartFile.getOriginalFilename());
        multipartFile.transferTo(file);
        return file.getAbsolutePath();
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
