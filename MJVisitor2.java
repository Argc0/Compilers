import syntaxtree.*;
import visitor.GJDepthFirst;
import java.util.*;

public class MJVisitor2 extends GJDepthFirst<String, SymbolInfo> {
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
  public String visit(MainClass n, SymbolInfo argu) throws Exception {
    String _ret=null;
    n.f0.accept(this, argu);
    argu.want_identifier=true;
    String str = n.f1.accept(this, argu);
    if(str==null) throw new Exception("Error2: Something wrong with MainClass Declaration");
    argu._class=str;                                                                          //enimerwsi gia to pia einai h clasi gia ta var declaration pou tha iparxoun mesa sti main
    argu._method = n.f6.toString();                                                           //enimerwsi tis idi iparxous ki ametavlitis methodou main gia tous epomenous
    n.f2.accept(this, argu);
    n.f3.accept(this, argu);
    n.f4.accept(this, argu);
    n.f5.accept(this, argu);
    n.f6.accept(this, argu);
    n.f7.accept(this, argu);
    n.f8.accept(this, argu);
    n.f9.accept(this, argu);
    n.f10.accept(this, argu);
    n.f11.accept(this, argu);
    n.f12.accept(this, argu);
    n.f13.accept(this, argu);
    n.f14.accept(this, argu);
    n.f15.accept(this, argu);
    n.f16.accept(this, argu);
    n.f17.accept(this, argu);
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
    String _ret=null;
    n.f0.accept(this, argu);
    argu.want_identifier=true;
    String str = n.f1.accept(this, argu);
    if(str==null) throw new Exception("Error2: Something wrong with Class Declaration");
    argu._class=str;                                                                        //enimerwsi twn epomenwn se pia klasi anikoun
    argu._method=null;
    argu.table.get(str).offset_end_var=0;                                                   //arxikopoihsh twn offset ena gia tis metavlites ki ena gia tis methodous
    argu.table.get(str).offset_end_meth=0;
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
    String _ret=null;
    n.f0.accept(this, argu);
    argu.want_identifier=true;
    String str = n.f1.accept(this, argu);
    if(str==null) throw new Exception("Error2: Something wrong with Class Extends Declaration");
    argu._class=str;                                                                        //enimerwsi twn epomenwn gia to se pia klasi anikoun
    argu._method=null;
    n.f2.accept(this, argu);
    argu.want_identifier=true;
    String str1 = n.f3.accept(this, argu);
    if(str1==null) throw new Exception("Error2: Something wrong with Class Extends Declaration");
    argu.table.get(str).offset_end_var=argu.table.get(str1).offset_end_var;                 //arkikopoihsh  twn offset
    argu.table.get(str).offset_end_meth=argu.table.get(str1).offset_end_meth;
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
    String _ret=null;
    if(argu._class == null) throw new Exception("Error2: There is no class for the Method Declaration");
    n.f0.accept(this, argu);
    argu.want_identifier=true;
    String t = n.f1.accept(this, argu);
    if(t==null) throw new Exception("The return type does not exist");
    argu.want_identifier=true;
    String str = n.f2.accept(this, argu);
    if(str==null) throw new Exception("The identifier does not exist");
    if(!(argu.table.containsKey(t) || t.equals("int") || t.equals("boolean") || t.equals("int[]"))){
      throw new Exception("The return type of the method is not correct");
    }
    argu._method=str;                                                                       //enimerwsi gia tous epomenous to onoma tis methodou pou anikoun
    n.f3.accept(this, argu);
    n.f4.accept(this, argu);
    n.f5.accept(this, argu);
    n.f6.accept(this, argu);
    n.f7.accept(this, argu);
    n.f8.accept(this, argu);
    n.f9.accept(this, argu);
    argu.want_identifier=false;
    String str2 = n.f10.accept(this, argu);
    if(argu.table.get(argu._class).methodtable==null) throw new Exception("Error2: The method table of class " + argu._class + " does not exist");
    String str1 = argu.table.get(argu._class).methodtable.get(str).return_type;
    Boolean bool;
    if(str1!=null&&str2!=null){
      if(!(str1.equals("int") || str1.equals("int[]") || str1.equals("boolean"))){
        bool=false;
        while(str2!=null){                                                                  //elenxos gia ypotipous tou typou epistrofis tis methodou
          if(argu.table.get(str2)!=null){
            if(str1.equals(str2)){
              bool=true;
              break;
            }
            str2=argu.table.get(str2).extend_class_name;
          }else
            throw new Exception("There is no type " + str1 + " to be returned");
        }
        if(!bool){
          throw new Exception("The return type of the method " +  str + " is not correct");
        }
      }else{
        if(!(str1.equals(str2))){
          throw new Exception("The return type of the method " +  str + " is not correct");
        }
      }
    }else{
      throw new Exception("Error2: Something wrong with the comparison of the return type of method " + str);
    }
    n.f11.accept(this, argu);
    n.f12.accept(this, argu);
    Boolean s = false;
    int num;
    MethodInfo minfo = argu.table.get(argu._class).methodtable.get(str);
    if(argu.table.get(argu._class).methodtable!=null){
      for(String s1: argu.table.get(argu._class).methodtable.keySet()){                     //elexei an einai h prwti methodos pouexei dilwthei
        if(!(s1.equals(str)))
          s=true;
        else
          break;
      }
    }else{
      throw new Exception("Error2: There is no method table of class " + argu._class);
    }
    if(s==false){
      String s1 = argu.table.get(argu._class).extend_class_name;
      if(s1 != null){
        num=8;
        while(s1!=null){                                                                    //! psaxnw an oles oi parapanw sinartiseis einai oles adeies gia na min prostethei offset epipleon
          if(argu.table.get(s1).methodtable.containsKey("main") || argu.table.get(s1).methodtable.size()==0)                              //!periptwsi pou mia klasi kanei extends tin klasi pou xei tin main ki na min metrisei ti main ws arxiki methodo sto offset table h kanei extend mia adeia klasi
            num=0;
          else{
            num=8;                                                                            //gnwrizoume idi oti einai sinartsi prin ki oxi allos typos
            break;
          }
          s1=argu.table.get(s1).extend_class_name;
        }
      }else{
        num=0;
      }
    }else{
      num=8;
    }                                                                                       //periptwsi pou den kanei overwrite
    if(minfo.overwrite==null || minfo.overwrite==false){
      minfo.offset = argu.table.get(argu._class).offset_end_meth + num;
      argu.table.get(argu._class).offset_end_meth = minfo.offset;                           //enimerwsi tis metavlitis offset methodou gia to telefteo offset
    }else{                                                                                  //perptwsi pou kanei overwrite anathetotantas tin idia timi me tin arxiki methodo xwris enimerwsi tis metavlitis offset methodou tis klasis
      String ex_name = argu.table.get(argu._class).extend_class_name;
      while(ex_name != null){                                                //!na vroume tin proigoumeni metavliti analoga se poio vathos vriskete gia na ipologisoume to offset
        if(argu.table.get(ex_name).methodtable.containsKey(str)){
          minfo.offset = argu.table.get(ex_name).methodtable.get(str).offset;
          break;
        }
        ex_name = argu.table.get(ex_name).extend_class_name;
      }
    }
    return _ret;
  }
  /**
  * f0 -> Type()
  * f1 -> Identifier()
  */
  public String visit(FormalParameter n, SymbolInfo argu) throws Exception {
    String _ret=null;
    if(argu._class == null || argu._method == null) throw new Exception("Error2: Something wrong with class or method of the Formal Parameter");
    String str=n.f0.accept(this, argu);
    if(str==null) throw new Exception("Error2: Something wrong with type of Formal Parameter");
    if(!(str.equals("int") || str.equals("int[]") || str.equals("boolean") || argu.table.containsKey(str))){
      throw new Exception("Type " + str + " does not exist");
    }
    n.f1.accept(this, argu);
    return _ret;
  }
  /**
  * f0 -> Type()
  * f1 -> Identifier()
  * f2 -> ";"
  */
  public String visit(VarDeclaration n, SymbolInfo argu) throws Exception {
    String _ret=null;
    if(argu._class == null) throw new Exception("Error2: There is no class for the Var Declaration");
    String str = n.f0.accept(this, argu);
    if(str==null) throw new Exception("Error2: Something wrong with type of Var Declaration");
    if(!(str.equals("int") || str.equals("int[]") || str.equals("boolean") || argu.table.containsKey(str))){
      throw new Exception("Type " + str + " is not exist");
    }
    argu.want_identifier=true;
    String str1 = n.f1.accept(this, argu);
    if(str==null) throw new Exception("Error2: Something wrong with name of Var Declaration");
    int num;
    String s=null;
    if(argu._method==null){                                                                 //periptwsi pou dilonoume pedia tis klasis
      VarInfo vinfo = argu.table.get(argu._class).vartable.get(str1);
      if(argu.table.get(argu._class).vartable!=null){
        for(String s1: argu.table.get(argu._class).vartable.keySet()){
          if(!(s1.equals(str1)))
            s=argu.table.get(argu._class).vartable.get(s1).type;                            //krataei tin proigoumeni timi tipou metavlitis tis trexousas apo ton pinaka metavlitwn tis klasis
          else
            break;
        }
      }
      VarInfo var=null;
      if(s==null){                                                                          //periptwsi pou einai h prwti dilwsi metavlitis mesa sti klasi
        String s1 = argu.table.get(argu._class).extend_class_name;
        String s3=null;
        if(s1!=null){
          if((argu.table.get(s1).vartable == null) && !argu.table.get(s1).methodtable.containsKey("main"))    //h peripwsi pou einai ki h klasi pou exei th main xwris vartable na min piastei ws error 
            throw new Exception("Error2: Var table of class  " + s1 + " does not exist");
          while( s1 != null){
            if(argu.table.get(s1).methodtable.containsKey("main")) break;                       //!periptwsi pou einai h main ara den exei vartable
            for(String s2 : argu.table.get(s1).vartable.keySet()) 
              s3=s2;                                                                          //krataei tin teleftea timi typou metavlitis tis yperklasis
            if(s3!=null){
              var = argu.table.get(s1).vartable.get(s3);
              break;
            }
            s1=argu.table.get(s1).extend_class_name;
          }
          if(s3==null){                                                                     //elenxos gia tin teleftea timi tis iperklasis ti typou einai
            num=0;
          }else if((var.type).equals("boolean")){
            num=1;
          }else if ((var.type).equals("int")){
            num=4;
          }else{
            if((var.type).equals("int[]") || argu.table.containsKey(var.type))
              num=8;
            else
              throw new Exception("Error2: The type" + var.type + " does not exist");
          }
        }else{
          num=0;
        }
      }else if(s.equals("boolean")){                                                        //periptwseis pou elenxou tous tipous twn metavlitwn gia ta offset
        num=1;
      }else if (s.equals("int")){
        num=4;
      }else{
        if(s.equals("int[]") || argu.table.containsKey(s))
          num=8;
        else
          throw new Exception("Error2: The type" + s + " does not exist");
      }
      vinfo.offset = argu.table.get(argu._class).offset_end_var + num;
      argu.table.get(argu._class).offset_end_var = vinfo.offset;                            //enimerwsi tis metavlitis tis klasis pou krataei to telefteo offset
    }
    n.f1.accept(this, argu);
    n.f2.accept(this, argu);
    return _ret;
  }
  /**
  * f0 -> "int"
  * f1 -> "["
  * f2 -> "]"
  */
  public String visit(ArrayType n, SymbolInfo argu) throws Exception{
    String _ret=null;
    n.f0.accept(this, argu);
    n.f1.accept(this, argu);
    n.f2.accept(this, argu);
    return (n.f0.toString() + n.f1.toString() + n.f2.toString() );
  }
  /**
  * f0 -> "boolean"
  */
  public String visit(BooleanType n, SymbolInfo argu) throws Exception{
    n.f0.accept(this, argu);
    return n.f0.toString();
  }
  /**
  * f0 -> "int"
  */
  public String visit(IntegerType n, SymbolInfo argu) throws Exception{
    n.f0.accept(this, argu);
    return n.f0.toString();
  }
  /**
  * f0 -> <IDENTIFIER>
  */
  public String visit(Identifier n, SymbolInfo argu) throws Exception{
    n.f0.accept(this,argu);
    String ident=n.f0.toString();
    if(argu.want_identifier){                                                               //periptwsi pou theloume na epistrepsei to onoma tis metavlitis
      return ident;
    }else{
      String type=null;
      if(ident != null){
        if(argu._class==null) throw new Exception("Error2: There is no class for identifier");    
        ClassInfo cinfo = argu.table.get(argu._class);                                      //periptwsi pou psaxnoume se olo ton pinaka ana epipedo ton typo tis metavlitis ki ton epistrefoume
        if(cinfo.methodtable.get(argu._method).vartable!=null){
          type = cinfo.methodtable.get(argu._method).vartable.get(ident);
        }
        if(argu._method==null) throw new Exception("Error2: There is no method for identifier");
        if((type==null)&&(cinfo.methodtable.get(argu._method).formaltable!=null)){
          type=cinfo.methodtable.get(argu._method).formaltable.get(ident);
        }
        while((type==null)&&(cinfo.vartable!=null)){
          if(cinfo.vartable.get(ident)!=null)
            type=cinfo.vartable.get(ident).type;
          if(argu.table.get(cinfo.extend_class_name)!=null){
            cinfo=argu.table.get(cinfo.extend_class_name);
          }else break;
        }
      }
      return type;
    }
  }
  /**
  * f0 -> Identifier()
  * f1 -> "="
  * f2 -> Expression()
  * f3 -> ";"
  */
  public String visit(AssignmentStatement n, SymbolInfo argu) throws Exception {
    String _ret=null,t1,t2,t3,tname;
    boolean bool;
    if(argu._class == null || argu._method == null) throw new Exception("Error2: Something wrong with class or method of the Assignment Statement");
    argu.want_identifier=false;
    t1=n.f0.accept(this, argu);
    if(t1==null){
      throw new Exception("The Identifier of Assignment Statement does not exists");
    }
    if(t1.equals("int[]")){
      argu.want_identifier=true;
      tname=n.f0.accept(this,argu);
      argu.name_table=tname;
    }
    n.f1.accept(this, argu);
    argu.want_identifier=false;
    t3=t2=n.f2.accept(this, argu);
    n.f3.accept(this, argu);
    if(t1!=null&&t2!=null){
      if(!(t1.equals("int") || t1.equals("int[]") || t1.equals("boolean"))){
        bool=false;
        while(t2!=null){                                                                    //elenxos an o typos einai ypotipos mia yperklasis gia na isxyei h anathesi
          if(argu.table.get(t2)!=null){
            if(t1.equals(t2)){
              bool=true;
              break;
            }
            t2=argu.table.get(t2).extend_class_name;
          }else
            throw new Exception("There is no type " + t1 + " to be assigned");
        }
        if(!bool){
          throw new Exception("Type " + t1 + " cannot be converted to type " + t3);
        }
      }else{
        if(!t1.equals(t2)){
          throw new Exception("The parts of the assigment are not the same " + t1 + " " +t2);
        }
      }
    }else{
      throw new Exception("One of the parts in Assignment Statement is wrong");
    }
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
    String _ret=null,t1,t2,t3;
    argu.want_identifier=false;
    if(argu._class == null || argu._method == null) throw new Exception("Error2: Something wrong with class or method of the Array Assignment Statement");
    t1=n.f0.accept(this, argu);
    if(t1!=null){
      if(!(t1.equals("int[]"))){
        throw new Exception("Variable " + t1 + " is not an array");
      }
    }else{
      throw new Exception("An error occured in Array Assignment Statement Identifier does not exist");
    }
    n.f1.accept(this, argu);
    argu.want_identifier=false;
    t2=n.f2.accept(this, argu);
    if(t2!=null){
      if(!(t2.equals("int"))){
        throw new Exception("Expression is not int in Array Assignment");
      }
    }else{
      throw new Exception("Error2: An error occured in expression inside [] of the Array Assignment Statement");
    }
    n.f3.accept(this, argu);
    n.f4.accept(this, argu);
    argu.want_identifier=false;
    t3=n.f5.accept(this, argu);
    if(t3!=null){
      if(!(t3.equals("int"))){
        throw new Exception("The expression to be assigned in the Array Assignment Statement is not int");
      }
    }else{
      throw new Exception("Error2: An error occured in Array Assignment Statement");
    }
    n.f6.accept(this, argu);
    return _ret;
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
    String _ret=null,t1;
    if(argu._class == null || argu._method == null) throw new Exception("Error2: Something wrong with class or method of the If Statement");
    n.f0.accept(this, argu);
    n.f1.accept(this, argu);
    argu.want_identifier=false;
    t1 = n.f2.accept(this, argu);
    if(t1!=null){
      if(!t1.equals("boolean")){
        throw new Exception("If expression is not boolean");
      }
    }else{
      throw new Exception("There is a wrong in If Expression");
    }
    n.f3.accept(this, argu);
    n.f4.accept(this, argu);
    n.f5.accept(this, argu);
    n.f6.accept(this, argu);
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
    String _ret=null,t1;
    if(argu._class == null || argu._method == null) throw new Exception("Error2: Something wrong with class or method of the While Statement");
    n.f0.accept(this, argu);
    n.f1.accept(this, argu);
    argu.want_identifier=false;
    t1=n.f2.accept(this, argu);
    if(t1!=null){
      if(!(t1.equals("boolean"))){
        throw new Exception("While expression is not boolean");
      }
    }else{
      throw new Exception("Something wrong with While Expression");
    }
    n.f3.accept(this, argu);
    n.f4.accept(this, argu);
    return _ret;
  }
  /**
  * f0 -> "System.out.println"
  * f1 -> "("
  * f2 -> Expression()
  * f3 -> ")"
  * f4 -> ";"
  */
  public String visit(PrintStatement n, SymbolInfo argu) throws Exception {
    String _ret=null,t1;
    if(argu._class == null || argu._method == null) throw new Exception("Error2: Something wrong with class or method of the Print Statement");
    n.f0.accept(this, argu);
    n.f1.accept(this, argu);
    argu.want_identifier=false;
    t1=n.f2.accept(this, argu);
    n.f3.accept(this, argu);
    n.f4.accept(this, argu);
    if(t1!=null){
      if(!(t1.equals("int"))){
        throw new Exception("The Print Statement is not int");
      }
    }else{
      throw new Exception("Something wrong with Print Statement");
    }
    return _ret;
  }
  /**
  * f0 -> Clause()
  * f1 -> "&&"
  * f2 -> Clause()
  */
  public String visit(AndExpression n, SymbolInfo argu) throws Exception {
    String _ret="boolean";
    String t1,t2;
    if(argu._class == null || argu._method == null) throw new Exception("Error2: Something wrong with class or method of the And Expression");
    argu.want_identifier=false;
    t1=n.f0.accept(this, argu);
    n.f1.accept(this, argu);
    argu.want_identifier=false;
    t2=n.f2.accept(this, argu);
    if(t1!=null && t2!=null){
      if(!(t1.equals("boolean") && t2.equals("boolean"))){
        throw new Exception("One of the parts of And Expression are not boolean " + t1 + " " + t2);
      }
    }else{
      throw new Exception("Something wrong with the And Expression");
    }
    return _ret;
  }
  /**
  * f0 -> PrimaryExpression()
  * f1 -> "<"
  * f2 -> PrimaryExpression()
  */
  public String visit(CompareExpression n, SymbolInfo argu) throws Exception {
    String _ret="boolean";
    String t1,t2;
    if(argu._class == null || argu._method == null) throw new Exception("Error2: Something wrong with class or method of the Compare Expression");
    argu.want_identifier=false;
    t1=n.f0.accept(this, argu);
    if(t1==null){
      throw new Exception("Left comparator of Compare Expression does not exist");
    }
    n.f1.accept(this, argu);
    argu.want_identifier=false;
    t2=n.f2.accept(this, argu);
    if(t2==null){
      throw new Exception("Right comparator of Compare Expression does not exist");
    }
    if(!(t1.equals("int") && t2.equals("int"))){
      throw new Exception("One of the parts of Compare Expression are not int " + t1 + " " + t2);
    }
    return _ret;
  }
  /**
  * f0 -> PrimaryExpression()
  * f1 -> "+"
  * f2 -> PrimaryExpression()
  */
  public String visit(PlusExpression n, SymbolInfo argu) throws Exception {
    String _ret="int";
    String t1,t2;
    if(argu._class == null || argu._method == null) throw new Exception("Error2: Something wrong with class or method of the Plus Expression");
    argu.want_identifier=false; 
    t1 = n.f0.accept(this, argu);
    n.f1.accept(this, argu);
    argu.want_identifier=false;
    t2 = n.f2.accept(this, argu);
    if(t1!=null &&  t2!=null){
      if(!(t1.equals("int") && t2.equals("int"))){
        throw new Exception("One of the parts of  Plus Expression are not int " + t1 + " " + t2);
      }
    }else{
      throw new Exception("Identifiers of Plus Expression are not correct");
    }
    return _ret;
  }
  /**
  * f0 -> PrimaryExpression()
  * f1 -> "-"
  * f2 -> PrimaryExpression()
  */
  public String visit(MinusExpression n, SymbolInfo argu) throws Exception {
    String _ret="int";
    String t1,t2;
    if(argu._class == null || argu._method == null) throw new Exception("Error2: Something wrong with class or method of the Minus Expression");
    argu.want_identifier=false;
    t1=n.f0.accept(this, argu);
    n.f1.accept(this, argu);
    argu.want_identifier=false;
    t2=n.f2.accept(this, argu);
    if(t1!=null &&  t2!=null){
      if(!(t1.equals("int") && t2.equals("int"))){
        throw new Exception("One of the parts of Minus Expression are not int " + t1 + " " + t2);
      }
    }else{
      throw new Exception("Identifiers of Minus Expression are not correct");
    }
    return _ret;
  }
  /**
  * f0 -> PrimaryExpression()
  * f1 -> "*"
  * f2 -> PrimaryExpression()
  */
  public String visit(TimesExpression n, SymbolInfo argu) throws Exception {
    String _ret="int";
    String t1,t2;
    if(argu._class == null || argu._method == null) throw new Exception("Error2: Something wrong with class or method of the Times Expression");
    argu.want_identifier=false;
    t1=n.f0.accept(this, argu);
    n.f1.accept(this, argu);
    argu.want_identifier=false;
    t2=n.f2.accept(this, argu);
    if(t1!=null &&  t2!=null){
      if(!(t1.equals("int") && t2.equals("int"))){
        throw new Exception("One of the parts of Times Expression are not int " + t1 + " " + t2);
      }
    }else{
      throw new Exception("Identifiers of Times Expression are not correct");
    }
    return _ret;
  }
  /**
  * f0 -> PrimaryExpression()
  * f1 -> "["
  * f2 -> PrimaryExpression()
  * f3 -> "]"
  */
  public String visit(ArrayLookup n, SymbolInfo argu) throws Exception {
    String _ret="int";
    String t1,t2;
    if(argu._class == null || argu._method == null) throw new Exception("Error2: Something wrong with class or method of the Array Lookup");
    argu.want_identifier=false;
    t1=n.f0.accept(this, argu);
    if(t1==null){
      throw new Exception("Identifier for Array Lookup expression does not exist");
    }
    n.f1.accept(this, argu);
    argu.want_identifier=false;
    t2=n.f2.accept(this, argu);
    n.f3.accept(this, argu);
    if(t1!=null && t2!=null){
      if(!(t1.equals("int[]")&&(t2.equals("int")))){
        throw new Exception("The Array Lookup is not correct");
      }
    }else{
      throw new Exception("Array Lookup Expression has an error");
    }
    return _ret;
  }
  /**
  * f0 -> PrimaryExpression()
  * f1 -> "."
  * f2 -> "length"
  */
  public String visit(ArrayLength n, SymbolInfo argu) throws Exception {
    String _ret="int";
    String t1;
    if(argu._class == null || argu._method == null) throw new Exception("Error2: Something wrong with class or method of the Array Length");
    argu.want_identifier=false;
    t1=n.f0.accept(this, argu);
    n.f1.accept(this, argu);
    n.f2.accept(this, argu);
    if(t1!=null){
      if(!t1.equals("int[]")){
        throw new Exception("Length is not set in Array type");
      }
    }
    else
      throw new Exception("Array Length has an error");
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
    String _ret=null,t1,t2;
    String str2;
    Boolean bool;
    if(argu._class == null || argu._method == null) throw new Exception("Error2: Something wrong with class or method of the Message Send");
    argu.want_identifier=false;
    t1=n.f0.accept(this, argu);
    if(t1!=null){
      if(t1.equals("int") || t1.equals("int[]") || t1.equals("boolean")){
        throw new Exception("Type " + t1 + " has not Message Send");
      }
    }else{
      throw new Exception("Something wrong with the Primary Expression of Message Send");
    }
    n.f1.accept(this, argu);
    argu.want_identifier=true;
    str2=n.f2.accept(this, argu);
    if(str2 == null) throw new Exception("Error2: Something wrong with the Identifier of Message Send");
    MethodInfo minfo=null;
    if(argu.table.get(t1).methodtable==null) throw new Exception("Error2: The method table of class " + t1 + " does not exist");
    bool=false;
    while(t1!=null){
      if(argu.table.get(t1)!=null){                                                         //psaxnei ti methodo sto pinaka methodwn tis klasis kai se oles tis iperklaseis
        if(argu.table.get(t1).methodtable.containsKey(str2)){
          minfo=argu.table.get(t1).methodtable.get(str2);
          bool=true;
          break;
        }else{
          t1=argu.table.get(t1).extend_class_name;
        }
      }else{
        throw new Exception("Unknown Primary Expression of Message Send" + t1);
      }
    }
    if(!bool){
      throw new Exception("There is no method " + str2);
    }
    argu.strp = new ArrayList<FormalInfo>();                                                    //array for holding parameters of method for checking
    ArrayList<String> types = new ArrayList<String>();
    n.f3.accept(this, argu);
    n.f4.accept(this, argu);                                                                //check after expression list
    int nom;
    Collection<String> x =minfo.formaltable.values();              
    if(argu.strp==null){
      nom=0;
    }else{
      nom= argu.strp.size();
    }
    if(x.size()!= nom){
      throw new Exception("The Formal Parameters of Method " + str2 + " are not the same size");
    }else{
      String a[] = new String[x.size()];                                                    //pinakas twn tipwn twn formal parameters tis methodou tis iperklasis pou idi iparxei
      String b[] = new String[nom];                                                         //pinakas twn tipwn twn formal parameter tis trexousas methodou
      String h;
      a = x.toArray(a);                                                                     //metatropi twn map/list gia kaliteri sigrisi ki diaxirisi twn timwn
      if(argu.strp!=null){                                                                  //!prostethike ki allo ena string gia kathe stoixeio tou gia na mporei na xrisimopoihthei ki apo ton trito visitor
        for(FormalInfo f : argu.strp){
          types.add(f.type);
        }
        b = types.toArray(b);
      }
      for(int i=0; i < x.size(); i++){
        if((a[i].equals("int") || a[i].equals("int[]") || a[i].equals("boolean"))){
          if(!a[i].equals(b[i])){
            throw new Exception("The Formal Parameters of Method " + str2 + " are not the same " + a[i] + " " +b[i]);
          }
        }else{
          bool=false;
          h=b[i];
          while(h!=null){
            if(argu.table.get(h)!=null){                                                    //elexnos an einai idiou tipou h einai ypotipos mia yperklasis
              if(a[i].equals(h)){
                bool=true;
                break;
              }
              h=argu.table.get(h).extend_class_name;
            }else{
              throw new Exception("The Formal Parameters are not the same " + a[i] + " " + h);
            }
          }
          if(!bool){
            throw new Exception("The type " + a[i] + " cannot be converted to " + b[i] + " while calling the method " + str2);
          }
        }
      }
    }
    n.f5.accept(this, argu);
    argu.strp.clear();                                                                      //katharismos tou pinaka kathe fora pou teleiwnei h sigrisi gia epomeni xrisi
    _ret=argu.table.get(t1).methodtable.get(str2).return_type;                              //epistrofi tou typou epistrofis tis methodou
    return _ret;
  }
  /**
  * f0 -> Expression()
  * f1 -> ExpressionTail()
  */
  public String visit(ExpressionList n, SymbolInfo argu) throws Exception {
    String _ret=null,t1;
    FormalInfo finfo = new FormalInfo();
    if(argu._class == null || argu._method == null) throw new Exception("Error2: Something wrong with class or method of the Expression List");
    argu.want_identifier=false;
    t1=n.f0.accept(this, argu);
    if(t1==null){
      throw new Exception("Something wrong with the Expression of Expression List");
    }
    finfo.type=t1;
    n.f1.accept(this, argu);
    argu.strp.add(finfo);                                                                      //!add to to array the wright type for checkin later
    Collections.reverse(argu.strp);                                                         //reverse for the right order
    return _ret;
  }
  /**
  * f0 -> ","
  * f1 -> Expression()
  */
  public String visit(ExpressionTerm n, SymbolInfo argu) throws Exception {
    String _ret=null,t1;
    FormalInfo finfo = new FormalInfo();
    if(argu._class == null || argu._method == null) throw new Exception("Error2: Something wrong with class or method of the Expression Term");
    n.f0.accept(this, argu);
    argu.want_identifier=false;
    t1=n.f1.accept(this, argu);
    if(t1==null){
      throw new Exception("Something wrong with the Expression of Expression Term");
    }
    finfo.type=t1;
    argu.strp.add(finfo);                                                                      //!add to array for check later
    Collections.reverse(argu.strp);                                                         //revesre for the right order
    return t1;
  }
  /**
  * f0 -> <INTEGER_LITERAL>
  */
  public String visit(IntegerLiteral n, SymbolInfo argu) throws Exception {
    n.f0.accept(this, argu);
    return "int";
  }
  /**
  * f0 -> "true"
  */
  public String visit(TrueLiteral n, SymbolInfo argu) throws Exception {
    n.f0.accept(this, argu);
    return "boolean";
  }
  /**
  * f0 -> "false"
  */
  public String visit(FalseLiteral n, SymbolInfo argu) throws Exception {
    n.f0.accept(this, argu);
    return "boolean";
  }
  /**
  * f0 -> "this"
  */
  public String visit(ThisExpression n, SymbolInfo argu) throws Exception {
    String _ret=null;
    if(argu._class == null || argu._method == null) throw new Exception("Error2: Something wrong with class or method of the This Expression");
    n.f0.accept(this, argu);
    _ret=argu._class;                                                                       //epistrofi tis klasis pou anikei to this
    return _ret;
  }
  /**
  * f0 -> "new"
  * f1 -> "int"
  * f2 -> "["
  * f3 -> Expression()
  * f4 -> "]"
  */
  public String visit(ArrayAllocationExpression n, SymbolInfo argu) throws Exception {
    String _ret="int[]";
    String t1;
    if(argu._class == null || argu._method == null) throw new Exception("Error2: Something wrong with class or method of the Array Allocation Expression");
    n.f0.accept(this, argu);
    n.f1.accept(this, argu);
    n.f2.accept(this, argu);
    argu.want_identifier=false;
    t1=n.f3.accept(this, argu);
    if(t1!=null){
      if(!t1.equals("int")){
        throw new Exception("The Expression inside [ ] is not int");
      }
    }else{
      throw new Exception("Something wrong with Array Allocation Expression");
    }
    n.f4.accept(this, argu);
    return _ret;
  }
  /**
  * f0 -> "new"
  * f1 -> Identifier()
  * f2 -> "("
  * f3 -> ")"
  */
  public String visit(AllocationExpression n, SymbolInfo argu) throws Exception {
    String _ret=null;
    String str1;
    if(argu._class == null || argu._method == null) throw new Exception("Error2: Something wrong with class or method of the Allocation Expression");
    n.f0.accept(this, argu);
    argu.want_identifier=true;
    str1=n.f1.accept(this, argu);
    if(str1 !=null ){
      if(!argu.table.containsKey(str1)){
        throw new Exception(str1 + " is not a class");
      }
    }else
      throw new Exception("The Identifier of Allocation Expression is not correct");
    n.f2.accept(this, argu);
    n.f3.accept(this, argu);
    return str1;
  }
  /**
  * f0 -> "!"
  * f1 -> Clause()
  */
  public String visit(NotExpression n, SymbolInfo argu) throws Exception {
    String _ret="boolean";
    String t1;
    if(argu._class == null || argu._method == null) throw new Exception("Error2: Something wrong with class or method of the Not Expression");
    n.f0.accept(this, argu);
    argu.want_identifier=false;
    t1=n.f1.accept(this, argu);
    if(t1!=null){
      if(!(t1.equals("boolean"))){
        throw new Exception("The part of Clause ! is not boolean");
      }
    }else{
      throw new Exception("Something wrong with Not Expression");
    }
    return _ret;
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
    if(_ret==null){
      throw new Exception("Something went wrong with the Bracket Expression");
    }
    n.f2.accept(this, argu);
    return _ret;
  }
}