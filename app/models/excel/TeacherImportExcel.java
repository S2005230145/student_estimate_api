package models.excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.annotation.ExcelProperty;
import io.ebean.DB;
import lombok.Data;
import models.admin.Group;
import models.admin.GroupUser;
import models.admin.ShopAdmin;
import models.business.ClassTeacherRelation;
import models.business.MonthlyRatingQuota;
import models.business.SchoolClass;
import myannotation.Translation;
import utils.DateUtils;
import utils.EncodeUtils;
import utils.ValidationUtil;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Data
@Translation("老师信息导入Excel")
public class TeacherImportExcel {

    @ExcelProperty(value = "教师姓名", index = 0)
    @Translation("教师姓名")
    private String teacherName;

    @ExcelProperty(value = "联系电话", index = 1)
    @Translation("联系电话")
    private String phoneNumber;

    @ExcelProperty(value = "教学职责", index = 2)
    @Translation("教学职责")
    private String teachingDuties; // 格式：班级|科目老师,班级|科目老师

    @ExcelProperty(value = "班主任", index = 3)
    @Translation("班主任")
    private String headTeacherClass; // 否 / 班级名

    private static final EncodeUtils encodeUtils = new EncodeUtils();
    private static final DateUtils dateUtils = new DateUtils();

    /** 入口：从 Excel 读取并批量导入 */
    public static void batchImport(InputStream inputStream, ShopAdmin currentAdmin) {
        List<TeacherImportExcel> excelList = EasyExcel.read(inputStream)
                .head(TeacherImportExcel.class)
                .sheet()
                .headRowNumber(1)
                .doReadSync();

        if (excelList == null || excelList.isEmpty()) {
            throw new RuntimeException("导入数据为空");
        }

        for (int i = 0; i < excelList.size(); i++) {
            TeacherImportExcel excel = excelList.get(i);
            int rowNum = i + 2; // 从第2行开始

            validateRow(excel, rowNum);
            importOneTeacher(excel, currentAdmin);
        }
    }

    /** 单行数据验证 */
    private static void validateRow(TeacherImportExcel excel, int rowNum) {
        if (ValidationUtil.isEmpty(excel.getTeacherName())) {
            throw new RuntimeException("第" + rowNum + "行: 教师姓名不能为空");
        }
        if (ValidationUtil.isEmpty(excel.getPhoneNumber())) {
            throw new RuntimeException("第" + rowNum + "行: 联系电话不能为空");
        }
        if (!excel.getPhoneNumber().matches("^1[3-9]\\d{9}$")) {
            throw new RuntimeException("第" + rowNum + "行: 联系电话格式不正确");
        }
        if (ValidationUtil.isEmpty(excel.getTeachingDuties())) {
            throw new RuntimeException("第" + rowNum + "行: 教学职责不能为空");
        }
    }

