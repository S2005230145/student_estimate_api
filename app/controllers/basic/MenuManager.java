package controllers.basic;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import controllers.BaseSecurityController;
import io.ebean.DB;
import io.ebean.ExpressionList;
import models.admin.*;
import play.Logger;
import play.db.ebean.Transactional;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Http;
import play.mvc.Result;
import utils.BizUtils;
import utils.ValidationUtil;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.stream.Collectors;

/**
 * 菜单管理
 */
public class MenuManager extends BaseSecurityController {
    @Inject
    BizUtils bizUtils;

    Logger.ALogger logger = Logger.of(MenuManager.class);

    /**
     * @api {GET} /v2/s/menu/?name=&parentId=  01菜单列表
     * @apiName listMenu
     * @apiGroup ADMIN-MENU
     * @apiSuccess (Success 200) {int} code 200 请求成功
     * @apiSuccess (Success 200) {JsonArray} list 菜单列表
     * @apiSuccess (Success 200) {long} id id
     * @apiSuccess (Success 200) {int} sort 排序 降序
     * @apiSuccess (Success 200) {boolean} enable 是否启用
     * @apiSuccess (Success 200){String} path 前端路径
     * @apiSuccess (Success 200){String} name 名称
     * @apiSuccess (Success 200){String} component 组件
     * @apiSuccess (Success 200){String} redirect 重定向地址
     * @apiSuccess (Success 200){String} title 标题
     * @apiSuccess (Success 200){String} icon 图标
     * @apiSuccess (Success 200){String} activeMenu 菜单路径，前端用
     * @apiSuccess (Success 200){boolean} noCache 是否缓存 true缓存，false不缓存
     * @apiSuccess (Success 200){String} relativePath 菜单上下级，用于菜单之间的关系
     * @apiSuccess (Success 200){long} parentId 父级菜单ID
     * @apiSuccess (Success 200){long} createTime 创建时间
     */
    public CompletionStage<Result> listMenu(final String name, int parentId) {
        return CompletableFuture.supplyAsync(() -> {
            ExpressionList<Menu> expressionList = Menu.find.query().where();
            if (!ValidationUtil.isEmpty(name)) expressionList.icontains("name", name);
            if (parentId > 0) expressionList.eq("parentId", parentId);
            List<Menu> list = expressionList
                    .orderBy().asc("relativePath")
                    .orderBy().desc("sort")
                    .findList();
            ObjectNode result = Json.newObject();
            result.put(CODE, CODE200);
            List<Menu> resultList = convertListToTreeNode(list);
            result.set("list", Json.toJson(resultList));
            return ok(result);
        });
    }

    public List<Menu> convertListToTreeNode(List<Menu> menuList) {
        List<Menu> nodeList = new ArrayList<>();
        if (null == menuList) return nodeList;
        for (Menu menu : menuList) {
            if (null != menu) {
                Meta meta = new Meta();
                meta.title = menu.title;
                meta.icon = menu.icon;
                meta.noCache = menu.noCache;
                menu.meta = meta;
                if (!ValidationUtil.isEmpty(menu.relativePath) && menu.relativePath.equalsIgnoreCase("/")) {
                    //根目录
                    nodeList.add(menu);
                } else {
                    updateMenuChildren(menu, menuList);
                }
            }
        }
        return nodeList;
    }

    private void updateMenuChildren(Menu menu, List<Menu> nodeList) {
        for (Menu parentMenu : nodeList) {
            if (null != parentMenu && menu.parentId == parentMenu.id) {
                if (parentMenu.children == null) parentMenu.children = new ArrayList<>();
                parentMenu.children.add(menu);
                break;
            }
        }
    }


