package race;

import java.util.*;

//Lab: Concurrent Programming 
//
//Description: You are to examine race conditions and ways to prevent them
//
//
//1. Using the code below, add two Threads (or more) that add a series of values
//  to the ArrayList provided. The values should be somewhat random. 
//  ArrayList insert() is suppose to create a list of values in-order
//  Your threads should insert their values into the SAME list
//  You should alter the code such that you EXACERBATE the possible race condition
//  <4 marks>
//
//2. Provide 2 different ways to prevent your race condition using techniques discussed
// Specifically, use sycnhronized on "something" - do not just put synchronized on the method header
// Also use ReentrantLock as your second technique.
// <4 marks>

class ArrayList{
  int pos = 0;
  int[] list;
  public ArrayList(){
      list = new int[0];
  }
  public boolean insert(int a){
      int cur = 0;
      if (list.length == 0){//first element added to a list
          list = new int[1];
          list[0] = a;
          return true;
      }

      while((cur < list.length) && (a > list[cur])){//find location for new element
          cur++;
      }

      return insert(a, cur);
  }
  private boolean insert(int a, int p){

      int[] temp = new int[list.length+1];
      if (p-1 > 0)//add values from first part of old list up to new spot
          System.arraycopy(list, 0, temp, 0, p);
      
      temp[p] = a;//add new value
      
      if(list.length-p > 0)//add values from last part of old list
          System.arraycopy(list, p, temp, p+1, list.length-p);
          
      list = temp;//replace list

      return true;
  }
  public void print(){
      for(int a: list)
          System.out.print(""+a+" ");
  }
}

class A extends Thread{
    int[] list;
    ArrayList al;
    public A(ArrayList al, int[] list) {
        this.al = al;
        this.list = list;
    }

    public void run() {
        for (int i: list) {
            al.insert(i);
        }
    }

}


public class Race {
  public static void main(String[] args){
      ArrayList l = new ArrayList();
      int[] test = {4,2,5,7,1,8};//just a test set of values for list
      int[] test2 = {1,11,3,9,5};
      
      
      A a1 = new A(l,test);
      A a2 = new A(l,test2);
      a1.start();
      a2.start();
      try {
          a1.join();
          a2.join();
      } catch (Exception e) {
          
      }
      //Thread.sleep(100);
      
      //Q2
      synchronized(l) {
          
          for (int i = 0; i < 5; i++) {
             // System.out.println(l[i]);
          }
      }
      l.print();
  }
}
