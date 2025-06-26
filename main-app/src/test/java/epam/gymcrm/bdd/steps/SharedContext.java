package epam.gymcrm.bdd.steps;

import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MvcResult;

import java.util.HashMap;
import java.util.Map;

@Component
public class SharedContext {
    private final Map<String, Object> context = new HashMap<>();

    public void setResult(MvcResult result) {
        context.put("result", result);
    }

    public MvcResult getResult() {
        return (MvcResult) context.get("result");
    }

    public void set(String key, Object value) {
        context.put(key, value);
    }

    public <T> T get(String key, Class<T> clazz) {
        return clazz.cast(context.get(key));
    }

    public boolean contains(String key) {
        return context.containsKey(key);
    }
}