    /**
     * @api {GET} /v2/s/menu/:id/ 02菜单详情
     * @apiName getMenu
     * @apiGroup ADMIN-MENU
     * @apiSuccess (Success 200) {long} id id
     * @apiSuccess (Success 200) {int} sort 排序 降序
     * @apiSuccess (Success 200) {boolean} enable 是否启用
     * @apiSuccess (Success 200){String} path 前端路径
     * @apiSuccess (Success 200){String} name 分类名称
     * @apiSuccess (Success 200){String} component 组件
     * @apiSuccess (Success 200){String} redirect 重定向地址
     * @apiSuccess (Success 200){String} title 标题
     * @apiSuccess (Success 200){String} icon 图标
     * @apiSuccess (Success 200){String} activeMenu 菜单路径，前端用
     * @apiSuccess (Success 200){boolean} noCache 是否缓存 true缓存，false不缓存
     * @apiSuccess (Success 200){String} relativePath 菜单上下级，用于菜单之间的关系
     * @apiSuccess (Success 200){long} createTime 创建时间
     */
    public CompletionStage<Result> getMenu(Http.Request request,int id) {
        return CompletableFuture.supplyAsync(() -> {
            if (id < 1) return okCustomJson(request,CODE40001, "base.argument.error");
            Menu menu = Menu.find.byId(id);
            if (null == menu) return okCustomJson(request,CODE40002, "menu.not.exist");
            List<Menu> children = Menu.find.query().where().eq("parentId", id).findList();
            menu.children = children;
            ObjectNode result = (ObjectNode) Json.toJson(menu);
            result.put(CODE, CODE200);
            return ok(result);
        });
    }

    /**
     * @api {POST} /v2/s/menu/new/ 03添加菜单
     * @apiName addMenu
     * @apiGroup ADMIN-MENU
     * @apiParam {int} sort 排序值
     * @apiParam {String} path 前端路径
     * @apiParam {String} name 分类名称
     * @apiParam {String} component 组件
     * @apiParam {String} redirect 定向地址
     * @apiParam {String} title 标题
     * @apiParam {String} icon 图标
     * @apiParam {String} activeMenu 菜单路径，前端用
     * @apiParam {boolean} noCache 是否缓存 true缓存，false不缓存
     * @apiParam {long} parentId 父类id
     * @apiSuccess (Error 40001) {int} code 40001 参数错误
     */
    @BodyParser.Of(BodyParser.Json.class)
    @Transactional
    public CompletionStage<Result> addMenu(Http.Request request) {
        JsonNode requestNode = request.body().asJson();
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((shopAdmin) -> {
            if (null == shopAdmin || shopAdmin.orgId < 1) return unauth503(request);
            Menu param = Json.fromJson(requestNode, Menu.class);
            if (ValidationUtil.isEmpty(param.title)) return okCustomJson(request,CODE40001, "menu.input.title");
            if (ValidationUtil.isEmpty(param.component)) return okCustomJson(request,CODE40001, "menu.input.module.name");
            if (ValidationUtil.isEmpty(param.path)) return okCustomJson(request,CODE40001, "menu.input.address");

            Menu titleMenu = Menu.find.query().where().eq("title", param.title).setMaxRows(1).findOne();
            if (null != titleMenu) return okCustomJson(request,CODE40001, "menu.title.exist");

            Menu parentMerchantCategory = null;
            if (param.parentId > 0) {
                parentMerchantCategory = Menu.find.byId(param.parentId);
                if (null == parentMerchantCategory) return okCustomJson(request,CODE40001, "menu.father.id.not.exist");
            }
            if (null != parentMerchantCategory) {
                String parentPath = parentMerchantCategory.relativePath;
                if (ValidationUtil.isEmpty(parentPath)) parentPath = "/";
                param.setRelativePath(parentPath + parentMerchantCategory.id + "/");
            } else param.setRelativePath("/");

            long currentTime = dateUtils.getCurrentTimeByMilliSecond();
            if (param.parentId == 0) param.parentId = 1;
            param.setCreateTime(currentTime);
            param.setEnable(true);
            param.setNoCache(true);
            param.save();
            String prefixMenuUpdate = bizUtils.getMessageValue(request, "prefix.menu.update");
            businessUtils.addOperationLog(request, shopAdmin, prefixMenuUpdate + requestNode.toString());
            ObjectNode resultNode = (ObjectNode) Json.toJson(param);
            resultNode.put(CODE, CODE200);
            return ok(resultNode);
        });
    }

