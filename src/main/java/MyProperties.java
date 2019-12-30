import lombok.Data;

import java.util.Map;

@Data
public class MyProperties {
    private String author;

    private Map<String, Object> db;

    private String packageNamePrefix;
}
