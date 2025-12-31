# API 文档

## 基础说明

### 响应格式

所有API响应都遵循以下格式：

**成功响应：**
```json
{
  "code": 200,
  // 其他数据字段...
}
```

**错误响应：**
```json
{
  "code": 40001,
  "reason": "错误信息"
}
```

### 错误码说明

- `200`: 成功
- `403`: 未授权
- `40001`: 参数错误/数据不存在
- `40002`: 验证码错误/账号已被注册
- `40003`: 用户名或密码错误
- `40004`: 无效的手机号码/密码
- `40005`: 该帐号不存在
- `40006`: 无效的手机号码
- `40008`: 账号被锁定/权限不足

### 认证说明

大部分接口需要在请求头中携带 `token` 进行认证（登录接口除外）。

---

## 1. 文件上传模块

### 1.1 通用文件上传
**接口地址：** `POST /v2/p/upload/`  
**接口说明：** 通用文件上传接口  
**请求头：** `Content-Type: multipart/form-data`  
**请求参数：**
- `file` (file, 必填): 上传的文件

**响应示例：**
```json
{
  "code": 200,
  "url": "文件访问地址"
}
```

### 1.2 上传2
**接口地址：** `POST /v2/p/upload2/`  
**接口说明：** 文件上传接口2  
**请求头：** `Content-Type: multipart/form-data`  
**请求参数：**
- `file` (file, 必填): 上传的文件

### 1.3 Base64上传
**接口地址：** `POST /v2/p/upload_base64/`  
**接口说明：** Base64格式文件上传  
**请求体：**
```json
{
  "file": "base64编码的文件内容",
  "filename": "文件名"
}
```

### 1.4 图片上传（用这个）
**接口地址：** `POST /v2/p/file/upload_image/`  
**接口说明：** 图片上传接口  
**请求头：** `Content-Type: multipart/form-data`  
**请求参数：**
- `file` (file, 必填): 图片文件

---

## 2. 登录认证模块

### 2.1 后台登录
**接口地址：** `POST /v2/p/login/noauth/`  
**接口说明：** 后台用户登录（学校端）  
**请求体：**
```json
{
  "username": "手机号码",
  "password": "密码",
  "vcode": "验证码（可选）",
  "type": "类型（可选）"
}
```

**响应示例：**
```json
{
  "id": 2,
  "userName": "13625063671",
  "realName": "真实姓名",
  "avatar": null,
  "createdTime": 1766211144978,
  "lastLoginTime": 1766211144978,
  "lastLoginIP": null,
  "phoneNumber": "13625063671",
  "isAdmin": false,
  "orgId": 1,
  "orgName": null,
  "shopId": 0,
  "shopName": null,
  "rules": "管理员",
  "status": 1,
  "code": 200,
  "token": "4c854b3a-e0fd-4d89-a89e-02624086d364"
}
```

### 2.2 前台登录
**接口地址：** `POST /V2/p/front/login/noauth/`  
**接口说明：** 前台app登录模块  
**请求体：**
```json
{
  "username": "用户名",
  "password": "密码",
  "loginRules": "0",  // 0-家长登录  1-非家长登录
  "vcode": "验证码（可选）"
}
```

**响应示例：** 同后台登录

### 2.3 检查登录状态
**接口地址：** `GET /v2/p/is_login/`  
**接口说明：** 检查是否已登录  
**响应示例：**
```json
{
  "code": 200,
  "login": true
}
```

### 2.4 用户注册
**接口地址：** `POST /v2/p/user/new/`  
**接口说明：** 商户入驻注册  
**请求体：**
```json
{
  "phoneNumber": "手机号",
  "password": "登录密码（6-20位）",
  "vCode": "短信验证码（预留）"
}
```

### 2.5 获取管理员信息
**接口地址：** `GET /v2/p/admin_member/info/`  
**接口说明：** 查看自己详情信息  
**响应示例：**
```json
{
  "code": 200,
  "id": 1,
  "name": "真实姓名",
  "nickName": "用户名",
  "avatar": "头像URL",
  "status": 1,
  "shopName": "店铺名称",
  "shopId": 1
}
```

### 2.6 重置登录密码
**接口地址：** `POST /v2/p/reset_login_password/`  
**接口说明：** 重置登录密码  
**请求体：**
```json
{
  "accountName": "账号",
  "vcode": "短信验证码",
  "newPassword": "新密码"
}
```

### 2.7 设置/修改登录密码
**接口地址：** `POST /v2/p/set_login_password/`  
**接口说明：** 设置/修改登录密码  
**请求体：**
```json
{
  "oldPassword": "旧密码（可选）",
  "password": "新密码",
  "vcode": "短信验证码（可选）",
  "uid": "用户ID"
}
```

### 2.8 退出登录
**接口地址：** `POST /v2/p/logout/`  
**接口说明：** 用户退出登录  
**响应示例：**
```json
{
  "code": 200
}
```

---

## 3. 学生模块

### 3.1 学生列表
**接口地址：** `POST /v2/p/student_list/`  
**接口说明：** 获取学生列表（系统会根据登录的对应账号返回对应的学生列表）  
**请求体：**
```json
{
  "page": 1,  // 0-全查  1-分页
  "studentName": "学生姓名（可选）"
}
```

