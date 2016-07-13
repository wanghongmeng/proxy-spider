##简介
抓取免费代理,包含HTTP/HTTPS,提供REST接口供调用

##实现原理

* 技术框架
    * spring boot/maven/http-client/httpcleaner/json/mail
* 抓取实现
    * 找到免费代理网站,通过http抓取网页信息,通过htmlcleaner解析xpath,直接定位到ip元素,通过定时任务不断轮询ip列表,检验代理有效性

##问题反馈
在使用中有任何问题，欢迎反馈给我，可以用以下联系方式跟我交流

* Email: wanghongmeng@fero.com.cn

##项目构建

```javascript
  mvn clean compile package -U -B
```
如果构建有问题,提示jar包找不到,请换成其他maven仓库,有些jar包开源中国仓库没有
