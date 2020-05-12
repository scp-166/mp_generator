package generator.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class RawPropertiesInfo {
    private String author;
    private Boolean lombok;
    private Map<String, Object> db;
    private String packageNamePrefix;
    private String modelName;
}