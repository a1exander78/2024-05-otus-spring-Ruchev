package ru.otus.hw.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.InputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@DisplayName("Class StreamsIOService")
@SpringBootTest
public class StreamsIOServiceTest {

    @Configuration
    static class StreamsIOServiceTestConfiguration {
        @Bean
        StreamsIOService streamsIOService() {
            return new StreamsIOService(mock(PrintStream.class), mock(InputStream.class));
        }
    }

    @Autowired
    private StreamsIOService streamsIOService;

    @Captor
    private ArgumentCaptor<String> captor;

    @DisplayName("should print \"Hello world\"")
    @Test
    void shouldPrintHelloWorld() {
        String text = "Hello world";
        streamsIOService.printLine(text);
        verify(streamsIOService.getPrintStream(), times(1)).println(captor.capture());
        assertEquals(captor.getValue(), text);
    }
}
