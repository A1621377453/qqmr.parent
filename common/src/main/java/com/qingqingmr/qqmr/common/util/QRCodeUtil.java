package com.qingqingmr.qqmr.common.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Map;

/**
 * 二维码工具类
 *
 * @author ythu
 * @datetime 2018-07-14 14:30:58
 */
public final class QRCodeUtil {
    private static final Logger LOG = LoggerFactory.getLogger(QRCodeUtil.class);
    /**
     * 二维码宽度
     */
    private final static int WIDTH = 300;
    /**
     * 二维码高度
     */
    private final static int HEIGHT = 300;

    /**
     * 生成二维码图片
     *
     * @param url
     * @param params
     * @return
     */
    public static ByteArrayOutputStream generateQRCode(String url, Map<String, String> params) throws Exception {
        int width = 0, height = 0;
        if (params.containsKey("width") && StrUtil.isNumericInt(params.get("width"))) {
            width = Integer.parseInt(params.get("width"));
        }
        if (params.containsKey("height") && StrUtil.isNumericInt(params.get("height"))) {
            height = Integer.parseInt(params.get("height"));
        }
        width = width < 1 ? WIDTH : width;
        height = height < 1 ? HEIGHT : height;

        StringBuffer text = new StringBuffer(url).append("?");
        params.forEach((k, v) -> {
            if (!"width".equals(k) && !"height".equals(k)) {
                text.append(k).append("=").append(v).append("&");
            }
        });
        text.deleteCharAt(text.length() - 1);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        BitMatrix bitMatrix = new MultiFormatWriter().encode(text.toString(), BarcodeFormat.QR_CODE, 300, 300);
        BufferedImage image = new BufferedImage(bitMatrix.getWidth(), bitMatrix.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
        for (int x = 0; x < bitMatrix.getWidth(); x++) {
            for (int y = 0; y < bitMatrix.getHeight(); y++) {
                image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }
        ImageIO.write(image, "jpg", byteArrayOutputStream);
        return byteArrayOutputStream;
    }
}
