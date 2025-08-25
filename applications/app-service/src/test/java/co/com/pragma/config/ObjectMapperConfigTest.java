package co.com.pragma.config;

import org.junit.jupiter.api.Test;
import org.reactivecommons.utils.ObjectMapper;
import org.reactivecommons.utils.ObjectMapperImp;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import static org.junit.jupiter.api.Assertions.*;

class ObjectMapperConfigTest {


    @Test
    void testObjectMapperBean() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ObjectMapperConfig.class);
        ObjectMapper objectMapper = context.getBean(ObjectMapper.class);
        assertNotNull(objectMapper);
        assertInstanceOf(ObjectMapperImp.class, objectMapper);
        context.close();
    }

    @Test
    void testOnlyOneObjectMapperBean() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ObjectMapperConfig.class);
        String[] beanNames = context.getBeanNamesForType(ObjectMapper.class);
        assertEquals(1, beanNames.length);
        context.close();
    }

    @Test
    void testContextClosesWithoutError() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ObjectMapperConfig.class);
        assertDoesNotThrow(context::close);
    }
}