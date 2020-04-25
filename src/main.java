import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class main {

	public static void main(String[] args) throws Exception {
		String desktopPath = "C:\\Users\\jzhao\\Desktop\\";
		String imageName = "pink.png";
		int horiPixelCount = 32;
		int vertiPixelCount = 32;
		BufferedImage original = ImageIO.read(new File(desktopPath + imageName));
		BufferedImage pixelArt = toPixelArt(original, horiPixelCount, vertiPixelCount);
		ImageIO.write(pixelArt, "png", new File(desktopPath + "PIXEL_" + imageName));
	}

	public static BufferedImage toPixelArt(BufferedImage original, int horiPixelCount, int vertiPixelCount) {
		// return a bufferedimage that is the same size as the original image but
		// pixelized
		int pixelWidth = (int) Math.round(original.getWidth() / ((double) horiPixelCount));
		int pixelHeight = (int) Math.round(original.getHeight() / ((double) vertiPixelCount));
		BufferedImage pixelArt = new BufferedImage(pixelWidth * horiPixelCount, pixelHeight * vertiPixelCount,
				BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics = pixelArt.createGraphics();
		graphics.setPaint(Color.WHITE);
		graphics.fillRect(0, 0, pixelArt.getWidth(), pixelArt.getHeight());
		graphics.drawImage(original, 0, 0, Color.WHITE, null);
		int[][][] originalPixels = convertTo2DIntArr(pixelArt);

		for (int i = 0; i < vertiPixelCount; i++) {
			for (int ii = 0; ii < horiPixelCount; ii++) {
				long A = 0;
				long R = 0;
				long G = 0;
				long B = 0;
				for (int y = 0; y < pixelHeight; y++) {
					for (int x = 0; x < pixelWidth; x++) {
						A += originalPixels[0][i * pixelHeight + y][ii * pixelWidth + x];
						R += originalPixels[1][i * pixelHeight + y][ii * pixelWidth + x];
						G += originalPixels[2][i * pixelHeight + y][ii * pixelWidth + x];
						B += originalPixels[3][i * pixelHeight + y][ii * pixelWidth + x];
					}
				}
				A /= pixelHeight * pixelWidth;
				R /= pixelHeight * pixelWidth;
				G /= pixelHeight * pixelWidth;
				B /= pixelHeight * pixelWidth;

				int argb = 0;
				argb += (((int) A & 0xff) << 24); // alpha
				argb += (((int) R & 0xff) << 16); // red
				argb += (((int) G & 0xff) << 8); // green
				argb += ((int) B & 0xff); // blue

				for (int y = 0; y < pixelHeight; y++) {
					for (int x = 0; x < pixelWidth; x++) {
						pixelArt.setRGB(ii * pixelWidth + x, i * pixelHeight + y, argb);
					}
				}

				// black grid
				for (int y = 0; y < pixelHeight; y++) {
					pixelArt.setRGB(ii * pixelWidth, i * pixelHeight + y, 0);
				}
				for (int x = 0; x < pixelWidth; x++) {
					pixelArt.setRGB(ii * pixelWidth + x, i * pixelHeight, 0);
				}
			}
		}

		return pixelArt;
	}

	public static int[][][] convertTo2DIntArr(BufferedImage image) {

		final int width = image.getWidth();
		final int height = image.getHeight();

		int[][][] result = new int[4][height][width];

		for (int pixel = 0, row = 0, col = 0; pixel < width * height; pixel++) {
			int curPixel = image.getRGB(col, row);
			result[0][row][col] = ((int) curPixel & 0xFF000000) >> 24; // a
			result[1][row][col] = ((int) curPixel & 0x00FF0000) >> 16; // r
			result[2][row][col] = ((int) curPixel & 0x0000FF00) >> 8; // g
			result[3][row][col] = ((int) curPixel & 0x000000FF); // b
			col++;
			if (col == width) {
				col = 0;
				row++;
			}
		}

		return result;
	}

}
