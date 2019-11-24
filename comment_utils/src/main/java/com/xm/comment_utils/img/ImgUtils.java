package com.xm.comment_utils.img;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImgUtils {

    
    /**
     * 获取图片尺寸
     * @param imageData
     * @return
     * @throws IOException
     */
    public static Size getImgSize(byte[] imageData) throws IOException {
        Image image2 = ImageIO.read(new ByteArrayInputStream(imageData));
        int width = image2.getWidth(null);
        int height = image2.getHeight(null);
        return new Size(width,height);
    }

    /**
     * 指定图形的长和宽
     * @param iamgeSrc
     * @param imageDest
     * @param width
     * @param height
     * @throws IOException
     */
    public static void resizeImage(String iamgeSrc, String imageDest, int width, int height) {
        FileOutputStream outputStream = null;
        try {
            //读入文件
            File file = new File(iamgeSrc);
            // 构造Image对象
            BufferedImage src = ImageIO.read(file);
            // 放大边长
            BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            //绘制放大后的图片
            tag.getGraphics().drawImage(src, 0, 0, width, height, null);
            outputStream = new FileOutputStream(imageDest);
            outputStream = new FileOutputStream(imageDest);
            ImageIO.write(tag, "jpg", outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args){

    }
}
