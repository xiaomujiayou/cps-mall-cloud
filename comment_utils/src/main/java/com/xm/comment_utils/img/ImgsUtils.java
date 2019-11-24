package com.xm.comment_utils.img;

import com.xm.comment_utils.number.MathUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 多图处理
 */
public class ImgsUtils {

    //图片数据
    private List<BufferedImage> imgs;
    //候补颜色
    private Integer alternateColor;

//    public static ImgsUtils create(List<byte[]> imgsBytes, Integer alternateColor) throws IOException {
//        ImgsUtils imgsUtils = new ImgsUtils();
//        imgsUtils.imgs = imgsUtils.getImgs(imgsBytes);
//        imgsUtils.alternateColor = alternateColor;
//        return imgsUtils;
//    }
    public static ImgsUtils create(List<BufferedImage> bufferedImages, Integer alternateColor) throws IOException {
        ImgsUtils imgsUtils = new ImgsUtils();
        imgsUtils.imgs = bufferedImages;
        imgsUtils.alternateColor = alternateColor;
        return imgsUtils;
    }

    public List<BufferedImage> getImgs(List<byte[]> imgsBytes) throws IOException {
        List<BufferedImage> result = new ArrayList<>();
        for (byte[] image : imgsBytes) {
            result.add(ImageIO.read(new ByteArrayInputStream(image)));
        }
        return result;
    }

    /**
     * 分割图片
     * @param direction :分割方向
     * @param unitPx    :单位像素
     * @param amount    :分割块数(unitPx存在则该值无效)
     * @return
     */
    public ImgsUtils splits(Direction direction, Integer unitPx, Integer amount) {
        List<BufferedImage> targrt = new ArrayList<>();
        for (BufferedImage img : imgs) {
            targrt.addAll(split(img, direction, unitPx, amount));
        }
        imgs.clear();
        imgs = targrt;
        return this;
    }

//    public static void main(String[] args) throws IOException {
//
//        File file = new File("C:\\Users\\xiaomu\\Desktop\\1\\123.jpeg");
//        File file22 = new File("C:\\Users\\xiaomu\\Desktop\\1\\122.jpg");
//
//        FileInputStream fileInputStream = new FileInputStream(file);
//        FileInputStream fileInputStream2 = new FileInputStream(file22);
//
//        byte[] fileByte = new byte[(int) file.length()];
//        fileInputStream.read(fileByte);
//        List<byte[]> imgs = new ArrayList<>();
//        byte[] fileByte22 = new byte[(int) file22.length()];
//        fileInputStream2.read(fileByte22);
//        imgs.add(fileByte);
//        imgs.add(fileByte22);
//        List<BufferedImage> convert = ImgsUtils.create(imgs, 0xFFFFFF)
//                .resizeImgs(Stretch.GEOMETRIC, 800, null)
//                .merge(null,null,Direction.DOWN,null)
//                .mirrors(Direction.LEFT,null)
//                .splits(Direction.DOWN,null,3).get();
////        BufferedImage convert = ImgsUtils.create(imgs, 0xFFFFFF).mirrors(Arrays.asList(0)).merge(1600, null, Direction.RIGHT, null).splits(Direction.LEFT, 150, null).get(1);
////        List<BufferedImage> convert = ImgsUtils.create(imgs, 0xFFFFFF).splits(Direction.RIGHT, 1500, null).get();
//
//        int i = 0;
//        for (BufferedImage image : convert) {
//            File file2 = new File("C:\\Users\\xiaomu\\Desktop\\1\\"+ ++i +".jpg");
//            FileOutputStream fileOutputStream = new FileOutputStream(file2);
//            ImageIO.write(image, "jpg", fileOutputStream);
//            fileOutputStream.flush();
//            fileOutputStream.close();
//            fileInputStream.close();
//        }
//    }