    /**
     * @api {POST} /v2/s/menu/:id/ 04修改菜单
     * @apiName updateMenu
     * @apiGroup ADMIN-MENU
     * @apiParam {String} path 前端路径
     * @apiParam {String} name 分类名称
     * @apiParam {String} component 组件
     * @apiParam {String} redirect 定向地址
     * @apiParam {String} title 标题
     * @apiParam {String} icon 图标
     * @apiParam {String} activeMenu 菜单路径，前端用
     * @apiParam {boolean} noCache 是否缓存 true缓存，false不缓存
     * @apiParam {long} parentId 父类id
     * @apiParam {int} sort 排序值
     * @apiSuccess (Success 200) {int} code 200 请求成功
     * @apiSuccess (Error 40001) {int} code 40001 参数错误
     */
    @BodyParser.Of(BodyParser.Json.class)
    @Transactional
    public CompletionStage<Result> updateMenu(Http.Request request, int id) {
        JsonNode requestNode = request.body().asJson();
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((shopAdmin) -> {
            if (null == shopAdmin || shopAdmin.orgId < 1) return unauth503(request);
            Menu menu = Menu.find.byId(id);
            if (null == menu) return okCustomJson(request,CODE40002, "menu.not.exist");
            Menu param = Json.fromJson(requestNode, Menu.class);
            if (null == param) return okCustomJson(request,CODE40001, "base.argument.error");
            if (id == param.parentId) return okCustomJson(request,CODE40002, "menu.child.like.father.error");
            if (requestNode.has("parentId")) {
                if (param.parentId == 0) param.parentId = 1;
                setMenuPath(menu, param.parentId);
            }
            if (param.sort > 0) menu.setSort(param.sort);
            if (requestNode.has("enable")) menu.setEnable(param.enable);
            if (requestNode.has("noCache")) menu.setNoCache(param.noCache);
            if (!ValidationUtil.isEmpty(param.path)) menu.setPath(param.path);
            if (!ValidationUtil.isEmpty(param.component)) menu.setComponent(param.component);
            if (!ValidationUtil.isEmpty(param.redirect)) menu.setRedirect(param.redirect);
            if (!ValidationUtil.isEmpty(param.name)) menu.setName(param.name);
            if (!ValidationUtil.isEmpty(param.activeMenu)) menu.setActiveMenu(param.activeMenu);
            if (!ValidationUtil.isEmpty(param.title) && !param.title.equalsIgnoreCase(menu.title)) {
                Menu titleMenu = Menu.find.query().where()
                        .eq("title", param.title)
                        .ne("id", menu.id)
                        .setMaxRows(1).findOne();
                if (null != titleMenu) return okCustomJson(request,CODE40001, "menu.title.exist");
                menu.setTitle(param.title);
            }
            if (!ValidationUtil.isEmpty(param.icon)) menu.setIcon(param.icon);
            if (requestNode.has("hidden")) menu.setHidden(param.hidden);
            menu.save();
            String prefixMenuUpdate = bizUtils.getMessageValue(request, "prefix.menu.update");
            businessUtils.addOperationLog(request, shopAdmin, prefixMenuUpdate + requestNode.toString());
            return okJSON200();
        });
    }

    private void setMenuPath(Menu category, int parentId) {
        if (parentId > -1) {
            category.setParentId(parentId);
            if (parentId > 0) {
                Menu parentMenu = Menu.find.byId(parentId);
                if (null != parentMenu) {
                    String parentPath = parentMenu.relativePath;
                    if (ValidationUtil.isEmpty(parentPath)) parentPath = "/";
                    category.setRelativePath(parentPath + parentMenu.id + "/");
                }
            } else category.setRelativePath("/");
        }
    }

