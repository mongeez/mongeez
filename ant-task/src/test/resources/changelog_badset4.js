//mongeez formatted javascript
//changeset nlloyd:makeDupData
var cur = db.dates.find().snapshot();
cur.forEach(function(aDate){
	aDate.notUnique = 1;
	db.dates.save(aDate);
});

//changeset nlloyd:breakingIndex
db.dates.ensureIndex({'notUnique': 1}, {unique:true});
