package ru.appsmart.demo.dkozakov.repository;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.appsmart.demo.dkozakov.BaseTests;

import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@Transactional
@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
public abstract class BaseRepositoryTests extends BaseTests {
}
