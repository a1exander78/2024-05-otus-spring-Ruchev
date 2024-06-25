package ru.otus.hw.service;

import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

public interface TestService {

    void executeTest();

    TestResult executeTestFor(Student student);
}
