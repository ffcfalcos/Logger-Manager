package com.github.ffcfalcos.logger;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FormatterHandlerProviderTest {

    @Test
    void get() {
        try {
            FormatterHandlerProvider formatterHandlerProvider = new FormatterHandlerProvider();
            FormatterHandler formatterHandler = formatterHandlerProvider.get(JsonFormatterHandler.class);
            assertEquals(formatterHandler.getClass(), JsonFormatterHandler.class);
            assertDoesNotThrow(() -> formatterHandlerProvider.get(null));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void setDefault() {
        try {
            FormatterHandlerProvider formatterHandlerProvider = new FormatterHandlerProvider();
            formatterHandlerProvider.setDefault(StringFormatterHandler.class);
            assertEquals(formatterHandlerProvider.get(void.class).getClass(), StringFormatterHandler.class);
            assertDoesNotThrow(() -> formatterHandlerProvider.setDefault(null));
            assertEquals(formatterHandlerProvider.get(void.class).getClass(), StringFormatterHandler.class);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void add() {
        try {
            class TestFormatterHandler implements FormatterHandler {
                @Override
                public String format(LogContent logContent) {
                    return null;
                }

                @Override
                public String format(Object logContent, Severity severity) {
                    return null;
                }
            }
            FormatterHandlerProvider formatterHandlerProvider = new FormatterHandlerProvider();
            formatterHandlerProvider.add(new TestFormatterHandler());
            assertEquals(formatterHandlerProvider.get(TestFormatterHandler.class).getClass(), TestFormatterHandler.class);
            assertDoesNotThrow(() -> formatterHandlerProvider.add(null));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
}