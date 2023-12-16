package appu26j.guilocator;

import java.util.ArrayList;

public class Threads
{
    private static final ArrayList<Thread> threads = new ArrayList<>();

    public static void addThread(Runnable runnable)
    {
        Thread thread = new Thread(runnable);
        threads.add(thread);
        thread.start();
    }

    public static boolean hasThreadsFinished()
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
