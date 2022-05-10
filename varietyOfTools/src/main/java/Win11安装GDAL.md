# windows环境下JAVA+GDAL

## 1.GDAL下载

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



## 异常问题：

1. ### 引用库报错 **java.lang.UnsatisfiedLinkError: no gdaljni in java.library.path**

   工程中只引用了gdal.jar包，没有任何gdal_java的dll，也就是gdal的dll文件拷贝位置不正确，导致java程序不能找到相应的dll引用。将dll拷贝到**%JAVA_HOME%/jdk/bin**或**%JAVA_HOME%/jre/bin**目录下

> Native library load failed.
> java.lang.UnsatisfiedLinkError: **no gdaljni in java.library.path**
> Exception in thread “main” java.lang.UnsatisfiedLinkError: org.gdal.gdal.gdalJNI.AllRegister()V
> at org.gdal.gdal.gdalJNI.AllRegister(Native Method)
> at org.gdal.gdal.gdal.AllRegister(gdal.java:499)
> at cn.edu.pku.extremetool.Main.main(Main.java:21)

2. ###  **java.lang.UnsatisfiedLinkError: D:WorkSpaceeclipseWPMJunoExtremeToolgdaljni.dll:Can’t find dependent libraries** 

   有引用了gdal.jar包 ，也将release/gdal/java的**gdaljni.dll**文件拷贝到正确位置，但是缺少GDAL本身的dll（即release-1600-gdal/bin下的众多dll文件）

> Native library load failed.
> java.lang.UnsatisfiedLinkError: D:WorkSpaceeclipseWPMJunoExtremeToolgdaljni.dll:  **Can’t find dependent libraries** 
> Exception in thread “main” java.lang.UnsatisfiedLinkError: org.gdal.gdal.gdalJNI.AllRegister()V
> at org.gdal.gdal.gdalJNI.AllRegister(Native Method)
> at org.gdal.gdal.gdal.AllRegister(gdal.java:499)
>
> at cn.edu.pku.extremetool.Main.main(Main.java:21)

3. ### 找不到proj.db

   在系统环境变量中配置，`PROJ_LIB：C:Program FilesGDALprojlibproj.db` 配置完后记得重启或注销计算机使配置⽣效

   ![img](https://img2020.cnblogs.com/blog/771906/202011/771906-20201130163115834-1398288475.png)

   ```sh
   变量名：PROJ_LIB
   变量值：C:\Program Files\GDAL\projlibproj.db #修改为自己存储的projlibproj.db文件路径
   ```

   > 如果没有该文件，下载[gdal core](https://github.com/OSGeo/gdal/tags)进行安装。
   
4. ### 如果提示缺失DLL文件（如gdal304.dll），需要配置环境变量

      在path变量中添加包含dll的文件夹路径（如C:\gdal\bin），gdal-data文件夹（如C:\gdal\bin\gdal-data）添加到path或新建GDAL_DATA变量

5. ### 需要注意的是Java和GDAL 或者都是X86 (Win32)版,或者都是X64 (Win64)版,否则可能存在兼容性问题。

## 相关文档

GDAL项目[官方网站](http://www.gdal.org)：http://www.gdal.org

[GDAL在Java中的使用](http://trac.osgeo.org/gdal/wiki/GdalOgrInJava)：http://trac.osgeo.org/gdal/wiki/GdalOgrInJava

[GDAL for Java Api文档](https://gdal.org/java/org/gdal/gdal/gdal.html)：https://gdal.org/java/org/gdal/gdal/gdal.html