    /**
     * @api {POST} /v2/s/menu/ 05删除菜单
     * @apiName deleteMenu
     * @apiGroup ADMIN-MENU
     * @apiParam {int} id 菜单id
     * @apiParam {String} operation 操作,"del"为删除
     * @apiSuccess (Success 200) {int} code 200 请求成功
     * @apiSuccess (Error 40001) {int} code 40001 参数错误
     * @apiSuccess (Error 40002) {int} code 40002 该菜单不存在
     * @apiSuccess (Error 40003) {int} code 40003 该菜单为父级分类,不能直接删除
     */
    @BodyParser.Of(BodyParser.Json.class)
    @Transactional
    public CompletionStage<Result> deleteMenu(Http.Request request) {
        JsonNode jsonNode = request.body().asJson();
        String operation = jsonNode.findPath("operation").asText();
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((shopAdmin) -> {
            if (null == shopAdmin || shopAdmin.orgId < 1) return unauth503(request);
            if (ValidationUtil.isEmpty(operation) || !operation.equals("del"))
                return okCustomJson(request,CODE40001, "base.argument.error");
            int id = jsonNode.findPath("id").asInt();
            if (id < 1) return okCustomJson(request,CODE40001, "base.argument.error");
            Menu menu = Menu.find.byId(id);
            if (null == menu) return okCustomJson(request,CODE40001, "menu.not.exist");
            Menu subCategories = Menu.find.query().where().eq("parentId", menu.id).setMaxRows(1).findOne();
            if (null != subCategories) return okCustomJson(request,CODE40003, "menu.father.direct.del.error");
            menu.delete();
            businessUtils.addOperationLog(request, shopAdmin, "删除菜单：" + jsonNode.toString());
            return okJSON200();
        });
    }

    /**
     * @api {POST} /v2/s/batch_update_menu_to_group/:groupId/ 06批量修改角色菜单
     * @apiName batchAddGroupMenu
     * @apiGroup ADMIN-MENU
     * @apiParam {JsonArray} list menuId的数组
     * @apiSuccess (Success 200){int} code 200
     */
    @BodyParser.Of(BodyParser.Json.class)
    @Transactional
    public CompletionStage<Result> batchUpdateGroupMenu(Http.Request request, int groupId) {
        JsonNode jsonNode = request.body().asJson();
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((shopAdmin) -> {
            if (null == shopAdmin || shopAdmin.orgId < 1) return unauth503(request);
            Group group = Group.find.byId(groupId);
            if (null == group) return okCustomJson(request,CODE40002, "menu.role.group.not.exist");
            ArrayNode list = (ArrayNode) jsonNode.findPath("list");
            if (null != list && list.size() > 0) {
                List<GroupMenu> menus = new ArrayList<>();
                if (list.size() > 0) {
                    list.forEach((node) -> {
                        GroupMenu groupMenu = new GroupMenu();
                        groupMenu.setGroupId(groupId);
                        groupMenu.setMenuId(node.asInt());
                        menus.add(groupMenu);
                    });
                    if (list.size() > 0) {
                        //删除旧的
                        List<GroupMenu> oldGroupMenu = GroupMenu.find.query().where()
                                .eq("groupId", groupId).findList();
                        if (oldGroupMenu.size() > 0) DB.deleteAll(oldGroupMenu);
                        DB.saveAll(menus);
                    }
                }
            }
            String prefixMenuRoleBachUpdate = bizUtils.getMessageValue(request, "menu.role.batch.update");
            businessUtils.addOperationLog(request, shopAdmin, prefixMenuRoleBachUpdate + jsonNode.toString());
            return okJSON200();
        });
    }


    /**
     * @api {GET} /v2/s/group_menu/?groupId=  07角色菜单列表
     * @apiName listGroupMenu
     * @apiGroup ADMIN-MENU
     * @apiSuccess (Success 200) {int} code 200 请求成功
     * @apiSuccess (Success 200) {JsonArray} list 菜单列表
     * @apiSuccess (Success 200) {long} id id
     * @apiSuccess (Success 200) {int} sort 排序 降序
     * @apiSuccess (Success 200) {boolean} enable 是否启用
     * @apiSuccess (Success 200){String} path 前端路径
     * @apiSuccess (Success 200){String} name 名称
     * @apiSuccess (Success 200){String} component 组件
     * @apiSuccess (Success 200){String} redirect 重定向地址
     * @apiSuccess (Success 200){String} title 标题
     * @apiSuccess (Success 200){String} icon 图标
     * @apiSuccess (Success 200){boolean} noCache 是否缓存 true缓存，false不缓存
     * @apiSuccess (Success 200){String} relativePath 菜单上下级，用于菜单之间的关系
     * @apiSuccess (Success 200){long} parentId 父级菜单ID
     * @apiSuccess (Success 200){long} createTime 创建时间
     */
    public CompletionStage<Result> listGroupMenu(Http.Request request,int groupId) {
        return CompletableFuture.supplyAsync(() -> {
            if (groupId < 1) return okCustomJson(request,CODE40001, "base.id.error");
            List<GroupMenu> list = GroupMenu.find.query().where().eq("groupId", groupId)
                    .orderBy().asc("id").findList();
            ObjectNode result = Json.newObject();
            result.put(CODE, CODE200);
            result.set("list", Json.toJson(list));
            return ok(result);
        });
    }


