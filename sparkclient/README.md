# sparkclient
Spark driver program that contains data processing logic.

# Components
Currently there is only one component **SourceAnalysisApp** - analyse incoming signal and their sources.  
It has 2 entry points:
- `SourceAnalysisApp` - use this to submit the application to Spark cluster
- `SourceAnalysisLocalApp` - use this for local testing

Both of them monitor given folder for new files and process their data as a stream.

## 1.1 SourceAnalysisApp - data processing
There is an aggregation logic in `SourceAggregation`.  It defines number of valid and failed signals for each source.  
Signal is considered valid if its value is a number. Non-numeric values (e.g. open/close) are not processed yet.  
Each new aggregation is then posted to the Client API.

## 1.2 Testing SourceAnalysisLocalApp
Use sbt to run it like this:  
`sbt run /tmp/input http://localhost:9000/api/source/agg-`  
- The first argument is the data input folder.  
- The second argument is the Client API endpoint to post source aggregations to. At the end it constructs a URL like this: `http://localhost:9000/api/source/agg-CPST_GL9951FT/true/48813`. See Client API methods documentation ([readme](/client-api/README.md)).  
Once compiled select this main class to run: `nishiseto.SourceAnalysisLocalApp`.

# Possible Failures
1. Input folder is not accessible or doesn't exist. Should throw and exception like this:  
`org.apache.spark.sql.AnalysisException: Path does not exist: /some/invalid/path`  
Make sure that the input folder exists.
2. Cannot post aggregation to the given Client API endpoint. Should throw and exception like this:  
`java.net.ConnectException: Connection refused`  
Make sure that Client API is running and accessible. Also check client API endpoint provided as an argument to the Spark job.
