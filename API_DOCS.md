# API Documentation

## Admin-Action

### 01权限详情

`GET` **/v2/s/action/:actionId/**

**Name:** `getAction`

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| int | code | 200 请求成功 |
| string | id | actionId |
| string | actionName | 权限名称，对应系统内部一个控制器 |
| string | actionDesc | 权限描述，中文说明 |
| string | moduleName | 权限名称，英文简写 |
| string | moduleDesc | 模块名称，中文说明 |
| boolean | needShow | 没有该模块的权限，模块是否显示，默认为false不显示 |
| int | sortValue | 排序值 |
| long | createdTime | 创建时间 |
| int | code | 40001 参数错误 |
| int | code | 40002 该权限不存在 |

---

### 02权限列表

`GET` **/v2/s/actions/**

**Name:** `listActions`

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| int | code | 200 请求成功 |
| - | - | {json} list |
| string | id | actionId |
| string | actionName | 权限名称，对应系统内部一个控制器 |
| string | actionDesc | 权限描述，中文说明 |
| string | moduleName | 权限名称，英文简写 |
| string | moduleDesc | 模块名称，中文说明 |
| boolean | needShow | 没有该模块的权限，模块是否显示，默认为false不显示 |
| int | sortValue | 排序值 |
| long | createdTime | 创建时间 |

---

### 03新建权限

`POST` **/v2/s/action/new/**

**Name:** `addAction`

#### Parameters

| Type | Name | Description |
| --- | --- | --- |
| string | actionName | 权限名称，对应系统内部一个控制器 |
| string | actionDesc | 权限描述，中文说明 |
| string | moduleName | 权限名称，英文简写 |
| string | moduleDesc | 模块名称，中文说明 |
| boolean | needShow | 没有该模块的权限，模块是否显示，默认为false不显示 |
| int | sortValue | 排序值 |

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| int | code | 200 请求成功 |
| int | code | 40001 参数错误 |
| int | code | 40002 该权限已存在 |

---

### 04修改权限

`POST` **/v2/s/action/:id/**

**Name:** `updateAction`

#### Parameters

| Type | Name | Description |
| --- | --- | --- |
| string | actionName | 权限名称，对应系统内部一个控制器 |
| string | actionDesc | 权限描述，中文说明 |
| string | moduleName | 权限名称，英文简写 |
| string | moduleDesc | 模块名称，中文说明 |
| boolean | needShow | 没有该模块的权限，模块是否显示，该值必须传，如果不传会导致使用默认的false写入库里 |
| int | sortValue | 排序值 |

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| int | code | 200 请求成功 |
| int | code | 40001 参数错误 |
| int | code | 40002 分组不存在 |

---

### 05删除权限

`POST` **/v2/s/action/**

**Name:** `delAction`

#### Parameters

| Type | Name | Description |
| --- | --- | --- |
| String | id | 权限id |
| String | operation | 操作,"del"为删除 |

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| int | code | 200 请求成功 |
| int | code | 40001 参数错误 |
| int | code | 40002 分组不存在 |

---

### 06根据groupId取出权限

`GET` **/v2/s/actions_by_filter/:groupId/**

**Name:** `getGroupActionByGroupId`

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| int | code | 200 请求成功 |
| string | id | actionId |
| string | actionName | 权限名称，对应系统内部一个控制器 |
| string | actionDesc | 权限描述，中文说明 |
| string | moduleName | 权限名称，英文简写 |
| string | moduleDesc | 模块名称，中文说明 |
| boolean | needShow | 没有该模块的权限，模块是否显示，默认为false不显示 |
| int | sortValue | 排序值 |
| long | createdTime | 创建时间 |
| int | code | 40001 参数错误 |

---

## 文件上传模块

### 上传图片或视频（通用，服务器）

`POST` **/v2/p/file/upload_file/**

**Name:** `uploadImage`

#### Parameters

| Type | Name | Description |
| --- | --- | --- |
| file | file | 图片或视频文件（支持格式：jpg, jpeg, png, gif, bmp, webp, mp4, avi, mov, wmv, flv, mkv, webm等） |
| String | style | 文件模块类型  徽章：badge 奖项：award  习惯评价：habit |

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| int | code | 200 |
| String | url | 文件访问路径 |
| int | code | 40001 参数错误 |
| int | code | 40003 上传失败 |

---

## Admin-GROUP

### 06修改组的权限

`POST` **/v2/s/group_action/**

**Name:** `updateGroupAction`

#### Parameters

| Type | Name | Description |
| --- | --- | --- |
| int | groupId | 组id |
| string | actionId | 权限id，多个权限以半角逗号(,)进行分隔 |

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| int | code | 40001 参数错误 |
| int | code | 40002 该组已拥有该权限 |
| int | code | 200 请求成功 |

---

### 01权限组详情

`GET` **/v2/s/groups/:groupId/**

**Name:** `getGroup`

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| string | groupName | 组名 |
| string | remark | 组－备注 |
| long | createdTime | 创建时间 |
| int | code | 200 请求成功 |
| int | code | 40001 参数错误 |
| int | code | 40002 该组不存在 |

---

### 02分组列表

`GET` **/v2/s/groups/**

**Name:** `listGroups`

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| - | - | {json} list |
| string | groupName | 组名 |
| string | groupRemark | 组－备注 |
| - | - | {boolean} isAdmin |
| long | createdTimeForShow | 创建时间 |
| int | code | 200 请求成功 |

---

### 03添加分组

`POST` **/v2/s/group/new/**

**Name:** `addGroup`

#### Parameters

| Type | Name | Description |
| --- | --- | --- |
| string | groupName | 组名 |
| string | description | 组－备注 |
| - | - | {boolean} isAdmin |

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| int | code | 40001 参数错误 |
| int | code | 40002 该组已存在 |
| int | code | 200 请求成功 |

---

### 04修改分组

`POST` **/v2/s/group/:id/**

**Name:** `updateGroup`

#### Parameters

| Type | Name | Description |
| --- | --- | --- |
| string | groupName | 组名 |
| string | description | 组－备注 |
| - | - | {boolean} isAdmin |

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| int | code | 40001 参数错误 |
| int | code | 40002 分组不存在 |
| int | code | 200 请求成功 |

---

### 05删除分组

`POST` **/v2/s/group/**

**Name:** `delGroup`

#### Parameters

| Type | Name | Description |
| --- | --- | --- |
| int | id | 组的id |
| String | operation | 操作,"del"为删除 |

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| int | code | 40001 参数错误 |
| int | code | 40002 分组不存在 |
| int | code | 200 请求成功 |

---

## Admin-GROUP-USER

### 01成员加入组

`POST` **/v2/s/group_user/new/**

**Name:** `addGroupUser`

#### Parameters

| Type | Name | Description |
| --- | --- | --- |
| int | groupId | 组id |
| long | memberId | 成员id |

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| int | code | 40001 参数错误 |
| int | code | 40002 该成员已属于该组 |
| int | code | 200 请求成功 |

---

### 02成员移出组

`POST` **/v2/s/group_user/**

**Name:** `delGroupUser`

#### Parameters

| Type | Name | Description |
| --- | --- | --- |
| int | groupId | 组id |
| int | memberId | 成员id |
| String | operation | 操作,"del"为删除 |

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| int | code | 40001 参数错误 |
| int | code | 40002 分组不存在 |
| int | code | 200 请求成功 |

---

### 03获取指定组的成员列表

`GET` **/v2/s/group_user/:groupId/**

**Name:** `listUsersByGroupId`

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| - | - | {jsonArray} list |
| int | groupId | 组的id |
| string | groupName | 组名 |
| int | memberId | 组员的id |
| string | realName | 组员的名字 |
| int | code | 200 请求成功 |

---

## 后台登录

### 01 后台用户登录（学校端）

`POST` **/v2/p/login/noauth/**

**Name:** `login`

#### Parameters

| Type | Name | Description |
| --- | --- | --- |
| string | username | 用户名（就是手机号码） |
| string | password | 密码, 6位至20位 |

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| String | userName | 用户名 |
| String | realName | 真名 |
| String | lastLoginTimeForShow | 最后登录时间 |
| String | lastLoginIP | 最后登录ip |
| long | id | 用户id |
| String | token | token |
| String | groupName | 所在组名 |
| int | code | 40001  参数错误 |
| int | code | 40003 用户名或密码错误 |

#### Request Example

```json
{json} 请求示例:
{
username: '',
password: '',
vcode: '',
}
```

#### Response Example

```json
{json} 响应示例:
{
"id": 2,
"userName": "13625063671",
"realName": "13625063671",
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
"pinyinAbbr": null,
"status": 1,
"bgImgUrl": null,
"groupIdList": [],
"groupUserList": [],
"groupName": null,
"admin": false,
"code": 200,
"token": "4c854b3a-e0fd-4d89-a89e-02624086d364"
}

```

---

## Admin-Authority

### 04是否已登录

`GET` **/v2/s/is_login/**

**Name:** `isLogin`

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| int | code | 200 |
| boolean | login | true已登录 false未登录 |
| int | code | 40001 参数错误 |

---

### 04商户入驻注册

`POST` **/v2/s/user/new/**

**Name:** `signUp`

#### Parameters

| Type | Name | Description |
| --- | --- | --- |
| string | phoneNumber | 手机号 |
| string | vCode | 短信验证码，预留 |
| String | password | 登录密码 6-20位，不允许包含非法字符 |

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| int | code | 200成功创建 |
| int | code | 40001 参数错误 |
| int | code | 40002 帐号已被注册 |
| int | code | 40003 无效的短信验证码 |
| int | code | 40004 登录密码无效 |
| int | code | 40006 无效的手机号码 |

---

### 08查看自己详情信息

`GET` **/v2/p/admin_member/info/**

**Name:** `getAdminMemberInfo`

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| int | code | 200 请求成功 |
| int | id | 用户id |
| string | name | 用户名 |
| string | avatar | avatar |
| int | groupId | 所在分组id |
| long | shopId | 店铺ID |
| String | avatar | 头像 |
| int | code | 40001 参数错误 |
| int | code | 40002 该管理员不存在 |

---

### 08获取店铺资料

`GET` **/v2/s/shop_info/**

**Name:** `getAdminMemberInfo`

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| int | code | 200 请求成功 |
| int | code | 40002 该管理员不存在 |

---

### 10重置登录密码

`POST` **/v2/s/reset_login_password/**

**Name:** `resetLoginPassword`

#### Parameters

| Type | Name | Description |
| --- | --- | --- |
| string | accountName | 帐号 |
| string | vcode | 短信验证码 |
| string | newPassword | 新密码 |

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| - | - | {int}code 200 |
| - | - | {int}code 40001无效的参数 |
| - | - | {int}code 40002 无效的短信验证码 |
| - | - | {int}code 40003 无效的密码 |
| - | - | {int}code 40004无效的手机号码 |
| - | - | {int}code 40005 该帐号不存在 |

---

### 03注销

`POST` **/v2/s/logout/**

**Name:** `logout`

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| int | code | 200 注销成功. |
| int | code | 40003 未提供token |

---

## User

### 11设置/修改登录密码

`POST` **/v2/s/set_login_password/**

**Name:** `setLoginPassword`

#### Parameters

| Type | Name | Description |
| --- | --- | --- |
| string | [oldPassword] | 旧密码 |
| string | password | 新密码 |
| string | [vcode] | 短信验证码 |
| long | uid | 用戶Id |

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| - | - | {int}code 200 |
| - | - | {int}code 40001 无效的参数 |
| - | - | {int}code 40004 该帐号不存在 |

---

## 手机端登录模块

### 前台app登录模块

`POST` **/V2/p/front/login/noauth/**

**Name:** `login`

#### Parameters

| Type | Name | Description |
| --- | --- | --- |
| jsonObject | data | json串格式 |
| string | username | 用户名 |
| string | password | 密码, 6位至20位 |
| string | loginRules | 登录规则 0-家长登录  1-非家长登录 |
| string | vcode | 手机验证码 |

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| String | userName | 用户名 |
| String | realName | 真名 |
| String | lastLoginTimeForShow | 最后登录时间 |
| String | lastLoginIP | 最后登录ip |
| long | id | 用户id |
| String | token | token |
| String | groupName | 所在组名 |
| int | code | 40001  参数错误 |
| int | code | 40003 用户名或密码错误 |

#### Request Example

```json
{json} 请求参数：
{
"username": "",
"password": "",
"loginRules": 0  // 0-家长登录  1-非家长登录
}

```

#### Response Example

```json
{json} 响应示例:
{
"id": 2,
"userName": "13625063671",//账号（手机号码）
"realName": "李**",//真实姓名
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
"rules": "科任教师",
"pinyinAbbr": null,
"status": 1,
"bgImgUrl": null,
"groupIdList": [],
"groupUserList": [],
"groupName": null,
"admin": false,
"code": 200,
"token": "4c854b3a-e0fd-4d89-a89e-02624086d364"
}

```

---

## ADMIN-MENU

### 01菜单列表

`GET` **/v2/s/menu/?name=&parentId=**