    /**
     * 分割图片
     *
     * @param img
     * @param direction :分割方向
     * @param unitPx    :单位像素
     * @param amount    :分割数量(单位像素存在则无效)
     * @return
     */
    public static List<BufferedImage> split(BufferedImage img, Direction direction, Integer unitPx, Integer amount) {
        List<BufferedImage> targrts = new ArrayList<>();
        Size targrtSize = new Size();
        if (direction == Direction.UP || direction == Direction.DOWN) {
            unitPx = (unitPx == null ? (int)Math.ceil(MathUtils.div(img.getHeight() , amount)) : unitPx);
            targrtSize.setWidth(img.getWidth());
            targrtSize.setHeight(unitPx);
            if(unitPx > img.getHeight()){
                targrts.add(img);
                return targrts;
            }
        } else {
            unitPx = (unitPx == null ?(int)Math.ceil(MathUtils.div(img.getWidth() , amount)) : unitPx);
            targrtSize.setWidth(unitPx);
            targrtSize.setHeight(img.getHeight());
            if(unitPx > img.getWidth()){
                targrts.add(img);
                return targrts;
            }
        }
        BufferedImage bufferedImage = new BufferedImage(targrtSize.getWidth(), targrtSize.getHeight(), BufferedImage.TYPE_INT_RGB);
        if (direction == Direction.UP || direction == Direction.DOWN) {
            for (int i1 = 0; i1 < img.getHeight(); i1++) {
                for (int i = 0; i < img.getWidth(); i++) {
                    int x = i % targrtSize.getWidth();
                    int y = i1 % unitPx;
                    if (direction == Direction.UP)
                        bufferedImage.setRGB(targrtSize.getWidth() - x - 1, targrtSize.getHeight() - y - 1, img.getRGB(img.getWidth() - i - 1, img.getHeight() - i1 - 1));
                    if (direction == Direction.DOWN)
                        bufferedImage.setRGB(x, y, img.getRGB(i, i1));
                    if (x == targrtSize.width - 1 && y == targrtSize.height - 1) {
                        if (direction == Direction.UP)
                            targrts.add(0, bufferedImage);
                        if (direction == Direction.DOWN)
                            targrts.add(bufferedImage);
                        if (img.getHeight() - i1 -1 < targrtSize.getHeight()) {
                            targrtSize.setHeight(img.getHeight() - i1 - 1);
                            if (targrtSize.width <= 0 || targrtSize.height <= 0)
                                continue;
                        }
                        bufferedImage = new BufferedImage(targrtSize.getWidth(), targrtSize.getHeight(), BufferedImage.TYPE_INT_RGB);
                    }
                }
            }
        }else{
            for (int i = 0; i < img.getWidth(); i++) {
                for (int i1 = 0; i1 < img.getHeight(); i1++) {
                    int x = i % unitPx;
                    int y = i1 % targrtSize.getHeight();

                    if (direction == Direction.LEFT)
                        bufferedImage.setRGB(targrtSize.getWidth() - x - 1, targrtSize.getHeight() - y - 1, img.getRGB(img.getWidth() - i - 1, img.getHeight() - i1 - 1));
                    if (direction == Direction.RIGHT)
                        bufferedImage.setRGB(x, y, img.getRGB(i, i1));
                    if (x == targrtSize.width - 1 && y == targrtSize.height - 1) {
                        if (direction == Direction.LEFT)
                            targrts.add(0, bufferedImage);
                        if (direction == Direction.RIGHT)
                            targrts.add(bufferedImage);
                        if (img.getWidth() - i -1 < targrtSize.getWidth()) {
                            targrtSize.setWidth(img.getWidth() - i-1 );
                            if (targrtSize.width <= 0 || targrtSize.height <= 0)
                                continue;
                        }
                        bufferedImage = new BufferedImage(targrtSize.getWidth(), targrtSize.getHeight(), BufferedImage.TYPE_INT_RGB);
                    }
                }
            }
        }
        return targrts;
    }

    /**
     * 合并图片
     *
     * @param width     :合并后的宽度(合并方向为上、下时生效)，为空则取图片组最大宽度
     * @param height    :合并后的高度(合并方向为左、右时生效)，为空则取图片组最大高度
     * @param direction :合并方向
     * @param stretch   :拉伸策略 为空默认等比缩放
     * @return
     */
    public ImgsUtils merge(Integer width, Integer height, Direction direction, Stretch stretch) throws IOException {
        if (direction == Direction.UP || direction == Direction.DOWN)
            if (width == null)
                width = maxWidth();
        if (direction == Direction.LEFT || direction == Direction.RIGHT)
            if (height == null)
                height = maxHeight();
        stretch = (stretch == null ? Stretch.GEOMETRIC : stretch);

        int totalWidth = 0;
        int totalHeight = 0;
        BufferedImage target = null;

        //大小处理
        for (int i = 0; i < imgs.size(); i++) {
            if (direction == Direction.UP || direction == Direction.DOWN) {
                resizeImgs(stretch, width, null);
                totalHeight = imgs.stream().mapToInt(img -> img.getHeight()).sum();
                target = new BufferedImage(width, totalHeight, BufferedImage.TYPE_INT_RGB);
            } else {
                resizeImgs(stretch, null, height);
                totalWidth = imgs.stream().mapToInt(img -> img.getWidth()).sum();
                target = new BufferedImage(totalWidth, height, BufferedImage.TYPE_INT_RGB);
            }
        }

        //合并
        for (int j = 0; j < target.getWidth(); j++) {
            for (int k = 0; k < target.getHeight(); k++) {
                int rgb = calcRgb(direction, j, k);
                target.setRGB(j, k, rgb);
            }
        }
        imgs.clear();
        imgs.add(target);
        return this;
    }