**响应示例：**
```json
{
  "code": 200,
  "pages": 108,
  "hasNest": true,
  "list": [
    {
      "orgId": 1,
      "id": 2174,
      "studentNumber": "20200754",
      "name": "严欣瑶",
      "classId": 47,
      "grade": 6,
      "classHg": 7,
      "evaluationScheme": 0,
      "classAverageScore": 0.0,
      "academicScore": 0.0,
      "specialtyScore": 0.0,
      "habitScore": 15.0,
      "points": 0.0,
      "totalScore": 0.0,
      "badges": null,
      "rewardRankGrade": 0,
      "rewardRankSchool": 0,
      "createTime": 1766649180731,
      "updateTime": 1766649180731,
      "className": "六年级七班",
      "overAverage": false,
      "highGrade": true,
      "pass": false
    }
  ]
}
```

### 3.2 学生详情
**接口地址：** `GET /v2/p/student/:id/`  
**接口说明：** 获取学生详情  
**路径参数：**
- `id` (long, 必填): 学生ID

**响应示例：**
```json
{
  "code": 200,
  "orgId": 1,
  "id": 2174,
  "studentNumber": "20200754",
  "name": "严欣瑶",
  "classId": 47,
  "grade": 6,
  "evaluationScheme": 0,
  "classAverageScore": 0.0,
  "academicScore": 0.0,
  "specialtyScore": 0.0,
  "habitScore": 15.0,
  "totalScore": 0.0,
  "badges": null,
  "createTime": 1766649180731,
  "updateTime": 1766649180731
}
```

### 3.3 添加学生
**接口地址：** `POST /v2/p/student/new/`  
**接口说明：** 添加学生  
**请求体：**
```json
{
  "studentNumber": "学号",
  "name": "学生姓名",
  "classId": 47,
  "grade": 6,
  "evaluationScheme": 0,
  "classAverageScore": 0.0,
  "academicScore": 0.0,
  "specialtyScore": 0.0,
  "habitScore": 0.0,
  "totalScore": 0.0,
  "badges": null
}
```

### 3.4 更新学生
**接口地址：** `POST /v2/p/student/:id/`  
**接口说明：** 更新学生信息  
**路径参数：**
- `id` (long, 必填): 学生ID

**请求体：** 同添加学生

### 3.5 删除学生
**接口地址：** `POST /v2/p/student/`  
**接口说明：** 删除学生  
**请求体：**
```json
{
  "id": 2174,
  "operation": "del"
}
```

### 3.6 导入学生（按班级）
**接口地址：** `POST /v2/p/student_excel/?classId=47`  
**接口说明：** 导入学生文件（按班级）  
**请求头：** `Content-Type: multipart/form-data`  
**请求参数：**
- `file` (file, 必填): Excel文件
- `classId` (long, 必填): 班级ID（URL参数）

### 3.7 导出学生导入模板
**接口地址：** `GET /v2/p/student_excel_template/`  
**接口说明：** 导出学生导入模板  
**响应：** Excel文件下载

### 3.8 导入学生（全校）
**接口地址：** `POST /v2/p/student_excel_school/`  
**接口说明：** 导入学生文件（全校）  
**请求头：** `Content-Type: multipart/form-data`  
**请求参数：**
- `file` (file, 必填): Excel文件

### 3.9 创建学生家长关系
**接口地址：** `POST /v2/p/student_parent/`  
**接口说明：** 创建学生家长关系  
**请求体：**
```json
{
  "studentId": 2174,
  "parentPhone": "13800138000",
  "relationship": "父亲"
}
```

### 3.10 获取学生家长列表
**接口地址：** `GET /v2/p/student/parents/:id/`  
**接口说明：** 获取学生家长列表  
**路径参数：**
- `id` (long, 必填): 学生ID

**响应示例：**
```json
{
  "code": 200,
  "parents": [
    {
      "id": 1,
      "name": "张三-父亲",
      "phone": "13800138000",
      "relationship": "父亲",
      "createTime": 1766649180731
    }
  ]
}
```

### 3.11 删除学生家长关系
**接口地址：** `POST /v2/p/student_parent/:id/`  
**接口说明：** 删除学生家长关系  
**路径参数：**
- `id` (long, 必填): 家长关系ID

**请求体：**
```json
{
  "operation": "del"
}
```

### 3.12 批量分配学生到班级
**接口地址：** `POST /v2/p/student_batch_assign/`  
**接口说明：** 批量分配学生到班级  
**请求体：**
```json
{
  "studentIds": [2174, 2175, 2176],
  "classId": 47
}
```

**响应示例：**
```json
{
  "code": 200,
  "message": "成功分配 3 个学生到班级",
  "successCount": 3,
  "totalCount": 3
}
```

### 3.13 当前用户所在班级的学生列表
**接口地址：** `GET /v2/p/student_list_class_currentUser/?classId=47`  
**接口说明：** 获取当前用户的所在班级的学生列表  
**查询参数：**
- `classId` (long, 必填): 班级ID

---

## 4. 班级模块

