package org.pshdl.model.utils;

import com.google.common.base.Optional;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.MapExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure2;
import org.pshdl.model.HDLAssignment;
import org.pshdl.model.HDLBlock;
import org.pshdl.model.HDLDeclaration;
import org.pshdl.model.HDLForLoop;
import org.pshdl.model.HDLFunctionCall;
import org.pshdl.model.HDLIfStatement;
import org.pshdl.model.HDLInstantiation;
import org.pshdl.model.HDLReference;
import org.pshdl.model.HDLRegisterConfig;
import org.pshdl.model.HDLResolvedRef;
import org.pshdl.model.HDLStatement;
import org.pshdl.model.HDLSwitchCaseStatement;
import org.pshdl.model.HDLSwitchStatement;
import org.pshdl.model.HDLUnit;
import org.pshdl.model.HDLUnresolvedFragment;
import org.pshdl.model.HDLVariable;
import org.pshdl.model.extensions.StringWriteExtension;
import org.pshdl.model.utils.HDLQuery.HDLFieldAccess;
import org.pshdl.model.utils.SyntaxHighlighter;

@SuppressWarnings("all")
public class ProcessModel {
  private static AtomicInteger ai = new Function0<AtomicInteger>() {
    public AtomicInteger apply() {
      AtomicInteger _atomicInteger = new AtomicInteger();
      return _atomicInteger;
    }
  }.apply();
  
  public Multimap<Integer,HDLStatement> unclockedStatements = new Function0<Multimap<Integer,HDLStatement>>() {
    public Multimap<Integer,HDLStatement> apply() {
      LinkedListMultimap<Integer,HDLStatement> _create = LinkedListMultimap.<Integer, HDLStatement>create();
      return _create;
    }
  }.apply();
  
  public Multimap<HDLRegisterConfig,HDLStatement> clockedStatements = new Function0<Multimap<HDLRegisterConfig,HDLStatement>>() {
    public Multimap<HDLRegisterConfig,HDLStatement> apply() {
      LinkedListMultimap<HDLRegisterConfig,HDLStatement> _create = LinkedListMultimap.<HDLRegisterConfig, HDLStatement>create();
      return _create;
    }
  }.apply();
  
  public final static int DEF_PROCESS = new Function0<Integer>() {
    public Integer apply() {
      int _minus = (-1);
      return _minus;
    }
  }.apply();
  
  public static ProcessModel toProcessModel(final HDLUnit stmnt) {
    ProcessModel _processModel = new ProcessModel();
    final ProcessModel pm = _processModel;
    ArrayList<HDLStatement> _inits = stmnt.getInits();
    final Procedure1<HDLStatement> _function = new Procedure1<HDLStatement>() {
      public void apply(final HDLStatement s) {
        ProcessModel _processModel = ProcessModel.toProcessModel(s, ProcessModel.DEF_PROCESS);
        pm.merge(_processModel);
      }
    };
    IterableExtensions.<HDLStatement>forEach(_inits, _function);
    ArrayList<HDLStatement> _statements = stmnt.getStatements();
    final Procedure1<HDLStatement> _function_1 = new Procedure1<HDLStatement>() {
      public void apply(final HDLStatement s) {
        ProcessModel _processModel = ProcessModel.toProcessModel(s, ProcessModel.DEF_PROCESS);
        pm.merge(_processModel);
      }
    };
    IterableExtensions.<HDLStatement>forEach(_statements, _function_1);
    return pm;
  }
  
  protected static ProcessModel _toProcessModel(final HDLFunctionCall stmnt, final int pid) {
    ProcessModel _processModel = new ProcessModel();
    ProcessModel _addUnclocked = _processModel.addUnclocked(pid, stmnt);
    return _addUnclocked;
  }
  
  protected static ProcessModel _toProcessModel(final HDLInstantiation stmnt, final int pid) {
    return null;
  }
  
  protected static ProcessModel _toProcessModel(final HDLDeclaration stmnt, final int pid) {
    return null;
  }
  
  protected static ProcessModel _toProcessModel(final HDLStatement stmnt, final int pid) {
    String _plus = ("Not implemented for statement:" + stmnt);
    RuntimeException _runtimeException = new RuntimeException(_plus);
    throw _runtimeException;
  }
  
  protected static ProcessModel _toProcessModel(final HDLBlock stmnt, final int pid) {
    int _xifexpression = (int) 0;
    Boolean _process = stmnt.getProcess();
    if ((_process).booleanValue()) {
      int _andIncrement = ProcessModel.ai.getAndIncrement();
      _xifexpression = _andIncrement;
    } else {
      _xifexpression = pid;
    }
    final int newPid = _xifexpression;
    return ProcessModel.<HDLBlock, HDLStatement, ArrayList<HDLStatement>>toProcessModel(stmnt, HDLBlock.fStatements, newPid);
  }
  
