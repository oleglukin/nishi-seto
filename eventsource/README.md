# Eventsource
This is a console app. Use it for testing only.  
Create a number of random events and send them to APIs HTTP endpoint. See Signal API [Readme](/signal-api/README.md) file.

## Arguments
Run it using sbt like this:  
`sbt run 86 8 100 http://127.0.0.1:9000/api/signal`  
- `events` - number of signal events to generate. Defaul is 16
- `maxEventSourceIds` - maximum number of signal sources. Default is 10
- `maxIntervalMs` - maximum interval (ms) between signal posting. Default is 0
- `url` - signal api endpoint. Default is `http://localhost:9000/api/signal`