### 4.1 班级列表
**接口地址：** `POST /v2/p/school_class_list/`  
**接口说明：** 获取班级列表  
**请求体：**
```json
{
  "page": 1,
  "className": "班级名称（可选）"
}
```

**响应示例：**
```json
{
  "code": 200,
  "pages": 10,
  "hasNest": true,
  "list": [
    {
      "orgId": 1,
      "id": 47,
      "className": "六年级七班",
      "grade": 6,
      "headTeacherId": 10,
      "studentNum": 45,
      "academicScore": 0.0,
      "specialtyScore": 0.0,
      "routineScore": 0.0,
      "homeVisitScore": 0.0,
      "totalScore": 0.0,
      "disqualified": false,
      "deductionScore": 0.0,
      "honorTitle": null,
      "createTime": 1766649180731
    }
  ]
}
```

### 4.2 班级详情
**接口地址：** `GET /v2/p/school_class/:id/`  
**接口说明：** 获取班级详情  
**路径参数：**
- `id` (long, 必填): 班级ID

### 4.3 添加班级
**接口地址：** `POST /v2/p/school_class/new/`  
**接口说明：** 添加班级信息  
**请求体：**
```json
{
  "className": "六年级七班",
  "grade": 6,
  "headTeacherId": 10,
  "studentNum": 45
}
```

### 4.4 更新班级
**接口地址：** `POST /v2/p/school_class/:id/`  
**接口说明：** 更新班级信息  
**路径参数：**
- `id` (long, 必填): 班级ID

**请求体：** 同添加班级

### 4.5 删除班级
**接口地址：** `POST /v2/p/school_class/`  
**接口说明：** 删除班级信息  
**请求体：**
```json
{
  "id": 47,
  "operation": "del"
}
```

### 4.6 设置班主任
**接口地址：** `POST /v2/p/school_class/:id/set_head_teacher/`  
**接口说明：** 设置班主任  
**路径参数：**
- `id` (long, 必填): 班级ID

**请求体：**
```json
{
  "teacherId": 10,
  "subject": "语文（可选）"
}
```

### 4.7 添加科任教师
**接口地址：** `POST /v2/p/school_class/:id/add_teacher/`  
**接口说明：** 添加科任教师  
**路径参数：**
- `id` (long, 必填): 班级ID

**请求体：**
```json
{
  "teacherId": 11,
  "subject": "数学",
  "teachingHours": 5,
  "responsibility": "数学教学（可选）"
}
```

### 4.8 移除班级教师
**接口地址：** `POST /v2/p/school_class/:id/remove_teacher/`  
**接口说明：** 移除班级教师  
**路径参数：**
- `id` (long, 必填): 班级ID

**请求体：**
```json
{
  "teacherId": 11
}
```

### 4.9 获取班级教师列表
**接口地址：** `GET /v2/p/school_class/:id/teachers/`  
**接口说明：** 获取班级教师列表  
**路径参数：**
- `id` (long, 必填): 班级ID

**响应示例：**
```json
{
  "code": 200,
  "headTeacher": {
    "relationId": 1,
    "teacherId": 10,
    "teacherName": "张老师",
    "phone": "13800138000",
    "subject": "语文",
    "isHeadTeacher": true,
    "createTime": 1766649180731
  },
  "subjectTeachers": [
    {
      "relationId": 2,
      "teacherId": 11,
      "teacherName": "李老师",
      "phone": "13800138001",
      "subject": "数学",
      "isHeadTeacher": false,
      "createTime": 1766649180731
    }
  ]
}
```

### 4.10 获取班主任信息
**接口地址：** `GET /v2/p/school_class/:id/head_teacher/`  
**接口说明：** 获取班主任信息  
**路径参数：**
- `id` (long, 必填): 班级ID

**响应示例：**
```json
{
  "code": 200,
  "teacherId": 10,
  "teacherName": "张老师",
  "phone": "13800138000",
  "subject": "语文",
  "createTime": 1766649180731
}
```

---

## 5. 学业模块

### 5.1 学业成绩记录列表
**接口地址：** `POST /v2/p/academic_record_list/`  
**接口说明：** 获取学业成绩记录列表  
**请求体：**
```json
{
  "page": 1,
  "studentName": "学生姓名（可选）",
  "className": "班级名称（可选）"
}
```

**响应示例：**
```json
{
  "code": 200,
  "pages": 10,
  "hasNest": true,
  "list": [
    {
      "orgId": 1,
      "id": 1,
      "studentId": 2174,
      "examType": 1,
      "chineseScore": 90.0,
      "mathScore": 95.0,
      "englishScore": 88.0,
      "averageScore": 91.0,
      "gradeRanking": 10,
      "classRanking": 5,
      "progressAmount": 5,
      "progressRanking": 1,
      "calculatedScore": 20.0,
      "badgeAwarded": null,
      "examDate": 1766649180731,
      "createTime": 1766649180731,
      "updateTime": 1766649180731,
      "className": "六年级七班"
    }
  ]
}
```

### 5.2 学业成绩记录详情
**接口地址：** `GET /v2/p/academic_record/:id/`  
**接口说明：** 获取学业成绩记录详情  
**路径参数：**
- `id` (long, 必填): 记录ID

