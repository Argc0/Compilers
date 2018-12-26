import syntaxtree.*;
import visitor.GJDepthFirst;
import java.util.*;
import java.io.*;

public class MJVisitor extends GJDepthFirst<String, SymbolInfo> {
  private final BufferedWriter writer;
  private int curvar;
  private int curlab;

  public MJVisitor(BufferedWriter fout) { this.writer=fout; }

  private String nextVar(){                                                                 //sinartisi pou epistrefei ton epomeno register
    this.curvar +=1;
    return "%_" + (curvar-1);
  }

  private String nextLabel(String s){                                                       //sinartisi pou epistrefei tin epomeni etiketa analoga me to tag(String s) pou exei dothei
    this.curlab +=1;
    return s + (curlab-1);
  }

  private void emit(String s){                                                              //eggrafi tou string ston buffer
    try{
      writer.write(s);
      writer.newLine();
    }catch(IOException ex){
      System.err.println(ex.getMessage());
    }
  }

  /**
  * f0 -> MainClass()
  * f1 -> ( TypeDeclaration() )*
  * f2 -> <EOF>
  */
  public String visit(Goal n, SymbolInfo argu) throws Exception {
    String _ret=null,str="",stri="",stro="",com=", ",h1="";
    int counter=0;
    int sizi=0;
    Boolean bool=false;
    String d_class=null, u_class=null,c;
    for(String s : argu.table.keySet()){                                                    //ektipwsi tou vtable kathe klasis analoga me tin klironomikotita tis
      d_class=s;                                                                            //d_class=o goneas klasi klassi gia elenxo
      if(argu.table.get(s).methodtable.containsKey("main")) continue;                       //perptwsi pou vroume einai h klasi pou exei th main na ginei skip
      stro="";                                                                              //krataei tis sinartisus kathe klasis
      h1="";                                                                                //voithitiki metavliti gia na swsta emfanisimo to vtable
      while(d_class != null){
        if(argu.table.get(d_class).methodtable.containsKey("main")) break;                  //periptwsi pou vroume tin klasi pou exei ti main teleionei to psa3imo
        h1=stro;
        stro="";
        if(argu.table.get(d_class).methodtable != null){
          counter=0;
          bool=false;
          for(String m : argu.table.get(d_class).methodtable.keySet()){
            MethodInfo minfo = argu.table.get(d_class).methodtable.get(m);
            if(minfo.overwrite==null || minfo.overwrite==false){                            //ama einai mia sinartisi overwrite na min 3anagraftei
              if(argu.table.get(s).methodtable.containsKey(m)){                             //elexno an h current klasi exei mia methodo overwrite methodo h oxi 
                u_class=s;                                                                  //periptwsi pou iparxei overwrite ara to onoma tis sinartisis xreiazete allagi
              }else{
                u_class=d_class;                                                            //periptwsi pou den iparxei overwrite auti tis methodo se parakatw klasis ara prepei na perastei auti h sinartisi kai stous apogonous tis                                      
              }
              stri="";
              for(String f : minfo.formaltable.values()){                                   //grapsimo ton tipon twn parametrwn tis sinartisis se llvm
                if(f.equals("int")){
                  stri = stri + ", i32";
                }else if(f.equals("int[]")){
                  stri = stri + ", i32*";
                }else if(f.equals("boolean")){
                  stri = stri + ", i1";
                }else{
                  stri = stri + ", i8*";
                }
              }
              c=argu.table.get(d_class).extend_class_name;
              if(c != null){
                while(c != null){                                                           //psaxnw an einai adeio pros ta panw (peripwsi pou mia sinartisi einai h prwti prwti ki den xreaizetai komma)
                  if(argu.table.get(c).methodtable.containsKey("main") || argu.table.get(c).methodtable.size() == 0)
                    bool=true;
                  else{
                    bool=false;
                    break;
                  }
                  c=argu.table.get(c).extend_class_name;
                }
              }
              if((counter==0) && (argu.table.get(d_class).extend_class_name==null || bool==true)){                          //periptwsi pou einai h prwti prwti sinartisi mesa se mia klasi kai kamia apo tis parapanw den exei alli methodo (na min 3ekinaei me komma)
                if((minfo.return_type).equals("int")){
                  stro =  "i8* bitcast (i32 (i8*" + stri + ")* @" + u_class + "." + m + " to i8*)"; 
                }else if((minfo.return_type).equals("int[]")){
                  stro =   "i8* bitcast (i32* (i8*" + stri + ")* @" + u_class + "." + m + " to i8*)"; 
                }else if((minfo.return_type).equals("boolean")){
                  stro = "i8* bitcast (i1 (i8*" + stri + ")* @" + u_class + "." + m + " to i8*)"; 
                }else{
                  stro = "i8* bitcast (i8* (i8*" + stri + ")* @" + u_class + "." + m + " to i8*)"; 
                }
              }else{
                if((minfo.return_type).equals("int")){
                  stro = stro + ", i8* bitcast (i32 (i8*" + stri + ")* @" + u_class + "." + m + " to i8*)"; 
                }else if((minfo.return_type).equals("int[]")){
                  stro = stro + ", i8* bitcast (i32* (i8*" + stri + ")* @" + u_class + "." + m + " to i8*)"; 
                }else if((minfo.return_type).equals("boolean")){
                  stro = stro + ", i8* bitcast (i1 (i8*" + stri + ")* @" + u_class + "." + m + " to i8*)"; 
                }else{
                  stro = stro + ", i8* bitcast (i8* (i8*" + stri + ")* @" + u_class + "." + m + " to i8*)"; 
                }
              }
              counter++;                                                                    //metraei ti seira sinartisewn mesa se klasi (paralipete to overwrite afou vriskete se pio panw klasi)
            }
          }
        }
        d_class=argu.table.get(d_class).extend_class_name;
        stro = stro + h1;                                                                   //to sinoliko string mpainei mprosta gt 3ekiname apo katw ki pame pros ta panw
      }

      sizi =  argu.table.get(s).offset_end_meth / 8;                                        //ipologismos plithous sinartisewn me vasi to offset afou gnwrisoume oti kathemethodos exei 8 byte
      sizi = sizi + 1;
      if((argu.table.get(s).methodtable.size()==0) || (argu.table.get(s).methodtable == null)){
        if(argu.table.get(s).offset_end_meth==0 && stro.equals(""))                         //periptwsi pou den iparxei kamia methodo
          str = str + "@." + s + "_vtable = global [0 x i8*] []\n";
        else                                                                                //periptwsi pou den iparxei kamia methodo sthn current klasi alla einai paidi mias klasis pou exei methodous
          str = str + "@." + s + "_vtable = global [" + sizi + " x i8*] [" + stro + "]\n";
      }else{
        str = str + "@." + s + "_vtable = global [" + sizi + " x i8*] [" + stro + "]\n";
      }
    }
    emit(str + "\n");
    n.f0.accept(this, argu);
    n.f1.accept(this, argu);
    n.f2.accept(this, argu);
    return _ret;
  }
  /**
  * f0 -> "class"
  * f1 -> Identifier()
  * f2 -> "{"
  * f3 -> "public"
  * f4 -> "static"
  * f5 -> "void"
  * f6 -> "main"
  * f7 -> "("
  * f8 -> "String"
  * f9 -> "["
  * f10 -> "]"
  * f11 -> Identifier()
  * f12 -> ")"
  * f13 -> "{"
  * f14 -> ( VarDeclaration() )*
  * f15 -> ( Statement() )*
  * f16 -> "}"
  * f17 -> "}"
  */
  public String visit(MainClass n, SymbolInfo argu) throws Exception{
    String _ret=null;                                                                       //emit simfona me ta stoixeia stin ekfonisi
    emit("declare i8* @calloc(i32, i32)\n" +
        "declare i32 @printf(i8*, ...)\n"+
        "declare void @exit(i32)\n");
    emit("@_cint = constant [4 x i8] c\"%d\\0a\\00\"\n" +
        "@_cOOB = constant [15 x i8] c\"Out of bounds\\0a\\00\"\n");
    emit("define void @print_int(i32 %i) {\n" +
          "\t%_str = bitcast [4 x i8]* @_cint to i8*\n" +
          "\tcall i32 (i8*, ...) @printf(i8* %_str, i32 %i)\n" +
          "\tret void\n}\n"+
        "define void @throw_oob() {\n" +
          "\t%_str = bitcast [15 x i8]* @_cOOB to i8*\n" +
          "\tcall i32 (i8*, ...) @printf(i8* %_str)\n" +
          "\tcall void @exit(i32 1)\n" +
          "\tret void\n}\n"
    );
    emit("define i32 @main() {");
    n.f0.accept(this, argu);
    String str1 = n.f1.accept(this, argu);
    n.f2.accept(this, argu);
    n.f3.accept(this, argu);
    n.f4.accept(this, argu);
    n.f5.accept(this, argu);
    n.f6.accept(this, argu);
    n.f7.accept(this, argu);
    n.f8.accept(this, argu);
    n.f9.accept(this, argu);
    n.f10.accept(this, argu);
    String str= n.f11.accept(this, argu);
    n.f12.accept(this, argu);
    argu._class=str1;
    argu._method="main";
    n.f13.accept(this, argu);
    n.f14.accept(this, argu);
    n.f15.accept(this, argu);
    n.f16.accept(this, argu);
    n.f17.accept(this, argu);
    emit("\tret i32 0\n}\n");
    return _ret;
  }
  /**
  * f0 -> "class"
  * f1 -> Identifier()
  * f2 -> "{"
  * f3 -> ( VarDeclaration() )*
  * f4 -> ( MethodDeclaration() )*
  * f5 -> "}"
  */
  public String visit(ClassDeclaration n, SymbolInfo argu) throws Exception {
    String _ret=null,str;
    n.f0.accept(this, argu);
    str=n.f1.accept(this, argu);
    argu._class=str;                                                                        //pernaei to onoma klasis stis metavlites ki methodous tis
    argu._method=null;
    n.f2.accept(this, argu);
    n.f3.accept(this, argu);
    n.f4.accept(this, argu);
    n.f5.accept(this, argu);
    return _ret;
  }
  /**
  * f0 -> "class"
  * f1 -> Identifier()
  * f2 -> "extends"
  * f3 -> Identifier()
  * f4 -> "{"
  * f5 -> ( VarDeclaration() )*
  * f6 -> ( MethodDeclaration() )*
  * f7 -> "}"
  */
  public String visit(ClassExtendsDeclaration n, SymbolInfo argu) throws Exception {
    String _ret=null,str;
    n.f0.accept(this, argu);
    str=n.f1.accept(this, argu);
    argu._class=str;
    argu._method=null;
    n.f2.accept(this, argu);
    n.f3.accept(this, argu);
    n.f4.accept(this, argu);
    n.f5.accept(this, argu);
    n.f6.accept(this, argu);
    n.f7.accept(this, argu);
    return _ret;
  }
  /**
  * f0 -> "public"
  * f1 -> Type()
  * f2 -> Identifier()
  * f3 -> "("
  * f4 -> ( FormalParameterList() )?
  * f5 -> ")"
  * f6 -> "{"
  * f7 -> ( VarDeclaration() )*
  * f8 -> ( Statement() )*
  * f9 -> "return"
  * f10 -> Expression()
  * f11 -> ";"
  * f12 -> "}"
  */
  public String visit(MethodDeclaration n, SymbolInfo argu) throws Exception {
    String _ret=null,t,t2,str,str1="",str2="";
    n.f0.accept(this, argu);
    t=n.f1.accept(this, argu);
    t2=n.f2.accept(this, argu);
    argu._method=t2;
    argu.strp = new ArrayList<FormalInfo>();                                                //ayto to arraylist einai gia ti seira pou dilontai oi metavlites kai na ginoun ki ta katallila alloca
    str = "define " + t + " @" + argu._class + "." + t2 + "(i8* %this";
    n.f4.accept(this,argu);
    for(FormalInfo f : argu.strp){                                                          //metatropi onoma twn ki tipwn se llvm
      str1 = str1 + ", " + f.type + " %." + f.name;
      str2 = str2 + "\t%"+ f.name +" = alloca " + f.type +"\n" + "\tstore " + f.type + " %." + f.name + ", " + f.type +"* %" + f.name + "\n";
    }
    str = str + str1 + ") {";
    emit(str);
    n.f5.accept(this, argu);
    emit(str2);
    n.f6.accept(this, argu);
    n.f7.accept(this, argu);
    n.f8.accept(this, argu);
    n.f9.accept(this, argu);
    emit("\n\tret " + t + " " + n.f10.accept(this, argu) );
    n.f11.accept(this, argu);
    n.f12.accept(this, argu);
    emit("}\n");
    return _ret;
  }
  /**
  * f0 -> Type()
  * f1 -> Identifier()
  */
  public String visit(FormalParameter n, SymbolInfo argu) throws Exception {
    String _ret=null,str1,str2;
    FormalInfo finfo = new FormalInfo();                                                    //kratei onoma kai tipo me ti seira etsi wste na grafei swsta o orismos tis sinartisis sto llvm
    finfo.type=n.f0.accept(this,argu);
    finfo.name=n.f1.accept(this,argu);
    argu.strp.add(finfo); 
    return _ret;
  }
  /**
  * f0 -> Type()
  * f1 -> Identifier()
  * f2 -> ";"
  */
  public String visit(VarDeclaration n, SymbolInfo argu) throws Exception {
    String _ret=null;
    if(argu._method!=null)                                                                  //periptwsi pou to vardeclaration proerxetai mesa apo mia methodo
      emit("\t%" + n.f1.accept(this,argu) + " = alloca " + n.f0.accept(this,argu) + "\n");
    return _ret;
  }
  /**
  * f0 -> ArrayType()
  *       | BooleanType()
  *       | IntegerType()
  *       | Identifier()
  */
  public String visit(Type n, SymbolInfo argu) throws Exception {
    if(n.f0.which==3){                                                                      //periptwsi identifier otan epistrefotai oi tipoi
      return "i8*";
    }else
      return n.f0.accept(this, argu);
  }
  /**
  * f0 -> Identifier()
  * f1 -> "="
  * f2 -> Expression()
  * f3 -> ";"
  */
  public String visit(AssignmentStatement n, SymbolInfo argu) throws Exception {
    String _ret=null,str1,str2,v1;
    int off=0;
    str1=n.f0.accept(this, argu);
    argu.want_identifier=false;                                                             //periptwsi pou den thelw na epistepsei identifier alla register
    str2=n.f2.accept(this, argu);
    if(str2.equals("")) return "";                                                          //periptwsi pou kanoume new tin klasi pou exei ti main
    String type=null;
    if(str1 != null){                                                                       //pasxnoume to offset tis metavlitis pou epistrefei to f2 an auti vriskete se pedio klasis
      if(argu._class==null) throw new Exception("Error3: There is no class for identifier");    
      ClassInfo cinfo = argu.table.get(argu._class);                                        //periptwsi pou psaxnoume se olo ton pinaka ana epipedo ton typo tis metavlitis ki ton epistrefoume
      if(argu._method==null) throw new Exception("Error3: There is no method for identifier");
      if(cinfo.methodtable.get(argu._method).vartable!=null){
        type = cinfo.methodtable.get(argu._method).vartable.get(str1);
      }
      if((type==null)&&(cinfo.methodtable.get(argu._method).formaltable!=null)){
        type=cinfo.methodtable.get(argu._method).formaltable.get(str1);
      }
      if((type==null)&&(cinfo.vartable!=null)){
        while((type==null)&&(cinfo.vartable!=null)){
          if(cinfo.vartable.get(str1)!=null){
            type=cinfo.vartable.get(str1).type;
            off=cinfo.vartable.get(str1).offset + 8;                                        //offset for variable
          }
          if(argu.table.get(cinfo.extend_class_name)!=null){
            cinfo=argu.table.get(cinfo.extend_class_name);
          }else 
            break;
        }
        v1 = nextVar();
        str1=nextVar();
        emit("\t" + v1 + " = getelementptr i8, i8* %this, i32 " + off);
        if(type.equals("int")){                                                             //case variable is a field
          emit("\t" + str1 + " = bitcast i8* " + v1 + " to i32*");
          emit("\tstore i32 " + str2 + ", i32* " + str1 + "\n");
        }else if(type.equals("int[]")){
          emit("\t" + str1 + " = bitcast i8* " + v1 + " to i32**");
          emit("\tstore i32* " + str2 + ", i32** " + str1 + "\n");
        }else if(type.equals("boolean")){
          emit("\t" + str1 + " = bitcast i8* " + v1 + " to i1*");
          emit("\tstore i1 " + str2 + ", i1* " + str1 + "\n");
        }else{
          emit("\t" + str1 + " = bitcast i8* " + v1 + " to i8**");
          emit("\tstore i8* " + str2 + ", i8** " + str1 + "\n");
        }
        return _ret;
      }
    }
    if(type.equals("int")){                                                                 //case variable is not a field but declared inside a method
      emit("\tstore i32 " + str2 + ", i32* %" + str1 + "\n");
    }else if(type.equals("int[]")){
      emit("\tstore i32* " + str2 + ", i32** %" + str1 + "\n");
    }else if(type.equals("boolean")){
      emit("\tstore i1 " + str2 + ", i1* %" + str1 + "\n");
    }else{
      emit("\tstore i8* " + str2 + ", i8** %" + str1 + "\n");
    }
    return _ret;
  }
  /**
  * f0 -> PrimaryExpression()
  * f1 -> "."
  * f2 -> Identifier()
  * f3 -> "("
  * f4 -> ( ExpressionList() )?
  * f5 -> ")"
  */
  public String visit(MessageSend n, SymbolInfo argu) throws Exception {
    String _ret=null,t,s1,s2,v1,v2,v3,v4,v5,v6,str="",rt,p;
    MethodInfo minfo = null;
    Boolean b=false;
    v1=nextVar();
    v2=nextVar();
    v3=nextVar();
    v4=nextVar();
    v5=nextVar();
    v6=nextVar();
    if(argu.want_identifier==true){                                                         //periptwsi pou theloun ton tipo epistrofis mias sinartisis
      argu.want_identifier=true;
      t=n.f0.accept(this, argu);
      s2=n.f2.accept(this, argu);
      if(argu.table.get(t).methodtable != null){
        p=t;
        while(p != null){
          if(argu.table.get(p).methodtable.containsKey(s2)){
            minfo = argu.table.get(p).methodtable.get(s2);
            break;
          }
          p=argu.table.get(p).extend_class_name;
        }
      }
      if(minfo==null) throw new Exception("Error3: Something wrong finding method");
      return minfo.return_type;
    }else{                                                                                  //periptwsi ektelesis tou messagesend
      argu.want_identifier=true;                                                            //theloume epistrofi identifier
      t=n.f0.accept(this, argu);
      argu.want_identifier=false;                                                           //theloume epistrofi register(diladi mias timis)
      s1=n.f0.accept(this, argu);
      s2=n.f2.accept(this, argu);
      argu.strp= new ArrayList<FormalInfo>();
      if(argu.table.get(t).methodtable != null){
        p=t;
        while(p!=null){
          if(argu.table.get(p).methodtable.containsKey(s2)){
            minfo = argu.table.get(p).methodtable.get(s2);                                  //evresi pliroforiwn tis sinartisis apo to symbol table
            break;
          }
          p=argu.table.get(p).extend_class_name;
        }
      }
      if(minfo==null) throw new Exception("Error3: Something wrong finding method");
      n.f4.accept(this, argu);
      if((minfo.return_type).equals("int")){                                                //elexnos tou tipou epistrofis gia na grafei to swsto type gia to llvm
        rt="i32";
      }else if((minfo.return_type).equals("int[]")){
        rt="i32*";
      }else if((minfo.return_type).equals("boolean")){
        rt="i1";
      }else{
        rt="i8*";
      } 
      ArrayList<String> xc = new ArrayList<String>(argu.table.get(t).methodtable.keySet());
      int iof = minfo.offset / 8;                                                           //se poia thesi vriskete h sinartisi sto vtable vasi tou offset 
      emit("\t" + v1 + " = bitcast i8* " + s1 + " to i8***");
      emit("\t" + v2 + " = load i8**, i8*** " + v1);
      emit("\t" + v3 + " = getelementptr i8*, i8** " + v2 + ", i32 " + iof);
      emit("\t" + v4 + " = load i8*, i8** " + v3);
      for(String r : minfo.formaltable.values()){                                           //seira ektipwsis orismatwn sinartisis kai metatropi  se typous llvm
        if(r.equals("int")){
          str = str + ", i32";
        }else if(r.equals("int[]")){
          str = str + ", i32*";
        }else if(r.equals("boolean")){
          str = str + ", i1";
        }else{
          str = str + ", i8*";
        }
      }
      emit("\t" + v5 + " = bitcast i8* " + v4 + " to " + rt + " (i8*" + str + ")*");
      int i=0;
      String x;
      str="";
      for (String r : minfo.formaltable.values()) {                                         //seira ektipwsis e3presions sinartisis 
        FormalInfo f = argu.strp.get(i);                                                    //apothikeuei tous registers twn expression
        x=f.name;                                                                           //register enos expression
        if(r.equals("int")){
          str = str + ", i32 " + x;
        }else if(r.equals("int[]")){
          str = str + ", i32* " + x;
        }else if(r.equals("boolean")){
          str = str + ", i1 " + x;
        }else{
          str = str + ", i8* " + x;
        }
        i++;      
      } 
      emit("\t" + v6 + " = call "+ rt + " " + v5 + "(i8* " + s1 + str + ")\n");
      argu.strp.clear();                                                                    //katharismos tou pinaka gia epanaxrisimopoihsh
      return v6;
    }
  }
  /**
  * f0 -> Expression()
  * f1 -> ExpressionTail()
  */
  public String visit(ExpressionList n, SymbolInfo argu) throws Exception {
    String _ret=null;
    FormalInfo finfo = new FormalInfo();
    _ret=n.f0.accept(this, argu);
    finfo.name=_ret;
    n.f1.accept(this, argu);
    argu.strp.add(finfo);                                                                   //eisagwgi twn registers pou epistrefontai apo ta expressions sto arraylist gia tin swsti ektipwsi tous
    Collections.reverse(argu.strp);                                                         //reverese arraylist to remain in order
    return _ret;
  }
  /**
  * f0 -> ","
  * f1 -> Expression()
  */
  public String visit(ExpressionTerm n, SymbolInfo argu) throws Exception {
    String _ret=null;
    FormalInfo finfo = new FormalInfo();
    n.f0.accept(this, argu);
    _ret=n.f1.accept(this, argu);
    finfo.name=_ret;
    argu.strp.add(finfo);                                                                   //eisagwgi register tis expression
    Collections.reverse(argu.strp);                                                         //reverese arraylist to remain in order
    return _ret;
  }