**Name:** `listMenu`

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| int | code | 200 请求成功 |
| JsonArray | list | 菜单列表 |
| long | id | id |
| int | sort | 排序 降序 |
| boolean | enable | 是否启用 |
| String | path | 前端路径 |
| String | name | 名称 |
| String | component | 组件 |
| String | redirect | 重定向地址 |
| String | title | 标题 |
| String | icon | 图标 |
| String | activeMenu | 菜单路径，前端用 |
| boolean | noCache | 是否缓存 true缓存，false不缓存 |
| String | relativePath | 菜单上下级，用于菜单之间的关系 |
| long | parentId | 父级菜单ID |
| long | createTime | 创建时间 |

---

### 02菜单详情

`GET` **/v2/s/menu/:id/**

**Name:** `getMenu`

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| long | id | id |
| int | sort | 排序 降序 |
| boolean | enable | 是否启用 |
| String | path | 前端路径 |
| String | name | 分类名称 |
| String | component | 组件 |
| String | redirect | 重定向地址 |
| String | title | 标题 |
| String | icon | 图标 |
| String | activeMenu | 菜单路径，前端用 |
| boolean | noCache | 是否缓存 true缓存，false不缓存 |
| String | relativePath | 菜单上下级，用于菜单之间的关系 |
| long | createTime | 创建时间 |

---

### 03添加菜单

`POST` **/v2/s/menu/new/**

**Name:** `addMenu`

#### Parameters

| Type | Name | Description |
| --- | --- | --- |
| int | sort | 排序值 |
| String | path | 前端路径 |
| String | name | 分类名称 |
| String | component | 组件 |
| String | redirect | 定向地址 |
| String | title | 标题 |
| String | icon | 图标 |
| String | activeMenu | 菜单路径，前端用 |
| boolean | noCache | 是否缓存 true缓存，false不缓存 |
| long | parentId | 父类id |

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| int | code | 40001 参数错误 |

---

### 04修改菜单

`POST` **/v2/s/menu/:id/**

**Name:** `updateMenu`

#### Parameters

| Type | Name | Description |
| --- | --- | --- |
| String | path | 前端路径 |
| String | name | 分类名称 |
| String | component | 组件 |
| String | redirect | 定向地址 |
| String | title | 标题 |
| String | icon | 图标 |
| String | activeMenu | 菜单路径，前端用 |
| boolean | noCache | 是否缓存 true缓存，false不缓存 |
| long | parentId | 父类id |
| int | sort | 排序值 |

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| int | code | 200 请求成功 |
| int | code | 40001 参数错误 |

---

### 05删除菜单

`POST` **/v2/s/menu/**

**Name:** `deleteMenu`

#### Parameters

| Type | Name | Description |
| --- | --- | --- |
| int | id | 菜单id |
| String | operation | 操作,"del"为删除 |

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| int | code | 200 请求成功 |
| int | code | 40001 参数错误 |
| int | code | 40002 该菜单不存在 |
| int | code | 40003 该菜单为父级分类,不能直接删除 |

---

### 06批量修改角色菜单

`POST` **/v2/s/batch_update_menu_to_group/:groupId/**

**Name:** `batchAddGroupMenu`

#### Parameters

| Type | Name | Description |
| --- | --- | --- |
| JsonArray | list | menuId的数组 |

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| int | code | 200 |

---

### 07角色菜单列表

`GET` **/v2/s/group_menu/?groupId=**

**Name:** `listGroupMenu`

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| int | code | 200 请求成功 |
| JsonArray | list | 菜单列表 |
| long | id | id |
| int | sort | 排序 降序 |
| boolean | enable | 是否启用 |
| String | path | 前端路径 |
| String | name | 名称 |
| String | component | 组件 |
| String | redirect | 重定向地址 |
| String | title | 标题 |
| String | icon | 图标 |
| boolean | noCache | 是否缓存 true缓存，false不缓存 |
| String | relativePath | 菜单上下级，用于菜单之间的关系 |
| long | parentId | 父级菜单ID |
| long | createTime | 创建时间 |

---

### 08获取用户菜单列表

`GET` **/v2/s/member_menu/**

**Name:** `getMemberMenu`

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| int | code | 200 请求成功 |
| JsonArray | list | 菜单列表 |
| long | id | id |
| int | sort | 排序 降序 |
| boolean | enable | 是否启用 |
| String | path | 前端路径 |
| String | name | 名称 |
| String | component | 组件 |
| String | redirect | 重定向地址 |
| String | title | 标题 |
| String | icon | 图标 |
| boolean | noCache | 是否缓存 true缓存，false不缓存 |
| String | relativePath | 菜单上下级，用于菜单之间的关系 |
| long | parentId | 父级菜单ID |
| long | createTime | 创建时间 |

---

## SHOP-ADMIN

### 01查看管理员列表

`POST` **/v2/s/admin_members/**

**Name:** `listShopMembers`

#### Parameters

| Type | Name | Description |
| --- | --- | --- |
| int | page | 页码，0表示不分页 |
| String | realName | 姓名筛选（模糊匹配） |
| String | rules | 角色筛选（模糊匹配） |

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| int | code | 200 请求成功 |
| - | - | {json} list |
| int | pages | 总页数（分页时返回） |
| boolean | hasNest | 是否有下一页（分页时返回） |
| int | id | 用户id |
| string | userName | 用户名 |
| string | realName | 真名 |
| String | avatar | 头像 |
| String | phoneNumber | 手机号码 |
| boolean | isAdmin | 是否是管理员 |
| String | shopName | 归属店铺 |
| String | orgName | 机构名 |
| int | status | 状态 1正常 2锁定 |
| String | lastLoginTime | 最后登录时间 |
| String | lastLoginIP | 最后登录ip |

---

### 02查看管理员详情

`GET` **/v2/s/admin_members/:memberId/**

**Name:** `getAdminMember`

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| int | code | 200 请求成功 |
| int | id | 用户id |
| string | userName | 用户名 |
| string | realName | 真名 |
| String | avatar | 头像 |
| String | phoneNumber | 手机号码 |
| boolean | isAdmin | 是否是管理员 |
| String | shopName | 归属店铺 |
| int | status | 状态 1正常 2锁定 |
| String | lastLoginTime | 最后登录时间 |
| String | lastLoginIP | 最后登录ip |
| int | code | 40001 参数错误 |
| int | code | 40002 该管理员不存在 |

---

### 03添加成员

`POST` ****

**Name:** `addAdminMember`

#### Parameters

| Type | Name | Description |
| --- | --- | --- |
| string | userName | 用户名 |
| string | phoneNumber | 手机号码 |
| string | realName | 真名 |
| string | password | 密码6-20 |
| string | [avatar] | 头像地址 |
| string | [rules] | 角色 |
| boolean | isAdmin | 是否是管理员 |

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| int | code | 200 请求成功 |
| int | code | 40001 参数错误 |
| int | code | 40002 该管理员已存在 |

---

### 04修改成员

`POST` **/v2/s/admin_member/:id/**

**Name:** `updateAdminMember`

#### Parameters

| Type | Name | Description |
| --- | --- | --- |
| string | [userName] | 用户名 |
| string | [realName] | 真名 |
| string | [phoneNumber] | 电话号码 |
| string | [password] | 新密码6-20 |
| string | [avatar] | 头像地址 |

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| int | code | 200 请求成功 |
| int | code | 40001 参数错误 |
| int | code | 40002 该管理员不存在 |

---

### 05删除成员

`POST` **/v2/s/admin_member/**

**Name:** `delAdminMember`

#### Parameters

| Type | Name | Description |
| --- | --- | --- |
| int | id | 管理员id |
| String | operation | 操作,"del"为删除 |

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| int | code | 200 请求成功 |
| int | code | 40001 参数错误 |
| int | code | 40002 该管理员不存在 |

---

### 06锁定/解锁成员

`POST` **/v2/s/admin_members/status/**

**Name:** `lockMember`

#### Parameters

| Type | Name | Description |
| --- | --- | --- |
| long | memberId | 用户ID |
| int | status | 1正常，2锁定 |

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| int | code | 200 |
| int | code | 40001 用户不存在 |

---

## Admin-Member

### 07查看自己详情信息

`GET` **/v2/s/admin_member/info/**

**Name:** `getAdminMemberInfo`

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| int | code | 200 请求成功 |
| int | id | 用户id |
| string | name | 用户名 |
| string | avatar | avatar |
| int | groupId | 所在分组id |
| long | shopId | 机构ID |
| String | avatar | 头像 |
| int | code | 40001 参数错误 |
| int | code | 40002 该管理员不存在 |

---

### 08批量绑定用户到角色组

`POST` **/v2/s/bind_member_to_group/**

**Name:** `bindMemberToGroup`

#### Parameters

| Type | Name | Description |
| --- | --- | --- |
| long | uid | 用户ID |
| JsonArray | list | groupId的数组 |

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| int | code | 200 |

---

### 09用户所属分组

`GET` **/v2/s/user_groups/?memberId=**

**Name:** `listUserGroups`

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| - | - | {json} list |
| int | code | 200 请求成功 |

---

## 教师模块

### 01获取老师列表

`GET` **/v2/s/teacher_members/**

**Name:** `listTeacherMembers`

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| int | code | 200 请求成功 |
| - | - | {json} list |
| int | id | 用户id |
| string | userName | 用户名 |
| string | realName | 真名 |
| String | avatar | 头像 |
| String | phoneNumber | 手机号码 |
| boolean | isAdmin | 是否是管理员 |
| String | shopName | 归属店铺 |
| String | orgName | 机构名 |
| int | status | 状态 1正常 2锁定 |
| String | lastLoginTime | 最后登录时间 |
| String | lastLoginIP | 最后登录ip |

---

### 02导入教师信息数据

`POST` **/v2/p/teacher_import/**

**Name:** `导入教师信息数据（初始数据）`

#### Parameters

| Type | Name | Description |
| --- | --- | --- |
| file | file | 老师文件 |

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| int | 200 | 成功 |

---

## SHOP-PARAM-CONFIG

### 01获取配置列表

`GET` **/v2/s/param_config/?page=&key=**

**Name:** `listParamConfig`

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| int | code | 200 请求成功 |
| int | pages | 页数 |
| JsonArray | list | 列表 |
| int | id | 配置id |
| String | key | key |
| String | value | 值 |
| String | note | 中文备注 |
| int | code | 40001 参数错误 |
| int | code | 40002 配置不存在 |
| int | code | 40003 该配置的KEY已存在 |

---

### 02获取配置详情

`GET` **/v2/s/param_config/param_config/:configId/**

**Name:** `getParamConfig`

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| int | code | 200 请求成功 |
| int | id | 配置id |
| String | key | key |
| String | value | 值 |
| String | note | 中文备注 |
| int | code | 40001 参数错误 |
| int | code | 40002 配置不存在 |
| int | code | 40003 该配置的KEY已存在 |

---

### 03更新配置value值

`POST` **/v2/s/param_config/:id/**

**Name:** `getParamConfig`

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| int | id | 配置id |
| String | value | 值 |
| int | code | 40001 参数错误 |
| int | code | 40002 配置不存在 |
| int | code | 40003 该配置的KEY已存在 |

---

## ADMIN-CONFIG

### 03增加配置

`POST` **/v2/s/param_config/new/**

**Name:** `addParamConfig`

#### Parameters

| Type | Name | Description |
| --- | --- | --- |
| String | key | key |
| String | value | 值 |
| String | note | 中文备注 |

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| int | code | 40001 参数错误 |
| int | code | 40002 配置不存在 |
| int | code | 40003 该配置的KEY已存在 |
| int | code | 200 请求成功 |

---

## ADMIN-System

### 01操作日志

`GET` **/v2/s/operation_logs/?page=&key=**

**Name:** `listOperationLog`

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| int | code | 200 请求成功 |
| int | pages | 页数 |
| JsonArray | list | 列表 |
| int | id | id |
| String | adminId | 管理员ID |
| String | adminName | 管理员名字 |
| String | ip | 操作时IP |
| String | place | ip地址 |
| String | note | 操作说明 |
| String | createTime | 操作时间 |

---

## System

### 上传

