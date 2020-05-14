# Client API
Client facing API. Exposes number of HTTP REST methods that could be used by some client UI/application.  
It accepts analysis resuts (such as aggregations) from Spark apps and makes them available for clients.  


Methods:  
`GET     /api/source/list` - returns list of sources that have some aggregations available  
`GET     /api/source/agg-:source` - get aggregation results by source  
`GET     /api/source/all` - get aggregations for all locations  
`GET     /api/source/totalevents` - get total number of events processed  
`POST    /api/source/agg-:source/:valid/:count` - accept new aggregation, add/update in memory to make it available for API clients  
`DELETE  /api/source/agg-:source` - remove aggregation for a given source  
`DELETE  /api/source/all` - remove all aggregations
