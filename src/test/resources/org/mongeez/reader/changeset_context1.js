//mongeez formatted javascript

//changeset shin:ChangeSet-1
db.user.insert({ "Name" : "Alice"});

//changeset shin:ChangeSet-2 runAlways:false
db.user.insert({ "Name" : "Bob"});

//changeset shin:ChangeSet-3 runAlways:true context:test
db.user.insert({ "Name" : "Charlie"});

//changeset shin:ChangeSet-4 runAlways:true context:test,staging
db.user.insert({ "Name" : "Edward"});



