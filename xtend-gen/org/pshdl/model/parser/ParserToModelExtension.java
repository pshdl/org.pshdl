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

import java.util.*;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.*;
import org.pshdl.model.*;
import org.pshdl.model.HDLArithOp.HDLArithOpType;
import org.pshdl.model.HDLAssignment.HDLAssignmentType;
import org.pshdl.model.HDLBitOp.HDLBitOpType;
import org.pshdl.model.HDLEqualityOp.HDLEqualityOpType;
import org.pshdl.model.HDLManip.HDLManipType;
import org.pshdl.model.HDLPrimitive.HDLPrimitiveType;
import org.pshdl.model.HDLShiftOp.HDLShiftOpType;
import org.pshdl.model.HDLVariableDeclaration.HDLDirection;
import org.pshdl.model.parser.PSHDLLangParser.PsAccessRangeContext;
import org.pshdl.model.parser.PSHDLLangParser.PsAddContext;
import org.pshdl.model.parser.PSHDLLangParser.PsAnnotationContext;
import org.pshdl.model.parser.PSHDLLangParser.PsAnnotationTypeContext;
import org.pshdl.model.parser.PSHDLLangParser.PsArgumentContext;
import org.pshdl.model.parser.PSHDLLangParser.PsArrayContext;
import org.pshdl.model.parser.PSHDLLangParser.PsArrayInitContext;
import org.pshdl.model.parser.PSHDLLangParser.PsArrayInitSubContext;
import org.pshdl.model.parser.PSHDLLangParser.PsAssignmentOpContext;
import org.pshdl.model.parser.PSHDLLangParser.PsAssignmentOrFuncContext;
import org.pshdl.model.parser.PSHDLLangParser.PsBitAccessContext;
import org.pshdl.model.parser.PSHDLLangParser.PsBitAndContext;
import org.pshdl.model.parser.PSHDLLangParser.PsBitLogAndContext;
import org.pshdl.model.parser.PSHDLLangParser.PsBitLogOrContext;
import org.pshdl.model.parser.PSHDLLangParser.PsBitOrContext;
import org.pshdl.model.parser.PSHDLLangParser.PsBitXorContext;
import org.pshdl.model.parser.PSHDLLangParser.PsBlockContext;
import org.pshdl.model.parser.PSHDLLangParser.PsCaseStatementsContext;
import org.pshdl.model.parser.PSHDLLangParser.PsCastContext;
import org.pshdl.model.parser.PSHDLLangParser.PsCompoundStatementContext;
import org.pshdl.model.parser.PSHDLLangParser.PsConcatContext;
import org.pshdl.model.parser.PSHDLLangParser.PsDeclAssignmentContext;
import org.pshdl.model.parser.PSHDLLangParser.PsDeclarationContext;
import org.pshdl.model.parser.PSHDLLangParser.PsDeclarationTypeContext;
import org.pshdl.model.parser.PSHDLLangParser.PsDirectGenerationContext;
import org.pshdl.model.parser.PSHDLLangParser.PsDirectionContext;
import org.pshdl.model.parser.PSHDLLangParser.PsEnumContext;
import org.pshdl.model.parser.PSHDLLangParser.PsEnumDeclarationContext;
import org.pshdl.model.parser.PSHDLLangParser.PsEqualityCompContext;
import org.pshdl.model.parser.PSHDLLangParser.PsEqualityContext;
import org.pshdl.model.parser.PSHDLLangParser.PsExpressionContext;
import org.pshdl.model.parser.PSHDLLangParser.PsForStatementContext;
import org.pshdl.model.parser.PSHDLLangParser.PsFuncArgsContext;
import org.pshdl.model.parser.PSHDLLangParser.PsFuncParamContext;
import org.pshdl.model.parser.PSHDLLangParser.PsFunctionContext;
import org.pshdl.model.parser.PSHDLLangParser.PsFunctionDeclarationContext;
import org.pshdl.model.parser.PSHDLLangParser.PsIfStatementContext;
import org.pshdl.model.parser.PSHDLLangParser.PsImportsContext;
import org.pshdl.model.parser.PSHDLLangParser.PsInlineFunctionContext;
import org.pshdl.model.parser.PSHDLLangParser.PsInstantiationContext;
import org.pshdl.model.parser.PSHDLLangParser.PsInterfaceContext;
import org.pshdl.model.parser.PSHDLLangParser.PsInterfaceDeclContext;
import org.pshdl.model.parser.PSHDLLangParser.PsInterfaceDeclarationContext;
import org.pshdl.model.parser.PSHDLLangParser.PsInterfaceInstantiationContext;
import org.pshdl.model.parser.PSHDLLangParser.PsManipContext;
import org.pshdl.model.parser.PSHDLLangParser.PsModelContext;
import org.pshdl.model.parser.PSHDLLangParser.PsMulContext;
import org.pshdl.model.parser.PSHDLLangParser.PsNativeFunctionContext;
import org.pshdl.model.parser.PSHDLLangParser.PsParensContext;
import org.pshdl.model.parser.PSHDLLangParser.PsPassedArgumentsContext;
import org.pshdl.model.parser.PSHDLLangParser.PsPortDeclarationContext;
import org.pshdl.model.parser.PSHDLLangParser.PsPrimitiveContext;
import org.pshdl.model.parser.PSHDLLangParser.PsPrimitiveTypeContext;
import org.pshdl.model.parser.PSHDLLangParser.PsProcessContext;
import org.pshdl.model.parser.PSHDLLangParser.PsQualifiedNameContext;
import org.pshdl.model.parser.PSHDLLangParser.PsQualifiedNameImportContext;
import org.pshdl.model.parser.PSHDLLangParser.PsRefPartContext;
import org.pshdl.model.parser.PSHDLLangParser.PsShiftContext;
import org.pshdl.model.parser.PSHDLLangParser.PsSimpleBlockContext;
import org.pshdl.model.parser.PSHDLLangParser.PsStatementContext;
import org.pshdl.model.parser.PSHDLLangParser.PsSubstituteFunctionContext;
import org.pshdl.model.parser.PSHDLLangParser.PsSwitchStatementContext;
import org.pshdl.model.parser.PSHDLLangParser.PsTernaryContext;
import org.pshdl.model.parser.PSHDLLangParser.PsTypeDeclarationContext;
import org.pshdl.model.parser.PSHDLLangParser.PsUnitContext;
import org.pshdl.model.parser.PSHDLLangParser.PsValueContext;
import org.pshdl.model.parser.PSHDLLangParser.PsValueExpContext;
import org.pshdl.model.parser.PSHDLLangParser.PsVariableContext;
import org.pshdl.model.parser.PSHDLLangParser.PsVariableDeclarationContext;
import org.pshdl.model.parser.PSHDLLangParser.PsVariableRefContext;
import org.pshdl.model.parser.PSHDLLangParser.PsWidthContext;
import org.pshdl.model.utils.*;

import com.google.common.base.Objects;

@SuppressWarnings("all")
public class ParserToModelExtension {
	private BufferedTokenStream tokens;

	public ParserToModelExtension(final BufferedTokenStream tokens) {
		this.tokens = tokens;
	}

	public static HDLPackage toHDL(final BufferedTokenStream tokens, final PsModelContext ctx, final String libURI, final String src) {
		ParserToModelExtension _parserToModelExtension = new ParserToModelExtension(tokens);
		return _parserToModelExtension.toHDLPkg(ctx, libURI, src);
	}

	public HDLPackage toHDLPkg(final PsModelContext ctx, final String libURI, final String src) {
		HDLPackage _hDLPackage = new HDLPackage();
		HDLPackage pkg = _hDLPackage.setLibURI(libURI);
		PsQualifiedNameContext _psQualifiedName = ctx.psQualifiedName();
		boolean _tripleNotEquals = (_psQualifiedName != null);
		if (_tripleNotEquals) {
			PsQualifiedNameContext _psQualifiedName_1 = ctx.psQualifiedName();
			String _name = this.toName(_psQualifiedName_1);
			HDLPackage _setPkg = pkg.setPkg(_name);
			pkg = _setPkg;
		}
		List<PsUnitContext> _psUnit = ctx.psUnit();
		final Function1<PsUnitContext, HDLUnit> _function = new Function1<PsUnitContext, HDLUnit>() {
			@Override
			public HDLUnit apply(final PsUnitContext it) {
				HDLUnit _hDLUnit = ParserToModelExtension.this.toHDLUnit(it, libURI);
				return _hDLUnit;
			}
		};
		List<HDLUnit> _map = ListExtensions.<PsUnitContext, HDLUnit> map(_psUnit, _function);
		HDLPackage _setUnits = pkg.setUnits(_map);
		pkg = _setUnits;
		List<PsDeclarationContext> _psDeclaration = ctx.psDeclaration();
		final Function1<PsDeclarationContext, HDLDeclaration> _function_1 = new Function1<PsDeclarationContext, HDLDeclaration>() {
			@Override
			public HDLDeclaration apply(final PsDeclarationContext it) {
				IHDLObject _hDL = ParserToModelExtension.this.toHDL(it);
				return ((HDLDeclaration) _hDL);
			}
		};
		List<HDLDeclaration> _map_1 = ListExtensions.<PsDeclarationContext, HDLDeclaration> map(_psDeclaration, _function_1);
		HDLPackage _setDeclarations = pkg.setDeclarations(_map_1);
		pkg = _setDeclarations;
		pkg.freeze(null);
		HDLLibrary _library = HDLLibrary.getLibrary(libURI);
		_library.addPkg(pkg, src);
		HDLPackage _attachContext = this.<HDLPackage> attachContext(pkg, ctx);
		return (_attachContext);
	}

	protected HDLDeclaration _toHDL(final PsDeclarationContext context) {
		PsDeclarationTypeContext _psDeclarationType = context.psDeclarationType();
		IHDLObject _hDL = this.toHDL(_psDeclarationType);
		HDLDeclaration res = ((HDLDeclaration) _hDL);
		List<PsAnnotationContext> _psAnnotation = context.psAnnotation();
		final Function1<PsAnnotationContext, HDLAnnotation> _function = new Function1<PsAnnotationContext, HDLAnnotation>() {
			@Override
			public HDLAnnotation apply(final PsAnnotationContext it) {
				IHDLObject _hDL = ParserToModelExtension.this.toHDL(it);
				return ((HDLAnnotation) _hDL);
			}
		};
		List<HDLAnnotation> _map = ListExtensions.<PsAnnotationContext, HDLAnnotation> map(_psAnnotation, _function);
		HDLDeclaration _setAnnotations = res.setAnnotations(_map);
		res = _setAnnotations;
		return this.<HDLDeclaration> attachContext(res, context);
	}

	public <T extends IHDLObject> T attachContext(final T obj, final ParserRuleContext context) {
		boolean _tripleEquals = (obj == null);
		if (_tripleEquals) {
			NullPointerException _nullPointerException = new NullPointerException("Null is not allowed");
			throw _nullPointerException;
		}
		SourceInfo _sourceInfo = new SourceInfo(this.tokens, context);
		obj.<SourceInfo> addMeta(SourceInfo.INFO, _sourceInfo);
		return obj;
	}

