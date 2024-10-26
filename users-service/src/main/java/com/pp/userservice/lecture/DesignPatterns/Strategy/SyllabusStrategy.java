package com.pp.userservice.lecture.DesignPatterns.Strategy;

import java.util.List;
import java.util.Map;

public interface SyllabusStrategy {
    List<Map<String, List<String>>> show();
}
