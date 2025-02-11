package source.code;

// 源码主要通过XMLConfigBuilder获取和解析MyBatis-config.xml配置文件
// TODO: MyBatis提供两种Transaction类型
//       源码层面分别使用JdbcTransaction或ManagedTransaction
// 1. <transactionManager type="JDBC">    直接使用JDBC提交和回滚功能的事务, 它依赖于从数据源检索到的连接来管理事务的范围
// 2. <transactionManager type="MANAGED"> 让容器管理事务的整个生命周期, 延迟连接检索直到调用getConnection(), 忽略所有提交或回滚请求
// TODO: MyBatis提供两种dataSource方式
//       源码层面分别使用PooledDataSource(UnpooledDataSource)或JndiDataSourceFactory
// 1. <dataSource type="POOLED">  使用数据库连接池访问
// 2. <dataSource type="JNDI">    使用JDNI接口提供的服务
public class LoaderXmlDataSource {

    // 使用SqlSessionFactoryBuilder构建SqlSessionFactory时, 创建XMLConfigBuilder
    // XMLConfigBuilder parser = new XMLConfigBuilder(inputStream, environment, properties);
    // return build(parser.parse());

    // 以下是对于环境参数的解析
    // <environments default="development">
    //     <environment id="development">
    //         <transactionManager type="JDBC"/>
    //         <dataSource type="POOLED">
    //             <property name="driver" value="${driverClassName}"/>
    //             <property name="url" value="${url}"/>
    //             <property name="username" value="${username}"/>
    //             <property name="password" value="${password}"/>
    //         </dataSource>
    //     </environment>
    // </environments>

    // 对于xml配置<configuration>中的所有全局参数，源码中都有对应的解析方法
    // private void parseConfiguration(XNode root) {
    //     try {
    //         this.propertiesElement(root.evalNode("properties"));
    //         Properties xml_settings = this.settingsAsProperties(root.evalNode("xml_settings"));
    //         this.loadCustomVfs(xml_settings);
    //         this.loadCustomLogImpl(xml_settings);
    //         this.typeAliasesElement(root.evalNode("typeAliases"));
    //         this.pluginElement(root.evalNode("plugins"));
    //         this.objectFactoryElement(root.evalNode("objectFactory"));
    //         this.objectWrapperFactoryElement(root.evalNode("objectWrapperFactory"));
    //         this.reflectorFactoryElement(root.evalNode("reflectorFactory"));
    //         this.settingsElement(xml_settings);
    //         this.environmentsElement(root.evalNode("environments"));
    //         this.databaseIdProviderElement(root.evalNode("databaseIdProvider"));
    //         this.typeHandlerElement(root.evalNode("typeHandlers"));
    //         this.mapperElement(root.evalNode("mappers"));
    //     } catch (Exception var3) {
    //         throw new BuilderException("Error parsing SQL Mapper Configuration. Cause: " + var3, var3);
    //     }
    // }

    // private void environmentsElement(XNode context) throws Exception {
    //     if (context != null) {
    //         if (this.environment == null) {
    //             this.environment = context.getStringAttribute("default");
    //         }
    //         Iterator var2 = context.getChildren().iterator();
    //         while(var2.hasNext()) {
    //             XNode child = (XNode)var2.next();
    //             String id = child.getStringAttribute("id");
    //             // 对于每一个带有id的environment配置，进行解析
    //             if (this.isSpecifiedEnvironment(id)) {
    //                 TransactionFactory txFactory = this.transactionManagerElement(child.evalNode("transactionManager"));
    //                 DataSourceFactory dsFactory = this.dataSourceElement(child.evalNode("dataSource"));
    //                 // 通过返回的DataSourceFactory，生成dsFactory.getDataSource();
    //                 DataSource dataSource = dsFactory.getDataSource();
    //                 Builder environmentBuilder = (new Builder(id)).transactionFactory(txFactory).dataSource(dataSource);
    //                 // 最终设置数据库源到Configuration
    //                 this.configuration.setEnvironment(environmentBuilder.build());
    //             }
    //         }
    //     }
    // }
    //
    //  // 根据"dataSource"生成DataSourceFactory
    //  private DataSourceFactory dataSourceElement(XNode context) throws Exception {
    //      if (context != null) {
    //          String type = context.getStringAttribute("type");
    //          Properties props = context.getChildrenAsProperties();
    //          DataSourceFactory factory = (DataSourceFactory)this.resolveClass(type).getDeclaredConstructor().newInstance();
    //          factory.setProperties(props);
    //          return factory;
    //      } else {
    //          throw new BuilderException("Environment declaration requires a DataSourceFactory.");
    //      }
    //  }
    //
    //  // Class XNode该类型用于获取一个property配置的"name"和"value"
    //  public Properties getChildrenAsProperties() {
    //      Properties properties = new Properties();
    //      Iterator var2 = this.getChildren().iterator();
    //      while(var2.hasNext()) {
    //          XNode child = (XNode)var2.next();
    //          String name = child.getStringAttribute("name");
    //          String value = child.getStringAttribute("value");
    //          if (name != null && value != null) {
    //              properties.setProperty(name, value);
    //          }
    //      }
    //      return properties;
    //  }
}