	protected HDLArgument _toHDL(final PsArgumentContext context) {
		HDLArgument _hDLArgument = new HDLArgument();
		TerminalNode _RULE_ID = context.RULE_ID();
		String _text = _RULE_ID.getText();
		HDLArgument res = _hDLArgument.setName(_text);
		PsExpressionContext _psExpression = context.psExpression();
		IHDLObject _hDL = this.toHDL(_psExpression);
		HDLArgument _setExpression = res.setExpression(((HDLExpression) _hDL));
		res = _setExpression;
		return this.<HDLArgument> attachContext(res, context);
	}

	protected HDLBlock _toHDL(final PsProcessContext context) {
		HDLBlock _hDLBlock = new HDLBlock();
		HDLBlock block = _hDLBlock;
		boolean _tripleNotEquals = (context.isProcess != null);
		if (_tripleNotEquals) {
			HDLBlock _setProcess = block.setProcess(true);
			block = _setProcess;
		}
		List<PsBlockContext> _psBlock = context.psBlock();
		final Function1<PsBlockContext, HDLStatement> _function = new Function1<PsBlockContext, HDLStatement>() {
			@Override
			public HDLStatement apply(final PsBlockContext it) {
				IHDLObject _hDL = ParserToModelExtension.this.toHDL(it);
				return ((HDLStatement) _hDL);
			}
		};
		List<HDLStatement> _map = ListExtensions.<PsBlockContext, HDLStatement> map(_psBlock, _function);
		HDLBlock _setStatements = block.setStatements(_map);
		block = _setStatements;
		return this.<HDLBlock> attachContext(block, context);
	}

	protected HDLAnnotation _toHDL(final PsAnnotationContext context) {
		PsAnnotationTypeContext _psAnnotationType = context.psAnnotationType();
		final String name = _psAnnotationType.getText();
		String value = null;
		TerminalNode _RULE_STRING = context.RULE_STRING();
		boolean _tripleNotEquals = (_RULE_STRING != null);
		if (_tripleNotEquals) {
			TerminalNode _RULE_STRING_1 = context.RULE_STRING();
			String str = _RULE_STRING_1.getText();
			int _length = str.length();
			int _minus = (_length - 1);
			String _substring = str.substring(1, _minus);
			str = _substring;
			value = str;
		}
		HDLAnnotation _hDLAnnotation = new HDLAnnotation();
		HDLAnnotation _setName = _hDLAnnotation.setName(name);
		HDLAnnotation _setValue = _setName.setValue(value);
		return this.<HDLAnnotation> attachContext(_setValue, context);
	}

	protected HDLDeclaration _toHDL(final PsDeclarationTypeContext context) {
		PsFunctionDeclarationContext _psFunctionDeclaration = context.psFunctionDeclaration();
		boolean _tripleNotEquals = (_psFunctionDeclaration != null);
		if (_tripleNotEquals) {
			PsFunctionDeclarationContext _psFunctionDeclaration_1 = context.psFunctionDeclaration();
			IHDLObject _hDL = this.toHDL(_psFunctionDeclaration_1);
			IHDLObject _attachContext = this.<IHDLObject> attachContext(_hDL, context);
			return ((HDLDeclaration) _attachContext);
		}
		PsTypeDeclarationContext _psTypeDeclaration = context.psTypeDeclaration();
		boolean _tripleNotEquals_1 = (_psTypeDeclaration != null);
		if (_tripleNotEquals_1) {
			PsTypeDeclarationContext _psTypeDeclaration_1 = context.psTypeDeclaration();
			IHDLObject _hDL_1 = this.toHDL(_psTypeDeclaration_1);
			IHDLObject _attachContext_1 = this.<IHDLObject> attachContext(_hDL_1, context);
			return ((HDLDeclaration) _attachContext_1);
		}
		PsVariableDeclarationContext _psVariableDeclaration = context.psVariableDeclaration();
		boolean _tripleNotEquals_2 = (_psVariableDeclaration != null);
		if (_tripleNotEquals_2) {
			PsVariableDeclarationContext _psVariableDeclaration_1 = context.psVariableDeclaration();
			IHDLObject _hDL_2 = this.toHDL(_psVariableDeclaration_1);
			IHDLObject _attachContext_2 = this.<IHDLObject> attachContext(_hDL_2, context);
			return ((HDLDeclaration) _attachContext_2);
		}
		Class<? extends PsDeclarationTypeContext> _class = context.getClass();
		String _plus = ("Not implemented:" + _class);
		IllegalArgumentException _illegalArgumentException = new IllegalArgumentException(_plus);
		throw _illegalArgumentException;
	}

	protected HDLVariableDeclaration _toHDL(final PsVariableDeclarationContext context) {
		HDLVariableDeclaration _hDLVariableDeclaration = new HDLVariableDeclaration();
		HDLVariableDeclaration res = _hDLVariableDeclaration;
		PsPrimitiveContext _psPrimitive = context.psPrimitive();
		IHDLObject _hDL = this.toHDL(_psPrimitive);
		HDLVariableDeclaration _setType = res.setType(((HDLType) _hDL));
		res = _setType;
		HDLDirection dir = HDLDirection.INTERNAL;
		PsDirectionContext _psDirection = context.psDirection();
		boolean _tripleNotEquals = (_psDirection != null);
		if (_tripleNotEquals) {
			PsDirectionContext _psDirection_1 = context.psDirection();
			String _text = _psDirection_1.getText();
			HDLDirection _op = HDLDirection.getOp(_text);
			dir = _op;
		}
		HDLVariableDeclaration _setDirection = res.setDirection(dir);
		res = _setDirection;
		List<PsDeclAssignmentContext> _psDeclAssignment = context.psDeclAssignment();
		for (final PsDeclAssignmentContext varDecl : _psDeclAssignment) {
			IHDLObject _hDL_1 = this.toHDL(varDecl);
			HDLVariableDeclaration _addVariables = res.addVariables(((HDLVariable) _hDL_1));
			res = _addVariables;
		}
		PsPrimitiveContext _psPrimitive_1 = context.psPrimitive();
		boolean _tripleNotEquals_1 = (_psPrimitive_1.isRegister != null);
		if (_tripleNotEquals_1) {
			ArrayList<HDLArgument> _arrayList = new ArrayList<HDLArgument>();
			Iterable<HDLArgument> args = _arrayList;
			PsPrimitiveContext _psPrimitive_2 = context.psPrimitive();
			PsPassedArgumentsContext _psPassedArguments = _psPrimitive_2.psPassedArguments();
			boolean _tripleNotEquals_2 = (_psPassedArguments != null);
			if (_tripleNotEquals_2) {
				PsPrimitiveContext _psPrimitive_3 = context.psPrimitive();
				PsPassedArgumentsContext _psPassedArguments_1 = _psPrimitive_3.psPassedArguments();
				List<PsArgumentContext> _psArgument = _psPassedArguments_1.psArgument();
				final Function1<PsArgumentContext, HDLArgument> _function = new Function1<PsArgumentContext, HDLArgument>() {
					@Override
					public HDLArgument apply(final PsArgumentContext it) {
						IHDLObject _hDL = ParserToModelExtension.this.toHDL(it);
						return ((HDLArgument) _hDL);
					}
				};
				List<HDLArgument> _map = ListExtensions.<PsArgumentContext, HDLArgument> map(_psArgument, _function);
				args = _map;
			}
			HDLRegisterConfig _fromArgs = HDLRegisterConfig.fromArgs(args);
			HDLVariableDeclaration _setRegister = res.setRegister(_fromArgs);
			res = _setRegister;
		}
		return this.<HDLVariableDeclaration> attachContext(res, context);
	}

	protected IHDLObject _toHDL(final PsArrayInitContext context) {
		PsExpressionContext _psExpression = context.psExpression();
		boolean _tripleNotEquals = (_psExpression != null);
		if (_tripleNotEquals) {
			PsExpressionContext _psExpression_1 = context.psExpression();
			IHDLObject _hDL = this.toHDL(_psExpression_1);
			return this.<IHDLObject> attachContext(_hDL, context);
		}
		HDLArrayInit _hDLArrayInit = new HDLArrayInit();
		List<PsArrayInitSubContext> _psArrayInitSub = context.psArrayInitSub();
		final Function1<PsArrayInitSubContext, HDLExpression> _function = new Function1<PsArrayInitSubContext, HDLExpression>() {
			@Override
			public HDLExpression apply(final PsArrayInitSubContext it) {
				IHDLObject _hDL = ParserToModelExtension.this.toHDL(it);
				return ((HDLExpression) _hDL);
			}
		};
		List<HDLExpression> _map = ListExtensions.<PsArrayInitSubContext, HDLExpression> map(_psArrayInitSub, _function);
		final HDLArrayInit arr = _hDLArrayInit.setExp(_map);
		return this.<HDLArrayInit> attachContext(arr, context);
	}

	protected IHDLObject _toHDL(final PsArrayInitSubContext context) {
		List<PsExpressionContext> _psExpression = context.psExpression();
		boolean _tripleNotEquals = (_psExpression != null);
		if (_tripleNotEquals) {
			List<PsExpressionContext> _psExpression_1 = context.psExpression();
			int _size = _psExpression_1.size();
			boolean _equals = (_size == 1);
			if (_equals) {
				List<PsExpressionContext> _psExpression_2 = context.psExpression();
				PsExpressionContext _get = _psExpression_2.get(0);
				IHDLObject _hDL = this.toHDL(_get);
				return this.<IHDLObject> attachContext(_hDL, context);
			}
			HDLArrayInit _hDLArrayInit = new HDLArrayInit();
			List<PsExpressionContext> _psExpression_3 = context.psExpression();
			final Function1<PsExpressionContext, HDLExpression> _function = new Function1<PsExpressionContext, HDLExpression>() {
				@Override
				public HDLExpression apply(final PsExpressionContext it) {
					IHDLObject _hDL = ParserToModelExtension.this.toHDL(it);
					return ((HDLExpression) _hDL);
				}
			};
			List<HDLExpression> _map = ListExtensions.<PsExpressionContext, HDLExpression> map(_psExpression_3, _function);
			final HDLArrayInit arr = _hDLArrayInit.setExp(_map);
			return this.<HDLArrayInit> attachContext(arr, context);
		}
		HDLArrayInit _hDLArrayInit_1 = new HDLArrayInit();
		List<PsArrayInitSubContext> _psArrayInitSub = context.psArrayInitSub();
		final Function1<PsArrayInitSubContext, HDLExpression> _function_1 = new Function1<PsArrayInitSubContext, HDLExpression>() {
			@Override
			public HDLExpression apply(final PsArrayInitSubContext it) {
				IHDLObject _hDL = ParserToModelExtension.this.toHDL(it);
				return ((HDLExpression) _hDL);
			}
		};
		List<HDLExpression> _map_1 = ListExtensions.<PsArrayInitSubContext, HDLExpression> map(_psArrayInitSub, _function_1);
		final HDLArrayInit arr_1 = _hDLArrayInit_1.setExp(_map_1);
		return this.<HDLArrayInit> attachContext(arr_1, context);
	}