    /**
     * 计算rgb
     *
     * @param direction
     * @return
     */
    private int calcRgb(Direction direction, Integer x, Integer y) {
        List<byte[]> sort = new ArrayList<>();

        for (int i = 0; i < imgs.size(); i++) {
            Size size = null;
            BufferedImage img = null;
            if (direction == Direction.UP) {
                size = new Size(imgs.get(imgs.size() - i - 1).getWidth(), imgs.get(imgs.size() - i - 1).getHeight());
                img = imgs.get(imgs.size() - i - 1);
                if (y > size.height - 1) {
                    y -= size.height;
                    continue;
                }
            } else if (direction == Direction.DOWN) {
                size = new Size(imgs.get(i).getWidth(), imgs.get(i).getHeight());
                img = imgs.get(i);
                if (y > size.height - 1) {
                    y -= size.height;
                    continue;
                }
            } else if (direction == Direction.LEFT) {
                size = new Size(imgs.get(imgs.size() - i - 1).getWidth(), imgs.get(imgs.size() - i - 1).getHeight());
                img = imgs.get(imgs.size() - i - 1);
                if (x > size.width - 1) {
                    x -= size.width;
                    continue;
                }
            } else if (direction == Direction.RIGHT) {
                size = new Size(imgs.get(i).getWidth(), imgs.get(i).getHeight());
                img = imgs.get(i);
                if (x > size.width - 1) {
                    x -= size.width;
                    continue;
                }
            }
            return img.getRGB(x, y);
        }
        throw new RuntimeException("找不到指定坐标");
    }

    /**
     * 指定图形的长和宽
     *
     * @param width
     * @param height
     * @throws IOException
     */
    public ImgsUtils resizeImgs(Stretch stretch, Integer width, Integer height) throws IOException {
        for (int i = 0; i < imgs.size(); i++) {
            BufferedImage resizeImg = resizeImg(imgs.get(i), stretch, width, height, alternateColor);
            imgs.remove(i);
            imgs.add(i, resizeImg);
        }
        return this;
    }

    /**
     * 镜像全部
     *
     * @param exclude 不处理的图片索引
     * @return
     * @throws IOException
     */
    public ImgsUtils mirrors(Direction direction, List<Integer> exclude) throws IOException {
        for (int i = 0; i < imgs.size(); i++) {
            if (exclude != null && exclude.contains(i))
                continue;
            BufferedImage img = mirror(imgs.get(i),direction);
            imgs.remove(i);
            imgs.add(i, img);
        }
        return this;
    }


