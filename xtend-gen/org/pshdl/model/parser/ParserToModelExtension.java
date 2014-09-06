/**
 * PSHDL is a library and (trans-)compiler for PSHDL input. It generates
 *     output suitable for implementation or simulation of it.
 * 
 *     Copyright (C) 2013 Karsten Becker (feedback (at) pshdl (dot) org)
 * 
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 *     This License does not grant permission to use the trade names, trademarks,
 *     service marks, or product names of the Licensor, except as required for
 *     reasonable and customary use in describing the origin of the Work.
 * 
 * Contributors:
 *     Karsten Becker - initial API and implementation
 */
package org.pshdl.model.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.antlr.v4.runtime.BufferedTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.pshdl.model.HDLAnnotation;
import org.pshdl.model.HDLArgument;
import org.pshdl.model.HDLArithOp;
import org.pshdl.model.HDLArrayInit;
import org.pshdl.model.HDLAssignment;
import org.pshdl.model.HDLBitOp;
import org.pshdl.model.HDLBlock;
import org.pshdl.model.HDLConcat;
import org.pshdl.model.HDLDeclaration;
import org.pshdl.model.HDLDirectGeneration;
import org.pshdl.model.HDLEnum;
import org.pshdl.model.HDLEnumDeclaration;
import org.pshdl.model.HDLEqualityOp;
import org.pshdl.model.HDLExpression;
import org.pshdl.model.HDLForLoop;
import org.pshdl.model.HDLFunctionParameter;
import org.pshdl.model.HDLIfStatement;
import org.pshdl.model.HDLInlineFunction;
import org.pshdl.model.HDLInstantiation;
import org.pshdl.model.HDLInterface;
import org.pshdl.model.HDLInterfaceDeclaration;
import org.pshdl.model.HDLInterfaceInstantiation;
import org.pshdl.model.HDLLiteral;
import org.pshdl.model.HDLManip;
import org.pshdl.model.HDLNativeFunction;
import org.pshdl.model.HDLPackage;
import org.pshdl.model.HDLPrimitive;
import org.pshdl.model.HDLRange;
import org.pshdl.model.HDLReference;
import org.pshdl.model.HDLRegisterConfig;
import org.pshdl.model.HDLShiftOp;
import org.pshdl.model.HDLStatement;
import org.pshdl.model.HDLSubstituteFunction;
import org.pshdl.model.HDLSwitchCaseStatement;
import org.pshdl.model.HDLSwitchStatement;
import org.pshdl.model.HDLTernary;
import org.pshdl.model.HDLType;
import org.pshdl.model.HDLUnit;
import org.pshdl.model.HDLUnresolvedFragment;
import org.pshdl.model.HDLUnresolvedFragmentFunction;
import org.pshdl.model.HDLVariable;
import org.pshdl.model.HDLVariableDeclaration;
import org.pshdl.model.HDLVariableRef;
import org.pshdl.model.IHDLObject;
import org.pshdl.model.utils.HDLLibrary;
import org.pshdl.model.utils.HDLQualifiedName;

@SuppressWarnings("all")
public class ParserToModelExtension {
	private final BufferedTokenStream tokens;

	public ParserToModelExtension(final BufferedTokenStream tokens) {
		this.tokens = tokens;
	}

	public static HDLPackage toHDL(final BufferedTokenStream tokens, final PSHDLLangParser.PsModelContext ctx, final String libURI, final String src) {
		final ParserToModelExtension _parserToModelExtension = new ParserToModelExtension(tokens);
		return _parserToModelExtension.toHDLPkg(ctx, libURI, src);
	}

	public static HDLExpression toHDLExpression(final BufferedTokenStream tokens, final PSHDLLangParser.PsExpressionContext ctx) {
		final ParserToModelExtension _parserToModelExtension = new ParserToModelExtension(tokens);
		final IHDLObject _hDL = _parserToModelExtension.toHDL(ctx, false);
		return ((HDLExpression) _hDL);
	}

	public HDLPackage toHDLPkg(final PSHDLLangParser.PsModelContext ctx, final String libURI, final String src) {
		final HDLPackage _hDLPackage = new HDLPackage();
		HDLPackage pkg = _hDLPackage.setLibURI(libURI);
		final PSHDLLangParser.PsQualifiedNameContext _psQualifiedName = ctx.psQualifiedName();
		final boolean _tripleNotEquals = (_psQualifiedName != null);
		if (_tripleNotEquals) {
			final PSHDLLangParser.PsQualifiedNameContext _psQualifiedName_1 = ctx.psQualifiedName();
			final String _name = this.toName(_psQualifiedName_1);
			final HDLPackage _setPkg = pkg.setPkg(_name);
			pkg = _setPkg;
		}
		final List<PSHDLLangParser.PsUnitContext> _psUnit = ctx.psUnit();
		final Function1<PSHDLLangParser.PsUnitContext, HDLUnit> _function = new Function1<PSHDLLangParser.PsUnitContext, HDLUnit>() {
			@Override
			public HDLUnit apply(final PSHDLLangParser.PsUnitContext it) {
				return ParserToModelExtension.this.toHDLUnit(it, libURI);
			}
		};
		final List<HDLUnit> _map = ListExtensions.<PSHDLLangParser.PsUnitContext, HDLUnit> map(_psUnit, _function);
		final HDLPackage _setUnits = pkg.setUnits(_map);
		pkg = _setUnits;
		final List<PSHDLLangParser.PsDeclarationContext> _psDeclaration = ctx.psDeclaration();
		final Function1<PSHDLLangParser.PsDeclarationContext, HDLDeclaration> _function_1 = new Function1<PSHDLLangParser.PsDeclarationContext, HDLDeclaration>() {
			@Override
			public HDLDeclaration apply(final PSHDLLangParser.PsDeclarationContext it) {
				final IHDLObject _hDL = ParserToModelExtension.this.toHDL(it, true);
				return ((HDLDeclaration) _hDL);
			}
		};
		final List<HDLDeclaration> _map_1 = ListExtensions.<PSHDLLangParser.PsDeclarationContext, HDLDeclaration> map(_psDeclaration, _function_1);
		final HDLPackage _setDeclarations = pkg.setDeclarations(_map_1);
		pkg = _setDeclarations;
		pkg.freeze(null);
		final HDLLibrary library = HDLLibrary.getLibrary(libURI);
		final boolean _tripleEquals = (library == null);
		if (_tripleEquals)
			throw new IllegalArgumentException((("The library " + libURI) + " is not valid"));
		library.addPkg(pkg, src);
		final HDLPackage _attachContext = this.<HDLPackage> attachContext(pkg, ctx);
		return (_attachContext);
	}

	protected HDLDeclaration _toHDL(final PSHDLLangParser.PsDeclarationContext context, final boolean isStatement) {
		final PSHDLLangParser.PsDeclarationTypeContext _psDeclarationType = context.psDeclarationType();
		final IHDLObject _hDL = this.toHDL(_psDeclarationType, isStatement);
		HDLDeclaration res = ((HDLDeclaration) _hDL);
		final List<PSHDLLangParser.PsAnnotationContext> _psAnnotation = context.psAnnotation();
		final Function1<PSHDLLangParser.PsAnnotationContext, HDLAnnotation> _function = new Function1<PSHDLLangParser.PsAnnotationContext, HDLAnnotation>() {
			@Override
			public HDLAnnotation apply(final PSHDLLangParser.PsAnnotationContext it) {
				final IHDLObject _hDL = ParserToModelExtension.this.toHDL(it, false);
				return ((HDLAnnotation) _hDL);
			}
		};
		final List<HDLAnnotation> _map = ListExtensions.<PSHDLLangParser.PsAnnotationContext, HDLAnnotation> map(_psAnnotation, _function);
		final HDLDeclaration _setAnnotations = res.setAnnotations(_map);
		res = _setAnnotations;
		return this.<HDLDeclaration> attachContext(res, context);
	}

	public <T extends IHDLObject> T attachContext(final T obj, final ParserRuleContext context) {
		final boolean _tripleEquals = (obj == null);
		if (_tripleEquals)
			throw new NullPointerException("Null is not allowed");
		final SourceInfo _sourceInfo = new SourceInfo(this.tokens, context);
		obj.<SourceInfo> addMeta(SourceInfo.INFO, _sourceInfo);
		return obj;
	}

	protected HDLArgument _toHDL(final PSHDLLangParser.PsArgumentContext context, final boolean isStatement) {
		final HDLArgument _hDLArgument = new HDLArgument();
		final TerminalNode _RULE_ID = context.RULE_ID();
		final String _text = _RULE_ID.getText();
		HDLArgument res = _hDLArgument.setName(_text);
		final PSHDLLangParser.PsExpressionContext _psExpression = context.psExpression();
		final IHDLObject _hDL = this.toHDL(_psExpression, false);
		final HDLArgument _setExpression = res.setExpression(((HDLExpression) _hDL));
		res = _setExpression;
		return this.<HDLArgument> attachContext(res, context);
	}

	protected HDLBlock _toHDL(final PSHDLLangParser.PsProcessContext context, final boolean isStatement) {
		HDLBlock block = new HDLBlock();
		final boolean _tripleNotEquals = (context.isProcess != null);
		if (_tripleNotEquals) {
			final HDLBlock _setProcess = block.setProcess(true);
			block = _setProcess;
		}
		final List<PSHDLLangParser.PsBlockContext> _psBlock = context.psBlock();
		final Function1<PSHDLLangParser.PsBlockContext, HDLStatement> _function = new Function1<PSHDLLangParser.PsBlockContext, HDLStatement>() {
			@Override
			public HDLStatement apply(final PSHDLLangParser.PsBlockContext it) {
				final IHDLObject _hDL = ParserToModelExtension.this.toHDL(it, true);
				return ((HDLStatement) _hDL);
			}
		};
		final List<HDLStatement> _map = ListExtensions.<PSHDLLangParser.PsBlockContext, HDLStatement> map(_psBlock, _function);
		final HDLBlock _setStatements = block.setStatements(_map);
		block = _setStatements;
		return this.<HDLBlock> attachContext(block, context);
	}

	protected HDLAnnotation _toHDL(final PSHDLLangParser.PsAnnotationContext context, final boolean isStatement) {
		final PSHDLLangParser.PsAnnotationTypeContext _psAnnotationType = context.psAnnotationType();
		final String name = _psAnnotationType.getText();
		String value = null;
		final TerminalNode _RULE_STRING = context.RULE_STRING();
		final boolean _tripleNotEquals = (_RULE_STRING != null);
		if (_tripleNotEquals) {
			final TerminalNode _RULE_STRING_1 = context.RULE_STRING();
			String str = _RULE_STRING_1.getText();
			final int _length = str.length();
			final int _minus = (_length - 1);
			final String _substring = str.substring(1, _minus);
			str = _substring;
			value = str;
		}
		final HDLAnnotation _hDLAnnotation = new HDLAnnotation();
		final HDLAnnotation _setName = _hDLAnnotation.setName(name);
		final HDLAnnotation _setValue = _setName.setValue(value);
		return this.<HDLAnnotation> attachContext(_setValue, context);
	}

	protected HDLDeclaration _toHDL(final PSHDLLangParser.PsDeclarationTypeContext context, final boolean isStatement) {
		final PSHDLLangParser.PsFunctionDeclarationContext _psFunctionDeclaration = context.psFunctionDeclaration();
		final boolean _tripleNotEquals = (_psFunctionDeclaration != null);
		if (_tripleNotEquals) {
			final PSHDLLangParser.PsFunctionDeclarationContext _psFunctionDeclaration_1 = context.psFunctionDeclaration();
			final IHDLObject _hDL = this.toHDL(_psFunctionDeclaration_1, true);
			final IHDLObject _attachContext = this.<IHDLObject> attachContext(_hDL, context);
			return ((HDLDeclaration) _attachContext);
		}
		final PSHDLLangParser.PsTypeDeclarationContext _psTypeDeclaration = context.psTypeDeclaration();
		final boolean _tripleNotEquals_1 = (_psTypeDeclaration != null);
		if (_tripleNotEquals_1) {
			final PSHDLLangParser.PsTypeDeclarationContext _psTypeDeclaration_1 = context.psTypeDeclaration();
			final IHDLObject _hDL_1 = this.toHDL(_psTypeDeclaration_1, true);
			final IHDLObject _attachContext_1 = this.<IHDLObject> attachContext(_hDL_1, context);
			return ((HDLDeclaration) _attachContext_1);
		}
		final PSHDLLangParser.PsVariableDeclarationContext _psVariableDeclaration = context.psVariableDeclaration();
		final boolean _tripleNotEquals_2 = (_psVariableDeclaration != null);
		if (_tripleNotEquals_2) {
			final PSHDLLangParser.PsVariableDeclarationContext _psVariableDeclaration_1 = context.psVariableDeclaration();
			final IHDLObject _hDL_2 = this.toHDL(_psVariableDeclaration_1, true);
			final IHDLObject _attachContext_2 = this.<IHDLObject> attachContext(_hDL_2, context);
			return ((HDLDeclaration) _attachContext_2);
		}
		final Class<? extends PSHDLLangParser.PsDeclarationTypeContext> _class = context.getClass();
		final String _plus = ("Not implemented:" + _class);
		throw new IllegalArgumentException(_plus);
	}