	protected HDLType _toHDL(final PsPrimitiveContext context) {
		PsQualifiedNameContext _psQualifiedName = context.psQualifiedName();
		boolean _tripleNotEquals = (_psQualifiedName != null);
		if (_tripleNotEquals) {
			HDLEnum _hDLEnum = new HDLEnum();
			PsQualifiedNameContext _psQualifiedName_1 = context.psQualifiedName();
			String _name = this.toName(_psQualifiedName_1);
			HDLEnum _setName = _hDLEnum.setName(_name);
			return this.<HDLEnum> attachContext(_setName, context);
		}
		PsPrimitiveTypeContext _psPrimitiveType = context.psPrimitiveType();
		String _text = _psPrimitiveType.getText();
		String _upperCase = _text.toUpperCase();
		final HDLPrimitiveType pt = HDLPrimitiveType.valueOf(_upperCase);
		PsWidthContext _psWidth = context.psWidth();
		IHDLObject _hDL = _psWidth == null ? (IHDLObject) null : this.toHDL(_psWidth);
		final HDLExpression width = ((HDLExpression) _hDL);
		HDLPrimitive _hDLPrimitive = new HDLPrimitive();
		HDLPrimitiveType _resultingType = this.getResultingType(pt, width);
		HDLPrimitive _setType = _hDLPrimitive.setType(_resultingType);
		HDLPrimitive _setWidth = _setType.setWidth(width);
		return this.<HDLPrimitive> attachContext(_setWidth, context);
	}

	protected HDLVariable _toHDL(final PsDeclAssignmentContext context) {
		HDLVariable _hDLVariable = new HDLVariable();
		PsVariableContext _psVariable = context.psVariable();
		String _name = this.toName(_psVariable);
		HDLVariable res = _hDLVariable.setName(_name);
		List<PsAnnotationContext> _psAnnotation = context.psAnnotation();
		final Function1<PsAnnotationContext, HDLAnnotation> _function = new Function1<PsAnnotationContext, HDLAnnotation>() {
			@Override
			public HDLAnnotation apply(final PsAnnotationContext it) {
				IHDLObject _hDL = ParserToModelExtension.this.toHDL(it);
				return ((HDLAnnotation) _hDL);
			}
		};
		List<HDLAnnotation> _map = ListExtensions.<PsAnnotationContext, HDLAnnotation> map(_psAnnotation, _function);
		HDLVariable _setAnnotations = res.setAnnotations(_map);
		res = _setAnnotations;
		PsArrayContext _psArray = context.psArray();
		boolean _tripleNotEquals = (_psArray != null);
		if (_tripleNotEquals) {
			PsArrayContext _psArray_1 = context.psArray();
			List<PsExpressionContext> _psExpression = _psArray_1.psExpression();
			final Function1<PsExpressionContext, HDLExpression> _function_1 = new Function1<PsExpressionContext, HDLExpression>() {
				@Override
				public HDLExpression apply(final PsExpressionContext it) {
					IHDLObject _hDL = ParserToModelExtension.this.toHDL(it);
					return ((HDLExpression) _hDL);
				}
			};
			List<HDLExpression> _map_1 = ListExtensions.<PsExpressionContext, HDLExpression> map(_psExpression, _function_1);
			HDLVariable _setDimensions = res.setDimensions(_map_1);
			res = _setDimensions;
		}
		PsArrayInitContext _psArrayInit = context.psArrayInit();
		boolean _tripleNotEquals_1 = (_psArrayInit != null);
		if (_tripleNotEquals_1) {
			PsArrayInitContext _psArrayInit_1 = context.psArrayInit();
			IHDLObject _hDL = this.toHDL(_psArrayInit_1);
			HDLVariable _setDefaultValue = res.setDefaultValue(((HDLExpression) _hDL));
			res = _setDefaultValue;
		}
		return this.<HDLVariable> attachContext(res, context);
	}

	public String toName(final PsVariableContext context) {
		TerminalNode _RULE_ID = context.RULE_ID();
		return _RULE_ID.getText();
	}

	public HDLPrimitiveType getResultingType(final HDLPrimitiveType pt, final HDLExpression width) {
		boolean _tripleNotEquals = (width != null);
		if (_tripleNotEquals) {
			boolean _matched = false;
			if (!_matched) {
				if (Objects.equal(pt, HDLPrimitiveType.BIT)) {
					_matched = true;
					return HDLPrimitiveType.BITVECTOR;
				}
			}
		} else {
			boolean _matched_1 = false;
			if (!_matched_1) {
				if (Objects.equal(pt, HDLPrimitiveType.INT)) {
					_matched_1 = true;
					return HDLPrimitiveType.INTEGER;
				}
			}
			if (!_matched_1) {
				if (Objects.equal(pt, HDLPrimitiveType.UINT)) {
					_matched_1 = true;
					return HDLPrimitiveType.NATURAL;
				}
			}
		}
		return pt;
	}

	protected IHDLObject _toHDL(final PsWidthContext context) {
		PsExpressionContext _psExpression = context.psExpression();
		IHDLObject _hDL = this.toHDL(_psExpression);
		return this.<IHDLObject> attachContext(_hDL, context);
	}

	protected IHDLObject _toHDL(final PsValueContext context) {
		TerminalNode _RULE_PS_LITERAL_TERMINAL = context.RULE_PS_LITERAL_TERMINAL();
		boolean _tripleNotEquals = (_RULE_PS_LITERAL_TERMINAL != null);
		if (_tripleNotEquals) {
			HDLLiteral _hDLLiteral = new HDLLiteral();
			HDLLiteral _setStr = _hDLLiteral.setStr(false);
			TerminalNode _RULE_PS_LITERAL_TERMINAL_1 = context.RULE_PS_LITERAL_TERMINAL();
			String _text = _RULE_PS_LITERAL_TERMINAL_1.getText();
			HDLLiteral _setVal = _setStr.setVal(_text);
			return this.<HDLLiteral> attachContext(_setVal, context);
		}
		TerminalNode _RULE_STRING = context.RULE_STRING();
		boolean _tripleNotEquals_1 = (_RULE_STRING != null);
		if (_tripleNotEquals_1) {
			TerminalNode _RULE_STRING_1 = context.RULE_STRING();
			String str = _RULE_STRING_1.getText();
			int _length = str.length();
			int _minus = (_length - 1);
			String _substring = str.substring(1, _minus);
			str = _substring;
			HDLLiteral _hDLLiteral_1 = new HDLLiteral();
			HDLLiteral _setStr_1 = _hDLLiteral_1.setStr(true);
			HDLLiteral _setVal_1 = _setStr_1.setVal(str);
			return this.<HDLLiteral> attachContext(_setVal_1, context);
		}
		PsVariableRefContext _psVariableRef = context.psVariableRef();
		boolean _tripleNotEquals_2 = (_psVariableRef != null);
		if (_tripleNotEquals_2) {
			PsVariableRefContext _psVariableRef_1 = context.psVariableRef();
			IHDLObject _hDL = this.toHDL(_psVariableRef_1);
			return this.<IHDLObject> attachContext(_hDL, context);
		}
		Class<? extends PsValueContext> _class = context.getClass();
		String _plus = ("Not correctly implemented:" + _class);
		IllegalArgumentException _illegalArgumentException = new IllegalArgumentException(_plus);
		throw _illegalArgumentException;
	}

	protected IHDLObject _toHDL(final PsValueExpContext context) {
		PsValueContext _psValue = context.psValue();
		IHDLObject _hDL = this.toHDL(_psValue);
		return this.<IHDLObject> attachContext(_hDL, context);
	}

	protected HDLConcat _toHDL(final PsConcatContext context) {
		HDLConcat _hDLConcat = new HDLConcat();
		HDLConcat cat = _hDLConcat;
		List<PsExpressionContext> _psExpression = context.psExpression();
		final Function1<PsExpressionContext, HDLExpression> _function = new Function1<PsExpressionContext, HDLExpression>() {
			@Override
			public HDLExpression apply(final PsExpressionContext it) {
				IHDLObject _hDL = ParserToModelExtension.this.toHDL(it);
				return ((HDLExpression) _hDL);
			}
		};
		List<HDLExpression> _map = ListExtensions.<PsExpressionContext, HDLExpression> map(_psExpression, _function);
		HDLConcat _setCats = cat.setCats(_map);
		cat = _setCats;
		return this.<HDLConcat> attachContext(cat, context);
	}

	protected HDLBitOp _toHDL(final PsBitLogOrContext context) {
		HDLBitOp _hDLBitOp = new HDLBitOp();
		HDLBitOp res = _hDLBitOp.setType(HDLBitOpType.LOGI_OR);
		PsExpressionContext _psExpression = context.psExpression(0);
		IHDLObject _hDL = this.toHDL(_psExpression);
		HDLBitOp _setLeft = res.setLeft(((HDLExpression) _hDL));
		res = _setLeft;
		PsExpressionContext _psExpression_1 = context.psExpression(1);
		IHDLObject _hDL_1 = this.toHDL(_psExpression_1);
		HDLBitOp _setRight = res.setRight(((HDLExpression) _hDL_1));
		res = _setRight;
		return this.<HDLBitOp> attachContext(res, context);
	}

	protected HDLBitOp _toHDL(final PsBitLogAndContext context) {
		HDLBitOp _hDLBitOp = new HDLBitOp();
		HDLBitOp res = _hDLBitOp.setType(HDLBitOpType.LOGI_AND);
		PsExpressionContext _psExpression = context.psExpression(0);
		IHDLObject _hDL = this.toHDL(_psExpression);
		HDLBitOp _setLeft = res.setLeft(((HDLExpression) _hDL));
		res = _setLeft;
		PsExpressionContext _psExpression_1 = context.psExpression(1);
		IHDLObject _hDL_1 = this.toHDL(_psExpression_1);
		HDLBitOp _setRight = res.setRight(((HDLExpression) _hDL_1));
		res = _setRight;
		return this.<HDLBitOp> attachContext(res, context);
	}

	protected HDLBitOp _toHDL(final PsBitXorContext context) {
		HDLBitOp _hDLBitOp = new HDLBitOp();
		HDLBitOp res = _hDLBitOp.setType(HDLBitOpType.XOR);
		PsExpressionContext _psExpression = context.psExpression(0);
		IHDLObject _hDL = this.toHDL(_psExpression);
		HDLBitOp _setLeft = res.setLeft(((HDLExpression) _hDL));
		res = _setLeft;
		PsExpressionContext _psExpression_1 = context.psExpression(1);
		IHDLObject _hDL_1 = this.toHDL(_psExpression_1);
		HDLBitOp _setRight = res.setRight(((HDLExpression) _hDL_1));
		res = _setRight;
		return this.<HDLBitOp> attachContext(res, context);
	}

	protected HDLBitOp _toHDL(final PsBitOrContext context) {
		HDLBitOp _hDLBitOp = new HDLBitOp();
		HDLBitOp res = _hDLBitOp.setType(HDLBitOpType.OR);
		PsExpressionContext _psExpression = context.psExpression(0);
		IHDLObject _hDL = this.toHDL(_psExpression);
		HDLBitOp _setLeft = res.setLeft(((HDLExpression) _hDL));
		res = _setLeft;
		PsExpressionContext _psExpression_1 = context.psExpression(1);
		IHDLObject _hDL_1 = this.toHDL(_psExpression_1);
		HDLBitOp _setRight = res.setRight(((HDLExpression) _hDL_1));
		res = _setRight;
		return this.<HDLBitOp> attachContext(res, context);
	}

