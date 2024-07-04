package ru.otus.hw.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.InputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

@DisplayName("Class StreamsIOService")
@ExtendWith({MockitoExtension.class})
public class StreamsIOServiceTest {

    @Mock
    private PrintStream printStream;

    @Mock
    private InputStream inputStream;

    @InjectMocks
    private StreamsIOService streamsIOService;

    @Captor
    private ArgumentCaptor<String> captor;

    @DisplayName("should print \"Hello world\"")
    @Test
    void shouldPrintHelloWorld() {
        String text = "Hello world";
        streamsIOService.printLine(text);
        verify(printStream).println(captor.capture());
        assertEquals(captor.getValue(), text);
    }
}