### 5.3 添加学业成绩记录
**接口地址：** `POST /v2/p/academic_record/new/`  
**接口说明：** 添加学业成绩记录  
**请求体：**
```json
{
  "studentId": 2174,
  "examType": 1,
  "chineseScore": 90.0,
  "mathScore": 95.0,
  "englishScore": 88.0,
  "averageScore": 91.0,
  "gradeRanking": 10,
  "classRanking": 5,
  "examDate": 1766649180731
}
```

### 5.4 更新学业成绩记录
**接口地址：** `POST /v2/p/academic_record/:id/`  
**接口说明：** 更新学业成绩记录  
**路径参数：**
- `id` (long, 必填): 记录ID

### 5.5 删除学业成绩记录
**接口地址：** `POST /v2/p/academic_record/`  
**接口说明：** 删除学业成绩记录  
**请求体：**
```json
{
  "id": 1,
  "operation": "del"
}
```

### 5.6 导入学业成绩
**接口地址：** `POST /v2/p/academic_record_excel/`  
**接口说明：** 导入学业成绩Excel文件  
**请求头：** `Content-Type: multipart/form-data`  
**请求参数：**
- `file` (file, 必填): Excel文件

### 5.7 导出学业成绩模板
**接口地址：** `GET /v2/p/academic_record_excel_template/`  
**接口说明：** 导出学业成绩导入模板  
**响应：** Excel文件下载

---

## 6. 特长获奖模块

### 6.1 特长获奖列表
**接口地址：** `GET /v2/p/specialty_award_list/?page=1&filter=&status=0`  
**接口说明：** 获取特长获奖列表  
**查询参数：**
- `page` (int, 可选): 页码，默认1
- `filter` (String, 可选): 搜索关键词
- `status` (int, 可选): 状态筛选，默认0

### 6.2 特长获奖详情
**接口地址：** `GET /v2/p/specialty_award/:id/`  
**接口说明：** 获取特长获奖详情  
**路径参数：**
- `id` (long, 必填): 记录ID

### 6.3 添加特长获奖
**接口地址：** `POST /v2/p/specialty_award/new/`  
**接口说明：** 添加特长获奖记录

### 6.4 更新特长获奖
**接口地址：** `POST /v2/p/specialty_award/:id/`  
**接口说明：** 更新特长获奖记录  
**路径参数：**
- `id` (long, 必填): 记录ID

### 6.5 删除特长获奖
**接口地址：** `POST /v2/p/specialty_award/`  
**接口说明：** 删除特长获奖记录  
**请求体：**
```json
{
  "id": 1,
  "operation": "del"
}
```

### 6.6 审核特长获奖
**接口地址：** `POST /v2/p/specialty_award_judge/`  
**接口说明：** 审核特长获奖记录

### 6.7 获取当前用户的特长获奖
**接口地址：** `GET /v2/p/specialty_award/current_user/:id/`  
**接口说明：** 获取当前用户的特长获奖详情  
**路径参数：**
- `id` (long, 必填): 记录ID

---

## 7. 徽章模块

### 7.1 徽章配置列表
**接口地址：** `POST /v2/p/badge_list/`  
**接口说明：** 获取徽章配置列表  
**请求体：**
```json
{
  "page": 1,
  "active": 1,
  "isParent": 0
}
```

**响应示例：**
```json
{
  "code": 200,
  "pages": 5,
  "hasNest": false,
  "list": [
    {
      "orgId": 1,
      "id": 1,
      "badgeId": 1,
      "badgeName": "学习之星",
      "description": "学习成绩优秀",
      "active": true,
      "createTime": 1766649180731,
      "isParent": 0
    }
  ]
}
```

### 7.2 徽章配置详情
**接口地址：** `GET /v2/p/badge/:id/`  
**接口说明：** 获取徽章配置详情  
**路径参数：**
- `id` (long, 必填): 徽章ID

### 7.3 添加徽章配置
**接口地址：** `POST /v2/p/badge/new/`  
**接口说明：** 添加徽章配置  
**请求体：**
```json
{
  "badgeId": 1,
  "badgeName": "学习之星",
  "description": "学习成绩优秀",
  "active": true,
  "isParent": 0
}
```

### 7.4 更新徽章配置
**接口地址：** `POST /v2/p/badge/:id/`  
**接口说明：** 更新徽章配置  
**路径参数：**
- `id` (long, 必填): 徽章ID

### 7.5 删除徽章配置
**接口地址：** `POST /v2/p/badge/`  
**接口说明：** 删除徽章配置  
**请求体：**
```json
{
  "id": 1,
  "operation": "del"
}
```

### 7.6 徽章授予记录列表
**接口地址：** `GET /v2/p/badge_record_list/?page=1&filter=&status=0`  
**接口说明：** 获取徽章授予记录列表  
**查询参数：**
- `page` (int, 可选): 页码，默认1
- `filter` (String, 可选): 搜索关键词
- `status` (int, 可选): 状态筛选，默认0

### 7.7 徽章授予记录详情
**接口地址：** `GET /v2/p/badge_record/:id/`  
**接口说明：** 获取徽章授予记录详情  
**路径参数：**
- `id` (long, 必填): 记录ID

