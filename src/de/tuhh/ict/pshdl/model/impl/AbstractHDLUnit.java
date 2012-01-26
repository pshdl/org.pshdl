package de.tuhh.ict.pshdl.model.impl;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.utils.*;
import java.util.*;

@SuppressWarnings("all")
public abstract class AbstractHDLUnit extends HDLObject{
	/**
	 * Constructs a new instance of {@link AbstractHDLUnit}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param name
	 *            the value for name. Can <b>not</b> be <code>null</code>.
	 * @param imports
	 *            the value for imports. Can be <code>null</code>.
	 * @param statements
	 *            the value for statements. Can be <code>null</code>.
	 */
	public AbstractHDLUnit(HDLObject container, String name, ArrayList<String> imports, ArrayList<HDLStatement> statements) {
		super(container);
		this.name=validateName(name);
		imports=validateImports(imports);
		this.imports=new ArrayList<String>(imports.size());
		for(String newValue:imports){
			this.imports.add(newValue);
		}
		statements=validateStatements(statements);
		this.statements=new ArrayList<HDLStatement>(statements.size());
		for(HDLStatement newValue:statements){
			this.statements.add((HDLStatement)newValue.setContainer(this));
		}
	}
	public AbstractHDLUnit() {
		super();
		this.name=null;
		this.imports=null;
		this.statements=null;
	}
	protected final String name;
	/**
	 * Get the name field.
	 * 
	 * @return the field
	 */
	public String getName(){
			return name;
	}
	protected String validateName(String name){
		if (name==null)
			throw new IllegalArgumentException("The field name can not be null!");
		return name;
	}
	protected final ArrayList<String> imports;
	/**
	 * Get the imports field.
	 * 
	 * @return a clone of the field. Will never return <code>null</code>.
	 */
	public ArrayList<String> getImports(){
			return (ArrayList<String>) imports.clone();
	}
	protected ArrayList<String> validateImports(ArrayList<String> imports){
		if (imports==null)
			return new ArrayList<String>();
		return imports;
	}
	protected final ArrayList<HDLStatement> statements;
	/**
	 * Get the statements field.
	 * 
	 * @return a clone of the field. Will never return <code>null</code>.
	 */
	public ArrayList<HDLStatement> getStatements(){
			return (ArrayList<HDLStatement>) statements.clone();
	}
	protected ArrayList<HDLStatement> validateStatements(ArrayList<HDLStatement> statements){
		if (statements==null)
			return new ArrayList<HDLStatement>();
		return statements;
	}
	/**
	 * Creates a copy of this class with the same fields.
	 * 
	 * @return a new instance of this class.
	 */
	public HDLUnit copy(){
			return new HDLUnit(container, name, imports, statements);
	}
	/**
	 * Setter for the field {@link #getContainer()}.
	 * 
	 * @param container
	 *            sets the new container of this object. Can be <code>null</code>.
	 * @return 
	 *			  the same instance of {@link HDLUnit} with the updated container field.
	 */
	public HDLUnit setContainer(HDLObject container){
		this.container=container;
		return (HDLUnit)this;
	}
	/**
	 * Setter for the field {@link #getName()}.
	 * 
	 * @param name
	 *            sets the new name of this object. Can <b>not</b> be <code>null</code>.
	 * @return 
	 *			  a new instance of {@link HDLUnit} with the updated name field.
	 */
	public HDLUnit setName(String name){
		return new HDLUnit(container, name, imports, statements);
	}
	/**
	 * Setter for the field {@link #getImports()}.
	 * 
	 * @param imports
	 *            sets the new imports of this object. Can be <code>null</code>.
	 * @return 
	 *			  a new instance of {@link HDLUnit} with the updated imports field.
	 */
	public HDLUnit setImports(ArrayList<String> imports){
		return new HDLUnit(container, name, imports, statements);
	}
	