	protected HDLBitOp _toHDL(final PsBitAndContext context) {
		HDLBitOp _hDLBitOp = new HDLBitOp();
		HDLBitOp res = _hDLBitOp.setType(HDLBitOpType.AND);
		PsExpressionContext _psExpression = context.psExpression(0);
		IHDLObject _hDL = this.toHDL(_psExpression);
		HDLBitOp _setLeft = res.setLeft(((HDLExpression) _hDL));
		res = _setLeft;
		PsExpressionContext _psExpression_1 = context.psExpression(1);
		IHDLObject _hDL_1 = this.toHDL(_psExpression_1);
		HDLBitOp _setRight = res.setRight(((HDLExpression) _hDL_1));
		res = _setRight;
		return this.<HDLBitOp> attachContext(res, context);
	}

	protected HDLShiftOp _toHDL(final PsShiftContext context) {
		String _text = context.op.getText();
		final HDLShiftOpType type = HDLShiftOpType.getOp(_text);
		HDLShiftOp _hDLShiftOp = new HDLShiftOp();
		HDLShiftOp res = _hDLShiftOp.setType(type);
		PsExpressionContext _psExpression = context.psExpression(0);
		IHDLObject _hDL = this.toHDL(_psExpression);
		HDLShiftOp _setLeft = res.setLeft(((HDLExpression) _hDL));
		res = _setLeft;
		PsExpressionContext _psExpression_1 = context.psExpression(1);
		IHDLObject _hDL_1 = this.toHDL(_psExpression_1);
		HDLShiftOp _setRight = res.setRight(((HDLExpression) _hDL_1));
		res = _setRight;
		return this.<HDLShiftOp> attachContext(res, context);
	}

	protected HDLEqualityOp _toHDL(final PsEqualityCompContext context) {
		String _text = context.op.getText();
		final HDLEqualityOpType type = HDLEqualityOpType.getOp(_text);
		HDLEqualityOp _hDLEqualityOp = new HDLEqualityOp();
		HDLEqualityOp res = _hDLEqualityOp.setType(type);
		PsExpressionContext _psExpression = context.psExpression(0);
		IHDLObject _hDL = this.toHDL(_psExpression);
		HDLEqualityOp _setLeft = res.setLeft(((HDLExpression) _hDL));
		res = _setLeft;
		PsExpressionContext _psExpression_1 = context.psExpression(1);
		IHDLObject _hDL_1 = this.toHDL(_psExpression_1);
		HDLEqualityOp _setRight = res.setRight(((HDLExpression) _hDL_1));
		res = _setRight;
		return this.<HDLEqualityOp> attachContext(res, context);
	}

	protected HDLEqualityOp _toHDL(final PsEqualityContext context) {
		String _text = context.op.getText();
		final HDLEqualityOpType type = HDLEqualityOpType.getOp(_text);
		HDLEqualityOp _hDLEqualityOp = new HDLEqualityOp();
		HDLEqualityOp res = _hDLEqualityOp.setType(type);
		PsExpressionContext _psExpression = context.psExpression(0);
		IHDLObject _hDL = this.toHDL(_psExpression);
		HDLEqualityOp _setLeft = res.setLeft(((HDLExpression) _hDL));
		res = _setLeft;
		PsExpressionContext _psExpression_1 = context.psExpression(1);
		IHDLObject _hDL_1 = this.toHDL(_psExpression_1);
		HDLEqualityOp _setRight = res.setRight(((HDLExpression) _hDL_1));
		res = _setRight;
		return this.<HDLEqualityOp> attachContext(res, context);
	}

	protected HDLArithOp _toHDL(final PsMulContext context) {
		String _text = context.op.getText();
		final HDLArithOpType type = HDLArithOpType.getOp(_text);
		HDLArithOp _hDLArithOp = new HDLArithOp();
		HDLArithOp res = _hDLArithOp.setType(type);
		PsExpressionContext _psExpression = context.psExpression(0);
		IHDLObject _hDL = this.toHDL(_psExpression);
		HDLArithOp _setLeft = res.setLeft(((HDLExpression) _hDL));
		res = _setLeft;
		PsExpressionContext _psExpression_1 = context.psExpression(1);
		IHDLObject _hDL_1 = this.toHDL(_psExpression_1);
		HDLArithOp _setRight = res.setRight(((HDLExpression) _hDL_1));
		res = _setRight;
		return this.<HDLArithOp> attachContext(res, context);
	}

	protected HDLArithOp _toHDL(final PsAddContext context) {
		String _text = context.op.getText();
		final HDLArithOpType type = HDLArithOpType.getOp(_text);
		HDLArithOp _hDLArithOp = new HDLArithOp();
		HDLArithOp res = _hDLArithOp.setType(type);
		PsExpressionContext _psExpression = context.psExpression(0);
		IHDLObject _hDL = this.toHDL(_psExpression);
		HDLArithOp _setLeft = res.setLeft(((HDLExpression) _hDL));
		res = _setLeft;
		PsExpressionContext _psExpression_1 = context.psExpression(1);
		IHDLObject _hDL_1 = this.toHDL(_psExpression_1);
		HDLArithOp _setRight = res.setRight(((HDLExpression) _hDL_1));
		res = _setRight;
		return this.<HDLArithOp> attachContext(res, context);
	}

	protected HDLPrimitive _toHDL(final PsCastContext context) {
		PsPrimitiveTypeContext _psPrimitiveType = context.psPrimitiveType();
		String _text = _psPrimitiveType.getText();
		String _upperCase = _text.toUpperCase();
		final HDLPrimitiveType pt = HDLPrimitiveType.valueOf(_upperCase);
		PsWidthContext _psWidth = context.psWidth();
		IHDLObject _hDL = _psWidth == null ? (IHDLObject) null : this.toHDL(_psWidth);
		final HDLExpression width = ((HDLExpression) _hDL);
		HDLPrimitive _hDLPrimitive = new HDLPrimitive();
		HDLPrimitiveType _resultingType = this.getResultingType(pt, width);
		HDLPrimitive _setType = _hDLPrimitive.setType(_resultingType);
		HDLPrimitive _setWidth = _setType.setWidth(width);
		return this.<HDLPrimitive> attachContext(_setWidth, context);
	}

	protected HDLManip _toHDL(final PsManipContext context) {
		HDLManip _hDLManip = new HDLManip();
		PsExpressionContext _psExpression = context.psExpression();
		IHDLObject _hDL = this.toHDL(_psExpression);
		HDLManip res = _hDLManip.setTarget(((HDLExpression) _hDL));
		PsCastContext _psCast = context.psCast();
		boolean _tripleNotEquals = (_psCast != null);
		if (_tripleNotEquals) {
			HDLManip _setType = res.setType(HDLManipType.CAST);
			res = _setType;
			PsCastContext _psCast_1 = context.psCast();
			IHDLObject _hDL_1 = this.toHDL(_psCast_1);
			HDLManip _setCastTo = res.setCastTo(((HDLType) _hDL_1));
			res = _setCastTo;
		} else {
			int _type = context.type.getType();
			final int _switchValue = _type;
			boolean _matched = false;
			if (!_matched) {
				if (Objects.equal(_switchValue, PSHDLLangLexer.LOGIC_NEG)) {
					_matched = true;
					HDLManip _setType_1 = res.setType(HDLManipType.LOGIC_NEG);
					res = _setType_1;
				}
			}
			if (!_matched) {
				if (Objects.equal(_switchValue, PSHDLLangLexer.ARITH_NEG)) {
					_matched = true;
					HDLManip _setType_2 = res.setType(HDLManipType.ARITH_NEG);
					res = _setType_2;
				}
			}
			if (!_matched) {
				if (Objects.equal(_switchValue, PSHDLLangLexer.BIT_NEG)) {
					_matched = true;
					HDLManip _setType_3 = res.setType(HDLManipType.BIT_NEG);
					res = _setType_3;
				}
			}
		}
		return this.<HDLManip> attachContext(res, context);
	}

	protected HDLTernary _toHDL(final PsTernaryContext context) {
		HDLTernary _hDLTernary = new HDLTernary();
		PsExpressionContext _psExpression = context.psExpression(0);
		IHDLObject _hDL = this.toHDL(_psExpression);
		HDLTernary res = _hDLTernary.setIfExpr(((HDLExpression) _hDL));
		PsExpressionContext _psExpression_1 = context.psExpression(1);
		IHDLObject _hDL_1 = this.toHDL(_psExpression_1);
		HDLTernary _setThenExpr = res.setThenExpr(((HDLExpression) _hDL_1));
		res = _setThenExpr;
		PsExpressionContext _psExpression_2 = context.psExpression(2);
		IHDLObject _hDL_2 = this.toHDL(_psExpression_2);
		HDLTernary _setElseExpr = res.setElseExpr(((HDLExpression) _hDL_2));
		res = _setElseExpr;
		return this.<HDLTernary> attachContext(res, context);
	}

	protected IHDLObject _toHDL(final PsParensContext context) {
		PsExpressionContext _psExpression = context.psExpression();
		IHDLObject _hDL = this.toHDL(_psExpression);
		return this.<IHDLObject> attachContext(_hDL, context);
	}

	protected HDLExpression _toHDL(final PsExpressionContext context) {
		Class<? extends PsExpressionContext> _class = context.getClass();
		String _plus = ("Not implemented:" + _class);
		IllegalArgumentException _illegalArgumentException = new IllegalArgumentException(_plus);
		throw _illegalArgumentException;
	}

	public String toName(final PsEnumContext context) {
		PsQualifiedNameContext _psQualifiedName = context.psQualifiedName();
		return this.toName(_psQualifiedName);
	}

	public String toName(final PsInterfaceContext context) {
		PsQualifiedNameContext _psQualifiedName = context.psQualifiedName();
		return this.toName(_psQualifiedName);
	}

	public HDLQualifiedName toFQNName(final PsQualifiedNameContext context) {
		String _text = context.getText();
		HDLQualifiedName _hDLQualifiedName = new HDLQualifiedName(_text);
		return _hDLQualifiedName;
	}

	public String toName(final PsQualifiedNameContext context) {
		String _text = context.getText();
		HDLQualifiedName _hDLQualifiedName = new HDLQualifiedName(_text);
		return _hDLQualifiedName.toString();
	}

