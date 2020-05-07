# nishi-seto
Signal processing API and jobs
Similar (but simplified version) is written in Java: [shimanami-kaido](https://github.com/oleglukin/shimanami-kaido)


## Business Case
Signal schema:

```
{
  "source": "CPST_GL9951FT", 	// signal source identifier
  "attribute": "Engine Speed", 	// attribute name, e.g. temperature, pressure
  "uom": "rpm", 		// unit of measure
  "value": "927",		// valid value (e.g. a number) or an invalid one like 'read failure'
  "timestamp": "2020-04-26T21:47:02.614Z" // date/time
}
```

