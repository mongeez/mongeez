//mongeez formatted javascript
//changeset nlloyd:InsertSomeMoreStuffNewCollection
for(var i = 0; i < 50; i++) {
	db.dates.insert({count: i, date: new Date()});
}