	protected HDLVariableDeclaration _toHDL(final PSHDLLangParser.PsVariableDeclarationContext context, final boolean isStatement) {
		HDLVariableDeclaration res = new HDLVariableDeclaration();
		final PSHDLLangParser.PsPrimitiveContext _psPrimitive = context.psPrimitive();
		final IHDLObject _hDL = this.toHDL(_psPrimitive, false);
		final HDLVariableDeclaration _setType = res.setType(((HDLType) _hDL));
		res = _setType;
		HDLVariableDeclaration.HDLDirection dir = HDLVariableDeclaration.HDLDirection.INTERNAL;
		final PSHDLLangParser.PsDirectionContext _psDirection = context.psDirection();
		final boolean _tripleNotEquals = (_psDirection != null);
		if (_tripleNotEquals) {
			final PSHDLLangParser.PsDirectionContext _psDirection_1 = context.psDirection();
			final String _text = _psDirection_1.getText();
			final HDLVariableDeclaration.HDLDirection _op = HDLVariableDeclaration.HDLDirection.getOp(_text);
			dir = _op;
		}
		final HDLVariableDeclaration _setDirection = res.setDirection(dir);
		res = _setDirection;
		final List<PSHDLLangParser.PsDeclAssignmentContext> _psDeclAssignment = context.psDeclAssignment();
		for (final PSHDLLangParser.PsDeclAssignmentContext varDecl : _psDeclAssignment) {
			final IHDLObject _hDL_1 = this.toHDL(varDecl, false);
			final HDLVariableDeclaration _addVariables = res.addVariables(((HDLVariable) _hDL_1));
			res = _addVariables;
		}
		final PSHDLLangParser.PsPrimitiveContext _psPrimitive_1 = context.psPrimitive();
		final boolean _tripleNotEquals_1 = (_psPrimitive_1.isRegister != null);
		if (_tripleNotEquals_1) {
			Iterable<HDLArgument> args = new ArrayList<HDLArgument>();
			final PSHDLLangParser.PsPrimitiveContext _psPrimitive_2 = context.psPrimitive();
			final PSHDLLangParser.PsPassedArgumentsContext _psPassedArguments = _psPrimitive_2.psPassedArguments();
			final boolean _tripleNotEquals_2 = (_psPassedArguments != null);
			if (_tripleNotEquals_2) {
				final PSHDLLangParser.PsPrimitiveContext _psPrimitive_3 = context.psPrimitive();
				final PSHDLLangParser.PsPassedArgumentsContext _psPassedArguments_1 = _psPrimitive_3.psPassedArguments();
				final List<PSHDLLangParser.PsArgumentContext> _psArgument = _psPassedArguments_1.psArgument();
				final Function1<PSHDLLangParser.PsArgumentContext, HDLArgument> _function = new Function1<PSHDLLangParser.PsArgumentContext, HDLArgument>() {
					@Override
					public HDLArgument apply(final PSHDLLangParser.PsArgumentContext it) {
						final IHDLObject _hDL = ParserToModelExtension.this.toHDL(it, false);
						return ((HDLArgument) _hDL);
					}
				};
				final List<HDLArgument> _map = ListExtensions.<PSHDLLangParser.PsArgumentContext, HDLArgument> map(_psArgument, _function);
				args = _map;
			}
			final HDLRegisterConfig _fromArgs = HDLRegisterConfig.fromArgs(args);
			final HDLVariableDeclaration _setRegister = res.setRegister(_fromArgs);
			res = _setRegister;
		}
		return this.<HDLVariableDeclaration> attachContext(res, context);
	}

	protected IHDLObject _toHDL(final PSHDLLangParser.PsArrayInitContext context, final boolean isStatement) {
		final PSHDLLangParser.PsExpressionContext _psExpression = context.psExpression();
		final boolean _tripleNotEquals = (_psExpression != null);
		if (_tripleNotEquals) {
			final PSHDLLangParser.PsExpressionContext _psExpression_1 = context.psExpression();
			final IHDLObject _hDL = this.toHDL(_psExpression_1, false);
			return this.<IHDLObject> attachContext(_hDL, context);
		}
		final PSHDLLangParser.PsArrayInitSubParensContext _psArrayInitSubParens = context.psArrayInitSubParens();
		return this.toHDL(_psArrayInitSubParens, false);
	}

	protected IHDLObject _toHDL(final PSHDLLangParser.PsArrayInitExpContext context, final boolean isStatement) {
		final PSHDLLangParser.PsArrayInitSubParensContext _psArrayInitSubParens = context.psArrayInitSubParens();
		return this.toHDL(_psArrayInitSubParens, false);
	}

	protected IHDLObject _toHDL(final PSHDLLangParser.PsArrayInitSubContext context, final boolean isStatement) {
		final List<PSHDLLangParser.PsExpressionContext> _psExpression = context.psExpression();
		final boolean _tripleNotEquals = (_psExpression != null);
		if (_tripleNotEquals) {
			final HDLArrayInit _hDLArrayInit = new HDLArrayInit();
			final List<PSHDLLangParser.PsExpressionContext> _psExpression_1 = context.psExpression();
			final Function1<PSHDLLangParser.PsExpressionContext, HDLExpression> _function = new Function1<PSHDLLangParser.PsExpressionContext, HDLExpression>() {
				@Override
				public HDLExpression apply(final PSHDLLangParser.PsExpressionContext it) {
					final IHDLObject _hDL = ParserToModelExtension.this.toHDL(it, isStatement);
					return ((HDLExpression) _hDL);
				}
			};
			final List<HDLExpression> _map = ListExtensions.<PSHDLLangParser.PsExpressionContext, HDLExpression> map(_psExpression_1, _function);
			final HDLArrayInit arr = _hDLArrayInit.setExp(_map);
			return this.<HDLArrayInit> attachContext(arr, context);
		}
		final PSHDLLangParser.PsArrayInitSubParensContext _psArrayInitSubParens = context.psArrayInitSubParens();
		return this.toHDL(_psArrayInitSubParens, false);
	}

	protected IHDLObject _toHDL(final PSHDLLangParser.PsArrayInitSubParensContext context, final boolean isStatement) {
		final PSHDLLangParser.PsArrayInitSubContext _psArrayInitSub = context.psArrayInitSub();
		final IHDLObject _hDL = this.toHDL(_psArrayInitSub, false);
		return this.<IHDLObject> attachContext(_hDL, context);
	}

	protected HDLType _toHDL(final PSHDLLangParser.PsPrimitiveContext context, final boolean isStatement) {
		final PSHDLLangParser.PsQualifiedNameContext _psQualifiedName = context.psQualifiedName();
		final boolean _tripleNotEquals = (_psQualifiedName != null);
		if (_tripleNotEquals) {
			final HDLEnum _hDLEnum = new HDLEnum();
			final PSHDLLangParser.PsQualifiedNameContext _psQualifiedName_1 = context.psQualifiedName();
			final String _name = this.toName(_psQualifiedName_1);
			final HDLEnum _setName = _hDLEnum.setName(_name);
			return this.<HDLEnum> attachContext(_setName, context);
		}
		final PSHDLLangParser.PsPrimitiveTypeContext _psPrimitiveType = context.psPrimitiveType();
		final String _text = _psPrimitiveType.getText();
		final String _upperCase = _text.toUpperCase();
		final HDLPrimitive.HDLPrimitiveType pt = HDLPrimitive.HDLPrimitiveType.valueOf(_upperCase);
		final PSHDLLangParser.PsWidthContext _psWidth = context.psWidth();
		IHDLObject _hDL = null;
		if (_psWidth != null) {
			_hDL = this.toHDL(_psWidth, false);
		}
		final HDLExpression width = ((HDLExpression) _hDL);
		final HDLPrimitive _hDLPrimitive = new HDLPrimitive();
		final HDLPrimitive.HDLPrimitiveType _resultingType = this.getResultingType(pt, width);
		final HDLPrimitive _setType = _hDLPrimitive.setType(_resultingType);
		final HDLPrimitive _setWidth = _setType.setWidth(width);
		return this.<HDLPrimitive> attachContext(_setWidth, context);
	}

	protected HDLVariable _toHDL(final PSHDLLangParser.PsDeclAssignmentContext context, final boolean isStatement) {
		final HDLVariable _hDLVariable = new HDLVariable();
		final PSHDLLangParser.PsVariableContext _psVariable = context.psVariable();
		final String _name = this.toName(_psVariable);
		HDLVariable res = _hDLVariable.setName(_name);
		final List<PSHDLLangParser.PsAnnotationContext> _psAnnotation = context.psAnnotation();
		final Function1<PSHDLLangParser.PsAnnotationContext, HDLAnnotation> _function = new Function1<PSHDLLangParser.PsAnnotationContext, HDLAnnotation>() {
			@Override
			public HDLAnnotation apply(final PSHDLLangParser.PsAnnotationContext it) {
				final IHDLObject _hDL = ParserToModelExtension.this.toHDL(it, false);
				return ((HDLAnnotation) _hDL);
			}
		};
		final List<HDLAnnotation> _map = ListExtensions.<PSHDLLangParser.PsAnnotationContext, HDLAnnotation> map(_psAnnotation, _function);
		final HDLVariable _setAnnotations = res.setAnnotations(_map);
		res = _setAnnotations;
		final PSHDLLangParser.PsArrayContext _psArray = context.psArray();
		final boolean _tripleNotEquals = (_psArray != null);
		if (_tripleNotEquals) {
			final PSHDLLangParser.PsArrayContext _psArray_1 = context.psArray();
			final List<PSHDLLangParser.PsExpressionContext> _psExpression = _psArray_1.psExpression();
			final Function1<PSHDLLangParser.PsExpressionContext, HDLExpression> _function_1 = new Function1<PSHDLLangParser.PsExpressionContext, HDLExpression>() {
				@Override
				public HDLExpression apply(final PSHDLLangParser.PsExpressionContext it) {
					final IHDLObject _hDL = ParserToModelExtension.this.toHDL(it, false);
					return ((HDLExpression) _hDL);
				}
			};
			final List<HDLExpression> _map_1 = ListExtensions.<PSHDLLangParser.PsExpressionContext, HDLExpression> map(_psExpression, _function_1);
			final HDLVariable _setDimensions = res.setDimensions(_map_1);
			res = _setDimensions;
		}
		final PSHDLLangParser.PsArrayInitContext _psArrayInit = context.psArrayInit();
		final boolean _tripleNotEquals_1 = (_psArrayInit != null);
		if (_tripleNotEquals_1) {
			final PSHDLLangParser.PsArrayInitContext _psArrayInit_1 = context.psArrayInit();
			final IHDLObject _hDL = this.toHDL(_psArrayInit_1, false);
			final HDLVariable _setDefaultValue = res.setDefaultValue(((HDLExpression) _hDL));
			res = _setDefaultValue;
		}
		return this.<HDLVariable> attachContext(res, context);
	}

	public String toName(final PSHDLLangParser.PsVariableContext context) {
		final TerminalNode _RULE_ID = context.RULE_ID();
		return _RULE_ID.getText();
	}

	public HDLPrimitive.HDLPrimitiveType getResultingType(final HDLPrimitive.HDLPrimitiveType pt, final HDLExpression width) {
		final boolean _tripleNotEquals = (width != null);
		if (_tripleNotEquals) {
			if (pt != null) {
				switch (pt) {
				case BIT:
					return HDLPrimitive.HDLPrimitiveType.BITVECTOR;
				default:
					break;
				}
			}
		} else {
			if (pt != null) {
				switch (pt) {
				case INT:
					return HDLPrimitive.HDLPrimitiveType.INTEGER;
				case UINT:
					return HDLPrimitive.HDLPrimitiveType.NATURAL;
				default:
					break;
				}
			}
		}
		return pt;
	}

	protected IHDLObject _toHDL(final PSHDLLangParser.PsWidthContext context, final boolean isStatement) {
		final PSHDLLangParser.PsExpressionContext _psExpression = context.psExpression();
		final IHDLObject _hDL = this.toHDL(_psExpression, false);
		return this.<IHDLObject> attachContext(_hDL, context);
	}

	protected IHDLObject _toHDL(final PSHDLLangParser.PsValueContext context, final boolean isStatement) {
		final TerminalNode _RULE_PS_LITERAL_TERMINAL = context.RULE_PS_LITERAL_TERMINAL();
		final boolean _tripleNotEquals = (_RULE_PS_LITERAL_TERMINAL != null);
		if (_tripleNotEquals) {
			final HDLLiteral _hDLLiteral = new HDLLiteral();
			final HDLLiteral _setStr = _hDLLiteral.setStr(false);
			final TerminalNode _RULE_PS_LITERAL_TERMINAL_1 = context.RULE_PS_LITERAL_TERMINAL();
			final String _text = _RULE_PS_LITERAL_TERMINAL_1.getText();
			final HDLLiteral _setVal = _setStr.setVal(_text);
			return this.<HDLLiteral> attachContext(_setVal, context);
		}
		final TerminalNode _RULE_STRING = context.RULE_STRING();
		final boolean _tripleNotEquals_1 = (_RULE_STRING != null);
		if (_tripleNotEquals_1) {
			final TerminalNode _RULE_STRING_1 = context.RULE_STRING();
			String str = _RULE_STRING_1.getText();
			final int _length = str.length();
			final int _minus = (_length - 1);
			final String _substring = str.substring(1, _minus);
			str = _substring;
			final HDLLiteral _hDLLiteral_1 = new HDLLiteral();
			final HDLLiteral _setStr_1 = _hDLLiteral_1.setStr(true);
			final HDLLiteral _setVal_1 = _setStr_1.setVal(str);
			return this.<HDLLiteral> attachContext(_setVal_1, context);
		}
		final PSHDLLangParser.PsVariableRefContext _psVariableRef = context.psVariableRef();
		final boolean _tripleNotEquals_2 = (_psVariableRef != null);
		if (_tripleNotEquals_2) {
			final PSHDLLangParser.PsVariableRefContext _psVariableRef_1 = context.psVariableRef();
			final IHDLObject _hDL = this.toHDL(_psVariableRef_1, false);
			return this.<IHDLObject> attachContext(_hDL, context);
		}
		final Class<? extends PSHDLLangParser.PsValueContext> _class = context.getClass();
		final String _plus = ("Not correctly implemented:" + _class);
		throw new IllegalArgumentException(_plus);
	}

	protected IHDLObject _toHDL(final PSHDLLangParser.PsValueExpContext context, final boolean isStatement) {
		final PSHDLLangParser.PsValueContext _psValue = context.psValue();
		final IHDLObject _hDL = this.toHDL(_psValue, false);
		return this.<IHDLObject> attachContext(_hDL, context);
	}

	protected HDLConcat _toHDL(final PSHDLLangParser.PsConcatContext context, final boolean isStatement) {
		HDLConcat cat = new HDLConcat();
		final List<PSHDLLangParser.PsExpressionContext> _psExpression = context.psExpression();
		final Function1<PSHDLLangParser.PsExpressionContext, HDLExpression> _function = new Function1<PSHDLLangParser.PsExpressionContext, HDLExpression>() {
			@Override
			public HDLExpression apply(final PSHDLLangParser.PsExpressionContext it) {
				final IHDLObject _hDL = ParserToModelExtension.this.toHDL(it, false);
				return ((HDLExpression) _hDL);
			}
		};
		final List<HDLExpression> _map = ListExtensions.<PSHDLLangParser.PsExpressionContext, HDLExpression> map(_psExpression, _function);
		final HDLConcat _setCats = cat.setCats(_map);
		cat = _setCats;
		return this.<HDLConcat> attachContext(cat, context);
	}

	protected HDLBitOp _toHDL(final PSHDLLangParser.PsBitLogOrContext context, final boolean isStatement) {
		final HDLBitOp _hDLBitOp = new HDLBitOp();
		HDLBitOp res = _hDLBitOp.setType(HDLBitOp.HDLBitOpType.LOGI_OR);
		final PSHDLLangParser.PsExpressionContext _psExpression = context.psExpression(0);
		final IHDLObject _hDL = this.toHDL(_psExpression, false);
		final HDLBitOp _setLeft = res.setLeft(((HDLExpression) _hDL));
		res = _setLeft;
		final PSHDLLangParser.PsExpressionContext _psExpression_1 = context.psExpression(1);
		final IHDLObject _hDL_1 = this.toHDL(_psExpression_1, false);
		final HDLBitOp _setRight = res.setRight(((HDLExpression) _hDL_1));
		res = _setRight;
		return this.<HDLBitOp> attachContext(res, context);
	}