`POST` **/v2/s/upload/**

**Name:** `upload`

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| - | - | {int}code 200 |
| string | url | 保存的地址 |
| - | - | {int}code 40003 上传失败 |

---

### 03上传并生成缩略图

`POST` **/v2/s/upload_resize/**

**Name:** `uploadAndResize`

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| - | - | {int}code 200 |
| string | big_url | 原图保存的地址 |
| string | thumbnail_url | 缩略图保存的地址 |
| - | - | {int}code 40003 上传失败 |

---

### 04上传base64

`POST` **/v2/s/upload_base64/**

**Name:** `uploadBase64`

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| - | - | {int}code 200 |
| string | imgUrl | 上传返回的地址 |
| - | - | {int}code 40003 上传失败 |

---

## 学业模块

### 01列表-学业成绩记录

`POST` **/v2/p/academic_record_list/**

**Name:** `listAcademicRecord`

#### Parameters

| Type | Name | Description |
| --- | --- | --- |
| int | page | 页码 |
| String | studentName | 学生姓名 |
| String | className | 班级名称 |

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| long | orgId | 机构ID |
| long | id | 唯一标识 |
| long | studentId | 学生ID |
| int | examType | 考试类型 |
| double | chineseScore | 语文成绩 |
| double | mathScore | 数学成绩 |
| double | englishScore | 英语成绩 |
| double | averageScore | 平均分 |
| int | gradeRanking | 年级排名 |
| int | classRanking | 班级排名 |
| int | progressAmount | 进步名次 |
| int | progressRanking | 进步排名 |
| double | calculatedScore | 计算得分 |
| String | badgeAwarded | 授予徽章 |
| long | examDate | 考试时间 |
| long | createTime | 创建时间 |
| long | updateTime | 更新时间 |
| String | className | 班级名称 |

---

### 02详情-AcademicRecord学业成绩记录

`GET` **/v2/p/academic_record/:id/**

**Name:** `getAcademicRecord`

#### Parameters

| Type | Name | Description |
| --- | --- | --- |
| long | id | id |

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| int | code | 200 |
| long | orgId | 机构ID |
| long | id | 唯一标识 |
| long | studentId | 学生ID |
| int | examType | 考试类型 |
| double | chineseScore | 语文成绩 |
| double | mathScore | 数学成绩 |
| double | englishScore | 英语成绩 |
| double | averageScore | 平均分 |
| int | gradeRanking | 年级排名 |
| int | classRanking | 班级排名 |
| int | progressAmount | 进步名次 |
| int | progressRanking | 进步排名 |
| double | calculatedScore | 计算得分 |
| String | badgeAwarded | 授予徽章 |
| long | examDate | 考试时间 |
| long | createTime | 创建时间 |
| long | updateTime | 更新时间 |

---

### 03添加-AcademicRecord学业成绩记录

`POST` **/v2/p/academic_record/new/**

**Name:** `addAcademicRecord`

#### Parameters

| Type | Name | Description |
| --- | --- | --- |
| long | orgId | 机构ID |
| long | id | 唯一标识 |
| long | studentId | 学生ID |
| int | examType | 考试类型 |
| double | chineseScore | 语文成绩 |
| double | mathScore | 数学成绩 |
| double | englishScore | 英语成绩 |
| double | averageScore | 平均分 |
| int | gradeRanking | 年级排名 |
| int | classRanking | 班级排名 |
| int | progressAmount | 进步名次 |
| int | progressRanking | 进步排名 |
| double | calculatedScore | 计算得分 |
| String | badgeAwarded | 授予徽章 |
| long | examDate | 考试时间 |
| long | createTime | 创建时间 |
| long | updateTime | 更新时间 |

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| int | code | 200 |

---

### 04更新-AcademicRecord学业成绩记录

`POST` **/v2/p/academic_record/:id/**

**Name:** `updateAcademicRecord`

#### Parameters

| Type | Name | Description |
| --- | --- | --- |
| long | orgId | 机构ID |
| long | id | 唯一标识 |
| long | studentId | 学生ID |
| int | examType | 考试类型 |
| double | chineseScore | 语文成绩 |
| double | mathScore | 数学成绩 |
| double | englishScore | 英语成绩 |
| double | averageScore | 平均分 |
| int | gradeRanking | 年级排名 |
| int | classRanking | 班级排名 |
| int | progressAmount | 进步名次 |
| int | progressRanking | 进步排名 |
| double | calculatedScore | 计算得分 |
| String | badgeAwarded | 授予徽章 |
| long | examDate | 考试时间 |
| long | createTime | 创建时间 |
| long | updateTime | 更新时间 |

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| int | code | 200 |

---

### 05删除-学业成绩记录

`POST` **/v2/p/academic_record/**

**Name:** `deleteAcademicRecord`

#### Parameters

| Type | Name | Description |
| --- | --- | --- |
| long | id | id |
| String | operation | del时删除 |

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| int | 200 | 成功 |

---

### 06导入学生成绩文件

`POST` **/v2/p/academic_record_excel/**

**Name:** `academicRecordImport`

#### Parameters

| Type | Name | Description |
| --- | --- | --- |
| file | file | 成绩文件 |

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| int | 200 | 成功 |

---

### 07导出学业成绩导入模板

`GET` **/v2/p/academic_record_excel_template/**

**Name:** `exportAcTemplate`

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| file | Excel文件 | 导入模板文件 |

---

## 徽章配置模块

### 01列表-徽章配置

`POST` **/v2/p/badge_list/**

**Name:** `listBadge`

#### Parameters

| Type | Name | Description |
| --- | --- | --- |
| int | page | 页码 |
| int | active | 状态筛选 |

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| long | orgId | 机构ID |
| long | id | 唯一标识 |
| int | badgeId | 所属徽章类型 |
| String | badgeName | 徽章名称 |
| String | description | 描述 |
| boolean | active | 是否启用 |
| long | createTime | 创建时间 |
| int | isParent | 是否家长 |

---

### 02详情-徽章配置

`GET` **/v2/p/badge/:id/**

**Name:** `getBadge`

#### Parameters

| Type | Name | Description |
| --- | --- | --- |
| long | id | id |

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| int | code | 200 |
| long | orgId | 机构ID |
| long | id | 唯一标识 |
| int | badgeId | 所属徽章类型 |
| String | badgeName | 徽章名称 |
| String | description | 描述 |
| boolean | active | 是否启用 |
| long | createTime | 创建时间 |

---

### 01添加-徽章配置

`POST` **/v2/p/badge/new/**

**Name:** `addBadge`

#### Parameters

| Type | Name | Description |
| --- | --- | --- |
| long | orgId | 机构ID |
| int | badgeId | 所属徽章类型 |
| String | badgeName | 徽章名称 |
| String | description | 描述 |
| boolean | active | 是否启用 |

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| int | code | 200 |

---

### 04更新-徽章配置

`POST` **/v2/p/badge/:id/**

**Name:** `updateBadge`

#### Parameters

| Type | Name | Description |
| --- | --- | --- |
| long | id | 唯一标识 |
| int | badgeId | 所属徽章类型 |
| String | badgeName | 徽章名称 |
| String | description | 描述 |
| boolean | active | 是否启用 |

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| int | code | 200 |

---

### 05删除-徽章配置

`POST` **/v2/p/badge/**

**Name:** `deleteBadge`

#### Parameters

| Type | Name | Description |
| --- | --- | --- |
| long | id | id |
| String | operation | del时删除 |

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| int | 200 | 成功 |

---

## 徽章授予记录模块

### 01列表-徽章授予记录

`GET` **/v2/p/badge_record_list/**

**Name:** `listBadgeRecord`

#### Parameters

| Type | Name | Description |
| --- | --- | --- |
| int | page | 页码 |
| String | filter | 搜索栏() |

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| long | orgId | 机构ID |
| long | id | 唯一标识 |
| long | studentId | 学生ID |
| String | badgeType | 徽章类型 |
| String | awardReason | 授予原因 |
| long | awardTime | 授予时间 |
| String | awardPeriod | 授予周期 |
| long | createTime | 创建时间 |

---

### 02详情-BadgeRecord徽章授予记录

`GET` **/v2/p/badge_record/:id/**

**Name:** `getBadgeRecord`

#### Parameters

| Type | Name | Description |
| --- | --- | --- |
| long | id | id |

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| int | code | 200 |
| long | orgId | 机构ID |
| long | id | 唯一标识 |
| long | studentId | 学生ID |
| String | badgeType | 徽章类型 |
| String | awardReason | 授予原因 |
| long | awardTime | 授予时间 |
| String | awardPeriod | 授予周期 |
| long | createTime | 创建时间 |

---

### 03添加-BadgeRecord徽章授予记录

`POST` **/v2/p/badge_record/new/**

**Name:** `addBadgeRecord`

#### Parameters

| Type | Name | Description |
| --- | --- | --- |
| long | orgId | 机构ID |
| long | id | 唯一标识 |
| long | studentId | 学生ID |
| String | badgeType | 徽章类型 |
| String | awardReason | 授予原因 |
| long | awardTime | 授予时间 |
| String | awardPeriod | 授予周期 |
| long | createTime | 创建时间 |

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| int | code | 200 |

---

### 04更新-BadgeRecord徽章授予记录

`POST` **/v2/p/badge_record/:id/**

**Name:** `updateBadgeRecord`

#### Parameters

| Type | Name | Description |
| --- | --- | --- |
| long | orgId | 机构ID |
| long | id | 唯一标识 |
| long | studentId | 学生ID |
| String | badgeType | 徽章类型 |
| String | awardReason | 授予原因 |
| long | awardTime | 授予时间 |
| String | awardPeriod | 授予周期 |
| long | createTime | 创建时间 |

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| int | code | 200 |

---

### 05删除-徽章授予记录

`POST` **/v2/p/badge_record/**

**Name:** `deleteBadgeRecord`

#### Parameters

| Type | Name | Description |
| --- | --- | --- |
| long | id | id |
| String | operation | del时删除 |

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| int | 200 | 成功 |

---

## 班级基础配置模块

### 01列表-班级配置信息

`GET` **/v2/p/class_config/**

**Name:** `listSchoolClassConfig`

#### Parameters

| Type | Name | Description |
| --- | --- | --- |
| int | page | 页码 |
| String | filter | 搜索栏() |

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| String | className | 班级名称 |
| long | orgId | 机构ID |

---

## 班级分组模块

### 01列表-该教师在此班级的分组配置

`POST` **/v2/p/class_group_list/**

**Name:** `listClassGroup`

#### Parameters

| Type | Name | Description |
| --- | --- | --- |
| int | page | 页码 |
| String | classId | 班级ID（可选） |

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| Long | id | 分组ID |
| Long | orgId | 机构ID |
| Long | classId | 班级ID |
| Long | teacherId | 老师ID |
| String | groupName | 分组名称 |
| Long | createTime | 创建时间 |
| Long | updateTime | 更新时间 |

#### Response Example

```json
{json} 响应示例:
{
"pages": 1,
"hasNest": false,
"code": 200,
"list": [
{
"id": 8,
"orgId": 1,
"classId": 1,
"teacherId": 5,
"groupName": "0101K组",
"createTime": 1767063985555,
"updateTime": 1767063985555,
"className": "一年级一班",
"studentGroups": [
{
"id": 36,
"groupId": 8,
"studentId": 35,
"createTime": 1767064055026,
"updateTime": 1767064055026,
"studentName": "邹锦浩",
"studentNumber": "20250135"
},
{
"id": 37,
"groupId": 8,
"studentId": 37,
"createTime": 1767064055026,
"updateTime": 1767064055026,
"studentName": "ZHENGBENJAMIN",
"studentNumber": "20250137"
},
{
"id": 38,
"groupId": 8,
"studentId": 38,
"createTime": 1767064055026,
"updateTime": 1767064055026,
"studentName": "翁嘉盛",
"studentNumber": "20250138"
},
{
"id": 39,
"groupId": 8,
"studentId": 39,
"createTime": 1767064055026,
"updateTime": 1767064055026,
"studentName": "CHENLIMATEO",
"studentNumber": "20250139"
}
]
},
{
"id": 7,
"orgId": 1,
"classId": 1,
"teacherId": 5,
"groupName": "0101J组",
"createTime": 1767063972705,
"updateTime": 1767063972705,
"className": "一年级一班",
"studentGroups": [
{
"id": 31,
"groupId": 7,
"studentId": 32,
"createTime": 1767064048443,
"updateTime": 1767064048443,
"studentName": "林梓昱",
"studentNumber": "20250132"
},
{
"id": 32,
"groupId": 7,
"studentId": 33,
"createTime": 1767064048443,
"updateTime": 1767064048443,
"studentName": "姚凯彬",
"studentNumber": "20250133"
},
{
"id": 33,
"groupId": 7,
"studentId": 34,
"createTime": 1767064048443,
"updateTime": 1767064048443,
"studentName": "李诗琪",
"studentNumber": "20250134"
},
{
"id": 34,
"groupId": 7,
"studentId": 36,
"createTime": 1767064048443,
"updateTime": 1767064048443,
"studentName": "YANEVELYNANGELICA",
"studentNumber": "20250136"
},
{
"id": 35,
"groupId": 7,
"studentId": 31,
"createTime": 1767064048443,
"updateTime": 1767064048443,
"studentName": "黄以安",
"studentNumber": "20250131"
}
]
},
{
"id": 6,
"orgId": 1,
"classId": 1,
"teacherId": 5,
"groupName": "0101F组",
"createTime": 1767063962227,
"updateTime": 1767063962227,
"className": "一年级一班",
"studentGroups": [
{
"id": 26,
"groupId": 6,
"studentId": 26,
"createTime": 1767064039140,
"updateTime": 1767064039140,
"studentName": "陈悦纳",
"studentNumber": "20250126"
},
{
"id": 27,
"groupId": 6,
"studentId": 27,
"createTime": 1767064039140,
"updateTime": 1767064039140,
"studentName": "庄沐恩",
"studentNumber": "20250127"
},
{
"id": 28,
"groupId": 6,
"studentId": 28,
"createTime": 1767064039140,
"updateTime": 1767064039140,
"studentName": "詹蓁蓁",
"studentNumber": "20250128"
},
{
"id": 29,
"groupId": 6,
"studentId": 29,
"createTime": 1767064039140,
"updateTime": 1767064039140,
"studentName": "陈炫宇",
"studentNumber": "20250129"
},
{
"id": 30,
"groupId": 6,
"studentId": 30,
"createTime": 1767064039140,
"updateTime": 1767064039140,
"studentName": "许梓轩",
"studentNumber": "20250130"
}
]
},
{
"id": 5,
"orgId": 1,
"classId": 1,
"teacherId": 5,
"groupName": "0101E组",
"createTime": 1767063954363,
"updateTime": 1767063954363,
"className": "一年级一班",
"studentGroups": [
{
"id": 21,
"groupId": 5,
"studentId": 21,
"createTime": 1767064032222,
"updateTime": 1767064032222,
"studentName": "林恩多",
"studentNumber": "20250121"
},
{
"id": 22,
"groupId": 5,
"studentId": 22,
"createTime": 1767064032222,
"updateTime": 1767064032222,
"studentName": "黄昱翎",
"studentNumber": "20250122"
},
{
"id": 23,
"groupId": 5,
"studentId": 23,
"createTime": 1767064032222,
"updateTime": 1767064032222,
"studentName": "薛逸菲",
"studentNumber": "20250123"
},
{
"id": 24,
"groupId": 5,
"studentId": 24,
"createTime": 1767064032222,
"updateTime": 1767064032222,
"studentName": "庄景皓",
"studentNumber": "20250124"
},
{
"id": 25,
"groupId": 5,
"studentId": 25,
"createTime": 1767064032222,
"updateTime": 1767064032222,
"studentName": "林璟怡",
"studentNumber": "20250125"
}
]
},
{
"id": 4,
"orgId": 1,
"classId": 1,
"teacherId": 5,
"groupName": "0101D组",
"createTime": 1767063945907,
"updateTime": 1767063945907,
"className": "一年级一班",
"studentGroups": [
{
"id": 16,
"groupId": 4,
"studentId": 16,
"createTime": 1767064024795,
"updateTime": 1767064024795,
"studentName": "余天鑫",
"studentNumber": "20250116"
},
{
"id": 17,
"groupId": 4,
"studentId": 17,
"createTime": 1767064024795,
"updateTime": 1767064024795,
"studentName": "何宥辰",
"studentNumber": "20250117"
},
{
"id": 18,
"groupId": 4,
"studentId": 18,
"createTime": 1767064024795,
"updateTime": 1767064024795,
"studentName": "林昊宸",
"studentNumber": "20250118"
},
{
"id": 19,
"groupId": 4,
"studentId": 19,
"createTime": 1767064024795,
"updateTime": 1767064024795,
"studentName": "严凌宇",
"studentNumber": "20250119"
},
{
"id": 20,
"groupId": 4,
"studentId": 20,
"createTime": 1767064024795,
"updateTime": 1767064024795,
"studentName": "杨欣妍",
"studentNumber": "20250120"
}
]
},
{
"id": 3,
"orgId": 1,
"classId": 1,
"teacherId": 5,
"groupName": "0101C组",
"createTime": 1767063937576,
"updateTime": 1767063937576,
"className": "一年级一班",
"studentGroups": [
{
"id": 11,
"groupId": 3,
"studentId": 11,
"createTime": 1767064016362,
"updateTime": 1767064016362,
"studentName": "洪煜飞",
"studentNumber": "20250111"
},
{
"id": 12,
"groupId": 3,
"studentId": 12,
"createTime": 1767064016362,
"updateTime": 1767064016362,
"studentName": "林睿豪",
"studentNumber": "20250112"
},
{
"id": 13,
"groupId": 3,
"studentId": 13,
"createTime": 1767064016362,
"updateTime": 1767064016362,
"studentName": "胡佳恩",
"studentNumber": "20250113"
},
{
"id": 14,
"groupId": 3,
"studentId": 14,
"createTime": 1767064016362,
"updateTime": 1767064016362,
"studentName": "何欣钥",
"studentNumber": "20250114"
},
{
"id": 15,
"groupId": 3,
"studentId": 15,
"createTime": 1767064016362,
"updateTime": 1767064016362,
"studentName": "林哲锐",
"studentNumber": "20250115"
}
]
},
{
"id": 2,
"orgId": 1,
"classId": 1,
"teacherId": 5,
"groupName": "0101B组",
"createTime": 1767063928081,
"updateTime": 1767063928081,
"className": "一年级一班",
"studentGroups": [
{
"id": 6,
"groupId": 2,
"studentId": 6,
"createTime": 1767064006979,
"updateTime": 1767064006979,
"studentName": "庄鹏博",
"studentNumber": "20250106"
},
{
"id": 7,
"groupId": 2,
"studentId": 7,
"createTime": 1767064006979,
"updateTime": 1767064006979,
"studentName": "林嘉韵",
"studentNumber": "20250107"
},
{
"id": 8,
"groupId": 2,
"studentId": 8,
"createTime": 1767064006979,
"updateTime": 1767064006979,
"studentName": "何安桐",
"studentNumber": "20250108"
},
{
"id": 9,
"groupId": 2,
"studentId": 9,
"createTime": 1767064006979,
"updateTime": 1767064006979,
"studentName": "周芯宥",
"studentNumber": "20250109"
},
{
"id": 10,
"groupId": 2,
"studentId": 10,
"createTime": 1767064006979,
"updateTime": 1767064006979,
"studentName": "余亦航",
"studentNumber": "20250110"
}
]
},
{
"id": 1,
"orgId": 1,
"classId": 1,
"teacherId": 5,
"groupName": "0101A组",
"createTime": 1767063912039,
"updateTime": 1767063912039,
"className": "一年级一班",
"studentGroups": [
{
"id": 1,
"groupId": 1,
"studentId": 1,
"createTime": 1767063996010,
"updateTime": 1767063996010,
"studentName": "王欣瑶",
"studentNumber": "20250101"
},
{
"id": 2,
"groupId": 1,
"studentId": 2,
"createTime": 1767063996010,
"updateTime": 1767063996010,
"studentName": "邓安妮",
"studentNumber": "20250102"
},
{
"id": 3,
"groupId": 1,
"studentId": 3,
"createTime": 1767063996010,
"updateTime": 1767063996010,
"studentName": "翁煜宸",
"studentNumber": "20250103"
},
{
"id": 4,
"groupId": 1,
"studentId": 4,
"createTime": 1767063996010,
"updateTime": 1767063996010,
"studentName": "魏博辉",
"studentNumber": "20250104"
},
{
"id": 5,
"groupId": 1,
"studentId": 5,
"createTime": 1767063996010,
"updateTime": 1767063996010,
"studentName": "郑嘉诺",
"studentNumber": "20250105"
}
]
}
]
}

```

---

### 02详情-班级分组

`GET` **/v2/p/class_group/:id/**

**Name:** `getClassGroup`

#### Parameters

| Type | Name | Description |
| --- | --- | --- |
| Long | id | 分组ID |

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| Long | id | 分组ID |
| Long | orgId | 机构ID |
| Long | classId | 班级ID |
| Long | teacherId | 老师ID |
| String | groupName | 分组名称 |
| Long | createTime | 创建时间 |
| Long | updateTime | 更新时间 |

---

### 03添加-班级分组

`POST` **/v2/p/class_group/new/**

**Name:** `addClassGroup`

#### Parameters

| Type | Name | Description |
| --- | --- | --- |
| Long | classId | 班级ID |
| Long | teacherId | 老师ID |
| String | groupName | 分组名称 |

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| int | code | 200 |

---

### 04更新-班级分组

`POST` **/v2/p/class_group/:id/**

**Name:** `updateClassGroup`

#### Parameters

| Type | Name | Description |
| --- | --- | --- |
| Long | id | 分组ID |
| Long | teacherId | 老师ID（可选） |
| String | groupName | 分组名称（可选） |

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| int | code | 200 |

---

### 05删除-班级分组

`POST` **/v2/p/class_group/**

**Name:** `deleteClassGroup`

#### Parameters

| Type | Name | Description |
| --- | --- | --- |
| Long | id | 分组ID |
| String | operation | del时删除 |

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| int | 200 | 成功 |

---

### 06获取分组中的学生列表

`GET` **/v2/p/class_group/students/:id/**

**Name:** `getGroupStudents`

#### Parameters

| Type | Name | Description |
| --- | --- | --- |
| Long | id | 分组ID |
| String | filter | 过滤条件（可选，支持按姓名或学号过滤） |

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| int | code | 200 |
| Array | list | 学生列表 |

---

### 07同步分组中的学生列表

`POST` **/v2/p/class_group/manage_students/:id/**

**Name:** `manageGroupStudents`

#### Parameters

| Type | Name | Description |
| --- | --- | --- |
| Long | id | 分组ID（路径参数） |
| Array | studentIds | 学生ID数组，例如：[1, 2, 3, 4, 5]。系统会根据此列表自动判断哪些需要添加，哪些需要删除 |

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| int | code | 200 |
| int | addedCount | 新增的学生数量 |
| int | removedCount | 删除的学生数量 |
| int | invalidCount | 无效的学生数量（学生不存在或不属于该班级） |

#### Response Example

```json
{json} 请求示例:
{
"studentIds": [1, 2, 3, 4, 5]
}
```

```json
{json} 响应示例:
{
"code": 200,
"addedCount": 2,
"removedCount": 1,
"invalidCount": 0
}

```

---

### 10获取班级中未分组的学生列表

`GET` **/v2/p/class_group/ungrouped_students/:classId/**

**Name:** `getUngroupedStudents`

#### Parameters

| Type | Name | Description |
| --- | --- | --- |
| Long | classId | 班级ID |
| String | filter | 过滤条件（可选，支持按姓名或学号过滤） |

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| int | code | 200 |
| Array | list | 未分组的学生列表 |

---

## 班级常规

### 01列表-班级常规评比

`POST` **/v2/p/class_routine_list/**

**Name:** `listClassRoutine`

#### Parameters

| Type | Name | Description |
| --- | --- | --- |
| int | page | 页码 |
| String | className | 班级名称 |

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| long | orgId | 机构ID |
| long | id | 唯一标识 |
| long | classId | 班级ID |
| int | weekNumber | 周次 |
| int | month | 月份 |
| int | year | 年份 |
| double | hygieneScore | 卫生得分 |
| double | disciplineScore | 纪律得分 |
| double | exerciseScore | 两操得分 |
| double | mannerScore | 文明礼仪得分 |
| double | readingScore | 晨诵午读得分 |
| double | totalScore | 周总分 |
| long | evaluatorId | 评分人ID |
| String | evaluatorName | 评分人姓名 |
| int | evaluateType | 评分类型 |
| String | comments | 评语 |
| long | recordTime | 记录时间 |
| long | createTime | 创建时间 |
| long | updateTime | 更新时间 |
| String | className | 班级名称 |

---

## CLASS-ROUTINE-CONTROLLER

### 02详情-ClassRoutine班级常规评比

`GET` **/v2/p/class_routine/:id/**

**Name:** `getClassRoutine`

#### Parameters

| Type | Name | Description |
| --- | --- | --- |
| long | id | id |

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| int | code | 200 |
| long | orgId | 机构ID |
| long | id | 唯一标识 |
| long | classId | 班级ID |
| int | weekNumber | 周次 |
| int | month | 月份 |
| int | year | 年份 |
| double | hygieneScore | 卫生得分 |
| double | disciplineScore | 纪律得分 |
| double | exerciseScore | 两操得分 |
| double | mannerScore | 文明礼仪得分 |
| double | readingScore | 晨诵午读得分 |
| double | totalScore | 周总分 |
| long | evaluatorId | 评分人ID |
| String | evaluatorName | 评分人姓名 |
| int | evaluateType | 评分类型 |
| String | comments | 评语 |
| long | recordTime | 记录时间 |
| long | createTime | 创建时间 |
| long | updateTime | 更新时间 |

---

### 01添加-ClassRoutine班级常规评比

`POST` **/v2/p/class_routine/new/**

**Name:** `addClassRoutine`

#### Parameters

| Type | Name | Description |
| --- | --- | --- |
| long | orgId | 机构ID |
| long | id | 唯一标识 |
| long | classId | 班级ID |
| int | weekNumber | 周次 |
| int | month | 月份 |
| int | year | 年份 |
| double | hygieneScore | 卫生得分 |
| double | disciplineScore | 纪律得分 |
| double | exerciseScore | 两操得分 |
| double | mannerScore | 文明礼仪得分 |
| double | readingScore | 晨诵午读得分 |
| double | totalScore | 周总分 |
| long | evaluatorId | 评分人ID |
| String | evaluatorName | 评分人姓名 |
| int | evaluateType | 评分类型 |
| String | comments | 评语 |
| long | recordTime | 记录时间 |
| long | createTime | 创建时间 |
| long | updateTime | 更新时间 |

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| int | code | 200 |

---

### 04更新-ClassRoutine班级常规评比

`POST` **/v2/p/class_routine/:id/**

**Name:** `updateClassRoutine`

#### Parameters

| Type | Name | Description |
| --- | --- | --- |
| long | orgId | 机构ID |
| long | id | 唯一标识 |
| long | classId | 班级ID |
| int | weekNumber | 周次 |
| int | month | 月份 |
| int | year | 年份 |
| double | hygieneScore | 卫生得分 |
| double | disciplineScore | 纪律得分 |
| double | exerciseScore | 两操得分 |
| double | mannerScore | 文明礼仪得分 |
| double | readingScore | 晨诵午读得分 |
| double | totalScore | 周总分 |
| long | evaluatorId | 评分人ID |
| String | evaluatorName | 评分人姓名 |
| int | evaluateType | 评分类型 |
| String | comments | 评语 |
| long | recordTime | 记录时间 |
| long | createTime | 创建时间 |
| long | updateTime | 更新时间 |

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| int | code | 200 |

---

### 05删除-班级常规评比

`POST` **/v2/p/class_routine/**

**Name:** `deleteClassRoutine`

#### Parameters

| Type | Name | Description |
| --- | --- | --- |
| long | id | id |
| String | operation | del时删除 |

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| int | 200 | 成功 |

---

## 班级教师模块

### 01列表-班级教师关系表

`POST` **/v2/p/class_teacher_relation_list/**

**Name:** `listClassTeacherRelation`

#### Parameters

| Type | Name | Description |
| --- | --- | --- |
| int | page | 页码 |
| String | filter | 搜索栏() |

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| long | orgId | 机构ID |
| long | id | 唯一标识 |
| long | classId | 班级ID |
| long | teacherId | 教师ID |
| String | subject | 任教科目 |
| boolean | isHeadTeacher | 是否班主任 |
| long | createTime | 创建时间 |
| long | updateTime | 更新时间 |

---

### 02详情-ClassTeacherRelation班级教师关系表

`GET` **/v2/p/class_teacher_relation/:id/**

**Name:** `getClassTeacherRelation`

#### Parameters

| Type | Name | Description |
| --- | --- | --- |
| long | id | id |

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| int | code | 200 |
| long | orgId | 机构ID |
| long | id | 唯一标识 |
| long | classId | 班级ID |
| long | teacherId | 教师ID |
| String | subject | 任教科目 |
| boolean | isHeadTeacher | 是否班主任 |
| long | createTime | 创建时间 |
| long | updateTime | 更新时间 |

---

### 03添加-ClassTeacherRelation班级教师关系表

`POST` **/v2/p/class_teacher_relation/new/**

**Name:** `addClassTeacherRelation`

#### Parameters

| Type | Name | Description |
| --- | --- | --- |
| long | orgId | 机构ID |
| long | id | 唯一标识 |
| long | classId | 班级ID |
| long | teacherId | 教师ID |
| String | subject | 任教科目 |
| boolean | isHeadTeacher | 是否班主任 |
| long | createTime | 创建时间 |
| long | updateTime | 更新时间 |

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| int | code | 200 |

---

### 04更新-ClassTeacherRelation班级教师关系表

`POST` **/v2/p/class_teacher_relation/:id/**

**Name:** `updateClassTeacherRelation`

#### Parameters

| Type | Name | Description |
| --- | --- | --- |
| long | orgId | 机构ID |
| long | id | 唯一标识 |
| long | classId | 班级ID |
| long | teacherId | 教师ID |
| String | subject | 任教科目 |
| boolean | isHeadTeacher | 是否班主任 |
| long | createTime | 创建时间 |
| long | updateTime | 更新时间 |

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| int | code | 200 |

---

### 05删除-班级教师关系表

`POST` **/v2/p/class_teacher_relation/**

**Name:** `deleteClassTeacherRelation`

#### Parameters

| Type | Name | Description |
| --- | --- | --- |
| long | id | id |
| String | operation | del时删除 |

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| int | 200 | 成功 |

---

### 06列表-班级教师关系列表

`POST` **/v2/p/class_teacher_relation_list/new/**

**Name:** `listClassTeacherRelation`

#### Parameters

| Type | Name | Description |
| --- | --- | --- |
| int | page | 页码 |
| String | filter | 搜索栏() |

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| long | orgId | 机构ID |
| long | id | 唯一标识 |
| long | classId | 班级ID |
| long | teacherId | 教师ID |
| String | teacherName | 教师姓名 |
| String | subject | 任教科目 |
| boolean | isHeadTeacher | 是否班主任 |
| long | createTime | 创建时间 |
| long | updateTime | 更新时间 |

---

### 07列表-获取当前班主任所在的班级

`GET` **/v2/p/header_teacher_class/**

**Name:** `headerTeacherClass`

#### Response Example

```json
{json} 响应示例:
{
"code": 200,
"list": [
{
"orgId": 1,
"id": 1,
"className": "一年级一班",
"grade": 1,
"classId": 1,
"headTeacherId": 2168,
"headTeacher": null,
"studentNum": 39,
"academicScore": 0.0,
"specialtyScore": 0.0,
"routineScore": 0.0,
"homeVisitScore": 0.0,
"totalScore": 0.0,
"disqualified": false,
"deductionScore": 0.0,
"honorTitle": "",
"createTime": 1766132506197,
"teachers": null,
"highGrade": false,
"academicRankInGrade": 1,
"specialtyRankInGrade": 1
}
]
}

```

---

### 08列表-获取当前教师所在的班级

`GET` **/v2/p/teacher_class/**

**Name:** `teacherClass`

#### Response Example

```json
{json} 响应示例:
{
"code": 200,
"list": [
{
"orgId": 1,
"id": 1,
"className": "一年级一班",
"grade": 1,
"classId": 1,
"headTeacherId": 2168,
"headTeacher": null,
"studentNum": 39,
"academicScore": 0.0,
"specialtyScore": 0.0,
"routineScore": 0.0,
"homeVisitScore": 0.0,
"totalScore": 0.0,
"disqualified": false,
"deductionScore": 0.0,
"honorTitle": "",
"createTime": 1766132506197,
"teachers": null,
"highGrade": false,
"academicRankInGrade": 1,
"specialtyRankInGrade": 1
}
]
}

```

---

## 评价规则配置模块

### 01列表-评价规则配置

`GET` **/v2/p/evaluation_rule_list/**

**Name:** `listEvaluationRule`

#### Parameters

| Type | Name | Description |
| --- | --- | --- |
| int | page | 页码 |
| String | filter | 搜索栏() |
| String | name | 指标名称 |
| double | scoreBasic | 类型基础分 |
| double | scoreMax | 类型上限分 |

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| long | orgId | 机构ID |
| long | id | 唯一标识 |
| String | badgeType | 徽章类型 |
| String | name | 指标名称 |
| String | description | 描述 |
| boolean | active | 是否启用 |
| long | createTime | 创建时间 |

#### Response Example

```json
{json} 响应示例:
{
"pages": 1,
"hasNest": false,
"code": 200,
"list": [
{
"orgId": 1,
"id": 5,
"name": "生活素养",
"scoreBasic": 3.0,
"scoreMax": 7.0,
"badgeType": "劳",
"description": "",
"active": true,
"createTime": 1766463839123,
"badges": [
{
"orgId": 1,
"id": 35,
"badgeId": 5,
"badgeName": "自理能力",
"badgeImage": null,
"description": "",
"active": true,
"createTime": 1766465112834
},
{
"orgId": 1,
"id": 36,
"badgeId": 5,
"badgeName": "物品归位",
"badgeImage": null,
"description": "",
"active": true,
"createTime": 1766465120764
},
{
"orgId": 1,
"id": 37,
"badgeId": 5,
"badgeName": "家务分担",
"badgeImage": null,
"description": "",
"active": true,
"createTime": 1766465128379
},
{
"orgId": 1,
"id": 38,
"badgeId": 5,
"badgeName": "值日负责",
"badgeImage": null,
"description": "",
"active": true,
"createTime": 1766465138269
},
{
"orgId": 1,
"id": 39,
"badgeId": 5,
"badgeName": "生命教育",
"badgeImage": null,
"description": "",
"active": true,
"createTime": 1766465147973
},
{
"orgId": 1,
"id": 40,
"badgeId": 5,
"badgeName": "合作意识",
"badgeImage": null,
"description": "",
"active": true,
"createTime": 1766465156953
}
]
},
{
"orgId": 1,
"id": 4,
"name": "生活素养",
"scoreBasic": 3.0,
"scoreMax": 7.0,
"badgeType": "美",
"description": "",
"active": true,
"createTime": 1766463812306,
"badges": [
{
"orgId": 1,
"id": 32,
"badgeId": 4,
"badgeName": "经典诵读",
"badgeImage": null,
"description": "",
"active": true,
"createTime": 1766465068581
},
{
"orgId": 1,
"id": 33,
"badgeId": 4,
"badgeName": "艺术培养",
"badgeImage": null,
"description": "",
"active": true,
"createTime": 1766465082651
},
{
"orgId": 1,
"id": 34,
"badgeId": 4,
"badgeName": "舞台展示",
"badgeImage": null,
"description": "",
"active": true,
"createTime": 1766465089492
}
]
},
{
"orgId": 1,
"id": 3,
"name": "生活素养",
"scoreBasic": 3.0,
"scoreMax": 7.0,
"badgeType": "体",
"description": "",
"active": true,
"createTime": 1766463785608,
"badges": [
{
"orgId": 1,
"id": 26,
"badgeId": 3,
"badgeName": "规律作息",
"badgeImage": null,
"description": "",
"active": true,
"createTime": 1766465002690
},
{
"orgId": 1,
"id": 27,
"badgeId": 3,
"badgeName": "用眼卫生",
"badgeImage": null,
"description": "",
"active": true,
"createTime": 1766465016336
},
{
"orgId": 1,
"id": 28,
"badgeId": 3,
"badgeName": "个人卫生",
"badgeImage": null,
"description": "",
"active": true,
"createTime": 1766465026984
},
{
"orgId": 1,
"id": 29,
"badgeId": 3,
"badgeName": "日常锻炼",
"badgeImage": null,
"description": "",
"active": true,
"createTime": 1766465033092
},
{
"orgId": 1,
"id": 30,
"badgeId": 3,
"badgeName": "饮食健康",
"badgeImage": null,
"description": "",
"active": true,
"createTime": 1766465041255
},
{
"orgId": 1,
"id": 31,
"badgeId": 3,
"badgeName": "运动安全",
"badgeImage": null,
"description": "",
"active": true,
"createTime": 1766465050019
}
]
},
{
"orgId": 1,
"id": 2,
"name": "生活素养",
"scoreBasic": 3.0,
"scoreMax": 9.0,
"badgeType": "智",
"description": "",
"active": true,
"createTime": 1766463764929,
"badges": [
{
"orgId": 1,
"id": 15,
"badgeId": 2,
"badgeName": "课堂专注",
"badgeImage": null,
"description": "",
"active": true,
"createTime": 1766464810944
},
{
"orgId": 1,
"id": 16,
"badgeId": 2,
"badgeName": "规范书写",
"badgeImage": null,
"description": "",
"active": true,
"createTime": 1766464818350
},
{
"orgId": 1,
"id": 17,
"badgeId": 2,
"badgeName": "积极发言",
"badgeImage": null,
"description": "",
"active": true,
"createTime": 1766464828244
},
{
"orgId": 1,
"id": 18,
"badgeId": 2,
"badgeName": "作业规范",
"badgeImage": null,
"description": "",
"active": true,
"createTime": 1766464836113
},
{
"orgId": 1,
"id": 19,
"badgeId": 2,
"badgeName": "电子管理",
"badgeImage": null,
"description": "",
"active": true,
"createTime": 1766464865563
},
{
"orgId": 1,
"id": 20,
"badgeId": 2,
"badgeName": "每日阅读",
"badgeImage": null,
"description": "坚持每天自主读20-30分钟课外书，读完能把内容讲出来；高年级学生可以尝试写读后感，制作思维导图等。（校长作业）",
"active": true,
"createTime": 1766464899469
},
{
"orgId": 1,
"id": 21,
"badgeId": 2,
"badgeName": "物品整理",
"badgeImage": null,
"description": "",
"active": true,
"createTime": 1766464922457
},
{
"orgId": 1,
"id": 22,
"badgeId": 2,
"badgeName": "时间管理",
"badgeImage": null,
"description": "",
"active": true,
"createTime": 1766464958219
},
{
"orgId": 1,
"id": 23,
"badgeId": 2,
"badgeName": "问题解决",
"badgeImage": null,
"description": "",
"active": true,
"createTime": 1766464969957
},
{
"orgId": 1,
"id": 24,
"badgeId": 2,
"badgeName": "复习习惯",
"badgeImage": null,
"description": "",
"active": true,
"createTime": 1766464976107
},
{
"orgId": 1,
"id": 25,
"badgeId": 2,
"badgeName": "考试规划",
"badgeImage": null,
"description": "",
"active": true,
"createTime": 1766464982989
}
]
},
{
"orgId": 1,
"id": 1,
"name": "生活素养",
"scoreBasic": 3.0,
"scoreMax": 12.0,
"badgeType": "德",
"description": "",
"active": true,
"createTime": 1766461919467,
"badges": [
{
"orgId": 1,
"id": 1,
"badgeId": 1,
"badgeName": "升旗礼仪",
"badgeImage": null,
"description": "",
"active": true,
"createTime": 1766464656430
},
{
"orgId": 1,
"id": 2,
"badgeId": 1,
"badgeName": "见面礼仪",
"badgeImage": null,
"description": "",
"active": true,
"createTime": 1766464664323
},
{
"orgId": 1,
"id": 3,
"badgeId": 1,
"badgeName": "交往礼仪",
"badgeImage": null,
"description": "",
"active": true,
"createTime": 1766464673365
},
{
"orgId": 1,
"id": 4,
"badgeId": 1,
"badgeName": "交谈礼仪",
"badgeImage": null,
"description": "",
"active": true,
"createTime": 1766464681526
},
{
"orgId": 1,
"id": 5,
"badgeId": 1,
"badgeName": "倾听礼仪",
"badgeImage": null,
"description": "",
"active": true,
"createTime": 1766464697282
},
{
"orgId": 1,
"id": 6,
"badgeId": 1,
"badgeName": "会客礼仪",
"badgeImage": null,
"description": "",
"active": true,
"createTime": 1766464703948
},
{
"orgId": 1,
"id": 7,
"badgeId": 1,
"badgeName": "访客礼仪",
"badgeImage": null,
"description": "",
"active": true,
"createTime": 1766464712176
},
{
"orgId": 1,
"id": 8,
"badgeId": 1,
"badgeName": "外出礼仪",
"badgeImage": null,
"description": "",
"active": true,
"createTime": 1766464718170
},
{
"orgId": 1,
"id": 9,
"badgeId": 1,
"badgeName": "行走礼仪",
"badgeImage": null,
"description": "",
"active": true,
"createTime": 1766464729539
},
{
"orgId": 1,
"id": 10,
"badgeId": 1,
"badgeName": "仪表礼仪",
"badgeImage": null,
"description": "",
"active": true,
"createTime": 1766464739948
},
{
"orgId": 1,
"id": 11,
"badgeId": 1,
"badgeName": "就餐礼仪",
"badgeImage": null,
"description": "",
"active": true,
"createTime": 1766464753823
},
{
"orgId": 1,
"id": 12,
"badgeId": 1,
"badgeName": "公共礼仪",
"badgeImage": null,
"description": "",
"active": true,
"createTime": 1766464761472
},
{
"orgId": 1,
"id": 13,
"badgeId": 1,
"badgeName": "责任担当",
"badgeImage": null,
"description": "",
"active": true,
"createTime": 1766464769958
},
{
"orgId": 1,
"id": 14,
"badgeId": 1,
"badgeName": "情绪管理",
"badgeImage": null,
"description": "",
"active": true,
"createTime": 1766464783716
}
]
}
]
}

```

---

### 02详情-EvaluationRule评价规则配置

`GET` **/v2/p/evaluation_rule/:id/**

**Name:** `getEvaluationRule`

#### Parameters

| Type | Name | Description |
| --- | --- | --- |
| long | id | id |
| double | scoreBasic | 类型基础分 |
| double | scoreMax | 类型上限分 |

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| int | code | 200 |
| long | orgId | 机构ID |
| long | id | 唯一标识 |
| String | badgeType | 徽章类型 |
| String | description | 描述 |
| boolean | active | 是否启用 |
| long | createTime | 创建时间 |

---

### 01添加-EvaluationRule评价规则配置

`POST` **/v2/p/evaluation_rule/new/**

**Name:** `addEvaluationRule`

#### Parameters

| Type | Name | Description |
| --- | --- | --- |
| long | orgId | 机构ID |
| long | id | 唯一标识 |
| double | scoreBasic | 类型基础分 |
| double | scoreMax | 类型上限分 |

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| String | badgeType | 徽章类型 |
| String | description | 描述 |
| boolean | active | 是否启用 |
| long | createTime | 创建时间 |
| int | code | 200 |

---

### 04更新-EvaluationRule评价规则配置

`POST` **/v2/p/evaluation_rule/:id/**

**Name:** `updateEvaluationRule`

#### Parameters

| Type | Name | Description |
| --- | --- | --- |
| long | orgId | 机构ID |
| long | id | 唯一标识 |
| double | scoreBasic | 类型基础分 |
| double | scoreMax | 类型上限分 |

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| String | badgeType | 徽章类型 |
| String | description | 描述 |
| boolean | active | 是否启用 |
| long | createTime | 创建时间 |
| int | code | 200 |

---

### 05删除-评价规则配置

`POST` **/v2/p/evaluation_rule/**

**Name:** `deleteEvaluationRule`

#### Parameters

| Type | Name | Description |
| --- | --- | --- |
| long | id | id |
| String | operation | del时删除 |

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| int | 200 | 成功 |

---

## 习惯评价

### 01列表-习惯评价记录

`GET` **/v2/p/habit_record_list/**

**Name:** `listHabitRecord`

#### Parameters

| Type | Name | Description |
| --- | --- | --- |
| int | page | 页码 |
| String | filter | 搜索栏() |

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| long | orgId | 机构ID |
| long | id | 唯一标识 |
| long | studentId | 学生ID |
| int | habitType | 习惯类型 |
| String | evaluatorType | 评价者类型 |
| long | evaluatorId | 评价者ID |
| double | scoreChange | 分数变化 |
| String | description | 行为描述 |
| String | evidenceImage | 证据图片 |
| long | recordTime | 记录时间 |
| long | createTime | 创建时间 |

---

### 02详情-HabitRecord习惯评价记录

`GET` **/v2/p/habit_record/:id/**

**Name:** `getHabitRecord`

#### Parameters

| Type | Name | Description |
| --- | --- | --- |
| long | id | id |

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| int | code | 200 |
| long | orgId | 机构ID |
| long | id | 唯一标识 |
| long | studentId | 学生ID |
| int | habitType | 习惯类型 |
| String | evaluatorType | 评价者类型 |
| long | evaluatorId | 评价者ID |
| double | scoreChange | 分数变化 |
| String | description | 行为描述 |
| String | evidenceImage | 证据图片 |
| long | recordTime | 记录时间 |
| long | createTime | 创建时间 |

---

### 03添加-HabitRecord习惯评价记录

`POST` **/v2/p/habit_record/new/**

**Name:** `addHabitRecord`

#### Parameters

| Type | Name | Description |
| --- | --- | --- |
| long | orgId | 机构ID |
| long | id | 唯一标识 |
| long | studentId | 学生ID |
| int | habitType | 习惯类型  对应德智体美劳  evaluationRule下的badge下的id |
| String | evaluatorType | 评价者类型 |
| long | evaluatorId | 评价者ID |
| double | scoreChange | 分数变化 |
| String | description | 行为描述 |
| String | evidenceImage | 证据图片 |
| long | recordTime | 记录时间 |
| long | createTime | 创建时间 |

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| int | code | 200 |

---

### 04更新-HabitRecord习惯评价记录

`POST` **/v2/p/habit_record/:id/**

**Name:** `updateHabitRecord`

#### Parameters

| Type | Name | Description |
| --- | --- | --- |
| long | orgId | 机构ID |
| long | id | 唯一标识 |
| long | studentId | 学生ID |
| int | habitType | 习惯类型 |
| String | evaluatorType | 评价者类型 |
| long | evaluatorId | 评价者ID |
| double | scoreChange | 分数变化 |
| String | description | 行为描述 |
| String | evidenceImage | 证据图片 |
| long | recordTime | 记录时间 |
| long | createTime | 创建时间 |

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| int | code | 200 |

---

### 05删除-习惯评价记录

`POST` **/v2/p/habit_record/**

**Name:** `deleteHabitRecord`

#### Parameters

| Type | Name | Description |
| --- | --- | --- |
| long | id | id |
| String | operation | del时删除 |

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| int | 200 | 成功 |

---

### 06列表-当前用户习惯评价记录

`GET` **/v2/p/habit_record_list_currentUser/**

**Name:** `listHabitRecord`

#### Parameters

| Type | Name | Description |
| --- | --- | --- |
| int | page | 页码 |
| String | filter | 搜索栏() |

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| long | orgId | 机构ID |
| long | id | 唯一标识 |
| long | studentId | 学生ID |
| int | habitType | 习惯类型 |
| String | evaluatorType | 评价者类型 |
| long | evaluatorId | 评价者ID |
| double | scoreChange | 分数变化 |
| String | description | 行为描述 |
| String | evidenceImage | 证据图片 |
| long | recordTime | 记录时间 |
| long | createTime | 创建时间 |

---

### 07列表-习惯评价记录

`GET` **/v2/p/habit_record_list_new/**

**Name:** `listHabitRecord`

#### Parameters

| Type | Name | Description |
| --- | --- | --- |
| int | page | 页码 |
| String | filter | 搜索栏() |

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| long | orgId | 机构ID |
| long | id | 唯一标识 |
| long | studentId | 学生ID |
| String | studentNumber | 学号 |
| String | studentName | 学生姓名 |
| int | habitType | 习惯类型 |
| String | evaluatorType | 评价者类型 |
| long | evaluatorId | 评价者ID |
| double | scoreChange | 分数变化 |
| String | description | 行为描述 |
| String | evidenceImage | 证据图片 |
| long | recordTime | 记录时间 |
| long | createTime | 创建时间 |

---

### 08获取-当前学生习惯评价记录

`GET` **/v2/p/habit_record/current_student/:id/**

**Name:** `getHabitRecordCurrentStudent`

#### Parameters

| Type | Name | Description |
| --- | --- | --- |
| long | id | studentId |

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| int | code | 200 |
| long | orgId | 机构ID |
| long | id | 唯一标识 |
| long | studentId | 学生ID |
| int | habitType | 习惯类型 |
| String | evaluatorType | 评价者类型 |
| long | evaluatorId | 评价者ID |
| double | scoreChange | 分数变化 |
| String | description | 行为描述 |
| String | evidenceImage | 证据图片 |
| long | recordTime | 记录时间 |
| long | createTime | 创建时间 |

---

### 09按班级小组批量添加学生习惯评价记录

`POST` **/v2/p/habit_record/group/new/**

**Name:** `addHabitRecordByGroup`

#### Parameters

| Type | Name | Description |
| --- | --- | --- |
| long | orgId | 机构ID |
| long | id | 唯一标识 |
| long | groupId | 小组ID |
| int | habitType | 习惯类型  对应德智体美劳  evaluationRule下的badge下的id |
| String | evaluatorType | 评价者类型 |
| long | evaluatorId | 评价者ID |
| double | scoreChange | 分数变化 |
| String | description | 行为描述 |
| String | evidenceImage | 证据图片 |
| long | recordTime | 记录时间 |
| long | createTime | 创建时间 |

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| int | code | 200 |

---

## General

### 10查询该学生当月的所有习惯评价记录

`POST` **/v2/p/habit_record/month/list/**

**Name:** `listHabitRecordCurrentUserMonth
`

---

### 11统计当前用户的所在班级的学生当月的所有习惯评价徽章种类*个数

`GET` **/v2/p/habit_record/month/statistics/**

**Name:** `listHabitRecordCurrentUserMonth
`

---

## 家访模块

### 01列表-家访工作记录

`GET` **/v2/p/home_visit_list/**

**Name:** `listHomeVisit`

#### Parameters

| Type | Name | Description |
| --- | --- | --- |
| int | page | 页码 |
| String | filter | 搜索栏() |

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| long | orgId | 机构ID |
| long | id | 唯一标识 |
| long | teacherId | 教师ID |
| long | classId | 班级ID |
| long | studentId | 学生ID |
| int | visitType | 家访类型 |
| String | recordContent | 记录内容 |
| String | caseStudy | 优秀案例 |
| String | caseLevel | 案例评价等级 |
| String | videoEvidence | 视频证据 |
| String | videoLevel | 视频评价等级 |
| int | baseScore | 基础分 |
| int | bonusScore | 加分 |
| int | totalScore | 总分 |
| int | status | 状态 |
| long | visitTime | 家访时间 |
| long | createTime | 创建时间 |

---

### 02详情-HomeVisit家访工作记录

`GET` **/v2/p/home_visit/:id/**

**Name:** `getHomeVisit`

#### Parameters

| Type | Name | Description |
| --- | --- | --- |
| long | id | id |

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| int | code | 200 |
| long | orgId | 机构ID |
| long | id | 唯一标识 |
| long | teacherId | 教师ID |
| long | classId | 班级ID |
| long | studentId | 学生ID |
| int | visitType | 家访类型 |
| String | recordContent | 记录内容 |
| String | caseStudy | 优秀案例 |
| String | caseLevel | 案例评价等级 |
| String | videoEvidence | 视频证据 |
| String | videoLevel | 视频评价等级 |
| int | baseScore | 基础分 |
| int | bonusScore | 加分 |
| int | totalScore | 总分 |
| int | status | 状态 |
| long | visitTime | 家访时间 |
| long | createTime | 创建时间 |

---

### 03添加-HomeVisit家访工作记录

`POST` **/v2/p/home_visit/new/**

**Name:** `addHomeVisit`

#### Parameters

| Type | Name | Description |
| --- | --- | --- |
| long | orgId | 机构ID |
| long | id | 唯一标识 |
| long | teacherId | 教师ID |
| long | classId | 班级ID |
| long | studentId | 学生ID |
| int | visitType | 家访类型 |
| String | recordContent | 记录内容 |
| String | caseStudy | 优秀案例 |
| String | caseLevel | 案例评价等级 |
| String | videoEvidence | 视频证据 |
| String | videoLevel | 视频评价等级 |
| int | baseScore | 基础分 |
| int | bonusScore | 加分 |
| int | totalScore | 总分 |
| int | status | 状态 |
| long | visitTime | 家访时间 |
| long | createTime | 创建时间 |

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| int | code | 200 |

---

### 04更新-HomeVisit家访工作记录

`POST` **/v2/p/home_visit/:id/**

**Name:** `updateHomeVisit`

#### Parameters

| Type | Name | Description |
| --- | --- | --- |
| long | orgId | 机构ID |
| long | id | 唯一标识 |
| long | teacherId | 教师ID |
| long | classId | 班级ID |
| long | studentId | 学生ID |
| int | visitType | 家访类型 |
| String | recordContent | 记录内容 |
| String | caseStudy | 优秀案例 |
| String | caseLevel | 案例评价等级 |
| String | videoEvidence | 视频证据 |
| String | videoLevel | 视频评价等级 |
| int | baseScore | 基础分 |
| int | bonusScore | 加分 |
| int | totalScore | 总分 |
| int | status | 状态 |
| long | visitTime | 家访时间 |
| long | createTime | 创建时间 |

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| int | code | 200 |

---

### 05删除-家访工作记录

`POST` **/v2/p/home_visit/**

**Name:** `deleteHomeVisit`

#### Parameters

| Type | Name | Description |
| --- | --- | --- |
| long | id | id |
| String | operation | del时删除 |

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| int | 200 | 成功 |

---

### 06评审打分

`POST` **/v2/p/home_visit/:id/review/**

**Name:** `reviewHomeVisit`

#### Parameters

| Type | Name | Description |
| --- | --- | --- |
| long | id | id |
| String | caseLevel | 优秀 优秀、良好、一般、不合格 |
| String | videoLevel | 优秀、良好、一般、不合格 |
| double | baseScore | 基础分 |

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| int | 200 | 成功 |

---

## 学生-家长模块

### 01列表-家长学生关系表

`GET` **/v2/p/parent_student_relation_list/**

**Name:** `listParentStudentRelation`

#### Parameters

| Type | Name | Description |
| --- | --- | --- |
| int | page | 页码 |
| String | filter | 搜索栏() |

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| long | orgId | 机构ID |
| long | id | 唯一标识 |
| long | parentId | 家长ID |
| long | studentId | 学生ID |
| String | relationship | 关系类型 |
| long | createTime | 创建时间 |
| long | updateTime | 更新时间 |

---

### 02详情-ParentStudentRelation家长学生关系表

`GET` **/v2/p/parent_student_relation/:id/**

**Name:** `getParentStudentRelation`

#### Parameters

| Type | Name | Description |
| --- | --- | --- |
| long | id | id |

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| int | code | 200 |
| long | orgId | 机构ID |
| long | id | 唯一标识 |
| long | parentId | 家长ID |
| long | studentId | 学生ID |
| String | relationship | 关系类型 |
| long | createTime | 创建时间 |
| long | updateTime | 更新时间 |

---

### 03添加-ParentStudentRelation家长学生关系表

`POST` **/v2/p/parent_student_relation/new/**

**Name:** `addParentStudentRelation`

#### Parameters

| Type | Name | Description |
| --- | --- | --- |
| long | studentId | 学生Id |
| String | relationship | 关系类型（如：爸爸、妈妈等） |
| String | parentPhone | 家长手机号码 |

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| int | code | 200 |

---

### 04更新-ParentStudentRelation家长学生关系表

`POST` **/v2/p/parent_student_relation/:id/**

**Name:** `updateParentStudentRelation`

#### Parameters

| Type | Name | Description |
| --- | --- | --- |
| long | orgId | 机构ID |
| long | id | 唯一标识 |
| long | parentId | 家长ID |
| long | studentId | 学生ID |
| String | relationship | 关系类型 |
| long | createTime | 创建时间 |
| long | updateTime | 更新时间 |

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| int | code | 200 |

---

### 05删除-家长学生关系，以及家长账号

`POST` **/v2/p/parent_student_relation/**

**Name:** `deleteParentStudentRelation`

#### Parameters

| Type | Name | Description |
| --- | --- | --- |
| long | id | 关系ID |
| String | operation | del时删除 |

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| int | 200 | 成功 |

---

### 06 获取当前家长的孩子信息

`POST` **/v2/p/parent_student/**

**Name:** `parentStudentList`

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| int | 200 | 成功 |

#### Response Example

```json
{json} 响应示例:
{
"code": 200,
"list": [
{
"orgId": 1,
"id": 1,
"studentNumber": "20250101",
"name": "王欣瑶",
"classId": 1,
"grade": 1,
"classHg": 1,
"evaluationScheme": 0,
"classAverageScore": 0.0,
"academicScore": 0.0,
"specialtyScore": 0.0,
"habitScore": 16.0,
"points": 16.0,
"totalScore": 0.0,
"badges": null,
"rewardRankGrade": 0,
"rewardRankSchool": 0,
"createTime": 1766727983372,
"updateTime": 1766734215190,
"className": "一年级一班",
"pass": false,
"overAverage": false,
"highGrade": false
}
]
}

```

---

### 06 获取当前家长的孩子所有习惯记录

`POST` **/v2/p/parent_student/habit_record/**

**Name:** `parentStudentList`

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| int | 200 | 成功 |

#### Response Example

```json
{json} 响应示例:
{
"code": 200,
"list": [
{
"orgId": 1,
"id": 1,
"studentId": 1,
"habitType": 1,
"evaluatorType": "teacher",
"evaluatorId": 5,
"scoreChange": 1.0,
"description": "666666",
"evidenceImage": "",
"recordTime": 1766734204254,
"createTime": 1766734215134,
"monthEndTime": 1767196799999,
"status": 0,
"studentName": "王欣瑶"
}
]
}

```

---

### 06 获取当前家长的孩子所有奖项记录

`POST` **/v2/p/parent_student/award_record/**

**Name:** `parentStudentList`

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| int | 200 | 成功 |

#### Response Example

```json
{json} 响应示例:
{
"code": 200,
"list": [
{
"orgId": 1,
"id": 1,
"studentId": 1,
"student": null,
"awardLevel": 0,
"awardGrade": 0,
"competitionName": "四百米跑步",
"category": "个人",
"awardScore": 20.0,
"status": 0,   // 0 待审核 1 审核通过 2 审核未通过
"certificateImage": "",
"badgeAwarded": "",
"awardDate": 1764518400000,
"createTime": 1766735064543,
"updateTime": 1766735064543,
"studentName": "王欣瑶"
}
]
}

```

---

## 班级模块

### 01列表-班级信息

`POST` **/v2/p/school_class_list/**

**Name:** `listSchoolClass`

#### Parameters

| Type | Name | Description |
| --- | --- | --- |
| int | page | 页码 |
| String | filter | 搜索栏() |

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| long | orgId | 机构ID |
| long | id | 唯一标识 |
| String | className | 班级名称 |
| int | grade | 年级 |
| long | headTeacherId | 班主任ID |
| - | - | {ShopAdmin} headTeacher |
| int | studentNum | 人数 |
| double | academicScore | 学业得分总分 |
| double | specialtyScore | 特长得分总分 |
| double | routineScore | 常规得分 |
| double | homeVisitScore | 家访得分 |
| double | totalScore | 总分 |
| boolean | disqualified | 一票否决 |
| double | deductionScore | 扣分 |
| String | honorTitle | 荣誉称号 |
| long | createTime | 创建时间 |
| - | - | {ShopAdmin} teachers |

---

### 02详情-SchoolClass班级信息

`GET` **/v2/p/school_class/:id/**

**Name:** `getSchoolClass`

#### Parameters

| Type | Name | Description |
| --- | --- | --- |
| long | id | id |

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| int | code | 200 |
| long | orgId | 机构ID |
| long | id | 唯一标识 |
| String | className | 班级名称 |
| int | grade | 年级 |
| long | headTeacherId | 班主任ID |
| - | - | {ShopAdmin} headTeacher |
| int | studentNum | 人数 |
| double | academicScore | 学业得分总分 |
| double | specialtyScore | 特长得分总分 |
| double | routineScore | 常规得分 |
| double | homeVisitScore | 家访得分 |
| double | totalScore | 总分 |
| boolean | disqualified | 一票否决 |
| double | deductionScore | 扣分 |
| String | honorTitle | 荣誉称号 |
| long | createTime | 创建时间 |
| - | - | {ShopAdmin} teachers |

---

### 03添加-SchoolClass班级信息

`POST` **/v2/p/school_class/new/**

**Name:** `addSchoolClass`

#### Parameters

| Type | Name | Description |
| --- | --- | --- |
| long | orgId | 机构ID |
| long | id | 唯一标识 |
| String | className | 班级名称 |
| - | - | {ShopAdmin} headTeacher |
| int | studentNum | 人数 |
| double | academicScore | 学业得分总分 |
| double | specialtyScore | 特长得分总分 |
| double | routineScore | 常规得分 |
| double | homeVisitScore | 家访得分 |
| double | totalScore | 总分 |
| boolean | disqualified | 一票否决 |
| double | deductionScore | 扣分 |
| String | honorTitle | 荣誉称号 |
| long | createTime | 创建时间 |
| - | - | {ShopAdmin} teachers |

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| int | code | 200 |

---

### 04更新-SchoolClass班级信息

`POST` **/v2/p/school_class/:id/**

**Name:** `updateSchoolClass`

#### Parameters

| Type | Name | Description |
| --- | --- | --- |
| long | orgId | 机构ID |
| long | id | 唯一标识 |
| String | className | 班级名称 |
| int | grade | 年级 |
| long | headTeacherId | 班主任ID |
| - | - | {ShopAdmin} headTeacher |
| int | studentNum | 人数 |
| double | academicScore | 学业得分总分 |
| double | specialtyScore | 特长得分总分 |
| double | routineScore | 常规得分 |
| double | homeVisitScore | 家访得分 |
| double | totalScore | 总分 |
| boolean | disqualified | 一票否决 |
| double | deductionScore | 扣分 |
| String | honorTitle | 荣誉称号 |
| long | createTime | 创建时间 |
| - | - | {ShopAdmin} teachers |

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| int | code | 200 |

---

### 05删除-班级信息

`POST` **/v2/p/school_class/**

**Name:** `deleteSchoolClass`

#### Parameters

| Type | Name | Description |
| --- | --- | --- |
| long | id | id |
| String | operation | del时删除 |

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| int | 200 | 成功 |

---

### 06设置班主任

`POST` **/v2/p/school_class/:id/set_head_teacher/**

**Name:** `setHeadTeacher`

#### Parameters

| Type | Name | Description |
| --- | --- | --- |
| long | id | 班级ID |
| String | subject | 任教科目（可选） |
| long | teacherId | 教师ID |

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| int | 200 | 成功 |

---

### 07添加科任教师

`POST` **/v2/p/school_class/:id/add_teacher/**

**Name:** `addClassTeacher`

#### Parameters

| Type | Name | Description |
| --- | --- | --- |
| long | id | 班级ID |
| long | teacherId | 教师ID |
| String | subject | 任教科目 |
| int | teachingHours | 周课时数（可选） |
| String | responsibility | 职责描述（可选） |

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| int | 200 | 成功 |

---

### 08移除班级教师

`POST` **/v2/p/school_class/:id/remove_teacher/**

**Name:** `removeClassTeacher`

#### Parameters

| Type | Name | Description |
| --- | --- | --- |
| long | id | 班级ID |
| long | teacherId | 教师ID |

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| int | 200 | 成功 |

---

### 09获取班级教师列表

`GET` **/v2/p/school_class/:id/teachers/**

**Name:** `getClassTeachers`

#### Parameters

| Type | Name | Description |
| --- | --- | --- |
| long | id | 班级ID |

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| int | code | 200 |
| Array | teachers | 教师列表 |
| Object | headTeacher | 班主任信息 |

---

### 10获取班主任信息

`GET` **/v2/p/school_class/:id/head_teacher/**

**Name:** `getHeadTeacher`

#### Parameters

| Type | Name | Description |
| --- | --- | --- |
| long | id | 班级ID |

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| int | code | 200 |
| Object | headTeacher | 班主任信息 |

---

## 特长获奖模块

### 01列表-特长获奖记录

`GET` **/v2/p/specialty_award_list/**

**Name:** `listSpecialtyAward`

#### Parameters

| Type | Name | Description |
| --- | --- | --- |
| int | page | 页码 |
| String | filter | 搜索栏() |

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| long | orgId | 机构ID |
| long | id | 唯一标识 |
| long | studentId | 学生ID |
| - | - | {Student} student |
| int | awardLevel | 奖项级别 |
| int | awardGrade | 奖项等级 |
| String | competitionName | 竞赛名称 |
| String | category | 比赛类别 |
| double | awardScore | 奖项得分 |
| int | status | 审核状态 |
| String | certificateImage | 证书图片 |
| String | badgeAwarded | 授予徽章 |
| long | awardDate | 获奖时间 |
| long | createTime | 创建时间 |
| long | updateTime | 更新时间 |

---

### 02详情-SpecialtyAward特长获奖记录

`GET` **/v2/p/specialty_award/:id/**

**Name:** `getSpecialtyAward`

#### Parameters

| Type | Name | Description |
| --- | --- | --- |
| long | id | id |

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| int | code | 200 |
| long | orgId | 机构ID |
| long | id | 唯一标识 |
| long | studentId | 学生ID |
| - | - | {Student} student |
| int | awardLevel | 奖项级别 |
| int | awardGrade | 奖项等级 |
| String | competitionName | 竞赛名称 |
| String | category | 比赛类别 |
| double | awardScore | 奖项得分 |
| int | status | 审核状态 |
| String | certificateImage | 证书图片 |
| String | badgeAwarded | 授予徽章 |
| long | awardDate | 获奖时间 |
| long | createTime | 创建时间 |
| long | updateTime | 更新时间 |

---

### 01添加-SpecialtyAward特长获奖记录

`POST` **/v2/p/specialty_award/new/**

**Name:** `addSpecialtyAward`

#### Parameters

| Type | Name | Description |
| --- | --- | --- |
| long | orgId | 机构ID |
| long | id | 唯一标识 |
| long | studentId | 学生ID |
| - | - | {Student} student |
| int | awardLevel | 奖项级别 |
| int | awardGrade | 奖项等级 |
| String | competitionName | 竞赛名称 |
| String | category | 比赛类别 |
| double | awardScore | 奖项得分 |
| int | status | 审核状态 |
| String | certificateImage | 证书图片 |
| String | badgeAwarded | 授予徽章 |
| long | awardDate | 获奖时间 |
| long | createTime | 创建时间 |
| long | updateTime | 更新时间 |

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| int | code | 200 |

---

### 04更新-SpecialtyAward特长获奖记录

`POST` **/v2/p/specialty_award/:id/**

**Name:** `updateSpecialtyAward`

#### Parameters

| Type | Name | Description |
| --- | --- | --- |
| long | orgId | 机构ID |
| long | id | 唯一标识 |
| long | studentId | 学生ID |
| - | - | {Student} student |
| int | awardLevel | 奖项级别 |
| int | awardGrade | 奖项等级 |
| String | competitionName | 竞赛名称 |
| String | category | 比赛类别 |
| double | awardScore | 奖项得分 |
| int | status | 审核状态 |
| String | certificateImage | 证书图片 |
| String | badgeAwarded | 授予徽章 |
| long | awardDate | 获奖时间 |
| long | createTime | 创建时间 |
| long | updateTime | 更新时间 |

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| int | code | 200 |

---

### 05删除-特长获奖记录

`POST` **/v2/p/specialty_award/**

**Name:** `deleteSpecialtyAward`

#### Parameters

| Type | Name | Description |
| --- | --- | --- |
| long | id | id |
| String | operation | del时删除 |

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| int | 200 | 成功 |

---

### 06审核特长获奖记录

`POST` **/v2/p/specialty_award_judge/**

**Name:** `specialtyAwardJudge`

#### Parameters

| Type | Name | Description |
| --- | --- | --- |
| long | id | 唯一标识 |
| long | opinion | 审核意见（1：通过，2：不通过） |

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| int | code | 200 |

---

### 07 获取当前学生特长获奖记录

`GET` **/v2/p/specialty_award/current_user/:id/**

**Name:** `getSpecialtyAwardCurrentUser`

#### Parameters

| Type | Name | Description |
| --- | --- | --- |
| long | id | student_id |

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| int | code | 200 |
| long | orgId | 机构ID |
| long | id | 唯一标识 |
| long | studentId | 学生ID |
| - | - | {Student} student |
| int | awardLevel | 奖项级别 |
| int | awardGrade | 奖项等级 |
| String | competitionName | 竞赛名称 |
| String | category | 比赛类别 |
| double | awardScore | 奖项得分 |
| int | status | 审核状态 |
| String | certificateImage | 证书图片 |
| String | badgeAwarded | 授予徽章 |
| long | awardDate | 获奖时间 |
| long | createTime | 创建时间 |
| long | updateTime | 更新时间 |

---

## 学生模块

### 01列表-学生（系统会根据登录的对应账号返回对应的学生列表）前后台

`POST` **/v2/p/student_list/**

**Name:** `listStudent`

#### Parameters

| Type | Name | Description |
| --- | --- | --- |
| int | page | 页码  //0-全查  1-分页 |
| String | studentName | 学生姓名 |

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| long | orgId | 机构ID |
| long | id | 唯一标识 |
| String | studentNumber | 学号 |
| String | name | 学生姓名 |
| long | classId | 班级ID |
| int | grade | 年级 |
| int | evaluationScheme | 评价方案 |
| double | classAverageScore | 班级平均分 |
| double | academicScore | 学业得分 |
| double | specialtyScore | 特长得分 |
| double | habitScore | 习惯得分 |
| double | totalScore | 总分 |
| String | badges | 获得徽章 |
| long | createTime | 创建时间 |
| long | updateTime | 更新时间 |

#### Request Example

```json
{json} 请求示例:
{
"page":1,
"studentName":"张三"
}
```

#### Response Example

```json
{json} 响应示例:
{
"pages": 108,
"hasNest": true,
"code": 200,
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
},
{
"orgId": 1,
"id": 2173,
"studentNumber": "20200753",
"name": "庄芯媛",
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
"createTime": 1766649180707,
"updateTime": 1766649180707,
"className": "六年级七班",
"overAverage": false,
"highGrade": true,
"pass": false
},
]
}

```

---

### 02详情-Student学生（前后台）

`GET` **/v2/p/student/:id/**

**Name:** `getStudent`

#### Parameters

| Type | Name | Description |
| --- | --- | --- |
| long | id | id |

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| int | code | 200 |
| long | orgId | 机构ID |
| long | id | 唯一标识 |
| String | studentNumber | 学号 |
| String | name | 学生姓名 |
| long | classId | 班级ID |
| int | grade | 年级 |
| int | evaluationScheme | 评价方案 |
| double | classAverageScore | 班级平均分 |
| double | academicScore | 学业得分 |
| double | specialtyScore | 特长得分 |
| double | habitScore | 习惯得分 |
| double | totalScore | 总分 |
| String | badges | 获得徽章 |
| long | createTime | 创建时间 |
| long | updateTime | 更新时间 |

---

### 03添加-Student学生

`POST` **/v2/p/student/new/**

**Name:** `addStudent`

#### Parameters

| Type | Name | Description |
| --- | --- | --- |
| long | orgId | 机构ID |
| long | id | 唯一标识 |
| String | studentNumber | 学号 |
| String | name | 学生姓名 |
| long | classId | 班级ID |
| int | grade | 年级 |
| int | evaluationScheme | 评价方案 |
| double | classAverageScore | 班级平均分 |
| double | academicScore | 学业得分 |
| double | specialtyScore | 特长得分 |
| double | habitScore | 习惯得分 |
| double | totalScore | 总分 |
| String | badges | 获得徽章 |
| long | createTime | 创建时间 |
| long | updateTime | 更新时间 |

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| int | code | 200 |

---

### 04更新-Student学生

`POST` **/v2/p/student/:id/**

**Name:** `updateStudent`

#### Parameters

| Type | Name | Description |
| --- | --- | --- |
| long | orgId | 机构ID |
| long | id | 唯一标识 |
| String | studentNumber | 学号 |
| String | name | 学生姓名 |
| long | classId | 班级ID |
| int | grade | 年级 |
| int | evaluationScheme | 评价方案 |
| double | classAverageScore | 班级平均分 |
| double | academicScore | 学业得分 |
| double | specialtyScore | 特长得分 |
| double | habitScore | 习惯得分 |
| double | totalScore | 总分 |
| String | badges | 获得徽章 |
| long | createTime | 创建时间 |
| long | updateTime | 更新时间 |

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| int | code | 200 |

---

### 05删除-学生

`POST` **/v2/p/student/**

**Name:** `deleteStudent`

#### Parameters

| Type | Name | Description |
| --- | --- | --- |
| long | id | id |
| String | operation | del时删除 |

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| int | 200 | 成功 |

---

### 06导入学生文件(按班的)

`POST` **/v2/p/student_excel/**

**Name:** `studentImport`

#### Parameters

| Type | Name | Description |
| --- | --- | --- |
| file | file | 学生文件 |
| long | classId | 班级ID |

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| int | 200 | 成功 |

---

### 07导出学生导入模板

`GET` **/v2/p/student_excel_template/**

**Name:** `exportStudentTemplate`

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| file | Excel文件 | 导入模板文件 |

---

### 08创建学生家长关系

`POST` **/v2/p/student_parent/**

**Name:** `createStudentParent`

#### Parameters

| Type | Name | Description |
| --- | --- | --- |
| long | studentId | 学生ID |
| String | parentPhone | 家长手机号 |
| String | relationship | 关系类型（父亲/母亲/爷爷/奶奶等） |

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| int | 200 | 成功 |

---

### 09获取学生家长列表

`GET` **/v2/p/student/parents/:id/**

**Name:** `getStudentParents`

#### Parameters

| Type | Name | Description |
| --- | --- | --- |
| long | id | 学生ID |

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| int | code | 200 |
| Array | parents | 家长列表 |

---

### 10删除学生家长关系

`POST` **/v2/p/student_parent/:id/**

**Name:** `deleteStudentParent`

#### Parameters

| Type | Name | Description |
| --- | --- | --- |
| long | id | 家长关系ID |
| String | operation | del时删除 |

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| int | 200 | 成功 |

---

### 11批量分配学生到班级

`POST` **/v2/p/student_batch_assign/**

**Name:** `batchAssignStudents`

#### Parameters

| Type | Name | Description |
| --- | --- | --- |
| Array | studentIds | 学生ID数组 |
| long | classId | 目标班级ID |

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| int | 200 | 成功 |

---

### 12列表-当前用户的所在班级的学生列表

`GET` **/v2/p/student_list_class_currentUser/**

**Name:** `listStudentCurrentUser`

#### Parameters

| Type | Name | Description |
| --- | --- | --- |
| long | classId | 班级ID |

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| long | orgId | 机构ID |
| long | id | 唯一标识 |
| String | studentNumber | 学号 |
| String | name | 学生姓名 |
| long | classId | 班级ID |
| int | grade | 年级 |
| int | evaluationScheme | 评价方案 |
| double | classAverageScore | 班级平均分 |
| double | academicScore | 学业得分 |
| double | specialtyScore | 特长得分 |
| double | habitScore | 习惯得分 |
| double | totalScore | 总分 |
| String | badges | 获得徽章 |
| long | createTime | 创建时间 |
| long | updateTime | 更新时间 |

---

### 13导入学生文件(全校)

`POST` **/v2/p/student_excel_school/**

**Name:** `studentImportSchool`

#### Parameters

| Type | Name | Description |
| --- | --- | --- |
| file | file | 学生文件 |

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| int | 200 | 成功 |

---

## ADMIN_MEMBER

### 01获取用户列表

`POST` **/v2/s/members/?page=&uid=&filter=**

