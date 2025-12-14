package controllers.business;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import constants.BusinessConstant;
import controllers.BaseSecurityController;
import io.ebean.ExpressionList;
import io.ebean.PagedList;
import models.admin.ShopAdmin;
import models.business.ClassTeacherRelation;
import models.business.MonthlyRatingQuota;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import utils.ValidationUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;
import java.util.stream.Collectors;

public class ClassTeacherRelationController extends BaseSecurityController {

    /**
     * @api {GET} /v2/p/class_teacher_relation_list/   01列表-班级教师关系表
     * @apiName listClassTeacherRelation
     * @apiGroup CLASS-TEACHER-RELATION-CONTROLLER
     * @apiParam {int} page 页码
     * @apiParam {String} filter 搜索栏()
     * @apiSuccess (Success 200) {long} orgId 机构ID
     * @apiSuccess (Success 200) {long} id 唯一标识
     * @apiSuccess (Success 200) {long} classId 班级ID
     * @apiSuccess (Success 200) {long} teacherId 教师ID
     * @apiSuccess (Success 200) {String} subject 任教科目
     * @apiSuccess (Success 200) {boolean} isHeadTeacher 是否班主任
     * @apiSuccess (Success 200) {long} createTime 创建时间
     * @apiSuccess (Success 200) {long} updateTime 更新时间
     */
    public CompletionStage<Result> listClassTeacherRelation(Http.Request request, int page, String filter, int status) {
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((adminMember) -> {
            if (null == adminMember) return unauth403();
            ExpressionList<ClassTeacherRelation> expressionList = ClassTeacherRelation.find.query().where().le("org_id", adminMember.getOrgId());
            if (status > 0) expressionList.eq("status", status);
            if (!ValidationUtil.isEmpty(filter)) expressionList
                    .or()
                    .icontains("filter", filter)
                    .endOr();               //编写其他条件  
            //编写其他条件
            //编写其他条件
            //编写其他条件

            ObjectNode result = Json.newObject();
            List<ClassTeacherRelation> list;
            if (page == 0) list = expressionList.findList();
            else {
                PagedList<ClassTeacherRelation> pagedList = expressionList
                        .order().desc("id")
                        .setFirstRow((page - 1) * BusinessConstant.PAGE_SIZE_20)
                        .setMaxRows(BusinessConstant.PAGE_SIZE_20)
                        .findPagedList();
                list = pagedList.getList();
                result.put("pages", pagedList.getTotalPageCount());
                result.put("hasNest", pagedList.hasNext());
            }
            result.put(CODE, CODE200);
            result.set("list", Json.toJson(list));
            return ok(result);

        });

    }

    /**
     * @api {GET} /v2/p/class_teacher_relation/:id/  02详情-ClassTeacherRelation班级教师关系表
     * @apiName getClassTeacherRelation
     * @apiGroup CLASS-TEACHER-RELATION-CONTROLLER
     * @apiParam {long} id id
     * @apiSuccess (Success 200){int} code 200
     * @apiSuccess (Success 200) {long} orgId 机构ID
     * @apiSuccess (Success 200) {long} id 唯一标识
     * @apiSuccess (Success 200) {long} classId 班级ID
     * @apiSuccess (Success 200) {long} teacherId 教师ID
     * @apiSuccess (Success 200) {String} subject 任教科目
     * @apiSuccess (Success 200) {boolean} isHeadTeacher 是否班主任
     * @apiSuccess (Success 200) {long} createTime 创建时间
     * @apiSuccess (Success 200) {long} updateTime 更新时间
     */
    public CompletionStage<Result> getClassTeacherRelation(Http.Request request, long id) {
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((adminMember) -> {
            if (null == adminMember) return unauth403();
            ClassTeacherRelation classTeacherRelation = ClassTeacherRelation.find.byId(id);
            if (null == classTeacherRelation) return okCustomJson(CODE40001, "数据不存在");
            //sass数据校验  
            if (classTeacherRelation.orgId > adminMember.getOrgId()) return okCustomJson(CODE40001, "数据不存在");
            ObjectNode result = (ObjectNode) Json.toJson(classTeacherRelation);
            result.put(CODE, CODE200);
            return ok(result);
        });

    }

    /**
     * @api {POST} /v2/p/class_teacher_relation/new/   01添加-ClassTeacherRelation班级教师关系表
     * @apiName addClassTeacherRelation
     * @apiDescription 描述
     * @apiGroup CLASS-TEACHER-RELATION-CONTROLLER
     * @apiParam {long} orgId 机构ID
     * @apiParam {long} id 唯一标识
     * @apiParam {long} classId 班级ID
     * @apiParam {long} teacherId 教师ID
     * @apiParam {String} subject 任教科目
     * @apiParam {boolean} isHeadTeacher 是否班主任
     * @apiParam {long} createTime 创建时间
     * @apiParam {long} updateTime 更新时间
     * @apiSuccess (Success 200){int} code 200
     */

