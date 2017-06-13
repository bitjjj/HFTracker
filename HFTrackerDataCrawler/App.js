var FundListCrawler = require("./FundListCrawler"),
	FundDetailsCrawler = require("./FundDetailsCrawler");
	
	
	var listCrawler = new FundListCrawler(),detailsCrawler = new FundDetailsCrawler();
	
	listCrawler.on("complete",function(){
		console.log(detailsCrawler);
		detailsCrawler.run();
	});
	
	listCrawler.run();