**Name:** `listMembers`

#### Parameters

| Type | Name | Description |
| --- | --- | --- |
| long | uid | uid |
| long | orgId | orgId |
| long | shopId | shopId |
| int | page | page |
| String | filter | realName/nickName/phoneNumber/dealerCode |
| int | status | 0all 1normal 2pause |

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| int | code | 200 |
| int | pages | 分页 |
| JsonArray | list | 用户列表 |
| long | id | 用户ID |
| int | status | 用户状态1正常2锁定 |
| string | realName | 实名 |
| string | nickName | 昵称 |
| string | phoneNumber | 手机号 |
| string | description | 备注 |
| string | agentCode | 代理编号 |
| string | updateTime | 更新时间 |
| string | createdTime | 创建时间 |

---

### 02获取用户详情

`GET` **/v2/s/members/:memberId/**

**Name:** `getUser`

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| int | code | 200 |
| int | pages | 分页 |
| JsonArray | list | 用户列表 |
| long | id | 用户ID |
| int | status | 用户状态1正常2锁定 |
| string | realName | 实名 |
| string | nickName | 昵称 |
| string | phoneNumber | 手机号 |
| string | description | 备注 |
| long | birthday | 生日 |
| String | idCardNo | 身份证号 |
| String | licenseNo | 营业执照 |
| String | licenseImgUrl | 营业执照图片地址 |
| string | agentCode | 代理编号 |
| string | idCardNo | 身份证号码 |
| string | licenseNo | 营业执照 |
| int | gender | 0：未知、1：男、2：女 |
| String | city | 城市 |
| String | province | 省份 |
| String | country | 国家 |
| String | shopName | 店铺 |
| String | contactPhoneNumber | 联系电话 |
| String | contactAddress | 联系地址 |
| String | businessItems | 经营类目 |
| String | images | 图片，多张，以逗号隔开 |
| string | createdTime | 创建时间 |

---

### 03锁定/解锁用户

`POST` **/v2/s/members/status/**

**Name:** `setMemberStatus`

#### Parameters

| Type | Name | Description |
| --- | --- | --- |
| long | memberId | 用户ID |
| int | status | 1正常，2锁定 |

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| int | code | 200 |
| int | code | 40001 用户不存在 |

---

### 13修改用户信息

`POST` **/v2/s/members/:uid/**

**Name:** `updateMember`

#### Parameters

| Type | Name | Description |
| --- | --- | --- |
| long | uid | 用户ID |
| int | [status] | 1正常，2锁定 |
| int | [dealerType] | 1正常，2锁定 |
| string | [realName] | 真实姓名 |
| string | [nickName] | 昵称 |
| string | [phoneNumber] | 手机号 |
| string | [physicalNumber] | 物理卡号 |
| string | [logicalNumber] | 逻辑卡号 |
| string | [cardPassword] | 卡密 |
| string | [vin] | 车架号 |
| string | [carNo] | 车牌号 |

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| int | code | 200 |
| int | code | 40001 用户不存在 |

---

### 14业务员归属客户列表

`POST` **/v2/s/dealer_customers/**

**Name:** `listCustomers`

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| int | code | 200 |
| int | pages | 分页 |
| JsonArray | list | 用户列表 |
| double | totalOrderMoney | totalOrderMoney |

---

## A

### 测试接口

`GET` **/v2/s/test/**

**Name:** `test`

#### Success Response

| Type | Name | Description |
| --- | --- | --- |
| String | msg | 测试成功 |

---

