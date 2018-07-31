SonarQube Report Plugin 
==========
提供对sonarqube扫描结果的PDF导出功能。
* 2018-7 支持sonarqube v6.7，v6.3及以下版本不支持
* 插件以web page形式呈现，在每个项目的菜单栏增加PDF Report页，可选择期望导出的Issue类型和严重程度，导出为PDF文件。
* 使用itext7作为PDF组件
* 使用时需要修改/constant/WSConfig中的HOST_URL，此处将端口改为80



参考文档：
* [SonarQube Extension Guide](https://docs.sonarqube.org/display/DEV/Extension+Guide)
* [iText7 Developer Examples](https://developers.itextpdf.com/itext-7-examples)
* https://github.com/SonarQubeCommunity/sonar-pdf-report

