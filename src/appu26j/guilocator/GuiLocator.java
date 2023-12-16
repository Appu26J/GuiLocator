package appu26j.guilocator;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.io.File;

public class GuiLocator
{
    private static Robot robot = null;

    static
    {
        try
        {
            robot = new Robot();
        }

        catch (Exception e)
        {
            ;
        }
    }

    public static int[] locateOnScreen(BufferedImage toFind)
    {
        try
        {
            Rectangle rectangle = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
            BufferedImage bufferedImage = robot.createScreenCapture(rectangle);
            return locate(bufferedImage, toFind);
        }

        catch (Exception e)
        {
            return null;
        }
    }

    public static int[] locateOnScreen(File toFind)
    {
        try
        {
            return locateOnScreen(ImageIO.read(toFind));
        }

        catch (Exception e)
        {
            return null;
        }
    }

    public static int[] locate(BufferedImage toLocateFrom, BufferedImage toFind)
    {
        int[] pos = null;

        for (int x = 0; x < toLocateFrom.getWidth() - toFind.getWidth(); x++)
        {
            for (int y = 0; y < toLocateFrom.getHeight() - toFind.getHeight(); y++)
            {
                BufferedImage subImage = toLocateFrom.getSubimage(x, y, toFind.getWidth(), toFind.getHeight());

                if (isEqual(subImage, toFind))
                {
                    pos = new int[]{x + (toFind.getWidth() / 2), y + (toFind.getHeight() / 2)};
                    break;
                }
            }
        }

        return pos;
    }

    public static int[] locate(File toLocateFrom, File toFind)
    {
        try
        {
            return locate(ImageIO.read(toLocateFrom), ImageIO.read(toFind));
        }

        catch (Exception e)
        {
            return null;
        }
    }

    public static void click(int[] pos)
    {
        if (pos != null)
        {
            try
            {
                int i = 0;

                while ((MouseInfo.getPointerInfo().getLocation().x != pos[0] || MouseInfo.getPointerInfo().getLocation().y != pos[1]) && i++ <= 5)
                {
                    robot.mouseMove(pos[0], pos[1]);
                }

                robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
            }

            catch (Exception e)
            {
                ;
            }
        }
    }

    private static boolean isEqual(BufferedImage image1, BufferedImage image2)
    {
        if (image1.getWidth() != image2.getWidth() || image1.getHeight() != image2.getHeight())
        {
            return false;
        }

        for (int x = 0; x < image1.getWidth(); x++)
        {
            for (int y = 0; y < image1.getHeight(); y++)
            {
                if (image1.getRGB(x, y) != image2.getRGB(x, y))
                {
                    return false;
                }
            }
        }

        return true;
    }
}
