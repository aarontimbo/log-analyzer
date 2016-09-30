package com.atimbo

parseArgs(args)

def parseArgs(args) {

    if (!args) {
    	showUsage('Oops! No log file specified.')
    	return
    }

    String logFile = args[0]
    if (!logFile.endsWith(/.csv/)) {
        showUsage('Input file must be a CSV.')
        return    	
    }

    File inputFile = new File(logFile)
    if (!inputFile.exists()) {
        showUsage("Unable to find input file from given path: $logFile")
        return
    }

	new LogAnalyzer().analyzeLogFile(logFile)
}

def showUsage(String msg) {
        println """\
            ************************************
            *       Log File Analyzer          *      
            ************************************

            ${msg}

            Usage: ./gradlew runScript -PlogFile=/path/to/logfile.csv

        """.stripIndent()
}

println '\n*** DONE ***'
