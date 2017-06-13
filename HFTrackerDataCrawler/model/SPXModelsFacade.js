var SPXDPSModel = require("./SPXDPSModel"),
	SPXEPSModel = require("./SPXEPSModel"),
	SPXShillerPEModel = require("./SPXShillerPEModel"),
	SPXPEModel = require("./SPXPEModel"),
	SPXInfoModel = require("./SPXInfoModel");

module.exports = {
	addDPS : function(model,success,failure){
		//console.log(fundInfo);
		new SPXDPSModel(model).add(success,failure);
	},
	
	addEPS : function(model,success,failure){
		//console.log(fundInfo);
		new SPXEPSModel(model).add(success,failure);
	},
	
	addPE : function(model,success,failure){
		//console.log(model);
		new SPXPEModel(model).add(success,failure);
	},
	
	addShillerPE : function(model,success,failure){
		new SPXShillerPEModel(model).add(success,failure);
	},
	
	updateInfo : function(model,success,failure){
		new SPXInfoModel(model).update(success,failure);
	}

};