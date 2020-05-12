# Signal API
This is a small service. It's only purpose so far is to accept incoming signals and output them for Spark jobs to process.  
It accepts all incoming signals, accumulates them in memory for a while, then dumps to a text file.

## Configuration
The application configuration can be done using environment variables or its configuration file (/conf/application.conf). Entries:
- `signal.fileDestinationFolder` - writes data (signals) to text files in this folder. Also removes old files from this folder.
- `signal.schedule.dump.duration.sec` - write data to a new text file using this duration interval.
- `signal.schedule.clearFolder.duration.sec` - remove old data from the folder using this duration interval. Meaning that files older than this number of seconds should be removed.


## API Methods/endpoints
There is one API method:

```
POST    /api/signal                 controllers.SignalController.newSignalEvent
```

Accepts a signal Json like this:

```
{
	"source": "CPST_GL9951FT",
	"attribute": "Engine Speed",
	"uom": "rpm",
	"value": "1006"
}
```

It the accumulates those signals and dump them into text files like this:

```
/home/myname/foldername/2020-05-12_13-57-55.txt
/home/myname/foldername/2020-05-12_13-58-00.txt
/home/myname/foldername/2020-05-12_13-58-05.txt
/home/myname/foldername/2020-05-12_13-58-10.txt
```

