/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package com.moviebooking;

import com.moviebooking.commands.CommandInvoker;
import com.moviebooking.config.ApplicationConfig;
import com.moviebooking.exceptions.NoSuchCommandException;
import com.moviebooking.exceptions.NoSuchDataException;
import com.moviebooking.repositories.data.DataLoader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class App {
    public static void run(List<String> commandLineArgs){
        ApplicationConfig applicationConfig = new ApplicationConfig();
        DataLoader dataLoader = applicationConfig.getDataLoader();
        CommandInvoker commandInvoker = applicationConfig.getCommandInvoker();
        BufferedReader reader;

        String inputFile = commandLineArgs.get(0).split("=")[1];
        commandLineArgs.remove(0);
        for(String arg: commandLineArgs){
            String[] splits = arg.split("=");
            try {
                dataLoader.executeData(splits[0],splits[1]);
            } catch (NoSuchDataException e) {
                e.printStackTrace();
            }
        }
        try {
            reader = new BufferedReader(new FileReader(inputFile));
            String line = reader.readLine();
            while (line != null) {
                List<String> tokens = Arrays.asList(line.split(" "));
                commandInvoker.executeCommand(tokens.get(0),tokens);
                // read next line
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException | NoSuchCommandException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        List<String> commandLineArgs = new LinkedList<>(Arrays.asList(args));
        String expectedSequence = "INPUT-FILE$CINEMA-DATA$SCREEN-DATA$SEAT-DATA$CUSTOMER-DATA$MOVIE-DATA$SHOW-DATA";
        String actualSequence = commandLineArgs.stream()
                .map(a -> a.split("=")[0])
                .collect(Collectors.joining("$"));
        if(expectedSequence.equals(actualSequence)){
            run(commandLineArgs);
        }
    }
}