	protected HDLBitOp _toHDL(final PSHDLLangParser.PsBitLogAndContext context, final boolean isStatement) {
		final HDLBitOp _hDLBitOp = new HDLBitOp();
		HDLBitOp res = _hDLBitOp.setType(HDLBitOp.HDLBitOpType.LOGI_AND);
		final PSHDLLangParser.PsExpressionContext _psExpression = context.psExpression(0);
		final IHDLObject _hDL = this.toHDL(_psExpression, false);
		final HDLBitOp _setLeft = res.setLeft(((HDLExpression) _hDL));
		res = _setLeft;
		final PSHDLLangParser.PsExpressionContext _psExpression_1 = context.psExpression(1);
		final IHDLObject _hDL_1 = this.toHDL(_psExpression_1, false);
		final HDLBitOp _setRight = res.setRight(((HDLExpression) _hDL_1));
		res = _setRight;
		return this.<HDLBitOp> attachContext(res, context);
	}

	protected HDLBitOp _toHDL(final PSHDLLangParser.PsBitXorContext context, final boolean isStatement) {
		final HDLBitOp _hDLBitOp = new HDLBitOp();
		HDLBitOp res = _hDLBitOp.setType(HDLBitOp.HDLBitOpType.XOR);
		final PSHDLLangParser.PsExpressionContext _psExpression = context.psExpression(0);
		final IHDLObject _hDL = this.toHDL(_psExpression, false);
		final HDLBitOp _setLeft = res.setLeft(((HDLExpression) _hDL));
		res = _setLeft;
		final PSHDLLangParser.PsExpressionContext _psExpression_1 = context.psExpression(1);
		final IHDLObject _hDL_1 = this.toHDL(_psExpression_1, false);
		final HDLBitOp _setRight = res.setRight(((HDLExpression) _hDL_1));
		res = _setRight;
		return this.<HDLBitOp> attachContext(res, context);
	}

	protected HDLBitOp _toHDL(final PSHDLLangParser.PsBitOrContext context, final boolean isStatement) {
		final HDLBitOp _hDLBitOp = new HDLBitOp();
		HDLBitOp res = _hDLBitOp.setType(HDLBitOp.HDLBitOpType.OR);
		final PSHDLLangParser.PsExpressionContext _psExpression = context.psExpression(0);
		final IHDLObject _hDL = this.toHDL(_psExpression, false);
		final HDLBitOp _setLeft = res.setLeft(((HDLExpression) _hDL));
		res = _setLeft;
		final PSHDLLangParser.PsExpressionContext _psExpression_1 = context.psExpression(1);
		final IHDLObject _hDL_1 = this.toHDL(_psExpression_1, false);
		final HDLBitOp _setRight = res.setRight(((HDLExpression) _hDL_1));
		res = _setRight;
		return this.<HDLBitOp> attachContext(res, context);
	}

	protected HDLBitOp _toHDL(final PSHDLLangParser.PsBitAndContext context, final boolean isStatement) {
		final HDLBitOp _hDLBitOp = new HDLBitOp();
		HDLBitOp res = _hDLBitOp.setType(HDLBitOp.HDLBitOpType.AND);
		final PSHDLLangParser.PsExpressionContext _psExpression = context.psExpression(0);
		final IHDLObject _hDL = this.toHDL(_psExpression, false);
		final HDLBitOp _setLeft = res.setLeft(((HDLExpression) _hDL));
		res = _setLeft;
		final PSHDLLangParser.PsExpressionContext _psExpression_1 = context.psExpression(1);
		final IHDLObject _hDL_1 = this.toHDL(_psExpression_1, false);
		final HDLBitOp _setRight = res.setRight(((HDLExpression) _hDL_1));
		res = _setRight;
		return this.<HDLBitOp> attachContext(res, context);
	}

	protected HDLShiftOp _toHDL(final PSHDLLangParser.PsShiftContext context, final boolean isStatement) {
		final String _text = context.op.getText();
		final HDLShiftOp.HDLShiftOpType type = HDLShiftOp.HDLShiftOpType.getOp(_text);
		final HDLShiftOp _hDLShiftOp = new HDLShiftOp();
		HDLShiftOp res = _hDLShiftOp.setType(type);
		final PSHDLLangParser.PsExpressionContext _psExpression = context.psExpression(0);
		final IHDLObject _hDL = this.toHDL(_psExpression, false);
		final HDLShiftOp _setLeft = res.setLeft(((HDLExpression) _hDL));
		res = _setLeft;
		final PSHDLLangParser.PsExpressionContext _psExpression_1 = context.psExpression(1);
		final IHDLObject _hDL_1 = this.toHDL(_psExpression_1, false);
		final HDLShiftOp _setRight = res.setRight(((HDLExpression) _hDL_1));
		res = _setRight;
		return this.<HDLShiftOp> attachContext(res, context);
	}

	protected HDLEqualityOp _toHDL(final PSHDLLangParser.PsEqualityCompContext context, final boolean isStatement) {
		final String _text = context.op.getText();
		final HDLEqualityOp.HDLEqualityOpType type = HDLEqualityOp.HDLEqualityOpType.getOp(_text);
		final HDLEqualityOp _hDLEqualityOp = new HDLEqualityOp();
		HDLEqualityOp res = _hDLEqualityOp.setType(type);
		final PSHDLLangParser.PsExpressionContext _psExpression = context.psExpression(0);
		final IHDLObject _hDL = this.toHDL(_psExpression, false);
		final HDLEqualityOp _setLeft = res.setLeft(((HDLExpression) _hDL));
		res = _setLeft;
		final PSHDLLangParser.PsExpressionContext _psExpression_1 = context.psExpression(1);
		final IHDLObject _hDL_1 = this.toHDL(_psExpression_1, false);
		final HDLEqualityOp _setRight = res.setRight(((HDLExpression) _hDL_1));
		res = _setRight;
		return this.<HDLEqualityOp> attachContext(res, context);
	}

	protected HDLEqualityOp _toHDL(final PSHDLLangParser.PsEqualityContext context, final boolean isStatement) {
		final String _text = context.op.getText();
		final HDLEqualityOp.HDLEqualityOpType type = HDLEqualityOp.HDLEqualityOpType.getOp(_text);
		final HDLEqualityOp _hDLEqualityOp = new HDLEqualityOp();
		HDLEqualityOp res = _hDLEqualityOp.setType(type);
		final PSHDLLangParser.PsExpressionContext _psExpression = context.psExpression(0);
		final IHDLObject _hDL = this.toHDL(_psExpression, false);
		final HDLEqualityOp _setLeft = res.setLeft(((HDLExpression) _hDL));
		res = _setLeft;
		final PSHDLLangParser.PsExpressionContext _psExpression_1 = context.psExpression(1);
		final IHDLObject _hDL_1 = this.toHDL(_psExpression_1, false);
		final HDLEqualityOp _setRight = res.setRight(((HDLExpression) _hDL_1));
		res = _setRight;
		return this.<HDLEqualityOp> attachContext(res, context);
	}

	protected HDLArithOp _toHDL(final PSHDLLangParser.PsMulContext context, final boolean isStatement) {
		final String _text = context.op.getText();
		final HDLArithOp.HDLArithOpType type = HDLArithOp.HDLArithOpType.getOp(_text);
		final HDLArithOp _hDLArithOp = new HDLArithOp();
		HDLArithOp res = _hDLArithOp.setType(type);
		final PSHDLLangParser.PsExpressionContext _psExpression = context.psExpression(0);
		final IHDLObject _hDL = this.toHDL(_psExpression, false);
		final HDLArithOp _setLeft = res.setLeft(((HDLExpression) _hDL));
		res = _setLeft;
		final PSHDLLangParser.PsExpressionContext _psExpression_1 = context.psExpression(1);
		final IHDLObject _hDL_1 = this.toHDL(_psExpression_1, false);
		final HDLArithOp _setRight = res.setRight(((HDLExpression) _hDL_1));
		res = _setRight;
		return this.<HDLArithOp> attachContext(res, context);
	}

	protected HDLArithOp _toHDL(final PSHDLLangParser.PsAddContext context, final boolean isStatement) {
		final String _text = context.op.getText();
		final HDLArithOp.HDLArithOpType type = HDLArithOp.HDLArithOpType.getOp(_text);
		final HDLArithOp _hDLArithOp = new HDLArithOp();
		HDLArithOp res = _hDLArithOp.setType(type);
		final PSHDLLangParser.PsExpressionContext _psExpression = context.psExpression(0);
		final IHDLObject _hDL = this.toHDL(_psExpression, false);
		final HDLArithOp _setLeft = res.setLeft(((HDLExpression) _hDL));
		res = _setLeft;
		final PSHDLLangParser.PsExpressionContext _psExpression_1 = context.psExpression(1);
		final IHDLObject _hDL_1 = this.toHDL(_psExpression_1, false);
		final HDLArithOp _setRight = res.setRight(((HDLExpression) _hDL_1));
		res = _setRight;
		return this.<HDLArithOp> attachContext(res, context);
	}

	protected HDLPrimitive _toHDL(final PSHDLLangParser.PsCastContext context, final boolean isStatement) {
		final PSHDLLangParser.PsPrimitiveTypeContext _psPrimitiveType = context.psPrimitiveType();
		final String _text = _psPrimitiveType.getText();
		final String _upperCase = _text.toUpperCase();
		final HDLPrimitive.HDLPrimitiveType pt = HDLPrimitive.HDLPrimitiveType.valueOf(_upperCase);
		final PSHDLLangParser.PsWidthContext _psWidth = context.psWidth();
		IHDLObject _hDL = null;
		if (_psWidth != null) {
			_hDL = this.toHDL(_psWidth, false);
		}
		final HDLExpression width = ((HDLExpression) _hDL);
		final HDLPrimitive _hDLPrimitive = new HDLPrimitive();
		final HDLPrimitive.HDLPrimitiveType _resultingType = this.getResultingType(pt, width);
		final HDLPrimitive _setType = _hDLPrimitive.setType(_resultingType);
		final HDLPrimitive _setWidth = _setType.setWidth(width);
		return this.<HDLPrimitive> attachContext(_setWidth, context);
	}

	protected HDLManip _toHDL(final PSHDLLangParser.PsManipContext context, final boolean isStatement) {
		final HDLManip _hDLManip = new HDLManip();
		final PSHDLLangParser.PsExpressionContext _psExpression = context.psExpression();
		final IHDLObject _hDL = this.toHDL(_psExpression, false);
		HDLManip res = _hDLManip.setTarget(((HDLExpression) _hDL));
		final PSHDLLangParser.PsCastContext _psCast = context.psCast();
		final boolean _tripleNotEquals = (_psCast != null);
		if (_tripleNotEquals) {
			final HDLManip _setType = res.setType(HDLManip.HDLManipType.CAST);
			res = _setType;
			final PSHDLLangParser.PsCastContext _psCast_1 = context.psCast();
			final IHDLObject _hDL_1 = this.toHDL(_psCast_1, false);
			final HDLManip _setCastTo = res.setCastTo(((HDLType) _hDL_1));
			res = _setCastTo;
		} else {
			final int _type = context.type.getType();
			switch (_type) {
			case PSHDLLangLexer.LOGIC_NEG:
				final HDLManip _setType_1 = res.setType(HDLManip.HDLManipType.LOGIC_NEG);
				res = _setType_1;
				break;
			case PSHDLLangLexer.ARITH_NEG:
				final HDLManip _setType_2 = res.setType(HDLManip.HDLManipType.ARITH_NEG);
				res = _setType_2;
				break;
			case PSHDLLangLexer.BIT_NEG:
				final HDLManip _setType_3 = res.setType(HDLManip.HDLManipType.BIT_NEG);
				res = _setType_3;
				break;
			}
		}
		return this.<HDLManip> attachContext(res, context);
	}

	protected HDLTernary _toHDL(final PSHDLLangParser.PsTernaryContext context, final boolean isStatement) {
		final HDLTernary _hDLTernary = new HDLTernary();
		final PSHDLLangParser.PsExpressionContext _psExpression = context.psExpression(0);
		final IHDLObject _hDL = this.toHDL(_psExpression, isStatement);
		HDLTernary res = _hDLTernary.setIfExpr(((HDLExpression) _hDL));
		final PSHDLLangParser.PsExpressionContext _psExpression_1 = context.psExpression(1);
		final IHDLObject _hDL_1 = this.toHDL(_psExpression_1, false);
		final HDLTernary _setThenExpr = res.setThenExpr(((HDLExpression) _hDL_1));
		res = _setThenExpr;
		final PSHDLLangParser.PsExpressionContext _psExpression_2 = context.psExpression(2);
		final IHDLObject _hDL_2 = this.toHDL(_psExpression_2, false);
		final HDLTernary _setElseExpr = res.setElseExpr(((HDLExpression) _hDL_2));
		res = _setElseExpr;
		return this.<HDLTernary> attachContext(res, context);
	}

	protected IHDLObject _toHDL(final PSHDLLangParser.PsParensContext context, final boolean isStatement) {
		final PSHDLLangParser.PsExpressionContext _psExpression = context.psExpression();
		final IHDLObject _hDL = this.toHDL(_psExpression, false);
		return this.<IHDLObject> attachContext(_hDL, context);
	}

	protected HDLExpression _toHDL(final PSHDLLangParser.PsExpressionContext context, final boolean isStatement) {
		final Class<? extends PSHDLLangParser.PsExpressionContext> _class = context.getClass();
		final String _plus = ("Not implemented:" + _class);
		throw new IllegalArgumentException(_plus);
	}

	public String toName(final PSHDLLangParser.PsEnumContext context) {
		final PSHDLLangParser.PsQualifiedNameContext _psQualifiedName = context.psQualifiedName();
		return this.toName(_psQualifiedName);
	}

	public String toName(final PSHDLLangParser.PsInterfaceContext context) {
		final PSHDLLangParser.PsQualifiedNameContext _psQualifiedName = context.psQualifiedName();
		return this.toName(_psQualifiedName);
	}

	public HDLQualifiedName toFQNName(final PSHDLLangParser.PsQualifiedNameContext context) {
		final String _text = context.getText();
		return new HDLQualifiedName(_text);
	}

	public String toName(final PSHDLLangParser.PsQualifiedNameContext context) {
		final String _text = context.getText();
		final HDLQualifiedName _hDLQualifiedName = new HDLQualifiedName(_text);
		return _hDLQualifiedName.toString();
	}

