package ru.otus.hw.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.service.LocalizedIOServiceImpl;
import ru.otus.hw.service.TestRunnerServiceImpl;

@RequiredArgsConstructor
@ShellComponent
public class ShellCommands {

    private final LocalizedIOServiceImpl ioService;

    private final TestRunnerServiceImpl testRunnerService;

    @ShellMethod(value = "Starting test", key = {"start", "s", "поехали"})
    public String startTest() {
        testRunnerService.run();
        return ioService.getMessage("ShellCommands.end.of.test");
    }
}
