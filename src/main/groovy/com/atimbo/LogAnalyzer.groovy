package com.atimbo

import com.atimbo.util.CSVFileHandler
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.Period
import org.joda.time.PeriodType
import org.joda.time.Seconds
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter


class LogAnalyzer {

    private static final String CSV_EXT  = '.csv'
    private static final String TIMESTAMP_COLUMN = '_messagetime'
    private static final String MESSAGE_COLUMN = '_raw'
    private static final int MAX_SECONDS = 1

    private final String defaultDir = '/tmp'

    private final CSVFileHandler fileHandler = new CSVFileHandler()

    DateTimeFormatter frmt = DateTimeFormat.forPattern("MM/dd/yyyy' 'HH:mm:ss.SSS' 'Z")

    String buildOutputFilePath(String inputFile, String outputPath = null) {
        if (outputPath) {
            return outputPath
        }
        def outFileName = inputFile.name[0 .. inputFile.name.lastIndexOf('.') - 1]
        outputPath = "${defaultDir}/${outFileName}_${frmt.print(new DateTime())}${CSV_EXT}"    
    }

    def analyzeLogFile(String logFile) {

        // Read in CSV file
        List rows = readCsvData(logFile).sort{ it[TIMESTAMP_COLUMN] }

        DateTime startTime = frmt.parseDateTime(rows[0][TIMESTAMP_COLUMN])
        DateTime lastTimeStamp
        String lastMessage
        Long gapMillis = 0
        rows.eachWithIndex { row, idx ->
            String msg = row[MESSAGE_COLUMN]
            DateTime timestamp = frmt.parseDateTime(row[TIMESTAMP_COLUMN])
            if (lastTimeStamp) {
                Seconds diff = Seconds.secondsBetween(lastTimeStamp, timestamp)
                if (diff.getSeconds() > MAX_SECONDS) {
                    Period period = new Period(lastTimeStamp, timestamp, PeriodType.millis())
                    gapMillis += period.getMillis()

                    println """\

                        -------------------------------------------

                        Big Gap!!! ${lastTimeStamp} - ${timestamp}: 

                        ${lastMessage}""".stripIndent()
                }
            }
            lastTimeStamp = timestamp
            lastMessage = msg
        }
        Long totalMillis = new Period(startTime, lastTimeStamp, PeriodType.millis()).getMillis()
        println """\

            ================================================

            Total Milliseconds: ${totalMillis}

            Gap Milliseconds: ${gapMillis}


            Percent of Total: ${gapMillis / totalMillis * 100}%""".stripIndent()


        // Find error messages

        // Calculate time between start and first error

        // Calculate time between start and end

        // print results

        // write results

        //println "\nWriting to ${outputPath}"

    }

    private List readCsvData(String csvFile) {
        return fileHandler.readRows(csvFile)
    }
}