  protected static ProcessModel _toProcessModel(final HDLForLoop stmnt, final int pid) {
    return ProcessModel.<HDLForLoop, HDLStatement, ArrayList<HDLStatement>>toProcessModel(stmnt, HDLForLoop.fDos, pid);
  }
  
  protected static ProcessModel _toProcessModel(final HDLSwitchCaseStatement stmnt, final int pid) {
    return ProcessModel.<HDLSwitchCaseStatement, HDLStatement, ArrayList<HDLStatement>>toProcessModel(stmnt, HDLSwitchCaseStatement.fDos, pid);
  }
  
  private static <T extends HDLStatement, V extends HDLStatement, C extends Collection<V>> ProcessModel toProcessModel(final T obj, final HDLFieldAccess<T,C> field, final int pid) {
    ProcessModel _processModel = new ProcessModel();
    final ProcessModel pm = _processModel;
    C _value = field.getValue(obj);
    for (final V subStmnt : _value) {
      ProcessModel _processModel_1 = ProcessModel.toProcessModel(subStmnt, pid);
      pm.merge(_processModel_1);
    }
    ProcessModel _processModel_2 = new ProcessModel();
    final ProcessModel res = _processModel_2;
    Map<Integer,Collection<HDLStatement>> _asMap = pm.unclockedStatements.asMap();
    final Procedure2<Integer,Collection<HDLStatement>> _function = new Procedure2<Integer,Collection<HDLStatement>>() {
      public void apply(final Integer subPid, final Collection<HDLStatement> stmnts) {
        ArrayList<HDLStatement> _arrayList = new ArrayList<HDLStatement>(stmnts);
        T _setValue = field.setValue(obj, ((C) _arrayList));
        res.unclockedStatements.put(subPid, _setValue);
      }
    };
    MapExtensions.<Integer, Collection<HDLStatement>>forEach(_asMap, _function);
    Map<HDLRegisterConfig,Collection<HDLStatement>> _asMap_1 = pm.clockedStatements.asMap();
    final Procedure2<HDLRegisterConfig,Collection<HDLStatement>> _function_1 = new Procedure2<HDLRegisterConfig,Collection<HDLStatement>>() {
      public void apply(final HDLRegisterConfig reg, final Collection<HDLStatement> stmnts) {
        ArrayList<HDLStatement> _arrayList = new ArrayList<HDLStatement>(stmnts);
        T _setValue = field.setValue(obj, ((C) _arrayList));
        res.clockedStatements.put(reg, _setValue);
      }
    };
    MapExtensions.<HDLRegisterConfig, Collection<HDLStatement>>forEach(_asMap_1, _function_1);
    return res;
  }
  
  protected static ProcessModel _toProcessModel(final HDLIfStatement stmnt, final int pid) {
    ProcessModel _processModel = new ProcessModel();
    final ProcessModel thenPM = _processModel;
    ArrayList<HDLStatement> _thenDo = stmnt.getThenDo();
    final Procedure1<HDLStatement> _function = new Procedure1<HDLStatement>() {
      public void apply(final HDLStatement s) {
        ProcessModel _processModel = ProcessModel.toProcessModel(s, pid);
        thenPM.merge(_processModel);
      }
    };
    IterableExtensions.<HDLStatement>forEach(_thenDo, _function);
    ProcessModel _processModel_1 = new ProcessModel();
    final ProcessModel elsePM = _processModel_1;
    ArrayList<HDLStatement> _elseDo = stmnt.getElseDo();
    final Procedure1<HDLStatement> _function_1 = new Procedure1<HDLStatement>() {
      public void apply(final HDLStatement s) {
        ProcessModel _processModel = ProcessModel.toProcessModel(s, pid);
        elsePM.merge(_processModel);
      }
    };
    IterableExtensions.<HDLStatement>forEach(_elseDo, _function_1);
    HashSet<HDLRegisterConfig> _hashSet = new HashSet<HDLRegisterConfig>();
    final HashSet<HDLRegisterConfig> clocks = _hashSet;
    Set<HDLRegisterConfig> _keySet = thenPM.clockedStatements.keySet();
    clocks.addAll(_keySet);
    Set<HDLRegisterConfig> _keySet_1 = elsePM.clockedStatements.keySet();
    clocks.addAll(_keySet_1);
    ProcessModel _processModel_2 = new ProcessModel();
    final ProcessModel res = _processModel_2;
    boolean _or = false;
    boolean _isEmpty = thenPM.unclockedStatements.isEmpty();
    boolean _not = (!_isEmpty);
    if (_not) {
      _or = true;
    } else {
      boolean _isEmpty_1 = elsePM.unclockedStatements.isEmpty();
      boolean _not_1 = (!_isEmpty_1);
      _or = (_not || _not_1);
    }
    if (_or) {
      Collection<HDLStatement> _unclocked = thenPM.getUnclocked(pid);
      HDLIfStatement _setThenDo = stmnt.setThenDo(_unclocked);
      Collection<HDLStatement> _unclocked_1 = elsePM.getUnclocked(pid);
      HDLIfStatement _setElseDo = _setThenDo.setElseDo(_unclocked_1);
      res.addUnclocked(pid, _setElseDo);
    }
    for (final HDLRegisterConfig reg : clocks) {
      Collection<HDLStatement> _clocked = thenPM.getClocked(reg);
      HDLIfStatement _setThenDo_1 = stmnt.setThenDo(_clocked);
      Collection<HDLStatement> _clocked_1 = elsePM.getClocked(reg);
      HDLIfStatement _setElseDo_1 = _setThenDo_1.setElseDo(_clocked_1);
      res.addClocked(reg, _setElseDo_1);
    }
    return res;
  }
  
