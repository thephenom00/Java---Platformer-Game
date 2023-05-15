package main;


import utilz.LoggerManager;

public class Main {
    public static void main(String[] args){
        LoggerManager loggerManager = new LoggerManager(true);
        new Game(loggerManager);
    }
}