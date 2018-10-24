# AES
aes 加密解密

### 中文秘钥JDK

使用中文秘钥时候汉字的key字节大小与什么机器相关么???

```
C:\Program Files\Java\jdk1.7.0_17\bin>java -version
java version "1.7.0_17"
Java(TM) SE Runtime Environment (build 1.7.0_17-b02)
Java HotSpot(TM) 64-Bit Server VM (build 23.7-b01, mixed mode)

String key = "中@文#密*钥s";
```


```
D:\jdk\jdk1.7.0_51\bin>java -version
java version "1.7.0_51"
Java(TM) SE Runtime Environment (build 1.7.0_51-b13)
Java HotSpot(TM) 64-Bit Server VM (build 24.51-b03, mixed mode)
String key = "中文啊XX";
```

```
D:\jdk\jdk1.8.0\bin>java -version
java version "1.8.0_77"
Java(TM) SE Runtime Environment (build 1.8.0_77-b03)
Java HotSpot(TM) 64-Bit Server VM (build 25.77-b03, mixed mode)
String key = "中文啊XX";
```

dependcy
------------------
bcprov-jdk15on-1.60.jar
commons-codec-1.11.jar