    /**
     * @api {GET} /v2/s/member_menu/  08获取用户菜单列表
     * @apiName getMemberMenu
     * @apiGroup ADMIN-MENU
     * @apiSuccess (Success 200) {int} code 200 请求成功
     * @apiSuccess (Success 200) {JsonArray} list 菜单列表
     * @apiSuccess (Success 200) {long} id id
     * @apiSuccess (Success 200) {int} sort 排序 降序
     * @apiSuccess (Success 200) {boolean} enable 是否启用
     * @apiSuccess (Success 200){String} path 前端路径
     * @apiSuccess (Success 200){String} name 名称
     * @apiSuccess (Success 200){String} component 组件
     * @apiSuccess (Success 200){String} redirect 重定向地址
     * @apiSuccess (Success 200){String} title 标题
     * @apiSuccess (Success 200){String} icon 图标
     * @apiSuccess (Success 200){boolean} noCache 是否缓存 true缓存，false不缓存
     * @apiSuccess (Success 200){String} relativePath 菜单上下级，用于菜单之间的关系
     * @apiSuccess (Success 200){long} parentId 父级菜单ID
     * @apiSuccess (Success 200){long} createTime 创建时间
     */
    public CompletionStage<Result> getMemberMenu(Http.Request request) {
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((shopAdmin) -> {
            if (null == shopAdmin || shopAdmin.orgId < 1) return unauth503(request);
//            admin.id = 54;
//            long id = 54;
            //TODO bug解决之后需要加缓存
            List<GroupUser> groupUserList = GroupUser.find.query().where()
                    .eq("memberId", shopAdmin.id)
                    .orderBy().asc("id")
                    .findList();
            Set<Integer> set = new ConcurrentSkipListSet<>();
            List<Menu> menuList = new ArrayList<>();
            groupUserList.forEach((each) -> {
                if (null != each) {
                    int groupId = each.groupId;
                    List<GroupMenu> list = GroupMenu.find.query().where()
                            .eq("groupId", groupId)
                            .orderBy().asc("id")
                            .findList();
                    list.forEach((groupMenu) -> {
                        if (null != groupMenu && groupMenu.menuId > 0 && !set.contains(groupMenu.menuId)) {
                            if (groupMenu == null) logger.info("groupmenu is null");
                            if (set == null) logger.info("set is null");
                            set.add(groupMenu.menuId);
                            Menu menu = Menu.find.byId(groupMenu.menuId);
                            if (null != menu) menuList.add(menu);
                        }
                    });
                }
            });
            List<Menu> hiddenMenuList = Menu.find.query().where()
                    .eq("hidden", true)
                    .findList();
            hiddenMenuList.parallelStream().forEach((menu) -> {
                if (null != menu && !set.contains(menu.id)) {
                    set.add(menu.id);
                    menuList.add(menu);
                }
            });
            List<Menu> sortMenuList = menuList.stream().filter((each) -> null != each)
                    .sorted((a, b) -> {
                        if (a.relativePath.compareTo(b.relativePath) > 0) return 1;
                        else if (a.relativePath.compareTo(b.relativePath) == 0) {
                            if (a.sort < b.sort) return 1;
                            else if (a.sort == b.sort) {
                                return 0;
                            } else return -1;
                        } else return -1;
                    }).collect(Collectors.toList());
            ObjectNode result = Json.newObject();
            result.put(CODE, CODE200);
            result.set("list", Json.toJson(convertListToTreeNode(sortMenuList)));
            return ok(result);
        });
    }


}