	protected IHDLObject _toHDL(final PSHDLLangParser.PsTypeDeclarationContext context, final boolean isStatement) {
		final PSHDLLangParser.PsEnumDeclarationContext _psEnumDeclaration = context.psEnumDeclaration();
		final boolean _tripleNotEquals = (_psEnumDeclaration != null);
		if (_tripleNotEquals) {
			final PSHDLLangParser.PsEnumDeclarationContext _psEnumDeclaration_1 = context.psEnumDeclaration();
			final IHDLObject _hDL = this.toHDL(_psEnumDeclaration_1, true);
			return this.<IHDLObject> attachContext(_hDL, context);
		}
		final PSHDLLangParser.PsInterfaceDeclarationContext _psInterfaceDeclaration = context.psInterfaceDeclaration();
		final boolean _tripleNotEquals_1 = (_psInterfaceDeclaration != null);
		if (_tripleNotEquals_1) {
			final PSHDLLangParser.PsInterfaceDeclarationContext _psInterfaceDeclaration_1 = context.psInterfaceDeclaration();
			final IHDLObject _hDL_1 = this.toHDL(_psInterfaceDeclaration_1, true);
			return this.<IHDLObject> attachContext(_hDL_1, context);
		}
		final Class<? extends PSHDLLangParser.PsTypeDeclarationContext> _class = context.getClass();
		final String _plus = ("Not implemented:" + _class);
		throw new IllegalArgumentException(_plus);
	}

	protected HDLInterfaceDeclaration _toHDL(final PSHDLLangParser.PsInterfaceDeclarationContext context, final boolean isStatement) {
		final HDLInterface _hDLInterface = new HDLInterface();
		final PSHDLLangParser.PsInterfaceContext _psInterface = context.psInterface();
		final String _name = this.toName(_psInterface);
		HDLInterface hIf = _hDLInterface.setName(_name);
		final PSHDLLangParser.PsInterfaceDeclContext _psInterfaceDecl = context.psInterfaceDecl();
		final List<PSHDLLangParser.PsPortDeclarationContext> _psPortDeclaration = _psInterfaceDecl.psPortDeclaration();
		final Function1<PSHDLLangParser.PsPortDeclarationContext, HDLVariableDeclaration> _function = new Function1<PSHDLLangParser.PsPortDeclarationContext, HDLVariableDeclaration>() {
			@Override
			public HDLVariableDeclaration apply(final PSHDLLangParser.PsPortDeclarationContext it) {
				final IHDLObject _hDL = ParserToModelExtension.this.toHDL(it, true);
				return ((HDLVariableDeclaration) _hDL);
			}
		};
		final List<HDLVariableDeclaration> _map = ListExtensions.<PSHDLLangParser.PsPortDeclarationContext, HDLVariableDeclaration> map(_psPortDeclaration, _function);
		final HDLInterface _setPorts = hIf.setPorts(_map);
		hIf = _setPorts;
		final HDLInterfaceDeclaration _hDLInterfaceDeclaration = new HDLInterfaceDeclaration();
		final HDLInterfaceDeclaration _setHIf = _hDLInterfaceDeclaration.setHIf(hIf);
		return this.<HDLInterfaceDeclaration> attachContext(_setHIf, context);
	}

	protected HDLVariableDeclaration _toHDL(final PSHDLLangParser.PsPortDeclarationContext context, final boolean isStatement) {
		final PSHDLLangParser.PsVariableDeclarationContext _psVariableDeclaration = context.psVariableDeclaration();
		final IHDLObject _hDL = this.toHDL(_psVariableDeclaration, true);
		HDLVariableDeclaration res = ((HDLVariableDeclaration) _hDL);
		final List<PSHDLLangParser.PsAnnotationContext> _psAnnotation = context.psAnnotation();
		final Function1<PSHDLLangParser.PsAnnotationContext, HDLAnnotation> _function = new Function1<PSHDLLangParser.PsAnnotationContext, HDLAnnotation>() {
			@Override
			public HDLAnnotation apply(final PSHDLLangParser.PsAnnotationContext it) {
				final IHDLObject _hDL = ParserToModelExtension.this.toHDL(it, true);
				return ((HDLAnnotation) _hDL);
			}
		};
		final List<HDLAnnotation> _map = ListExtensions.<PSHDLLangParser.PsAnnotationContext, HDLAnnotation> map(_psAnnotation, _function);
		final HDLVariableDeclaration _setAnnotations = res.setAnnotations(_map);
		res = _setAnnotations;
		return this.<HDLVariableDeclaration> attachContext(res, context);
	}

	protected IHDLObject _toHDL(final PSHDLLangParser.PsBlockContext context, final boolean isStatement) {
		final PSHDLLangParser.PsDeclarationContext _psDeclaration = context.psDeclaration();
		final boolean _tripleNotEquals = (_psDeclaration != null);
		if (_tripleNotEquals) {
			final PSHDLLangParser.PsDeclarationContext _psDeclaration_1 = context.psDeclaration();
			final IHDLObject _hDL = this.toHDL(_psDeclaration_1, true);
			return this.<IHDLObject> attachContext(_hDL, context);
		}
		final PSHDLLangParser.PsInstantiationContext _psInstantiation = context.psInstantiation();
		final boolean _tripleNotEquals_1 = (_psInstantiation != null);
		if (_tripleNotEquals_1) {
			final PSHDLLangParser.PsInstantiationContext _psInstantiation_1 = context.psInstantiation();
			final IHDLObject _hDL_1 = this.toHDL(_psInstantiation_1, true);
			return this.<IHDLObject> attachContext(_hDL_1, context);
		}
		final PSHDLLangParser.PsStatementContext _psStatement = context.psStatement();
		final boolean _tripleNotEquals_2 = (_psStatement != null);
		if (_tripleNotEquals_2) {
			final PSHDLLangParser.PsStatementContext _psStatement_1 = context.psStatement();
			final IHDLObject _hDL_2 = this.toHDL(_psStatement_1, true);
			return this.<IHDLObject> attachContext(_hDL_2, context);
		}
		final List<PSHDLLangParser.PsBlockContext> _psBlock = context.psBlock();
		final boolean _tripleNotEquals_3 = (_psBlock != null);
		if (_tripleNotEquals_3) {
			final HDLBlock _hDLBlock = new HDLBlock();
			final HDLBlock _setProcess = _hDLBlock.setProcess(false);
			final List<PSHDLLangParser.PsBlockContext> _psBlock_1 = context.psBlock();
			final Function1<PSHDLLangParser.PsBlockContext, HDLStatement> _function = new Function1<PSHDLLangParser.PsBlockContext, HDLStatement>() {
				@Override
				public HDLStatement apply(final PSHDLLangParser.PsBlockContext it) {
					final IHDLObject _hDL = ParserToModelExtension.this.toHDL(it, true);
					return ((HDLStatement) _hDL);
				}
			};
			final List<HDLStatement> _map = ListExtensions.<PSHDLLangParser.PsBlockContext, HDLStatement> map(_psBlock_1, _function);
			return _setProcess.setStatements(_map);
		}
		final Class<? extends PSHDLLangParser.PsBlockContext> _class = context.getClass();
		final String _plus = ("Not correctly implemented type:" + _class);
		throw new IllegalArgumentException(_plus);
	}

	protected HDLDirectGeneration _toHDL(final PSHDLLangParser.PsDirectGenerationContext context, final boolean isStatement) {
		final HDLDirectGeneration _hDLDirectGeneration = new HDLDirectGeneration();
		HDLDirectGeneration gen = _hDLDirectGeneration.setGeneratorContent("");
		final boolean _tripleNotEquals = (context.isInclude != null);
		final HDLDirectGeneration _setInclude = gen.setInclude(_tripleNotEquals);
		gen = _setInclude;
		final PSHDLLangParser.PsInterfaceContext _psInterface = context.psInterface();
		final IHDLObject _hDL = this.toHDL(_psInterface, false);
		final HDLDirectGeneration _setHIf = gen.setHIf(((HDLInterface) _hDL));
		gen = _setHIf;
		final PSHDLLangParser.PsVariableContext _psVariable = context.psVariable();
		final IHDLObject _hDL_1 = this.toHDL(_psVariable, false);
		final HDLDirectGeneration _setVar = gen.setVar(((HDLVariable) _hDL_1));
		gen = _setVar;
		final TerminalNode _RULE_ID = context.RULE_ID();
		final String _text = _RULE_ID.getText();
		final HDLDirectGeneration _setGeneratorID = gen.setGeneratorID(_text);
		gen = _setGeneratorID;
		final PSHDLLangParser.PsPassedArgumentsContext _psPassedArguments = context.psPassedArguments();
		final boolean _tripleNotEquals_1 = (_psPassedArguments != null);
		if (_tripleNotEquals_1) {
			final PSHDLLangParser.PsPassedArgumentsContext _psPassedArguments_1 = context.psPassedArguments();
			final List<PSHDLLangParser.PsArgumentContext> _psArgument = _psPassedArguments_1.psArgument();
			final Function1<PSHDLLangParser.PsArgumentContext, HDLArgument> _function = new Function1<PSHDLLangParser.PsArgumentContext, HDLArgument>() {
				@Override
				public HDLArgument apply(final PSHDLLangParser.PsArgumentContext it) {
					final IHDLObject _hDL = ParserToModelExtension.this.toHDL(it, isStatement);
					return ((HDLArgument) _hDL);
				}
			};
			final List<HDLArgument> _map = ListExtensions.<PSHDLLangParser.PsArgumentContext, HDLArgument> map(_psArgument, _function);
			final HDLDirectGeneration _setArguments = gen.setArguments(_map);
			gen = _setArguments;
		}
		final TerminalNode _RULE_GENERATOR_CONTENT = context.RULE_GENERATOR_CONTENT();
		final boolean _tripleNotEquals_2 = (_RULE_GENERATOR_CONTENT != null);
		if (_tripleNotEquals_2) {
			final TerminalNode _RULE_GENERATOR_CONTENT_1 = context.RULE_GENERATOR_CONTENT();
			final String _text_1 = _RULE_GENERATOR_CONTENT_1.getText();
			final HDLDirectGeneration _setGeneratorContent = gen.setGeneratorContent(_text_1);
			gen = _setGeneratorContent;
		}
		return this.<HDLDirectGeneration> attachContext(gen, context);
	}

	protected HDLVariable _toHDL(final PSHDLLangParser.PsVariableContext context, final boolean isStatement) {
		final HDLVariable _hDLVariable = new HDLVariable();
		final String _name = this.toName(context);
		final HDLVariable _setName = _hDLVariable.setName(_name);
		return this.<HDLVariable> attachContext(_setName, context);
	}

	protected HDLInterface _toHDL(final PSHDLLangParser.PsInterfaceContext context, final boolean isStatement) {
		final HDLInterface _hDLInterface = new HDLInterface();
		final String _name = this.toName(context);
		final HDLInterface _setName = _hDLInterface.setName(_name);
		return this.<HDLInterface> attachContext(_setName, context);
	}

	protected IHDLObject _toHDL(final PSHDLLangParser.PsInstantiationContext context, final boolean isStatement) {
		HDLInstantiation res = null;
		final PSHDLLangParser.PsDirectGenerationContext _psDirectGeneration = context.psDirectGeneration();
		final boolean _tripleNotEquals = (_psDirectGeneration != null);
		if (_tripleNotEquals) {
			final PSHDLLangParser.PsDirectGenerationContext _psDirectGeneration_1 = context.psDirectGeneration();
			final IHDLObject _hDL = this.toHDL(_psDirectGeneration_1, true);
			res = ((HDLInstantiation) _hDL);
		}
		final PSHDLLangParser.PsInterfaceInstantiationContext _psInterfaceInstantiation = context.psInterfaceInstantiation();
		final boolean _tripleNotEquals_1 = (_psInterfaceInstantiation != null);
		if (_tripleNotEquals_1) {
			final PSHDLLangParser.PsInterfaceInstantiationContext _psInterfaceInstantiation_1 = context.psInterfaceInstantiation();
			final IHDLObject _hDL_1 = this.toHDL(_psInterfaceInstantiation_1, true);
			res = ((HDLInstantiation) _hDL_1);
		}
		final boolean _tripleNotEquals_2 = (res != null);
		if (_tripleNotEquals_2) {
			final List<PSHDLLangParser.PsAnnotationContext> _psAnnotation = context.psAnnotation();
			final Function1<PSHDLLangParser.PsAnnotationContext, HDLAnnotation> _function = new Function1<PSHDLLangParser.PsAnnotationContext, HDLAnnotation>() {
				@Override
				public HDLAnnotation apply(final PSHDLLangParser.PsAnnotationContext it) {
					final IHDLObject _hDL = ParserToModelExtension.this.toHDL(it, false);
					return ((HDLAnnotation) _hDL);
				}
			};
			final List<HDLAnnotation> _map = ListExtensions.<PSHDLLangParser.PsAnnotationContext, HDLAnnotation> map(_psAnnotation, _function);
			final HDLInstantiation _setAnnotations = res.setAnnotations(_map);
			res = _setAnnotations;
			return this.<HDLInstantiation> attachContext(res, context);
		}
		final Class<? extends PSHDLLangParser.PsInstantiationContext> _class = context.getClass();
		final String _plus = ("Not implemented type:" + _class);
		throw new IllegalArgumentException(_plus);
	}

	protected HDLEnum _toHDL(final PSHDLLangParser.PsEnumContext context, final boolean isStatement) {
		final HDLEnum _hDLEnum = new HDLEnum();
		final String _name = this.toName(context);
		final HDLEnum _setName = _hDLEnum.setName(_name);
		return this.<HDLEnum> attachContext(_setName, context);
	}

	protected HDLEnumDeclaration _toHDL(final PSHDLLangParser.PsEnumDeclarationContext context, final boolean isStatement) {
		final PSHDLLangParser.PsEnumContext _psEnum = context.psEnum();
		final IHDLObject _hDL = this.toHDL(_psEnum, false);
		HDLEnum he = ((HDLEnum) _hDL);
		final List<PSHDLLangParser.PsVariableContext> _psVariable = context.psVariable();
		final Function1<PSHDLLangParser.PsVariableContext, HDLVariable> _function = new Function1<PSHDLLangParser.PsVariableContext, HDLVariable>() {
			@Override
			public HDLVariable apply(final PSHDLLangParser.PsVariableContext it) {
				final IHDLObject _hDL = ParserToModelExtension.this.toHDL(it, false);
				return ((HDLVariable) _hDL);
			}
		};
		final List<HDLVariable> _map = ListExtensions.<PSHDLLangParser.PsVariableContext, HDLVariable> map(_psVariable, _function);
		final HDLEnum _setEnums = he.setEnums(_map);
		he = _setEnums;
		final HDLEnumDeclaration _hDLEnumDeclaration = new HDLEnumDeclaration();
		final HDLEnumDeclaration _setHEnum = _hDLEnumDeclaration.setHEnum(he);
		return this.<HDLEnumDeclaration> attachContext(_setHEnum, context);
	}

