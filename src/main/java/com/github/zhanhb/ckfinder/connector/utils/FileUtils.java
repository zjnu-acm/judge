/*
 * CKFinder
 * ========
 * http://cksource.com/ckfinder
 * Copyright (C) 2007-2015, CKSource - Frederico Knabben. All rights reserved.
 *
 * The software, this file and its contents are subject to the CKFinder
 * License. Please read the license.txt file before using, installing, copying,
 * modifying or distribute this file or part of its contents. The contents of
 * this file is part of the Source Code of CKFinder.
 */
package com.github.zhanhb.ckfinder.connector.utils;

import cn.edu.zjnu.acm.judge.util.DeleteHelper;
import com.github.zhanhb.ckfinder.connector.configuration.Constants;
import com.github.zhanhb.ckfinder.connector.configuration.IConfiguration;
import com.github.zhanhb.ckfinder.connector.data.ResourceType;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.Part;

/**
 * Utils for files.
 *
 */
@SuppressWarnings("UtilityClassWithoutPrivateConstructor")
public class FileUtils {

    /**
     * max read file buffer size.
     */
    private static final int MAX_BUFFER_SIZE = 1024;
    private static final Pattern invalidFileNamePatt = Pattern.compile(Constants.INVALID_FILE_NAME_REGEX);

    /**
     * Gets list of children folder or files for dir, according to searchDirs
     * param.
     *
     * @param dir folder to search.
     * @param searchDirs if true method return list of folders, otherwise list
     * of files.
     * @return list of files or subdirectories in selected directory
     * @throws java.io.IOException
     */
    public static List<String> findChildrensList(Path dir,
            boolean searchDirs) throws IOException {
        @SuppressWarnings("CollectionWithoutInitialCapacity")
        List<String> files = new ArrayList<>();
        for (Path file : Files.newDirectoryStream(dir, file -> searchDirs == Files.isDirectory(file))) {
            files.add(file.getFileName().toString());
        }
        return files;
    }

    /**
     * Gets file extension.
     *
     * @param fileName name of file.
     * @param shortExtensionMode
     * @return file extension
     */
    public static String getFileExtension(String fileName, boolean shortExtensionMode) {
        if (shortExtensionMode) {
            return FileUtils.getFileExtension(fileName);
        }
        if (fileName == null
                || fileName.indexOf('.') == -1
                || fileName.indexOf('.') == fileName.length() - 1) {
            return null;
        }
        return fileName.substring(fileName.indexOf('.') + 1);
    }

    /**
     * Gets file last extension.
     *
     * @param fileName name of file.
     * @return file extension
     */
    public static String getFileExtension(String fileName) {
        if (fileName == null
                || fileName.lastIndexOf('.') == -1
                || fileName.lastIndexOf('.') == fileName.length() - 1) {
            return null;
        }
        return fileName.substring(fileName.lastIndexOf('.') + 1);
    }

    /**
     * Gets file name without its extension.
     *
     * @param fileName name of file
     * @param shortExtensionMode
     * @return file extension
     */
    public static String getFileNameWithoutExtension(String fileName, boolean shortExtensionMode) {
        if (shortExtensionMode) {
            return FileUtils.getFileNameWithoutExtension(fileName);
        }

        if (fileName == null || fileName.indexOf('.') == -1) {
            return null;
        }
        return fileName.substring(0, fileName.indexOf('.'));
    }

    /**
     * Gets file name without its last extension.
     *
     * @param fileName name of file
     * @return file extension
     */
    public static String getFileNameWithoutExtension(String fileName) {
        if (fileName == null || fileName.lastIndexOf('.') == -1) {
            return null;
        }
        return fileName.substring(0, fileName.lastIndexOf('.'));
    }

    /**
     * Print file content to outputstream.
     *
     * @param file file to be printed.
     * @param out outputstream.
     * @throws IOException when io error occurs.
     */
    public static void printFileContentToResponse(Path file,
            OutputStream out) throws IOException {
        Files.copy(file, out);
    }