  protected static ProcessModel _toProcessModel(final HDLSwitchStatement stmnt, final int pid) {
    final Map<HDLSwitchCaseStatement,ProcessModel> pms = Maps.<HDLSwitchCaseStatement, ProcessModel>newLinkedHashMap();
    HashSet<HDLRegisterConfig> _hashSet = new HashSet<HDLRegisterConfig>();
    final HashSet<HDLRegisterConfig> clocks = _hashSet;
    boolean hasUnclocked = false;
    ArrayList<HDLSwitchCaseStatement> _cases = stmnt.getCases();
    for (final HDLSwitchCaseStatement caze : _cases) {
      {
        final ProcessModel casePM = ProcessModel.toProcessModel(caze, pid);
        boolean _isEmpty = casePM.unclockedStatements.isEmpty();
        boolean _not = (!_isEmpty);
        if (_not) {
          hasUnclocked = true;
        }
        Set<HDLRegisterConfig> _keySet = casePM.clockedStatements.keySet();
        clocks.addAll(_keySet);
        pms.put(caze, casePM);
      }
    }
    ProcessModel _processModel = new ProcessModel();
    final ProcessModel res = _processModel;
    if (hasUnclocked) {
      final List<HDLSwitchCaseStatement> newCases = Lists.<HDLSwitchCaseStatement>newLinkedList();
      final Procedure2<HDLSwitchCaseStatement,ProcessModel> _function = new Procedure2<HDLSwitchCaseStatement,ProcessModel>() {
        public void apply(final HDLSwitchCaseStatement caze, final ProcessModel caseStatements) {
          Collection<HDLStatement> _get = caseStatements.unclockedStatements.get(Integer.valueOf(pid));
          HDLSwitchCaseStatement _setDos = caze.setDos(_get);
          newCases.add(_setDos);
        }
      };
      MapExtensions.<HDLSwitchCaseStatement, ProcessModel>forEach(pms, _function);
      HDLSwitchStatement _setCases = stmnt.setCases(newCases);
      res.addUnclocked(pid, _setCases);
    }
    for (final HDLRegisterConfig reg : clocks) {
      {
        final List<HDLSwitchCaseStatement> newCases_1 = Lists.<HDLSwitchCaseStatement>newLinkedList();
        final Procedure2<HDLSwitchCaseStatement,ProcessModel> _function_1 = new Procedure2<HDLSwitchCaseStatement,ProcessModel>() {
          public void apply(final HDLSwitchCaseStatement caze, final ProcessModel caseStatements) {
            Collection<HDLStatement> _get = caseStatements.clockedStatements.get(reg);
            HDLSwitchCaseStatement _setDos = caze.setDos(_get);
            newCases_1.add(_setDos);
          }
        };
        MapExtensions.<HDLSwitchCaseStatement, ProcessModel>forEach(pms, _function_1);
        HDLSwitchStatement _setCases_1 = stmnt.setCases(newCases_1);
        res.addClocked(reg, _setCases_1);
      }
    }
    return res;
  }
  
  protected static ProcessModel _toProcessModel(final HDLAssignment stmnt, final int pid) {
    final HDLReference ref = stmnt.getLeft();
    if ((ref instanceof HDLUnresolvedFragment)) {
      String _plus = ("Not implemented for HDLUnresolvedFragment:" + stmnt);
      RuntimeException _runtimeException = new RuntimeException(_plus);
      throw _runtimeException;
    }
    final HDLResolvedRef rRef = ((HDLResolvedRef) ref);
    Optional<HDLVariable> _resolveVar = rRef.resolveVar();
    final HDLVariable hVar = _resolveVar.get();
    final HDLRegisterConfig regConfig = hVar.getRegisterConfig();
    HDLRegisterConfig _registerConfig = hVar.getRegisterConfig();
    boolean _tripleNotEquals = (_registerConfig != null);
    if (_tripleNotEquals) {
      ProcessModel _processModel = new ProcessModel();
      return _processModel.addClocked(regConfig, stmnt);
    }
    ProcessModel _processModel_1 = new ProcessModel();
    return _processModel_1.addUnclocked(pid, stmnt);
  }
  