### 7.8 添加徽章授予记录
**接口地址：** `POST /v2/p/badge_record/new/`  
**接口说明：** 添加徽章授予记录

### 7.9 更新徽章授予记录
**接口地址：** `POST /v2/p/badge_record/:id/`  
**接口说明：** 更新徽章授予记录  
**路径参数：**
- `id` (long, 必填): 记录ID

### 7.10 删除徽章授予记录
**接口地址：** `POST /v2/p/badge_record/`  
**接口说明：** 删除徽章授予记录  
**请求体：**
```json
{
  "id": 1,
  "operation": "del"
}
```

---

## 8. 习惯评价模块

### 8.1 习惯记录列表
**接口地址：** `GET /v2/p/habit_record_list/?page=1&filter=&status=0`  
**接口说明：** 获取习惯记录列表  
**查询参数：**
- `page` (int, 可选): 页码，默认1
- `filter` (String, 可选): 搜索关键词
- `status` (int, 可选): 状态筛选，默认0

### 8.2 习惯记录详情
**接口地址：** `GET /v2/p/habit_record/:id/`  
**接口说明：** 获取习惯记录详情  
**路径参数：**
- `id` (long, 必填): 记录ID

### 8.3 添加习惯记录
**接口地址：** `POST /v2/p/habit_record/new/`  
**接口说明：** 添加习惯记录

### 8.4 更新习惯记录
**接口地址：** `POST /v2/p/habit_record/:id/`  
**接口说明：** 更新习惯记录  
**路径参数：**
- `id` (long, 必填): 记录ID

### 8.5 删除习惯记录
**接口地址：** `POST /v2/p/habit_record/`  
**接口说明：** 删除习惯记录  
**请求体：**
```json
{
  "id": 1,
  "operation": "del"
}
```

### 8.6 获取当前学生的习惯记录
**接口地址：** `GET /v2/p/habit_record/current_student/:id/`  
**接口说明：** 获取当前学生的习惯记录  
**路径参数：**
- `id` (long, 必填): 学生ID

### 8.7 批量添加习惯记录
**接口地址：** `POST /v2/p/habit_record/group/new/`  
**接口说明：** 批量添加习惯记录（按组）

### 8.8 当前用户的习惯记录列表
**接口地址：** `GET /v2/p/habit_record_list_currentUser/?page=1&filter=&status=0`  
**接口说明：** 获取当前用户的习惯记录列表

### 8.9 习惯记录列表（新）
**接口地址：** `GET /v2/p/habit_record_list_new/?page=1&filter=&status=0`  
**接口说明：** 获取习惯记录列表（新版本）

---

## 9. 家访工作记录模块

### 9.1 家访记录列表
**接口地址：** `GET /v2/p/home_visit_list/?page=1&filter=&status=0`  
**接口说明：** 获取家访记录列表  
**查询参数：**
- `page` (int, 可选): 页码，默认1
- `filter` (String, 可选): 搜索关键词
- `status` (int, 可选): 状态筛选，默认0

### 9.2 家访记录详情
**接口地址：** `GET /v2/p/home_visit/:id/`  
**接口说明：** 获取家访记录详情  
**路径参数：**
- `id` (long, 必填): 记录ID

### 9.3 添加家访记录
**接口地址：** `POST /v2/p/home_visit/new/`  
**接口说明：** 添加家访记录

### 9.4 更新家访记录
**接口地址：** `POST /v2/p/home_visit/:id/`  
**接口说明：** 更新家访记录  
**路径参数：**
- `id` (long, 必填): 记录ID

### 9.5 删除家访记录
**接口地址：** `POST /v2/p/home_visit/`  
**接口说明：** 删除家访记录  
**请求体：**
```json
{
  "id": 1,
  "operation": "del"
}
```

### 9.6 审核家访记录
**接口地址：** `POST /v2/p/home_visit/:id/review/`  
**接口说明：** 审核家访记录  
**路径参数：**
- `id` (long, 必填): 记录ID

---

## 10. 学生分组模块

### 10.1 班级分组列表
**接口地址：** `POST /v2/p/class_group_list/`  
**接口说明：** 获取班级分组列表  
**请求体：**
```json
{
  "classId": 47,
  "page": 1
}
```

### 10.2 班级分组详情
**接口地址：** `GET /v2/p/class_group/:id/`  
**接口说明：** 获取班级分组详情  
**路径参数：**
- `id` (long, 必填): 分组ID

### 10.3 添加班级分组
**接口地址：** `POST /v2/p/class_group/new/`  
**接口说明：** 添加班级分组  
**请求体：**
```json
{
  "classId": 47,
  "groupName": "第一组"
}
```

### 10.4 更新班级分组
**接口地址：** `POST /v2/p/class_group/:id/`  
**接口说明：** 更新班级分组  
**路径参数：**
- `id` (long, 必填): 分组ID

### 10.5 删除班级分组
**接口地址：** `POST /v2/p/class_group/`  
**接口说明：** 删除班级分组  
**请求体：**
```json
{
  "id": 1,
  "operation": "del"
}
```