	/**
	 * Adds a new value to the field {@link #getImports()}.
	 * 
	 * @param newImports
	 *            the value that should be added to the field {@link #getImports()}
	 * @return a new instance of {@link HDLUnit} with the updated imports field.
	 */
	public HDLUnit addImports(String newImports){
		ArrayList<String> imports=(ArrayList<String>)this.imports.clone();
		imports.add(newImports);
		return new HDLUnit(container, name, imports, statements);
	}
	
	/**
	 * Removes a value from the field {@link #getImports()}.
	 * 
	 * @param newImports
	 *            the value that should be removed from the field {@link #getImports()}
	 * @return a new instance of {@link HDLUnit} with the updated imports field.
	 */	
	public HDLUnit removeImports(String newImports){
		ArrayList<String> imports=(ArrayList<String>)this.imports.clone();
		imports.remove(newImports);
		return new HDLUnit(container, name, imports, statements);
	}
	
	/**
	 * Removes a value from the field {@link #getImports()}.
	 * 
	 * @param idx
	 *            the index of the value that should be removed from the field {@link #getImports()}
	 * @return a new instance of {@link HDLUnit} with the updated imports field.
	 */	
	public HDLUnit removeImports(int idx){
		ArrayList<String> imports=(ArrayList<String>)this.imports.clone();
		imports.remove(idx);
		return new HDLUnit(container, name, imports, statements);
	}	
	/**
	 * Setter for the field {@link #getStatements()}.
	 * 
	 * @param statements
	 *            sets the new statements of this object. Can be <code>null</code>.
	 * @return 
	 *			  a new instance of {@link HDLUnit} with the updated statements field.
	 */
	public HDLUnit setStatements(ArrayList<HDLStatement> statements){
		return new HDLUnit(container, name, imports, statements);
	}
	
	/**
	 * Adds a new value to the field {@link #getStatements()}.
	 * 
	 * @param newStatements
	 *            the value that should be added to the field {@link #getStatements()}
	 * @return a new instance of {@link HDLUnit} with the updated statements field.
	 */
	public HDLUnit addStatements(HDLStatement newStatements){
		ArrayList<HDLStatement> statements=(ArrayList<HDLStatement>)this.statements.clone();
		statements.add(newStatements);
		return new HDLUnit(container, name, imports, statements);
	}
	
	/**
	 * Removes a value from the field {@link #getStatements()}.
	 * 
	 * @param newStatements
	 *            the value that should be removed from the field {@link #getStatements()}
	 * @return a new instance of {@link HDLUnit} with the updated statements field.
	 */	
	public HDLUnit removeStatements(HDLStatement newStatements){
		ArrayList<HDLStatement> statements=(ArrayList<HDLStatement>)this.statements.clone();
		statements.remove(newStatements);
		return new HDLUnit(container, name, imports, statements);
	}
	
	/**
	 * Removes a value from the field {@link #getStatements()}.
	 * 
	 * @param idx
	 *            the index of the value that should be removed from the field {@link #getStatements()}
	 * @return a new instance of {@link HDLUnit} with the updated statements field.
	 */	
	public HDLUnit removeStatements(int idx){
		ArrayList<HDLStatement> statements=(ArrayList<HDLStatement>)this.statements.clone();
		statements.remove(idx);
		return new HDLUnit(container, name, imports, statements);
	}	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof AbstractHDLUnit))
			return false;
		if (!super.equals(obj))
			return false;
		AbstractHDLUnit other = (AbstractHDLUnit) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (imports == null) {
			if (other.imports != null)
				return false;
		} else if (!imports.equals(other.imports))
			return false;
		if (statements == null) {
			if (other.statements != null)
				return false;
		} else if (!statements.equals(other.statements))
			return false;
		return true;
	}
	@Override
	public int hashCode() {
		int result = super.hashCode();
		final int prime = 31;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((imports == null) ? 0 : imports.hashCode());
		result = prime * result + ((statements == null) ? 0 : statements.hashCode());
		return result;
	}
}