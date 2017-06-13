var querystring = require("querystring"),
	http = require("http"),
	url = require("url"),
	util = require('util'),
	zlib = require("zlib");

module.exports = {
	
	get:function(url,callback,errorCallback){	
		var self = this;
		http.get(url, function(res) {
			console.log("Get response: ",res.statusCode,url);
		
			self._gunzipResponse(res,callback);	
		}).on('error', function(e) {
			console.error(e);
			errorCallback && errorCallback();
		})
	},
	
	getWithHeaders:function(urlStr,headers,callback,errorCallback){	
		var self = this,urlInfo = url.parse(urlStr),
			options = {
				"host": urlInfo.hostname,
				"port": urlInfo.port || 80,
				"path": urlInfo.path,
				"method": 'GET',
				"headers": headers
			};
		
		var req = http.request(options, function(res) {
			console.log("Get response: ",res.statusCode);
		
			self._gunzipResponse(res,callback);	
		}).on('error', function(e) {
			console.error(e);
			errorCallback && errorCallback();
		});
		req.end();
	},
	
	post:function(urlStr,content,headers,callback,errorCallback){	
		var self = this,urlInfo = url.parse(urlStr),
			options = {
				"host": urlInfo.hostname,
				"port": urlInfo.port || 80,
				"path": urlInfo.path,
				"method": 'POST',
				"headers": headers
			},
			content=querystring.stringify(content);
		headers["Content-Length"] = content.length;
		var req = http.request(options, function(res) {
			console.log("Get response: ",res.statusCode);
			
			self._gunzipResponse(res,callback);
		}).on('error', function(e) {
			console.error("error: " + e.message);
			errorCallback && errorCallback();
		});
		req.write(content);
		req.end();
	},
	
	_gunzipResponse:function (response,callback,errorCallback){
		
		var chunks = [],self = this;
		response.on('data', function(chunk) {
			chunks.push(chunk);
		});
	 
		response.on('end', function() {
			var buffer = Buffer.concat(chunks);
			var encoding = response.headers['content-encoding'];
			if (encoding == 'gzip') {
				zlib.gunzip(buffer, function(err, decoded) {
					callback(decoded && decoded.toString());
				});
			} else if (encoding == 'deflate') {
				zlib.inflate(buffer, function(err, decoded) {
					callback(decoded && decoded.toString());
				})
			} else {
				callback(buffer.toString());
			}
		});
		
		response.on("error",function(err){
			console.error("gunzip error: ",err);
			errorCallback && errorCallback();
		});
	},
	
	_eoc_:null
}