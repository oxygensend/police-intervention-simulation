package com.iisi.statistics;

import com.iisi.City;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class StatisticsCsvWriter {

    private static final Logger LOGGER = LoggerFactory.getLogger(StatisticsCsvWriter.class);
    private static final String DIRECTORY_PATH = "statistics";
    private static final String DISTRICT_STATISTICS_FILE = DIRECTORY_PATH + "/district_statistics.csv";
    private static final String[] DISTRICT_STATISTICS_HEADERS = new String[]{
            "district",
            "iteration",
            "dangerCoefficient",
            "numberOfPatrols",
            "numberOfInterventions",
            "numberOfSolvedInterventions",
            "numberOfFirings",
            "numberOfSolvedFirings",
            "numberOfNeutralizedPatrols",
            "numberOfPatrolsComingFromOtherDistricts"
    };

    private StatisticsCsvWriter() {
    }

    public static void saveDistrictStatistics() {

        try {
            Files.createDirectories(Paths.get(DIRECTORY_PATH));

            BufferedWriter writer = new BufferedWriter(new FileWriter(DISTRICT_STATISTICS_FILE));

            writer.write(String.join(",", DISTRICT_STATISTICS_HEADERS));
            writer.newLine();

            var stat = City.instance().historicDistrictStatisticsList;
            for (var statistics : City.instance().historicDistrictStatisticsList) {
                writer.write(String.format("%s, %d,%.2f,%d,%d,%d,%d,%d,%d,%d",
                                           statistics.districtName(),
                                           statistics.iteration(),
                                           statistics.dangerCoefficient(),
                                           statistics.numberOfPatrols(),
                                           statistics.numberOfInterventions(),
                                           statistics.numberOfSolvedInterventions(),
                                           statistics.numberOfFirings(),
                                           statistics.numberOfSolvedFirings(),
                                           statistics.numberOfNeutralizedPatrols(),
                                           statistics.numberOfPatrolsComingFromOtherDistricts()));
                writer.newLine();
            }

            writer.close();

            LOGGER.info("Data saved to file: {}", DISTRICT_STATISTICS_FILE);
        } catch (IOException e) {
            LOGGER.info("Error while saving data to file: {}", e.getMessage());
        }
    }

}
