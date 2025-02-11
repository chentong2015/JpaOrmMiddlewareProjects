package metadata;

import org.hibernate.MappingException;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataBuilder;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.BootstrapServiceRegistry;
import org.hibernate.boot.registry.BootstrapServiceRegistryBuilder;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.dialect.PostgresPlusDialect;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.type.BasicType;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

public class MetadataSourcesHelper {

    private BootstrapServiceRegistry bootstrapServiceRegistry;
    private StandardServiceRegistryBuilder standardServiceRegistryBuilder;

    private Metadata metadata;
    private Map<String, PersistentClass> mappings = new HashMap<>();
    private MetadataSources metadataSources = new MetadataSources();

    private ClassLoader classLoader;
    private File configFile;

    // TODO. 从Metadata中获取所有的加载的PersistentClass持久层实例对象
    protected Map<String, PersistentClass> getMappings() {
        Metadata innerMetadata = getMetadata();
        Iterator<PersistentClass> classMappings = innerMetadata.getEntityBindings().iterator();

        // 这里PersistentClass持久层对象的属性className和jpaEntityName在加载时被赋值 !!
        while (classMappings.hasNext()) {
            PersistentClass mapping = classMappings.next();
            mappings.put(mapping.getClassName(), mapping);
        }
        return mappings;
    }

    // TODO. 构建Metadata的完整配置，包含自定义的属性
    protected Metadata getMetadata() {
        if (metadata == null) {
            this.bootstrapServiceRegistry = new BootstrapServiceRegistryBuilder().applyClassLoader(classLoader).build();
            this.standardServiceRegistryBuilder = new StandardServiceRegistryBuilder(bootstrapServiceRegistry);
            if (configFile != null) {
                standardServiceRegistryBuilder.configure(configFile);
            }

            // 配置Properties信息必须和指定的数据库对应
            // 不使用cfg.xml的配置形式，这里的属性必须提供完整的JDBC连接信息
            Properties properties = new Properties();
            properties.put(AvailableSettings.URL, "jdbc:postgresql://localhost:5432/my_database");
            properties.put(AvailableSettings.USER, "postgres");
            properties.put(AvailableSettings.PASS, "admin");
            properties.put(AvailableSettings.DIALECT, PostgresPlusDialect.class.getName());
            standardServiceRegistryBuilder.applySettings(properties);

            StandardServiceRegistry standardServiceRegistry = standardServiceRegistryBuilder.build();
            MetadataBuilder metadataBuilder = metadataSources.getMetadataBuilder(standardServiceRegistry);

            // 注入自定义的基本类型
            for (BasicType customType : getCustomTypes()) {
                metadataBuilder.applyBasicType(customType);
            }
            metadata = metadataBuilder.build();
        }
        return metadata;
    }

    // 这里需要使用v6新版本的类型
    public static BasicType[] getCustomTypes() {
        return new BasicType[]{};
    }

    /**
     * Add HBM files to the metadata sources 添加指定路径下的映射文件
     *
     * @param mappingFiles HBM files or directories/jars containing HBM files
     */
    public void addHBMEntities(File... mappingFiles) {
        for (File mappingFile : mappingFiles) {
            try {
                if (mappingFile.getName().endsWith(".jar")) {
                    metadataSources.addJar(mappingFile);
                } else if (mappingFile.isDirectory()) {
                    metadataSources.addDirectory(mappingFile);
                } else {
                    metadataSources.addFile(mappingFile);
                }
            } catch (MappingException e) {
                throw new MappingException("Error with : " + mappingFile.getAbsolutePath(), e);
            }
        }
    }

    /**
     * Add HBM files to the metadata sources based on their Java classes.
     * We suppose that a class named com.example.Foo is mapped by a file com/example/Foo.hbm.xml
     */
    // 指定的映射文件必须在相同的package路径下才能被加载到metadata sources
    // V6版本必须添加带注解的Entity Class
    public void addHBMEntities(Class<?>... classes) {
        for (Class<?> clazz : classes) {
            // metadataSources.addClass(clazz);
            metadataSources.addAnnotatedClass(clazz);
        }
    }

    public void addHBMEntities(InputStream... inputStreams) {
        for (InputStream inputStream : inputStreams) {
            metadataSources.addInputStream(inputStream);
        }
    }

    // addAnnotatedClass() 加载有JPA注解的实体类型
    public void addJPAEntities(Class<?>... jpaEntities) {
        for (Class<?> jpaEntity : jpaEntities) {
            metadataSources.addAnnotatedClass(jpaEntity);
        }
    }
}