	protected HDLSubstituteFunction _toHDL(final PSHDLLangParser.PsSubstituteFunctionContext context, final boolean isStatement) {
		HDLSubstituteFunction func = new HDLSubstituteFunction();
		final PSHDLLangParser.PsFunctionContext _psFunction = context.psFunction();
		final String _name = this.toName(_psFunction);
		final HDLSubstituteFunction _setName = func.setName(_name);
		func = _setName;
		final List<PSHDLLangParser.PsStatementContext> _psStatement = context.psStatement();
		final Function1<PSHDLLangParser.PsStatementContext, HDLStatement> _function = new Function1<PSHDLLangParser.PsStatementContext, HDLStatement>() {
			@Override
			public HDLStatement apply(final PSHDLLangParser.PsStatementContext it) {
				final IHDLObject _hDL = ParserToModelExtension.this.toHDL(it, true);
				return ((HDLStatement) _hDL);
			}
		};
		final List<HDLStatement> _map = ListExtensions.<PSHDLLangParser.PsStatementContext, HDLStatement> map(_psStatement, _function);
		final HDLSubstituteFunction _setStmnts = func.setStmnts(_map);
		func = _setStmnts;
		final PSHDLLangParser.PsFuncParamContext _psFuncParam = context.psFuncParam();
		final List<PSHDLLangParser.PsFuncSpecContext> _psFuncSpec = _psFuncParam.psFuncSpec();
		final Function1<PSHDLLangParser.PsFuncSpecContext, HDLFunctionParameter> _function_1 = new Function1<PSHDLLangParser.PsFuncSpecContext, HDLFunctionParameter>() {
			@Override
			public HDLFunctionParameter apply(final PSHDLLangParser.PsFuncSpecContext it) {
				final IHDLObject _hDL = ParserToModelExtension.this.toHDL(it, false);
				return ((HDLFunctionParameter) _hDL);
			}
		};
		final List<HDLFunctionParameter> _map_1 = ListExtensions.<PSHDLLangParser.PsFuncSpecContext, HDLFunctionParameter> map(_psFuncSpec, _function_1);
		final HDLSubstituteFunction _setArgs = func.setArgs(_map_1);
		func = _setArgs;
		final PSHDLLangParser.PsFuncRecturnTypeContext _psFuncRecturnType = context.psFuncRecturnType();
		final boolean _tripleNotEquals = (_psFuncRecturnType != null);
		if (_tripleNotEquals) {
			final PSHDLLangParser.PsFuncRecturnTypeContext _psFuncRecturnType_1 = context.psFuncRecturnType();
			final IHDLObject _hDL = this.toHDL(_psFuncRecturnType_1, false);
			final HDLSubstituteFunction _setReturnType = func.setReturnType(((HDLFunctionParameter) _hDL));
			func = _setReturnType;
		}
		return this.<HDLSubstituteFunction> attachContext(func, context);
	}

	protected HDLNativeFunction _toHDL(final PSHDLLangParser.PsNativeFunctionContext context, final boolean isStatement) {
		HDLNativeFunction func = new HDLNativeFunction();
		final PSHDLLangParser.PsFunctionContext _psFunction = context.psFunction();
		final String _name = this.toName(_psFunction);
		final HDLNativeFunction _setName = func.setName(_name);
		func = _setName;
		final boolean _tripleNotEquals = (context.isSim != null);
		final HDLNativeFunction _setSimOnly = func.setSimOnly(_tripleNotEquals);
		func = _setSimOnly;
		final PSHDLLangParser.PsFuncParamContext _psFuncParam = context.psFuncParam();
		final List<PSHDLLangParser.PsFuncSpecContext> _psFuncSpec = _psFuncParam.psFuncSpec();
		final Function1<PSHDLLangParser.PsFuncSpecContext, HDLFunctionParameter> _function = new Function1<PSHDLLangParser.PsFuncSpecContext, HDLFunctionParameter>() {
			@Override
			public HDLFunctionParameter apply(final PSHDLLangParser.PsFuncSpecContext it) {
				final IHDLObject _hDL = ParserToModelExtension.this.toHDL(it, false);
				return ((HDLFunctionParameter) _hDL);
			}
		};
		final List<HDLFunctionParameter> _map = ListExtensions.<PSHDLLangParser.PsFuncSpecContext, HDLFunctionParameter> map(_psFuncSpec, _function);
		final HDLNativeFunction _setArgs = func.setArgs(_map);
		func = _setArgs;
		final PSHDLLangParser.PsFuncRecturnTypeContext _psFuncRecturnType = context.psFuncRecturnType();
		final boolean _tripleNotEquals_1 = (_psFuncRecturnType != null);
		if (_tripleNotEquals_1) {
			final PSHDLLangParser.PsFuncRecturnTypeContext _psFuncRecturnType_1 = context.psFuncRecturnType();
			final IHDLObject _hDL = this.toHDL(_psFuncRecturnType_1, false);
			final HDLNativeFunction _setReturnType = func.setReturnType(((HDLFunctionParameter) _hDL));
			func = _setReturnType;
		}
		return this.<HDLNativeFunction> attachContext(func, context);
	}

	protected HDLFunctionParameter _toHDL(final PSHDLLangParser.PsFuncRecturnTypeContext context, final boolean isStatement) {
		final PSHDLLangParser.PsFuncParamTypeContext _psFuncParamType = context.psFuncParamType();
		final IHDLObject _hDL = this.toHDL(_psFuncParamType, isStatement);
		HDLFunctionParameter res = ((HDLFunctionParameter) _hDL);
		final HDLFunctionParameter _setRw = res.setRw(HDLFunctionParameter.RWType.RETURN);
		res = _setRw;
		final Function1<PSHDLLangParser.PsFuncOptArrayContext, HDLExpression> _function = new Function1<PSHDLLangParser.PsFuncOptArrayContext, HDLExpression>() {
			@Override
			public HDLExpression apply(final PSHDLLangParser.PsFuncOptArrayContext it) {
				HDLExpression _xifexpression = null;
				final PSHDLLangParser.PsExpressionContext _psExpression = it.psExpression();
				final boolean _tripleNotEquals = (_psExpression != null);
				if (_tripleNotEquals) {
					final PSHDLLangParser.PsExpressionContext _psExpression_1 = it.psExpression();
					final IHDLObject _hDL = ParserToModelExtension.this.toHDL(_psExpression_1, false);
					_xifexpression = ((HDLExpression) _hDL);
				} else {
					_xifexpression = HDLFunctionParameter.EMPTY_ARR();
				}
				return _xifexpression;
			}
		};
		final List<HDLExpression> _map = ListExtensions.<PSHDLLangParser.PsFuncOptArrayContext, HDLExpression> map(context.dims, _function);
		final HDLFunctionParameter _setDim = res.setDim(_map);
		res = _setDim;
		return res;
	}

	protected HDLFunctionParameter _toHDL(final PSHDLLangParser.PsFuncSpecContext context, final boolean isStatement) {
		final PSHDLLangParser.PsFuncParamWithRWContext _psFuncParamWithRW = context.psFuncParamWithRW();
		final IHDLObject _hDL = this.toHDL(_psFuncParamWithRW, false);
		HDLFunctionParameter res = ((HDLFunctionParameter) _hDL);
		final HDLVariable _hDLVariable = new HDLVariable();
		final TerminalNode _RULE_ID = context.RULE_ID();
		final String _text = _RULE_ID.getText();
		final HDLVariable _setName = _hDLVariable.setName(_text);
		final HDLFunctionParameter _setName_1 = res.setName(_setName);
		res = _setName_1;
		final Function1<PSHDLLangParser.PsFuncOptArrayContext, HDLExpression> _function = new Function1<PSHDLLangParser.PsFuncOptArrayContext, HDLExpression>() {
			@Override
			public HDLExpression apply(final PSHDLLangParser.PsFuncOptArrayContext it) {
				HDLExpression _xifexpression = null;
				final PSHDLLangParser.PsExpressionContext _psExpression = it.psExpression();
				final boolean _tripleNotEquals = (_psExpression != null);
				if (_tripleNotEquals) {
					final PSHDLLangParser.PsExpressionContext _psExpression_1 = it.psExpression();
					final IHDLObject _hDL = ParserToModelExtension.this.toHDL(_psExpression_1, false);
					_xifexpression = ((HDLExpression) _hDL);
				} else {
					_xifexpression = HDLFunctionParameter.EMPTY_ARR();
				}
				return _xifexpression;
			}
		};
		final List<HDLExpression> _map = ListExtensions.<PSHDLLangParser.PsFuncOptArrayContext, HDLExpression> map(context.dims, _function);
		final HDLFunctionParameter _setDim = res.setDim(_map);
		res = _setDim;
		return res;
	}

	protected HDLFunctionParameter _toHDL(final PSHDLLangParser.PsFuncParamWithRWContext context, final boolean isStatement) {
		final PSHDLLangParser.PsFuncParamTypeContext _psFuncParamType = context.psFuncParamType();
		final IHDLObject _hDL = this.toHDL(_psFuncParamType, isStatement);
		HDLFunctionParameter res = ((HDLFunctionParameter) _hDL);
		final PSHDLLangParser.PsFuncParamRWTypeContext _psFuncParamRWType = context.psFuncParamRWType();
		final boolean _tripleNotEquals = (_psFuncParamRWType != null);
		if (_tripleNotEquals) {
			final PSHDLLangParser.PsFuncParamRWTypeContext _psFuncParamRWType_1 = context.psFuncParamRWType();
			final String _text = _psFuncParamRWType_1.getText();
			final HDLFunctionParameter.RWType _op = HDLFunctionParameter.RWType.getOp(_text);
			final HDLFunctionParameter _setRw = res.setRw(_op);
			res = _setRw;
		} else {
			final HDLFunctionParameter _setRw_1 = res.setRw(HDLFunctionParameter.RWType.READ);
			res = _setRw_1;
		}
		final boolean _tripleNotEquals_1 = (context.constant != null);
		if (_tripleNotEquals_1) {
			final HDLFunctionParameter _setConstant = res.setConstant(true);
			res = _setConstant;
		}
		return res;
	}

	protected HDLFunctionParameter _toHDL(final PSHDLLangParser.PsFuncParamTypeContext context, final boolean isStatement) {
		final HDLFunctionParameter _hDLFunctionParameter = new HDLFunctionParameter();
		HDLFunctionParameter res = _hDLFunctionParameter.setConstant(false);
		final PSHDLLangParser.PsFuncParamTypeContext x = context;
		boolean _matched = false;
		if (!_matched) {
			final TerminalNode _ANY_INT = x.ANY_INT();
			final boolean _tripleNotEquals = (_ANY_INT != null);
			if (_tripleNotEquals) {
				_matched = true;
				final HDLFunctionParameter _setType = res.setType(HDLFunctionParameter.Type.ANY_INT);
				res = _setType;
			}
		}
		if (!_matched) {
			final TerminalNode _ANY_UINT = x.ANY_UINT();
			final boolean _tripleNotEquals_1 = (_ANY_UINT != null);
			if (_tripleNotEquals_1) {
				_matched = true;
				final HDLFunctionParameter _setType_1 = res.setType(HDLFunctionParameter.Type.ANY_UINT);
				res = _setType_1;
			}
		}
		if (!_matched) {
			final TerminalNode _ANY_BIT = x.ANY_BIT();
			final boolean _tripleNotEquals_2 = (_ANY_BIT != null);
			if (_tripleNotEquals_2) {
				_matched = true;
				final HDLFunctionParameter _setType_2 = res.setType(HDLFunctionParameter.Type.ANY_BIT);
				res = _setType_2;
			}
		}
		if (!_matched) {
			final TerminalNode _INT = x.INT();
			final boolean _tripleNotEquals_3 = (_INT != null);
			if (_tripleNotEquals_3) {
				_matched = true;
				final HDLFunctionParameter _setType_3 = res.setType(HDLFunctionParameter.Type.REG_INT);
				res = _setType_3;
			}
		}
		if (!_matched) {
			final TerminalNode _UINT = x.UINT();
			final boolean _tripleNotEquals_4 = (_UINT != null);
			if (_tripleNotEquals_4) {
				_matched = true;
				final HDLFunctionParameter _setType_4 = res.setType(HDLFunctionParameter.Type.REG_UINT);
				res = _setType_4;
			}
		}
		if (!_matched) {
			final TerminalNode _BIT = x.BIT();
			final boolean _tripleNotEquals_5 = (_BIT != null);
			if (_tripleNotEquals_5) {
				_matched = true;
				final HDLFunctionParameter _setType_5 = res.setType(HDLFunctionParameter.Type.REG_BIT);
				res = _setType_5;
			}
		}
		if (!_matched) {
			final TerminalNode _BOOL = x.BOOL();
			final boolean _tripleNotEquals_6 = (_BOOL != null);
			if (_tripleNotEquals_6) {
				_matched = true;
				final HDLFunctionParameter _setType_6 = res.setType(HDLFunctionParameter.Type.BOOL_TYPE);
				res = _setType_6;
			}
		}
		if (!_matched) {
			final TerminalNode _STRING = x.STRING();
			final boolean _tripleNotEquals_7 = (_STRING != null);
			if (_tripleNotEquals_7) {
				_matched = true;
				final HDLFunctionParameter _setType_7 = res.setType(HDLFunctionParameter.Type.STRING_TYPE);
				res = _setType_7;
			}
		}
		if (!_matched) {
			final TerminalNode _ANY_IF = x.ANY_IF();
			final boolean _tripleNotEquals_8 = (_ANY_IF != null);
			if (_tripleNotEquals_8) {
				_matched = true;
				final HDLFunctionParameter _setType_8 = res.setType(HDLFunctionParameter.Type.ANY_IF);
				res = _setType_8;
			}
		}
		if (!_matched) {
			final TerminalNode _ANY_ENUM = x.ANY_ENUM();
			final boolean _tripleNotEquals_9 = (_ANY_ENUM != null);
			if (_tripleNotEquals_9) {
				_matched = true;
				final HDLFunctionParameter _setType_9 = res.setType(HDLFunctionParameter.Type.ANY_ENUM);
				res = _setType_9;
			}
		}
		if (!_matched) {
			final TerminalNode _INTERFACE = x.INTERFACE();
			final boolean _tripleNotEquals_10 = (_INTERFACE != null);
			if (_tripleNotEquals_10) {
				_matched = true;
				final HDLFunctionParameter _setType_10 = res.setType(HDLFunctionParameter.Type.IF);
				res = _setType_10;
				final PSHDLLangParser.PsQualifiedNameContext _psQualifiedName = x.psQualifiedName();
				final HDLQualifiedName _fQNName = this.toFQNName(_psQualifiedName);
				final HDLFunctionParameter _setIfSpec = res.setIfSpec(_fQNName);
				res = _setIfSpec;
			}
		}
		if (!_matched) {
			final TerminalNode _ENUM = x.ENUM();
			final boolean _tripleNotEquals_11 = (_ENUM != null);
			if (_tripleNotEquals_11) {
				_matched = true;
				final HDLFunctionParameter _setType_11 = res.setType(HDLFunctionParameter.Type.ENUM);
				res = _setType_11;
				final PSHDLLangParser.PsQualifiedNameContext _psQualifiedName_1 = x.psQualifiedName();
				final HDLQualifiedName _fQNName_1 = this.toFQNName(_psQualifiedName_1);
				final HDLFunctionParameter _setEnumSpec = res.setEnumSpec(_fQNName_1);
				res = _setEnumSpec;
			}
		}
		if (!_matched) {
			final TerminalNode _FUNCTION = x.FUNCTION();
			final boolean _tripleNotEquals_12 = (_FUNCTION != null);
			if (_tripleNotEquals_12) {
				_matched = true;
				final HDLFunctionParameter _setType_12 = res.setType(HDLFunctionParameter.Type.FUNCTION);
				res = _setType_12;
				final List<PSHDLLangParser.PsFuncParamWithRWContext> _psFuncParamWithRW = x.psFuncParamWithRW();
				final Function1<PSHDLLangParser.PsFuncParamWithRWContext, HDLFunctionParameter> _function = new Function1<PSHDLLangParser.PsFuncParamWithRWContext, HDLFunctionParameter>() {
					@Override
					public HDLFunctionParameter apply(final PSHDLLangParser.PsFuncParamWithRWContext it) {
						final IHDLObject _hDL = ParserToModelExtension.this.toHDL(it, false);
						return ((HDLFunctionParameter) _hDL);
					}
				};
				final List<HDLFunctionParameter> _map = ListExtensions.<PSHDLLangParser.PsFuncParamWithRWContext, HDLFunctionParameter> map(_psFuncParamWithRW, _function);
				final HDLFunctionParameter _setFuncSpec = res.setFuncSpec(_map);
				res = _setFuncSpec;
				final PSHDLLangParser.PsFuncParamTypeContext _psFuncParamType = x.psFuncParamType();
				final boolean _tripleNotEquals_13 = (_psFuncParamType != null);
				if (_tripleNotEquals_13) {
					final PSHDLLangParser.PsFuncParamTypeContext _psFuncParamType_1 = x.psFuncParamType();
					final IHDLObject _hDL = this.toHDL(_psFuncParamType_1, false);
					final HDLFunctionParameter _setFuncReturnSpec = res.setFuncReturnSpec(((HDLFunctionParameter) _hDL));
					res = _setFuncReturnSpec;
				}
			}
		}
		return res;
	}

