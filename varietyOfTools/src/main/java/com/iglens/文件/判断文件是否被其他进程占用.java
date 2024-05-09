package com.iglens.文件;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

public class 判断文件是否被其他进程占用 {
    public static boolean isFileLocked(File file) {
        try {
            // 使用 RandomAccessFile 打开文件，并以只读模式获取 FileChannel
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
            FileChannel fileChannel = randomAccessFile.getChannel();
            
            // 尝试获取文件锁
            FileLock fileLock = fileChannel.tryLock();
            
            // 如果获取成功，则释放锁，并返回 false
            if (fileLock != null) {
                fileLock.release();
                return false;
            }
            
            // 如果获取失败，则返回 true
            return true;
        } catch (Exception e) {
            // 处理异常
            e.printStackTrace();
            return false;
        }
    }
    
    public static void main(String[] args) {
        File file = new File("test.txt");
        System.out.println("File is locked: " + isFileLocked(file));
    }
}