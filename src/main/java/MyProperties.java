import lombok.*;
import org.yaml.snakeyaml.Yaml;

import java.util.Map;

@Getter
@Setter
@ToString
public class MyProperties {
    private String author;
    private Map<String, Object> db;
    private String packageNamePrefix;
    private String modelName;

    // db 中的其他元素
    private String userName;
    private String password;
    private String dbUrl;
    private String dbName;
    private String[] tableNames;
    private String tablePrefix;


    /**
     * 读取 yml 文件中的数据
     *
     * @return
     */
    public static MyProperties readYml() {
        Yaml yaml = new Yaml();
        MyProperties myProperties = yaml.loadAs(CodeGenerator.class.getResourceAsStream("/properties.yml"), MyProperties.class);
        String temptableNames = (String) myProperties.getDb().get("tableNames");
        temptableNames = temptableNames.replace(" ", "");
        myProperties.setUserName((String) myProperties.getDb().get("userName"));
        myProperties.setPassword((String) myProperties.getDb().get("password"));
        myProperties.setDbUrl((String) myProperties.getDb().get("dbUrl"));
        myProperties.setDbName((String) myProperties.getDb().get("dbName"));
        temptableNames = temptableNames.replace(" ", "");
        myProperties.setTableNames(temptableNames.split(","));
        myProperties.setTablePrefix((String) myProperties.getDb().get("tablePrefix"));
        return myProperties;
    }

}
