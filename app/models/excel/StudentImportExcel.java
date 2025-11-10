package models.excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import models.admin.ShopAdmin;
import models.business.ParentStudentRelation;
import models.business.SchoolClass;
import models.business.Student;
import myannotation.Translation;
import utils.EncodeUtils;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

@Data
@Translation("学生信息")
public class StudentImportExcel {

    @ExcelIgnore
    @Translation("班级ID")
    public long classId;

    @ExcelProperty(value = "学号", index = 0)
    @Translation("学号")
    public String studentNumber;

    @ExcelProperty(value = "姓名", index = 1)
    @Translation("姓名")
    public String name;

    @ExcelProperty(value = "关系一", index = 2)
    @Translation("关系一")
    public String relationNameOne;

    @ExcelProperty(value = "关系一手机号", index = 3)
    @Translation("关系一手机号")
    public String relationPhoneOne;

    @ExcelProperty(value = "关系二", index = 4)
    @Translation("关系二")
    public String relationNameTwo;

    @ExcelProperty(value = "关系二手机号", index = 5)
    @Translation("关系二手机号")
    public String relationPhoneTwo;

    @ExcelIgnore
    private static EncodeUtils encodeUtils = new EncodeUtils();

    /**
     * 转为实体类数据
     */
    public static void toEntity(List<StudentImportExcel> list, long classId) {
        if (list == null || list.isEmpty()) {
            throw new RuntimeException("数据为空");
        }

        // 验证班级是否存在
        SchoolClass schoolClass = SchoolClass.find.byId(classId);
        if (schoolClass == null) {
            throw new RuntimeException("班级不存在");
        }

        for (StudentImportExcel studentExcel : list) {
            // 验证学号唯一性
            Student existing = Student.find.query()
                    .where()
                    .eq("student_number", studentExcel.getStudentNumber())
                    .findOne();
            if (existing != null) {
                throw new RuntimeException("学号 " + studentExcel.getStudentNumber() + " 已存在");
            }

            // 创建学生
            Student student = createStudent(studentExcel, classId);

            // 创建家长关系和账号
            createParentRelations(studentExcel, student.getId());
        }
    }

    /**
     * 创建学生
     */
    private static Student createStudent(StudentImportExcel excel, long classId) {
        Student student = new Student();
        student.setStudentNumber(excel.getStudentNumber());
        student.setName(excel.getName());
        SchoolClass schoolClass = SchoolClass.find.byId(classId);
        student.setClassId(classId);
        if (schoolClass != null) {
            student.setGrade(schoolClass.grade);
        }
        student.setCreateTime(System.currentTimeMillis());
        student.setUpdateTime(System.currentTimeMillis());
        student.save();

        return student;
    }

    /**
     * 创建家长关系和账号
     */
    private static void createParentRelations(StudentImportExcel excel, long studentId) {
        String studentName = excel.getName();

        // 创建关系一账号和关系
        if (isValidParentInfo(excel.getRelationNameOne(), excel.getRelationPhoneOne())) {
            ShopAdmin relationOne = findOrCreateParent(
                    excel.getRelationNameOne(),
                    excel.getRelationPhoneOne(),
                    excel.getRelationNameOne(),
                    studentName
            );
            if (relationOne != null) {
                ParentStudentRelation.addRelation(relationOne.getId(), studentId, excel.getRelationNameOne());
            }
        }

        // 创建关系二账号和关系
        if (isValidParentInfo(excel.getRelationNameTwo(), excel.getRelationPhoneTwo())) {
            ShopAdmin relationTwo = findOrCreateParent(
                    excel.getRelationNameTwo(),
                    excel.getRelationPhoneTwo(),
                    excel.getRelationNameTwo(),
                    studentName
            );
            if (relationTwo != null) {
                ParentStudentRelation.addRelation(relationTwo.getId(), studentId, excel.getRelationNameTwo());
            }
        }
    }

    /**
     * 查找或创建家长账号
     */
    private static ShopAdmin findOrCreateParent(String name, String phone, String relationship, String studentName) {
        if (name == null || name.trim().isEmpty() || phone == null || phone.trim().isEmpty()) {
            return null;
        }

        name = name.trim();
        phone = phone.trim();

        // 验证手机号格式
        if (!isValidPhone(phone)) {
            throw new RuntimeException("家长身份： " + name + " 的手机号格式不正确: " + phone);
        }

        // 查找现有家长（按手机号查找，确保唯一性）
        ShopAdmin parent = ShopAdmin.find.query()
                .where()
                .eq("phone_number", phone)
                .findOne();

        if (parent != null) {
//            // 如果找到现有家长，更新姓名（如果姓名不同）
            if (!generateParentRealName(studentName, relationship).equals(parent.getRealName())) {
                parent.setUserName(name);
                parent.update();
            }
            return parent;
        }

        // 创建新家长账号
        parent = new ShopAdmin();
        parent.setPhoneNumber(phone);
        parent.setUserName(phone); // 用户名设为手机号
        parent.setRealName(generateParentRealName(studentName, relationship)); // 真实姓名设为"学生名_关系"
        parent.setRules("家长");
        parent.setPassword(generateDefaultPassword());
        parent.save();

        return parent;
    }

    /**
     * 生成家长真实姓名（学生名_关系）
     */
    private static String generateParentRealName(String studentName, String relationship) {
        return studentName + "-" + relationship;
    }

    /**
     * 生成默认密码
     */
    private static String generateDefaultPassword() {
        return encodeUtils.getMd5WithSalt("123456");
    }