	protected IHDLObject _toHDL(final PsTypeDeclarationContext context) {
		PsEnumDeclarationContext _psEnumDeclaration = context.psEnumDeclaration();
		boolean _tripleNotEquals = (_psEnumDeclaration != null);
		if (_tripleNotEquals) {
			PsEnumDeclarationContext _psEnumDeclaration_1 = context.psEnumDeclaration();
			IHDLObject _hDL = this.toHDL(_psEnumDeclaration_1);
			return this.<IHDLObject> attachContext(_hDL, context);
		}
		PsInterfaceDeclarationContext _psInterfaceDeclaration = context.psInterfaceDeclaration();
		boolean _tripleNotEquals_1 = (_psInterfaceDeclaration != null);
		if (_tripleNotEquals_1) {
			PsInterfaceDeclarationContext _psInterfaceDeclaration_1 = context.psInterfaceDeclaration();
			IHDLObject _hDL_1 = this.toHDL(_psInterfaceDeclaration_1);
			return this.<IHDLObject> attachContext(_hDL_1, context);
		}
		Class<? extends PsTypeDeclarationContext> _class = context.getClass();
		String _plus = ("Not implemented:" + _class);
		IllegalArgumentException _illegalArgumentException = new IllegalArgumentException(_plus);
		throw _illegalArgumentException;
	}

	protected HDLInterfaceDeclaration _toHDL(final PsInterfaceDeclarationContext context) {
		HDLInterface _hDLInterface = new HDLInterface();
		PsInterfaceContext _psInterface = context.psInterface();
		String _name = this.toName(_psInterface);
		HDLInterface hIf = _hDLInterface.setName(_name);
		PsInterfaceDeclContext _psInterfaceDecl = context.psInterfaceDecl();
		List<PsPortDeclarationContext> _psPortDeclaration = _psInterfaceDecl.psPortDeclaration();
		final Function1<PsPortDeclarationContext, HDLVariableDeclaration> _function = new Function1<PsPortDeclarationContext, HDLVariableDeclaration>() {
			@Override
			public HDLVariableDeclaration apply(final PsPortDeclarationContext it) {
				IHDLObject _hDL = ParserToModelExtension.this.toHDL(it);
				return ((HDLVariableDeclaration) _hDL);
			}
		};
		List<HDLVariableDeclaration> _map = ListExtensions.<PsPortDeclarationContext, HDLVariableDeclaration> map(_psPortDeclaration, _function);
		HDLInterface _setPorts = hIf.setPorts(_map);
		hIf = _setPorts;
		HDLInterfaceDeclaration _hDLInterfaceDeclaration = new HDLInterfaceDeclaration();
		HDLInterfaceDeclaration _setHIf = _hDLInterfaceDeclaration.setHIf(hIf);
		return this.<HDLInterfaceDeclaration> attachContext(_setHIf, context);
	}

	protected HDLVariableDeclaration _toHDL(final PsPortDeclarationContext context) {
		PsVariableDeclarationContext _psVariableDeclaration = context.psVariableDeclaration();
		IHDLObject _hDL = this.toHDL(_psVariableDeclaration);
		HDLVariableDeclaration res = ((HDLVariableDeclaration) _hDL);
		List<PsAnnotationContext> _psAnnotation = context.psAnnotation();
		final Function1<PsAnnotationContext, HDLAnnotation> _function = new Function1<PsAnnotationContext, HDLAnnotation>() {
			@Override
			public HDLAnnotation apply(final PsAnnotationContext it) {
				IHDLObject _hDL = ParserToModelExtension.this.toHDL(it);
				return ((HDLAnnotation) _hDL);
			}
		};
		List<HDLAnnotation> _map = ListExtensions.<PsAnnotationContext, HDLAnnotation> map(_psAnnotation, _function);
		HDLVariableDeclaration _setAnnotations = res.setAnnotations(_map);
		res = _setAnnotations;
		return this.<HDLVariableDeclaration> attachContext(res, context);
	}

	protected IHDLObject _toHDL(final PsBlockContext context) {
		PsDeclarationContext _psDeclaration = context.psDeclaration();
		boolean _tripleNotEquals = (_psDeclaration != null);
		if (_tripleNotEquals) {
			PsDeclarationContext _psDeclaration_1 = context.psDeclaration();
			IHDLObject _hDL = this.toHDL(_psDeclaration_1);
			return this.<IHDLObject> attachContext(_hDL, context);
		}
		PsInstantiationContext _psInstantiation = context.psInstantiation();
		boolean _tripleNotEquals_1 = (_psInstantiation != null);
		if (_tripleNotEquals_1) {
			PsInstantiationContext _psInstantiation_1 = context.psInstantiation();
			IHDLObject _hDL_1 = this.toHDL(_psInstantiation_1);
			return this.<IHDLObject> attachContext(_hDL_1, context);
		}
		PsStatementContext _psStatement = context.psStatement();
		boolean _tripleNotEquals_2 = (_psStatement != null);
		if (_tripleNotEquals_2) {
			PsStatementContext _psStatement_1 = context.psStatement();
			IHDLObject _hDL_2 = this.toHDL(_psStatement_1);
			return this.<IHDLObject> attachContext(_hDL_2, context);
		}
		Class<? extends PsBlockContext> _class = context.getClass();
		String _plus = ("Not correctly implemented type:" + _class);
		IllegalArgumentException _illegalArgumentException = new IllegalArgumentException(_plus);
		throw _illegalArgumentException;
	}

	protected HDLDirectGeneration _toHDL(final PsDirectGenerationContext context) {
		HDLDirectGeneration _hDLDirectGeneration = new HDLDirectGeneration();
		HDLDirectGeneration gen = _hDLDirectGeneration.setGeneratorContent("");
		boolean _tripleNotEquals = (context.isInclude != null);
		HDLDirectGeneration _setInclude = gen.setInclude(_tripleNotEquals);
		gen = _setInclude;
		PsInterfaceContext _psInterface = context.psInterface();
		IHDLObject _hDL = this.toHDL(_psInterface);
		HDLDirectGeneration _setHIf = gen.setHIf(((HDLInterface) _hDL));
		gen = _setHIf;
		PsVariableContext _psVariable = context.psVariable();
		IHDLObject _hDL_1 = this.toHDL(_psVariable);
		HDLDirectGeneration _setVar = gen.setVar(((HDLVariable) _hDL_1));
		gen = _setVar;
		TerminalNode _RULE_ID = context.RULE_ID();
		String _text = _RULE_ID.getText();
		HDLDirectGeneration _setGeneratorID = gen.setGeneratorID(_text);
		gen = _setGeneratorID;
		PsPassedArgumentsContext _psPassedArguments = context.psPassedArguments();
		boolean _tripleNotEquals_1 = (_psPassedArguments != null);
		if (_tripleNotEquals_1) {
			PsPassedArgumentsContext _psPassedArguments_1 = context.psPassedArguments();
			List<PsArgumentContext> _psArgument = _psPassedArguments_1.psArgument();
			final Function1<PsArgumentContext, HDLArgument> _function = new Function1<PsArgumentContext, HDLArgument>() {
				@Override
				public HDLArgument apply(final PsArgumentContext it) {
					IHDLObject _hDL = ParserToModelExtension.this.toHDL(it);
					return ((HDLArgument) _hDL);
				}
			};
			List<HDLArgument> _map = ListExtensions.<PsArgumentContext, HDLArgument> map(_psArgument, _function);
			HDLDirectGeneration _setArguments = gen.setArguments(_map);
			gen = _setArguments;
		}
		TerminalNode _RULE_GENERATOR_CONTENT = context.RULE_GENERATOR_CONTENT();
		boolean _tripleNotEquals_2 = (_RULE_GENERATOR_CONTENT != null);
		if (_tripleNotEquals_2) {
			TerminalNode _RULE_GENERATOR_CONTENT_1 = context.RULE_GENERATOR_CONTENT();
			String _text_1 = _RULE_GENERATOR_CONTENT_1.getText();
			HDLDirectGeneration _setGeneratorContent = gen.setGeneratorContent(_text_1);
			gen = _setGeneratorContent;
		}
		return this.<HDLDirectGeneration> attachContext(gen, context);
	}

	protected HDLVariable _toHDL(final PsVariableContext context) {
		HDLVariable _hDLVariable = new HDLVariable();
		String _name = this.toName(context);
		HDLVariable _setName = _hDLVariable.setName(_name);
		return this.<HDLVariable> attachContext(_setName, context);
	}

	protected HDLInterface _toHDL(final PsInterfaceContext context) {
		HDLInterface _hDLInterface = new HDLInterface();
		String _name = this.toName(context);
		HDLInterface _setName = _hDLInterface.setName(_name);
		return this.<HDLInterface> attachContext(_setName, context);
	}

	protected IHDLObject _toHDL(final PsInstantiationContext context) {
		PsDirectGenerationContext _psDirectGeneration = context.psDirectGeneration();
		boolean _tripleNotEquals = (_psDirectGeneration != null);
		if (_tripleNotEquals) {
			PsDirectGenerationContext _psDirectGeneration_1 = context.psDirectGeneration();
			IHDLObject _hDL = this.toHDL(_psDirectGeneration_1);
			return this.<IHDLObject> attachContext(_hDL, context);
		}
		PsInterfaceInstantiationContext _psInterfaceInstantiation = context.psInterfaceInstantiation();
		boolean _tripleNotEquals_1 = (_psInterfaceInstantiation != null);
		if (_tripleNotEquals_1) {
			PsInterfaceInstantiationContext _psInterfaceInstantiation_1 = context.psInterfaceInstantiation();
			IHDLObject _hDL_1 = this.toHDL(_psInterfaceInstantiation_1);
			return this.<IHDLObject> attachContext(_hDL_1, context);
		}
		Class<? extends PsInstantiationContext> _class = context.getClass();
		String _plus = ("Not implemented type:" + _class);
		IllegalArgumentException _illegalArgumentException = new IllegalArgumentException(_plus);
		throw _illegalArgumentException;
	}

	protected HDLEnum _toHDL(final PsEnumContext context) {
		HDLEnum _hDLEnum = new HDLEnum();
		String _name = this.toName(context);
		HDLEnum _setName = _hDLEnum.setName(_name);
		return this.<HDLEnum> attachContext(_setName, context);
	}

	protected HDLEnumDeclaration _toHDL(final PsEnumDeclarationContext context) {
		PsEnumContext _psEnum = context.psEnum();
		IHDLObject _hDL = this.toHDL(_psEnum);
		HDLEnum he = ((HDLEnum) _hDL);
		List<PsVariableContext> _psVariable = context.psVariable();
		final Function1<PsVariableContext, HDLVariable> _function = new Function1<PsVariableContext, HDLVariable>() {
			@Override
			public HDLVariable apply(final PsVariableContext it) {
				IHDLObject _hDL = ParserToModelExtension.this.toHDL(it);
				return ((HDLVariable) _hDL);
			}
		};
		List<HDLVariable> _map = ListExtensions.<PsVariableContext, HDLVariable> map(_psVariable, _function);
		HDLEnum _setEnums = he.setEnums(_map);
		he = _setEnums;
		HDLEnumDeclaration _hDLEnumDeclaration = new HDLEnumDeclaration();
		HDLEnumDeclaration _setHEnum = _hDLEnumDeclaration.setHEnum(he);
		return this.<HDLEnumDeclaration> attachContext(_setHEnum, context);
	}

