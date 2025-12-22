package models.business;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.ebean.Finder;
import io.ebean.Model;
import io.ebean.annotation.DbComment;
import jakarta.persistence.*;
import lombok.Data;
import myannotation.EscapeHtmlAuthoritySerializer;
import myannotation.Translation;

import java.util.List;

@Data
@Entity
@Table(name = "v1_evaluation_rule")
@DbComment("评价规则配置")
public class EvaluationRule  extends Model {

    @Column(name = "org_id")
    @DbComment("机构ID")
    public long orgId;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @DbComment("唯一标识")
    public long id;

    @Column(name = "name")
    @DbComment("指标名称")
    public String name;

    @Column(name = "score_basic")
    @DbComment("基础分")
    public double scoreBasic;

    @Column(name = "score_max")
    @DbComment("上限分")
    public double scoreMax;
    
    @Column(name = "badge_type")
    @DbComment("徽章类型")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String badgeType;

    @Column(name = "description")
    @DbComment("描述")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String description;
    
    @Column(name = "active")
    @DbComment("是否启用")
    public boolean active;
    
    @Column(name = "create_time")
    @DbComment("创建时间")
    public long createTime;

    @Transient
    @JsonIgnore
    public List<Badge> badges;

    public static Finder<Long, EvaluationRule> find = new Finder<>(EvaluationRule.class);
}