package ru.appsmart.demo.dkozakov;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = Application.AppConfiguration.class)
@ActiveProfiles("test")
public class BaseTests {
}