  /**
  * f0 -> Identifier()
  * f1 -> "["
  * f2 -> Expression()
  * f3 -> "]"
  * f4 -> "="
  * f5 -> Expression()
  * f6 -> ";"
  */
  public String visit(ArrayAssignmentStatement n, SymbolInfo argu) throws Exception {
    String _ret=null,id,e1,e2,lb1,lb2,lb3,v1,v2,v3,v4,v5,v6,v7=null;
    lb1=nextLabel("oob");
    lb2=nextLabel("oob");
    lb3=nextLabel("oob");
    id=n.f0.accept(this, argu);
    if(id != null){                                                                         //periptwsi pou h metavliti einai pedio klasis ki xreiazomaste to offset tis
      String type=null;
      int off=0;
      if(argu._class==null) throw new Exception("Error3: There is no class for identifier");    
      ClassInfo cinfo = argu.table.get(argu._class);                                        //periptwsi pou psaxnoume se olo ton pinaka ana epipedo ton typo tis metavlitis ki ton epistrefoume
      if(argu._method==null) throw new Exception("Error3: There is no method for identifier");
      if(cinfo.methodtable.get(argu._method).vartable!=null){
        type = cinfo.methodtable.get(argu._method).vartable.get(id);
      }
      if((type==null)&&(cinfo.methodtable.get(argu._method).formaltable!=null)){
        type=cinfo.methodtable.get(argu._method).formaltable.get(id);
      }
      if((type==null)&&(cinfo.vartable!=null)){
        while((type==null)&&(cinfo.vartable!=null)){
          if(cinfo.vartable.get(id)!=null){
            type=cinfo.vartable.get(id).type;
            off=cinfo.vartable.get(id).offset + 8;                                          //offset of variable
          }
          if(argu.table.get(cinfo.extend_class_name)!=null){
            cinfo=argu.table.get(cinfo.extend_class_name);
          }else 
            break;
        }
        v6 = nextVar();
        v7 = nextVar();
        emit("\t" +  v6 + " = getelementptr i8, i8* %this, i32 " + off);
        if(type.equals("int[]")){
          emit("\t" + v7 + " = bitcast i8* " + v6 + " to i32**");
        }
        id=v7;                                                                              //periptwsi pou h metavliti proerxetai apo pedio ara oxi identifier alla register pou proerxetai apo to bitcast
      }
    }
    v4=nextVar();                                                                           //gnwrizoume oti einai pinakas ara theloume i32 h i32* analoga
    if(v7==null)
      emit("\t" + v4 + " = load i32*, i32** %" + id);
    else
      emit("\t" + v4 + " = load i32*, i32** " + id);
    v3=nextVar();
    emit("\t" + v3 + " = load i32, i32* " + v4);
    v2=nextVar();
    e1=n.f2.accept(this, argu);
    emit("\t" + v2 + " = icmp ult i32 " + e1 +", " + v3);
    emit("\tbr i1 " + v2 + " ,label %" + lb1 + ", label %" + lb2);
    emit(lb1+":");
    v5=nextVar();
    emit("\t" + v5 + " = add i32 "+ e1 + ", 1" );
    v1=nextVar();
    emit("\t" + v1 + " = getelementptr i32, i32* "+ v4 +", i32 " + v5);
    e2=n.f5.accept(this, argu);
    emit("\tstore i32 " + e2 + ", i32* " + v1);
    emit("\tbr label %" + lb3 + "\n");
    emit(lb2+":");
    emit("\tcall void @throw_oob()");                                                       //elexnos se periptwsi pou eimaste ektos oriwn tou pinaka
    emit("\tbr label %" + lb3 + "\n");
    emit(lb3+":");

    return _ret;
  }
  /**
  * f0 -> PrimaryExpression()
  * f1 -> "["
  * f2 -> PrimaryExpression()
  * f3 -> "]"
  */
  public String visit(ArrayLookup n, SymbolInfo argu) throws Exception {
    String _ret=null,pe1,pe2,l1,l2,l3,v1,v2,v3,v4,v5,v6;
    l1=nextLabel("oob");
    l2=nextLabel("oob");
    l3=nextLabel("oob");
    argu.want_identifier=false;
    pe1=n.f0.accept(this, argu);
    v3=nextVar();                                                                           //gnwrizoume oti einai typou int[] ara gia auto i32*
    emit("\t" + v3 + " = load i32, i32* " + pe1);
    pe2=n.f2.accept(this, argu);
    v2=nextVar();
    emit("\t" + v2 + " = icmp ult i32 " + pe2 +", " + v3);
    emit("\tbr i1 " + v2 + " ,label %" + l1 + ", label %" + l2);
    emit(l1+":");
    v5=nextVar();
    emit("\t" + v5 + " = add i32 " + pe2 + ", 1");
    v1=nextVar();
    emit("\t" + v1 + " = getelementptr i32, i32* "+ pe1 +", i32 " + v5 );
    v6=nextVar();
    emit("\t" + v6 + " = load i32 , i32* " + v1);
    emit("\tbr label %" + l3 + "\n");
    emit(l2+":");
    emit("\tcall void @throw_oob()");                                                       //elexnos se periptwsi pou eimaste ektos oriwn tou pinaka
    emit("\tbr label %" + l3 + "\n");
    emit(l3+":");
    return v6;
  }
  /**
  * f0 -> "new"
  * f1 -> "int"
  * f2 -> "["
  * f3 -> Expression()
  * f4 -> "]"
  */
  public String visit(ArrayAllocationExpression n, SymbolInfo argu) throws Exception {
    String str=null,v1,v2,v3,v4,label1,label2;
    str=n.f3.accept(this, argu);
    label1 = nextLabel("arr_alloc");
    label2 = nextLabel("arr_alloc");
    v4 = nextVar();
    emit("\t" + v4 + " = icmp slt i32 " + str + " , 0");                                    //elexnos an ginetai desmefsi pinaka me arnitiko arithmo
    emit("\tbr i1 " + v4 + ", label %" + label1 + ", label %" + label2);
    emit(label1 + ":");
    emit("\tcall void @throw_oob()");
    emit("\tbr label %" + label2);
    emit(label2 + ":");
    v1 = nextVar();
    emit("\t" + v1 + " = add i32 " + str + ", 1");
    v2= nextVar();
    emit("\t" + v2 + " = call i8* @calloc(i32 4, i32 "+ v1 +")");
    v3=nextVar();
    emit("\t" + v3 + " = bitcast i8* "+ v2 +" to i32*");
    emit("\tstore i32 " + str + ", i32* " + v3 + "\n");                                     //apothikeusi tou megethous tou pinaka sti prwti thesi(gia to array length)
    return v3;
  }
  /**
  * f0 -> PrimaryExpression()
  * f1 -> "."
  * f2 -> "length"
  */
  public String visit(ArrayLength n, SymbolInfo argu) throws Exception {
    String _ret=null,str,v1,v2,v3;
    v1=nextVar();
    v2=nextVar();
    v3=nextVar();
    str=n.f0.accept(this, argu);
    emit("\t" + v1 + " = getelementptr i32, i32* "+ str +", i32 0");
    emit("\t" + v2 + " = load i32 , i32* " + v1 + "\n");
    return v2;                                                                              //epistrofi timis tou megethous tou pinaka
  }
  /**
  * f0 -> "new"
  * f1 -> Identifier()
  * f2 -> "("
  * f3 -> ")"
  */
  public String visit(AllocationExpression n, SymbolInfo argu) throws Exception {
    String _ret=null,str,v1,v2,v3,s=null,c;
    int ofi,sizi;
    Boolean bool=false;
    v1=nextVar();
    v2=nextVar();
    v3=nextVar();
    str=n.f1.accept(this, argu);
    ofi=argu.table.get(str).offset_end_var;
    if(argu.table.get(str).methodtable.containsKey("main")){                               //periptwsi pou kanoume new ti klasi pou exei ti main kai den iparxei tipota na grapsoume ara epistrefei keno
      return "";
    }
    if(argu.want_identifier==false){                                                        //periptwsi pou theloume na epistrepsoume register(tou new)
      if(!argu.table.get(str).methodtable.containsKey("main")){
        for ( VarInfo v : argu.table.get(str).vartable.values()){
          s=v.type;                                                                         //last type of class gia ti dimiourgia tou katallilou offset
        }
      }
      if(s!=null){
        if(s.equals("int")){
          ofi = ofi + 4 + 8;                                                                //int + offset vtable
        }else if(s.equals("int[]")){
          ofi = ofi + 8 + 8;                                                                //int[] + offset vtable
        }else if(s.equals("boolean")){
          ofi = ofi + 1 + 8;                                                                //boolean + offset vtable
        }else{
          ofi = ofi + 8 + 8;                                                                //pointer + offset vtable
        }
      }else{
        ofi = ofi + 8;
      }
      if(argu.table.get(str).offset_end_meth == 0){                                         //elexnos an iparxei parapanw klasi pou exei mia sinartisi wste to megethos tou vtable na au3ithei kata ena h oxi 
        c=argu.table.get(str).extend_class_name;
        bool=true;
        if(c == null){
          if(argu.table.get(str).methodtable.size()!= 0)                                    //periptwsi pou h current klasi exei mia sinartisi
            bool=false;
        }
        while(c != null){
          if(argu.table.get(c).methodtable.containsKey("main") || argu.table.get(c).methodtable.size()!= 0){                //iparxei sinartisi pio prin ara den einai to plithos twn methodwn tis (ypo)klasis 0 
            bool=false;
            break;
          }
          c = argu.table.get(c).extend_class_name;
        }
      }
      if(!bool)
        sizi = (argu.table.get(str).offset_end_meth / 8) + 1;                               //plithos method tis klasis me vasi to offset_end_meth diairontas to me to 8
      else
        sizi=0;
      emit("\t" + v1 + " = call i8* @calloc(i32 1, i32 " + ofi + ")");                      //offset + 8 gia to vtable gia na desmefsw to swsto xwro
      emit("\t" + v2 + " = bitcast i8* " + v1 + " to i8***");
      emit("\t" + v3 + " = getelementptr ["+ sizi + " x i8*], [" + sizi + " x i8*]* @." + str + "_vtable, i32 0, i32 0");   //to plithos [yxi8*] to 3eroume apo to symbol table(mekatalliles metatropes)
      emit("\tstore i8** " + v3 + ", i8*** " + v2 + "\n");
      return v1;
    }else                                                                                   //periptwsi pou theloume na epistrepsoume to onoma tis klasis
      return str;
  }
  /**
  * f0 -> "System.out.println"
  * f1 -> "("
  * f2 -> Expression()
  * f3 -> ")"
  * f4 -> ";"
  */
  public String visit(PrintStatement n, SymbolInfo argu) throws Exception {
    String _ret=null,str;
    str=n.f2.accept(this,argu);
    emit("\tcall void (i32) @print_int(i32 " + str +")\n");                                 //ektipwsi register typou int
    return _ret;
  }
  /**
  * f0 -> PrimaryExpression()
  * f1 -> "+"
  * f2 -> PrimaryExpression()
  */
  public String visit(PlusExpression n, SymbolInfo argu) throws Exception {
    String var=nextVar();
    emit("\t" + var + " = " + "add i32 " + n.f0.accept(this,argu) + ", " + n.f2.accept(this,argu));
    return var;
  }
  /**
  * f0 -> PrimaryExpression()
  * f1 -> "-"
  * f2 -> PrimaryExpression()
  */
  public String visit(MinusExpression n, SymbolInfo argu) throws Exception {
    String var=nextVar();
    emit("\t" + var + " = " + "sub i32 " + n.f0.accept(this,argu) + ", " + n.f2.accept(this,argu));
    return var;
  }
  /**
  * f0 -> PrimaryExpression()
  * f1 -> "*"
  * f2 -> PrimaryExpression()
  */
  public String visit(TimesExpression n, SymbolInfo argu) throws Exception {
    String var=nextVar();
    emit("\t" + var + " = " + "mul i32 " + n.f0.accept(this,argu) + ", " + n.f2.accept(this,argu));
    return var;
  }
  /**
  * f0 -> PrimaryExpression()
  * f1 -> "<"
  * f2 -> PrimaryExpression()
  */
  public String visit(CompareExpression n, SymbolInfo argu) throws Exception {
    String var=nextVar();
    emit("\t" + var + " = " + "icmp slt i32 " + n.f0.accept(this,argu) + ", " + n.f2.accept(this,argu));
    return var;
  }
  /**
  * f0 -> IntegerLiteral()
  *       | TrueLiteral()
  *       | FalseLiteral()
  *       | Identifier()
  *       | ThisExpression()
  *       | ArrayAllocationExpression()
  *       | AllocationExpression()
  *       | BracketExpression()
  */
  public String visit(PrimaryExpression n, SymbolInfo argu) throws Exception {
    String _ret=n.f0.accept(this, argu);
    if(n.f0.which==3){                                                                      //periptwsi pou to primary expresion(diladi aristera mias anathesis h theloume timi tou expression) einai identifier
      String v1 = nextVar();
      String v2 = nextVar();
      String var=nextVar();
      String type=null;
      int off=0;
      if(_ret != null){                                                                     //periptwsi pou h metavliti pou epistrefete aniki se pedio klasis
        if(argu._class==null) throw new Exception("Error3: There is no class for identifier");    
        ClassInfo cinfo = argu.table.get(argu._class);                                      //periptwsi pou psaxnoume se olo ton pinaka ana epipedo ton typo tis metavlitis ki ton epistrefoume
        if(argu._method==null) throw new Exception("Error3: There is no method for identifier");
        if(cinfo.methodtable.get(argu._method).vartable!=null){
          type = cinfo.methodtable.get(argu._method).vartable.get(_ret);
        }
        if((type==null)&&(cinfo.methodtable.get(argu._method).formaltable!=null)){
          type=cinfo.methodtable.get(argu._method).formaltable.get(_ret);
        }
        if((type==null)&&(cinfo.vartable!=null)){
          while((type==null)&&(cinfo.vartable!=null)){
            if(cinfo.vartable.get(_ret)!=null){
              type=cinfo.vartable.get(_ret).type;
              off=cinfo.vartable.get(_ret).offset + 8;                                      //offset of variable
            }
            if(argu.table.get(cinfo.extend_class_name)!=null){
              cinfo=argu.table.get(cinfo.extend_class_name);
            }else 
              break;
          }
          if(argu.want_identifier==false){                                                  //periptwsi pou theloume na epistrafi h timis tis metavlitis pou vrikete se pedio klasis
            emit("\t" +  v1 + " = getelementptr i8, i8* %this, i32 " + off);
            if(type.equals("int")){
              emit("\t" + v2 + " = bitcast i8* " + v1 + " to i32*");
              emit("\t" + var + " = " + "load i32, i32* " + v2 + "\n");
            }else if(type.equals("int[]")){
              emit("\t" + v2 + " = bitcast i8* " + v1 + " to i32**");
              emit("\t" + var + " = " + "load i32*, i32** " + v2 + "\n");
            }else if(type.equals("boolean")){
              emit("\t" + v2 + " = bitcast i8* " + v1 + " to i1*");
              emit("\t" + var + " = " + "load i1, i1* " + v2 + "\n");
            }else{
              emit("\t" + v2 + " = bitcast i8* " + v1 + " to i8**");
              emit("\t" + var + " = " + "load i8*, i8** " + v2 + "\n");
            }
            return var;
          }else{                                                                            //periptwsi tou tipou epistrofis
            return type;
          }
        }
      }
      if(argu.want_identifier==false){                                                      //periptwsi pou h metavliti den einai pedio klasis kai theloume na epistrafi h timi tis
        if(type.equals("int")){
          emit("\t" + var + " = " + "load i32, i32* %" + _ret + "\n");
        }else if(type.equals("int[]")){
          emit("\t" + var + " = " + "load i32*, i32** %" + _ret + "\n");
        }else if(type.equals("boolean")){
          emit("\t" + var + " = " + "load i1, i1* %" + _ret + "\n");
        }else{
          emit("\t" + var + " = " + "load i8*, i8** %" + _ret + "\n");
        }
        return var;
      }else{                                                                                //periptwsi pou theloume ton tipo epistrofis tis metavlitis gia na elenxoun oi pio panw visit
        return type;
      }
    }else if(n.f0.which==4){                                                                //periptwsi pou to primari expression einai to this
      if(argu.want_identifier==false){                                                      //periptwsi pou theloume tin timi tou this ara ton register %this
        return "%this";
      }else                                                                                 //alliws epistrefi twn tipo tis klasis pou vriskete to this
        return _ret;
    }else                                                                                   //se opoiadipote alli periptwsi epestrepse kanonika
      return _ret;
  }
  /**
  * f0 -> Clause()
  * f1 -> "&&"
  * f2 -> Clause()
  */
  public String visit(AndExpression n, SymbolInfo argu) throws Exception {
    String var=nextVar(),lb1,lb2,lb3,lb4,v,b;
    lb1 = nextLabel("andclause");
    lb2 = nextLabel("andclause");
    lb3 = nextLabel("andclause");
    lb4 = nextLabel("andclause");
    b=n.f0.accept(this,argu);                                                               //to clause sort circuit me tin entoli phi opws tin ekfwnisi
    emit("\tbr label %" + lb1);
    emit(lb1+":");
    emit("\tbr i1 " + b + ", label %" + lb2 + ", label %" + lb4);
    emit(lb2+":");
    v=n.f2.accept(this,argu);
    emit("\tbr label %" + lb3);
    emit(lb3+":");
    emit("\tbr label %" + lb4);
    emit(lb4+":");
    emit("\t" + var + " = phi i1 [ 0, %" + lb1 + " ], [ " + v + ", %" + lb3 + " ]");
    return var;
  }
  /**
  * f0 -> "!"
  * f1 -> Clause()
  */
  public String visit(NotExpression n, SymbolInfo argu) throws Exception {
    String var=nextVar();
    emit("\t" + var + " = " + "xor i1 1, " + n.f1.accept(this,argu));
    return var;
  }
  /**
  * f0 -> "if"
  * f1 -> "("
  * f2 -> Expression()
  * f3 -> ")"
  * f4 -> Statement()
  * f5 -> "else"
  * f6 -> Statement()
  */
  public String visit(IfStatement n, SymbolInfo argu) throws Exception {
    String _ret=null;
    n.f0.accept(this, argu);
    String lb,lb1,lb2;
    lb1=nextLabel("if");                                                                    //kalw tin nextLAbel me to katallilo tag string se auti tin periptwsi
    lb2=nextLabel("if");
    emit("\n");
    emit("\t" + "br i1 " + n.f2.accept(this,argu) + ", label %" + lb1 + ", label %" + lb2);
    n.f1.accept(this, argu);
    n.f3.accept(this, argu);
    emit(lb1+":");
    n.f4.accept(this, argu);
    lb=nextLabel("exit_if");
    emit("\tbr label %" + lb);
    n.f5.accept(this, argu);
    emit(lb2+":");
    n.f6.accept(this, argu);
    emit("\tbr label %" + lb);
    emit(lb+":");
    return _ret;
  }
  /**
  * f0 -> "while"
  * f1 -> "("
  * f2 -> Expression()
  * f3 -> ")"
  * f4 -> Statement()
  */
  public String visit(WhileStatement n, SymbolInfo argu) throws Exception {
    String _ret=null,lb1,lb2,lb3;
    lb1=nextLabel("loop");                                                                  //kalw tin nextLAbel me to katallilo tag string se auti tin periptwsi
    lb2=nextLabel("loop");
    lb3=nextLabel("loop");
    emit("\n");
    emit("\tbr label %" + lb1);
    n.f0.accept(this, argu);
    n.f1.accept(this, argu);
    emit(lb1+":");
    emit("\tbr i1 " + n.f2.accept(this,argu) + ", label %"+lb2+", label %"+lb3);
    emit(lb2+":");
    n.f3.accept(this, argu);
    n.f4.accept(this, argu);
    emit("\tbr label %" +lb1);
    emit(lb3+":");
    return _ret;
  }
  /**
  * f0 -> "int"
  * f1 -> "["
  * f2 -> "]"
  */
  public String visit(ArrayType n, SymbolInfo argu) throws Exception {
    return "i32*";
  }
  /**
  * f0 -> "boolean"
  */
  public String visit(BooleanType n, SymbolInfo argu) throws Exception {
    return "i1";
  }
  /**
  * f0 -> "int"
  */
  public String visit(IntegerType n, SymbolInfo argu) throws Exception {
    return "i32";
  }
  /**
  * f0 -> <IDENTIFIER>
  */
  public String visit(Identifier n, SymbolInfo argu) throws Exception {
    return n.f0.toString();
  }
  /**
  * f0 -> "this"
  */
  public String visit(ThisExpression n, SymbolInfo argu) throws Exception {
    return argu._class;                                                                     //epistrefei ti klasi pou vriskete
  }
  /**
  * f0 -> <INTEGER_LITERAL>
  */
  public String visit(IntegerLiteral n, SymbolInfo argu) throws Exception {
    String var = nextVar();
    n.f0.accept(this,argu);
    emit("\t" + var + " = " + "add i32 0, " + n.f0.toString());                             //epistrefei ton register pou apothikeutike to integer literal
    return var;
  }
  /**
  * f0 -> "true"
  */
  public String visit(TrueLiteral n, SymbolInfo argu) throws Exception {
    return "1";
  }
  /**
  * f0 -> "false"
  */
  public String visit(FalseLiteral n, SymbolInfo argu) throws Exception {
    return "0";
  }
  /**
  * f0 -> "("
  * f1 -> Expression()
  * f2 -> ")"
  */
  public String visit(BracketExpression n, SymbolInfo argu) throws Exception {
    String _ret=null;
    n.f0.accept(this, argu);
    _ret=n.f1.accept(this, argu);
    n.f2.accept(this, argu);
    return _ret;
  }
}