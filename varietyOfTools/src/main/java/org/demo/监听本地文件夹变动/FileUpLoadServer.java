package org.demo.监听本地文件夹变动;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.math.RoundingMode;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DecimalFormat;
import org.apache.commons.lang3.StringUtils;

public class FileUpLoadServer extends ServerSocket {

	// 文件大小
	private static DecimalFormat df = null;
	// 退出标识
	private boolean quit = false;

	private static String socketPath;

	static {
		// 设置数字格式，保留一位有效小数
		df = new DecimalFormat("#0.0");
		df.setRoundingMode(RoundingMode.HALF_UP);
		df.setMinimumFractionDigits(1);
		df.setMaximumFractionDigits(1);
	}
	
	
	public FileUpLoadServer(int report,String socket) throws IOException {
		super(report);
		FileUpLoadServer.socketPath = socket;
	}
	
	
	/**
	 * 使用线程处理每个客户端传输的文件
	 * 
	 * @throws Exception
	 */
	public void load() throws Exception {
		System.out.println("【文件上传】服务器：" + this.getInetAddress() + " 正在运行中...");
		while (!quit) {
			// server尝试接收其他Socket的连接请求，server的accept方法是阻塞式的
			Socket socket = this.accept();
			/**
			 * 我们的服务端处理客户端的连接请求是同步进行的， 每次接收到来自客户端的连接请求后，
			 * 都要先跟当前的客户端通信完之后才能再处理下一个连接请求。 这在并发比较多的情况下会严重影响程序的性能，
			 * 为此，我们可以把它改为如下这种异步处理与客户端通信的方式
			 */
			// 收到请求，验证合法性
			String ip = socket.getInetAddress().toString();
			ip = ip.substring(1, ip.length());
			System.out.println("服务器接收到请求，正在开启验证对方合法性IP：" + ip + "！");
			// 每接收到一个Socket就建立一个新的线程来处理它
			new Thread(new Task(socket, ip)).start();
		}
	}
	
	
	/**
	 * 处理客户端传输过来的文件线程类
	 */
	class Task implements Runnable {

		private final Socket sk;	//  当前连接
		private final String ips; //  当前连接IP址

		public Task(Socket socket, String ip) {
			this.sk = socket;
			this.ips = ip;
		}

		@Override
		public void run() {
			
			Socket socket = sk;					//  重新定义，请不要移出run()方法外部，否则连接两会被重置
			String ip = ips;					//  重新定义，同上IP会变
			long serverLength;			//  定义：存放在服务器里的文件长度，默认没有为-1
			char pathChar = File.separatorChar;	//  获取：系统路径分隔符
//			String panFu = "E:";				//  路径：存储文件盘符
			
			DataInputStream dis = null;			//  获取：客户端输出流
			DataOutputStream dos;		//  发送：向客户端输入流
			FileOutputStream fos = null;		//  读取：服务器本地文件流
			RandomAccessFile rantmpfile = null;	//  操作类：随机读取
			
			try {
				// 获取
				dis = new DataInputStream(socket.getInputStream());
				// 发送
				dos = new DataOutputStream(socket.getOutputStream());
				// 定义客户端传过来的文件名
				String fileName = "";
				while (StringUtils.isEmpty(fileName)) {
					// 读取客户端传来的数据
					fileName = dis.readUTF();
					System.out.println("服务器获取客户端文件名称：" + fileName);
					File file = new File(socketPath + fileName);
					if (file.exists()) {
						serverLength = file.length();
						dos.writeLong(serverLength);
					} else {
						serverLength = 0L;
						dos.writeLong(serverLength);
						System.out.println("文件不存在");
					}
					System.out.println("向客户端返回文件长度：" + serverLength + " B");
				}
				System.out.println("服务器建立新线程处理客户端请求，对方IP：" + ip + "，传输正在进行中...");
				// 从客户端获取输入流
				dis = new DataInputStream(socket.getInputStream());
				// 文件名和长度
				long fileLength = dis.readLong();
				File directory = new File(socketPath);
				if (!directory.exists()) {
					directory.mkdirs();
				}
				int length = 0;
				byte[] bytes = new byte[1024];
				File file = new File(directory.getAbsolutePath() + pathChar + fileName);
				if (!file.exists()) {
					// 不存在
					fos = new FileOutputStream(file);
					// 开始接收文件
					while ((length = dis.read(bytes, 0, bytes.length)) != -1) {
						fos.write(bytes, 0, length);
						fos.flush();
					}
				} else {
					// 存储在服务器中的文件长度
					long fileSize = file.length(), pointSize = 0;
					// 判断是否已下载完成
					if (fileLength > fileSize) {
						// 断点下载
						pointSize = fileSize;
					} else {
						// 重新下载
						file.delete();
						file.createNewFile();
					}

					rantmpfile = new RandomAccessFile(file, "rw");
					/*
					 * java.io.InputStream.skip() 用法：跳过 n 个字节（丢弃） 如果 n
					 * 为负，则不跳过任何字节。
					 */
					// dis.skip(pointSize); （已从客户端读取进度）
					/**
					 * 资源，文件定位（游标、指针） 将ras的指针设置到8，则读写ras是从第9个字节读写到
					 */
					rantmpfile.seek(pointSize);

					while ((length = dis.read(bytes, 0, bytes.length)) != -1){ rantmpfile.write(bytes, 0, length);}
				}
				System.out.println("======== 文件接收成功 [File Name：" + fileName + "] [ClientIP:" + ip + "] [Size：" + getFormatFileSize(file.length()) + "] ========");
//				FileUtil.move(file,new File(socketPath),true);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (fos != null) {
						fos.close();
					}
					if (dis != null) {
						dis.close();
					}
					if (rantmpfile != null) {
						rantmpfile.close();
					}
					socket.close();
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("Socket关闭失败！");
				}
			}
		}
	}
	
	/**
	 * 格式化文件大小
	 * 
	 * @param length
	 * @return
	 */
	public String getFormatFileSize(long length) {
		double size = ((double) length) / (1 << 30);
		if (size >= 1) {
			return df.format(size) + "GB";
		}
		size = ((double) length) / (1 << 20);
		if (size >= 1) {
			return df.format(size) + "MB";
		}
		size = ((double) length) / (1 << 10);
		if (size >= 1) {
			return df.format(size) + "KB";
		}
		return length + "B";
	}
	
	/**
	 * 退出
	 */
	public void quit() {
		this.quit = true;
		try {
			this.close();
		} catch (IOException e) {
			System.out.println("服务器关闭发生异常，原因未知");
		}
	}
	
}