package ru.vokazak;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import lombok.experimental.Accessors;
import org.junit.Test;

import java.math.BigDecimal;;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class JacksonTest {

    ObjectMapper subj = new ObjectMapper();

    @Test
    public void testJackson_write() throws Exception {

        Transaction transaction = new Transaction()
                .setDate(new Date())
                .setIn(new Account(123L, "Tinkoff"))
                .setSum(BigDecimal.valueOf(35.05))
                .setCategories(Collections.singletonList(
                        new Category(1L, "ЗП")
                        )
                );

        System.out.println(subj.writeValueAsString(transaction));
    }

    @Test
    public void testJackson_read() throws Exception {
        Transaction transaction = subj.readValue("{\n" +
                "  " +
                "\"date\":1616410673122,\n" +
                "  \"in\":{\n" +
                "    \"id\":123,\"name\":\"Tinkoff\"\n" +
                "  },\n" +
                "  \"out\":null,\n" +
                "  \"sum\":35.05,\n" +
                "  \"categories\":[\n" +
                "    {\n" +
                "      \"id\":1,\"name\":\"ЗП\"\n" +
                "    }\n" +
                "  ]\n" +
                "}", Transaction.class);

        System.out.println(transaction);
    }

    //@Getter + @Setter + @ToString + ... = @Data
    //val.set(...) returns val
    @Data
    @Accessors(chain = true)
    static class Transaction {
        private Date date;
        private Account in;
        private Account out;
        private BigDecimal sum;
        private List<Category> categories;
    }

    @Data
    @Accessors(chain = true)
    @AllArgsConstructor
    static class Category {
        private Long id;
        private String name;
    }

    @Data
    @Accessors(chain = true)
    @AllArgsConstructor
    static class Account {
        private Long id;
        private String name;
    }
}
