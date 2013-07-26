//mongeez formatted javascript
//changeset nlloyd:DropDate
var cur = db.dates.find({'date': {$exists: true}});
cur.forEach(function(aDate){
	delete aDate.date;
	db.dates.save(aDate);
});