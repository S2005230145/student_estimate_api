package controllers.basic;

import com.fasterxml.jackson.databind.node.ObjectNode;
import controllers.BaseController;
import org.apache.commons.io.FilenameUtils;
import play.libs.Files;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;

import java.io.File;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.CompletableFuture;

/**
 * 文件上传控制器
 * 用于上传图片到本地服务器
 */
public class FileController extends BaseController {

    // 允许的图片扩展名
    private static final String[] ALLOWED_EXTENSIONS = {"jpg", "jpeg", "png", "gif", "bmp", "webp"};

    /**
     * @api {POST} /v2/p/file/upload_image/ 上传图片
     * @apiName uploadImage
     * @apiGroup FILE-CONTROLLER
     * @apiParam {file} file 图片文件
     * @apiSuccess (Success 200) {int} code 200
     * @apiSuccess (Success 200) {String} url 图片访问路径
     * @apiSuccess (Error 40001) {int} code 40001 参数错误
     * @apiSuccess (Error 40003) {int} code 40003 上传失败
     */
    public CompletionStage<Result> uploadImage(Http.Request request) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Http.MultipartFormData<Files.TemporaryFile> body = request.body().asMultipartFormData();
                if (body == null) {
                    return okCustomJson(CODE40001, "请求格式错误，请使用multipart/form-data");
                }

                Http.MultipartFormData.FilePart<Files.TemporaryFile> uploadFile = body.getFile("file");
                if (uploadFile == null) {
                    return okCustomJson(CODE40001, "未找到上传文件，请确保表单字段名为'file'");
                }

                // ★ 从 multipart 表单字段里取 style
                Map<String, String[]> formFields = body.asFormUrlEncoded();
                String[] styleArr = formFields.get("style");
                String style = (styleArr != null && styleArr.length > 0) ? styleArr[0] : null;
                if (style == null || style.isEmpty()) {
                    return okCustomJson(CODE40001, "请指定文件类型");
                }

                String fileName = uploadFile.getFilename();
                if (fileName == null || fileName.isEmpty()) {
                    return okCustomJson(CODE40001, "文件名不能为空");
                }

                String extension = FilenameUtils.getExtension(fileName).toLowerCase();
                if (!isAllowedExtension(extension)) {
                    return okCustomJson(CODE40001, "不支持的文件格式，仅支持: " + String.join(", ", ALLOWED_EXTENSIONS));
                }

                // 基础存储目录（与 BaseController.FILE_DIR_LOCATION 保持一致）
                String baseDir = FILE_DIR_LOCATION;

                // 目标目录：{base}/{style}/
                String uploadDirPath = Paths.get(baseDir, style).toString() + File.separator;
                File uploadDir = new File(uploadDirPath);
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }

                String uniqueFileName = UUID.randomUUID().toString() + "." + extension;
                String destPath = uploadDirPath + uniqueFileName;

                Files.TemporaryFile file = uploadFile.getRef();
                file.copyTo(Paths.get(destPath), true);
                // 返回文件绝对路径
                String url = style + "/" + uniqueFileName;
                ObjectNode result = Json.newObject();
                result.put(CODE, CODE200);
                result.put("url", url);
                return ok(result);

            } catch (Exception e) {
                return okCustomJson(CODE40003, "上传失败: " + e.getMessage());
            }
        });
    }

    /**
     * 检查文件扩展名是否允许
     * @param extension 文件扩展名
     * @return 是否允许
     */
    private boolean isAllowedExtension(String extension) {
        if (extension == null || extension.isEmpty()) {
            return false;
        }
        for (String allowed : ALLOWED_EXTENSIONS) {
            if (allowed.equalsIgnoreCase(extension)) {
                return true;
            }
        }
        return false;
    }
}

