package appu26j.guilocator;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

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

    public static Result locateOnScreen(BufferedImage toFind)
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

    public static Result[] locateAllOnScreen(BufferedImage toFind)
    {
        try
        {
            Rectangle rectangle = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
            BufferedImage bufferedImage = robot.createScreenCapture(rectangle);
            return locateAll(bufferedImage, toFind);
        }

        catch (Exception e)
        {
            return new Result[]{};
        }
    }

    public static Result locateOnScreen(File toFind)
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

    public static Result[] locateAllOnScreen(File toFind)
    {
        try
        {
            return locateAllOnScreen(ImageIO.read(toFind));
        }

        catch (Exception e)
        {
            return new Result[]{};
        }
    }

    public static Result locate(BufferedImage toLocateFrom, BufferedImage toFind)
    {
        AtomicReference<Holder> pos = new AtomicReference<>(new Holder());

        for (int x = 0; x < toLocateFrom.getWidth() - toFind.getWidth(); x++)
        {
            int finalX = x;

            Threads.addThread(() ->
            {
                for (int y = 0; y < toLocateFrom.getHeight() - toFind.getHeight(); y++)
                {
                    BufferedImage subImage = toLocateFrom.getSubimage(finalX, y, toFind.getWidth(), toFind.getHeight());

                    if (isEqual(subImage, toFind))
                    {
                        pos.get().set(new int[]{finalX + (toFind.getWidth() / 2), y + (toFind.getHeight() / 2)});
                        break;
                    }
                }
            });
        }

        while (!Threads.haveFinished())
        {
            try
            {
                ;
            }

            catch (Exception e)
            {
                ;
            }
        }

        int[] array = pos.get().get();
        return array.length == 0 ? null : new Result(array);
    }

    public static Result[] locateAll(BufferedImage toLocateFrom, BufferedImage toFind)
    {
        AtomicReference<ArrayList<Result>> pos = new AtomicReference<>(new ArrayList<>());

        for (int x = 0; x < toLocateFrom.getWidth() - toFind.getWidth(); x++)
        {
            int finalX = x;

            Threads.addThread(() ->
            {
                for (int y = 0; y < toLocateFrom.getHeight() - toFind.getHeight(); y++)
                {
                    BufferedImage subImage = toLocateFrom.getSubimage(finalX, y, toFind.getWidth(), toFind.getHeight());

                    if (isEqual(subImage, toFind))
                    {
                        Result result = new Result(new int[]{finalX + (toFind.getWidth() / 2), y + (toFind.getHeight() / 2)});

                        if (!pos.get().contains(result))
                        {
                            pos.get().add(result);
                        }
                    }
                }
            });
        }

        while (!Threads.haveFinished())
        {
            try
            {
                ;
            }

            catch (Exception e)
            {
                ;
            }
        }

        return pos.get().toArray(new Result[0]);
    }

    public static Result locate(File toLocateFrom, File toFind)
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

    public static Result[] locateAll(File toLocateFrom, File toFind)
    {
        try
        {
            return locateAll(ImageIO.read(toLocateFrom), ImageIO.read(toFind));
        }

        catch (Exception e)
        {
            return new Result[]{};
        }
    }

    public static void leftClickAt(Result result)
    {
        if (result != null)
        {
            try
            {
                int i = 0;

                while ((MouseInfo.getPointerInfo().getLocation().x != result.getX() || MouseInfo.getPointerInfo().getLocation().y != result.getY()) && i++ <= 5)
                {
                    robot.mouseMove(result.getX(), result.getY());
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

    public static void leftClickAt(int[] pos)
    {
        leftClickAt(new Result(pos));
    }

    public static void rightClickAt(Result result)
    {
        if (result != null)
        {
            try
            {
                int i = 0;

                while ((MouseInfo.getPointerInfo().getLocation().x != result.getX() || MouseInfo.getPointerInfo().getLocation().y != result.getY()) && i++ <= 5)
                {
                    robot.mouseMove(result.getX(), result.getY());
                }

                robot.mousePress(InputEvent.BUTTON2_DOWN_MASK);
                robot.mouseRelease(InputEvent.BUTTON2_DOWN_MASK);
            }

            catch (Exception e)
            {
                ;
            }
        }
    }

    public static void rightClickAt(int[] pos)
    {
        rightClickAt(new Result(pos));
    }

    private static boolean isEqual(BufferedImage image1, BufferedImage image2)
    {
        if (image1.getWidth() != image2.getWidth() || image1.getHeight() != image2.getHeight())
        {
            return false;
        }

        int increment = image1.getWidth() / 15;

        for (int x = 0; x < image1.getWidth(); x += increment)
        {
            for (int y = 0; y < image1.getHeight(); y += increment)
            {
                if (image1.getRGB(x, y) != image2.getRGB(x, y))
                {
                    return false;
                }
            }
        }

        return true;
    }

    private static class Holder
    {
        private int[] array;

        public Holder()
        {
            array = new int[]{};
        }

        public int[] get()
        {
            return array;
        }

        public void set(int[] array)
        {
            this.array = array;
        }
    }

    private static class Threads
    {
        private static final ArrayList<Thread> threads = new ArrayList<>();

        public static void addThread(Runnable runnable)
        {
            Thread thread = new Thread(runnable);
            threads.add(thread);
            thread.start();
        }

        public static boolean haveFinished()
        {
            boolean done = true;

            for (int i = 0; i < threads.size(); i++)
            {
                Thread thread = threads.get(i);

                if (thread.isAlive())
                {
                    done = false;
                    break;
                }
            }

            return done;
        }
    }

    public static class Result
    {
        private int[] array;

        public Result(int[] array)
        {
            this.array = array;
        }

        public int getX()
        {
            return this.array[0];
        }

        public int getY()
        {
            return this.array[1];
        }

        @Override
        public String toString()
        {
            return "Result@[" + this.array[0] + ", " + this.array[1] + "]";
        }

        @Override
        public boolean equals(Object obj)
        {
            if (obj instanceof Result)
            {
                Result result = (Result) obj;
                return result.getX() == this.getX() && result.getY() == this.getY();
            }

            return super.equals(obj);
        }
    }
}
