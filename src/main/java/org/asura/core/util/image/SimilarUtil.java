package org.asura.core.util.image;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.imageio.ImageIO;

import org.asura.core.util.FileUtil;
import org.asura.core.util.math.Probability;
import org.asura.core.util.model.Combination;

public class SimilarUtil {
	public static final int SAMEVALUE = 5;
	public static final int SIMILARVALUE = 10;

	public static void main(String[] args) {
		String[] files = FileUtil.getAllFileNames("E:/pic");
		List<List<String>> llist = new Combination<String>().getCombination(files, 2);

		for (List<String> list : llist) {
			System.out.println(list);
			System.out.println(diff(getPix(list.get(0)), getPix(list.get(1))));
		}
	}

	public static int hammingDistance(String sourceHashCode, String hashCode) {
		int difference = 0;
		int len = sourceHashCode.length();
		for (int i = 0; i < len; ++i) {
			if (sourceHashCode.charAt(i) != hashCode.charAt(i)) {
				++difference;
			}
		}
		return difference;
	}

	public static String produceFingerPrint(String filename) {
		BufferedImage source = ImageHelper.readPNGImage(filename);
		int width = 8;
		int height = 8;
		BufferedImage thumb = ImageHelper.thumb(source, width, height, false);

		int[] pixels = new int[width * height];
		for (int i = 0; i < width; ++i) {
			for (int j = 0; j < height; ++j) {
				pixels[(i * height + j)] = ImageHelper.rgbToGray(thumb.getRGB(i, j));
			}

		}

		int avgPixel = ImageHelper.average(pixels);

		int[] comps = new int[width * height];
		for (int i = 0; i < comps.length; ++i) {
			if (pixels[i] >= avgPixel)
				comps[i] = 1;
			else {
				comps[i] = 0;
			}

		}

		StringBuffer hashCode = new StringBuffer();
		for (int i = 0; i < comps.length; i += 4) {
			int result = comps[i] * (int) Math.pow(2.0D, 3.0D) + comps[(i + 1)] * (int) Math.pow(2.0D, 2.0D)
					+ comps[(i + 2)] * (int) Math.pow(2.0D, 1.0D) + comps[(i + 2)];
			hashCode.append(binaryToHex(result));
		}

		return hashCode.toString();
	}

	private static int[] getPix(String fileName) {
		BufferedImage source = ImageHelper.readPNGImage(fileName);

		int width = 8;
		int height = 8;

		BufferedImage thumb = ImageHelper.thumb(source, width, height, false);

		int[] pixels = new int[width * height];
		for (int i = 0; i < width; ++i) {
			for (int j = 0; j < height; ++j) {
				pixels[(i * height + j)] = ImageHelper.rgbToGray(thumb.getRGB(i, j));
			}
		}

		return pixels;
	}

	private static double diff(int[] p1s, int[] p2s) {
		double[] r = new double[p1s.length];

		for (int i = 0; i < p1s.length; ++i) {
			r[i] = (p1s[i] - p2s[i]);
		}

		return Math.sqrt(Probability.getVariance(r));
	}

	private static char binaryToHex(int binary) {
		char ch = ' ';
		switch (binary) {
		case 0:
			ch = '0';
			break;
		case 1:
			ch = '1';
			break;
		case 2:
			ch = '2';
			break;
		case 3:
			ch = '3';
			break;
		case 4:
			ch = '4';
			break;
		case 5:
			ch = '5';
			break;
		case 6:
			ch = '6';
			break;
		case 7:
			ch = '7';
			break;
		case 8:
			ch = '8';
			break;
		case 9:
			ch = '9';
			break;
		case 10:
			ch = 'a';
			break;
		case 11:
			ch = 'b';
			break;
		case 12:
			ch = 'c';
			break;
		case 13:
			ch = 'd';
			break;
		case 14:
			ch = 'e';
			break;
		case 15:
			ch = 'f';
			break;
		default:
			ch = ' ';
		}
		return ch;
	}

	static class ImageHelper {
		public static final String path = System.getProperty("user.dir");

		public static BufferedImage thumb(BufferedImage source, int width, int height, boolean b) {
			int type = source.getType();
			BufferedImage target = null;
			double sx = width / source.getWidth();
			double sy = height / source.getHeight();

			if (b) {
				if (sx > sy) {
					sx = sy;
					width = (int) (sx * source.getWidth());
				} else {
					sy = sx;
					height = (int) (sy * source.getHeight());
				}
			}

			if (type == 0) {
				ColorModel cm = source.getColorModel();
				WritableRaster raster = cm.createCompatibleWritableRaster(width, height);
				boolean alphaPremultiplied = cm.isAlphaPremultiplied();
				target = new BufferedImage(cm, raster, alphaPremultiplied, null);
			} else {
				target = new BufferedImage(width, height, type);
			}
			Graphics2D g = target.createGraphics();

			g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
			g.drawRenderedImage(source, AffineTransform.getScaleInstance(sx, sy));
			g.dispose();
			return target;
		}

		public static void waterMark(String imgPath, String markPath, int x, int y, float alpha) {
			try {
				Image img = ImageIO.read(new File(imgPath));

				BufferedImage image = new BufferedImage(img.getWidth(null), img.getHeight(null), 1);
				Graphics2D g = image.createGraphics();
				g.drawImage(img, 0, 0, null);

				Image src_biao = ImageIO.read(new File(markPath));
				g.setComposite(AlphaComposite.getInstance(10, alpha));
				g.drawImage(src_biao, x, y, null);
				g.dispose();
				
				String formatName = imgPath.substring(imgPath.lastIndexOf(".") + 1);  
				ImageIO.write(image, formatName, new File(imgPath));  
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		public static void textMark(String imgPath, String text, Font font, Color color, int x, int y, float alpha) {
			try {
				Font Dfont = (font == null) ? new Font("宋体", 20, 13) : font;

				Image img = ImageIO.read(new File(imgPath));

				BufferedImage image = new BufferedImage(img.getWidth(null), img.getHeight(null), 1);
				Graphics2D g = image.createGraphics();

				g.drawImage(img, 0, 0, null);
				g.setColor(color);
				g.setFont(Dfont);
				g.setComposite(AlphaComposite.getInstance(10, alpha));
				g.drawString(text, x, y);
				g.dispose();
				String formatName = imgPath.substring(imgPath.lastIndexOf(".") + 1);  
				ImageIO.write(image, formatName, new File(imgPath));  
			} catch (Exception e) {
				System.out.println(e);
			}
		}

		public static BufferedImage readJPEGImage(String filename) {
			try {
				InputStream imageIn = new FileInputStream(new File(filename));
				
				return ImageIO.read(imageIn);

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}catch (IOException e) {
				e.printStackTrace();
			}

			return null;
		}

		public static BufferedImage readPNGImage(String filename) {
			try {
				File inputFile = new File(filename);
				BufferedImage sourceImage = ImageIO.read(inputFile);
				return sourceImage;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			return null;
		}

		public static int rgbToGray(int pixels) {
			int _red = pixels >> 16 & 0xFF;
			int _green = pixels >> 8 & 0xFF;
			int _blue = pixels & 0xFF;
			return (int) (0.3D * _red + 0.59D * _green + 0.11D * _blue);
		}

		public static int average(int[] pixels) {
			float m = 0.0F;
			for (int i = 0; i < pixels.length; ++i) {
				m += pixels[i];
			}
			m /= pixels.length;
			return (int) m;
		}
	}
}
