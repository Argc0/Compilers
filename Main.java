import syntaxtree.*;
import visitor.*;
import java.io.*;
import java.util.*;

class Main {
  public static void main (String [] args){
    if(args.length == 0){
		  System.err.println("Usage: java Driver <inputFile1> <inputFile2> ...");
		  System.exit(1);
	  }
	  FileInputStream fis = null;
    BufferedWriter bw =null;
	  try{
		  for(int i=0; i< args.length; i++){
        fis = new FileInputStream(args[i]);
	    	MiniJavaParser parser = new MiniJavaParser(fis);
	    	System.err.println("Program parsed successfully.");
        bw = new BufferedWriter(new FileWriter(args[i].replace(".java",".ll")));      //o buffer gia tin egrafi tou metaglwttismenou arxeiou
        MJVisitor1 mnj1 = new MJVisitor1();
        MJVisitor2 mnj2 = new MJVisitor2();
	    	MJVisitor mnj = new MJVisitor(bw);
	    	Goal root = parser.Goal();
	    	SymbolInfo sinfo = new SymbolInfo();
	    	sinfo._class=null;
	    	sinfo._method=null;
	    	sinfo.table=new LinkedHashMap<String,ClassInfo>();
	    	try{
	    	  root.accept(mnj1, sinfo);                               //klisi tou prwtou visitor gia silogi pliroforiwn
          sinfo._class=null;
          sinfo._method=null;
          try{
            root.accept(mnj2, sinfo);                             //klisi tou deuterou visitor gia elexno tou programmatos mazi me tis plirofories pou parixthisan
            /*
            for(String s: sinfo.table.keySet()){                  //ektipwsi offset table
              if(sinfo.table.get(s).vartable!=null){
                System.out.println("-----------Class " + s + "-----------");
                System.out.println("---Variables---");
                for(String s1: sinfo.table.get(s).vartable.keySet()){
                  if(s1!=null)
                    System.out.println(s + "." + s1 + " : " + sinfo.table.get(s).vartable.get(s1).offset);
                }
              }else 
                continue;
              if(sinfo.table.get(s).methodtable!=null){
                System.out.println("---Methods---");
                for(String s2: sinfo.table.get(s).methodtable.keySet()){
                  Boolean b = sinfo.table.get(s).methodtable.get(s2).overwrite;
                  if(b == null || b == false)
                    System.out.println(s + "." + s2 + " : " + sinfo.table.get(s).methodtable.get(s2).offset);
                }
              }
            }
            */
            try{
              sinfo.want_identifier=false;                                  //arxikopoihsh tou want identifier
              root.accept(mnj,sinfo);                                       //klisi tou tritou visitor gia metaglwtii tou programmatos se llvm
              bw.close();
            }catch(Exception e){
              System.out.println(args[i] + "-" + e);
              e.printStackTrace();
            }

          }catch (Exception e){
            System.out.println(args[i] +"-"+e);
          }
	    	}catch(Exception e){
	    		System.out.println(args[i] + "-" + e);
	    	}
	    }
	  }catch(ParseException ex){
	    System.out.println(ex.getMessage());
    }catch(IOException ex){
        System.err.println(ex.getMessage()); 
	  }finally{
      try{
		    if(fis != null) fis.close();
        if( bw != null) bw.close();
      }catch(IOException ex){
        System.err.println(ex.getMessage());
      }
	  }
  }
}
