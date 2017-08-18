/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.primefinder;

import java.io.*;

/**
 *
 */
public class Control extends Thread {

    private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    public static Object obj = new Object();
    private String waiting = Thread.State.WAITING.toString();
    private String terminated = Thread.State.TERMINATED.toString();
    private int time;
    
    
    private final static int NTHREADS = 3;
    private final static int MAXVALUE = 30000000;
    private final static int TMILISECONDS = 5000;

    private final int NDATA = MAXVALUE / NTHREADS;

    private PrimeFinderThread pft[];
    
    private Control(int time) {
        super();
        this.pft = new  PrimeFinderThread[NTHREADS];

        int i;
        this.time = time;
        for(i = 0;i < NTHREADS - 1; i++) {
            PrimeFinderThread elem = new PrimeFinderThread(i*NDATA, (i+1)*NDATA);
            pft[i] = elem;
        }
        pft[i] = new PrimeFinderThread(i*NDATA, MAXVALUE + 1);
    }
    
    public static Control newControl(int time) {
        return new Control(time);
    }

    @Override
    public void run() {
        for(int i = 0;i < NTHREADS;i++ ) {
            pft[i].start();
        }
        int fin = 1;
        int acabar = 0;
        while(fin!=0 || acabar>NTHREADS){
            try{
                Thread.sleep(time);
            }catch(InterruptedException e){}
            for(int i = 0; i < NTHREADS; i++){ pft[i].changeDormir(); }
            int n = 0;
            int j = 0;
            while(n<NTHREADS){
                if(pft[j].getState().toString().equals(waiting)){
                    System.out.println(pft[j].getPrimes().size());
                    n++;
                    j++;
                }
            }
            try{
                fin = Integer.parseInt(br.readLine());
            }catch(IOException ex){}
            if(fin!=0){
                for(int i = 0; i < NTHREADS; i++){
                    pft[i].changeDormir();
                    if(pft[i].getState().toString().equals(terminated)) acabar++;
                }
                synchronized(obj){
                    obj.notifyAll();
                }
            }
        }
    }
    
}
