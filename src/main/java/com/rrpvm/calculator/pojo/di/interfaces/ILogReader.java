package com.rrpvm.calculator.pojo.di.interfaces;


import javafx.util.Pair;

import java.util.List;

public interface ILogReader {
    List<Pair<String, String>> readClientLogs();

    void setup();

    void release();
}
