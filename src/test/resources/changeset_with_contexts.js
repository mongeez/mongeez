//mongeez formatted javascript
// changeset mlysaght:ChangeSet-1 context:organizations
db.organization.insert({
    "Organization" : "10Gen",
    "Location" : "NYC",
    DateFounded : {"Year" : 2008, "Month" : 01, "day" :01}
});
db.organization.insert({
    "Organization" : "SecondMarket",
    "Location" : "NYC",
    DateFounded : {"Year" : 2004, "Month" : 05, "day" :04}
});

// changeset mlysaght:ChangeSet-2 context:users
db.user.insert({ "Name" : "Michael Lysaght"});
db.user.insert({ "Name" : "Oleksii Iepishkin"});

// changeset exell:ChangeSet-3
db.car.insert({ "Type" : "Porsche"});
db.car.insert({ "Type" : "Lamborghini"});

// changeset exell:ChangeSet-4 context:users,organizations
db.house.insert({ "Type" : "Bungalow"});
db.house.insert({ "Type" : "Split-Level"});
