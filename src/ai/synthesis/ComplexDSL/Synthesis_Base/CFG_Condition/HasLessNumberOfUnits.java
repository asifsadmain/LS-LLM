package ai.synthesis.ComplexDSL.Synthesis_Base.CFG_Condition;

import ai.abstraction.AbstractAction;
import ai.abstraction.Build;
import ai.abstraction.Train;
import ai.synthesis.ComplexDSL.Synthesis_Base.AIs.Interpreter;
import ai.synthesis.ComplexDSL.Synthesis_Base.CFG.*;
import rts.GameState;
import rts.PhysicalGameState;
import rts.units.Unit;

import java.util.ArrayList;
import java.util.List;

public class HasLessNumberOfUnits implements ChildB {
	
	Type type;
	N n;
	boolean value;

	public HasLessNumberOfUnits() {
		// TODO Auto-generated constructor stub
		type = new Type();
		n = new N();
	}

	
	
	public HasLessNumberOfUnits(Type type, N n) {
		super();
		this.type = type;
		this.n = n;
		
	}



	public Type getType() {
		return type;
	}



	public void setType(Type type) {
		this.type = type;
	}



	public N getN() {
		return n;
	}



	public void setN(N n) {
		this.n = n;
	}



	@Override
	public String translate() {
		// TODO Auto-generated method stub
		return "HasLessNumberOfUnits("+type.getValue()+","+n.getValue()+")";
	}

	@Override
	public void interpret(GameState gs, int player, Unit u, Interpreter automato) throws Exception {
		// TODO Auto-generated method stub
		PhysicalGameState pgs = gs.getPhysicalGameState();
	       
        int cont =0;
    	int n_int= Integer.parseInt(n.getValue());
  
    	
    	
		 for(Unit u2:pgs.getUnits()) {

			 if(u2.getPlayer() == player){
	            if (  u2.getType().name.equals(this.type.getValue())) {
	            
	            	cont++;
	         
	            }
	            //UnitAction a1 = gs.getActionAssignment(u2).action;
	            AbstractAction a2 = automato.getAbstractAction(u2);
	            boolean aux=false;
	            
	            if(a2 instanceof Build) {
	            	Build b =(Build)a2;
	            	if(b.type.name.equals(this.type.getValue())) {
	            		aux=true;
	            	}
	            	
	            }
	            if(a2 instanceof Train) {
	            	Train b =(Train)a2;
	         
	            	if(b.type.name.equals(this.type.getValue())) {
	            		aux=true;
	            	}
	            	
	            }
	            
	            
	            
	            if(aux)cont++;
			 }

		 }
		 
		 this.value = cont<n_int;

	}

	@Override
	public boolean isComplete() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "HasLessNumberOfUnits";
	}

	@Override
	public String translateIndentation(int tap) {
		// TODO Auto-generated method stub
		return this.translate();
	}

	@Override
	public boolean getValue() {
		// TODO Auto-generated method stub
		return this.value;
	}



	@Override
	public Node Clone(Factory f) {
		// TODO Auto-generated method stub
		return f.build_HasLessNumberOfUnits((Type)this.type.Clone(f),(N)this.n.Clone(f));
	}



	@Override
	public boolean equals(Node n) {
		// TODO Auto-generated method stub
		if (!(n instanceof HasLessNumberOfUnits)) return false;
		HasLessNumberOfUnits n2 = (HasLessNumberOfUnits) n;
		return this.n.equals(n2.n) && this.type.equals(n2.type);
	}



	@Override
	public List<ChildB> AllCombinations(Factory f) {
		// TODO Auto-generated method stub
		Type type1 = (Type) f.build_Type();
		N n1 = (N) f.build_N();
		List<ChildB> l = new ArrayList<>();
		for(AlmostTerminal s : type1.AllCombinations(f) ) {
			for(AlmostTerminal s2 : n1.AllCombinations(f) ) {
				HasLessNumberOfUnits hlnou = (HasLessNumberOfUnits) f.build_HasLessNumberOfUnits((Type)s.Clone(f),(N)s2.Clone(f));
				l.add(hlnou);
			}
		}
		
		return l;
		
		
		
	}



	@Override
	public void resert() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean clear(Node father,Factory f) {
		// TODO Auto-generated method stub
		return true;
	}



	@Override
	public void load(List<String> list,Factory f) {
		// TODO Auto-generated method stub
		String s = list.get(0);
		list.remove(0);
		this.type = (Type) f.build_Type(s);
		String s1 = list.get(0);
		list.remove(0);
		this.n = (N) f.build_N(s1);
	}



	@Override
	public void salve(List<String> list) {
		// TODO Auto-generated method stub
		list.add(this.getName());
		list.add(this.type.getValue());
		list.add(this.n.getN());
		
	}
	
}
