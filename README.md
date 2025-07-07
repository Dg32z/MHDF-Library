<p align="center">
   <img src="./Logo.png" width="200px" height="200px" alt="MHDF-Library">
</p>

<div align="center">

# MHDF-Library

_✨一款便携性的依赖管理库✨_

_✨轻量 便携 快捷 好用✨_

</div>

<p align="center">
    <a href="https://github.com/ChengZhiMeow/MHDF-Library/issues">
        <img src="https://img.shields.io/github/issues/ChengZhiMeow/MHDF-Library?style=flat-square" alt="issues">
    </a>
    <a href="https://github.com/ChengZhiMeow/MHDF-Library/blob/main/LICENSE">
        <img src="https://img.shields.io/github/license/ChengZhiMeow/MHDF-Library?style=flat-square" alt="license">
    </a>
    <a href="https://qm.qq.com/cgi-bin/qm/qr?k=T047YB6lHNMMcMuVlK_hGBcT5HNESxMA&jump_from=webapi&authKey=0/IFGIO6xLjjHB2YKF7laLxkKWbtWbDhb1lt//m7GgbElJSWdRZ8RjbWzSsufkO6">
        <img src="https://img.shields.io/badge/QQ群-129139830-brightgreen?style=flat-square" alt="qq-group">
    </a>
</p>

<div align="center">
    <a href="https://github.com/ChengZhiMeow/MHDF-Library/pulse">
        <img src="https://repobeats.axiom.co/api/embed/e58f3e1358766291db33ba451d3e90be99811f4f.svg" alt="pulse">
    </a>
</div>

## maven配置

```xml

<repositories>
    <repository>
        <id>chengzhimeow-maven-repo-releases</id>
        <name>橙汁喵の仓库</name>
        <url>https://maven.chengzhimeow.cn/releases</url>
    </repository>
</repositories>

<dependencies>
<dependency>
    <groupId>cn.chengzhiya</groupId>
    <artifactId>MHDF-Library</artifactId>
    <version>1.0.1</version>
    <scope>compile</scope>
</dependency>
</dependencies>
```

## 使用方法

1. 将本库导入至您的项目
2. 创建一个MHDFLibrary实例就像这样:
   ```java
    MHDFLibrary mhdfLibrary = new MHDFLibrary(
        {程序主类}.class,
        {依赖重定位的前缀(例如"cn.chengzhiya.mhdftools.libs")},
        {依赖文件夹}
    );
   ```
3. 然后添加需要的依赖，就像这样
   ```java
    mhdfLibrary.addDependencyConfig(new DependencyConfig(
        "cn.chengzhiya",
        "MHDF-Scheduler",
        "1.0.2",
        new RepositoryConfig("https://maven.chengzhimeow.cn/releases/"),
        new RelocateConfig(false)
    ));
   ```
4. 然后就可以像这样下载和加载所有依赖了
   ```java
   mhdfLibrary.downloadDependencies();
   mhdfLibrary.loadDependencies();
   ```

### 重定向配置

```java
new RelocateConfig({是否开启重定向},{重定向groupId(可选)},{需要重定向的包});
```

## 贡献者

<a href="https://github.com/ChengZhiMeow/MHDF-Library/graphs/contributors">
  <img src="https://stg.contrib.rocks/image?repo=ChengZhiMeow/MHDF-Library" alt="contributors"/>
</a>

## 精神支柱

- [Xiao-MoMi](https://github.com/Xiao-MoMi)

## Star

[![Stargazers over time](https://starchart.cc/ChengZhiMeow/MHDF-Library.svg?variant=adaptive)](https://starchart.cc/ChengZhiMeow/MHDF-Library)

## 友链

<div>
    <a href="https://plugin.mhdf.cn/">插件文档</a>
    <a href="https://www.mhdf.cn/">梦回东方</a>
    <a href="https://www.yuque.com/xiaoyutang-ayhvn/rnr4ym/">鱼酱の萌新开服教程</a>
</div>
