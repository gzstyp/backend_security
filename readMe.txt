
依赖关系图

--> 表示依赖关系;

api --> swagger --> service --> dao --> bean --> common
web --> swagger --> service --> dao --> bean --> common

****************************************************************

１.生产环境时使用本方案,需要修改子模块api的swagger依赖直接改为service即可
api --> service --> dao --> bean --> common

２.开发环境调试时本方案
api --> swagger --> service --> dao --> bean --> common

３.负载均衡|集群环境
web --> auth --> service --> dao --> bean --> common

注意 application.properties 的文件读取,是以最终打包的模块的文件为主,否则是获取不到数据的问题：

比如本项目就只有把配置文件放在api、web、frontend模块里的值才能获取到,否则会报错获取不到值

注意service层的方法上是否有final关键字,否则也会这个报错,或提示系统出现错误(其实是空指针)

使用token时，注意返回和请求做统一处理，即可

IE浏览器对URL的GET请求最大限制为2083个字符，如果超过这个数字，提交按钮没有任何反应