    public CompletionStage<Result> addClassTeacherRelation(Http.Request request) {
        JsonNode jsonNode = request.body().asJson();
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((admin) -> {
            if (null == admin) return unauth403();
            if (null == jsonNode) return okCustomJson(CODE40001, "参数错误");
            ClassTeacherRelation classTeacherRelation = Json.fromJson(jsonNode, ClassTeacherRelation.class);
// 数据sass化
            classTeacherRelation.setOrgId(admin.getOrgId());
            long currentTimeBySecond = dateUtils.getCurrentTimeByMilliSecond();
            classTeacherRelation.setCreateTime(currentTimeBySecond);
            classTeacherRelation.setUpdateTime(currentTimeBySecond);
            classTeacherRelation.save();
//分配每月评分额度
            MonthlyRatingQuota monthlyRatingQuota = new MonthlyRatingQuota();
            monthlyRatingQuota.setOrgId(admin.getOrgId());
            monthlyRatingQuota.setClassId(classTeacherRelation.getClassId());
            monthlyRatingQuota.setEvaluatorId(classTeacherRelation.getTeacherId());
            monthlyRatingQuota.setRoleType(admin.getRules());
            //获取当前月的月份
            String monthKey = dateUtils.getCurrentMonth();
            monthlyRatingQuota.setMonthKey(monthKey);
            if(admin.getRules().contains("班主任")){
                monthlyRatingQuota.setRatingAmount(300);
            }else if(admin.getRules().contains("科任教师")){
                monthlyRatingQuota.setRatingAmount(200);
            }else if(admin.getRules().contains("其他老师")){
                monthlyRatingQuota.setRatingAmount(50);
            }
            monthlyRatingQuota.setCreateTime(currentTimeBySecond);
            monthlyRatingQuota.setUpdateTime(currentTimeBySecond);
            monthlyRatingQuota.save();
            return okJSON200();
        });
    }

    /**
     * @api {POST} /v2/p/class_teacher_relation/:id/  04更新-ClassTeacherRelation班级教师关系表
     * @apiName updateClassTeacherRelation
     * @apiGroup CLASS-TEACHER-RELATION-CONTROLLER
     * @apiParam {long} orgId 机构ID
     * @apiParam {long} id 唯一标识
     * @apiParam {long} classId 班级ID
     * @apiParam {long} teacherId 教师ID
     * @apiParam {String} subject 任教科目
     * @apiParam {boolean} isHeadTeacher 是否班主任
     * @apiParam {long} createTime 创建时间
     * @apiParam {long} updateTime 更新时间
     * @apiSuccess (Success 200){int} code 200
     */
    public CompletionStage<Result> updateClassTeacherRelation(Http.Request request, long id) {
        JsonNode jsonNode = request.body().asJson();
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((adminMember) -> {
            if (null == adminMember) return unauth403();
            ClassTeacherRelation originalClassTeacherRelation = ClassTeacherRelation.find.byId(id);
            ClassTeacherRelation newClassTeacherRelation = Json.fromJson(jsonNode, ClassTeacherRelation.class);
            if (null == originalClassTeacherRelation) return okCustomJson(CODE40001, "数据不存在");
            //sass数据校验  
            if (originalClassTeacherRelation.orgId != adminMember.getOrgId())
                return okCustomJson(CODE40001, "数据不存在");
            if (newClassTeacherRelation.classId > 0)
                originalClassTeacherRelation.setClassId(newClassTeacherRelation.classId);
            if (newClassTeacherRelation.teacherId > 0)
                originalClassTeacherRelation.setTeacherId(newClassTeacherRelation.teacherId);
            if (!ValidationUtil.isEmpty(newClassTeacherRelation.subject))
                originalClassTeacherRelation.setSubject(newClassTeacherRelation.subject);
            if (newClassTeacherRelation.isHeadTeacher != originalClassTeacherRelation.isHeadTeacher)
                originalClassTeacherRelation.setHeadTeacher(newClassTeacherRelation.isHeadTeacher);
            if (newClassTeacherRelation.updateTime > 0)
                originalClassTeacherRelation.setUpdateTime(newClassTeacherRelation.updateTime);
            long currentTimeBySecond = dateUtils.getCurrentTimeByMilliSecond();
            originalClassTeacherRelation.setUpdateTime(currentTimeBySecond);
            originalClassTeacherRelation.save();
            return okJSON200();
        });
    }

