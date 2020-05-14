package generator.model;

import generator.CodeGenerator;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.BeanUtils;
import org.yaml.snakeyaml.Yaml;

import java.util.Objects;

@Getter
@Setter
@ToString
public class MyProperties {
    private String author;
    private Boolean lombok;
    private String packageNamePrefix;
    private String modelName;

    private DB dbInfo;

    @Getter
    @Setter
    @ToString
    public static class DB {
        // db 中的其他元素
        private String userName;
        private String password;
        private String dbUrl;
        private String dbName;
        private String[] tableNames;
        private String tablePrefix;
    }

    public void loadInfo(RawPropertiesInfo rawPropertiesInfo) {
        if (Objects.isNull(dbInfo)) {
            dbInfo = new DB();

        }
        // 拷贝相同字段
        BeanUtils.copyProperties(rawPropertiesInfo, this);

        dbInfo.setUserName(
                (String) rawPropertiesInfo.getDb().get("userName"));
        dbInfo.setPassword((String) rawPropertiesInfo.getDb().get("password"));

        dbInfo.setDbUrl(
                (String) rawPropertiesInfo.getDb().get("dbUrl"));
        dbInfo.setDbName(
                (String) rawPropertiesInfo.getDb().get("dbName"));
        dbInfo.setTableNames(((String) rawPropertiesInfo.getDb().get("tableNames"))
                // 去除空格
                .replace(" ", "")
                .split(","));
        dbInfo.setTablePrefix(
                (String) rawPropertiesInfo.getDb().get("tablePrefix"));
    }

    /**
     * 读取 yml 文件中的数据
     *
     * @return
     */
    public static MyProperties readYml() {
        Yaml yaml = new Yaml();
        RawPropertiesInfo rawPropertiesInfo = yaml.loadAs(CodeGenerator.class.getResourceAsStream("/tableInfo.yml"),
                RawPropertiesInfo.class);
        MyProperties myProperties = new MyProperties();
        myProperties.loadInfo(rawPropertiesInfo);
        return myProperties;

    }

    public static void main(String[] args) {
        System.out.println(MyProperties.readYml());
    }

}


