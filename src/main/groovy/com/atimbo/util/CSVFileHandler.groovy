package com.atimbo.util

import org.apache.commons.csv.CSVParser
import static org.apache.commons.csv.CSVFormat.*

import java.nio.file.Paths

class CSVFileHandler {
	
    List readRows(String csvFile) {
    	List rows = []
		Paths.get(csvFile).withReader { reader ->
		    CSVParser csv = new CSVParser(reader, DEFAULT.withHeader())

		    for (record in csv.iterator()) {
		        rows << (record.toMap() as Expando)
		    }
		}
		return rows
    }

}