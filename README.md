## 目前自用

## 使用
- 进入 `classpath` 中的 `properties.yml` 设置下属性
- 进入 `CodeGenerator.java` 执行 `main` 函数即可

### 注意事项
- 省事直接用了 springboot 的 web 依赖的日志...
- 取消了 `Service` 层的 `I` 前缀
- 更改 `mapper.java` 文件为 `xxxDOMapper.java`
- 更改 `entity.java` 文件名为 `xxxDO.java`
- **很多属性可以看配置的字段注释跟着改**