### 10.6 获取分组学生列表
**接口地址：** `GET /v2/p/class_group/students/:id/`  
**接口说明：** 获取分组学生列表  
**路径参数：**
- `id` (long, 必填): 分组ID

### 10.7 管理分组学生
**接口地址：** `POST /v2/p/class_group/manage_students/:id/`  
**接口说明：** 管理分组学生（添加/移除）  
**路径参数：**
- `id` (long, 必填): 分组ID

**请求体：**
```json
{
  "studentIds": [2174, 2175, 2176],
  "operation": "add"  // add-添加, remove-移除
}
```

### 10.8 获取未分组学生列表
**接口地址：** `GET /v2/p/class_group/ungrouped_students/:classId/`  
**接口说明：** 获取未分组学生列表  
**路径参数：**
- `classId` (long, 必填): 班级ID

---

## 11. 评价规则模块

### 11.1 评价规则列表
**接口地址：** `GET /v2/p/evaluation_rule_list/?page=1&filter=&status=0`  
**接口说明：** 获取评价规则列表  
**查询参数：**
- `page` (int, 可选): 页码，默认1
- `filter` (String, 可选): 搜索关键词
- `status` (int, 可选): 状态筛选，默认0

### 11.2 评价规则详情
**接口地址：** `GET /v2/p/evaluation_rule/:id/`  
**接口说明：** 获取评价规则详情  
**路径参数：**
- `id` (long, 必填): 规则ID

### 11.3 添加评价规则
**接口地址：** `POST /v2/p/evaluation_rule/new/`  
**接口说明：** 添加评价规则

### 11.4 更新评价规则
**接口地址：** `POST /v2/p/evaluation_rule/:id/`  
**接口说明：** 更新评价规则  
**路径参数：**
- `id` (long, 必填): 规则ID

### 11.5 删除评价规则
**接口地址：** `POST /v2/p/evaluation_rule/`  
**接口说明：** 删除评价规则  
**请求体：**
```json
{
  "id": 1,
  "operation": "del"
}
```

---

## 12. 课程表模块

### 12.1 课程表列表
**接口地址：** `POST /v2/p/class_routine_list/`  
**接口说明：** 获取课程表列表

### 12.2 课程表详情
**接口地址：** `GET /v2/p/class_routine/:id/`  
**接口说明：** 获取课程表详情  
**路径参数：**
- `id` (long, 必填): 课程表ID

### 12.3 添加课程表
**接口地址：** `POST /v2/p/class_routine/new/`  
**接口说明：** 添加课程表

### 12.4 更新课程表
**接口地址：** `POST /v2/p/class_routine/:id/`  
**接口说明：** 更新课程表  
**路径参数：**
- `id` (long, 必填): 课程表ID

### 12.5 删除课程表
**接口地址：** `POST /v2/p/class_routine/`  
**接口说明：** 删除课程表  
**请求体：**
```json
{
  "id": 1,
  "operation": "del"
}
```

---

## 13. 班级教师关系模块

### 13.1 班主任班级列表
**接口地址：** `GET /v2/p/header_teacher_class/`  
**接口说明：** 获取班主任的班级列表

### 13.2 教师班级列表
**接口地址：** `GET /v2/p/teacher_class/`  
**接口说明：** 获取教师的班级列表

### 13.3 添加班级教师关系
**接口地址：** `POST /v2/p/class_teacher_relation/new/`  
**接口说明：** 添加班级教师关系

### 13.4 班级教师关系详情
**接口地址：** `GET /v2/p/class_teacher_relation/:id/`  
**接口说明：** 获取班级教师关系详情  
**路径参数：**
- `id` (long, 必填): 关系ID

### 13.5 更新班级教师关系
**接口地址：** `POST /v2/p/class_teacher_relation/:id/`  
**接口说明：** 更新班级教师关系  
**路径参数：**
- `id` (long, 必填): 关系ID

### 13.6 删除班级教师关系
**接口地址：** `POST /v2/p/class_teacher_relation/`  
**接口说明：** 删除班级教师关系  
**请求体：**
```json
{
  "id": 1,
  "operation": "del"
}
```

### 13.7 班级教师关系列表
**接口地址：** `POST /v2/p/class_teacher_relation_list/`  
**接口说明：** 获取班级教师关系列表

### 13.8 班级教师关系列表（新）
**接口地址：** `POST /v2/p/class_teacher_relation_list/new/`  
**接口说明：** 获取班级教师关系列表（新版本）

---

## 14. 学生家长关系模块

### 14.1 学生家长关系列表
**接口地址：** `GET /v2/p/parent_student_relation_list/?page=1&filter=&status=0`  
**接口说明：** 获取学生家长关系列表  
**查询参数：**
- `page` (int, 可选): 页码，默认1
- `filter` (String, 可选): 搜索关键词
- `status` (int, 可选): 状态筛选，默认0

### 14.2 添加学生家长关系
**接口地址：** `POST /v2/p/parent_student_relation/new/`  
**接口说明：** 添加学生家长关系

### 14.3 学生家长关系详情
**接口地址：** `GET /v2/p/parent_student_relation/:id/`  
**接口说明：** 获取学生家长关系详情  
**路径参数：**
- `id` (long, 必填): 关系ID

