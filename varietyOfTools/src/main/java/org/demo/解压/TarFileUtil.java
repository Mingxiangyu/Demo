package org.demo.解压;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.io.FilenameUtils;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.springframework.stereotype.Component;

@Component
public class TarFileUtil {

    public static String rarFile;

//    @Value("${unrar}")
    public void setRarFile(String rarFile) {
        this.rarFile = rarFile;
    }

    @Deprecated
    public static List<File> tartest1(String filePath, String satellitePath) {
        File file = new File(filePath);
        String name = file.getName();
        if (name.endsWith(".gz") || name.endsWith(".tar")) {
            return unTarFiletest(file, satellitePath);
        } else if (name.endsWith(".zip")) {
//            return RarFileUtil.unZip(file,satellitePath);
        } else if (name.endsWith(".rar")) {
            return RarFileUtil.unZip(file, satellitePath);
        }
        return null;
    }

    public static List<File> tar(String filePath) {
        File file = new File(filePath);
        String name = file.getName();
        if (name.endsWith(".gz") || name.endsWith(".tar")) {
            return unTarFile(file);
        } else if (name.endsWith(".zip")) {
            return unzip(file);
        } else {
            return RarFileUtil.unrar(file);
        }
    }

    public static List<File> unzip(File file) {
        List<File> zFileNames = new ArrayList<File>();
        File fil = new File(rarFile + FilenameUtils.getBaseName(file.getName()));
        String name = null;
        if (!fil.exists()) {
            fil.mkdirs();
        } else {
//            TODO 后续部署去除
            File[] files = fil.listFiles();
            zFileNames.addAll(Arrays.asList(files));
            return zFileNames;
        }
        FileOutputStream out = null;
        InputStream in = null;
        ZipFile zip = null;
        try {
            zip = new ZipFile(file);
            for (Enumeration enumeration = zip.getEntries(); enumeration.hasMoreElements(); ) {
                ZipEntry entry = (ZipEntry) enumeration.nextElement();
                String zipEntryName = entry.getName();
                in = zip.getInputStream(entry);
                String s = FilenameUtils.getName(zipEntryName);
                if (fil.isDirectory()) {
                    File fileList = new File(fil, s);
                    fileList.createNewFile();
                    name = fileList.getName();
                }
                out = new FileOutputStream(fil + File.separator + name);
                zFileNames.add(new File(fil + File.separator + name));
                byte[] buff = new byte[1024];
                int len;
                while ((len = in.read(buff)) > 0) {
                    out.write(buff, 0, len);
                }
            }
        } catch (IOException e) {
//            TODO 如果解压失败，将解压出来的文件夹删除
            e.printStackTrace();
        } finally {
            try {
                if (out!=null) out.close();
                if (in!=null)in.close();
                if (zip!=null)zip.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return zFileNames;
    }

    public static List<File> unTarFile(File file) {
        List<File> zFileNames = new ArrayList<>();
        TarArchiveInputStream is = null;
        File fil;
        fil = new File(rarFile + File.separator + file.getName());
        String name = null;
        if (!fil.exists()) {
            fil.mkdirs();
        } else {
            File[] files = fil.listFiles();
            zFileNames.addAll(Arrays.asList(files));
            return zFileNames;
        }
        FileOutputStream fos = null;
        try {
            if (file.getName().endsWith(".gz")) {
                is = new TarArchiveInputStream(new GzipCompressorInputStream(new FileInputStream(file)));
            } else if (file.getName().endsWith(".tar")) {
                is = new TarArchiveInputStream(new FileInputStream(file));
            }
            while (true) {
                TarArchiveEntry entry = is.getNextTarEntry();
                if (entry == null) {
                    break;
                }
                String s = FilenameUtils.getName(entry.getName());
//                    if (entry.isDirectory()) {
//                        // 一般不会执行
//                        new File(basePath + entry.getName()).mkdirs();
//                    } else {
                try {
                    if (fil.isDirectory()) {
                        File fileList = new File(fil, s);
                        fileList.createNewFile();
                        name = fileList.getName();
                        System.out.println("filname" + fil + "\\" + name);//打印被复制文件路径
                    }
                    fos = new FileOutputStream(fil + File.separator + name);
                    zFileNames.add(new File(fil + File.separator + name));
                    //定义字节数组,缓冲
                    byte[] bytes = new byte[1024 * 10];
                    //读取数组,写入数组
                    int len = 0;
                    while ((len = is.read(bytes)) != -1) {
                        fos.write(bytes, 0, len);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return zFileNames;
        } catch (IOException ex) {
            throw new RuntimeException("文件复制失败");
        } finally {
            try {
                if (fos!=null)fos.close();
                if (fos!=null)is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Deprecated
    public static List<File> unTarFiletest(File file, String path) {
        List<File> zFileNames = new ArrayList<>();
        TarArchiveInputStream is = null;
        File fil;
        fil = new File(rarFile + File.separator + file.getName());
        String name = null;
        if (!fil.exists()) {
            fil.mkdirs();
        } else {
            File[] files = fil.listFiles();
            zFileNames.addAll(Arrays.asList(files));
            return zFileNames;
        }
        FileOutputStream fos = null;
        try {
            if (file.getName().endsWith(".gz")) {
                is = new TarArchiveInputStream(new GzipCompressorInputStream(new FileInputStream(file)));
            } else if (file.getName().endsWith(".tar")) {
                is = new TarArchiveInputStream(new FileInputStream(file));
            }
            while (true) {
                TarArchiveEntry entry = is.getNextTarEntry();
                if (entry == null) {
                    break;
                }
                String s = FilenameUtils.getName(entry.getName());
//                    if (entry.isDirectory()) {
//                        // 一般不会执行
//                        new File(basePath + entry.getName()).mkdirs();
//                    } else {
                try {
                    if (fil.isDirectory()) {
                        File fileList = new File(fil, s);
                        fileList.createNewFile();
                        name = fileList.getName();
                        System.out.println("filname" + fil + "\\" + name);//打印被复制文件路径
                    }
                    fos = new FileOutputStream(fil + File.separator + name);
                    zFileNames.add(new File(fil + File.separator + name));
                    //定义字节数组,缓冲
                    byte[] bytes = new byte[1024 * 10];
                    //读取数组,写入数组
                    int len = 0;
                    while ((len = is.read(bytes)) != -1) {
                        fos.write(bytes, 0, len);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return zFileNames;
        } catch (IOException ex) {
            throw new RuntimeException("文件复制失败");
        } finally {
            try {
                fos.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }
}