	protected HDLInlineFunction _toHDL(final PSHDLLangParser.PsInlineFunctionContext context, final boolean isStatement) {
		HDLInlineFunction func = new HDLInlineFunction();
		final PSHDLLangParser.PsFunctionContext _psFunction = context.psFunction();
		final String _name = this.toName(_psFunction);
		final HDLInlineFunction _setName = func.setName(_name);
		func = _setName;
		final PSHDLLangParser.PsExpressionContext _psExpression = context.psExpression();
		final IHDLObject _hDL = this.toHDL(_psExpression, false);
		final HDLInlineFunction _setExpr = func.setExpr(((HDLExpression) _hDL));
		func = _setExpr;
		final PSHDLLangParser.PsFuncParamContext _psFuncParam = context.psFuncParam();
		final List<PSHDLLangParser.PsFuncSpecContext> _psFuncSpec = _psFuncParam.psFuncSpec();
		final Function1<PSHDLLangParser.PsFuncSpecContext, HDLFunctionParameter> _function = new Function1<PSHDLLangParser.PsFuncSpecContext, HDLFunctionParameter>() {
			@Override
			public HDLFunctionParameter apply(final PSHDLLangParser.PsFuncSpecContext it) {
				final IHDLObject _hDL = ParserToModelExtension.this.toHDL(it, false);
				return ((HDLFunctionParameter) _hDL);
			}
		};
		final List<HDLFunctionParameter> _map = ListExtensions.<PSHDLLangParser.PsFuncSpecContext, HDLFunctionParameter> map(_psFuncSpec, _function);
		final HDLInlineFunction _setArgs = func.setArgs(_map);
		func = _setArgs;
		final PSHDLLangParser.PsFuncRecturnTypeContext _psFuncRecturnType = context.psFuncRecturnType();
		final IHDLObject _hDL_1 = this.toHDL(_psFuncRecturnType, false);
		final HDLInlineFunction _setReturnType = func.setReturnType(((HDLFunctionParameter) _hDL_1));
		func = _setReturnType;
		return this.<HDLInlineFunction> attachContext(func, context);
	}

	public String toName(final PSHDLLangParser.PsFunctionContext context) {
		final TerminalNode _RULE_ID = context.RULE_ID();
		return _RULE_ID.getText();
	}

	protected IHDLObject _toHDL(final PSHDLLangParser.PsFunctionDeclarationContext context, final boolean isStatement) {
		final PSHDLLangParser.PsInlineFunctionContext _psInlineFunction = context.psInlineFunction();
		final boolean _tripleNotEquals = (_psInlineFunction != null);
		if (_tripleNotEquals) {
			final PSHDLLangParser.PsInlineFunctionContext _psInlineFunction_1 = context.psInlineFunction();
			final IHDLObject _hDL = this.toHDL(_psInlineFunction_1, true);
			return this.<IHDLObject> attachContext(_hDL, context);
		}
		final PSHDLLangParser.PsNativeFunctionContext _psNativeFunction = context.psNativeFunction();
		final boolean _tripleNotEquals_1 = (_psNativeFunction != null);
		if (_tripleNotEquals_1) {
			final PSHDLLangParser.PsNativeFunctionContext _psNativeFunction_1 = context.psNativeFunction();
			final IHDLObject _hDL_1 = this.toHDL(_psNativeFunction_1, true);
			return this.<IHDLObject> attachContext(_hDL_1, context);
		}
		final PSHDLLangParser.PsSubstituteFunctionContext _psSubstituteFunction = context.psSubstituteFunction();
		final boolean _tripleNotEquals_2 = (_psSubstituteFunction != null);
		if (_tripleNotEquals_2) {
			final PSHDLLangParser.PsSubstituteFunctionContext _psSubstituteFunction_1 = context.psSubstituteFunction();
			final IHDLObject _hDL_2 = this.toHDL(_psSubstituteFunction_1, true);
			return this.<IHDLObject> attachContext(_hDL_2, context);
		}
		final Class<? extends PSHDLLangParser.PsFunctionDeclarationContext> _class = context.getClass();
		final String _plus = ("Not implemented type:" + _class);
		throw new IllegalArgumentException(_plus);
	}

	protected HDLUnresolvedFragment _toHDL(final PSHDLLangParser.PsRefPartContext context, final boolean isStatement) {
		HDLUnresolvedFragment frag = null;
		final PSHDLLangParser.PsFuncArgsContext _psFuncArgs = context.psFuncArgs();
		final boolean _tripleNotEquals = (_psFuncArgs != null);
		if (_tripleNotEquals) {
			final HDLUnresolvedFragmentFunction _hDLUnresolvedFragmentFunction = new HDLUnresolvedFragmentFunction();
			final TerminalNode _RULE_ID = context.RULE_ID();
			final String _text = _RULE_ID.getText();
			final HDLUnresolvedFragmentFunction uff = _hDLUnresolvedFragmentFunction.setFrag(_text);
			final PSHDLLangParser.PsFuncArgsContext _psFuncArgs_1 = context.psFuncArgs();
			final List<PSHDLLangParser.PsExpressionContext> _psExpression = _psFuncArgs_1.psExpression();
			final Function1<PSHDLLangParser.PsExpressionContext, HDLExpression> _function = new Function1<PSHDLLangParser.PsExpressionContext, HDLExpression>() {
				@Override
				public HDLExpression apply(final PSHDLLangParser.PsExpressionContext it) {
					final IHDLObject _hDL = ParserToModelExtension.this.toHDL(it, false);
					return ((HDLExpression) _hDL);
				}
			};
			final List<HDLExpression> _map = ListExtensions.<PSHDLLangParser.PsExpressionContext, HDLExpression> map(_psExpression, _function);
			final HDLUnresolvedFragmentFunction _setParams = uff.setParams(_map);
			frag = _setParams;
		} else {
			final HDLUnresolvedFragment _hDLUnresolvedFragment = new HDLUnresolvedFragment();
			final TerminalNode _RULE_ID_1 = context.RULE_ID();
			final String _text_1 = _RULE_ID_1.getText();
			final HDLUnresolvedFragment _setFrag = _hDLUnresolvedFragment.setFrag(_text_1);
			frag = _setFrag;
			final PSHDLLangParser.PsArrayContext _psArray = context.psArray();
			final boolean _tripleNotEquals_1 = (_psArray != null);
			if (_tripleNotEquals_1) {
				final PSHDLLangParser.PsArrayContext _psArray_1 = context.psArray();
				final List<PSHDLLangParser.PsExpressionContext> _psExpression_1 = _psArray_1.psExpression();
				final Function1<PSHDLLangParser.PsExpressionContext, HDLExpression> _function_1 = new Function1<PSHDLLangParser.PsExpressionContext, HDLExpression>() {
					@Override
					public HDLExpression apply(final PSHDLLangParser.PsExpressionContext it) {
						final IHDLObject _hDL = ParserToModelExtension.this.toHDL(it, false);
						return ((HDLExpression) _hDL);
					}
				};
				final List<HDLExpression> _map_1 = ListExtensions.<PSHDLLangParser.PsExpressionContext, HDLExpression> map(_psExpression_1, _function_1);
				final HDLUnresolvedFragment _setArray = frag.setArray(_map_1);
				frag = _setArray;
			}
			final PSHDLLangParser.PsBitAccessContext _psBitAccess = context.psBitAccess();
			final boolean _tripleNotEquals_2 = (_psBitAccess != null);
			if (_tripleNotEquals_2) {
				final PSHDLLangParser.PsBitAccessContext _psBitAccess_1 = context.psBitAccess();
				final List<PSHDLLangParser.PsAccessRangeContext> _psAccessRange = _psBitAccess_1.psAccessRange();
				final Function1<PSHDLLangParser.PsAccessRangeContext, HDLRange> _function_2 = new Function1<PSHDLLangParser.PsAccessRangeContext, HDLRange>() {
					@Override
					public HDLRange apply(final PSHDLLangParser.PsAccessRangeContext it) {
						final IHDLObject _hDL = ParserToModelExtension.this.toHDL(it, false);
						return ((HDLRange) _hDL);
					}
				};
				final List<HDLRange> _map_2 = ListExtensions.<PSHDLLangParser.PsAccessRangeContext, HDLRange> map(_psAccessRange, _function_2);
				final HDLUnresolvedFragment _setBits = frag.setBits(_map_2);
				frag = _setBits;
			}
		}
		final HDLUnresolvedFragment _setIsStatement = frag.setIsStatement(isStatement);
		frag = _setIsStatement;
		return this.<HDLUnresolvedFragment> attachContext(frag, context);
	}

	protected HDLReference _toHDL(final PSHDLLangParser.PsVariableRefContext context, final boolean isStatement) {
		final boolean _tripleNotEquals = (context.isClk != null);
		if (_tripleNotEquals) {
			final HDLVariable _defaultClk = HDLRegisterConfig.defaultClk(true);
			final HDLVariableRef _asHDLRef = _defaultClk.asHDLRef();
			return this.<HDLVariableRef> attachContext(_asHDLRef, context);
		}
		final boolean _tripleNotEquals_1 = (context.isRst != null);
		if (_tripleNotEquals_1) {
			final HDLVariable _defaultRst = HDLRegisterConfig.defaultRst(true);
			final HDLVariableRef _asHDLRef_1 = _defaultRst.asHDLRef();
			return this.<HDLVariableRef> attachContext(_asHDLRef_1, context);
		}
		HDLUnresolvedFragment current = null;
		final List<PSHDLLangParser.PsRefPartContext> _psRefPart = context.psRefPart();
		final List<PSHDLLangParser.PsRefPartContext> _reverseView = ListExtensions.<PSHDLLangParser.PsRefPartContext> reverseView(_psRefPart);
		for (final PSHDLLangParser.PsRefPartContext sub : _reverseView) {
			{
				final IHDLObject _hDL = this.toHDL(sub, false);
				HDLUnresolvedFragment frag = ((HDLUnresolvedFragment) _hDL);
				final boolean _tripleNotEquals_2 = (current != null);
				if (_tripleNotEquals_2) {
					final HDLUnresolvedFragment _setSub = frag.setSub(current);
					frag = _setSub;
				}
				current = frag;
			}
		}
		final boolean _tripleNotEquals_2 = (current != null);
		if (_tripleNotEquals_2) {
			final HDLUnresolvedFragment _setIsStatement = current.setIsStatement(isStatement);
			current = _setIsStatement;
			return this.<HDLUnresolvedFragment> attachContext(current, context);
		}
		return null;
	}

	protected HDLRange _toHDL(final PSHDLLangParser.PsAccessRangeContext context, final boolean isStatement) {
		final HDLRange _hDLRange = new HDLRange();
		final IHDLObject _hDL = this.toHDL(context.from, false);
		HDLRange res = _hDLRange.setTo(((HDLExpression) _hDL));
		final boolean _tripleNotEquals = (context.to != null);
		if (_tripleNotEquals) {
			final IHDLObject _hDL_1 = this.toHDL(context.from, false);
			final HDLRange _setFrom = res.setFrom(((HDLExpression) _hDL_1));
			final IHDLObject _hDL_2 = this.toHDL(context.to, isStatement);
			final HDLRange _setTo = _setFrom.setTo(((HDLExpression) _hDL_2));
			res = _setTo;
		}
		final boolean _tripleNotEquals_1 = (context.inc != null);
		if (_tripleNotEquals_1) {
			final IHDLObject _hDL_3 = this.toHDL(context.from, false);
			final HDLRange _setTo_1 = res.setTo(((HDLExpression) _hDL_3));
			final IHDLObject _hDL_4 = this.toHDL(context.inc, isStatement);
			final HDLRange _setInc = _setTo_1.setInc(((HDLExpression) _hDL_4));
			res = _setInc;
		}
		final boolean _tripleNotEquals_2 = (context.dec != null);
		if (_tripleNotEquals_2) {
			final IHDLObject _hDL_5 = this.toHDL(context.from, false);
			final HDLRange _setTo_2 = res.setTo(((HDLExpression) _hDL_5));
			final IHDLObject _hDL_6 = this.toHDL(context.dec, isStatement);
			final HDLRange _setDec = _setTo_2.setDec(((HDLExpression) _hDL_6));
			res = _setDec;
		}
		return this.<HDLRange> attachContext(res, context);
	}