### 14.4 更新学生家长关系
**接口地址：** `POST /v2/p/parent_student_relation/:id/`  
**接口说明：** 更新学生家长关系  
**路径参数：**
- `id` (long, 必填): 关系ID

### 14.5 删除学生家长关系
**接口地址：** `POST /v2/p/parent_student_relation/`  
**接口说明：** 删除学生家长关系  
**请求体：**
```json
{
  "id": 1,
  "operation": "del"
}
```

### 14.6 家长学生列表
**接口地址：** `POST /v2/p/parent_student/`  
**接口说明：** 获取家长学生列表

### 14.7 家长学生习惯记录列表
**接口地址：** `POST /v2/p/parent_student/habit_record/`  
**接口说明：** 获取家长学生的习惯记录列表

### 14.8 家长学生获奖记录列表
**接口地址：** `POST /v2/p/parent_student/award_record/`  
**接口说明：** 获取家长学生的获奖记录列表

---

## 15. 管理员模块

### 15.1 管理员列表
**接口地址：** `POST /v2/p/admin_members/`  
**接口说明：** 获取管理员列表

### 15.2 管理员详情
**接口地址：** `GET /v2/p/admin_members/:memberId/`  
**接口说明：** 获取管理员详情  
**路径参数：**
- `memberId` (long, 必填): 管理员ID

### 15.3 添加管理员
**接口地址：** `POST /v2/p/admin_members/new/`  
**接口说明：** 添加管理员

### 15.4 更新管理员
**接口地址：** `POST /v2/p/admin_member/:id/`  
**接口说明：** 更新管理员信息  
**路径参数：**
- `id` (long, 必填): 管理员ID

### 15.5 删除管理员
**接口地址：** `POST /v2/p/admin_member/`  
**接口说明：** 删除管理员  
**请求体：**
```json
{
  "id": 1,
  "operation": "del"
}
```

### 15.6 设置管理员状态
**接口地址：** `POST /v2/p/admin_members/status/`  
**接口说明：** 设置管理员状态  
**请求体：**
```json
{
  "id": 1,
  "status": 1  // 1-启用, 0-禁用
}
```

### 15.7 绑定管理员到组
**接口地址：** `POST /v2/p/bind_member_to_group/`  
**接口说明：** 绑定管理员到组

### 15.8 用户组列表
**接口地址：** `GET /v2/p/user_groups/?memberId=0`  
**接口说明：** 获取用户组列表  
**查询参数：**
- `memberId` (long, 可选): 成员ID，默认0

### 15.9 教师导入
**接口地址：** `POST /v2/p/teacher_import/`  
**接口说明：** 导入教师  
**请求头：** `Content-Type: multipart/form-data`  
**请求参数：**
- `file` (file, 必填): Excel文件

### 15.10 教师成员列表
**接口地址：** `GET /v2/p/teacher_members/`  
**接口说明：** 获取教师成员列表

---

## 16. 权限管理模块

### 16.1 组列表
**接口地址：** `GET /v2/p/groups/`  
**接口说明：** 获取组列表

### 16.2 添加组
**接口地址：** `POST /v2/p/group/new/`  
**接口说明：** 添加组

### 16.3 删除组
**接口地址：** `POST /v2/p/group/`  
**接口说明：** 删除组  
**请求体：**
```json
{
  "id": 1,
  "operation": "del"
}
```

### 16.4 更新组
**接口地址：** `POST /v2/p/group/:id/`  
**接口说明：** 更新组信息  
**路径参数：**
- `id` (int, 必填): 组ID

### 16.5 组详情
**接口地址：** `GET /v2/p/groups/:groupId/`  
**接口说明：** 获取组详情  
**路径参数：**
- `groupId` (int, 必填): 组ID

### 16.6 操作列表
**接口地址：** `GET /v2/p/actions/`  
**接口说明：** 获取操作列表

### 16.7 添加操作
**接口地址：** `POST /v2/p/action/new/`  
**接口说明：** 添加操作

### 16.8 更新操作
**接口地址：** `POST /v2/p/action/:id/`  
**接口说明：** 更新操作  
**路径参数：**
- `id` (String, 必填): 操作ID

### 16.9 删除操作
**接口地址：** `POST /v2/p/action/`  
**接口说明：** 删除操作  
**请求体：**
```json
{
  "id": "action_id",
  "operation": "del"
}
```

### 16.10 操作详情
**接口地址：** `GET /v2/p/action/:actionId/`  
**接口说明：** 获取操作详情  
**路径参数：**
- `actionId` (String, 必填): 操作ID

### 16.11 按组获取操作
**接口地址：** `GET /v2/p/actions_by_filter/:groupId/`  
**接口说明：** 按组获取操作列表  
**路径参数：**
- `groupId` (int, 必填): 组ID

### 16.12 更新组操作
**接口地址：** `POST /v2/p/group_action/`  
**接口说明：** 更新组操作

### 16.13 添加组用户
**接口地址：** `POST /v2/p/group_user/new/`  
**接口说明：** 添加组用户

### 16.14 删除组用户
**接口地址：** `POST /v2/p/group_user/`  
**接口说明：** 删除组用户  
**请求体：**
```json
{
  "id": 1,
  "operation": "del"
}
```

