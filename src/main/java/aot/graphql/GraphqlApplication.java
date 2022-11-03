package aot.graphql;


import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportRuntimeHints;
import org.springframework.core.io.ClassPathResource;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;


@ImportRuntimeHints(GraphqlRuntimeHintsRegistrar.class)
@SpringBootApplication
public class GraphqlApplication {

    public static void main(String[] args) {
        SpringApplication.run(GraphqlApplication.class, args);
    }

}

@Controller
class CustomerGraphqlController {

    @QueryMapping
    Collection<Customer> customers() {
        return List.of(new Customer(1, "Andreas"), new Customer(2, "Rossen"));
    }
}

record Customer(Integer id, String name) {
}

class GraphqlRuntimeHintsRegistrar implements RuntimeHintsRegistrar {

    private final MemberCategory[] values = MemberCategory.values();

    @Override
    public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
        List.of(Customer.class, CustomerGraphqlController.class).forEach(c -> hints.reflection().registerType(c, this.values));
        Set.of(new ClassPathResource("graphiql/index.html"), new ClassPathResource("graphql/schema.graphqls"))
                .forEach(s -> hints.resources().registerResource(s));
    }
}