	protected HDLSubstituteFunction _toHDL(final PsSubstituteFunctionContext context) {
		HDLSubstituteFunction _hDLSubstituteFunction = new HDLSubstituteFunction();
		HDLSubstituteFunction func = _hDLSubstituteFunction;
		PsFunctionContext _psFunction = context.psFunction();
		String _name = this.toName(_psFunction);
		HDLSubstituteFunction _setName = func.setName(_name);
		func = _setName;
		List<PsStatementContext> _psStatement = context.psStatement();
		final Function1<PsStatementContext, HDLStatement> _function = new Function1<PsStatementContext, HDLStatement>() {
			@Override
			public HDLStatement apply(final PsStatementContext it) {
				IHDLObject _hDL = ParserToModelExtension.this.toHDL(it);
				return ((HDLStatement) _hDL);
			}
		};
		List<HDLStatement> _map = ListExtensions.<PsStatementContext, HDLStatement> map(_psStatement, _function);
		HDLSubstituteFunction _setStmnts = func.setStmnts(_map);
		func = _setStmnts;
		PsFuncParamContext _psFuncParam = context.psFuncParam();
		List<PsVariableContext> _psVariable = _psFuncParam.psVariable();
		final Function1<PsVariableContext, HDLVariable> _function_1 = new Function1<PsVariableContext, HDLVariable>() {
			@Override
			public HDLVariable apply(final PsVariableContext it) {
				IHDLObject _hDL = ParserToModelExtension.this.toHDL(it);
				return ((HDLVariable) _hDL);
			}
		};
		List<HDLVariable> _map_1 = ListExtensions.<PsVariableContext, HDLVariable> map(_psVariable, _function_1);
		HDLSubstituteFunction _setArgs = func.setArgs(_map_1);
		func = _setArgs;
		return this.<HDLSubstituteFunction> attachContext(func, context);
	}

	protected HDLNativeFunction _toHDL(final PsNativeFunctionContext context) {
		HDLNativeFunction _hDLNativeFunction = new HDLNativeFunction();
		HDLNativeFunction func = _hDLNativeFunction;
		PsFunctionContext _psFunction = context.psFunction();
		String _name = this.toName(_psFunction);
		HDLNativeFunction _setName = func.setName(_name);
		func = _setName;
		boolean _tripleNotEquals = (context.isSim != null);
		HDLNativeFunction _setSimOnly = func.setSimOnly(_tripleNotEquals);
		func = _setSimOnly;
		return this.<HDLNativeFunction> attachContext(func, context);
	}

	protected HDLInlineFunction _toHDL(final PsInlineFunctionContext context) {
		HDLInlineFunction _hDLInlineFunction = new HDLInlineFunction();
		HDLInlineFunction func = _hDLInlineFunction;
		PsFunctionContext _psFunction = context.psFunction();
		String _name = this.toName(_psFunction);
		HDLInlineFunction _setName = func.setName(_name);
		func = _setName;
		PsExpressionContext _psExpression = context.psExpression();
		IHDLObject _hDL = this.toHDL(_psExpression);
		HDLInlineFunction _setExpr = func.setExpr(((HDLExpression) _hDL));
		func = _setExpr;
		PsFuncParamContext _psFuncParam = context.psFuncParam();
		List<PsVariableContext> _psVariable = _psFuncParam.psVariable();
		final Function1<PsVariableContext, HDLVariable> _function = new Function1<PsVariableContext, HDLVariable>() {
			@Override
			public HDLVariable apply(final PsVariableContext it) {
				IHDLObject _hDL = ParserToModelExtension.this.toHDL(it);
				return ((HDLVariable) _hDL);
			}
		};
		List<HDLVariable> _map = ListExtensions.<PsVariableContext, HDLVariable> map(_psVariable, _function);
		HDLInlineFunction _setArgs = func.setArgs(_map);
		func = _setArgs;
		return this.<HDLInlineFunction> attachContext(func, context);
	}

	public String toName(final PsFunctionContext context) {
		TerminalNode _RULE_ID = context.RULE_ID();
		return _RULE_ID.getText();
	}

	protected IHDLObject _toHDL(final PsFunctionDeclarationContext context) {
		PsInlineFunctionContext _psInlineFunction = context.psInlineFunction();
		boolean _tripleNotEquals = (_psInlineFunction != null);
		if (_tripleNotEquals) {
			PsInlineFunctionContext _psInlineFunction_1 = context.psInlineFunction();
			IHDLObject _hDL = this.toHDL(_psInlineFunction_1);
			return this.<IHDLObject> attachContext(_hDL, context);
		}
		PsNativeFunctionContext _psNativeFunction = context.psNativeFunction();
		boolean _tripleNotEquals_1 = (_psNativeFunction != null);
		if (_tripleNotEquals_1) {
			PsNativeFunctionContext _psNativeFunction_1 = context.psNativeFunction();
			IHDLObject _hDL_1 = this.toHDL(_psNativeFunction_1);
			return this.<IHDLObject> attachContext(_hDL_1, context);
		}
		PsSubstituteFunctionContext _psSubstituteFunction = context.psSubstituteFunction();
		boolean _tripleNotEquals_2 = (_psSubstituteFunction != null);
		if (_tripleNotEquals_2) {
			PsSubstituteFunctionContext _psSubstituteFunction_1 = context.psSubstituteFunction();
			IHDLObject _hDL_2 = this.toHDL(_psSubstituteFunction_1);
			return this.<IHDLObject> attachContext(_hDL_2, context);
		}
		Class<? extends PsFunctionDeclarationContext> _class = context.getClass();
		String _plus = ("Not implemented type:" + _class);
		IllegalArgumentException _illegalArgumentException = new IllegalArgumentException(_plus);
		throw _illegalArgumentException;
	}

	protected HDLUnresolvedFragment _toHDL(final PsRefPartContext context) {
		HDLUnresolvedFragment frag = null;
		PsFuncArgsContext _psFuncArgs = context.psFuncArgs();
		boolean _tripleNotEquals = (_psFuncArgs != null);
		if (_tripleNotEquals) {
			HDLUnresolvedFragmentFunction _hDLUnresolvedFragmentFunction = new HDLUnresolvedFragmentFunction();
			TerminalNode _RULE_ID = context.RULE_ID();
			String _text = _RULE_ID.getText();
			HDLUnresolvedFragmentFunction uff = _hDLUnresolvedFragmentFunction.setFrag(_text);
			PsFuncArgsContext _psFuncArgs_1 = context.psFuncArgs();
			List<PsExpressionContext> _psExpression = _psFuncArgs_1.psExpression();
			final Function1<PsExpressionContext, HDLExpression> _function = new Function1<PsExpressionContext, HDLExpression>() {
				@Override
				public HDLExpression apply(final PsExpressionContext it) {
					IHDLObject _hDL = ParserToModelExtension.this.toHDL(it);
					return ((HDLExpression) _hDL);
				}
			};
			List<HDLExpression> _map = ListExtensions.<PsExpressionContext, HDLExpression> map(_psExpression, _function);
			HDLUnresolvedFragmentFunction _setParams = uff.setParams(_map);
			frag = _setParams;
		} else {
			HDLUnresolvedFragment _hDLUnresolvedFragment = new HDLUnresolvedFragment();
			TerminalNode _RULE_ID_1 = context.RULE_ID();
			String _text_1 = _RULE_ID_1.getText();
			HDLUnresolvedFragment _setFrag = _hDLUnresolvedFragment.setFrag(_text_1);
			frag = _setFrag;
			PsArrayContext _psArray = context.psArray();
			boolean _tripleNotEquals_1 = (_psArray != null);
			if (_tripleNotEquals_1) {
				PsArrayContext _psArray_1 = context.psArray();
				List<PsExpressionContext> _psExpression_1 = _psArray_1.psExpression();
				final Function1<PsExpressionContext, HDLExpression> _function_1 = new Function1<PsExpressionContext, HDLExpression>() {
					@Override
					public HDLExpression apply(final PsExpressionContext it) {
						IHDLObject _hDL = ParserToModelExtension.this.toHDL(it);
						return ((HDLExpression) _hDL);
					}
				};
				List<HDLExpression> _map_1 = ListExtensions.<PsExpressionContext, HDLExpression> map(_psExpression_1, _function_1);
				HDLUnresolvedFragment _setArray = frag.setArray(_map_1);
				frag = _setArray;
			}
			PsBitAccessContext _psBitAccess = context.psBitAccess();
			boolean _tripleNotEquals_2 = (_psBitAccess != null);
			if (_tripleNotEquals_2) {
				PsBitAccessContext _psBitAccess_1 = context.psBitAccess();
				List<PsAccessRangeContext> _psAccessRange = _psBitAccess_1.psAccessRange();
				final Function1<PsAccessRangeContext, HDLRange> _function_2 = new Function1<PsAccessRangeContext, HDLRange>() {
					@Override
					public HDLRange apply(final PsAccessRangeContext it) {
						IHDLObject _hDL = ParserToModelExtension.this.toHDL(it);
						return ((HDLRange) _hDL);
					}
				};
				List<HDLRange> _map_2 = ListExtensions.<PsAccessRangeContext, HDLRange> map(_psAccessRange, _function_2);
				HDLUnresolvedFragment _setBits = frag.setBits(_map_2);
				frag = _setBits;
			}
		}
		return this.<HDLUnresolvedFragment> attachContext(frag, context);
	}

	protected HDLReference _toHDL(final PsVariableRefContext context) {
		boolean _tripleNotEquals = (context.isClk != null);
		if (_tripleNotEquals) {
			HDLVariable _defaultClk = HDLRegisterConfig.defaultClk();
			HDLVariableRef _asHDLRef = _defaultClk.asHDLRef();
			return this.<HDLVariableRef> attachContext(_asHDLRef, context);
		}
		boolean _tripleNotEquals_1 = (context.isRst != null);
		if (_tripleNotEquals_1) {
			HDLVariable _defaultRst = HDLRegisterConfig.defaultRst();
			HDLVariableRef _asHDLRef_1 = _defaultRst.asHDLRef();
			return this.<HDLVariableRef> attachContext(_asHDLRef_1, context);
		}
		HDLUnresolvedFragment current = null;
		List<PsRefPartContext> _psRefPart = context.psRefPart();
		List<PsRefPartContext> _reverseView = ListExtensions.<PsRefPartContext> reverseView(_psRefPart);
		for (final PsRefPartContext sub : _reverseView) {
			{
				IHDLObject _hDL = this.toHDL(sub);
				HDLUnresolvedFragment frag = ((HDLUnresolvedFragment) _hDL);
				boolean _tripleNotEquals_2 = (current != null);
				if (_tripleNotEquals_2) {
					HDLUnresolvedFragment _setSub = frag.setSub(current);
					frag = _setSub;
				}
				current = frag;
			}
		}
		return this.<HDLUnresolvedFragment> attachContext(current, context);
	}

	protected HDLRange _toHDL(final PsAccessRangeContext context) {
		HDLRange _hDLRange = new HDLRange();
		IHDLObject _hDL = this.toHDL(context.from);
		HDLRange res = _hDLRange.setTo(((HDLExpression) _hDL));
		boolean _tripleNotEquals = (context.to != null);
		if (_tripleNotEquals) {
			IHDLObject _hDL_1 = this.toHDL(context.from);
			HDLRange _setFrom = res.setFrom(((HDLExpression) _hDL_1));
			IHDLObject _hDL_2 = this.toHDL(context.to);
			HDLRange _setTo = _setFrom.setTo(((HDLExpression) _hDL_2));
			res = _setTo;
		}
		return this.<HDLRange> attachContext(res, context);
	}