### 16.15 按组获取用户列表
**接口地址：** `GET /v2/p/group_user/:groupId/`  
**接口说明：** 按组获取用户列表  
**路径参数：**
- `groupId` (int, 必填): 组ID

---

## 17. 菜单管理模块

### 17.1 菜单列表
**接口地址：** `GET /v2/p/menu/?name=&parentId=0`  
**接口说明：** 获取菜单列表  
**查询参数：**
- `name` (String, 可选): 菜单名称，默认空
- `parentId` (int, 可选): 父菜单ID，默认0

### 17.2 菜单详情
**接口地址：** `GET /v2/p/menu/:id/`  
**接口说明：** 获取菜单详情  
**路径参数：**
- `id` (int, 必填): 菜单ID

### 17.3 添加菜单
**接口地址：** `POST /v2/p/menu/new/`  
**接口说明：** 添加菜单

### 17.4 更新菜单
**接口地址：** `POST /v2/p/menu/:id/`  
**接口说明：** 更新菜单  
**路径参数：**
- `id` (int, 必填): 菜单ID

### 17.5 删除菜单
**接口地址：** `POST /v2/p/menu/`  
**接口说明：** 删除菜单  
**请求体：**
```json
{
  "id": 1,
  "operation": "del"
}
```

### 17.6 批量更新组菜单
**接口地址：** `POST /v2/p/batch_update_menu_to_group/:groupId/`  
**接口说明：** 批量更新组菜单  
**路径参数：**
- `groupId` (int, 必填): 组ID

### 17.7 组菜单列表
**接口地址：** `GET /v2/p/group_menu/?groupId=1`  
**接口说明：** 获取组菜单列表  
**查询参数：**
- `groupId` (int, 必填): 组ID

### 17.8 成员菜单
**接口地址：** `GET /v2/p/member_menu/`  
**接口说明：** 获取成员菜单

---

## 18. 系统配置模块

### 18.1 参数配置列表
**接口地址：** `GET /v2/p/param_config/?key=`  
**接口说明：** 获取参数配置列表  
**查询参数：**
- `key` (String, 可选): 配置键，默认空

### 18.2 参数配置详情
**接口地址：** `GET /v2/p/param_config/:id/`  
**接口说明：** 获取参数配置详情  
**路径参数：**
- `id` (long, 必填): 配置ID

### 18.3 添加参数配置
**接口地址：** `POST /v2/p/param_config/new/`  
**接口说明：** 添加参数配置

### 18.4 更新参数配置
**接口地址：** `POST /v2/p/param_config/:id/`  
**接口说明：** 更新参数配置  
**路径参数：**
- `id` (long, 必填): 配置ID

### 18.5 操作日志列表
**接口地址：** `GET /v2/p/operation_logs/?page=1&adminName=&adminId=0`  
**接口说明：** 获取操作日志列表  
**查询参数：**
- `page` (int, 可选): 页码，默认1
- `adminName` (String, 可选): 管理员名称，默认空
- `adminId` (long, 可选): 管理员ID，默认0

### 18.6 班级配置列表
**接口地址：** `GET /v2/p/class_config/?page=1&filter=&status=0`  
**接口说明：** 获取班级配置列表  
**查询参数：**
- `page` (int, 可选): 页码，默认1
- `filter` (String, 可选): 搜索关键词
- `status` (int, 可选): 状态筛选，默认0

---

## 19. 成员模块

### 19.1 成员列表
**接口地址：** `POST /v2/p/members/`  
**接口说明：** 获取成员列表

### 19.2 成员详情
**接口地址：** `GET /v2/p/members/:uid/`  
**接口说明：** 获取成员详情  
**路径参数：**
- `uid` (long, 必填): 成员ID

### 19.3 设置成员状态
**接口地址：** `POST /v2/p/members/status/`  
**接口说明：** 设置成员状态  
**请求体：**
```json
{
  "id": 1,
  "status": 1
}
```

### 19.4 更新成员
**接口地址：** `POST /v2/p/members/:uid/`  
**接口说明：** 更新成员信息  
**路径参数：**
- `uid` (long, 必填): 成员ID

### 19.5 客户列表
**接口地址：** `POST /v2/p/dealer_customers/`  
**接口说明：** 获取客户列表

---

## 20. 测试接口

### 20.1 测试接口
**接口地址：** `GET /v2/p/test/`  
**接口说明：** 测试接口

---

## 注意事项

1. **认证Token**: 除登录相关接口外，所有接口都需要在请求头中携带 `token` 进行认证
2. **分页参数**: 列表接口中，`page=0` 表示查询全部数据，`page>=1` 表示分页查询
3. **文件上传**: 文件上传接口需要使用 `multipart/form-data` 格式
4. **时间格式**: 所有时间字段均为时间戳（毫秒）
5. **数据权限**: 系统会根据登录用户的 `orgId` 自动过滤数据，确保数据隔离
6. **错误处理**: 所有错误响应都包含 `code` 和 `reason` 字段，前端应根据 `code` 进行相应处理

---

## 更新日志

- 2024-01-XX: 初始版本API文档

