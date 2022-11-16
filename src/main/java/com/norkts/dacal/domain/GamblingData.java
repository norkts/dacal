package com.norkts.dacal.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import com.norkts.dacal.util.WindowQueue;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class GamblingData implements Serializable {

    private static final long serialVersionUID = 1788730643656580028L;
    public RollSummary rollSummary = new RollSummary();
    public PlanetSummary planetSummary = new PlanetSummary();
    public CardSummary cardSummary = new CardSummary();
    public MoheSummary moheSummary = new MoheSummary();


    public static class RollSummary implements Serializable{

        private static final long serialVersionUID = -4324723246665144397L;
        @Getter
        private WindowQueue<String> summaryHistorys = new WindowQueue<>(20);

        public AtomicInteger g2Num = new AtomicInteger(0);
        public AtomicInteger g5Num = new AtomicInteger(0);
        public AtomicInteger g10Num = new AtomicInteger(0);
        public AtomicInteger g100Num = new AtomicInteger(0);

        public AtomicInteger g2AfterG5Num = new AtomicInteger(0);
        public AtomicInteger g2AfterG10Num = new AtomicInteger(0);
        public AtomicInteger g5AfterG10Num = new AtomicInteger(0);
        public AtomicInteger g10AfterG100Num = new AtomicInteger(0);

        public long lastG2Time = System.currentTimeMillis();
        public long lastG5Time = System.currentTimeMillis();
        public long lastG10Time = System.currentTimeMillis();
        public long lastG100Time = System.currentTimeMillis();

        public void onG2(){
            lastG2Time = System.currentTimeMillis();
            g2Num.incrementAndGet();
            g2AfterG5Num.incrementAndGet();
            g2AfterG10Num.incrementAndGet();
        }

        public void onG5(){
            lastG5Time = System.currentTimeMillis();
            g5Num.incrementAndGet();
            g5AfterG10Num.incrementAndGet();
            g2AfterG5Num.set(0);
        }

        public void onG10(){
            lastG10Time = System.currentTimeMillis();
            g10Num.incrementAndGet();
            g10AfterG100Num.incrementAndGet();
            g5AfterG10Num.set(0);
            g2AfterG10Num.set(0);
        }

        public void onG100(){

            lastG100Time = System.currentTimeMillis();
            g100Num.incrementAndGet();
            summaryHistorys.add(getSummary());

            g2Num.set(0);
            g5Num.set(0);
            g10Num.set(0);

            g10AfterG100Num.set(0);

        }

        public String getSummary(){
            return g100Num.get() + "-" + g10Num.get() + "-" + g5Num.get() + "-" + g2Num.get();
        }


        public String getLastGiftTime(){
            long lastTime = Lists.newArrayList(lastG2Time,lastG5Time,lastG10Time).stream()
                    .max(Long::compare)
                    .orElse(System.currentTimeMillis());

            if(lastTime < 1){
                return "00:00";
            }

            long seconds = (System.currentTimeMillis() - lastTime)/1000;
            return String.format("%02d", seconds/60) + ":" + String.format("%02d", seconds%60);
        }


        public String getG2CountText(){
            return String.valueOf(Math.min(g2AfterG5Num.get(), g2AfterG10Num.get()));
        }

        public void clear(){
            g2Num.set(0);
            g5Num.set(0);
            g10Num.set(0);
            g100Num.set(0);
            g2AfterG5Num.set(0);
            g2AfterG10Num.set(0);

            g5AfterG10Num.set(0);
            g10AfterG100Num.set(0);

            lastG10Time = 0;
            lastG100Time = 0;
            lastG2Time = 0;
            lastG5Time = 0;
        }


    }

    public static class PlanetSummary  implements Serializable{
        private static final long serialVersionUID = 8048664744461128010L;
        public AtomicInteger g1Num = new AtomicInteger(0);
        public AtomicInteger g2Num = new AtomicInteger(0);
        public AtomicInteger g10Num = new AtomicInteger(0);
        public AtomicInteger mg10Num = new AtomicInteger(0);
        public AtomicInteger tg10Num = new AtomicInteger(0);
        public AtomicInteger bg10Num = new AtomicInteger(0);

        public AtomicInteger g1afterG2 = new AtomicInteger(0);
        public AtomicInteger g1afterbG10 = new AtomicInteger(0);
        public AtomicInteger g2aftertG10 = new AtomicInteger(0);

        public long lastG1Time = System.currentTimeMillis();
        public long lastG2Time = System.currentTimeMillis();
        public long lastG10Time = System.currentTimeMillis();
        public long lastbG10Time = System.currentTimeMillis();
        public long lasttG10Time = System.currentTimeMillis();
        public long lastmG10Time = System.currentTimeMillis();

        public void onG1(){
            g1Num.incrementAndGet();
            g1afterG2.incrementAndGet();
            g1afterbG10.incrementAndGet();
            lastG1Time = System.currentTimeMillis();
        }

        public void onG2(){
            g2Num.incrementAndGet();
            g2aftertG10.incrementAndGet();
            g1afterG2.set(0);
            lastG2Time = System.currentTimeMillis();
        }

        public void onG10(String type){
            g10Num.incrementAndGet();
            lastG10Time = System.currentTimeMillis();
            if("m".equals(type)){
                lastmG10Time = System.currentTimeMillis();
                mg10Num.incrementAndGet();
            }

            if("t".equals(type)){
                lasttG10Time = System.currentTimeMillis();
                g2aftertG10.set(0);
                tg10Num.incrementAndGet();
                g2Num.set(0);
            }

            if("b".equals(type)){
                lastbG10Time = System.currentTimeMillis();
                bg10Num.incrementAndGet();
                g1afterbG10.set(0);
                g1Num.set(0);
            }
        }


        public String getSummary(){
            return g2aftertG10+"-"+g1afterbG10;
        }


        public String getLastG10TimeText(){
            return getLastbG10TimeText() + "," + getLasttG10TimeText();
        }



        private String getLasttG10TimeText(){
            long lastTime = lasttG10Time;
            if(lastTime < 1){
                lastTime = System.currentTimeMillis();
            }

            long seconds = (System.currentTimeMillis() - lastTime)/1000;
            return String.format("%02d", seconds/60) + ":" + String.format("%02d", seconds%60);
        }

        private String getLastbG10TimeText(){
            long lastTime = lastbG10Time;
            if(lastTime < 1){
                return "00:00";
            }

            long seconds = (System.currentTimeMillis() - lastTime)/1000;
            return "b"+String.format("%02d", seconds/60) + ":" + String.format("%02d", seconds%60);
        }

        public void clear(){
            g1Num.set(0);
            g2Num.set(0 );
            g10Num.set(0);

            g1afterG2.set(0);
            g1afterbG10.set(0);
            g2aftertG10.set(0);
            bg10Num.set(0);
            tg10Num.set(0);
            mg10Num.set(0);

            lastG1Time = 0;
            lastG2Time = 0;
            lastG10Time = 0;
            lastbG10Time = 0;
            lasttG10Time = 0;
            lastmG10Time = 0;
        }
    }

    public static class CardSummary  implements Serializable{
        private static final long serialVersionUID = -1569259857311926915L;
        public AtomicInteger g3Num = new AtomicInteger(0);
        public AtomicInteger g10Num = new AtomicInteger(0);
        public AtomicInteger g50Num = new AtomicInteger(0);

        public long lastG3Time = System.currentTimeMillis();
        public long lastG10Time = System.currentTimeMillis();
        public long lastG50Time = System.currentTimeMillis();
        public long lastG5time = System.currentTimeMillis();
        public long g5TimePeriod = 15*60*1000;

        private final WindowQueue<Long> windowQueue = new WindowQueue<>(5);

        @Getter
        private WindowQueue<String> bigCardSummaryHistorys = new WindowQueue<>(20);

        @Getter
        private WindowQueue<String> yuanYangSummaryHistorys = new WindowQueue<>(20);

        public void onG3(){
            g3Num.incrementAndGet();
            lastG3Time = System.currentTimeMillis();
        }

        public void onG10(){
            g10Num.incrementAndGet();
            lastG10Time = System.currentTimeMillis();
        }

        public void onG50(){
            g50Num.incrementAndGet();
            bigCardSummaryHistorys.add(getBigCardSummary());

            g3Num.set(0);
            g10Num.set(0);
            lastG50Time = System.currentTimeMillis();
        }

        public void onG5(){
            if(lastG5time > 0){
                g5TimePeriod = System.currentTimeMillis() - lastG5time;
                if(g5TimePeriod > 5000){
                    windowQueue.add(g5TimePeriod);
                    yuanYangSummaryHistorys.add(getYuanYangTime());
                }
            }

            lastG5time = System.currentTimeMillis();
        }

        @JsonIgnore
        public String getBigCardSummary(){
            return g10Num.get() + "-" + g3Num.get();
        }


        public String getYuanYangTime(){
            long lastTime = lastG5time;
            if(lastTime < 1){
                lastTime = System.currentTimeMillis();
            }

            long seconds = (System.currentTimeMillis() - lastTime)/1000;
            return String.format("%02d", seconds/60) + ":" + String.format("%02d", seconds%60);
        }


        public String getYuanYangPeriod(){

            if(windowQueue.size() < 1){
                return "00:00";
            }

            long avgPeriod = 0;

            for(Long p : windowQueue.getItems()){
                avgPeriod += p;
            }

            long avgSeconds = avgPeriod/windowQueue.size()/1000;
            return String.format("%02d", avgSeconds/60) + ":" + String.format("%02d", avgSeconds%60);
        }

        public void clear(){
            g3Num.set(0);
            g10Num.set(0);
            g50Num.set(0);

            lastG3Time = 0;
            lastG10Time = 0;
            lastG50Time = 0;
            windowQueue.clear();
        }
    }

    public static class MoheSummary  implements Serializable{
        private static final long serialVersionUID = -3626602473877186731L;
        public AtomicInteger g02Num = new AtomicInteger(0);
        public AtomicInteger g05Num = new AtomicInteger(0);
        public AtomicInteger g1Num = new AtomicInteger(0);
        public AtomicInteger g5Num = new AtomicInteger(0);

        @Getter
        private WindowQueue<String> meheSummaryHistory = new WindowQueue<>();
        public void onG02(){
            g02Num.incrementAndGet();
        }

        public void onG05(){
            g05Num.incrementAndGet();
        }

        public void onG1(){
            g1Num.incrementAndGet();
        }

        public void onG5(){
            g5Num.incrementAndGet();
            meheSummaryHistory.add(getSummary());

            g02Num.set(0);
            g05Num.set(0);
            g1Num.set(0);
        }

        public String getSummary(){
            return g5Num.get()+"-" + g1Num.get() + "-" + g05Num.get() + "-" + g02Num.get();
        }
    }
}

