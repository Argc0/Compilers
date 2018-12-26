import java.io.*;
import java.util.*;


public class SymbolInfo{                       	//voithitiki domi pou krataei ton sybol table ki alles xrisimes metavlites
	Map<String,ClassInfo> table ;
	String _class;								//krataei to onoma tis klasis gia na einai orath sta paidia tis
  	String _method;								//krataei to onoma tis methodou gia na einai orath sta paidia tis
  	ArrayList<FormalInfo> strp;            			//table for formal parameter list ki gia to llvm
  	Boolean want_identifier;                    //metavliti pou xrisimopoihte gia epistrofi onoma metavlitis h tou typos metavlitis
  	String name_table;                          //onoma metavlitis pinaka voithitiko
  	
}
class ClassInfo{                                //periexomeno tou pinaka symvolwn
	Map<String,VarInfo> vartable;       		//<varname,vartype,varoffset>
	Map<String,MethodInfo> methodtable;  		//<methodname,methodinfo>
	String extend_class_name;
	int offset_end_var;
	int offset_end_meth;
}
class MethodInfo{
	String return_type;
	Boolean overwrite;							//ama h method exei ginei overwrite
	int offset;
	Map<String,String> vartable;       			//<varname,vartype>
	Map<String,String> formaltable;       		//<varname,vartype>
}
class VarInfo{
	String type;
	int offset;
}
class FormalInfo{
	String type;
	String name;
}