    /**
     * 改变图片大小
     *
     * @param img
     * @param stretch
     * @param width
     * @param height
     * @param alternateColor
     * @return
     * @throws IOException
     */
    public static BufferedImage resizeImg(BufferedImage img, Stretch stretch, Integer width, Integer height, Integer alternateColor) throws IOException {
        Size imgSize = new Size(img.getWidth(), img.getHeight());
        // 构造Image对象
        BufferedImage src = img;
        // 新建空白图片
        BufferedImage target = null;
        if (stretch == Stretch.STRETCH) {
            //绘制拉伸后的图片
            target = new BufferedImage(width == null ? imgSize.getWidth() : width, height == null ? imgSize.getHeight() : height, BufferedImage.TYPE_INT_RGB);
            target.getGraphics().drawImage(src, 0, 0, width, height, null);
        } else if (stretch == Stretch.TILE) {
            //平铺
            target = new BufferedImage(width == null ? imgSize.getWidth() : width, height == null ? imgSize.getHeight() : height, BufferedImage.TYPE_INT_RGB);
            for (int j = 0; j < target.getWidth(); j++) {
                for (int k = 0; k < target.getHeight(); k++) {
                    target.setRGB(j, k, src.getRGB(j % src.getWidth(), k % src.getHeight()));
                }
            }
        } else if (stretch == Stretch.GEOMETRIC) {
            //等比缩放
            //长宽都被指定则按比例最小的缩放
            if (width != null && height != null)
                throw new IllegalArgumentException("等比缩放宽高只能存在一个");
            //计算缩放比例
            double ratio = MathUtils.div(width == null ? height : width, width == null ? src.getHeight() : src.getWidth());
            Size newSize = new Size(width == null ? (int) (Math.ceil(MathUtils.mul(src.getWidth(), ratio))) : width, height == null ? (int) (Math.ceil(MathUtils.mul(src.getHeight(), ratio))) : height);
            target = new BufferedImage(newSize.getWidth(), newSize.getHeight(), BufferedImage.TYPE_INT_RGB);
            target.getGraphics().drawImage(src, 0, 0, newSize.getWidth(), newSize.getHeight(), null);
        } else if (stretch == Stretch.ALIGN) {
            target = new BufferedImage(width == null ? imgSize.getWidth() : width, height == null ? imgSize.getHeight() : height, BufferedImage.TYPE_INT_RGB);
            for (int j = 0; j < target.getWidth(); j++) {
                for (int k = 0; k < target.getHeight(); k++) {
                    if (j < src.getWidth() && k < src.getHeight()) {
                        target.setRGB(j, k, src.getRGB(j, k));
                    } else {
                        target.setRGB(j, k, alternateColor);
                    }
                }
            }
        } else if (stretch == Stretch.CENTER) {
            target = new BufferedImage(width == null ? imgSize.getWidth() : width, height == null ? imgSize.getHeight() : height, BufferedImage.TYPE_INT_RGB);
            int widthFlag = (width - src.getWidth()) / 2;
            int heightFlag = (height - src.getHeight()) / 2;
            for (int j = 0; j < target.getWidth(); j++) {
                for (int k = 0; k < target.getHeight(); k++) {
                    if (j > widthFlag && k > heightFlag && j - widthFlag < src.getWidth() && k - heightFlag < src.getHeight()) {
                        target.setRGB(j, k, src.getRGB(j - widthFlag, k - heightFlag));
                    } else {
                        target.setRGB(j, k, alternateColor);
                    }
                }
            }
        }
        return target;
    }

    /**
     * 图片镜像
     * @param img
     * @return
     */
    public static BufferedImage mirror(BufferedImage img,Direction direction) {
        BufferedImage target = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
        if(direction == Direction.LEFT || direction == Direction.RIGHT){
            for (int i = 0; i < img.getWidth(); i++) {
                for (int i1 = 0; i1 < img.getHeight(); i1++) {
                    target.setRGB(i, i1, img.getRGB(img.getWidth() - i - 1, i1));
                }
            }
        }else {
            for (int i = 0; i < img.getWidth(); i++) {
                for (int i1 = 0; i1 < img.getHeight(); i1++) {
                    target.setRGB(i, i1, img.getRGB(i, img.getHeight() - i1 - 1));
                }
            }
        }

        return target;
    }

    public List<BufferedImage> get() {
        return imgs;
    }

    public BufferedImage get(Integer index) {
        return imgs.get(index);
    }

    /**
     * 取图片组最大高度
     *
     * @return
     * @throws IOException
     */
    private Integer maxHeight() throws IOException {
        return imgs.stream()
                .sorted((img1, img2) -> {
                    return ((Integer) img1.getHeight()).compareTo((Integer) img2.getHeight());
                })
                .findFirst()
                .get()
                .getHeight();
    }

    /**
     * 取图片组最大宽度
     *
     * @return
     * @throws IOException
     */
    private Integer maxWidth() throws IOException {
        return imgs.stream()
                .sorted((img1, img2) -> {
                    return ((Integer) img1.getWidth()).compareTo((Integer) img2.getWidth());
                })
                .findFirst()
                .get()
                .getWidth();
    }


    /**
     * 图片合并方向
     */
    public enum Direction {
        UP,
        DOWN,
        LEFT,
        RIGHT;
    }

    /**
     * 图片生成策略
     */
    public enum Stretch {
        //拉伸
        STRETCH,
        //平铺
        TILE,
        //等比缩放
        GEOMETRIC,
        //对齐补白(自动识别边缘颜色)
        ALIGN,
        //居中补白
        CENTER;
    }
}
