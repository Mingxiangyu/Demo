# 动手实现Holocubic

## 准备

### 焊接工具清单

| 是否必须 |               物料                |  价格  |                           购买地址                           | 型号                                          |                             链接                             |
| -------- | :-------------------------------: | :----: | :----------------------------------------------------------: | --------------------------------------------- | :----------------------------------------------------------: |
| &#10004; |              电焊笔               | 19.80  |       [**艾瑞泽旗舰店**](https://airuizewj.tmall.com/)       | 60W机械调温（速热恒温）6件套【配收纳盒】      | https://detail.tmall.com/item.htm?id=533283580098&spm=a1z09.2.0.0.31152e8dB8LXcd&_u=s119dtpbc8b3 |
| &#10004; |           电焊笔头-刀头           |  3.50  |          **[天工配件](https://13961.taobao.com/)**           | 柠檬黄[大刀头1个]                             | https://item.taobao.com/item.htm?spm=a1z09.2.0.0.31152e8dB8LXcd&id=19033217031&_u=s119dtpb227d |
| &#10004; |              焊锡丝               |        |           买电焊笔一般会送，如果自己采购选择63%的            |                                               |                                                              |
| &#10004; |             松香-助焊             |  6.49  |          **[天工配件](https://13961.taobao.com/)**           | 50G【铝盒装】高纯度松香                       | https://item.taobao.com/item.htm?spm=a1z09.2.0.0.31152e8dB8LXcd&id=586236371841&_u=s119dtpbfc88 |
| &#10004; | 焊锡膏-助焊<br />（效果比松香好） | *9.80* |    **[深圳工具批发](https://shop322187831.taobao.com/)**     | 金鸡环保焊锡膏100g 1瓶包邮                    | https://item.taobao.com/item.htm?spm=a1z09.2.0.0.31152e8dB8LXcd&id=557957630716&_u=s119dtpb7476 |
| &#10004; |               锡浆                | 13.80  |        [**安立信旗舰店**](https://anlixin.tmall.com/)        | 183℃中温有铅锡膏30G/支(常用款)【送推杆/针头】 | https://detail.tmall.com/item.htm?id=626569273184&spm=a1z09.2.0.0.31152e8dB8LXcd&_u=s119dtpb2028 |
| &#10004; |              拆焊台               | 21.80  |    [**鹿仙子五金专营店**](https://luxianziwj.tmall.com/)     | 【大功率400W】预热板+8件                      | https://detail.tmall.com/item.htm?id=591896261597&spm=a1z09.2.0.0.31152e8dB8LXcd&_u=s119dtpbcd38 |
| &#10004; |               镊子                |  1.25  |  **[深圳市吉兴达电子](https://shop148999102.taobao.com/)**   | 防静电尖头特尖镊11                            | https://item.taobao.com/item.htm?spm=a1z09.2.0.0.31152e8dB8LXcd&id=553391976137&_u=s119dtpb39e1 |
| &#10004; |              洗板水               | 15.00  |        **[广州楷翔电子](https://ebs03.taobao.com/)**         | I6 500ML水+针咀瓶+刷子                        | https://item.taobao.com/item.htm?spm=a1z09.2.0.0.31152e8dB8LXcd&id=9893313086&_u=s119dtpbd369 |
|          |        酒精瓶按压式喷水瓶         |  6.00  |   **[天胜五金配件店](https://shop156141875.taobao.com/)**    | 按压式180ML酒精瓶                             | https://item.taobao.com/item.htm?spm=a1z09.2.0.0.31152e8dB8LXcd&id=624331352100&_u=s119dtpbf637 |
|          |         磁性PCB夹 霞林柱          | 26.80  |   **[天胜五金配件店](https://shop156141875.taobao.com/)**    | 方形卡具【6磁柱】                             | https://item.taobao.com/item.htm?spm=a1z09.2.0.0.31152e8dB8LXcd&id=623047677822&_u=s119dtpbe946 |
|          |    多格整理零件电子元器件盒子     |  8.80  |   **[天胜五金配件店](https://shop156141875.taobao.com/)**    | 多功能收纳盒【1个】                           | https://item.taobao.com/item.htm?spm=a1z09.2.0.0.31152e8dB8LXcd&id=652504535581&_u=s119dtpb54af |
| &#10004; |              万用表               | 46.50  | [**德力西电气旗舰店**](https://delixidianqi.tmall.com/?spm=a220o.1000855.1997427721.d4918089.58ec4f9cosgeBw) | DEM11万用表【限量秒杀】                       | https://detail.tmall.com/item.htm?id=532558554291&spm=a1z09.2.0.0.31152e8dB8LXcd&_u=s119dtpbf707 |
|          |        焊接放大镜辅助夹具         | 24.90  |  **[深圳市吉兴达电子](https://shop148999102.taobao.com/)**   | 黑色                                          | https://item.taobao.com/item.htm?spm=a1z09.2.0.0.31152e8dB8LXcd&id=583256956902&_u=s119dtpbb08e |
|          |              吸锡带               |        |                                                              |                                               |                                                              |

### 元器件清单

|          | 元器件名称                                                  |        物料         |    价格（元）    |                           购买地址                           |                             链接                             | 备注                                                         |
| -------- | ----------------------------------------------------------- | :-----------------: | :--------------: | :----------------------------------------------------------: | :----------------------------------------------------------: | ------------------------------------------------------------ |
| &#10004; |                                                             |       主板PCB       |        0         |                           立创商城                           | [嘉立创下单教程  ](https://www.bilibili.com/video/BV1AZ4y1V7CX?from=search&seid=12288439115622007662&spm_id_from=333.337.0.0) （每个月两单免费） |                                                              |
| &#10004; |                                                             |       屏幕PCB       |        0         |                           立创商城                           | [嘉立创下单教程  ](https://www.bilibili.com/video/BV1AZ4y1V7CX?from=search&seid=12288439115622007662&spm_id_from=333.337.0.0) （每个月两单免费） |                                                              |
| &#10004; |                                                             |      分光棱镜       | 86.4（优惠后70） |  **[光学镜片加工厂家](https://shop393484141.taobao.com/)**   | 38，这要去以他于和生时生天啊 https://m.tb.cn/h.f8IMX7r?sm=d13b56 稚晖君同款 分光棱镜25.4mm半透半反 HoloCubic光学影像分光投射 | 客服提 aio固件群来的，有优惠额                               |
|          |                                                             |        OCA胶        |                  |                                                              |                                                              |                                                              |
|          |                                                             |        UV胶         |                  |                                                              |                                                              |                                                              |
|          |                                                             |       b7000胶       |                  |                                                              |                                                              |                                                              |
| &#10004; | 'LCD-240x240                                                |        屏幕         |     *13.00*      |      **[中景园电子](https://zjy-display.taobao.com/)**       | https://item.taobao.com/item.htm?spm=a1z09.2.0.0.31152e8dB8LXcd&id=565306950948&_u=s119dtpbfe22 | 型号：焊接式12Pin裸屏玻璃品牌：翰彩                          |
| &#10004; | 'TF-Socket                                                  |       TF卡槽        |       2.55       |          **[zave旗舰店](https://zave.tmall.com/)**           | 89嘻心她的人他于和年下为们哈 https://m.tb.cn/h.fjHJsPD?sm=d22864 SD/MINI/TF/MICRO卡座卡槽卡托卡套 大小/长短体带自弹式翻盖贴片 | 型号：翻盖内焊式                                             |
| &#10004; | 'XC6206P332MR                                               | '低压差线性稳压芯片 |        3         | [**深圳市行芯电子商城**](https://baiwangdadong.taobao.com/)  | 39啊然里有能他于和那和以去信 https://m.tb.cn/h.fjHsU4H?sm=438622 全新原装 进口 LP2992 LP2992AIM5X-3.3 丝印LFEA SOT23-5 稳压器 |                                                              |
| &#10004; | 'BH1750FVI-TR                                               |    光强度传感器     |       3.9        |        **[佑印旗舰店](https://youyinsm.tmall.com/)**         | https://detail.tmall.com/item.htm?id=661451560891&spm=a1z09.2.0.0.31152e8dB8LXcd&_u=s119dtpb445e |                                                              |
| &#10004; | 'RAINSUN                                                    |    2.4G陶瓷天线     |      *1.60*      | **[深圳市优信电子科技有限公司](https://youxin-electronic.taobao.com/)** | https://item.taobao.com/item.htm?spm=a1z09.2.0.0.31152e8dB8LXcd&id=558699169012&_u=s119dtpb1e46 |                                                              |
| &#10004; | 'MPU6050                                                    |     六轴传感器      |     *17.70*      | **[深圳市优信电子科技有限公司](https://youxin-electronic.taobao.com/)** | https://item.taobao.com/item.htm?spm=a1z09.2.0.0.31152e8dB8LXcd&id=522554835655&_u=s119dtpbf310 | 也可以买模块拆上面的mpu6050用*模块便宜*[模块链接](https://item.taobao.com/item.htm?spm=a1z09.2.0.0.31152e8dB8LXcd&id=522575310310&_u=s119dtpbf399) |
| &#10004; | 'ESP32_SIP                                                  |   ESP-32核心芯片    |     *13.60*      | **[深圳市优信电子科技有限公司](https://youxin-electronic.taobao.com/)** | https://item.taobao.com/item.htm?spm=a1z09.2.0.0.31152e8dB8LXcd&id=635409903966&_u=s119dtpb56cc |                                                              |
| &#10004; | 'CP2102                                                     |      'USB转TTL      |      *9.60*      | **[深圳市优信电子科技有限公司](https://youxin-electronic.taobao.com/)** | https://item.taobao.com/item.htm?spm=a1z09.2.0.0.31152e8dB8LXcd&id=522575454240&_u=s119dtpb25a4 |                                                              |
|          | 贴片 SOT-23 XC6206P332MR (662K) 3.3V 300MA 稳压芯片（20只） |    拓展板稳压器     |                  | **[深圳市优信电子科技有限公司](https://youxin-electronic.taobao.com/)** |       https://item.taobao.com/item.htm?id=522573858401       |                                                              |

> 因为立创商城新用户有30元优惠券，所以有部分元器件在**立创商城**付邮费8元白嫖了

|      | 元器件名称                                                  | 商品编号 |               购买地址               |      备注      |
| ---- | ----------------------------------------------------------- | :------: | :----------------------------------: | :------------: |
|      | USB连接器 / Type-C 母座 卧贴                                | C709357  | https://item.szlcsc.com/747093.html  |                |
|      | 发光二极管 / 5050 RGB集成光源 4Pin                          | C114586  | https://item.szlcsc.com/115830.html  |                |
|      | 贴片电阻 / 200Ω ±1%                                         | C114764  | https://item.szlcsc.com/116008.html  |                |
|      | FFC/FPC连接线 / FFC连接线 8P 间距0.5mm 长5CM 同向           |  C29534  |  https://item.szlcsc.com/30288.html  |                |
|      | 贴片电容(MLCC) / 100nF ±10% 16V                             |  C1525   |  https://item.szlcsc.com/1877.html   |                |
|      | 贴片电阻 / 1kΩ ±0.5%                                        | C2763771 | https://item.szlcsc.com/2893684.html |                |
|      | 三极管(BJT) / NPN 25V 500mA (200-350)                       |  C2146   |  https://item.szlcsc.com/2503.html   |                |
|      | 无线收发芯片 / ESP8266EX                                    |  C77967  |  https://item.szlcsc.com/79101.html  | 凑单，可不购卖 |
|      | 贴片电阻 / 0Ω ±5%                                           |  C21376  |  https://item.szlcsc.com/22093.html  |                |
|      | 各类开发板 / LDO方案验证板（A版）                           | C2842719 | https://item.szlcsc.com/3028920.html |      白嫖      |
|      | 贴片电容(MLCC) / 2.2nF ±10% 50V                             |  C1531   |  https://item.szlcsc.com/1883.html   |                |
|      | 场效应管(MOSFET) / P沟道 20V 4.3A                           | C700955  | https://item.szlcsc.com/736023.html  |                |
|      | 贴片电阻 / 12kΩ ±1%                                         | C114760  | https://item.szlcsc.com/116004.html  |                |
|      | 贴片电容(MLCC) / 10uF ±20% 6.3V                             |  C15525  |  https://item.szlcsc.com/16204.html  |                |
|      | 贴片电阻 / 10kΩ ±5%                                         | C140214  | https://item.szlcsc.com/151540.html  |                |
|      | FFC/FPC连接器 / 0.5mm P数:8 翻盖式 下接                     | C262657  | https://item.szlcsc.com/253916.html  |                |
|      | 贴片电容(MLCC) / 10nF ±10% 50V                              |  C60133  |  https://item.szlcsc.com/61185.html  |                |
|      | 贴片 SOT-23 XC6206P332MR (662K) 3.3V 300MA 稳压芯片（20只） |          |                                      |                |



## 常见问题解答

### 软件

 #### Altium Designer安装(群主提供版本(21.7.2),其他自行百度对应版本AD破解教程)

 1. 安装

解压之后，有两个文件。

-  Crack（破解文件
             ![img](https://docimg7.docs.qq.com/image/l8OQZPwhl1VDU1lE44Shxg.png?w=1064&h=596)        

-   Setup.exe（本体安装文件）              ![img](https://docimg5.docs.qq.com/image/fgPrVoHiJGOc11UQ1NfMbw.png?w=666&h=61)        

`Setup.exe 是安装文件，打开，一直点击下一步即可`

Setup.exe 是安装文件，打开，一直点击下一步即可



 2. 汉化中文

右上角齿轮图标点击后![image-20211223182705541](https://gitee.com/ming-xiangyu/Imageshack/raw/master/img/image-20211223182705541.png)

在弹出框中勾选这两个选项，然后重启软件，*汉化成功*

![image-20211223183023460](https://gitee.com/ming-xiangyu/Imageshack/raw/master/img/image-20211223183023460.png)

 3. 破解（未破解前，打开工程（open project）等和工程（project）相关文件都是灰色，无法操作）
- 安装完成后，打开软件 首次打开会提示创文件夹在文档里 同意就行

- 打开软件点击 `Add standalone license fil`

  ​                 ![img](https://docimg10.docs.qq.com/image/ZzGPt_DO0Z6IE_2cWU7n7A.png?w=403&h=485)        

在弹出文件框中选择`crack`文件夹下`Licenses`文件夹内任意文件

​                 ![img](https://docimg3.docs.qq.com/image/Kvgwk_Ac9aHmxQSTgGd5IQ.png?w=856&h=529)        

- 至此，Altium软件激活完成

#### PIO Home插件一直Loading

- 一般重启电脑即可，再次打开即可正常访问重启电脑后需要翻墙，再次打开。

- ps：翻墙查看群文件，有详细介绍

#### Gerber文件生成error

扩展版在生成gerber文件时报错或者error，参考[第九个视频](https://www.bilibili.com/video/BV11h41147iJ?p=9)的最后三四分钟，up有提及，需要重新铺铜一遍。

#### .lvgl.h无法打开源文件 lvgl.h找不到或者无法启动，

可以更改下版本号(错误提示的是找不到xdemo.h文件也可以按照这个方法改)也有可能是因为有中文路径，记得改回来，最好也不要有空格。

#### SDL2

SDL2出问题，一般是你的msys2没下，或者下了没有配置好环境变量，回去再看看[B站第六个视频](即https://www.bilibili.com/video/BV11h41147iJ?p=6)可。

#### fusion-360下载路径

https://www.autodesk.com.cn/products/fusion-360/personal



### 硬件

> 引用自：一灯大师

​		目前一共有两个PCB版本，透明底座版本 Naive 和钢铁侠版本 Ironman ，群友大多数使用的是透明底座 Naive 版本。很多大佬写了 Holocubic固件，各版本固件基本通用这两个硬件版本（至少AIO固件是全兼容的）。要做哪个版本大家自行选择。holocubic原作者稚晖君的部分电路是存在问题的，群里有改良过的版本。重要！硬件只支持 2.4G 的wifi， 5G 以及双频合一的都不支持。任何固件都无法突破硬件的限制。

#### 屏幕问题

屏幕的规格：1.3寸，驱动为ST7789 分辨率为240*240，焊接式12Pin。 

这样的屏幕网上有很多，但对于制作holocubic来说需要选择好，屏幕将会影响整体的显示效果。目前 测试了很多家屏幕，效果最好的就是中景园的。不推荐其他家的屏幕用来制作Holocubic。 

下面来实际比对下（请忽略背景差异）： 左边：中景园(翰彩) 右边：优智景

![image-20211223183458681](https://gitee.com/ming-xiangyu/Imageshack/raw/master/img/image-20211223183458681.png)

两个屏幕贴上棱镜后的差距主要在于 `优智景` 显示的周围空白区域严重偏蓝，很影响最终的效果。`中景园` 的空白区域显示就很纯净。注：实际上中景园底色也偏蓝，但很轻微。

关于亮度问题（与屏幕无关）：

 上图也可以看到，左边的明显亮度高，这是由于左边没有撕掉出厂偏光片带的保护膜（能增大亮度）。 做过实现的同学还会发现，在撕掉保护膜后，棱镜 X轴 放置和 Y轴 放置亮度是不一样的。为了增加 holocubic 最终的显示亮度，建议不要撕掉保护膜，买屏幕的时候可以要求卖家发货的时候带保护膜 且不要给保护膜贴那个绿色的标签。

####  关于焊接

1. 由于主板的封装以及紧凑的排布，导致焊接过程中很容易出现部分引脚没焊上或虚焊。请认真。

2. 钢铁侠版本硬件，大多数屏幕板是金属外壳，注意短路问题。

3. 在测试主板的时候，推荐使用 AIO固件 的全套工具进行测试，如果使用 AIO上位机 刷机时卡住不 动，请检查主板（不要怀疑工具或者固件）。

4. 刷机成功也不意味着硬件完全没问题。如果iic线路焊接有问题，就会导致陀螺仪用不了切换不了页 面。如果内存卡不兼容就会导致读取不了内存卡或者 holocubic 重启。具体问题定位，请直接查 看 AIO上位机 的打印信息来判断哪部分存在问题。

5. 焊接时可以选择先把贴片电容电阻焊接上，最后再焊接芯片，避免芯片长时间在焊台上烤导致损坏

6. 焊接拓展版屏幕时，需要用烙铁，先在左上角焊一点，用于固定好屏幕排线和拓展版，然后开始进行拖焊

   #### 焊接教程 
   [透明小电视Holocubic】零基础焊接教学Holo cubic](https://www.bilibili.com/video/BV1Pf4y1P7LU?spm_id_from=333.999.0.0 )

> esp32是最容易虚焊的，焊接的时候可以按压一次，手抖的朋友可以尝试拿个小重物（比如螺钉）压在上边。如果ESP32焊接正确，两个8050上方是3.3ｖ左右。

   [保姆级教程：从0到1带你做稚晖君HoloCubic小电视](https://www.bilibili.com/video/BV11h41147iJ?p=11 )

#### 内存卡的选择

 固件对内存卡有一定的兼容性问题，并不是所有都兼容。推荐以下已验证的内存卡。 

1. 爱国者32G普通卡。淘宝大概20块钱。（性价比高，推荐） 
2.  闪迪32G普通卡。淘宝大概30。

#### 元器件购买

1. 大多数元件在淘宝的优信电子都可以购买到。
2.  主板上的AN5120天线不好搜到，推荐直接买优信电子的AN2051 2.4G天线。
3.  屏幕与主板连接使用FPC座，购买FPC座规格间距0.5 8P 下接和FPC排线规格间距0.5 8P同向 6cm。
5. 稚晖君原版的 naive 和钢铁侠Ironman 内存卡卡槽型号都是 DM3D-SF （淘宝少，比较贵）。是否有内存卡改进版的主板，可以看下群文件或问问群友。卡槽为翻盖[淘宝链接](https://detail.tmall.com/item.htm?spm=a1z0d.6639537.1997196601.4.127374843MgCuF&id=622308751712)。

#### Naive透明底座版本硬件

1. 主板上原设计的C7电容为 0.1uf 是设计缺陷，原理图里提示的"第一次短接"的结论是错的，无法本根解决自动下载。推荐大家把C7换成 1uf~10uf 即可解决问题，任何时候都不需要短接操作。

2. 如下图，原拓展板GND使用的是覆铜连接，而中间连个铜只有箭头指向的位置是相连的，加工 PCB的时候很容易导致没能连上，于是GND断路，现象是屏幕背光都不亮。这里推荐使用群文件

#### 棱镜贴合视频

棱镜与屏幕、屏幕拓展板与底座胶合，都推荐使用B7000胶或者UV胶。注：UV胶需要额外购买固化灯。[棱镜贴合视频教程](https://www.bilibili.com/video/BV1jh411a7pV?p=5)

#### 相关视频

[连3岁小孩子都能看懂的万用表使用方法](https://www.bilibili.com/video/BV1Gx411z7x2?p=1)

[一分钟教你贴片阻容类器件焊接](https://www.bilibili.com/video/BV1iJ411i7y6)

### 注意事项

1. 元器件不要直接放到加热台上，否则很快元器件就被烫坏
2. 最好再pcb板上上好锡浆，元器件摆好后再开加热台，尽量减少多次或长时间加热pcb版，否则板子会给烤坏

## 群号为：738321278

祝大家都能顺利用上 Holocubic 。