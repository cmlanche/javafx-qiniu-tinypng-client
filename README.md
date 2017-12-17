# javafx-qiniu-tinypng-client
javafx七牛云图片压缩上传客户端

运行说明:
1. 注册七牛，然后获取你的bucket的accesskey和secretkey放到程序的config/config.properties中。
```
qiniu.accesskey=your access key
qiniu.secretkey=your secret key
```
2. 注册tinypng(https://tinypng.com/)，然后把获取到的token填入到config的tinypng.token中
```tinypng.token=your tiny token```