	protected HDLSwitchCaseStatement _toHDL(final PSHDLLangParser.PsCaseStatementsContext context, final boolean isStatement) {
		HDLSwitchCaseStatement hCase = new HDLSwitchCaseStatement();
		final PSHDLLangParser.PsValueContext _psValue = context.psValue();
		final boolean _tripleNotEquals = (_psValue != null);
		if (_tripleNotEquals) {
			final PSHDLLangParser.PsValueContext _psValue_1 = context.psValue();
			final IHDLObject _hDL = this.toHDL(_psValue_1, false);
			final HDLSwitchCaseStatement _setLabel = hCase.setLabel(((HDLExpression) _hDL));
			hCase = _setLabel;
		}
		final List<PSHDLLangParser.PsBlockContext> _psBlock = context.psBlock();
		final Function1<PSHDLLangParser.PsBlockContext, HDLStatement> _function = new Function1<PSHDLLangParser.PsBlockContext, HDLStatement>() {
			@Override
			public HDLStatement apply(final PSHDLLangParser.PsBlockContext it) {
				final IHDLObject _hDL = ParserToModelExtension.this.toHDL(it, true);
				return ((HDLStatement) _hDL);
			}
		};
		final List<HDLStatement> _map = ListExtensions.<PSHDLLangParser.PsBlockContext, HDLStatement> map(_psBlock, _function);
		final HDLSwitchCaseStatement _setDos = hCase.setDos(_map);
		hCase = _setDos;
		return this.<HDLSwitchCaseStatement> attachContext(hCase, context);
	}

	protected HDLSwitchStatement _toHDL(final PSHDLLangParser.PsSwitchStatementContext context, final boolean isStatement) {
		final HDLSwitchStatement _hDLSwitchStatement = new HDLSwitchStatement();
		final PSHDLLangParser.PsVariableRefContext _psVariableRef = context.psVariableRef();
		final IHDLObject _hDL = this.toHDL(_psVariableRef, false);
		HDLSwitchStatement switchStmnt = _hDLSwitchStatement.setCaseExp(((HDLExpression) _hDL));
		final List<PSHDLLangParser.PsCaseStatementsContext> _psCaseStatements = context.psCaseStatements();
		final Function1<PSHDLLangParser.PsCaseStatementsContext, HDLSwitchCaseStatement> _function = new Function1<PSHDLLangParser.PsCaseStatementsContext, HDLSwitchCaseStatement>() {
			@Override
			public HDLSwitchCaseStatement apply(final PSHDLLangParser.PsCaseStatementsContext it) {
				final IHDLObject _hDL = ParserToModelExtension.this.toHDL(it, true);
				return ((HDLSwitchCaseStatement) _hDL);
			}
		};
		final List<HDLSwitchCaseStatement> _map = ListExtensions.<PSHDLLangParser.PsCaseStatementsContext, HDLSwitchCaseStatement> map(_psCaseStatements, _function);
		final HDLSwitchStatement _setCases = switchStmnt.setCases(_map);
		switchStmnt = _setCases;
		return this.<HDLSwitchStatement> attachContext(switchStmnt, context);
	}

	protected HDLInterfaceInstantiation _toHDL(final PSHDLLangParser.PsInterfaceInstantiationContext context, final boolean isStatement) {
		final PSHDLLangParser.PsVariableContext _psVariable = context.psVariable();
		final IHDLObject _hDL = this.toHDL(_psVariable, isStatement);
		HDLVariable hVar = ((HDLVariable) _hDL);
		final PSHDLLangParser.PsArrayContext _psArray = context.psArray();
		final boolean _tripleNotEquals = (_psArray != null);
		if (_tripleNotEquals) {
			final PSHDLLangParser.PsArrayContext _psArray_1 = context.psArray();
			final List<PSHDLLangParser.PsExpressionContext> _psExpression = _psArray_1.psExpression();
			final Function1<PSHDLLangParser.PsExpressionContext, HDLExpression> _function = new Function1<PSHDLLangParser.PsExpressionContext, HDLExpression>() {
				@Override
				public HDLExpression apply(final PSHDLLangParser.PsExpressionContext it) {
					final IHDLObject _hDL = ParserToModelExtension.this.toHDL(it, false);
					return ((HDLExpression) _hDL);
				}
			};
			final List<HDLExpression> _map = ListExtensions.<PSHDLLangParser.PsExpressionContext, HDLExpression> map(_psExpression, _function);
			final HDLVariable _setDimensions = hVar.setDimensions(_map);
			hVar = _setDimensions;
		}
		final HDLInterfaceInstantiation _hDLInterfaceInstantiation = new HDLInterfaceInstantiation();
		final HDLInterfaceInstantiation _setVar = _hDLInterfaceInstantiation.setVar(hVar);
		final PSHDLLangParser.PsQualifiedNameContext _psQualifiedName = context.psQualifiedName();
		final HDLQualifiedName _fQNName = this.toFQNName(_psQualifiedName);
		HDLInterfaceInstantiation hii = _setVar.setHIf(_fQNName);
		final PSHDLLangParser.PsPassedArgumentsContext _psPassedArguments = context.psPassedArguments();
		final boolean _tripleNotEquals_1 = (_psPassedArguments != null);
		if (_tripleNotEquals_1) {
			final PSHDLLangParser.PsPassedArgumentsContext _psPassedArguments_1 = context.psPassedArguments();
			final List<PSHDLLangParser.PsArgumentContext> _psArgument = _psPassedArguments_1.psArgument();
			final Function1<PSHDLLangParser.PsArgumentContext, HDLArgument> _function_1 = new Function1<PSHDLLangParser.PsArgumentContext, HDLArgument>() {
				@Override
				public HDLArgument apply(final PSHDLLangParser.PsArgumentContext it) {
					final IHDLObject _hDL = ParserToModelExtension.this.toHDL(it, false);
					return ((HDLArgument) _hDL);
				}
			};
			final List<HDLArgument> _map_1 = ListExtensions.<PSHDLLangParser.PsArgumentContext, HDLArgument> map(_psArgument, _function_1);
			final HDLInterfaceInstantiation _setArguments = hii.setArguments(_map_1);
			hii = _setArguments;
		}
		return this.<HDLInterfaceInstantiation> attachContext(hii, context);
	}

	protected HDLForLoop _toHDL(final PSHDLLangParser.PsForStatementContext context, final boolean isStatement) {
		final HDLForLoop _hDLForLoop = new HDLForLoop();
		final PSHDLLangParser.PsVariableContext _psVariable = context.psVariable();
		final IHDLObject _hDL = this.toHDL(_psVariable, false);
		HDLForLoop loop = _hDLForLoop.setParam(((HDLVariable) _hDL));
		final PSHDLLangParser.PsBitAccessContext _psBitAccess = context.psBitAccess();
		final List<PSHDLLangParser.PsAccessRangeContext> _psAccessRange = _psBitAccess.psAccessRange();
		final Function1<PSHDLLangParser.PsAccessRangeContext, HDLRange> _function = new Function1<PSHDLLangParser.PsAccessRangeContext, HDLRange>() {
			@Override
			public HDLRange apply(final PSHDLLangParser.PsAccessRangeContext it) {
				final IHDLObject _hDL = ParserToModelExtension.this.toHDL(it, false);
				return ((HDLRange) _hDL);
			}
		};
		final List<HDLRange> _map = ListExtensions.<PSHDLLangParser.PsAccessRangeContext, HDLRange> map(_psAccessRange, _function);
		final HDLForLoop _setRange = loop.setRange(_map);
		loop = _setRange;
		final PSHDLLangParser.PsSimpleBlockContext _psSimpleBlock = context.psSimpleBlock();
		final List<PSHDLLangParser.PsBlockContext> _psBlock = _psSimpleBlock.psBlock();
		final Function1<PSHDLLangParser.PsBlockContext, HDLStatement> _function_1 = new Function1<PSHDLLangParser.PsBlockContext, HDLStatement>() {
			@Override
			public HDLStatement apply(final PSHDLLangParser.PsBlockContext it) {
				final IHDLObject _hDL = ParserToModelExtension.this.toHDL(it, true);
				return ((HDLStatement) _hDL);
			}
		};
		final List<HDLStatement> _map_1 = ListExtensions.<PSHDLLangParser.PsBlockContext, HDLStatement> map(_psBlock, _function_1);
		final HDLForLoop _setDos = loop.setDos(_map_1);
		loop = _setDos;
		return this.<HDLForLoop> attachContext(loop, context);
	}

	protected HDLIfStatement _toHDL(final PSHDLLangParser.PsIfStatementContext context, final boolean isStatement) {
		final HDLIfStatement _hDLIfStatement = new HDLIfStatement();
		final PSHDLLangParser.PsExpressionContext _psExpression = context.psExpression();
		final IHDLObject _hDL = this.toHDL(_psExpression, false);
		HDLIfStatement res = _hDLIfStatement.setIfExp(((HDLExpression) _hDL));
		final List<PSHDLLangParser.PsBlockContext> _psBlock = context.ifBlk.psBlock();
		final Function1<PSHDLLangParser.PsBlockContext, HDLStatement> _function = new Function1<PSHDLLangParser.PsBlockContext, HDLStatement>() {
			@Override
			public HDLStatement apply(final PSHDLLangParser.PsBlockContext it) {
				final IHDLObject _hDL = ParserToModelExtension.this.toHDL(it, true);
				return ((HDLStatement) _hDL);
			}
		};
		final List<HDLStatement> _map = ListExtensions.<PSHDLLangParser.PsBlockContext, HDLStatement> map(_psBlock, _function);
		final HDLIfStatement _setThenDo = res.setThenDo(_map);
		res = _setThenDo;
		final boolean _tripleNotEquals = (context.elseBlk != null);
		if (_tripleNotEquals) {
			final List<PSHDLLangParser.PsBlockContext> _psBlock_1 = context.elseBlk.psBlock();
			final Function1<PSHDLLangParser.PsBlockContext, HDLStatement> _function_1 = new Function1<PSHDLLangParser.PsBlockContext, HDLStatement>() {
				@Override
				public HDLStatement apply(final PSHDLLangParser.PsBlockContext it) {
					final IHDLObject _hDL = ParserToModelExtension.this.toHDL(it, true);
					return ((HDLStatement) _hDL);
				}
			};
			final List<HDLStatement> _map_1 = ListExtensions.<PSHDLLangParser.PsBlockContext, HDLStatement> map(_psBlock_1, _function_1);
			final HDLIfStatement _setElseDo = res.setElseDo(_map_1);
			res = _setElseDo;
		}
		return this.<HDLIfStatement> attachContext(res, context);
	}

	protected IHDLObject _toHDL(final PSHDLLangParser.PsCompoundStatementContext context, final boolean isStatement) {
		final PSHDLLangParser.PsForStatementContext _psForStatement = context.psForStatement();
		final boolean _tripleNotEquals = (_psForStatement != null);
		if (_tripleNotEquals) {
			final PSHDLLangParser.PsForStatementContext _psForStatement_1 = context.psForStatement();
			final IHDLObject _hDL = this.toHDL(_psForStatement_1, true);
			return this.<IHDLObject> attachContext(_hDL, context);
		}
		final PSHDLLangParser.PsIfStatementContext _psIfStatement = context.psIfStatement();
		final boolean _tripleNotEquals_1 = (_psIfStatement != null);
		if (_tripleNotEquals_1) {
			final PSHDLLangParser.PsIfStatementContext _psIfStatement_1 = context.psIfStatement();
			final IHDLObject _hDL_1 = this.toHDL(_psIfStatement_1, true);
			return this.<IHDLObject> attachContext(_hDL_1, context);
		}
		final PSHDLLangParser.PsSwitchStatementContext _psSwitchStatement = context.psSwitchStatement();
		final boolean _tripleNotEquals_2 = (_psSwitchStatement != null);
		if (_tripleNotEquals_2) {
			final PSHDLLangParser.PsSwitchStatementContext _psSwitchStatement_1 = context.psSwitchStatement();
			final IHDLObject _hDL_2 = this.toHDL(_psSwitchStatement_1, true);
			return this.<IHDLObject> attachContext(_hDL_2, context);
		}
		final Class<? extends PSHDLLangParser.PsCompoundStatementContext> _class = context.getClass();
		final String _plus = ("Unhandled type:" + _class);
		throw new IllegalArgumentException(_plus);
	}

	protected IHDLObject _toHDL(final PSHDLLangParser.PsStatementContext context, final boolean isStatement) {
		final PSHDLLangParser.PsAssignmentOrFuncContext _psAssignmentOrFunc = context.psAssignmentOrFunc();
		final boolean _tripleNotEquals = (_psAssignmentOrFunc != null);
		if (_tripleNotEquals) {
			final PSHDLLangParser.PsAssignmentOrFuncContext _psAssignmentOrFunc_1 = context.psAssignmentOrFunc();
			final IHDLObject _hDL = this.toHDL(_psAssignmentOrFunc_1, true);
			return this.<IHDLObject> attachContext(_hDL, context);
		}
		final PSHDLLangParser.PsCompoundStatementContext _psCompoundStatement = context.psCompoundStatement();
		final boolean _tripleNotEquals_1 = (_psCompoundStatement != null);
		if (_tripleNotEquals_1) {
			final PSHDLLangParser.PsCompoundStatementContext _psCompoundStatement_1 = context.psCompoundStatement();
			final IHDLObject _hDL_1 = this.toHDL(_psCompoundStatement_1, true);
			return this.<IHDLObject> attachContext(_hDL_1, context);
		}
		final PSHDLLangParser.PsProcessContext _psProcess = context.psProcess();
		final boolean _tripleNotEquals_2 = (_psProcess != null);
		if (_tripleNotEquals_2) {
			final PSHDLLangParser.PsProcessContext _psProcess_1 = context.psProcess();
			final IHDLObject _hDL_2 = this.toHDL(_psProcess_1, true);
			return this.<IHDLObject> attachContext(_hDL_2, context);
		}
		final Class<? extends PSHDLLangParser.PsStatementContext> _class = context.getClass();
		final String _plus = ("Unhandled type:" + _class);
		throw new IllegalArgumentException(_plus);
	}