	protected HDLSwitchCaseStatement _toHDL(final PsCaseStatementsContext context) {
		HDLSwitchCaseStatement _hDLSwitchCaseStatement = new HDLSwitchCaseStatement();
		HDLSwitchCaseStatement hCase = _hDLSwitchCaseStatement;
		PsValueContext _psValue = context.psValue();
		boolean _tripleNotEquals = (_psValue != null);
		if (_tripleNotEquals) {
			PsValueContext _psValue_1 = context.psValue();
			IHDLObject _hDL = this.toHDL(_psValue_1);
			HDLSwitchCaseStatement _setLabel = hCase.setLabel(((HDLExpression) _hDL));
			hCase = _setLabel;
		}
		List<PsBlockContext> _psBlock = context.psBlock();
		final Function1<PsBlockContext, HDLStatement> _function = new Function1<PsBlockContext, HDLStatement>() {
			@Override
			public HDLStatement apply(final PsBlockContext it) {
				IHDLObject _hDL = ParserToModelExtension.this.toHDL(it);
				return ((HDLStatement) _hDL);
			}
		};
		List<HDLStatement> _map = ListExtensions.<PsBlockContext, HDLStatement> map(_psBlock, _function);
		HDLSwitchCaseStatement _setDos = hCase.setDos(_map);
		hCase = _setDos;
		return this.<HDLSwitchCaseStatement> attachContext(hCase, context);
	}

	protected HDLSwitchStatement _toHDL(final PsSwitchStatementContext context) {
		HDLSwitchStatement _hDLSwitchStatement = new HDLSwitchStatement();
		PsVariableRefContext _psVariableRef = context.psVariableRef();
		IHDLObject _hDL = this.toHDL(_psVariableRef);
		HDLSwitchStatement switchStmnt = _hDLSwitchStatement.setCaseExp(((HDLExpression) _hDL));
		List<PsCaseStatementsContext> _psCaseStatements = context.psCaseStatements();
		final Function1<PsCaseStatementsContext, HDLSwitchCaseStatement> _function = new Function1<PsCaseStatementsContext, HDLSwitchCaseStatement>() {
			@Override
			public HDLSwitchCaseStatement apply(final PsCaseStatementsContext it) {
				IHDLObject _hDL = ParserToModelExtension.this.toHDL(it);
				return ((HDLSwitchCaseStatement) _hDL);
			}
		};
		List<HDLSwitchCaseStatement> _map = ListExtensions.<PsCaseStatementsContext, HDLSwitchCaseStatement> map(_psCaseStatements, _function);
		HDLSwitchStatement _setCases = switchStmnt.setCases(_map);
		switchStmnt = _setCases;
		return this.<HDLSwitchStatement> attachContext(switchStmnt, context);
	}

	protected HDLInterfaceInstantiation _toHDL(final PsInterfaceInstantiationContext context) {
		PsVariableContext _psVariable = context.psVariable();
		IHDLObject _hDL = this.toHDL(_psVariable);
		HDLVariable hVar = ((HDLVariable) _hDL);
		PsArrayContext _psArray = context.psArray();
		boolean _tripleNotEquals = (_psArray != null);
		if (_tripleNotEquals) {
			PsArrayContext _psArray_1 = context.psArray();
			List<PsExpressionContext> _psExpression = _psArray_1.psExpression();
			final Function1<PsExpressionContext, HDLExpression> _function = new Function1<PsExpressionContext, HDLExpression>() {
				@Override
				public HDLExpression apply(final PsExpressionContext it) {
					IHDLObject _hDL = ParserToModelExtension.this.toHDL(it);
					return ((HDLExpression) _hDL);
				}
			};
			List<HDLExpression> _map = ListExtensions.<PsExpressionContext, HDLExpression> map(_psExpression, _function);
			HDLVariable _setDimensions = hVar.setDimensions(_map);
			hVar = _setDimensions;
		}
		HDLInterfaceInstantiation _hDLInterfaceInstantiation = new HDLInterfaceInstantiation();
		HDLInterfaceInstantiation _setVar = _hDLInterfaceInstantiation.setVar(hVar);
		PsQualifiedNameContext _psQualifiedName = context.psQualifiedName();
		HDLQualifiedName _fQNName = this.toFQNName(_psQualifiedName);
		HDLInterfaceInstantiation hii = _setVar.setHIf(_fQNName);
		PsPassedArgumentsContext _psPassedArguments = context.psPassedArguments();
		boolean _tripleNotEquals_1 = (_psPassedArguments != null);
		if (_tripleNotEquals_1) {
			PsPassedArgumentsContext _psPassedArguments_1 = context.psPassedArguments();
			List<PsArgumentContext> _psArgument = _psPassedArguments_1.psArgument();
			final Function1<PsArgumentContext, HDLArgument> _function_1 = new Function1<PsArgumentContext, HDLArgument>() {
				@Override
				public HDLArgument apply(final PsArgumentContext it) {
					IHDLObject _hDL = ParserToModelExtension.this.toHDL(it);
					return ((HDLArgument) _hDL);
				}
			};
			List<HDLArgument> _map_1 = ListExtensions.<PsArgumentContext, HDLArgument> map(_psArgument, _function_1);
			HDLInterfaceInstantiation _setArguments = hii.setArguments(_map_1);
			hii = _setArguments;
		}
		return this.<HDLInterfaceInstantiation> attachContext(hii, context);
	}

	protected HDLForLoop _toHDL(final PsForStatementContext context) {
		HDLForLoop _hDLForLoop = new HDLForLoop();
		PsVariableContext _psVariable = context.psVariable();
		IHDLObject _hDL = this.toHDL(_psVariable);
		HDLForLoop loop = _hDLForLoop.setParam(((HDLVariable) _hDL));
		PsBitAccessContext _psBitAccess = context.psBitAccess();
		List<PsAccessRangeContext> _psAccessRange = _psBitAccess.psAccessRange();
		final Function1<PsAccessRangeContext, HDLRange> _function = new Function1<PsAccessRangeContext, HDLRange>() {
			@Override
			public HDLRange apply(final PsAccessRangeContext it) {
				IHDLObject _hDL = ParserToModelExtension.this.toHDL(it);
				return ((HDLRange) _hDL);
			}
		};
		List<HDLRange> _map = ListExtensions.<PsAccessRangeContext, HDLRange> map(_psAccessRange, _function);
		HDLForLoop _setRange = loop.setRange(_map);
		loop = _setRange;
		PsSimpleBlockContext _psSimpleBlock = context.psSimpleBlock();
		List<PsBlockContext> _psBlock = _psSimpleBlock.psBlock();
		final Function1<PsBlockContext, HDLStatement> _function_1 = new Function1<PsBlockContext, HDLStatement>() {
			@Override
			public HDLStatement apply(final PsBlockContext it) {
				IHDLObject _hDL = ParserToModelExtension.this.toHDL(it);
				return ((HDLStatement) _hDL);
			}
		};
		List<HDLStatement> _map_1 = ListExtensions.<PsBlockContext, HDLStatement> map(_psBlock, _function_1);
		HDLForLoop _setDos = loop.setDos(_map_1);
		loop = _setDos;
		return this.<HDLForLoop> attachContext(loop, context);
	}

	protected HDLIfStatement _toHDL(final PsIfStatementContext context) {
		HDLIfStatement _hDLIfStatement = new HDLIfStatement();
		PsExpressionContext _psExpression = context.psExpression();
		IHDLObject _hDL = this.toHDL(_psExpression);
		HDLIfStatement res = _hDLIfStatement.setIfExp(((HDLExpression) _hDL));
		List<PsBlockContext> _psBlock = context.ifBlk.psBlock();
		final Function1<PsBlockContext, HDLStatement> _function = new Function1<PsBlockContext, HDLStatement>() {
			@Override
			public HDLStatement apply(final PsBlockContext it) {
				IHDLObject _hDL = ParserToModelExtension.this.toHDL(it);
				return ((HDLStatement) _hDL);
			}
		};
		List<HDLStatement> _map = ListExtensions.<PsBlockContext, HDLStatement> map(_psBlock, _function);
		HDLIfStatement _setThenDo = res.setThenDo(_map);
		res = _setThenDo;
		boolean _tripleNotEquals = (context.elseBlk != null);
		if (_tripleNotEquals) {
			List<PsBlockContext> _psBlock_1 = context.elseBlk.psBlock();
			final Function1<PsBlockContext, HDLStatement> _function_1 = new Function1<PsBlockContext, HDLStatement>() {
				@Override
				public HDLStatement apply(final PsBlockContext it) {
					IHDLObject _hDL = ParserToModelExtension.this.toHDL(it);
					return ((HDLStatement) _hDL);
				}
			};
			List<HDLStatement> _map_1 = ListExtensions.<PsBlockContext, HDLStatement> map(_psBlock_1, _function_1);
			HDLIfStatement _setElseDo = res.setElseDo(_map_1);
			res = _setElseDo;
		}
		return this.<HDLIfStatement> attachContext(res, context);
	}

	protected IHDLObject _toHDL(final PsCompoundStatementContext context) {
		PsForStatementContext _psForStatement = context.psForStatement();
		boolean _tripleNotEquals = (_psForStatement != null);
		if (_tripleNotEquals) {
			PsForStatementContext _psForStatement_1 = context.psForStatement();
			IHDLObject _hDL = this.toHDL(_psForStatement_1);
			return this.<IHDLObject> attachContext(_hDL, context);
		}
		PsIfStatementContext _psIfStatement = context.psIfStatement();
		boolean _tripleNotEquals_1 = (_psIfStatement != null);
		if (_tripleNotEquals_1) {
			PsIfStatementContext _psIfStatement_1 = context.psIfStatement();
			IHDLObject _hDL_1 = this.toHDL(_psIfStatement_1);
			return this.<IHDLObject> attachContext(_hDL_1, context);
		}
		PsSwitchStatementContext _psSwitchStatement = context.psSwitchStatement();
		boolean _tripleNotEquals_2 = (_psSwitchStatement != null);
		if (_tripleNotEquals_2) {
			PsSwitchStatementContext _psSwitchStatement_1 = context.psSwitchStatement();
			IHDLObject _hDL_2 = this.toHDL(_psSwitchStatement_1);
			return this.<IHDLObject> attachContext(_hDL_2, context);
		}
		Class<? extends PsCompoundStatementContext> _class = context.getClass();
		String _plus = ("Unhandled type:" + _class);
		IllegalArgumentException _illegalArgumentException = new IllegalArgumentException(_plus);
		throw _illegalArgumentException;
	}

