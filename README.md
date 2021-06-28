# What is it?
This is my first attempt to work within reactive stack. I used SpringWebFlux + MongoDb.

## How to run it
First install docker.

###Run:

####Pull images
docker pull chocolazerboom/assessment:app

docker pull chocolazerboom/assessment:mongo

Oh shit, I always mix up assignment and assessment :(

####Create network
docker network create -d bridge backend

####Run containers
####(CAPPED=true means that streaming is allowed, but doc/collection sizes cannot be changed)
docker run -dp 27017:27017 --env CAPPED=false --name mongo --net backend mongo
docker run -dp 8000:8080 --name app --net backend --link mongo:mongo app

###Endpoints

GET /building/all - get all data

GET /building/filter - YOU CAN USE MONGO QUERIES AS URL PARAMETER!!! Just replace { } to () !!! 

For example /building/filter?query=(name:"name"), /building/filter?query=(name:/namePart/) etc.

POST /building - add new building (will be added only if its geo exists)

PATCH /building/{id} - change a building, just provide new name or description here like following:

{
"name": "building500"
}

Don't use it in capped mode, or be sure that your changes don't affect document's size

GET /building/all/stream - this guy is for streaming, do not forget to have capped collection to use it

###Tests
There is only one integration test that I added just as an example, because I was already exhausted.
I missed security because of the same reason (I was supposed to add JWT here) :(
