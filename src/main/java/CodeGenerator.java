import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableFill;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import org.yaml.snakeyaml.Yaml;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// 演示例子，执行 main 方法控制台输入模块表名回车自动生成对应项目目录中
public class CodeGenerator {

    private static MyProperties myProperties;

    private static String author;

    private static String userName;
    private static String password;
    private static String dbUrl;
    private static String tableName;
    private static String tablePrefix;

    private static String packageNamePrefix;

    static {
        myProperties = readYml();
        System.out.println(myProperties);
        author = myProperties.getAuthor();

        userName = (String) myProperties.getDb().get("userName");
        password = (String) myProperties.getDb().get("password");
        dbUrl = (String) myProperties.getDb().get("dbUrl");
        tableName = (String) myProperties.getDb().get("tableName");
        tablePrefix = (String) myProperties.getDb().get("tablePrefix");

        packageNamePrefix = myProperties.getPackageNamePrefix();
    }

    public static void main(String[] args) {
        Template();
    }

    /**
     * <p>
     * 读取控制台内容
     * </p>
     */
    public static String scanner(String tip) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入" + tip + "：");
        if (scanner.hasNext()) {
            String ipt = scanner.next();
            if (StringUtils.isNotEmpty(ipt)) {
                return ipt;
            }
        }
        throw new MybatisPlusException("请输入正确的" + tip + "！");
    }

    /**
     * 读取 yml 文件中的数据
     *
     * @return
     */
    public static MyProperties readYml() {
        Yaml yaml = new Yaml();
        return yaml.loadAs(CodeGenerator.class.getResourceAsStream("/properties.yml"), MyProperties.class);
    }


    public static void Template() {
        AutoGenerator autoGenerator = new AutoGenerator();
        // 1. 全局配置
        GlobalConfig config = new GlobalConfig();

        String projectPath = System.getProperty("user.dir");
        config
                .setActiveRecord(false)                         // 是否支持 AR 模式
                .setAuthor(author)
                .setOutputDir(projectPath + "/src/main/java")   // 文件生成位置
                .setFileOverride(false)                         // 是否文件覆盖
                .setIdType(IdType.AUTO)                         // 主键生成策略
                // .setSwagger2(true)                           // entity 是否生成 swagger 注解
                .setOpen(false);                                //生成完成后是否打开文件目录

        config
                .setEntityName("%sDO")                          // 设置 entity 文件名
                .setMapperName("%sDOMapper")                    // 设置 mapper.java 文件名
                .setServiceName("%sService")                    // 设置 service 的首字母不为I
                .setXmlName(null);                              // 设置 mapper.xml 文件名,后面取消掉了
        config
                .setEnableCache(false)                          // 是否在 xml 中配置二级缓存
                .setBaseResultMap(true)                         // mapper.xml 设置基本的 resultMapper
                .setBaseColumnList(true);                       // mapper.xml设置基本 Column


        // 2. 数据源配置
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setDbType(DbType.MYSQL);
        dataSourceConfig.setUrl("jdbc:mysql://" + dbUrl + "/" + tableName + "?useUnicode=true&useSSL=false&characterEncoding=utf8&serverTimezone=UTC");
        dataSourceConfig.setDriverName("com.mysql.cj.jdbc.Driver");
        dataSourceConfig.setUsername(userName);
        dataSourceConfig.setPassword(password);
        // 3. 包配置
        PackageConfig packageConfig = new PackageConfig();
        packageConfig.setModuleName(scanner("模块名"));
        packageConfig.setParent(packageNamePrefix)                  // 设置包名, 以及各个分层的包名
                .setMapper("dao")
                .setService("service")
                .setController("controller")
                .setEntity("pojo.dataobject");
        // .setXml("mapper");
        // 4. 策略配置
        // 配置自动填充字段
        List<TableFill> tableFillList = new ArrayList<>(2);
        tableFillList.add(new TableFill("create_at", FieldFill.INSERT));
        tableFillList.add(new TableFill("update_at", FieldFill.INSERT_UPDATE));

        StrategyConfig strategy = new StrategyConfig();
        strategy.setCapitalMode(true)                               // 全局大写
                .setRestControllerStyle(true)                       // 配置@RestController
                .setNaming(NamingStrategy.underline_to_camel)       // 数据库表映射到实体的命名策略
                .setTablePrefix(tablePrefix)                               // 设置表前缀，否则生成的文件会带表前缀
                .setSkipView(true)                                  // 跳过视图
                .setEntityTableFieldAnnotationEnable(true)          // 生成注释
                .setEntityLombokModel(true)                         // 启用 lombok
                .setVersionFieldName("version")                     // 设置乐观锁字段
                .setLogicDeleteFieldName("deleted")                 // 设置逻辑删除字段
                .setTableFillList(tableFillList)                    // 自动填充字段
                .setInclude(scanner("表名，多个英文逗号分割").split(","));

        // 5. 自定义注入配置
        InjectionConfig injectionConfig = new InjectionConfig() {
            @Override
            public void initMap() {
                /*
                    自定义返回配置 Map 对象
                    该对象可以传递到模板引擎通过 cfg.xxx 引用
                 */
            }
        };

        // 如果模板引擎是 freemarker
        String templatePath = "/templates/mapper.xml.ftl";
        // 如果模板引擎是 velocity
        // String templatePath = "/templates/mapper.xml.vm";

        // 自定义输出文件
        List<FileOutConfig> fileOutConfigList = new ArrayList<>();
        fileOutConfigList.add(
                new FileOutConfig(templatePath) {
                    @Override
                    public String outputFile(TableInfo tableInfo) {
                        // 自定义输出文件名，如果 Entity 设置了前后缀，此处 xml 的名称会跟着发生变化
                        return projectPath + "/src/main/resources/mapper/" + packageConfig.getModuleName()
                                + "/" + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
                    }
                }
        );
        injectionConfig.setFileOutConfigList(fileOutConfigList);

        // 6. 配置模板
        TemplateConfig templateConfig = new TemplateConfig();

        // 配置自定义输出模板
        //指定自定义模板路径，注意不要带上.ftl/.vm, 会根据使用的模板引擎自动识别
        // templateConfig.setEntity("templates/entity2.java");
        // templateConfig.setService();
        // templateConfig.setController();

        // 不生成默认的 mapper 文件
        templateConfig.setXml(null);


        autoGenerator.setGlobalConfig(config)
                .setCfg(injectionConfig)
                .setTemplate(templateConfig)
                .setDataSource(dataSourceConfig)
                .setStrategy(strategy)
                .setPackageInfo(packageConfig)
                // 默认模板是 Velocity
                // .setTemplateEngine(new VelocityTemplateEngine())
                .setTemplateEngine(new FreemarkerTemplateEngine());
        // 执行
        autoGenerator.execute();
    }
}