import syntaxtree.*;
import visitor.GJDepthFirst;
import java.util.*;

public class MJVisitor1 extends GJDepthFirst<String, SymbolInfo> {
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
    String _ret=null;
    ClassInfo cinfo = new ClassInfo();                                                            //arxikopoihsh twn metavlitwn tis argu gia tous epomenous
    cinfo.methodtable = new LinkedHashMap<String,MethodInfo>();
    MethodInfo minfo = new MethodInfo();
    minfo.return_type = n.f5.toString();
    minfo.vartable = new LinkedHashMap<String,String>();
    minfo.formaltable = new LinkedHashMap<String,String>();
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
    if(str==null||str1==null) throw new Exception("Error1: Something wrong with MainClass Declaration");
    minfo.formaltable.put(str, (n.f8.toString() + n.f9.toString() + n.f10.toString() ) );         //eisagwgi tis parametrou pou iparxei sti main
    argu._class=str1;
    argu._method=n.f6.toString();
    cinfo.methodtable.put(n.f6.toString(),minfo);                                                 //eisagwgi tis methodou main ston pinaka tis prwtis kalsis
    argu.table.put(str1,cinfo);                                                                   //eisagwgi tis klasis pou exei th main sto pinaka
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
  public String visit(ClassDeclaration n, SymbolInfo argu) throws Exception{
    String _ret=null;
    n.f0.accept(this, argu);
    String str = n.f1.accept(this, argu);
    if(str==null) throw new Exception("Error1: Something wrong with Class Declaration");
    argu._class=str;                                                                            //enimerwsi gia tous epomenous apo poia klasi einai
    argu._method=null;
    if(argu.table.containsKey(str)){
      throw new Exception("Class " + str + " already exists");
    }
    ClassInfo cinfo = new ClassInfo();
    cinfo.vartable = new LinkedHashMap<String,VarInfo>();
    cinfo.methodtable = new LinkedHashMap<String,MethodInfo>();
    argu.table.put(str,cinfo);                                                                  //eisagwgi klasis sto pinaka
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
  public String visit(ClassExtendsDeclaration n, SymbolInfo argu) throws Exception{
    String _ret=null;
    n.f0.accept(this, argu);
    String str = n.f1.accept(this, argu);
    n.f2.accept(this, argu);
    String str1 = n.f3.accept(this, argu);
    if(str==null||str1==null) throw new Exception("Error1: Something wrong with Class Extends Declaration");
    argu._class=str;
    argu._method=null;
    if(argu.table.containsKey(str)){
      throw new Exception("Class " + str + " already exists");
    }
    ClassInfo cinfo = new ClassInfo();
    cinfo.vartable = new LinkedHashMap<String,VarInfo>();
    cinfo.methodtable = new LinkedHashMap<String,MethodInfo>();
    if(!argu.table.containsKey(str1)){
      throw new Exception("There is no " + str1 + " class above to be extended");
    }
    cinfo.extend_class_name = str1;                                                             //eisagwgi tou onomas ths klasis apo tin opoia kanei extend
    argu.table.put(str,cinfo);                                                                  //eisagwgi tis klasis sto pinaka
    n.f4.accept(this, argu);
    n.f5.accept(this, argu);
    n.f6.accept(this, argu);
    n.f7.accept(this, argu);
    return _ret;
  }
  /**
  * f0 -> Type()
  * f1 -> Identifier()
  * f2 -> ";"
  */
  public String visit(VarDeclaration n, SymbolInfo argu) throws Exception{
    String _ret=null;
    if(argu._class == null) throw new Exception("Error1: There is no class for the Variable Declaration");
    String str = n.f0.accept(this,null);
    String str1 = n.f1.accept(this,null);
    if(str==null||str1==null) throw new Exception("Error1: Something wrong with Variable Declaration");
    if(argu!=null){
      if(argu._method != null){
        MethodInfo minfo = argu.table.get(argu._class).methodtable.get(argu._method);           //elexnos gia to an iparxei idi metavliti me to idio onoma pou exei dilwthei mesa se mia methodo
        if(!(minfo.vartable.containsKey(str1) || minfo.formaltable.containsKey(str1))){
          minfo.vartable.put(str1,str);                                                         //eisagwgi metavlitis sto table ths methodou
        }else
          throw new Exception("Variable " + str1 + " is already exists");
      }else{
        ClassInfo cinfo = argu.table.get(argu._class);                                          //elengxos gia to an iparxei idio onoma metavlitis me pedio tis klasis pou exeihdh dilwthei
        if(!cinfo.vartable.containsKey(str1)){                                        
          VarInfo varstr = new VarInfo();
          varstr.type=str;
          cinfo.vartable.put(str1,varstr);                                                      //eisagwgi metavlitis sto table tis klasis
        }else
          throw new Exception("Variable " + str1 + " is already exists");
      }
    }else{
      throw new Exception("Error1: There is no method to have Var Declaration");
    }
    n.f2.accept(this, null);
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
  public String visit(MethodDeclaration n, SymbolInfo argu) throws Exception{
    String _ret=null;
    if(argu._class == null) throw new Exception("Error1: There is no class for the Method Declaration");
    n.f0.accept(this, null);
    String str = n.f1.accept(this, null);
    String str1 = n.f2.accept(this, null);
    if(str==null||str1==null) throw new Exception("Error1: Something wrong with name or type of Method Declaration");
    argu._method = str1;
    MethodInfo minfo = new MethodInfo();
    minfo.return_type = str;
    minfo.vartable = new LinkedHashMap<String,String>();
    minfo.formaltable = new LinkedHashMap<String,String>();
    String extend_name=argu.table.get(argu._class).extend_class_name;
    if(argu.table.get(argu._class).methodtable.containsKey(str1)){                              //elexnei an iparxei methodo me to idio onoma(kathe methodos einai monadiki)
      throw new Exception("The function " + str1 + " is already exists");
    }
    argu.table.get(argu._class).methodtable.put(str1,minfo);
    n.f3.accept(this, argu);
    n.f4.accept(this, argu);                                                                    //elenxos ths formal parameter list prin prolavei na bei
    while(extend_name != null){
      if(argu.table.get(extend_name).methodtable.containsKey(str1)){
        MethodInfo method1 = argu.table.get(extend_name).methodtable.get(str1);                 //sigkrisi method table tis idias tis klasis kai tis iperklasis gia overloading
        MethodInfo method2 = argu.table.get(argu._class).methodtable.get(str1);
        if(method1==null||method2==null ) throw new Exception("Error1: There are no Method Tables");
        Boolean bool=true;
        if(method1.formaltable.values().size()==method2.formaltable.values().size()){
          String x[] = new String[method1.formaltable.values().size()];                         //pinakas me ta onomata twn typwn twn parametrwn tis methodou tis iperklasis
          String y[] = new String[method2.formaltable.values().size()];                         //pinakas me ta onomata twn typwn twn parametrwn tis methodou tis trexousas klasis
          x = method1.formaltable.values().toArray(x);                                          //metatroph twn map/list se array gia kaliteri prospelasi kai sigrisi twn timwn
          y = method2.formaltable.values().toArray(y);
          for(int i=0; i < y.length; i++){
            if(!(x[i].equals(y[i]))){
              throw new Exception("The methods " + str1 + " from " + argu._class + " to " + extend_name + " are not the same");
            }
          }
        }else{
          throw new Exception("There is overloading in method " + str1 + " from " + argu._class + " to " + extend_name);
        }
        if(!(method1.return_type.equals(method2.return_type) && bool)){
          throw new Exception("There is overloading in method " + str1 + " from " + argu._class + " to " + extend_name);
        }else{
          minfo.overwrite=true;                                                                 //periptwsi pou mia methodo einai overwrite mia allis kai eisagwgi autis sto pinaka methodwn tis klasis
          argu.table.get(argu._class).methodtable.put(str1, minfo);
        }

      }
      extend_name=argu.table.get(extend_name).extend_class_name;                                //anazitisi idias methodous se iperklasis gia sigrisi
    }    
    n.f5.accept(this, argu);
    n.f6.accept(this, argu);
    n.f7.accept(this, argu);
    n.f8.accept(this, argu);
    n.f9.accept(this, argu);
    n.f10.accept(this, argu);
    n.f11.accept(this, argu);
    n.f12.accept(this, argu);
    return _ret;
  }
  /**
  * f0 -> Type()
  * f1 -> Identifier()
  */
  public String visit(FormalParameter n, SymbolInfo argu) throws Exception{
    String _ret=null;
    String str = n.f0.accept(this, argu);
    String str1 = n.f1.accept(this, argu);
    if(str==null||str1==null) throw new Exception("Error1: Something wrong with Formal Paremeter");
    if(argu!=null){
      if(argu._class!=null && argu._method != null){
        MethodInfo minfo = argu.table.get(argu._class).methodtable.get(argu._method);
        if(!minfo.formaltable.containsKey(str1))                                                //elexnos an iparxei sti lista parametrwn metavliti me idio onoma
          minfo.formaltable.put(str1,str);
        else
          throw new Exception("Variable " + str1 + " is already exists in formal parameter of method " + argu._method);
      }else{
        throw new Exception("Error1: There is no method to put the formal parameters");
      }
    }
    return _ret;
  }
  /**
  * f0 -> "int"
  * f1 -> "["
  * f2 -> "]"
  */
  public String visit(ArrayType n, SymbolInfo argu) throws Exception{
    n.f0.accept(this, argu);
    n.f1.accept(this, argu);
    n.f2.accept(this, argu);
    return ( n.f0.toString() + n.f1.toString() + n.f2.toString() );
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
    return n.f0.toString();
  }

}