  public ProcessModel merge(final ProcessModel model) {
    boolean _tripleNotEquals = (model != null);
    if (_tripleNotEquals) {
      this.unclockedStatements.putAll(model.unclockedStatements);
      this.clockedStatements.putAll(model.clockedStatements);
    }
    return this;
  }
  
  public Collection<HDLStatement> getClocked(final HDLRegisterConfig reg) {
    return this.clockedStatements.get(reg);
  }
  
  public Collection<HDLStatement> getUnclocked(final int pid) {
    return this.unclockedStatements.get(Integer.valueOf(pid));
  }
  
  public ProcessModel addUnclocked(final int pid, final HDLStatement stmnt) {
    this.unclockedStatements.put(Integer.valueOf(pid), stmnt);
    return this;
  }
  
  public ProcessModel addClocked(final HDLRegisterConfig config, final HDLStatement stmnt) {
    this.clockedStatements.put(config, stmnt);
    return this;
  }
  
  public String toString() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<------------------>");
    _builder.newLine();
    _builder.append("Unclocked Statements:");
    _builder.newLine();
    {
      Map<Integer,Collection<HDLStatement>> _asMap = this.unclockedStatements.asMap();
      Set<Entry<Integer,Collection<HDLStatement>>> _entrySet = _asMap.entrySet();
      for(final Entry<Integer, Collection<HDLStatement>> e : _entrySet) {
        _builder.append("Process:");
        Integer _key = e.getKey();
        _builder.append(_key, "");
        _builder.newLineIfNotEmpty();
        {
          Collection<HDLStatement> _value = e.getValue();
          for(final HDLStatement s : _value) {
            SyntaxHighlighter _none = SyntaxHighlighter.none();
            String _asString = StringWriteExtension.asString(s, _none);
            _builder.append(_asString, "");
            _builder.newLineIfNotEmpty();
          }
        }
      }
    }
    _builder.newLine();
    _builder.append("<------------------>");
    _builder.newLine();
    _builder.append("Clocked Statements:");
    _builder.newLine();
    {
      Map<HDLRegisterConfig,Collection<HDLStatement>> _asMap_1 = this.clockedStatements.asMap();
      Set<Entry<HDLRegisterConfig,Collection<HDLStatement>>> _entrySet_1 = _asMap_1.entrySet();
      for(final Entry<HDLRegisterConfig, Collection<HDLStatement>> e_1 : _entrySet_1) {
        _builder.append("Process:");
        HDLRegisterConfig _key_1 = e_1.getKey();
        _builder.append(_key_1, "");
        _builder.newLineIfNotEmpty();
        {
          Collection<HDLStatement> _value_1 = e_1.getValue();
          for(final HDLStatement s_1 : _value_1) {
            SyntaxHighlighter _none_1 = SyntaxHighlighter.none();
            String _asString_1 = StringWriteExtension.asString(s_1, _none_1);
            _builder.append(_asString_1, "");
            _builder.newLineIfNotEmpty();
          }
        }
      }
    }
    return _builder.toString();
  }
  
  public static ProcessModel toProcessModel(final HDLStatement stmnt, final int pid) {
    if (stmnt instanceof HDLForLoop) {
      return _toProcessModel((HDLForLoop)stmnt, pid);
    } else if (stmnt instanceof HDLIfStatement) {
      return _toProcessModel((HDLIfStatement)stmnt, pid);
    } else if (stmnt instanceof HDLSwitchCaseStatement) {
      return _toProcessModel((HDLSwitchCaseStatement)stmnt, pid);
    } else if (stmnt instanceof HDLSwitchStatement) {
      return _toProcessModel((HDLSwitchStatement)stmnt, pid);
    } else if (stmnt instanceof HDLAssignment) {
      return _toProcessModel((HDLAssignment)stmnt, pid);
    } else if (stmnt instanceof HDLBlock) {
      return _toProcessModel((HDLBlock)stmnt, pid);
    } else if (stmnt instanceof HDLDeclaration) {
      return _toProcessModel((HDLDeclaration)stmnt, pid);
    } else if (stmnt instanceof HDLFunctionCall) {
      return _toProcessModel((HDLFunctionCall)stmnt, pid);
    } else if (stmnt instanceof HDLInstantiation) {
      return _toProcessModel((HDLInstantiation)stmnt, pid);
    } else if (stmnt != null) {
      return _toProcessModel(stmnt, pid);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(stmnt, pid).toString());
    }
  }
}