    /**
     *
     * @param sourceFile source file
     * @param destFile destination file
     * @param move if source file should be deleted.
     * @param conf connector configuration
     * @return true if file moved/copied correctly
     * @throws IOException when IOerror occurs
     */
    public static boolean copyFromSourceToDestFile(Path sourceFile,
            Path destFile,
            boolean move,
            IConfiguration conf)
            throws IOException {
        createPath(destFile, true);
        if (move) {
            Files.move(sourceFile, destFile, StandardCopyOption.REPLACE_EXISTING);
        } else {
            Files.copy(sourceFile, destFile, StandardCopyOption.REPLACE_EXISTING);
        }
        return true;

    }

    /**
     * Parse date with pattern yyyyMMddHHmm. Pattern is used in get file command
     * response XML.
     *
     * @param file input file.
     * @return parsed file modification date.
     * @throws java.io.IOException
     */
    public static String parseLastModifDate(Path file) throws IOException {
        Date date = Date.from(Files.getLastModifiedTime(file).toInstant());
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmm");
        return dateFormat.format(date);
    }

    /**
     * check if dirname matches configuration hidden folder regex.
     *
     * @param dirName dir name
     * @param conf connector configuration
     * @return true if matches.
     */
    public static boolean checkIfDirIsHidden(String dirName,
            IConfiguration conf) {
        if (dirName == null || dirName.isEmpty()) {
            return false;
        }
        String dir = PathUtils.removeSlashFromEnd(PathUtils.escape(dirName));
        StringTokenizer sc = new StringTokenizer(dir, "/");
        while (sc.hasMoreTokens()) {
            boolean check = Pattern.compile(getHiddenFileOrFolderRegex(
                    conf.getHiddenFolders())).matcher(sc.nextToken()).matches();
            if (check) {
                return true;
            }
        }
        return false;
    }

    /**
     * check if filename matches configuration hidden file regex.
     *
     * @param fileName file name
     * @param conf connector configuration
     * @return true if matches.
     */
    public static boolean checkIfFileIsHidden(String fileName,
            IConfiguration conf) {
        return Pattern.compile(getHiddenFileOrFolderRegex(
                conf.getHiddenFiles())).matcher(fileName).matches();
    }

    /**
     * get hidden folder regex pattern.
     *
     * @param hiddenList list of hidden file or files patterns.
     * @return full folder regex pattern
     */
    private static String getHiddenFileOrFolderRegex(List<String> hiddenList) {
        StringBuilder sb = new StringBuilder("(");
        for (String item : hiddenList) {
            if (sb.length() > 3) {
                sb.append("|");
            }

            sb.append("(");
            sb.append(item.replace(".", "\\.").replace("*", ".+").replace("?", "."));
            sb.append(")");
        }
        sb.append(")+");
        return sb.toString();
    }

    /**
     * deletes file or folder with all subfolders and subfiles.
     *
     * @param file file or directory to delete.
     * @return true if all files are deleted.
     */
    public static boolean delete(Path file) {
        try {
            DeleteHelper.delete(file);
            return true;
        } catch (IOException ex) {
            return false;
        }
    }

    /**
     * check if file or folder name doesn't match invalid name.
     *
     * @param fileName file name
     * @return true if file name is correct
     */
    public static boolean checkFileName(String fileName) {
        return !(fileName == null || fileName.isEmpty()
                || fileName.charAt(fileName.length() - 1) == '.'
                || fileName.contains("..")
                || checkFolderNamePattern(fileName));
    }

    /**
     * check if new folder name contains disallowed chars.
     *
     * @param fileName file name
     * @return true if it does contain disallowed characters.
     */
    private static boolean checkFolderNamePattern(String fileName) {
        return invalidFileNamePatt.matcher(fileName).find();
    }

    /**
     * checks if file extension is on denied list or isn't on allowed list.
     *
     * @param fileName filename
     * @param type resource type
     * @return 0 if ok, 1 if not ok, 2 if rename required
     */
    public static int checkFileExtension(String fileName,
            ResourceType type) {
        if (type == null || fileName == null) {
            return 1;
        }

        if (fileName.indexOf('.') == -1) {
            return 0;
        }

        return checkSingleExtension(getFileExtension(fileName), type) ? 0 : 1;
    }

