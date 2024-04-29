package ru.otus.hw.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import ru.otus.hw.domain.Student;
import ru.otus.hw.security.LoginContext;
import ru.otus.hw.service.ResultService;
import ru.otus.hw.service.StudentService;
import ru.otus.hw.service.TestService;

@ShellComponent(value = "Commands for test application")
@RequiredArgsConstructor
public class ApplicationCommands {
    private final LoginContext<Student> loginContext;

    private final StudentService studentService;

    private final ResultService resultService;

    private final TestService testService;


    @ShellMethod(key = {"s", "start"}, value = "Start the test")
    @ShellMethodAvailability("isTestingAvailable")
    public void start() {
        var student = loginContext.getLoggedIn();
        var result = testService.executeTestFor(student);
        resultService.showResult(result);
    }

    @ShellMethod(key = {"l", "login"}, value = "Login student")
    public void login() {
        var student = studentService.determineCurrentStudent();
        loginContext.login(student);
    }

    public Availability isTestingAvailable() {
        return loginContext.isLoggedIn() ? Availability.available() : Availability.unavailable("Need to login");
    }

}
