SELECT fund_name,COUNT(*) FROM fund_news GROUP BY fund_name


DELETE FROM fund_holdings WHERE fund_name='Ken Griffin$Citadel Investment Group'

INSERT INTO fund_holdings(`security`,ticker,shares,`value`,activity,`action`,`port`,dateStr,fund_name,securityType,addBy) 
VALUES('Adecoagro S A','AGRO','25,915,076','$244,638','',0,'1.84%','2014-06-30','George Soros$Soros Fund Management','STOCK','nodejs') Adecoagro S A 2014-06-30 George Soros$Soros Fund Management STOCK

SELECT fund_name,dateStr,COUNT(*) AS c FROM fund_holdings  GROUP BY  fund_name,dateStr ORDER BY fund_name,dateStr DESC

SELECT COUNT(*) FROM  fund_holdings WHERE fund_name='Ken Fisher$Fisher Asset Management'

SELECT * FROM fund_news WHERE fund_name='Bill Ackman$Pershing Square' ORDER BY timeMark DESC


UPDATE fund_holdings SET `port` = REPLACE(`port`, "%", "");

UPDATE fund_holdings SET `activity` = REPLACE(`activity`, "%", "");

UPDATE fund_holdings SET `value` = REPLACE(`value`, "$", "");
UPDATE fund_holdings SET `value` = REPLACE(`value`, ",", "");

UPDATE fund_holdings SET `shares` = REPLACE(`shares`, ",", "");

SELECT fund_name,`security`,securityType,ticker,dateStr,SUM(shares) AS shares,SUM(`value`) AS `value`,SUM(`activity`) AS `activity`,SUM(`port`) AS `port` FROM fund_holdings 
WHERE ticker='AAPL' AND dateStr='2014-06-30' AND securityType='STOCK'
GROUP BY fund_name,`security`,securityType,ticker,dateStr 
ORDER BY fund_name,`port` DESC


SELECT * FROM fund_holdings WHERE ticker='AAPL' AND fund_name='George Soros$Soros Fund Management' AND securityType='STOCK'
ORDER BY dateStr DESC

SELECT DISTINCT(`name`) FROM fund_industry_percentage

SELECT DISTINCT(`securityType`) FROM fund_holdings

SELECT fund_name,`security`,securityType,ticker,dateStr,SUM(shares) AS shares,SUM(`value`) AS `value`,SUM(`activity`) AS `activity`,SUM(`port`) AS `port` FROM fund_holdings 
WHERE dateStr='2014-06-30' AND `action`!= -2 AND (fund_name='Israel Englander$Millennium Management' OR fund_name='Ray Dalio$Bridgewater Associates')
GROUP BY fund_name,`security`,securityType,ticker,dateStr 
ORDER BY fund_name,`port` DESC


SELECT `security`,securityType,ticker FROM fund_holdings WHERE dateStr='2014-06-30' AND fund_name='Israel Englander$Millennium Management' AND ticker='BRK-B'
UNION
SELECT `security`,securityType,ticker FROM fund_holdings WHERE dateStr='2014-06-30' AND fund_name='Ray Dalio$Bridgewater Associates' AND ticker='BRK-B'


SELECT COUNT(*) FROM fund_holdings WHERE dateStr='2014-06-30' AND fund_name='Israel Englander$Millennium Management'


SELECT `security`,securityType,ticker,SUM(shares) AS shares, GROUP_CONCAT(DISTINCT fund_name SEPARATOR ' & ') AS fund_names,GROUP_CONCAT(DISTINCT fund_name SEPARATOR ' & ') AS fund_names_count
FROM fund_holdings 
WHERE dateStr='2014-06-30' AND `action`!= -2 AND (fund_name='George Soros$Soros Fund Management' OR fund_name='Warren Buffett$Berkshire Hathaway' OR fund_name='David Einhorn$Greenlight Capital')
GROUP BY `security`,securityType,ticker
HAVING LENGTH(fund_names_count) - LENGTH(REPLACE(fund_names_count, '&', '')) >= 2


SELECT `security`,securityType,ticker,SUM(shares) AS shares,GROUP_CONCAT(DISTINCT fund_name SEPARATOR ' | ') AS fund_names,GROUP_CONCAT(DISTINCT fund_name SEPARATOR ' | ') AS fund_names_count
FROM fund_holdings 
WHERE dateStr='2014-06-30' AND `action`!= -2 AND (fund_name='George Soros$Soros Fund Management' OR fund_name='Ken Fisher$Fisher Asset Management')
GROUP BY `security`,securityType,ticker
HAVING LENGTH(fund_names_count) - LENGTH(REPLACE(fund_names_count, '|', '')) >= 1
ORDER BY ticker,`security`
LIMIT 10,10

SELECT `security`,securityType,ticker,COUNT(*) AS `count`,SUM(shares) AS shares,GROUP_CONCAT(DISTINCT fund_name SEPARATOR ' & ') AS fund_names,GROUP_CONCAT(DISTINCT fund_name SEPARATOR ' & ') AS fund_names_count
FROM fund_holdings 
WHERE dateStr='2014-06-30' AND `action`!= -2
GROUP BY `security`,securityType,ticker
HAVING LENGTH(fund_names_count) - LENGTH(REPLACE(fund_names_count, '&', '')) >= 1 AND `count` >=2
ORDER BY `count` DESC
LIMIT 10,10
