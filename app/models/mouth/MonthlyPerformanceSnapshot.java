package models.mouth;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.ebean.Finder;
import io.ebean.Model;
import io.ebean.annotation.DbComment;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import myannotation.EscapeHtmlSerializer;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "monthly_performance_snapshot")
public class MonthlyPerformanceSnapshot extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @DbComment("唯一标识")
    private Long Id;
    @Column(name = "student_id")
    private Long studentId;
    @Column(name = "year")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    private String year;
    @Column(name = "mouth")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    private String mouth;
    @Column(name = "sum_final_score")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    private Double sumFinalScore;
    @Column(name = "settle_state")
    private Long settleState;
    @Column(name = "settle_time")
    private Long settleTime;
    @Column(name = "type")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    private String type;

    public static final Finder<Long, MonthlyPerformanceSnapshot> find = new Finder<>(MonthlyPerformanceSnapshot.class);

}
