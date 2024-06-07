package ru.otus.hw.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;


public class StreamsIOServiceTest {
    private OutputStream output = new ByteArrayOutputStream();
    private IOService sioservice = new StreamsIOService(new PrintStream(output));

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(output));
    }

    @Test
    void shouldPrintHelloWorld() {
        sioservice.printLine("Hello world");
        assertEquals("Hello world\r\n", output.toString());
    }

    @AfterEach
    void cleanUp() {
        System.setOut(null);
    }
}
