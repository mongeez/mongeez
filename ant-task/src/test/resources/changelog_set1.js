//mongeez formatted javascript
//changeset nlloyd:InsertSomeStuff
db.test.insert({stuff: 'stuff', num: 1});
db.test.insert({stuff: 'stuff', num: 2});
db.test.insert({stuff: 'stuff', num: 3});
//changeset otherguy:CreateAnIndex
db.test.ensureIndex({'num': 1});
