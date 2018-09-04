package com.qingqingmr.qqmr.base.util.file;

/**
 * <p>
 * 文件类型
 * </p>
 *
 * @author ythu
 * @datetime 2018年1月30日 下午5:08:26
 */
public enum FileType {

    /**
     * 图片文件
     */
    FYPE_IMG(1, "图片文件", "图片类型，可选的有gif,jpg,jpeg,png,bmp,ico六种格式的文件",
            // 后缀
            new String[]{"gif", "jpg", "jpeg", "png", "bmp", "ico"},
            // mime
            new String[]{"image/gif", "image/jpeg", "image/png", "application/x-MS-bmp", "image/x-icon"}),
    /**
     * 文本文件
     */
    FYPE_TXT(2, "文本文件", "文本文件，可选的为txt文件",
            // 后缀
            new String[]{"txt"},
            // mime
            new String[]{"text/plain"}),
    /**
     * 视频文件
     */
    FYPE_VIDEO(3, "视频文件", "视频文件类型，可选的有mp4,3gp,avi,wmv,rm,rmvb五种格式的文件",
            // 后缀
            new String[]{"mp4", "3gp", "avi", "wmv", "rm", "rmvb"},
            // mime
            new String[]{"video/mp4", "video/3gpp", "video/x-msvideo", "audio/x-ms-wmv", "audio/x-pn-realaudio"}),
    /**
     * 表格文件
     */
    FYPE_AUDIO(4, "表格文件", "音频文件，可选的有mp3,wav,wma三种格式的文件",
            // 后缀
            new String[]{"mp3", "wav", "wma"},
            // mime
            new String[]{"audio/x-mpeg", "audio/x-wav", "audio/x-ms-wma"}),
    /**
     * 表格文件
     */
    FYPE_XLS(5, "表格文件", "表格文件，可选的有xls,xlsx两种格式的文件",
            // 后缀
            new String[]{"xls", "xlsx"},
            // mime
            new String[]{"application/vnd.ms-excel",
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"}),
    /**
     * Word文件
     */
    FYPE_DOC(6, "Word文件", "Word文件，可选的有doc,docx两种格式的文件",
            // 后缀
            new String[]{"docx", "doc"},
            // mime
            new String[]{"docx,application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                    "application/msword"}),
    /**
     * 演示文稿
     */
    FYPE_PPT(7, "演示文稿", "演示文稿，可选的有ppt,pptx两种格式的文件",
            // 后缀
            new String[]{"ppt", "pptx"},
            // mime
            new String[]{"application/vnd.ms-powerpoint",
                    "application/vnd.openxmlformats-officedocument.presentationml.presentation"});

    /**
     * 文件类型代号
     */
    public int code;

    /**
     * 文件类型名称
     */
    public String type;

    /**
     * 文件类型描述
     */
    public String description;

    /**
     * 该文件类型常用后缀
     */
    public String[] suffix;

    /**
     * 该文件类型常用的mimeTyps
     */
    public String[] mime;

    private FileType(int code, String type, String description, String[] suffix, String[] mime) {
        this.code = code;
        this.type = type;
        this.suffix = suffix;
        this.mime = mime;
        this.description = description;
    }

    public static FileType getEnum(int code) {
        FileType[] statuies = FileType.values();
        for (FileType stat : statuies) {
            if (stat.code == code) {
                return stat;
            }
        }
        return null;
    }

    public static FileType getFileTypeByMime(String mime) {
        FileType[] statuies = FileType.values();
        for (FileType type : statuies) {
            String[] legalMimes = type.mime;
            for (String legalMime : legalMimes) {
                if (legalMime.equalsIgnoreCase(mime)) {
                    return type;
                }
            }
        }
        return null;
    }
}