    /**
     * @api {POST} /v2/p/class_teacher_relation/   05删除-班级教师关系表
     * @apiName deleteClassTeacherRelation
     * @apiGroup CLASS-TEACHER-RELATION-CONTROLLER
     * @apiParam {long} id id
     * @apiParam {String} operation del时删除
     * @apiSuccess (Success 200){int} 200 成功
     */
    public CompletionStage<Result> deleteClassTeacherRelation(Http.Request request) {
        JsonNode jsonNode = request.body().asJson();
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((adminMember) -> {
            if (null == adminMember) return unauth403();
            long id = jsonNode.findPath("id").asLong();
            String operation = jsonNode.findPath("operation").asText();
            if (!"del".equals(operation)) return okCustomJson(CODE40001, "操作错误");
            ClassTeacherRelation deleteModel = ClassTeacherRelation.find.byId(id);
            if (null == deleteModel) return okCustomJson(CODE40001, "数据不存在");
            //sass数据校验  
            if (deleteModel.orgId != adminMember.getOrgId()) return okCustomJson(CODE40001, "数据不存在");
            deleteModel.delete();
            return okJSON200();
        });
    }

    /**
     * @api {GET} /v2/p/class_teacher_relation_list/new/   06列表-班级教师关系列表
     * @apiName listClassTeacherRelation
     * @apiGroup CLASS-TEACHER-RELATION-CONTROLLER
     * @apiParam {int} page 页码
     * @apiParam {String} filter 搜索栏()
     * @apiSuccess (Success 200) {long} orgId 机构ID
     * @apiSuccess (Success 200) {long} id 唯一标识
     * @apiSuccess (Success 200) {long} classId 班级ID
     * @apiSuccess (Success 200) {long} teacherId 教师ID
     * @apiSuccess (Success 200) {String} teacherName 教师姓名
     * @apiSuccess (Success 200) {String} subject 任教科目
     * @apiSuccess (Success 200) {boolean} isHeadTeacher 是否班主任
     * @apiSuccess (Success 200) {long} createTime 创建时间
     * @apiSuccess (Success 200) {long} updateTime 更新时间
     */
    public CompletionStage<Result> listClassTeacherRelationNew(Http.Request request, int page, String filter, int status) {
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((adminMember) -> {
            if (null == adminMember) return unauth403();
            ExpressionList<ClassTeacherRelation> expressionList = ClassTeacherRelation.find.query().where().le("org_id", adminMember.getOrgId());
            if (status > 0) expressionList.eq("status", status);
            if (!ValidationUtil.isEmpty(filter)) expressionList
                    .or()
                    .icontains("filter", filter)
                    .endOr();               //编写其他条件
            //编写其他条件
            //编写其他条件
            //编写其他条件

            ObjectNode result = Json.newObject();
            List<ClassTeacherRelation> list;
            if (page == 0) list = expressionList.findList();
            else {
                PagedList<ClassTeacherRelation> pagedList = expressionList
                        .order().desc("id")
                        .setFirstRow((page - 1) * BusinessConstant.PAGE_SIZE_20)
                        .setMaxRows(BusinessConstant.PAGE_SIZE_20)
                        .findPagedList();
                list = pagedList.getList();
                result.put("pages", pagedList.getTotalPageCount());
                result.put("hasNest", pagedList.hasNext());
            }
            
            // 批量获取教师姓名
            Set<Long> teacherIds = list.stream()
                    .map(ctr -> ctr.teacherId)
                    .filter(id -> id > 0)
                    .collect(Collectors.toSet());
            
            Map<Long, String> teacherNameMap = new HashMap<>();
            if (!teacherIds.isEmpty()) {
                List<ShopAdmin> teachers = ShopAdmin.find.query()
                        .where()
                        .in("id", teacherIds)
                        .findList();
                for (ShopAdmin teacher : teachers) {
                    teacherNameMap.put(teacher.id, teacher.realName);
                }
            }
            
            // 为每个关系添加教师姓名
            List<ObjectNode> listWithTeacherName = list.stream().map(ctr -> {
                ObjectNode item = (ObjectNode) Json.toJson(ctr);
                String teacherName = teacherNameMap.getOrDefault(ctr.teacherId, "");
                item.put("teacherName", teacherName);
                return item;
            }).collect(Collectors.toList());
            
            result.put(CODE, CODE200);
            result.set("list", Json.toJson(listWithTeacherName));
            return ok(result);
        });

    }
}