	protected IHDLObject _toHDL(final PSHDLLangParser.PsAssignmentOrFuncContext context, final boolean isStatement) {
		final PSHDLLangParser.PsVariableRefContext _psVariableRef = context.psVariableRef();
		final IHDLObject _hDL = this.toHDL(_psVariableRef, isStatement);
		HDLReference hVar = ((HDLReference) _hDL);
		final PSHDLLangParser.PsAssignmentOpContext _psAssignmentOp = context.psAssignmentOp();
		final boolean _tripleNotEquals = (_psAssignmentOp != null);
		if (_tripleNotEquals) {
			final PSHDLLangParser.PsAssignmentOpContext _psAssignmentOp_1 = context.psAssignmentOp();
			final String _text = _psAssignmentOp_1.getText();
			final HDLAssignment.HDLAssignmentType type = HDLAssignment.HDLAssignmentType.getOp(_text);
			if ((hVar instanceof HDLUnresolvedFragment)) {
				final HDLUnresolvedFragment _setIsStatement = ((HDLUnresolvedFragment) hVar).setIsStatement(false);
				hVar = _setIsStatement;
			}
			final HDLAssignment _hDLAssignment = new HDLAssignment();
			final HDLAssignment _setLeft = _hDLAssignment.setLeft(hVar);
			HDLAssignment ass = _setLeft.setType(type);
			final PSHDLLangParser.PsExpressionContext _psExpression = context.psExpression();
			final IHDLObject _hDL_1 = this.toHDL(_psExpression, false);
			final HDLAssignment _setRight = ass.setRight(((HDLExpression) _hDL_1));
			ass = _setRight;
			return this.<HDLAssignment> attachContext(ass, context);
		}
		return this.<HDLReference> attachContext(hVar, context);
	}

	protected IHDLObject _toHDL(final Object context, final boolean isStatement) {
		final Class<?> _class = context.getClass();
		final String _plus = ("Unhandled type:" + _class);
		throw new IllegalArgumentException(_plus);
	}

	public HDLUnit toHDLUnit(final PSHDLLangParser.PsUnitContext context, final String libURI) {
		final HDLUnit _hDLUnit = new HDLUnit();
		final PSHDLLangParser.PsInterfaceContext _psInterface = context.psInterface();
		final String _name = this.toName(_psInterface);
		final HDLUnit _setName = _hDLUnit.setName(_name);
		HDLUnit unit = _setName.setLibURI(libURI);
		final int _type = context.unitType.getType();
		final boolean _equals = (_type == PSHDLLangLexer.TESTBENCH);
		final HDLUnit _setSimulation = unit.setSimulation(_equals);
		unit = _setSimulation;
		final List<PSHDLLangParser.PsAnnotationContext> _psAnnotation = context.psAnnotation();
		final Function1<PSHDLLangParser.PsAnnotationContext, HDLAnnotation> _function = new Function1<PSHDLLangParser.PsAnnotationContext, HDLAnnotation>() {
			@Override
			public HDLAnnotation apply(final PSHDLLangParser.PsAnnotationContext it) {
				final IHDLObject _hDL = ParserToModelExtension.this.toHDL(it, true);
				return ((HDLAnnotation) _hDL);
			}
		};
		final List<HDLAnnotation> _map = ListExtensions.<PSHDLLangParser.PsAnnotationContext, HDLAnnotation> map(_psAnnotation, _function);
		final HDLUnit _setAnnotations = unit.setAnnotations(_map);
		unit = _setAnnotations;
		final List<PSHDLLangParser.PsImportsContext> _psImports = context.psImports();
		final Function1<PSHDLLangParser.PsImportsContext, String> _function_1 = new Function1<PSHDLLangParser.PsImportsContext, String>() {
			@Override
			public String apply(final PSHDLLangParser.PsImportsContext it) {
				return ParserToModelExtension.this.toName(it);
			}
		};
		final List<String> _map_1 = ListExtensions.<PSHDLLangParser.PsImportsContext, String> map(_psImports, _function_1);
		final HDLUnit _setImports = unit.setImports(_map_1);
		unit = _setImports;
		final List<PSHDLLangParser.PsBlockContext> _psBlock = context.psBlock();
		final Function1<PSHDLLangParser.PsBlockContext, HDLStatement> _function_2 = new Function1<PSHDLLangParser.PsBlockContext, HDLStatement>() {
			@Override
			public HDLStatement apply(final PSHDLLangParser.PsBlockContext it) {
				final IHDLObject _hDL = ParserToModelExtension.this.toHDL(it, true);
				return ((HDLStatement) _hDL);
			}
		};
		final List<HDLStatement> _map_2 = ListExtensions.<PSHDLLangParser.PsBlockContext, HDLStatement> map(_psBlock, _function_2);
		final HDLUnit _setStatements = unit.setStatements(_map_2);
		unit = _setStatements;
		return this.<HDLUnit> attachContext(unit, context);
	}

	public String toName(final PSHDLLangParser.PsImportsContext context) {
		final PSHDLLangParser.PsQualifiedNameImportContext _psQualifiedNameImport = context.psQualifiedNameImport();
		return _psQualifiedNameImport.getText();
	}

	public IHDLObject toHDL(final Object context, final boolean isStatement) {
		if (context instanceof PSHDLLangParser.PsAddContext)
			return _toHDL((PSHDLLangParser.PsAddContext) context, isStatement);
		else if (context instanceof PSHDLLangParser.PsArrayInitExpContext)
			return _toHDL((PSHDLLangParser.PsArrayInitExpContext) context, isStatement);
		else if (context instanceof PSHDLLangParser.PsBitAndContext)
			return _toHDL((PSHDLLangParser.PsBitAndContext) context, isStatement);
		else if (context instanceof PSHDLLangParser.PsBitLogAndContext)
			return _toHDL((PSHDLLangParser.PsBitLogAndContext) context, isStatement);
		else if (context instanceof PSHDLLangParser.PsBitLogOrContext)
			return _toHDL((PSHDLLangParser.PsBitLogOrContext) context, isStatement);
		else if (context instanceof PSHDLLangParser.PsBitOrContext)
			return _toHDL((PSHDLLangParser.PsBitOrContext) context, isStatement);
		else if (context instanceof PSHDLLangParser.PsBitXorContext)
			return _toHDL((PSHDLLangParser.PsBitXorContext) context, isStatement);
		else if (context instanceof PSHDLLangParser.PsConcatContext)
			return _toHDL((PSHDLLangParser.PsConcatContext) context, isStatement);
		else if (context instanceof PSHDLLangParser.PsEqualityCompContext)
			return _toHDL((PSHDLLangParser.PsEqualityCompContext) context, isStatement);
		else if (context instanceof PSHDLLangParser.PsEqualityContext)
			return _toHDL((PSHDLLangParser.PsEqualityContext) context, isStatement);
		else if (context instanceof PSHDLLangParser.PsManipContext)
			return _toHDL((PSHDLLangParser.PsManipContext) context, isStatement);
		else if (context instanceof PSHDLLangParser.PsMulContext)
			return _toHDL((PSHDLLangParser.PsMulContext) context, isStatement);
		else if (context instanceof PSHDLLangParser.PsParensContext)
			return _toHDL((PSHDLLangParser.PsParensContext) context, isStatement);
		else if (context instanceof PSHDLLangParser.PsShiftContext)
			return _toHDL((PSHDLLangParser.PsShiftContext) context, isStatement);
		else if (context instanceof PSHDLLangParser.PsTernaryContext)
			return _toHDL((PSHDLLangParser.PsTernaryContext) context, isStatement);
		else if (context instanceof PSHDLLangParser.PsValueExpContext)
			return _toHDL((PSHDLLangParser.PsValueExpContext) context, isStatement);
		else if (context instanceof PSHDLLangParser.PsAccessRangeContext)
			return _toHDL((PSHDLLangParser.PsAccessRangeContext) context, isStatement);
		else if (context instanceof PSHDLLangParser.PsAnnotationContext)
			return _toHDL((PSHDLLangParser.PsAnnotationContext) context, isStatement);
		else if (context instanceof PSHDLLangParser.PsArgumentContext)
			return _toHDL((PSHDLLangParser.PsArgumentContext) context, isStatement);
		else if (context instanceof PSHDLLangParser.PsArrayInitContext)
			return _toHDL((PSHDLLangParser.PsArrayInitContext) context, isStatement);
		else if (context instanceof PSHDLLangParser.PsArrayInitSubContext)
			return _toHDL((PSHDLLangParser.PsArrayInitSubContext) context, isStatement);
		else if (context instanceof PSHDLLangParser.PsArrayInitSubParensContext)
			return _toHDL((PSHDLLangParser.PsArrayInitSubParensContext) context, isStatement);
		else if (context instanceof PSHDLLangParser.PsAssignmentOrFuncContext)
			return _toHDL((PSHDLLangParser.PsAssignmentOrFuncContext) context, isStatement);
		else if (context instanceof PSHDLLangParser.PsBlockContext)
			return _toHDL((PSHDLLangParser.PsBlockContext) context, isStatement);
		else if (context instanceof PSHDLLangParser.PsCaseStatementsContext)
			return _toHDL((PSHDLLangParser.PsCaseStatementsContext) context, isStatement);
		else if (context instanceof PSHDLLangParser.PsCastContext)
			return _toHDL((PSHDLLangParser.PsCastContext) context, isStatement);
		else if (context instanceof PSHDLLangParser.PsCompoundStatementContext)
			return _toHDL((PSHDLLangParser.PsCompoundStatementContext) context, isStatement);
		else if (context instanceof PSHDLLangParser.PsDeclAssignmentContext)
			return _toHDL((PSHDLLangParser.PsDeclAssignmentContext) context, isStatement);
		else if (context instanceof PSHDLLangParser.PsDeclarationContext)
			return _toHDL((PSHDLLangParser.PsDeclarationContext) context, isStatement);
		else if (context instanceof PSHDLLangParser.PsDeclarationTypeContext)
			return _toHDL((PSHDLLangParser.PsDeclarationTypeContext) context, isStatement);
		else if (context instanceof PSHDLLangParser.PsDirectGenerationContext)
			return _toHDL((PSHDLLangParser.PsDirectGenerationContext) context, isStatement);
		else if (context instanceof PSHDLLangParser.PsEnumContext)
			return _toHDL((PSHDLLangParser.PsEnumContext) context, isStatement);
		else if (context instanceof PSHDLLangParser.PsEnumDeclarationContext)
			return _toHDL((PSHDLLangParser.PsEnumDeclarationContext) context, isStatement);
		else if (context instanceof PSHDLLangParser.PsExpressionContext)
			return _toHDL((PSHDLLangParser.PsExpressionContext) context, isStatement);
		else if (context instanceof PSHDLLangParser.PsForStatementContext)
			return _toHDL((PSHDLLangParser.PsForStatementContext) context, isStatement);
		else if (context instanceof PSHDLLangParser.PsFuncParamTypeContext)
			return _toHDL((PSHDLLangParser.PsFuncParamTypeContext) context, isStatement);
		else if (context instanceof PSHDLLangParser.PsFuncParamWithRWContext)
			return _toHDL((PSHDLLangParser.PsFuncParamWithRWContext) context, isStatement);
		else if (context instanceof PSHDLLangParser.PsFuncRecturnTypeContext)
			return _toHDL((PSHDLLangParser.PsFuncRecturnTypeContext) context, isStatement);
		else if (context instanceof PSHDLLangParser.PsFuncSpecContext)
			return _toHDL((PSHDLLangParser.PsFuncSpecContext) context, isStatement);
		else if (context instanceof PSHDLLangParser.PsFunctionDeclarationContext)
			return _toHDL((PSHDLLangParser.PsFunctionDeclarationContext) context, isStatement);
		else if (context instanceof PSHDLLangParser.PsIfStatementContext)
			return _toHDL((PSHDLLangParser.PsIfStatementContext) context, isStatement);
		else if (context instanceof PSHDLLangParser.PsInlineFunctionContext)
			return _toHDL((PSHDLLangParser.PsInlineFunctionContext) context, isStatement);
		else if (context instanceof PSHDLLangParser.PsInstantiationContext)
			return _toHDL((PSHDLLangParser.PsInstantiationContext) context, isStatement);
		else if (context instanceof PSHDLLangParser.PsInterfaceContext)
			return _toHDL((PSHDLLangParser.PsInterfaceContext) context, isStatement);
		else if (context instanceof PSHDLLangParser.PsInterfaceDeclarationContext)
			return _toHDL((PSHDLLangParser.PsInterfaceDeclarationContext) context, isStatement);
		else if (context instanceof PSHDLLangParser.PsInterfaceInstantiationContext)
			return _toHDL((PSHDLLangParser.PsInterfaceInstantiationContext) context, isStatement);
		else if (context instanceof PSHDLLangParser.PsNativeFunctionContext)
			return _toHDL((PSHDLLangParser.PsNativeFunctionContext) context, isStatement);
		else if (context instanceof PSHDLLangParser.PsPortDeclarationContext)
			return _toHDL((PSHDLLangParser.PsPortDeclarationContext) context, isStatement);
		else if (context instanceof PSHDLLangParser.PsPrimitiveContext)
			return _toHDL((PSHDLLangParser.PsPrimitiveContext) context, isStatement);
		else if (context instanceof PSHDLLangParser.PsProcessContext)
			return _toHDL((PSHDLLangParser.PsProcessContext) context, isStatement);
		else if (context instanceof PSHDLLangParser.PsRefPartContext)
			return _toHDL((PSHDLLangParser.PsRefPartContext) context, isStatement);
		else if (context instanceof PSHDLLangParser.PsStatementContext)
			return _toHDL((PSHDLLangParser.PsStatementContext) context, isStatement);
		else if (context instanceof PSHDLLangParser.PsSubstituteFunctionContext)
			return _toHDL((PSHDLLangParser.PsSubstituteFunctionContext) context, isStatement);
		else if (context instanceof PSHDLLangParser.PsSwitchStatementContext)
			return _toHDL((PSHDLLangParser.PsSwitchStatementContext) context, isStatement);
		else if (context instanceof PSHDLLangParser.PsTypeDeclarationContext)
			return _toHDL((PSHDLLangParser.PsTypeDeclarationContext) context, isStatement);
		else if (context instanceof PSHDLLangParser.PsValueContext)
			return _toHDL((PSHDLLangParser.PsValueContext) context, isStatement);
		else if (context instanceof PSHDLLangParser.PsVariableContext)
			return _toHDL((PSHDLLangParser.PsVariableContext) context, isStatement);
		else if (context instanceof PSHDLLangParser.PsVariableDeclarationContext)
			return _toHDL((PSHDLLangParser.PsVariableDeclarationContext) context, isStatement);
		else if (context instanceof PSHDLLangParser.PsVariableRefContext)
			return _toHDL((PSHDLLangParser.PsVariableRefContext) context, isStatement);
		else if (context instanceof PSHDLLangParser.PsWidthContext)
			return _toHDL((PSHDLLangParser.PsWidthContext) context, isStatement);
		else if (context != null)
			return _toHDL(context, isStatement);
		else
			throw new IllegalArgumentException("Unhandled parameter types: " + Arrays.<Object> asList(context, isStatement).toString());
	}
}
