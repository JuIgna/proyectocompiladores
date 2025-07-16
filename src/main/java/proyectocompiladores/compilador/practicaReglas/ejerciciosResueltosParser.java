// Generated from c:/Users/vazqu/OneDrive/Documentos/Ingenieria Informatica/5 año/SEMESTRE 9/TC/FINAL COMPILADORES/proyectocompiladores/src/main/java/proyectocompiladores/compilador/practicaReglas/ejerciciosResueltos.g4 by ANTLR 4.13.1
 package proyectocompiladores.compilador.practicaReglas; 
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue"})
public class ejerciciosResueltosParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.13.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		LC=1, BC=2, IF=3, MAYOR=4, ID=5, PA=6, PC=7;
	public static final int
		RULE_s = 0, RULE_condicion = 1, RULE_e = 2;
	private static String[] makeRuleNames() {
		return new String[] {
			"s", "condicion", "e"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, null, null, "'if'", "'>'", null, "'('", "')'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "LC", "BC", "IF", "MAYOR", "ID", "PA", "PC"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "ejerciciosResueltos.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public ejerciciosResueltosParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SContext extends ParserRuleContext {
		public TerminalNode IF() { return getToken(ejerciciosResueltosParser.IF, 0); }
		public TerminalNode PA() { return getToken(ejerciciosResueltosParser.PA, 0); }
		public CondicionContext condicion() {
			return getRuleContext(CondicionContext.class,0);
		}
		public TerminalNode PC() { return getToken(ejerciciosResueltosParser.PC, 0); }
		public SContext s() {
			return getRuleContext(SContext.class,0);
		}
		public SContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_s; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ejerciciosResueltosListener ) ((ejerciciosResueltosListener)listener).enterS(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ejerciciosResueltosListener ) ((ejerciciosResueltosListener)listener).exitS(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ejerciciosResueltosVisitor ) return ((ejerciciosResueltosVisitor<? extends T>)visitor).visitS(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SContext s() throws RecognitionException {
		SContext _localctx = new SContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_s);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(6);
			match(IF);
			setState(7);
			match(PA);
			setState(8);
			condicion();
			setState(9);
			match(PC);
			setState(10);
			s();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class CondicionContext extends ParserRuleContext {
		public List<EContext> e() {
			return getRuleContexts(EContext.class);
		}
		public EContext e(int i) {
			return getRuleContext(EContext.class,i);
		}
		public TerminalNode MAYOR() { return getToken(ejerciciosResueltosParser.MAYOR, 0); }
		public CondicionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_condicion; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ejerciciosResueltosListener ) ((ejerciciosResueltosListener)listener).enterCondicion(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ejerciciosResueltosListener ) ((ejerciciosResueltosListener)listener).exitCondicion(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ejerciciosResueltosVisitor ) return ((ejerciciosResueltosVisitor<? extends T>)visitor).visitCondicion(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CondicionContext condicion() throws RecognitionException {
		CondicionContext _localctx = new CondicionContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_condicion);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(12);
			e();
			setState(13);
			match(MAYOR);
			setState(14);
			e();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class EContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(ejerciciosResueltosParser.ID, 0); }
		public EContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_e; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ejerciciosResueltosListener ) ((ejerciciosResueltosListener)listener).enterE(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ejerciciosResueltosListener ) ((ejerciciosResueltosListener)listener).exitE(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ejerciciosResueltosVisitor ) return ((ejerciciosResueltosVisitor<? extends T>)visitor).visitE(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EContext e() throws RecognitionException {
		EContext _localctx = new EContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_e);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(16);
			match(ID);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\u0004\u0001\u0007\u0013\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001"+
		"\u0002\u0002\u0007\u0002\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000"+
		"\u0001\u0000\u0001\u0000\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0002\u0001\u0002\u0001\u0002\u0000\u0000\u0003\u0000\u0002\u0004"+
		"\u0000\u0000\u000f\u0000\u0006\u0001\u0000\u0000\u0000\u0002\f\u0001\u0000"+
		"\u0000\u0000\u0004\u0010\u0001\u0000\u0000\u0000\u0006\u0007\u0005\u0003"+
		"\u0000\u0000\u0007\b\u0005\u0006\u0000\u0000\b\t\u0003\u0002\u0001\u0000"+
		"\t\n\u0005\u0007\u0000\u0000\n\u000b\u0003\u0000\u0000\u0000\u000b\u0001"+
		"\u0001\u0000\u0000\u0000\f\r\u0003\u0004\u0002\u0000\r\u000e\u0005\u0004"+
		"\u0000\u0000\u000e\u000f\u0003\u0004\u0002\u0000\u000f\u0003\u0001\u0000"+
		"\u0000\u0000\u0010\u0011\u0005\u0005\u0000\u0000\u0011\u0005\u0001\u0000"+
		"\u0000\u0000\u0000";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}