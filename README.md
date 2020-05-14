# nishi-seto
Signal processing API and jobs  
Similar (but simplified version) is written in Java: [shimanami-kaido](https://github.com/oleglukin/shimanami-kaido)


## Business Case
Suppose that there are certain signals or events that come from some source system. The task is to do some aggregation on the fly and keep current results available for users to view.  
Signal schema:

```
{
  "source": "CPST_GL9951FT", 	// signal source identifier
  "attribute": "Engine Speed", 	// attribute name, e.g. temperature, pressure
  "uom": "rpm", 		// unit of measure
  "value": "927"		// valid value (e.g. a number) or an invalid one like 'read failure'
}
```
The basic requirement is to find current number of both functional and failed signals per `source`.

## Components
There are 4 components (applications) in this repository:
1. [sparkclient](/sparkclient)
2. [siganl-api](/signal-api)
3. [client-api](/client-api)
4. [eventsource](/eventsource)


![Components](./components.png)


### 1. Sparkclient
See sparckclient [Readme file](/sparkclient/README.md).  
This application contains Spark driver program and events stream processing logic (aggregation).

It ingests data from a local folder (configurable within the properties file). The folder is monitored for new files and the new files are ingested as a stream.

This data should have a schema matching signal event to be parsed (see data model above).

It defines if value is a valid number (otherwise failed) and then groupes by `source` to find total number (count) of functional and failed events for each source.  
Once grouped the data is being sent to an HTTP endpoint (see API methods below).

### 2. Signal API
See Signal API [Readme file](/signal-api/README.md).  
Process incoming signals and output them for Spark jobs to process.  
There is one method so far:

```
POST    /api/signal                 controllers.SignalController.newSignalEvent
```

There is a class called `SignalHandler`. It's purpose is to accumulate new signals from controller and dump them to a text file once in a while.

### 3. Client API
See Client API [Readme file](/client-api/README.md).  
Client facing API. It accepts analysis resuts (such as aggregations) from Spark apps and makes them available for clients.

### 4. EventSource
See EventSource [Readme file](/eventsource/README.md).  
This is only for testing. Create a number of random events and send them to APIs HTTP endpoint.


## Things to Improve / Consider Changing
- Ingest evens through TCP socket connection or use Kafka. Currently it reads events from  files in a folder
- Probably would be better to output aggregation results to Kafka or other queue as well
- Test and make sure that sparkclient can work with Spark cluster (standalone or YARN/Mesos)

