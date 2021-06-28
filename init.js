db = new Mongo().getDB("skenario")

const isCapped = (_getEnv('CAPPED') === 'true')

db.createCollection('building', { capped: isCapped, size: 999999 })
db.building.insert({
    "name": "building",
    "street": "Mayakovskaya",
    "number": "10",
    "postalCode": "197372",
    "city": "Saint-Petersburg",
    "country": "Russia",
    "description": "Cool old building"
});
db.building.insert({
    "name": "building1",
    "street": "Mayakovskaya",
    "number": "10",
    "postalCode": "197372",
    "city": "Saint-Petersburg",
    "country": "Russia",
    "description": "Cool old building"
});
db.building.insert({
    "name": "building2",
    "street": "Mayakovskaya",
    "number": "10",
    "postalCode": "197372",
    "city": "Saint-Petersburg",
    "country": "Russia",
    "description": "Cool old building"
});
db.building.insert({
    "name": "building3",
    "street": "Mayakovskaya",
    "number": "10",
    "postalCode": "197372",
    "city": "Saint-Petersburg",
    "country": "Russia",
    "description": "Cool old building"
});
db.building.insert({
    "name": "building4",
    "street": "Mayakovskaya",
    "number": "10",
    "postalCode": "197372",
    "city": "Saint-Petersburg",
    "country": "Russia",
    "description": "Cool old building"
});
db.building.insert({
    "name": "building5",
    "street": "Mayakovskaya",
    "number": "10",
    "postalCode": "197372",
    "city": "Saint-Petersburg",
    "country": "Russia",
    "description": "Cool old building"
});
db.building.insert({
    "name": "building6",
    "street": "Mayakovskaya",
    "number": "10",
    "postalCode": "197372",
    "city": "Saint-Petersburg",
    "country": "Russia",
    "description": "Cool old building"
});
db.building.insert({
    "name": "building7",
    "street": "Mayakovskaya",
    "number": "10",
    "postalCode": "197372",
    "city": "Saint-Petersburg",
    "country": "Russia",
    "description": "Cool old building"
});
db.building.insert({
    "name": "building8",
    "street": "Mayakovskaya",
    "number": "10",
    "postalCode": "197372",
    "city": "Saint-Petersburg",
    "country": "Russia",
    "description": "Cool old building"
});
db.building.insert({
    "name": "building9",
    "street": "Mayakovskaya",
    "number": "10",
    "postalCode": "197372",
    "city": "Saint-Petersburg",
    "country": "Russia",
    "description": "Cool old building"
});
db.building.insert({
    "name": "building10",
    "street": "Mayakovskaya",
    "number": "10",
    "postalCode": "197372",
    "city": "Saint-Petersburg",
    "country": "Russia",
    "description": "Cool old building"
});
