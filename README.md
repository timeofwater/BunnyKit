# ���

С��������С�ù��ߣ�2021��

**java 11+**

��ʼ����Ҫһ��������ȫ����ʱ��ʱ��Ĺ��� �������Խ��Խ�๦����

# ����

* ����ʱ��
* ���
* ��ʱ��
* ���ػ�����
* ���κ�λ��������Լ��������
* ��ȡ��������
* �����õĶ���˹����

�������ڲ��ϸ��¡�

# ����

### Ŀ¼�ṹ��

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

### �����ļ�para.xml��

```
<?xml version="1.0" encoding="UTF-8"?>
<para>
    <!--Ĭ��ʱ�Ӵ��ں�������Ϳ�ߣ��������Ļ���Ͻǣ������أ�-->
    <x>900</x>
    <y>200</y>
    <width>220</width>
    <height>30</height>
    <!--ʱ��ģ���������Ϳ�ߣ�����ڴ������Ͻǣ������أ�-->
    <timeX>0</timeX>
    <timeY>0</timeY>
    <timeWidth>195</timeWidth>
    <timeHeight>30</timeHeight>
    <!--����ģ���������Ϳ�ߣ�����ڴ������Ͻǣ������أ�-->
    <weatherX>195</weatherX>
    <weatherY>0</weatherY>
    <weatherWidth>25</weatherWidth>
    <weatherHeight>30</weatherHeight>
    <!--�����ֺ�-->
    <font>Monospaced</font>
    <fontSize>20</fontSize>
    <!--ʱ����ʾ��ʽ-->
    <timeFormatter>MM,dd,���� HH:mm:ss</timeFormatter>
    <!--������ʾͼ��-->
    <weatherIcon>?</weatherIcon>
    <!--��괥��ǰ��͸���ȣ�0-1��-->
    <normalOpacity>0.7</normalOpacity>
    <lightOpacity>0.2</lightOpacity>
    <!--��ѯ��ʱʱ�䣨1-10�����룩-->
    <timeout>2</timeout>
    <!--�˵���ʾ����-->
    <info>v1.5</info>
    <!--&��ER%&@_*-->
    <ids>
        <id name="����֮����">5176</id>
    </ids>
    <explorer>C:\Program Files (x86)\Google\Chrome\Application\chrome.exe</explorer>
    <!--������䣨�������ں�С�ںţ�-->
    <pws>
        <pw name="default">1234567</pw>
    </pws>
</para>
```

���У�ʱ����ʾ��ʽ�Ὣ`����`
�����������ڣ��������ű�ʾ��[?����](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/time/LocalDateTime.html#format(java.time.format.DateTimeFormatter))

# ʹ��

### ������

���´򿪷�ʽѡ����һ���ɣ�

1. ˫��BunnyKit.jar
2. �Ҽ�BunnyKit.jar -> �򿪷�ʽ -> ����Ӧ�ó��� -> Java(TM) Platform SE binary
3. �Ϸ���ַ������`cmd`���س� -> ����`java -jar BunnyKit.jar`���س�

### �˳���

�Ҽ�������ͼ��![icon.png](src/main/resources/images/icon2.png) -> exit

### ���棺

* ʱ��ģ�� �������ļ�������ʾ���� 10fps ��������
* ����ģ�� �������ļ�������ʾ���� ���������ʾ���� ��Ҫ����

### �Ҽ��˵���

* info �˴���ʾ�汾��Ϣ
* para �������ļ�
* ��� �����
* ���� ������

### �������˵���

* picPiecer ���ػ�������
* bunny bot ������������
* flashList ˢ�������б�
* flashClock ˢ��ʱ�Ӵ��ڣ�ʱ���������
* visibleClock ��ʾʱ��
* hideClock ����ʱ��
* some news ��ѯ����
* Tetris Game! ���ö���˹������Ϸ
* exit �˳�

# Q&A

##### 1.�����޷���ʾ��

> �ȵ�����ٽ����ͣ��������ģ�鼸�뼴��

##### 2.ʱ����Ų�֪��������Ӧ��

> ���÷��ţ�
>
>   * `����` ��������
>   * `yyyy` ��
>   * `MM` ��
>   * `dd` ��
>   * `HH` ʱ
>   * `mm` ��
>   * `ss` ��

##### 3.ʲôʱ������¹��ܣ�

> �пյ�ʱ��

##### 4.Ϊʲôʵ�ʹ��ܺ��ĵ���Щ���룿

> ���ø����ĵ�����ʵ�ʹ���Ϊ׼

##### 5.�����Ŀ�ǿ�Դ����

> ����Ҳûɶ�������������Ļ��ܿ����������������ҪԴ�����˽��
>
> mail: 694300642@qq.com