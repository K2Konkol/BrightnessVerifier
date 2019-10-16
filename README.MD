sbt run 
to run application with default settings - in and out folders are in the main project folder, cutoff is set to 65 - the amount that filters correctly 100% of provided pictures

to set different values from command line, use for example:
sbt run -Dinput=/<your-path-here>/in/ -Doutput=/<your-path-here>/out/ -Dcutoff=40