    /** 导入一名老师：创建/更新账号 + 分配角色 + 班主任 + 班级教师关系 + 自动分配月额度 */
    private static void importOneTeacher(TeacherImportExcel excel, ShopAdmin currentAdmin) {
        // 1. 找或建 ShopAdmin 账号（老师账号）
        ShopAdmin teacher = ShopAdmin.find.query()
                .where().eq("phone_number", excel.getPhoneNumber().trim())
                .setMaxRows(1)
                .findOne();

        if (teacher == null) {
            teacher = new ShopAdmin();
            teacher.setPhoneNumber(excel.getPhoneNumber().trim());
            teacher.setUserName(excel.getPhoneNumber().trim());
            teacher.setRealName(excel.getTeacherName().trim());
            // 默认密码：123456（加盐加密）
            teacher.setPassword(encodeUtils.getMd5WithSalt("123456"));
            teacher.setStatus(ShopAdmin.STATUS_NORMAL);
            teacher.setAdmin(false);
            // 机构信息从当前管理员继承
            teacher.setOrgId(currentAdmin.getOrgId());
            teacher.setOrgName(currentAdmin.getOrgName());
            teacher.setShopId(currentAdmin.getShopId());
            teacher.setShopName(currentAdmin.getShopName());
            long now = System.currentTimeMillis();
            teacher.setCreatedTime(now);
            teacher.setLastLoginTime(now);
            teacher.save();
        } else {
            // 已存在则更新姓名（可选）
            teacher.setRealName(excel.getTeacherName().trim());
            teacher.update();
        }

        // 2. 解析教学职责：格式为 "班级|科目老师,班级|科目老师"
        List<TeachingDuty> duties = parseTeachingDuties(excel.getTeachingDuties());
        
        // 3. 为每个教学职责创建班级教师关系
        boolean hasHeadTeacher = false; // 标记是否是班主任
        List<ClassTeacherRelation> createdRelations = new ArrayList<>(); // 记录新创建的关系
        
        for (TeachingDuty duty : duties) {
            SchoolClass schoolClass = SchoolClass.find.query()
                    .where().eq("class_name", duty.getClassName().trim())
                    .setMaxRows(1)
                    .findOne();
            if (schoolClass == null) {
                throw new RuntimeException("班级不存在: " + duty.getClassName());
            }

            // 判断该班级是否是班主任班级
            boolean isHead = !ValidationUtil.isEmpty(excel.getHeadTeacherClass()) 
                    && !"否".equals(excel.getHeadTeacherClass().trim())
                    && duty.getClassName().trim().equals(excel.getHeadTeacherClass().trim());
            if (isHead) {
                hasHeadTeacher = true;
            }

            // 插入 ClassTeacherRelation 记录（避免重复）
            if (!ClassTeacherRelation.isTeacherInClass(teacher.getId(), schoolClass.getId())) {
                ClassTeacherRelation relation = new ClassTeacherRelation();
                relation.setOrgId(currentAdmin.getOrgId());
                relation.setClassId(schoolClass.getId());
                relation.setTeacherId(teacher.getId());
                relation.setSubject(duty.getSubject());
                relation.setHeadTeacher(isHead);
                long now = System.currentTimeMillis();
                relation.setCreateTime(now);
                relation.setUpdateTime(now);
                relation.save();
                createdRelations.add(relation);
            }

            // 4. 如果是班主任，同步到 SchoolClass.headTeacherId
            if (isHead) {
                schoolClass.setHeadTeacherId(teacher.getId());
                schoolClass.update();
            }
        }

        // 5. 设置老师角色（rules）
        // 只要在一个班有任课就是"科任教师"
        // 只要在一个班有任课并且在一个班是班主任就是"科任教师,班主任"
        if (!duties.isEmpty()) {
            List<String> rulesList = new ArrayList<>();
            rulesList.add("科任教师");
            if (hasHeadTeacher) {
                rulesList.add("班主任");
            }
            String rules = String.join(",", rulesList);
            teacher.setRules(rules);
            teacher.update();
        }
        
        // 6. 为新创建的关系自动分配每月评分额度
        long currentTimeBySecond = System.currentTimeMillis();
        String monthKey = dateUtils.getCurrentMonth();
        for (ClassTeacherRelation relation : createdRelations) {
            MonthlyRatingQuota monthlyRatingQuota = new MonthlyRatingQuota();
            monthlyRatingQuota.setOrgId(currentAdmin.getOrgId());
            monthlyRatingQuota.setClassId(relation.getClassId());
            monthlyRatingQuota.setEvaluatorId(relation.getTeacherId());
            monthlyRatingQuota.setRoleType(teacher.getRules());
            monthlyRatingQuota.setMonthKey(monthKey);
            
            // 根据是否是班主任和科目来设置额度
            if (relation.isHeadTeacher) {
                monthlyRatingQuota.setRatingAmount(300);
            } else if (relation.getSubject().equals("语文") || relation.getSubject().equals("数学") || relation.getSubject().equals("英语")) {
                monthlyRatingQuota.setRatingAmount(200);
            } else if (relation.getSubject().equals("美术") || relation.getSubject().equals("音乐") || relation.getSubject().equals("体育")) {
                monthlyRatingQuota.setRatingAmount(50);
            } else {
                // 其他科目默认额度
                monthlyRatingQuota.setRatingAmount(50);
            }
            
            monthlyRatingQuota.setCreateTime(currentTimeBySecond);
            monthlyRatingQuota.setUpdateTime(currentTimeBySecond);
            monthlyRatingQuota.save();
        }
    }

    /** 解析教学职责字符串：格式为 "班级|科目老师,班级|科目老师" */
    private static List<TeachingDuty> parseTeachingDuties(String teachingDuties) {
        List<TeachingDuty> duties = new ArrayList<>();
        if (ValidationUtil.isEmpty(teachingDuties)) {
            return duties;
        }

        // 先按逗号分割多个职责
        String[] dutyArray = teachingDuties.split("[,，]");
        for (String dutyStr : dutyArray) {
            dutyStr = dutyStr.trim();
            if (ValidationUtil.isEmpty(dutyStr)) {
                continue;
            }

            // 按 | 分割班级和科目
            String[] parts = dutyStr.split("\\|");
            if (parts.length != 2) {
                throw new RuntimeException("教学职责格式错误，应为：班级|科目老师，实际：" + dutyStr);
            }

            String className = parts[0].trim();
            String roleName = parts[1].trim();
            String subject = parseSubjectFromRoleName(roleName);

            TeachingDuty duty = new TeachingDuty();
            duty.setClassName(className);
            duty.setSubject(subject);
            duties.add(duty);
        }

        return duties;
    }

    /** 从角色名里解析出科目，比如"语文老师" -> "语文" */
    private static String parseSubjectFromRoleName(String roleName) {
        if (ValidationUtil.isEmpty(roleName)) {
            return "";
        }
        // 移除"老师"、"班主任"等后缀
        String subject = roleName.replace("老师", "").replace("班主任", "").trim();
        return subject;
    }

    /** 教学职责内部类 */
    private static class TeachingDuty {
        private String className;
        private String subject;

        public String getClassName() {
            return className;
        }

        public void setClassName(String className) {
            this.className = className;
        }

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }
    }


}