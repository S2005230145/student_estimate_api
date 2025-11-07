package models.business;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.ebean.Finder;
import io.ebean.Model;
import io.ebean.annotation.DbComment;
import jakarta.persistence.*;
import lombok.Data;
import myannotation.EscapeHtmlAuthoritySerializer;
import myannotation.Translation;

@Data
@Entity
@Table(name = "v1_evaluation_rule")
@DbComment("评价规则配置")
public class EvaluationRule  extends Model {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @DbComment("唯一标识")
    public long id;
    
    @Column(name = "rule_type")
    @DbComment("规则类型")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String ruleType;
    
    @Column(name = "conditions")
    @DbComment("条件")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String conditions;
    
    @Column(name = "score")
    @DbComment("得分")
    public double score;
    
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

    public static Finder<Long, EvaluationRule> find = new Finder<>(EvaluationRule.class);
}