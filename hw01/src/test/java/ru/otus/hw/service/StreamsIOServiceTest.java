package ru.otus.hw.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

public class StreamsIOServiceTest {

    private PrintStream printStream;
    private ArgumentCaptor<String> captor;
    private IOService streamsIOService;

    @BeforeEach
    void setUp() {
        printStream = Mockito.mock(PrintStream.class);
        streamsIOService = new StreamsIOService(printStream);
        captor = ArgumentCaptor.forClass(String.class);
    }

    @Test
    void shouldPrintHelloWorld() {
        String text = "Hello world";
        streamsIOService.printLine(text);
        Mockito.verify(printStream).println(captor.capture());
        assertEquals(captor.getValue(), text);
    }

    @AfterEach
    void cleanUp() {
        System.setOut(null);
    }
}
