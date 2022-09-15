# 简介

小兔制作：小兔工具（2021）

**java 11+**

开始是想要一个可以在全屏的时候看时间的工具 结果现在越来越多功能了

# 功能

* 桌面时钟
* 秒表
* 定时器
* 像素画生成
* 在任何位置填充难以记忆的密码
* 获取最新新闻
* 玩内置的俄罗斯方块

功能正在不断更新。

# 下载

### 目录结构：

> BunnyKit.jar
>
> para.xml
>
> lib
>
> > gson-2.9.1.jar
> >
> > jlayer-1.0.1.4.jar
> >
> > junit-3.8.2.jar

### 配置文件para.xml：

```
<?xml version="1.0" encoding="UTF-8"?>
<para>
    <!--默认时钟窗口横纵坐标和宽高（相对于屏幕左上角）（像素）-->
    <x>900</x>
    <y>200</y>
    <width>220</width>
    <height>30</height>
    <!--时间模块横纵坐标和宽高（相对于窗口左上角）（像素）-->
    <timeX>0</timeX>
    <timeY>0</timeY>
    <timeWidth>195</timeWidth>
    <timeHeight>30</timeHeight>
    <!--天气模块横纵坐标和宽高（相对于窗口左上角）（像素）-->
    <weatherX>195</weatherX>
    <weatherY>0</weatherY>
    <weatherWidth>25</weatherWidth>
    <weatherHeight>30</weatherHeight>
    <!--字体字号-->
    <font>Monospaced</font>
    <fontSize>20</fontSize>
    <!--时间显示格式-->
    <timeFormatter>MM,dd,星期 HH:mm:ss</timeFormatter>
    <!--天气显示图标-->
    <weatherIcon>?</weatherIcon>
    <!--鼠标触碰前后透明度（0-1）-->
    <normalOpacity>0.7</normalOpacity>
    <lightOpacity>0.2</lightOpacity>
    <!--查询超时时间（1-10）（秒）-->
    <timeout>2</timeout>
    <!--菜单显示内容-->
    <info>v1.5</info>
    <!--&￥ER%&@_*-->
    <ids>
        <id name="旭日之记忆">5176</id>
    </ids>
    <explorer>C:\Program Files (x86)\Google\Chrome\Application\chrome.exe</explorer>
    <!--密码填充（不含大于号小于号）-->
    <pws>
        <pw name="default">1234567</pw>
    </pws>
</para>
```

其中，时间显示格式会将`星期`
换成中文星期，其他符号表示见[?链接](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/time/LocalDateTime.html#format(java.time.format.DateTimeFormatter))

# 使用

### 启动：

以下打开方式选择其一即可：

1. 双击BunnyKit.jar
2. 右键BunnyKit.jar -> 打开方式 -> 其他应用程序 -> Java(TM) Platform SE binary
3. 上方地址栏输入`cmd`并回车 -> 输入`java -jar BunnyKit.jar`并回车

### 退出：

右键任务栏图标![icon.png](src/main/resources/images/icon2.png) -> exit

### 界面：

* 时间模块 由配置文件决定显示内容 10fps 整点提醒
* 天气模块 由配置文件决定显示内容 鼠标悬浮显示天气 需要网络

### 右键菜单：

* info 此处显示版本信息
* para 打开配置文件
* 秒表 打开秒表
* 闹钟 打开闹钟

### 任务栏菜单：

* picPiecer 像素画生成器
* bunny bot 密码填充机器人
* flashList 刷新密码列表
* flashClock 刷新时钟窗口（时间和天气）
* visibleClock 显示时钟
* hideClock 隐藏时钟
* some news 查询新闻
* Tetris Game! 内置俄罗斯方块游戏
* exit 退出

# Q&A

##### 1.天气无法显示？

> 先点击，再将鼠标停留在天气模块几秒即可

##### 2.时间符号不知道怎样对应？

> 常用符号：
>
>   * `星期` 中文星期
>   * `yyyy` 年
>   * `MM` 月
>   * `dd` 日
>   * `HH` 时
>   * `mm` 分
>   * `ss` 秒

##### 3.什么时候更新新功能？

> 有空的时候

##### 4.为什么实际功能和文档有些出入？

> 懒得更新文档，以实际功能为准

##### 5.这个项目是开源的吗？

> 里面也没啥东西，有心做的话很快就能做出来，有需要源码的请私信
>
> mail: 694300642@qq.com