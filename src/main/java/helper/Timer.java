package helper;

import java.util.HashMap;

public class Timer {
    private static HashMap<String, TimeTuple> timers = new HashMap<>();

    public static void start(String ID, String type) {
        if(timers.get(ID) != null && timers.get(ID).isActive()) {
            System.out.println("WARNING: A timer is already active for the ID \"" + ID + "\" and a new one cannot be started");
            System.exit(1);
        } else if(timers.get(ID) == null) {
            if(type.equals("milli")) {
                timers.put(ID, new TimeTuple(System.currentTimeMillis(), "milli"));
            } else if(type.equals("nano")) {
                timers.put(ID, new TimeTuple(System.nanoTime(), "nano"));
            }
        } else {
            if(type.equals("milli")) {
                timers.get(ID).start(System.currentTimeMillis());
            } else if(type.equals("nano")) {
                timers.get(ID).start(System.nanoTime());
            }
        }
    }

    public static void stop(String ID, boolean printResult) {
        if(timers.get(ID) == null) {
            System.out.println("WARNING: No timer with ID \"" + ID + "\" exists and therefore cannot be stopped");
            System.exit(1);
        } else {
            String type = timers.get(ID).getType();

            if(type.equals("milli")) {
                timers.get(ID).stop(System.currentTimeMillis());
            } else if(type.equals("nano")) {
                timers.get(ID).stop(System.nanoTime());
            }

            if(printResult) {
                if(type.equals("milli")) {
                    System.out.println(ID + " finished (" + formatMilliTime(timers.get(ID).getTime()) + ")");
                } else if(type.equals("nano")) {
                    System.out.println(ID + " finished (" + formatNanoTime(timers.get(ID).getTime()) + ")");
                }
            }
        }
    }

    private static String formatMilliTime(long milliseconds) {
        long minutes = milliseconds / (60 * 1000);
        long secondsTotal = milliseconds / 1000;
        long seconds = secondsTotal % 60;
        long remainingMilliseconds = milliseconds % 1000;
    
        StringBuilder formattedTime = new StringBuilder();
    
        if (minutes > 0) {
            formattedTime.append(minutes).append(" min ");
        }
    
        if (seconds > 0 || minutes > 0) {
            formattedTime.append(seconds).append(" sec ");
        }
    
        formattedTime.append(remainingMilliseconds).append(" ms");
    
        return formattedTime.toString();
    }
    
    private static String formatNanoTime(long nanoseconds) {
        long secondsTotal = nanoseconds / 1_000_000_000;
        long remainingNanoseconds = nanoseconds % 1_000_000_000;
        long milliseconds = remainingNanoseconds / 1_000_000;
        long remainingNanosecondsAfterMilliseconds = remainingNanoseconds % 1_000_000;
    
        StringBuilder formattedTime = new StringBuilder();
    
        if (secondsTotal > 0) {
            formattedTime.append(secondsTotal).append(" sec ");
        }
    
        if (milliseconds > 0 || secondsTotal > 0) {
            formattedTime.append(milliseconds).append(" ms ");
        }
    
        formattedTime.append(remainingNanosecondsAfterMilliseconds).append(" ns");
    
        return formattedTime.toString();
    }
    
    
    static class TimeTuple {
        private long startTime;
        private long endTime;
        private boolean active = false;
        private String type = "";

        public TimeTuple(long startTime, String type) {
            this.startTime = startTime;
            this.type = type;
            active = true;
        }

        public boolean isActive() {
            return active == true;
        }

        public void start(long startTime) {
            this.startTime = startTime;
            active = true;
        }

        public void stop(long endTime) {
            this.endTime = endTime;
            active = false;
        }

        public long getTime() {
            return endTime - startTime;
        }

        public String getType() {
            return type;
        }
    }
}
