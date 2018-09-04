package com.qingqingmr.qqmr.base.util.file;

import com.qingqingmr.qqmr.base.util.BaseHttpUtil;
import com.qingqingmr.qqmr.common.ResultInfo;
import com.qingqingmr.qqmr.common.util.Arith;
import org.apache.commons.fileupload.FileItem;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;

/**
 * <p>
 * 文件处理
 * </p>
 *
 * @author ythu
 * @datetime 2018年1月30日 下午3:58:37
 */
public class FileUtil {
    /**
     * 上传文件大小
     */
    public static final int MAX_SIZE = 2;
    /**
     * 上传文件目录
     */
    public static final String SAVE_UPLOAD = "/data/upload/";
    /**
     * 上传文件目录的零时目录
     */
    public static final String SAVE_UPLOAD_TMP = "/data/upload/tmp/";

    /**
     * 上传图片
     *
     * @param file
     * @param device
     * @return
     */
    public static ResultInfo uploadImage(MultipartFile file, String... device) {
        ResultInfo result = new ResultInfo();
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest();
        // 判断参数
        if (request == null) {
            result.setInfo(-1, "请求非法");
            return result;
        }
        if (file == null) {
            result.setInfo(-1, "文件为空");
            return result;
        }
        if (Arith.div(file.getSize(), 1024 * 1024, 2) > MAX_SIZE) {
            result.setInfo(-1, "头像大小不能超过" + MAX_SIZE + "MB");
            return result;
        }

        // 判断文件是否合法
        String fileName = file.getOriginalFilename();
        // 有的手机上传的图片是没有后缀的，自己动态拼接一个
        if (fileName.indexOf(".") < 0) {
            fileName += ".jpg";
        }
        String fileSuffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        List<String> array = Arrays.asList("gif", "jpg", "jpeg", "png", "bmp", "ico");
        boolean flag = false;
        for (int i = 0; i < array.size(); i++) {
            if (array.get(i).equalsIgnoreCase(fileSuffix)) {
                flag = true;
                break;
            }
        }
        if (!flag) {
            result.setInfo(-1, "文件后缀不支持");
            return result;
        }

        CommonsMultipartFile multipartFile = (CommonsMultipartFile) file;
        FileItem fileItem = multipartFile.getFileItem();
        String mime = fileItem.getContentType();
        if (mime == null || "".equals(mime)) {
            result.setInfo(-1, "未知的文件类型");
            return result;
        }
        if (!mime.startsWith("application/octet-stream") && !mime.startsWith("file")) {
            FileType fileType = FileType.getFileTypeByMime(mime);
            if (fileType == null) {
                result.setInfo(-1, "文件类型不支持");
                return result;
            }
        }

        // 保存文件
        String save = request.getSession().getServletContext().getRealPath(SAVE_UPLOAD);
        File targetFile = new File(save, UUID.randomUUID().toString());
        Map<String, Object> map = new HashMap<String, Object>();
        String baseURL = BaseHttpUtil.getBaseURL(request);
        try {
            file.transferTo(targetFile);
            /** 保存名称 */
            if (device != null && device.length > 0 && ("api".equals(device[0]))) {
                map.put("name", File.separator + device[0] + SAVE_UPLOAD + targetFile.getName());
            } else {
                map.put("name", baseURL + SAVE_UPLOAD + targetFile.getName());
            }
            /** 后缀 */
            map.put("suffix", fileSuffix);
            /** 上传后文件大些 */
            map.put("size", Arith.div(targetFile.length(), 1024, 2));
            if ("ico".equalsIgnoreCase(fileSuffix)) {
                ImageIcon icon = new ImageIcon(targetFile.getAbsolutePath());
                /** 高度 */
                map.put("height", icon.getIconHeight());
                /** 宽度 */
                map.put("width", icon.getIconWidth());
                /** 分辨率 */
                map.put("resolution", icon.getIconWidth() + "*" + icon.getIconHeight());
            } else {
                Image src = ImageIO.read(targetFile);
                /** 高度 */
                map.put("height", src.getHeight(null));
                /** 宽度 */
                map.put("width", src.getWidth(null));
                /** 分辨率 */
                map.put("resolution", src.getWidth(null) + "*" + src.getHeight(null));
            }
        } catch (IllegalStateException | IOException e) {
            e.printStackTrace();
        }

        // 返回数据
        result.setInfo(1, "上传成功");
        result.setObj(map);
        return result;
    }

    /**
     * <p>
     * 获取文件类型
     * </p>
     *
     * @param path
     * @return
     * @throws IOException
     * @author ythu
     * @datetime 2018年1月30日 下午4:58:13
     */
    public static String getFileMime(String path) {
        if (path == null || "".equals(path))
            return null;
        Path source = Paths.get(path);
        try {
            return Files.probeContentType(source);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
