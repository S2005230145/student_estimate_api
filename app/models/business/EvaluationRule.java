package models.business;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.ebean.Finder;
import io.ebean.Model;
import jakarta.persistence.*;
import lombok.Data;
import myannotation.EscapeHtmlAuthoritySerializer;
import myannotation.Translation;

@Data
@Entity
@Table(name = "v1_evaluation_rule")
@Translation("评价规则配置")
public class EvaluationRule  extends Model {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Translation("唯一标识")
    public long id;
    
    @Column(name = "rule_type")
    @Translation("规则类型")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String ruleType;
    
    @Column(name = "condition")
    @Translation("条件")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String condition;
    
    @Column(name = "score")
    @Translation("得分")
    public double score;
    
    @Column(name = "badge_type")
    @Translation("徽章类型")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String badgeType;
    
    @Column(name = "description")
    @Translation("描述")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String description;
    
    @Column(name = "active")
    @Translation("是否启用")
    public boolean active;
    
    @Column(name = "create_time")
    @Translation("创建时间")
    public long createTime;

    public static Finder<Long, EvaluationRule> find = new Finder<>(EvaluationRule.class);
}