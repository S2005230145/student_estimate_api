package models.business;

import io.ebean.Finder;
import io.ebean.Model;
import io.ebean.annotation.DbComment;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "v1_class_config")
@DbComment("班级配置表")
public class ClassConfig extends Model {
    @Id
    @Column(name = "id")
    @DbComment("id")
    private String id;

    @Column(name = "class_name")
    @DbComment("班级名称")
    private String className;

    @Column(name = "org_id")
    @DbComment("机构id")
    private String orgId;

    @Column(name = "status")
    @DbComment("状态")
    private Long  status;

    public static Finder<String, ClassConfig> find = new Finder<>(ClassConfig.class);
}
