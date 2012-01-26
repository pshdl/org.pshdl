package de.tuhh.ict.pshdl.model.impl;

import de.tuhh.ict.pshdl.model.HDLRegisterConfig.HDLRegClockType;
import de.tuhh.ict.pshdl.model.HDLRegisterConfig.HDLRegResetType;
import de.tuhh.ict.pshdl.model.HDLRegisterConfig.HDLRegSyncType;
import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.utils.*;
import java.util.*;

@SuppressWarnings("all")
public abstract class AbstractHDLRegisterConfig extends HDLObject{
	/**
	 * Constructs a new instance of {@link AbstractHDLRegisterConfig}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param clk
	 *            the value for clk. Can <b>not</b> be <code>null</code>.
	 * @param rst
	 *            the value for rst. Can <b>not</b> be <code>null</code>.
	 * @param clockType
	 *            the value for clockType. If <code>null</code>, {@link HDLRegClockType#RISING} is used as default.
	 * @param resetType
	 *            the value for resetType. If <code>null</code>, {@link HDLRegResetType#HIGH_ACTIVE} is used as default.
	 * @param syncType
	 *            the value for syncType. If <code>null</code>, {@link HDLRegSyncType#SYNC} is used as default.
	 * @param resetValue
	 *            the value for resetValue. Can <b>not</b> be <code>null</code>.
	 */
	public AbstractHDLRegisterConfig(HDLObject container, HDLQualifiedName clk, HDLQualifiedName rst, HDLRegClockType clockType, HDLRegResetType resetType, HDLRegSyncType syncType, HDLExpression resetValue) {
		super(container);
		this.clk=validateClk(clk);
		this.rst=validateRst(rst);
		this.clockType=validateClockType(clockType);
		this.resetType=validateResetType(resetType);
		this.syncType=validateSyncType(syncType);
		resetValue=validateResetValue(resetValue);
		if (this.resetValue!=null)
			this.resetValue=(HDLExpression)resetValue.setContainer(this);
		else
			this.resetValue=null;
	}
	public AbstractHDLRegisterConfig() {
		super();
		this.clk=null;
		this.rst=null;
		this.clockType=null;
		this.resetType=null;
		this.syncType=null;
		this.resetValue=null;
	}
	protected final HDLQualifiedName clk;
	public HDLVariable resolveClk(){
			return container.resolveVariable(clk);
	}
	protected HDLQualifiedName validateClk(HDLQualifiedName clk){
		if (clk==null)
			throw new IllegalArgumentException("The field clk can not be null!");
		return clk;
	}
	protected final HDLQualifiedName rst;
	public HDLVariable resolveRst(){
			return container.resolveVariable(rst);
	}
	protected HDLQualifiedName validateRst(HDLQualifiedName rst){
		if (rst==null)
			throw new IllegalArgumentException("The field rst can not be null!");
		return rst;
	}
	protected final HDLRegClockType clockType;
	/**
	 * Get the clockType field.
	 * 
	 * @return the field
	 */
	public HDLRegClockType getClockType(){
			return clockType;
	}
	protected HDLRegClockType validateClockType(HDLRegClockType clockType){
		return clockType==null?HDLRegClockType.RISING:clockType;
	}
	protected final HDLRegResetType resetType;
	/**
	 * Get the resetType field.
	 * 
	 * @return the field
	 */
	public HDLRegResetType getResetType(){
			return resetType;
	}
	protected HDLRegResetType validateResetType(HDLRegResetType resetType){
		return resetType==null?HDLRegResetType.HIGH_ACTIVE:resetType;
	}
	protected final HDLRegSyncType syncType;
	/**
	 * Get the syncType field.
	 * 
	 * @return the field
	 */
	public HDLRegSyncType getSyncType(){
			return syncType;
	}
	protected HDLRegSyncType validateSyncType(HDLRegSyncType syncType){
		return syncType==null?HDLRegSyncType.SYNC:syncType;
	}
	protected final HDLExpression resetValue;
	/**
	 * Get the resetValue field.
	 * 
	 * @return the field
	 */
	public HDLExpression getResetValue(){
			return resetValue;
	}
	protected HDLExpression validateResetValue(HDLExpression resetValue){
		if (resetValue==null)
			throw new IllegalArgumentException("The field resetValue can not be null!");
		return resetValue;
	}
	/**
	 * Creates a copy of this class with the same fields.
	 * 
	 * @return a new instance of this class.
	 */
	public HDLRegisterConfig copy(){
			return new HDLRegisterConfig(container, clk, rst, clockType, resetType, syncType, resetValue);
	}
	/**
	 * Setter for the field {@link #getContainer()}.
	 * 
	 * @param container
	 *            sets the new container of this object. Can be <code>null</code>.
	 * @return 
	 *			  the same instance of {@link HDLRegisterConfig} with the updated container field.
	 */
	public HDLRegisterConfig setContainer(HDLObject container){
		this.container=container;
		return (HDLRegisterConfig)this;
	}
	/**
	 * Setter for the field {@link #getClk()}.
	 * 
	 * @param clk
	 *            sets the new clk of this object. Can <b>not</b> be <code>null</code>.
	 * @return 
	 *			  a new instance of {@link HDLRegisterConfig} with the updated clk field.
	 */
	public HDLRegisterConfig setClk(HDLQualifiedName clk){
		return new HDLRegisterConfig(container, clk, rst, clockType, resetType, syncType, resetValue);
	}
	/**
	 * Setter for the field {@link #getRst()}.
	 * 
	 * @param rst
	 *            sets the new rst of this object. Can <b>not</b> be <code>null</code>.
	 * @return 
	 *			  a new instance of {@link HDLRegisterConfig} with the updated rst field.
	 */
	public HDLRegisterConfig setRst(HDLQualifiedName rst){
		return new HDLRegisterConfig(container, clk, rst, clockType, resetType, syncType, resetValue);
	}
	/**
	 * Setter for the field {@link #getClockType()}.
	 * 
	 * @param clockType
	 *            sets the new clockType of this object. If <code>null</code>, {@link HDLRegClockType#RISING} is used as default.
	 * @return 
	 *			  a new instance of {@link HDLRegisterConfig} with the updated clockType field.
	 */
	public HDLRegisterConfig setClockType(HDLRegClockType clockType){
		return new HDLRegisterConfig(container, clk, rst, clockType, resetType, syncType, resetValue);
	}
	/**
	 * Setter for the field {@link #getResetType()}.
	 * 
	 * @param resetType
	 *            sets the new resetType of this object. If <code>null</code>, {@link HDLRegResetType#HIGH_ACTIVE} is used as default.
	 * @return 
	 *			  a new instance of {@link HDLRegisterConfig} with the updated resetType field.
	 */
	public HDLRegisterConfig setResetType(HDLRegResetType resetType){
		return new HDLRegisterConfig(container, clk, rst, clockType, resetType, syncType, resetValue);
	}
	/**
	 * Setter for the field {@link #getSyncType()}.
	 * 
	 * @param syncType
	 *            sets the new syncType of this object. If <code>null</code>, {@link HDLRegSyncType#SYNC} is used as default.
	 * @return 
	 *			  a new instance of {@link HDLRegisterConfig} with the updated syncType field.
	 */
	public HDLRegisterConfig setSyncType(HDLRegSyncType syncType){
		return new HDLRegisterConfig(container, clk, rst, clockType, resetType, syncType, resetValue);
	}
	/**
	 * Setter for the field {@link #getResetValue()}.
	 * 
	 * @param resetValue
	 *            sets the new resetValue of this object. Can <b>not</b> be <code>null</code>.
	 * @return 
	 *			  a new instance of {@link HDLRegisterConfig} with the updated resetValue field.
	 */
	public HDLRegisterConfig setResetValue(HDLExpression resetValue){
		return new HDLRegisterConfig(container, clk, rst, clockType, resetType, syncType, resetValue);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof AbstractHDLRegisterConfig))
			return false;
		if (!super.equals(obj))
			return false;
		AbstractHDLRegisterConfig other = (AbstractHDLRegisterConfig) obj;
		if (clk == null) {
			if (other.clk != null)
				return false;
		} else if (!clk.equals(other.clk))
			return false;
		if (rst == null) {
			if (other.rst != null)
				return false;
		} else if (!rst.equals(other.rst))
			return false;
		if (clockType == null) {
			if (other.clockType != null)
				return false;
		} else if (!clockType.equals(other.clockType))
			return false;
		if (resetType == null) {
			if (other.resetType != null)
				return false;
		} else if (!resetType.equals(other.resetType))
			return false;
		if (syncType == null) {
			if (other.syncType != null)
				return false;
		} else if (!syncType.equals(other.syncType))
			return false;
		if (resetValue == null) {
			if (other.resetValue != null)
				return false;
		} else if (!resetValue.equals(other.resetValue))
			return false;
		return true;
	}
	@Override
	public int hashCode() {
		int result = super.hashCode();
		final int prime = 31;
		result = prime * result + ((clk == null) ? 0 : clk.hashCode());
		result = prime * result + ((rst == null) ? 0 : rst.hashCode());
		result = prime * result + ((clockType == null) ? 0 : clockType.hashCode());
		result = prime * result + ((resetType == null) ? 0 : resetType.hashCode());
		result = prime * result + ((syncType == null) ? 0 : syncType.hashCode());
		result = prime * result + ((resetValue == null) ? 0 : resetValue.hashCode());
		return result;
	}
}