package com.iglens.解压;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;

public class RarFileUtil {
    public static List<File> unZip(File zipFile,String satellitePath) {
        FileOutputStream out = null;
        InputStream in = null;
        List<File> zFileNames = new ArrayList<File>();
        ZipFile zip = null;
        try {
            zip = new ZipFile(zipFile);
            for (Enumeration enumeration = zip.getEntries(); enumeration.hasMoreElements(); ) {
                ZipEntry entry = (ZipEntry) enumeration.nextElement();
                String zipEntryName = entry.getName();
                in = zip.getInputStream(entry);
                if (entry.isDirectory()) {      //处理压缩文件包含文件夹的情况
                    new File("C:\\" + entry.getName()).mkdirs();
                    continue;
                }
                String[] split = zipEntryName.split("/");
                String s = split[split.length-1];
                File outDir = toPath(s,satellitePath);
                String name = null;
                if (!outDir.exists()) {
                    outDir.mkdirs();
                }
                if (outDir.isDirectory()) {
                    File fileList = new File(outDir, s);
                    fileList.createNewFile();
                    name = fileList.getName();
//                    System.out.println("filname" + outDir + "\\" + name);//打印被复制文件路径
                }
                out = new FileOutputStream(outDir + "\\" + name);
                zFileNames.add(new File(outDir + "\\" + name));
                byte[] buff = new byte[1024];
                int len;
                while ((len = in.read(buff)) > 0) {
                    out.write(buff, 0, len);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                out.close();
                in.close();
                zip.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return zFileNames;
    }
    public static final String winrarPath = "C:\\Program Files\\WinRAR\\WinRAR.exe";
    public static List<File> unrar(File f) {
        String target = "C:"+File.separator+"WINRAR" +File.separator+f.getName();
        File path = new File(target);
        List<File> zFileNames = new ArrayList<File>();
        if (!path.exists()){
            path.mkdirs();
        }else{
            File[] files = path.listFiles();
            for (File e:files
            ) {
                try {
                    List<File> readfile = readfile(e.getPath(),new ArrayList<>());
                    zFileNames.addAll(readfile);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
            return zFileNames;
        }
        String rarFile = f.getPath();
        if(!f.exists()){
            return null;
        }
        String cmd = winrarPath + " X -o+ " + rarFile + " " +target;
        try {
            Process proc = Runtime.getRuntime().exec(cmd);
            System.out.println(proc.getInputStream());
            if (proc.waitFor() != 0) {
                if (proc.exitValue() == 0) {
                    System.out.println("解压RAR文件失败");
                }
            } else {
                File[] files = path.listFiles();
                for (File e:files
                     ) {
                    List<File> readfile = readfile(e.getPath(),new ArrayList<>());
                    zFileNames.addAll(readfile);
                }
              /*  File outDir = toPath(f.getName(),storagePath);
                if (!outDir.exists()){
                    outDir.mkdirs();
                }
                File[] files = path.getParentFile().listFiles();
                for (File file:files) {
                    List<File> readfile = readfile(file.getPath(),new ArrayList<>());
                    System.out.println("readfile"+readfile);
                        for (File ff:readfile
                             ) {
                            String name = ff.getName();
//                            Files.copy(ff.toPath(), Paths.get(outDir + File.separator + name), StandardCopyOption.REPLACE_EXISTING);
                            FileUtil.move(ff,outDir,true);
                            zFileNames.add(new File(outDir.getPath()+File.separator+ff.getName()));
                        }
                    }*/
//                    FileUtil.del(path.getParentFile());
                }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return zFileNames;
    }
    public static File toPath(String filename,String path){
        int index = 0;
        int flag=0;
        for(int i=0;i<filename.length();i++) {//遍历字符串
            char c = filename.charAt(i);// c为字符串中的字符
            if (('A' <= c && c < 'Z')) {
                flag++;
                if (flag == 1) {
                    index = i;
                    break;
                }
            }
        }
        String[] s = filename.substring(index).split("_");
        String[] sate = s[0].split("-");
        System.out.println("分类："+sate[0]);
        File outDir = new File("C:"+File.separator+path+File.separator+sate[0]);
        return outDir;
    }

    /**
     * 读取某个文件夹下的所有文件(支持多级文件夹)
     */
    public static List<File> readfile(String filepath,List<File> list) throws FileNotFoundException, IOException {
        try {
            File file = new File(filepath);
            if (!file.isDirectory()) {
                list.add(file);
/*                System.out.println("文件");
                System.out.println("path=" + file.getPath());
                System.out.println("absolutepath=" + file.getAbsolutePath());
                System.out.println("name=" + file.getName());*/
            } else if (file.isDirectory()) {
//                System.out.println("文件夹");
                String[] filelist = file.list();
                for (int i = 0; i < filelist.length; i++) {
                    File readfile = new File(filepath + "\\" + filelist[i]);
                    if (!readfile.isDirectory()) {
                        list.add(readfile);
//                        System.out.println("path=" + readfile.getPath());
//                        System.out.println("absolutepath=" + readfile.getAbsolutePath());
//                        System.out.println("name=" + readfile.getName());
                    } else if (readfile.isDirectory()) {
                        readfile(filepath + "\\" + filelist[i],list);
                    }
                }

            }

        } catch (FileNotFoundException e) {
            System.out.println("readfile()   Exception:" + e.getMessage());
        }
        return list;
    }

}