	protected IHDLObject _toHDL(final PsStatementContext context) {
		PsAssignmentOrFuncContext _psAssignmentOrFunc = context.psAssignmentOrFunc();
		boolean _tripleNotEquals = (_psAssignmentOrFunc != null);
		if (_tripleNotEquals) {
			PsAssignmentOrFuncContext _psAssignmentOrFunc_1 = context.psAssignmentOrFunc();
			IHDLObject _hDL = this.toHDL(_psAssignmentOrFunc_1);
			return this.<IHDLObject> attachContext(_hDL, context);
		}
		PsCompoundStatementContext _psCompoundStatement = context.psCompoundStatement();
		boolean _tripleNotEquals_1 = (_psCompoundStatement != null);
		if (_tripleNotEquals_1) {
			PsCompoundStatementContext _psCompoundStatement_1 = context.psCompoundStatement();
			IHDLObject _hDL_1 = this.toHDL(_psCompoundStatement_1);
			return this.<IHDLObject> attachContext(_hDL_1, context);
		}
		PsProcessContext _psProcess = context.psProcess();
		boolean _tripleNotEquals_2 = (_psProcess != null);
		if (_tripleNotEquals_2) {
			PsProcessContext _psProcess_1 = context.psProcess();
			IHDLObject _hDL_2 = this.toHDL(_psProcess_1);
			return this.<IHDLObject> attachContext(_hDL_2, context);
		}
		Class<? extends PsStatementContext> _class = context.getClass();
		String _plus = ("Unhandled type:" + _class);
		IllegalArgumentException _illegalArgumentException = new IllegalArgumentException(_plus);
		throw _illegalArgumentException;
	}

	protected IHDLObject _toHDL(final PsAssignmentOrFuncContext context) {
		PsVariableRefContext _psVariableRef = context.psVariableRef();
		IHDLObject _hDL = this.toHDL(_psVariableRef);
		final HDLReference hVar = ((HDLReference) _hDL);
		PsAssignmentOpContext _psAssignmentOp = context.psAssignmentOp();
		boolean _tripleNotEquals = (_psAssignmentOp != null);
		if (_tripleNotEquals) {
			PsAssignmentOpContext _psAssignmentOp_1 = context.psAssignmentOp();
			String _text = _psAssignmentOp_1.getText();
			final HDLAssignmentType type = HDLAssignmentType.getOp(_text);
			HDLAssignment _hDLAssignment = new HDLAssignment();
			HDLAssignment _setLeft = _hDLAssignment.setLeft(hVar);
			HDLAssignment ass = _setLeft.setType(type);
			PsExpressionContext _psExpression = context.psExpression();
			IHDLObject _hDL_1 = this.toHDL(_psExpression);
			HDLAssignment _setRight = ass.setRight(((HDLExpression) _hDL_1));
			ass = _setRight;
			return this.<HDLAssignment> attachContext(ass, context);
		}
		return this.<HDLReference> attachContext(hVar, context);
	}

	protected IHDLObject _toHDL(final Object context) {
		Class<? extends Object> _class = context.getClass();
		String _plus = ("Unhandled type:" + _class);
		IllegalArgumentException _illegalArgumentException = new IllegalArgumentException(_plus);
		throw _illegalArgumentException;
	}

	public HDLUnit toHDLUnit(final PsUnitContext context, final String libURI) {
		HDLUnit _hDLUnit = new HDLUnit();
		PsInterfaceContext _psInterface = context.psInterface();
		String _name = this.toName(_psInterface);
		HDLUnit _setName = _hDLUnit.setName(_name);
		HDLUnit unit = _setName.setLibURI(libURI);
		int _type = context.unitType.getType();
		boolean _equals = (_type == PSHDLLangLexer.TESTBENCH);
		HDLUnit _setSimulation = unit.setSimulation(_equals);
		unit = _setSimulation;
		List<PsAnnotationContext> _psAnnotation = context.psAnnotation();
		final Function1<PsAnnotationContext, HDLAnnotation> _function = new Function1<PsAnnotationContext, HDLAnnotation>() {
			@Override
			public HDLAnnotation apply(final PsAnnotationContext it) {
				IHDLObject _hDL = ParserToModelExtension.this.toHDL(it);
				return ((HDLAnnotation) _hDL);
			}
		};
		List<HDLAnnotation> _map = ListExtensions.<PsAnnotationContext, HDLAnnotation> map(_psAnnotation, _function);
		HDLUnit _setAnnotations = unit.setAnnotations(_map);
		unit = _setAnnotations;
		List<PsImportsContext> _psImports = context.psImports();
		final Function1<PsImportsContext, String> _function_1 = new Function1<PsImportsContext, String>() {
			@Override
			public String apply(final PsImportsContext it) {
				String _name = ParserToModelExtension.this.toName(it);
				return _name;
			}
		};
		List<String> _map_1 = ListExtensions.<PsImportsContext, String> map(_psImports, _function_1);
		HDLUnit _setImports = unit.setImports(_map_1);
		unit = _setImports;
		List<PsBlockContext> _psBlock = context.psBlock();
		final Function1<PsBlockContext, HDLStatement> _function_2 = new Function1<PsBlockContext, HDLStatement>() {
			@Override
			public HDLStatement apply(final PsBlockContext it) {
				IHDLObject _hDL = ParserToModelExtension.this.toHDL(it);
				return ((HDLStatement) _hDL);
			}
		};
		List<HDLStatement> _map_2 = ListExtensions.<PsBlockContext, HDLStatement> map(_psBlock, _function_2);
		HDLUnit _setStatements = unit.setStatements(_map_2);
		unit = _setStatements;
		return this.<HDLUnit> attachContext(unit, context);
	}

	public String toName(final PsImportsContext context) {
		PsQualifiedNameImportContext _psQualifiedNameImport = context.psQualifiedNameImport();
		return _psQualifiedNameImport.getText();
	}

	public IHDLObject toHDL(final Object context) {
		if (context instanceof PsAddContext)
			return _toHDL((PsAddContext) context);
		else if (context instanceof PsBitAndContext)
			return _toHDL((PsBitAndContext) context);
		else if (context instanceof PsBitLogAndContext)
			return _toHDL((PsBitLogAndContext) context);
		else if (context instanceof PsBitLogOrContext)
			return _toHDL((PsBitLogOrContext) context);
		else if (context instanceof PsBitOrContext)
			return _toHDL((PsBitOrContext) context);
		else if (context instanceof PsBitXorContext)
			return _toHDL((PsBitXorContext) context);
		else if (context instanceof PsConcatContext)
			return _toHDL((PsConcatContext) context);
		else if (context instanceof PsEqualityCompContext)
			return _toHDL((PsEqualityCompContext) context);
		else if (context instanceof PsEqualityContext)
			return _toHDL((PsEqualityContext) context);
		else if (context instanceof PsManipContext)
			return _toHDL((PsManipContext) context);
		else if (context instanceof PsMulContext)
			return _toHDL((PsMulContext) context);
		else if (context instanceof PsParensContext)
			return _toHDL((PsParensContext) context);
		else if (context instanceof PsShiftContext)
			return _toHDL((PsShiftContext) context);
		else if (context instanceof PsTernaryContext)
			return _toHDL((PsTernaryContext) context);
		else if (context instanceof PsValueExpContext)
			return _toHDL((PsValueExpContext) context);
		else if (context instanceof PsAccessRangeContext)
			return _toHDL((PsAccessRangeContext) context);
		else if (context instanceof PsAnnotationContext)
			return _toHDL((PsAnnotationContext) context);
		else if (context instanceof PsArgumentContext)
			return _toHDL((PsArgumentContext) context);
		else if (context instanceof PsArrayInitContext)
			return _toHDL((PsArrayInitContext) context);
		else if (context instanceof PsArrayInitSubContext)
			return _toHDL((PsArrayInitSubContext) context);
		else if (context instanceof PsAssignmentOrFuncContext)
			return _toHDL((PsAssignmentOrFuncContext) context);
		else if (context instanceof PsBlockContext)
			return _toHDL((PsBlockContext) context);
		else if (context instanceof PsCaseStatementsContext)
			return _toHDL((PsCaseStatementsContext) context);
		else if (context instanceof PsCastContext)
			return _toHDL((PsCastContext) context);
		else if (context instanceof PsCompoundStatementContext)
			return _toHDL((PsCompoundStatementContext) context);
		else if (context instanceof PsDeclAssignmentContext)
			return _toHDL((PsDeclAssignmentContext) context);
		else if (context instanceof PsDeclarationContext)
			return _toHDL((PsDeclarationContext) context);
		else if (context instanceof PsDeclarationTypeContext)
			return _toHDL((PsDeclarationTypeContext) context);
		else if (context instanceof PsDirectGenerationContext)
			return _toHDL((PsDirectGenerationContext) context);
		else if (context instanceof PsEnumContext)
			return _toHDL((PsEnumContext) context);
		else if (context instanceof PsEnumDeclarationContext)
			return _toHDL((PsEnumDeclarationContext) context);
		else if (context instanceof PsExpressionContext)
			return _toHDL((PsExpressionContext) context);
		else if (context instanceof PsForStatementContext)
			return _toHDL((PsForStatementContext) context);
		else if (context instanceof PsFunctionDeclarationContext)
			return _toHDL((PsFunctionDeclarationContext) context);
		else if (context instanceof PsIfStatementContext)
			return _toHDL((PsIfStatementContext) context);
		else if (context instanceof PsInlineFunctionContext)
			return _toHDL((PsInlineFunctionContext) context);
		else if (context instanceof PsInstantiationContext)
			return _toHDL((PsInstantiationContext) context);
		else if (context instanceof PsInterfaceContext)
			return _toHDL((PsInterfaceContext) context);
		else if (context instanceof PsInterfaceDeclarationContext)
			return _toHDL((PsInterfaceDeclarationContext) context);
		else if (context instanceof PsInterfaceInstantiationContext)
			return _toHDL((PsInterfaceInstantiationContext) context);
		else if (context instanceof PsNativeFunctionContext)
			return _toHDL((PsNativeFunctionContext) context);
		else if (context instanceof PsPortDeclarationContext)
			return _toHDL((PsPortDeclarationContext) context);
		else if (context instanceof PsPrimitiveContext)
			return _toHDL((PsPrimitiveContext) context);
		else if (context instanceof PsProcessContext)
			return _toHDL((PsProcessContext) context);
		else if (context instanceof PsRefPartContext)
			return _toHDL((PsRefPartContext) context);
		else if (context instanceof PsStatementContext)
			return _toHDL((PsStatementContext) context);
		else if (context instanceof PsSubstituteFunctionContext)
			return _toHDL((PsSubstituteFunctionContext) context);
		else if (context instanceof PsSwitchStatementContext)
			return _toHDL((PsSwitchStatementContext) context);
		else if (context instanceof PsTypeDeclarationContext)
			return _toHDL((PsTypeDeclarationContext) context);
		else if (context instanceof PsValueContext)
			return _toHDL((PsValueContext) context);
		else if (context instanceof PsVariableContext)
			return _toHDL((PsVariableContext) context);
		else if (context instanceof PsVariableDeclarationContext)
			return _toHDL((PsVariableDeclarationContext) context);
		else if (context instanceof PsVariableRefContext)
			return _toHDL((PsVariableRefContext) context);
		else if (context instanceof PsWidthContext)
			return _toHDL((PsWidthContext) context);
		else if (context != null)
			return _toHDL(context);
		else
			throw new IllegalArgumentException("Unhandled parameter types: " + Arrays.<Object> asList(context).toString());
	}
}