    /**
     * Checks whether files extension is allowed.
     *
     * @param fileExt a string representing file extension to test
     * @param type a {@code ResourceType} object holding list of allowed and
     * denied extensions against which parameter fileExt will be tested
     * @return {@code true} is extension is on allowed extensions list or if
     * allowed extensions is empty. The {@code false} is returned when file is
     * on denied extensions list or if none of the above conditions is met.
     */
    private static boolean checkSingleExtension(String fileExt,
            ResourceType type) {
        StringTokenizer scanner = new StringTokenizer(type.getDeniedExtensions(), ",");
        while (scanner.hasMoreTokens()) {
            if (scanner.nextToken().equalsIgnoreCase(fileExt)) {
                return false;
            }
        }

        scanner = new StringTokenizer(type.getAllowedExtensions(), ",");
        //The allowedExtensions is empty. Allow everything that isn't dissallowed.
        if (!scanner.hasMoreTokens()) {
            return true;
        }

        while (scanner.hasMoreTokens()) {
            if (scanner.nextToken().equalsIgnoreCase(fileExt)) {
                return true;
            }
        }
        return false;
    }

    /**
     * converts filename to ASCII.
     *
     * @param fileName file name
     * @return encoded file name
     */
    public static String convertToASCII(String fileName) {
        String newFileName = fileName;
        for (Map.Entry<String, String> entry : Utf8AccentsHolder.UTF8_LOWER_ACCENTS.entrySet()) {
            newFileName = newFileName.replace(entry.getKey(), entry.getValue());
        }
        for (Map.Entry<String, String> entry : Utf8AccentsHolder.UTF8_UPPER_ACCENTS.entrySet()) {
            newFileName = newFileName.replace(entry.getKey(), entry.getValue());
        }
        return newFileName;
    }

    /**
     * creates file and all above folders that do not exist.
     *
     * @param file file to create.
     * @param asFile if it is path to folder.
     * @throws IOException when io error occurs.
     */
    public static void createPath(Path file, boolean asFile) throws IOException {
        if (asFile) {
            Path path = file.toAbsolutePath();
            Path dir = path.getParent();
            if (dir != null) {
                Files.createDirectories(dir);
            }
            Files.createFile(path);
        } else {
            Files.createDirectories(file);
        }
    }

    /**
     * check if file size isn't bigger then max size for type.
     *
     * @param type resource type
     * @param fileSize file size
     * @return true if file size isn't bigger then max size for type.
     */
    public static boolean checkFileSize(ResourceType type, long fileSize) {
        final long maxSize = type.getMaxSize();
        return (maxSize == 0 || maxSize > fileSize);
    }

    /**
     * check if file has html file extension.
     *
     * @param file file name
     * @param configuration connector configuration
     * @return true if has
     */
    public static boolean checkIfFileIsHtmlFile(String file,
            IConfiguration configuration) {

        return configuration.getHTMLExtensions().contains(
                getFileExtension(file).toLowerCase());

    }