    /**
     * 验证家长信息是否有效
     */
    private static boolean isValidParentInfo(String name, String phone) {
        return name != null && !name.trim().isEmpty() &&
                phone != null && !phone.trim().isEmpty();
    }

    /**
     * 验证手机号格式
     */
    private static boolean isValidPhone(String phone) {
        return phone != null && phone.matches("^1[3-9]\\d{9}$");
    }

    /**
     * 将Student实体列表转换为StudentImportExcel列表
     */
    public static List<StudentImportExcel> toExcel(List<Student> list) {
        if (list == null || list.isEmpty()) {
            throw new RuntimeException("数据为空");
        }

        List<StudentImportExcel> result = new ArrayList<>();
        for (Student student : list) {
            StudentImportExcel studentExcel = new StudentImportExcel();
            studentExcel.setStudentNumber(student.getStudentNumber());
            studentExcel.setName(student.getName());
            studentExcel.setClassId(student.getClassId());

            // 查询家长信息
            List<ParentStudentRelation> parentRelations = ParentStudentRelation.findByStudentId(student.getId());
            int relationCount = 0;
            for (ParentStudentRelation relation : parentRelations) {
                ShopAdmin parent = ShopAdmin.find.byId(relation.getParentId());
                if (parent != null) {
                    if (relationCount == 0) {
                        studentExcel.setRelationNameOne(relation.getRelationship());
                        studentExcel.setRelationPhoneOne(parent.getPhoneNumber());
                    } else if (relationCount == 1) {
                        studentExcel.setRelationNameTwo(relation.getRelationship());
                        studentExcel.setRelationPhoneTwo(parent.getPhoneNumber());
                    }
                    relationCount++;
                }
            }

            result.add(studentExcel);
        }
        return result;
    }

    /**
     * 导入Excel数据，转换为StudentImportExcel列表
     */
    public static List<StudentImportExcel> importFromExcel(InputStream inputStream) {
        List<StudentImportExcel> list = EasyExcel.read(inputStream)
                .head(StudentImportExcel.class)
                .sheet()
                .doReadSync();

        if (list == null || list.isEmpty()) {
            throw new RuntimeException("导入数据为空");
        }
        return list;
    }

    /**
     * 导出StudentImportExcel列表到Excel
     */
    public static void exportToExcel(OutputStream outputStream, List<StudentImportExcel> data) {
        EasyExcel.write(outputStream, StudentImportExcel.class)
                .sheet("学生信息")
                .doWrite(data);
    }


    /**
     * 数据验证
     */
    public static void validateData(List<StudentImportExcel> list) {
        if (list == null || list.isEmpty()) {
            throw new RuntimeException("数据为空");
        }

        for (int i = 0; i < list.size(); i++) {
            StudentImportExcel excel = list.get(i);
            int rowNum = i + 2; // 从第2行开始（标题行是第1行）

            // 必填字段验证
            if (excel.getStudentNumber() == null || excel.getStudentNumber().trim().isEmpty()) {
                throw new RuntimeException("第" + rowNum + "行: 学号不能为空");
            }

            if (excel.getName() == null || excel.getName().trim().isEmpty()) {
                throw new RuntimeException("第" + rowNum + "行: 姓名不能为空");
            }

            // 学号唯一性验证
            Student existing = Student.find.query()
                    .where()
                    .eq("student_number", excel.getStudentNumber().trim())
                    .findOne();
            if (existing != null) {
                throw new RuntimeException("第" + rowNum + "行: 学号 " + excel.getStudentNumber() + " 已存在");
            }

            // 家长信息验证
            validateParentInfo(excel, rowNum);
        }
    }

    /**
     * 验证家长信息
     */
    private static void validateParentInfo(StudentImportExcel excel, int rowNum) {
        // 至少需要一个家长
        boolean hasRelationOne = isValidParentInfo(excel.getRelationNameOne(), excel.getRelationPhoneOne());
        boolean hasRelationTwo = isValidParentInfo(excel.getRelationNameTwo(), excel.getRelationPhoneTwo());

        if (!hasRelationOne && !hasRelationTwo) {
            throw new RuntimeException("第" + rowNum + "行: 至少需要填写一个家长信息");
        }

        // 验证关系一信息
        if (hasRelationOne) {
            if (!isValidPhone(excel.getRelationPhoneOne().trim())) {
                throw new RuntimeException("第" + rowNum + "行: 关系一手机号格式不正确");
            }
        }

        // 验证关系二信息
        if (hasRelationTwo) {
            if (!isValidPhone(excel.getRelationPhoneTwo().trim())) {
                throw new RuntimeException("第" + rowNum + "行: 关系二手机号格式不正确");
            }
        }

        // 验证关系名称不能为空
        if (hasRelationOne && (excel.getRelationNameOne() == null || excel.getRelationNameOne().trim().isEmpty())) {
            throw new RuntimeException("第" + rowNum + "行: 关系一名称不能为空");
        }

        if (hasRelationTwo && (excel.getRelationNameTwo() == null || excel.getRelationNameTwo().trim().isEmpty())) {
            throw new RuntimeException("第" + rowNum + "行: 关系二名称不能为空");
        }
    }

    /**
     * 批量导入学生数据
     */
    public static void batchImport(InputStream inputStream, long classId) {
        // 读取Excel数据
        List<StudentImportExcel> excelData = importFromExcel(inputStream);

        // 数据验证
        validateData(excelData);

        // 转换为实体并保存
        toEntity(excelData, classId);
    }


}