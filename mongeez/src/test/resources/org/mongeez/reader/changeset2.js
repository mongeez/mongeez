//mongeez formatted javascript
//changeset someuser:cs3 runAlways:true
db.organization.update({Location : "NYC"}, {$set : {Location : "NY"}}, false, true);
//changeset someotheruser:cs4 runAlways:false
db.organization.find().forEach(function(org) {
    var year = org.DateFounded.Year;
    var month = org.DateFounded.Month;
    var day = org.DateFounded.day;
    //Year is minimum required information
    if (year != null) {
    var date = new Date();
    if (month != null) {
    if (day != null) {
    date.setUTCDate(day);
    }
date.setMonth(month - 1);
}
date.setFullYear(year);
}
if (date != null) {
    db.organization.update({Organization : org.Organization}, {$set : {DateFounded : date}});
}
else {
    db.organization.update({Organization : org.Organization}, {$unset : {DateFounded : 1 }});
}
});