    /**
     * Detect HTML in the first KB to prevent against potential security issue
     * with IE/Safari/Opera file type auto detection bug. Returns true if file
     * contain insecure HTML code at the beginning.
     *
     * @param item file upload item
     * @return true if detected.
     * @throws IOException when io error occurs.
     */
    public static boolean detectHtml(Part item) throws IOException {
        byte[] buff = new byte[MAX_BUFFER_SIZE];
        try (InputStream is = item.getInputStream()) {
            is.read(buff, 0, MAX_BUFFER_SIZE);
            String content = new String(buff);
            content = content.toLowerCase().trim();

            if (Pattern.compile("<!DOCTYPE\\W+X?HTML.+",
                    Pattern.CASE_INSENSITIVE
                    | Pattern.DOTALL
                    | Pattern.MULTILINE).matcher(content).matches()) {
                return true;
            }

            String[] tags = {"<body", "<head", "<html", "<img", "<pre",
                "<script", "<table", "<title"};

            for (String tag : tags) {
                if (content.contains(tag)) {
                    return true;
                }
            }

            if (Pattern.compile("type\\s*=\\s*['\"]?\\s*(?:\\w*/)?(?:ecma|java)",
                    Pattern.CASE_INSENSITIVE
                    | Pattern.DOTALL
                    | Pattern.MULTILINE).matcher(content).find()) {
                return true;
            }

            if (Pattern.compile(
                    "(?:href|src|data)\\s*=\\s*['\"]?\\s*(?:ecma|java)script:",
                    Pattern.CASE_INSENSITIVE
                    | Pattern.DOTALL
                    | Pattern.MULTILINE).matcher(content).find()) {
                return true;
            }

            if (Pattern.compile("url\\s*\\(\\s*['\"]?\\s*(?:ecma|java)script:",
                    Pattern.CASE_INSENSITIVE
                    | Pattern.DOTALL
                    | Pattern.MULTILINE).matcher(content).find()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if folder has any subfolders but respects ACL and hideFolders
     * setting from configuration.
     *
     * @param accessControl
     * @param dirPath path to current folder.
     * @param dir current folder being checked. Represented by File object.
     * @param configuration configuration object.
     * @param resourceType name of resource type, folder is assigned to.
     * @param currentUserRole user role.
     * @return true if there are any allowed and non-hidden subfolders.
     * @throws java.io.IOException
     */
    public static Boolean hasChildren(AccessControl accessControl, String dirPath, Path dir, IConfiguration configuration, String resourceType, String currentUserRole) throws IOException {
        DirectoryStream<Path> subDirsList = Files.newDirectoryStream(dir, Files::isDirectory);

        if (subDirsList != null) {
            for (Path subDirsList1 : subDirsList) {
                String subDirName = subDirsList1.getFileName().toString();
                if (!FileUtils.checkIfDirIsHidden(subDirName, configuration)
                        && accessControl.checkFolderACL(resourceType,
                                dirPath + subDirName, currentUserRole, AccessControl.CKFINDER_CONNECTOR_ACL_FOLDER_VIEW)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * rename file with double extension.
     *
     * @param type a {@code ResourceType} object holding list of allowed and
     * denied extensions against which file extension will be tested.
     * @param fileName file name
     * @return new file name with . replaced with _ (but not last)
     */
    public static String renameFileWithBadExt(ResourceType type, String fileName) {
        if (type == null || fileName == null) {
            return null;
        }

        if (fileName.indexOf('.') == -1) {
            return fileName;
        }

        StringTokenizer tokens = new StringTokenizer(fileName, ".");
        String cfileName = tokens.nextToken();
        String currToken;
        while (tokens.hasMoreTokens()) {
            currToken = tokens.nextToken();
            if (tokens.hasMoreElements()) {
                cfileName = cfileName.concat(checkSingleExtension(currToken, type) ? "." : "_");
                cfileName = cfileName.concat(currToken);
            } else {
                cfileName = cfileName.concat(".".concat(currToken));
            }
        }
        return cfileName;
    }

    public static String encodeURIComponent(String fileName) throws UnsupportedEncodingException {
        String fileNameHelper = URLEncoder.encode(fileName, "utf-8");
        for (Map.Entry<String, String> entry : EncodingMapHolder.encodingMap.entrySet()) {
            fileNameHelper = fileNameHelper.replace(entry.getKey(), entry.getValue());
        }
        return fileNameHelper;
    }

    public static boolean checkFolderName(String folderName, IConfiguration configuration) {
        return !((configuration.isDisallowUnsafeCharacters()
                && (folderName.contains(".") || folderName.contains(";")))
                || FileUtils.checkFolderNamePattern(folderName));
    }

    public static boolean checkFileName(String fileName, IConfiguration configuration) {
        return !((configuration.isDisallowUnsafeCharacters() && fileName.contains(";"))
                || !FileUtils.checkFileName(fileName));
    }

    public static String backupWithBackSlash(String fileName, String toReplace) {
        return fileName.replaceAll(toReplace, "\\\\" + Matcher.quoteReplacement(toReplace));
    }

    private static class EncodingMapHolder {

        private static final Map<String, String> encodingMap;

        static {
            Map<String, String> mapHelper = new HashMap<>(6);
            mapHelper.put("%21", "!");
            mapHelper.put("%27", "'");
            mapHelper.put("%28", "(");
            mapHelper.put("%29", ")");
            mapHelper.put("%7E", "~");
            mapHelper.put("[+]", "%20");
            encodingMap = Collections.unmodifiableMap(mapHelper);
        }

    }

    private static class Utf8AccentsHolder {

        private static final Map<String, String> UTF8_LOWER_ACCENTS = new HashMap<>(120);
        private static final Map<String, String> UTF8_UPPER_ACCENTS = new HashMap<>(120);

        static {
            UTF8_UPPER_ACCENTS.put("À", "A");
            UTF8_UPPER_ACCENTS.put("Ô", "O");
            UTF8_UPPER_ACCENTS.put("Ď", "D");
            UTF8_UPPER_ACCENTS.put("Ḟ", "F");
            UTF8_UPPER_ACCENTS.put("Ë", "E");
            UTF8_UPPER_ACCENTS.put("Š", "S");
            UTF8_UPPER_ACCENTS.put("Ơ", "O");
            UTF8_UPPER_ACCENTS.put("Ă", "A");
            UTF8_UPPER_ACCENTS.put("Ř", "R");
            UTF8_UPPER_ACCENTS.put("Ț", "T");
            UTF8_UPPER_ACCENTS.put("Ň", "N");
            UTF8_UPPER_ACCENTS.put("Ā", "A");
            UTF8_UPPER_ACCENTS.put("Ķ", "K");
            UTF8_UPPER_ACCENTS.put("Ŝ", "S");
            UTF8_UPPER_ACCENTS.put("Ỳ", "Y");
            UTF8_UPPER_ACCENTS.put("Ņ", "N");
            UTF8_UPPER_ACCENTS.put("Ĺ", "L");
            UTF8_UPPER_ACCENTS.put("Ħ", "H");
            UTF8_UPPER_ACCENTS.put("Ṗ", "P");
            UTF8_UPPER_ACCENTS.put("Ó", "O");
            UTF8_UPPER_ACCENTS.put("Ú", "U");
            UTF8_UPPER_ACCENTS.put("Ě", "E");
            UTF8_UPPER_ACCENTS.put("É", "E");
            UTF8_UPPER_ACCENTS.put("Ç", "C");
            UTF8_UPPER_ACCENTS.put("Ẁ", "W");
            UTF8_UPPER_ACCENTS.put("Ċ", "C");
            UTF8_UPPER_ACCENTS.put("Õ", "O");
            UTF8_UPPER_ACCENTS.put("Ṡ", "S");
            UTF8_UPPER_ACCENTS.put("Ø", "O");
            UTF8_UPPER_ACCENTS.put("Ģ", "G");
            UTF8_UPPER_ACCENTS.put("Ŧ", "T");
            UTF8_UPPER_ACCENTS.put("Ș", "S");
            UTF8_UPPER_ACCENTS.put("Ė", "E");
            UTF8_UPPER_ACCENTS.put("Ĉ", "C");
            UTF8_UPPER_ACCENTS.put("Ś", "S");
            UTF8_UPPER_ACCENTS.put("Î", "I");
            UTF8_UPPER_ACCENTS.put("Ű", "U");
            UTF8_UPPER_ACCENTS.put("Ć", "C");
            UTF8_UPPER_ACCENTS.put("Ę", "E");
            UTF8_UPPER_ACCENTS.put("Ŵ", "W");
            UTF8_UPPER_ACCENTS.put("Ṫ", "T");
            UTF8_UPPER_ACCENTS.put("Ū", "U");
            UTF8_UPPER_ACCENTS.put("Č", "C");
            UTF8_UPPER_ACCENTS.put("Ö", "Oe");
            UTF8_UPPER_ACCENTS.put("È", "E");
            UTF8_UPPER_ACCENTS.put("Ŷ", "Y");
            UTF8_UPPER_ACCENTS.put("Ą", "A");
            UTF8_UPPER_ACCENTS.put("Ł", "L");
            UTF8_UPPER_ACCENTS.put("Ų", "U");
            UTF8_UPPER_ACCENTS.put("Ů", "U");
            UTF8_UPPER_ACCENTS.put("Ş", "S");
            UTF8_UPPER_ACCENTS.put("Ğ", "G");
            UTF8_UPPER_ACCENTS.put("Ļ", "L");
            UTF8_UPPER_ACCENTS.put("Ƒ", "F");
            UTF8_UPPER_ACCENTS.put("Ž", "Z");
            UTF8_UPPER_ACCENTS.put("Ẃ", "W");
            UTF8_UPPER_ACCENTS.put("Ḃ", "B");
            UTF8_UPPER_ACCENTS.put("Å", "A");
            UTF8_UPPER_ACCENTS.put("Ì", "I");
            UTF8_UPPER_ACCENTS.put("Ï", "I");
            UTF8_UPPER_ACCENTS.put("Ḋ", "D");
            UTF8_UPPER_ACCENTS.put("Ť", "T");
            UTF8_UPPER_ACCENTS.put("Ŗ", "R");
            UTF8_UPPER_ACCENTS.put("Ä", "Ae");
            UTF8_UPPER_ACCENTS.put("Í", "I");
            UTF8_UPPER_ACCENTS.put("Ŕ", "R");
            UTF8_UPPER_ACCENTS.put("Ê", "E");
            UTF8_UPPER_ACCENTS.put("Ü", "Ue");
            UTF8_UPPER_ACCENTS.put("Ò", "O");
            UTF8_UPPER_ACCENTS.put("Ē", "E");
            UTF8_UPPER_ACCENTS.put("Ñ", "N");
            UTF8_UPPER_ACCENTS.put("Ń", "N");
            UTF8_UPPER_ACCENTS.put("Ĥ", "H");
            UTF8_UPPER_ACCENTS.put("Ĝ", "G");
            UTF8_UPPER_ACCENTS.put("Đ", "D");
            UTF8_UPPER_ACCENTS.put("Ĵ", "J");
            UTF8_UPPER_ACCENTS.put("Ÿ", "Y");
            UTF8_UPPER_ACCENTS.put("Ũ", "U");
            UTF8_UPPER_ACCENTS.put("Ŭ", "U");
            UTF8_UPPER_ACCENTS.put("Ư", "U");
            UTF8_UPPER_ACCENTS.put("Ţ", "T");
            UTF8_UPPER_ACCENTS.put("Ý", "Y");
            UTF8_UPPER_ACCENTS.put("Ő", "O");
            UTF8_UPPER_ACCENTS.put("Ÿ", "Y");
            UTF8_UPPER_ACCENTS.put("Ũ", "U");
            UTF8_UPPER_ACCENTS.put("Ŭ", "U");
            UTF8_UPPER_ACCENTS.put("Ư", "U");
            UTF8_UPPER_ACCENTS.put("Ţ", "T");
            UTF8_UPPER_ACCENTS.put("Ý", "Y");
            UTF8_UPPER_ACCENTS.put("Ő", "O");
            UTF8_UPPER_ACCENTS.put("Â", "A");
            UTF8_UPPER_ACCENTS.put("Ľ", "L");
            UTF8_UPPER_ACCENTS.put("Ẅ", "W");
            UTF8_UPPER_ACCENTS.put("Ż", "Z");
            UTF8_UPPER_ACCENTS.put("Ī", "I");
            UTF8_UPPER_ACCENTS.put("Ã", "A");
            UTF8_UPPER_ACCENTS.put("Ġ", "G");
            UTF8_UPPER_ACCENTS.put("Ṁ", "M");
            UTF8_UPPER_ACCENTS.put("Ō", "O");
            UTF8_UPPER_ACCENTS.put("Ĩ", "I");
            UTF8_UPPER_ACCENTS.put("Ù", "U");
            UTF8_UPPER_ACCENTS.put("Į", "I");
            UTF8_UPPER_ACCENTS.put("Ź", "Z");
            UTF8_UPPER_ACCENTS.put("Á", "A");
            UTF8_UPPER_ACCENTS.put("Û", "U");
            UTF8_UPPER_ACCENTS.put("Þ", "Th");
            UTF8_UPPER_ACCENTS.put("Ð", "Dh");
            UTF8_UPPER_ACCENTS.put("Æ", "Ae");
            UTF8_UPPER_ACCENTS.put("Ĕ", "E");

            UTF8_LOWER_ACCENTS.put("à", "a");
            UTF8_LOWER_ACCENTS.put("ô", "o");
            UTF8_LOWER_ACCENTS.put("ď", "d");
            UTF8_LOWER_ACCENTS.put("ḟ", "f");
            UTF8_LOWER_ACCENTS.put("ë", "e");
            UTF8_LOWER_ACCENTS.put("š", "s");
            UTF8_LOWER_ACCENTS.put("ơ", "o");
            UTF8_LOWER_ACCENTS.put("ß", "ss");
            UTF8_LOWER_ACCENTS.put("ă", "a");
            UTF8_LOWER_ACCENTS.put("ř", "r");
            UTF8_LOWER_ACCENTS.put("ț", "t");
            UTF8_LOWER_ACCENTS.put("ň", "n");
            UTF8_LOWER_ACCENTS.put("ā", "a");
            UTF8_LOWER_ACCENTS.put("ķ", "k");
            UTF8_LOWER_ACCENTS.put("ŝ", "s");
            UTF8_LOWER_ACCENTS.put("ỳ", "y");
            UTF8_LOWER_ACCENTS.put("ņ", "n");
            UTF8_LOWER_ACCENTS.put("ĺ", "l");
            UTF8_LOWER_ACCENTS.put("ħ", "h");
            UTF8_LOWER_ACCENTS.put("ṗ", "p");
            UTF8_LOWER_ACCENTS.put("ó", "o");
            UTF8_LOWER_ACCENTS.put("ú", "u");
            UTF8_LOWER_ACCENTS.put("ě", "e");
            UTF8_LOWER_ACCENTS.put("é", "e");
            UTF8_LOWER_ACCENTS.put("ç", "c");
            UTF8_LOWER_ACCENTS.put("ẁ", "w");
            UTF8_LOWER_ACCENTS.put("ċ", "c");
            UTF8_LOWER_ACCENTS.put("õ", "o");
            UTF8_LOWER_ACCENTS.put("ṡ", "s");
            UTF8_LOWER_ACCENTS.put("ø", "o");
            UTF8_LOWER_ACCENTS.put("ģ", "g");
            UTF8_LOWER_ACCENTS.put("ŧ", "t");
            UTF8_LOWER_ACCENTS.put("ș", "s");
            UTF8_LOWER_ACCENTS.put("ė", "e");
            UTF8_LOWER_ACCENTS.put("ĉ", "c");
            UTF8_LOWER_ACCENTS.put("ś", "s");
            UTF8_LOWER_ACCENTS.put("î", "i");
            UTF8_LOWER_ACCENTS.put("ű", "u");
            UTF8_LOWER_ACCENTS.put("ć", "c");
            UTF8_LOWER_ACCENTS.put("ę", "e");
            UTF8_LOWER_ACCENTS.put("ŵ", "w");
            UTF8_LOWER_ACCENTS.put("ṫ", "t");
            UTF8_LOWER_ACCENTS.put("ū", "u");
            UTF8_LOWER_ACCENTS.put("č", "c");
            UTF8_LOWER_ACCENTS.put("ö", "oe");
            UTF8_LOWER_ACCENTS.put("è", "e");
            UTF8_LOWER_ACCENTS.put("ŷ", "y");
            UTF8_LOWER_ACCENTS.put("ą", "a");
            UTF8_LOWER_ACCENTS.put("ł", "l");
            UTF8_LOWER_ACCENTS.put("ų", "u");
            UTF8_LOWER_ACCENTS.put("ů", "u");
            UTF8_LOWER_ACCENTS.put("ş", "s");
            UTF8_LOWER_ACCENTS.put("ğ", "g");
            UTF8_LOWER_ACCENTS.put("ļ", "l");
            UTF8_LOWER_ACCENTS.put("ƒ", "f");
            UTF8_LOWER_ACCENTS.put("ž", "z");
            UTF8_LOWER_ACCENTS.put("ẃ", "w");
            UTF8_LOWER_ACCENTS.put("ḃ", "b");
            UTF8_LOWER_ACCENTS.put("å", "a");
            UTF8_LOWER_ACCENTS.put("ì", "i");
            UTF8_LOWER_ACCENTS.put("ï", "i");
            UTF8_LOWER_ACCENTS.put("ḋ", "d");
            UTF8_LOWER_ACCENTS.put("ť", "t");
            UTF8_LOWER_ACCENTS.put("ŗ", "r");
            UTF8_LOWER_ACCENTS.put("ä", "ae");
            UTF8_LOWER_ACCENTS.put("í", "i");
            UTF8_LOWER_ACCENTS.put("ŕ", "r");
            UTF8_LOWER_ACCENTS.put("ê", "e");
            UTF8_LOWER_ACCENTS.put("ü", "ue");
            UTF8_LOWER_ACCENTS.put("ò", "o");
            UTF8_LOWER_ACCENTS.put("ē", "e");
            UTF8_LOWER_ACCENTS.put("ñ", "n");
            UTF8_LOWER_ACCENTS.put("ń", "n");
            UTF8_LOWER_ACCENTS.put("ĥ", "h");
            UTF8_LOWER_ACCENTS.put("ĝ", "g");
            UTF8_LOWER_ACCENTS.put("đ", "d");
            UTF8_LOWER_ACCENTS.put("ĵ", "j");
            UTF8_LOWER_ACCENTS.put("ÿ", "y");
            UTF8_LOWER_ACCENTS.put("ũ", "u");
            UTF8_LOWER_ACCENTS.put("ŭ", "u");
            UTF8_LOWER_ACCENTS.put("ư", "u");
            UTF8_LOWER_ACCENTS.put("ţ", "t");
            UTF8_LOWER_ACCENTS.put("ý", "y");
            UTF8_LOWER_ACCENTS.put("ő", "o");
            UTF8_LOWER_ACCENTS.put("â", "a");
            UTF8_LOWER_ACCENTS.put("ľ", "l");
            UTF8_LOWER_ACCENTS.put("ẅ", "w");
            UTF8_LOWER_ACCENTS.put("ż", "z");
            UTF8_LOWER_ACCENTS.put("ī", "i");
            UTF8_LOWER_ACCENTS.put("ã", "a");
            UTF8_LOWER_ACCENTS.put("ġ", "g");
            UTF8_LOWER_ACCENTS.put("ṁ", "m");
            UTF8_LOWER_ACCENTS.put("ō", "o");
            UTF8_LOWER_ACCENTS.put("ĩ", "i");
            UTF8_LOWER_ACCENTS.put("ù", "u");
            UTF8_LOWER_ACCENTS.put("į", "i");
            UTF8_LOWER_ACCENTS.put("ź", "z");
            UTF8_LOWER_ACCENTS.put("á", "a");
            UTF8_LOWER_ACCENTS.put("û", "u");
            UTF8_LOWER_ACCENTS.put("þ", "th");
            UTF8_LOWER_ACCENTS.put("ð", "dh");
            UTF8_LOWER_ACCENTS.put("æ", "ae");
            UTF8_LOWER_ACCENTS.put("µ", "u");
            UTF8_LOWER_ACCENTS.put("ĕ", "e");
        }

    }

}
