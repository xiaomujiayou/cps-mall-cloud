package com.xm.comment_utils.img;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class WaterMarkUtils {
    public static final String MARK_TEXT = "hello~~";
    public static final String FONT_NAME = "微软雅黑";
    public static final int FONT_STYLE = Font.BOLD;	//黑体
    public static final int FONT_SIZE = 50;			//文字大小
    public static final Color FONT_COLOR = Color.GRAY;//文字颜色
    public static final int ROTATION_ANGLE = 30;//顺时针旋转角度
    public static final int SPACE = 100;//水印间隔
    public static float ALPHA = 0.02F; //文字水印透明度

    public static byte[] watermark(byte[] imageData) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            //1 创建图片缓存对象
            Image image2 = ImageIO.read(new ByteArrayInputStream(imageData));
            int width = image2.getWidth(null);
            int height = image2.getHeight(null);
            BufferedImage bufferedImage = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
            //2 创建Java绘图工具对象
            Graphics2D g = bufferedImage.createGraphics();
            //3 使用绘图工具对象将原图绘制到缓存图片对象
            g.drawImage(image2, 0, 0, width, height, null);
            //4 使用绘图工具对象将水印（文字/图片）绘制到缓存图片
            g.setFont(new Font(FONT_NAME,FONT_STYLE,FONT_SIZE));
            g.setColor(FONT_COLOR);
            int width1 = FONT_SIZE * getTextLength(MARK_TEXT);//文字水印宽度
            int height1= FONT_SIZE;							//文字水印高度
            //透明度的设置
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,ALPHA));
            //旋转图片(30°)
            g.rotate(Math.toRadians(ROTATION_ANGLE),bufferedImage.getWidth()/2,bufferedImage.getHeight()/2);
            //设置水印的坐标
            int x= -width/2;
            int y= -height/2;
            while(x < width*1.5){
                y = -height/2;
                while(y < height*1.5){
                    g.drawString(MARK_TEXT,x,y);
                    y += height1 + SPACE;
                }
                x += width1 + SPACE;	//水印之间的间隔设为50
            }
            //释放工具
            g.dispose();
            ImageIO.write(bufferedImage,"jpg",byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } finally{
            try {
                byteArrayOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //处理文字水印的中英文字符的宽度转换
    private static int getTextLength(String text){
        int length = text.length();
        for(int i=0;i<text.length();i++){
            String s = String.valueOf(text.charAt(i));
            if(s.getBytes().length>1){	//中文字符
                length++;
            }
        }
        length = length%2 == 0?length/2:length/2+1;  //中文和英文字符的转换
        return length;
    }
    

}
