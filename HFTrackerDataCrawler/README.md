配置：
在config/config.json里，可以配置数据库链接
里面有一个Cookie的配置，需要自己注册账号登陆insidermonkey.com，然后用浏览器查看一下对应的Cookie值。
这个网站不登录查看不了对冲基金全部的持仓。

运行：
首先导入数据库hftracker_empty.sql或者直接导入db_bak_2015.2.22.sql（推荐）

然后运行 node FundListCrawler，从insidermonkey.com获取要抓取对冲基金列表

接着运行 node FundDetailsCrawler，抓取对冲基金持仓概要

在运行 node FundHoldingsCrawler，抓取对冲基金详细的持仓情况

还可以运行 node FundNewsENCrawler，抓取对冲基金的相关新闻

还可以运行 node SPXDataCrawler，抓取标普500相关的指标，包括席勒PE,PE，EPS等指标

===================================

由于抓取的这个对冲基金网站列表会变化，里面的对冲基金配置会过时，可以自己手动更新
config/fundlistExtra.json和数据库fund_info表

