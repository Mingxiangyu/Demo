[TOC]



# windows环境下JAVA+GDAL

## 1.GDAL下载

> GDAL介绍：
>
> GDAL(Geospatial Data Abstraction Library)是一个开源栅格空间数据转换库。它利用抽象数据模型来表达所支持的各种文件格式。它还有一系列命令行工具来进行数据转换和处理。
>
> GDAL使用C++开发，因此要在Java环境下使用GDAL库，要用JNI（Java跨平台调用的一种方式）的方法调用dll库。所以，我们可以下载GDAL的源码，依照官网教程，通过Visual Studio编译出可用dll。但是最简单的做法就是使用已经编译生成好的符合JNI标准的动态dll库和jar包了，推荐这个网址：http://www.gisinternals.com/archive.php，下载合适的文件。

[**java GDAL下载**](http://download.gisinternals.com/sdk.php)：http://download.gisinternals.com/sdk.php

![image-20220507105437998](C:\Users\zhouhuilin\AppData\Roaming\Typora\typora-user-images\image-20220507105437998.png)

![image-20220507105841707](C:\Users\zhouhuilin\AppData\Roaming\Typora\typora-user-images\image-20220507105841707.png)

## 2.GDAL安装（既解压后拷贝）

![image-20220507111815126](C:\Users\zhouhuilin\AppData\Roaming\Typora\typora-user-images\image-20220507111815126.png)

![image-20220507112249301](C:\Users\zhouhuilin\AppData\Roaming\Typora\typora-user-images\image-20220507112249301.png)

将下好的压缩包里**gdal-release-1928-x64/bin/**目录下的东西解压到jdk/bin目录下（如果放jdk/bin下运行程序还是报错`Native library load failed.`，尝试放`jre/bin`目录下或者`java工程根目录下`）

将r**elease-1928-x64/bin/gdal/java/**下的**gdalalljni.dll以及该目录下其他dll**拷贝到java工程根目录（如果放jdk/bin下运行程序还是报错`Native library load failed.`，尝试放`jre/bin`目录下或者`java工程根目录下`），~~gdal.jar拷贝到java工程里并用maven引用~~,现在gdal for java已经放到maven库中，直接就可以引入了

~~本地引入，已弃用~~，建议直接使用maven库引入

~~~sh
<dependency>
    <groupId>org.gdal</groupId>
    <artifactId>gdal</artifactId>
    <scope>system</scope>
    <systemPath>${project.basedir}/lib/gdal.jar</systemPath>
</dependency>
~~~

maven库引入

~~~sh
<dependency>
  <groupId>org.gdal</groupId>
  <artifactId>gdal</artifactId>
  <version>3.2.0</version>
</dependency>
~~~

## 3.GDAL测试

~~~java
import org.gdal.gdal.Band;
import org.gdal.gdal.Driver;
import org.gdal.gdal.gdal;
import org.gdal.gdal.Dataset;
import org.gdal.gdalconst.gdalconstConstants;


/**
 * @author xming
 * @createTime 2022/5/7 16:37
 * @description
 */
public class RasterGdalTest {
  private static final String FILE_PATH = "D:\\WeChat Files\\aion_my_god\\FileStorage\\File\\2022-05\\L18.tif";
  static {
    // 注册所有的驱动
    gdal.AllRegister();
    // 为了支持中文路径，请添加下面这句代码
    gdal.SetConfigOption("GDAL_FILENAME_IS_UTF8","YES");
    // 为了使属性表字段支持中文，请添加下面这句
    gdal.SetConfigOption("SHAPE_ENCODING","");
  }

  public static void main(String[] args) {
    // 读取影像数据
    Dataset dataset = gdal.Open(FILE_PATH, gdalconstConstants.GA_ReadOnly);
    if (dataset == null) {
      System.out.println("read fail!");
      return;
    }

    //  providing various methods for a format specific driver.
    Driver driver = dataset.GetDriver();
    System.out.println("driver name : " + driver.getLongName());

    // 读取影像信息
    int xSize = dataset.getRasterXSize();
    int ySzie = dataset.getRasterYSize();
    int rasterCount = dataset.getRasterCount();
    System.out.println("dataset xSize:" + xSize + ", ySzie = " + ySzie + ", rasterCount = " + rasterCount);

    Band band = dataset.GetRasterBand(1);
    //the data type of the band.
    int type = band.GetRasterDataType();
    System.out.println("data type = " + type + ", " + (type == gdalconstConstants.GDT_Byte));

    //Frees the native resource associated to a Dataset object and close the file.
    dataset.delete();

    gdal.GDALDestroyDriverManager();
  }
}
~~~

## 4.Jar包方式运行 https://www.jianshu.com/p/6bce533f6595

### 项目资源结构

采用资源管理gdal文件，通过Maven加载gdal.jar，在web应用resources目录下新增gdal文件件，再新建linux,win32两个子文件夹，结构如下

![img](https://upload-images.jianshu.io/upload_images/18566927-5fbc5e83a9b8498d.png?imageMogr2/auto-orient/strip|imageView2/2/w/193/format/webp)

### 资源配置gdal.jar,动态库文件

从*gdal\bin\gdal\java* 复制 *gdal.jar、lgdalalljni.dll* 到项目 *resources/gdal/win32*

![img](https://upload-images.jianshu.io/upload_images/18566927-48093bcba1713b82.png?imageMogr2/auto-orient/strip|imageView2/2/w/326/format/webp)

IDE中启动，运行正常，打成jar包后直接无法启动，启动报错**no gdaljni in java.library.path**，原因本地动为态库*gdalalljni*载入错误，需要加载gdalalljni动态库

### 编写LibraryUtil.java动态库加载类

~~~java
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/*
 * 动态库加载类：从资源加载动态库
 **/
public class LibraryUtil {
    private static final Logger log = LoggerFactory.getLogger(LibraryUtil.class);
    /**
     * 从资源文件加载类
     * * @param clazz jar中类
     * @param file resources目录下文件全路径：/gdal/win32/gdalalljni.dll
     */
    public static void loadFromResource(String file) throws IOException{
        try {
            //获取系统路径
            String[] libraryPaths=initializePath("java.library.path");
            log.info("---LibraryUtil-----java.library.path={}",StringUtil.join(";", libraryPaths));
            if(libraryPaths==null||libraryPaths.length==0) {
                log.info("---LibraryUtil--请设置环境变量java.library.path");
                return;
            }
            String nativeTempDir=libraryPaths[0];
            
            int sepIndex=file.lastIndexOf(File.separator);
            if(sepIndex==-1) {
                sepIndex=file.lastIndexOf("/");
            }
            String fileName=file.substring(sepIndex+1);
            
            log.info("---LibraryUtil--从环境变量{}加载{}",nativeTempDir,fileName);
            //系统库不存在，就从资源文件复制
            File extractedLibFile = new File(nativeTempDir+File.separator +fileName);   
            if(!extractedLibFile.exists()){
                //file resources目录下文件全路径：/gdal/windows/gdalalljni.dll
                InputStream in = LibraryUtil.class.getResourceAsStream(file);
                if(in==null) {
                    log.info("---LibraryUtil--资源文件不存在{}",file);
                    throw new FileNotFoundException(file);
                } 
                saveFile(in,extractedLibFile);
                //保存文件到java.library.path
                log.info("---LibraryUtil--成功保存文件{}到{}",fileName,extractedLibFile.getPath());
            }
            //注意采用loadLibrary加载时mapLibraryName方法会根据系统补全名称
            int startIndex=fileName.startsWith("lib")?3:0;
            String libName=fileName.substring(startIndex,fileName.indexOf("."));
            String mapLibraryName=System.mapLibraryName(libName);
            log.info("---LibraryUtil--mapLibraryName={}",mapLibraryName);
            //输出调试信息
            log.info("---LibraryUtil--系统加载动态库{}开始",libName);
            System.loadLibrary(libName);
            log.info("---LibraryUtil--系统加载动态库{}完成",libName);
        }
        catch(Exception ex) {
            log.error(ex.getMessage());
        }
    }
    private static String[] initializePath(String propname) {
        String ldpath = System.getProperty(propname, "");
        String ps = File.pathSeparator;
        int ldlen = ldpath.length();
        int i, j, n;
        // Count the separators in the path
        i = ldpath.indexOf(ps);
        n = 0;
        while (i >= 0) {
            n++;
            i = ldpath.indexOf(ps, i + 1);
        }

        // allocate the array of paths - n :'s = n + 1 path elements
        String[] paths = new String[n + 1];

        // Fill the array with paths from the ldpath
        n = i = 0;
        j = ldpath.indexOf(ps);
        while (j >= 0) {
            if (j - i > 0) {
                paths[n++] = ldpath.substring(i, j);
            } else if (j - i == 0) {
                paths[n++] = ".";
            }
            i = j + 1;
            j = ldpath.indexOf(ps, i);
        }
        paths[n] = ldpath.substring(i, ldlen);
        return paths;
    }
    public static void saveFile(InputStream in, File extractedLibFile) throws IOException{
        BufferedInputStream reader = null;   
        FileOutputStream writer = null;   
        try {
            //文件不存在创建文件，否则获取流报异常
            createFile(extractedLibFile);
            
            reader = new BufferedInputStream(in);   
            writer = new FileOutputStream(extractedLibFile);   
               
            byte[] buffer = new byte[1024];   
               
            while (reader.read(buffer) > 0){   
                writer.write(buffer);   
                buffer = new byte[1024];   
            }   
        } catch (IOException e){   
            throw e;  
        } finally {   
            if(in!=null)   
                in.close();   
            if(writer!=null)   
                writer.close();   
        }
    }
    /**
     * 文件不存在创建文件，包括上级目录
     */
    public static void createFile(File destFile) throws IOException{
        File pfile = destFile.getParentFile();
        if (!pfile.exists()) {
            pfile.mkdirs();
        }
        if(!destFile.exists()){
            destFile.createNewFile();
        }
    }
}
~~~

### 加载动态库

~~~java
  static {
        log.info("---LibraryUtil--gdal载入动态库");
        try {
            //根据系统环境加载资源
            String systemType = System.getProperty("os.name");   
            String file="";  
            boolean isWin=systemType.toLowerCase().indexOf("win")!=-1;
            if(isWin) {
                file="/gdal/win32/gdalalljni.dll"; //win下jni路径
            }
            else {
                file="/gdal/linux/libgdalalljni.so";//linux下jni路径
            }
            //从资源文件加载动态库
            LibraryUtil.loadFromResource(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
~~~

缺少依赖库，载入*gdalalljni*库时出错，梳理执行过程：

```css
- 检查java.library.path环境变量值
- 从java.library.path环境变量取第一个路径为目标路径D:\Program Files\Java\jdk1.8.0_231\bin
- 从目标路径判断是否存在gdalalljni.dll
- gdalalljni.dll不存在时从资源文件复制到目标路径
- System.loadLibrary载入gdalalljni.dll
```

### 复制gdalalljni.dll依赖库到目标路径

把 *gdal\bin* 下所有的dll复制到目标路径 *D:\Program Files\Java\jdk1.8.0_231\bin(即jdk所在目录）*

![img](https://upload-images.jianshu.io/upload_images/18566927-ae1531d583e4610f.png?imageMogr2/auto-orient/strip|imageView2/2/w/626/format/webp)

再次启动jar包就可以正常运行

## 异常问题：

### 引用库报错 **java.lang.UnsatisfiedLinkError: no gdaljni in java.library.path**

工程中只引用了gdal.jar包，没有任何gdal_java的dll，但是未正确配置 gdal 的 dll 文件，也就是gdal的dll文件拷贝位置不正确，导致java程序不能找到相应的dll引用。将dll拷贝到**%JAVA_HOME%/jdk/bin**或**%JAVA_HOME%/jre/bin**目录下

> Native library load failed.
> java.lang.UnsatisfiedLinkError: **no gdaljni in java.library.path**
> Exception in thread “main” java.lang.UnsatisfiedLinkError: org.gdal.gdal.gdalJNI.AllRegister()V
> at org.gdal.gdal.gdalJNI.AllRegister(Native Method)
> at org.gdal.gdal.gdal.AllRegister(gdal.java:499)
> at cn.edu.pku.extremetool.Main.main(Main.java:21)

###  **java.lang.UnsatisfiedLinkError: D:WorkSpaceeclipseWPMJunoExtremeToolgdaljni.dll:Can’t find dependent libraries** 

有引用了gdal.jar包 ，也将release/gdal/java的**gdaljni.dll**文件拷贝到正确位置，但是缺少GDAL本身的dll（即release-1600-gdal/bin下的众多dll文件）

> Native library load failed.
> java.lang.UnsatisfiedLinkError: D:WorkSpaceeclipseWPMJunoExtremeToolgdaljni.dll:  **Can’t find dependent libraries** 
> Exception in thread “main” java.lang.UnsatisfiedLinkError: org.gdal.gdal.gdalJNI.AllRegister()V
> at org.gdal.gdal.gdalJNI.AllRegister(Native Method)
> at org.gdal.gdal.gdal.AllRegister(gdal.java:499)
>
> at cn.edu.pku.extremetool.Main.main(Main.java:21)

### 找不到proj.db

在系统环境变量中配置，`PROJ_LIB：C:Program FilesGDALprojlibproj.db` 配置完后记得重启或注销计算机使配置⽣效

![img](https://img2020.cnblogs.com/blog/771906/202011/771906-20201130163115834-1398288475.png)

```sh
变量名：PROJ_LIB
变量值：C:\Program Files\GDAL\projlibproj.db #修改为自己存储的projlibproj.db文件路径
```

> 如果没有该文件，下载[gdal core](https://github.com/OSGeo/gdal/tags)进行安装。

### 如果提示缺失DLL文件（如gdal304.dll），需要配置环境变量

在path变量中添加包含dll的文件夹路径（如C:\gdal\bin），gdal-data文件夹（如C:\gdal\bin\gdal-data）添加到path或新建GDAL_DATA变量

### 需要注意的是Java和GDAL 或者都是X86 (Win32)版,或者都是X64 (Win64)版,否则可能存在兼容性问题。

### 使用maven 中央仓库的 gdal 报错

```java
java.lang.UnsatisfiedLinkError: org.gdal.gdal.gdalJNI.Dataset_SWIGUpcast(J)J
at org.gdal.gdal.gdalJNI.Dataset_SWIGUpcast(Native Method)
```

没有出现`Native library load failed.`说明已经正确配置好了 dll
那么上述错误的原因何在？

—— SWIG 版本不一致
maven中央仓库中的 `gdal.jar` 与我们下载使用的 gdal dll 文件，不是由同一人/团队 编译生成的！gisinternals 很有可能使用的 SWIG 1.3.4 版本，而maven仓库中的gdal使用的 SWIG版本 暂时不知道，可能是 SWIG 2.x 版本。
**解决方案**
\- 找到与 maven 仓库中的 gdal.jar 一致的 dll 文件 —— 暂时没找到
\- 不使用 maven 中央仓库的 jar，**使用 gisinternals 提供的 jar 文件**！

## 相关文档

GDAL项目[官方网站](http://www.gdal.org)：http://www.gdal.org

[GDAL在Java中的使用](http://trac.osgeo.org/gdal/wiki/GdalOgrInJava)：http://trac.osgeo.org/gdal/wiki/GdalOgrInJava

[GDAL for Java Api文档](https://gdal.org/java/org/gdal/gdal/gdal.html)：https://gdal.org/java/org/gdal/gdal/gdal.html

[GDAL相关使用](https://www.jianshu.com/p/ecf1b369f195)：https://www.jianshu.com/p/ecf1b369f195

[gdal for java 从安装到各种案例demo实现](https://blog.csdn.net/qq_42522024/article/details/123838982?spm=1001.2101.3001.6650.1&utm_medium=distribute.pc_relevant.none-task-blog-2%7Edefault%7ECTRLIST%7Edefault-1-123838982-blog-114399800.pc_relevant_default&depth_1-utm_source=distribute.pc_relevant.none-task-blog-2%7Edefault%7ECTRLIST%7Edefault-1-123838982-blog-114399800.pc_relevant_default&utm_relevant_index=2)：https://blog.csdn.net/qq_42522024/article/details/123838982?spm=1001.2101.3001.6650.1&utm_medium=distribute.pc_relevant.none-task-blog-2%7Edefault%7ECTRLIST%7Edefault-1-123838982-blog-114399800.pc_relevant_default&depth_1-utm_source=distribute.pc_relevant.none-task-blog-2%7Edefault%7ECTRLIST%7Edefault-1-123838982-blog-114399800.pc_relevant_default&utm_relevant_index=2

# Linux系统编译、发布gdal项目 https://www.jianshu.com/p/ff4cf2b59613

## 编译GDAL依赖

### 1、安装 sqlite3

```bash
wget https://www.sqlite.org/2020/sqlite-src-3310100.zip
unzip sqlite-src-3310100.zip
cd sqlite-src-3310100
bash ./configure --prefix=/usr/local/pgsql/plugin/sqlite3
make && make install
echo "/usr/local/pgsql/plugin/sqlite3/lib" > /etc/ld.so.conf.d/sqlite3.conf
ldconfig
```

### 2、安装 json-c-0.13.1

```swift
wget https://github.com/json-c/json-c/archive/json-c-0.13.1-20180305.tar.gz
tar -xzvf  json-c-0.13.1-20180305.tar.gz
cd json-c-json-c-0.13.1-20180305
bash ./configure --prefix=/usr/local/pgsql/plugin/json-c
make && make install
echo "/usr/local/pgsql/plugin/json-c/lib" > /etc/ld.so.conf.d/json-c-0.13.1.conf
ldconfig
```

### 3、编译 protobuf-3.11.4,protobuf-c 1.3.3

- 编译protobuf-3.11.4

```go
wget https://github.com/protocolbuffers/protobuf/releases/download/v3.11.4/protobuf-cpp-3.11.4.tar.gz
tar -xzvf  protobuf-cpp-3.11.4.tar.gz
cd protobuf-3.11.4
bash ./configure
make && make install
```

- 编译protobuf-c 1.3.3

```go
wget https://github.com/protobuf-c/protobuf-c/releases/download/v1.3.3/protobuf-c-1.3.3.tar.gz
tar -xzvf  protobuf-c-1.3.3.tar.gz
cd protobuf-c-1.3.3
./autogen.sh
bash ./configure
make && make install
```

出现找不到`libprotobuf.so.22`时，`vi /etc/profile`  #添加以下配置

```sh
PKG_CONFIG_PATH=/usr/local/lib/pkgconfig:$PKG_CONFIG_PATH
export PKG_CONFIG_PATH
```

`source /etc/profile`  #配置生效

### 4、安装Proj 6.3.1

```bash
wget https://download.osgeo.org/proj/proj-6.3.1.tar.gz
tar -xf proj-6.3.1.tar.gz
cd proj-6.3.1
./configure --prefix=/usr/local/pgsql/plugin/proj
make && make install
echo "/usr/local/pgsql/plugin/proj/lib" > /etc/ld.so.conf.d/proj-6.3.1.conf
ldconfig
```

### 5、安装GEOS 3.8.0

```bash
wget http://download.osgeo.org/geos/geos-3.8.0.tar.bz2
tar -jxf geos-3.8.0.tar.bz2
cd geos-3.8.0
./configure --prefix=/usr/local/pgsql/plugin/geos
make && make install
echo "/usr/local/pgsql/plugin/geos/lib" > /etc/ld.so.conf.d/geos-3.8.0.conf
ldconfig
```

### 6、安装SFCGAL 1.3.7

由于SFCGAL需要依赖Boost、CGAL、GMP、MPFR这四个软件，所以具体总共需要安装以下四个软件：
 boost-devel.x86_64
 gmp-devel.x86_64
 mpfr-devel.x86_64
 CGAL-4.14

#### 6.1 Boost-1.5.3

```undefined
yum install boost boost-devel
```

#### 6.2 GMP-6.1.2

```go
wget https://gmplib.org/download/gmp/gmp-6.1.2.tar.bz2
tar -jxvf gmp-6.1.2.tar.bz2
cd gmp-6.1.2
bash ./configure --enable-cxx
make && make install
```

#### 6.3 MPFR-4.0.2

```go
wget https://www.mpfr.org/mpfr-current/mpfr-4.0.2.tar.gz
tar -xzvf  mpfr-4.0.2.tar.gz
cd mpfr-4.0.2
./configure
make && make install
```

#### 6.4 cgal-4.14

```go
wget http://distfiles.macports.org/cgal/cgal-4.14.tar.xz
xz -d cgal-4.14.tar.xz 
tar xvf cgal-4.14.tar 
cd CGAL-4.14
mkdir build && cd build
cmake ..
make && make install
```

安装过程中会出现以下错误

![img](https://upload-images.jianshu.io/upload_images/18566927-5ed512cca0ee048c.png?imageMogr2/auto-orient/strip|imageView2/2/w/576/format/webp)

解决方法： 修改/usr/include/boost/ cstdint.hpp 44行代码为：

![img](https://upload-images.jianshu.io/upload_images/18566927-9743532a7cdfb461.png?imageMogr2/auto-orient/strip|imageView2/2/w/576/format/webp)

```ruby
#if defined(BOOST_HAS_STDINT_H) \
&& (!defined(__GLIBC__) \
|| defined(__GLIBC_HAVE_LONG_LONG) \
|| (defined(__GLIBC__) && ((__GLIBC__ > 2) || ((__GLIBC__ == 2) && (__GLIBC_MINOR__ >= 17)))))
```

#### 6.5 SFCGAL 1.3.7

```bash
wget  https://github.com/Oslandia/SFCGAL/archive/v1.3.7.tar.gz
tar -zxvf SFCGAL-1.3.7.tar.gz
cd SFCGAL-1.3.7  
mkdir build && cd build 
cmake -DCMAKE_INSTALL_PREFIX=/usr/local/sfcgal ..
make  && make install
echo "/usr/local/sfcgal/lib64" > /etc/ld.so.conf.d/sfcgal-1.3.7.conf
ldconfig
```

### 7、pcre-8.44安装

```bash
wget https://ftp.pcre.org/pub/pcre/pcre-8.44.tar.gz
tar -xzvf  pcre-8.44.tar.gz
cd pcre-8.44
./configure --enable-utf8 --prefix=/usr/local/pcre
make && make intall
echo "/usr/local/pcre/lib" > /etc/ld.so.conf.d/pcre-8.44.conf
ldconfig
```

### 8、libxml2-2.9.10安装

```bash
wget http://xmlsoft.org/sources/libxml2-2.9.10.tar.gz
tar -xzvf  libxml2-2.9.10.tar.gz
cd libxml2-2.9.10
./configure --prefix=/usr/local/libxml2
make && make intall
echo "/usr/local/libxml2/lib" > /etc/ld.so.conf.d/libxml2-2.9.10.conf
ldconfig
```

### 9、编译gdal.jar进行 java开发，不需要开发，可以跳过

```ruby
wget https://mirrors.tuna.tsinghua.edu.cn/apache//ant/binaries/apache-ant-1.10.7-bin.tar.gz
tar -zxvf apache-ant-1.10.7-bin.tar.gz
mv apache-ant-1.10.7-bin /usr/local/ant
```

#### 9.2、swig 4.0.1

```go
wget http://prdownloads.sourceforge.net/swig/swig-4.0.1.tar.gz
tar -zxvf  swig-4.0.1.tar.gz
cd swig-4.0.1
bash ./configure --prefix=/usr/local/swig
make && make install
```

#### 9.3、配置profile

`vim /etc/profile`

```bash
ANT_HOME=/usr/local/ant
SWIG_HOME=/usr/local/swig

PATH=$ANT_HOME/bin:$SWIG_HOME/bin:$PATH

export ANT_HOME SWIG_HOME
```

`source /etc/profile`

查看版本

> ant -version
> swig -version

## 安装GDAL 3.0.4

### 1、编译

```jsx
wget http://download.osgeo.org/gdal/3.0.4/gdal-3.0.4.tar.gz
tar -xf gdal-3.0.4.tar.gz
cd gdal-3.0.4
bash ./configure --prefix=/usr/local/pgsql/plugin/gdal --with-proj=/usr/local/pgsql/plugin/proj --with-geos=/usr/local/pgsql/plugin/geos/bin/geos-config --with-sqlite3=/usr/local/pgsql/plugin/sqlite3 --with-libjson-c=/usr/local/pgsql/plugin/json-c
make && make install
echo "/usr/local/pgsql/plugin/gdal/lib" > /etc/ld.so.conf.d/gdal-3.0.4.conf
ldconfig
```

### 2、错误

> 错误1：修改/usr/local/src/gdal-3.0.4/gcore/gdal_version.h.in为gdal_version.h

![img](https://upload-images.jianshu.io/upload_images/18566927-9455d2b776f2a048.png?imageMogr2/auto-orient/strip|imageView2/2/w/698/format/webp)

> 错误2：出现这个错误时，一般是sqlite3生成文件在/usr/local/lib下，必须移除后，重新编译sqlite3生成文件单独放置，如usr/local/sqlite3下，在gdal配置时指明--with-sqlite3
>
> 清除语句:rm要慎用，要确认清楚，删库跑路就是因为这 rm -rf /usr/local/lib/libsqlite3* /usr/local/bin/sqlite3 /usr/local/lib/pkgconfig/sqlite3.pc

![img](https://upload-images.jianshu.io/upload_images/18566927-5c1a2f75f10bcfc5.png?imageMogr2/auto-orient/strip|imageView2/2/w/698/format/webp)

> 错误3：缺少json-c静态库文件,可以通过指明静态库路径方式来解决，但这是临时解决方案。最好重新编译json-c到usr/local/json-c, 在gdal配置时指明--with-libjson-c

```bash
cd /usr/local/src/gdal-3.0.4/apps
/bin/sh /usr/local/src/gdal-3.0.4/libtool --mode=link --silent g++ -std=c++11  gdalserver.lo  /usr/local/src/gdal-3.0.4/libgdal.la /usr/local/lib/libjson-c.a -o gdalserver
```

![img](https://upload-images.jianshu.io/upload_images/18566927-2db2c35624fc1767.png?imageMogr2/auto-orient/strip|imageView2/2/w/698/format/webp)

> 错误4： make: execvp: /usr/local/gdal-3.0.4/install-sh: Permission denied

```undefined
看到这个问题，是不是很高兴，表明已经编译完成了，执行安装脚本，没权限
执行  chmod 777  install-sh，赋予最高权限
```

### 3 配置GDAL环境变量

通过vi /etc/profile去配置gdal/lib、gdal/bin、gdal/data吧，也可以把profile下载到本地改完再上传

```bash
GDAL_HOME=/usr/local/pgsql/plugin/gdal
GDAL_DATA=$GDAL_HOME/share/gdal

LD_LIBRARY_PATH=$GDAL_HOME/lib:/usr/local/lib64:$JRE_HOME/lib:$LD_LIBRARY_PATH

PATH=$GDAL_HOME/bin:$PATH

export ... PATH LD_LIBRARY_PATH GDAL_DATA
```

### 4 GDAL安装完成，看看版本号和支持的数据格式吧

gdalinfo --version
ogr2ogr --formats

![img](https://upload-images.jianshu.io/upload_images/18566927-a42a6d3b2bafbbd9.png?imageMogr2/auto-orient/strip|imageView2/2/w/626/format/webp)

## 编译生成gdal.jar

### 配置java.opt

```bash
修改文件：/usr/local/src/gdal-3.0.4/swig/java/java.opt，修改JAVA_HOME的值为java的根目录
JAVA_HOME = /usr/java/jdk1.8.0_221
```

###  编译

```bash
cd /usr/local/src/gdal-3.0.4/swig/java
make && make install
```

## JavaWeb应用的 gdal环境配置

#### 1、整理文件，把生成的文件放置到gdal/java目录

```bash
mkdir -p /usr/local/pgsql/plugin/gdal/java
cp /usr/local/src/gdal-3.0.4/swig/java/gdal.jar  /usr/local/pgsql/plugin/gdal/java
cp /usr/local/src/gdal-3.0.4/swig/java/.libs/*.so /usr/local/pgsql/plugin/gdal/java
```

#### 2、在centos下, java.library.path=/usr/java/packages/lib/amd64，放置libgdalalljni.so到此目录，javaWeb项目从这里加载libgdalalljni.so动态库

```bash
cp /usr/local/src/gdal-3.0.4/swig/java/.libs/*.so /usr/java/packages/lib/amd64
```

#### 3、把/swig/java中生成的gdal.jar文件复制到jre/lib/ext，jvm启动加载

```bash
cp /usr/local/src/gdal-3.0.4/swig/java/gdal.jar  /usr/java/jdk1.8.0_221/jre/lib/ext

vi /etc/profile
CLASS_PATH=$JAVA_HOME/jre/lib/ext/gdal.jar:$CLASS_PATH
source /etc/profile
```

#### 4、在/usr/local/gdal-3.0.4/swig/java/目录下复制gdal.jar、libgdalalljni.so文件到项目resources/gdal/linux

![img](https://upload-images.jianshu.io/upload_images/18566927-d2a91808dd11adb7.png?imageMogr2/auto-orient/strip|imageView2/2/w/576/format/webp)

#### 5、启动JavaWeb看看吧

## 可能出现的问题

> 错误1： java.lang.UnsatisfiedLinkError: Native Library /usr/java/packages/lib/amd64/libgdalalljni.so already loaded in another classloader

```css
jvm启动时已经加载过libgdalalljni.so，不能能再次加载了，在linux系统上无需代码加载libgdalalljni.so
```

> 错误2：libpq.so.5: cannot open shared object file: No such file or directory 原因是未设置pg库到系统动态库配置请设置ld.so.conf

```bash
echo '/usr/local/pgsql/lib' >> /etc/ld.so.conf
ldconfig
```

> 错误3：nested exception is java.lang.UnsatisfiedLinkError: org.gdal.ogr.ogrJNI.RegisterAll()V

```bash
gdal.jar未放置到/usr/java/jdk1.8.0_221/jre/lib/ext

vi /etc/profile
CLASS_PATH=$JAVA_HOME/jre/lib/ext/gdal.jar:$CLASS_PATH
source /etc/profile
```

# 相关信息说明

https://blog.csdn.net/qq_42522024/article/details/123838982

## 1、连接数据源

### 1.1、**连接之前**

~~~java
/*
准备工作
*/
gdal.AllRegister();
//注册所有驱动
ogr.RegisterAll();
//支持中文路径
gdal.SetConfigOption("GDAL_FILENAME_IS_UTF8", "YES");
//属性表支持中文
gdal.SetConfigOption("SHAPE_ENCODING", "CP936");
~~~

### 1.2、数据源驱动
~~~java
/*
5种主要驱动程序的名称
 */
public class DriverName {
    
    //String connShp = "D:\\DIST\\Code\\Project\\zhenjiang\\镇江shp\\五角星.shp";
    public static final String shp = "ESRI Shapefile";
    
    //String connGdb = "D:\\DIST\\geoData\\shpFile\\unionResult.gdb";
    public static final String gdb = "FileGDB";//或者OpenFileGDB
    
    //String connPg = "PG:dbname=dggis host=192.168.200.34 port=30013 user=sde password=sde";
    public static final String postgreSQL = "PostgreSQL";
    
    //String connStr = "OCI:sde/sde:SYS.Yjjbnt";//连接本地库可以忽略数据库实例
    //String connSde = "OCI:gis_qf/Passw0rd@192.168.1.163/orcl:CHDYZT.JSFAFW";
    //String connStr = "OCI:rcgtkjgh/pass@192.168.200.230:1521/orcl:RCGTKJGH.DELSH_STBHHX";
    public static final String sde = "OCI";
    
    //String connMdb = "D:\\DIST\\geoData\\shpFile\\test.mdb";
    public static final String mdb = "PGeo";
}
~~~

### 1.3、连接数据源的基本过程

```java
/*
连接数据源的基本过程
*/
1、根据驱动名称获取驱动
    Driver driver = ogr.GetDriverByName(driverName);
2、通过驱动打开数据源
    DataSource dataSource = driver.Open(FilePath, 0);//文件路径或者连接字符串，0表示不更新数据集，为只读
3、获取数据源里的图层
    dataSource.GetLayer(index:)//根据id获取
    dataSource.GetLayer(layerName:"")//根据名称获取
4、获取图层里的要素
    //读取GDB里面的图层时，获取Feature得用GetNextFeature()方法，不能根据GetFeature(long fid)	
    layer.GetFeature(int:);//根据id

	layer.ResetReading();//把要素读取顺序重置为从第一个开始
	layer.GetNextFeature();

```

### 1.4、读取数据基本信息

```java
读取图层数量：
        int layerCount = dataSource.GetLayerCount();
图层名称
        String layerName = layer.GetName();
图层要素数量
        long featureCount = layer.GetFeatureCount();
图层空间参考信息
        SpatialReference s = layer.GetSpatialRef();
图层的属性表结构
        FeatureDefn featureDefn = layer.GetLayerDefn();
属性表字段数量
        int fieldCount = featureDefn.GetFieldCount();
属性表的属性字段
        FieldDefn fieldDefn = featureDefn.GetFieldDefn(i1);//根据索引获取
属性字段类型
        int fieldType = fieldDefn.GetFieldType();
        String fieldTypeName = fieldDefn.GetFieldTypeName(fieldType);
属性字段名称
        String fieldName = fieldDefn.GetName();
获取FID
        long fid = feature.GetFID();//这个是通过Feature来获取的
获取Geometry
    	Geometry geometry = feature.GetGeometryRef();
    	String geoJson = geometry.ExportToJson();
{ "type": "Polygon", "coordinates": [ [ [ 119.456586303, 32.063698523000028, 0.0 ], [ 119.468721554000012, 32.045852565000018, 0.0 ], [ 119.490850540999986, 32.040141859000016, 0.0 ], [ 119.472290745, 32.028006608, 0.0 ], [ 119.478001451999944, 32.003736104999973, 0.0 ], [ 119.457300141000019, 32.023723577999988, 0.0 ], [ 119.43659882999998, 32.002308428999982, 0.0 ], [ 119.443023375000053, 32.025865093, 0.0 ], [ 119.422322064000014, 32.034431152000025, 0.0 ], [ 119.445878728000025, 32.043711050000013, 0.0 ], [ 119.456586303, 32.063698523000028, 0.0 ] ] ] }
获取图层范围
		double[] extent = layer.GetExtent();//返回4个坐标点

```

## 2、关于投影

```java
SpatialReference spatialReference = layer.GetSpatialRef();//获取图层的空间信息
//设定空间信息
//通过EPSG
SpatialReference spatialReference = new SpatialReference();
spatialReference.ImportFromEPSG(4490);
//通过WKT字符串
String strwkt = "GEOGCS[\"GCS_North_American_1927\"," +
                "DATUM[\"North_American_Datum_1927\"," +
                "SPHEROID[\"Clarke_1866\",6378206.4,294.9786982]]," +
                "PRIMEM[\"Greenwich\",0]," +
                "UNIT[\"Degree\",0.0174532925199433]]";
SpatialReference spatialReference = new SpatialReference(strwkt);
```

投影信息如下（WKT字符串）:

~~~bash
PROJCS[“CGCS2000 / 3-degree Gauss-Kruger CM 117E”, //投影名称
GEOGCS[“China Geodetic Coordinate System 2000”, //地理坐标系名称
DATUM[“China_2000”, //水平基准面
SPHEROID[“CGCS2000”,6378137,298.257222101,//椭球体、长半轴，反偏率
AUTHORITY[“EPSG”,“1024”]],
AUTHORITY[“EPSG”,“1043”]],
PRIMEM[“Greenwich”,0, //中央经线Greenwich，0度标准子午线
AUTHORITY[“EPSG”,“8901”]],
UNIT[“degree”,0.0174532925199433, //指定测量使用的单位。在地理坐标系下使用角度。
AUTHORITY[“EPSG”,“9122”]],
AUTHORITY[“EPSG”,“4490”]],
PROJECTION[“Transverse_Mercator”], //投影方式
PARAMETER[“latitude_of_origin”,0], //PARAMETER表示投影参数,0表示纬度起点为0度
PARAMETER[“central_meridian”,117], //投影带的中央经线是东经117度
PARAMETER[“scale_factor”,1], //中央经线的长度比是1
PARAMETER[“false_easting”,500000], //坐标纵轴向西移动500km
PARAMETER[“false_northing”,0], //横轴没有平移
UNIT[“metre”,1, //指定测量使用的单位，指定米为测量单位
AUTHORITY[“EPSG”,“9001”]],
AXIS[“Northing”,NORTH],
AXIS[“Easting”,EAST],
AUTHORITY[“EPSG”,“4548”]]
~~~

## 3、属性查询

### 3.1 通过图层layer查询

```java
//查询没有发生错误则返回0，注意此时的layer只包含 满足条件的要素
int result = layer.SetAttributeFilter("YSDM='3002020100'");//属性查询时注意不能用字段别名
//参数设为null表示清空查询
layer.SetAttributeFilter(null);
```

### 3.2 通过数据源datasource查询

```java
Layer queryLayer = dataSource.ExecuteSQL("select * from union5 where YSDM='3002020100'");
```

## 4、地理处理

```java
/**
 * 两个图层之间的地理处理操作
 * @param inputLayer：输入图层
 * @param queryLayer：求交图层
 * @param resultLayer：返回结果图层
 * @param spatialFilter：地理处理操作
 */
public static void spatialQuery(Layer inputLayer,Layer queryLayer,Layer resultLayer,int spatialFilter){

    Vector v = new Vector(4);
    v.add("SKIP_FAILURES=YES");//跳过处理过程中出现的错误
    v.add("PROMOTE_TO_MULTI=NO");//Polygon不转为MultiPolygon，如果设为YES则会
    v.add("INPUT_PREFIX=1_");//输入图层在属性表中的字段前缀
    v.add("METHOD_PREFIX=2_");//求交图层的字段前缀
    switch (spatialFilter){
        //相交
        case 0:
            inputLayer.Intersection(queryLayer,resultLayer,v,null);
            break;
        //合并
        case 1:
            inputLayer.Union(queryLayer,resultLayer,v,null);
    }

}
```

## 5、创建图层

```java
/**
 * 创建矢量图层
 * @param driverName:驱动名称
 * @param path:图层保存路径，要和驱动匹配
 * @param layerName：图层名称
 * @param spatialReference：图层空间参考
 * @return 返回创建好的图层
 */
public static Layer createLayer(String driverName, String path, String layerName, SpatialReference spatialReference) {
    Layer result = null;

    Driver driver = ogr.GetDriverByName(driverName);
    if (driver == null) {
        log.info(driverName + "不可用");
        System.out.println(driverName + "不可用");
        return null;
    }
    DataSource dataSource = null;
    //这里需要判断一下path是否已经存在，存在的话先删除再创建(如果是已存在的gdb，则直接打开)
    File file = new File(path);
    if (file.exists()) {
        if (file.isFile()) {
            file.delete();
            dataSource = driver.CreateDataSource(path, null);
        } else if (file.isDirectory()) {
            dataSource = driver.Open(path, 1);
            //GDB中存在同名图层则删除
            for (int i = 0; i < dataSource.GetLayerCount(); i++) {
                Layer layer = dataSource.GetLayer(i);
                if (layerName.equals(layer.GetName())) {
                    dataSource.DeleteLayer(i);
                    dataSource.FlushCache();
                }
            }
        }
    } else {
        dataSource = driver.CreateDataSource(path, null);
    }
    if (dataSource == null) {
        log.info("数据源创建/打开失败");
        System.out.println("数据源创建/打开失败");
        return null;
    }
    result = dataSource.CreateLayer(layerName, spatialReference, ogr.wkbPolygon, null);
    if (result == null) {
        log.info(layerName + "创建失败");
        System.out.println(layerName + "创建失败");
        return null;
    }
    log.info("【"+layerName+"】" + "创建成功");
    return result;
}
```

## 6、创建要素

```java
/**
 * 传入Geometry创建Feature，这里不定义属性字段
 * @param layer
 * @param geometry
 */
public static void createFeatureByGeometry(Layer layer, Geometry geometry) {
    FeatureDefn featureDefn = layer.GetLayerDefn();
    Feature feature = new Feature(featureDefn);
    feature.SetGeometry(geometry);
    layer.CreateFeature(feature);
}
```



