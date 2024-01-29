1、数据库设计 文件地址/trade/doc下

2、项目模块说明

---------  trade-admin 后台管理api :上下架商品 新增sku等等商家操作

---------  trade-commons 公共包 各个包引即可；

---------  trade-member 用户管理 登录注册账户生成 充值 体现之类操作

---------  trade-order 用户购买操作：购买支付扣库存生成订单

---------  trade-job 风控系统 独立部署即可；



nginx 

      ----->  /order 转发到下单模块      

      ----->  /member 转发到用户模块

      ----->  /admin 转发到商户后台模块











server {

    listen       80;

    server_name  localhost;

    #access_log  /var/log/nginx/host.access.log  main;



    location / {

        root   /usr/share/nginx/html;

        index  index.html index.htm;

    }



    server_name locahost;

    location /member {

        client_max_body_size    5m;

        proxy_pass http://localhost:6001;

        proxy_set_header Host $http_host;

        proxy_set_header X-Real-IP $remote_addr;

        proxy_set_header X-Scheme $scheme;

        proxy_set_header Upgrade $http_upgrade;

        proxy_set_header Connection "upgrade";

    }

    location /order {

        client_max_body_size    5m;

        proxy_pass http://localhost:7001;

        proxy_set_header Host $host;

        proxy_set_header X-Real-IP $remote_addr;

    }



    location /admin {

        client_max_body_size    5m;

        proxy_pass http://localhost:6002;

        proxy_set_header Host $host;

        proxy_set_header X-Real-IP $remote_addr;

    }

    error_page   500 502 503 504  /50x.html;

    location = /50x.html {

        root   /usr/share/nginx/html;

    }

}







功能点：

1、账户注册 生成多货币账户；后续新加入货币，使用定时任务生成或者脚本；

2、购买幂等：通过数据库悲观 select for update 机制

3、spring --transcation 保证账户转化 库存 订单生成的事务 

4、账户redis缓存 hash结构 member:1001   2020-02-14 {"usd":100,"cny":100,"jpy":100};

5、一个初始的多模块项目；

------------------------------------------------------

后续加入

库存模块拆分成独立服务； 由下单服务远程调用；

下单服务拆分，拆分成下单 ---> 支付服务 : 产品层面也要相应的调整；原因：下单事务周期大，大并发大流量造成数据库死锁；

下单服务：使用分布式事务seata

下单：分布式事务

{

资金暂扣：balance--->frozenBalance;

库村暂扣  stock-->stock_lock; 调用库存服务；30分钟未支付 回滚；

订单生成  status 未支付；

}

支付： {

用户冻结释放；

资金划到商家；

库存释放；

}

网关加入 支持负载均衡搞可用；

redis cluster模式；

数据库主备 分库分表；

多实例场景下 分布式锁；

--------------------------------------------------------

























