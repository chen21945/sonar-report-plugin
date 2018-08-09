SonarQube Report Plugin 
==========
提供对sonarqube扫描结果的PDF导出功能。
* 2018-7 支持sonarqube v6.7，v6.3及以下版本不支持
* 插件以web page形式呈现，在每个项目的菜单栏增加PDF Report页，可选择期望导出的Issue类型和严重程度，导出为PDF文件。
* 使用itext7作为PDF组件

使用须知
* 使用时需要修改/constant/WSConfig中的HOST_URL，此处将端口改为80
* resources/static/img中包含作者公司logo，需要自行修改
* 默认使用思源黑体

版本更新
* v1.2 2018-8-9
    * 调整页面格式，使用自定义css
    * 增加过滤条件 sinceLeakPeriod，支持导出新增bug、缺陷
* v1.1 2018-7-31
    * 页面增加过滤条件，支持根据问题类型（bug,缺陷，坏味道）和严重程度过滤数据导出
    * 字体调整为SourceHanSans
* v1.0 2018-7 
    * 实现sonarqube pdf导出功能，增加页面插件



参考文档：
* [SonarQube Extension Guide](https://docs.sonarqube.org/display/DEV/Extension+Guide)
* [iText7 Developer Examples](https://developers.itextpdf.com/itext-7-examples)
* https://github.com/SonarQubeCommunity/sonar-pdf-report

