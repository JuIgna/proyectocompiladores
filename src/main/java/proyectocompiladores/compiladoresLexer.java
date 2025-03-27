// Generated from c:/Users/vazqu/OneDrive/Documentos/Ingenieria Informatica/5 año/SEMESTRE 9/TC/FINAL COMPILADORES/proyectocompiladores/src/main/java/proyectocompiladores/compiladores.g4 by ANTLR 4.13.1

package proyectocompiladores;

import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue", "this-escape"})
public class compiladoresLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.13.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, NUMERO=4, ID=5, PYC=6, IGUAL=7, SUMA=8, RESTA=9, 
		MULT=10, DIV=11, MOD=12, PA=13, PC=14, LLA=15, LLC=16, COMP=17, AND=18, 
		OR=19, WS=20;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"T__0", "T__1", "T__2", "DIGITO", "CARACTER", "NUMERO", "ID", "PYC", 
			"IGUAL", "SUMA", "RESTA", "MULT", "DIV", "MOD", "PA", "PC", "LLA", "LLC", 
			"COMP", "AND", "OR", "WS"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'int'", "'double'", "'bool'", null, null, "';'", "'='", "'+'", 
			"'-'", "'*'", "'/'", "'%'", "'('", "')'", "'{'", "'}'", null, "'&&'", 
			"'||'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, "NUMERO", "ID", "PYC", "IGUAL", "SUMA", "RESTA", 
			"MULT", "DIV", "MOD", "PA", "PC", "LLA", "LLC", "COMP", "AND", "OR", 
			"WS"
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


	public compiladoresLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "compiladores.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\u0004\u0000\u0014\u0080\u0006\uffff\uffff\u0002\u0000\u0007\u0000\u0002"+
		"\u0001\u0007\u0001\u0002\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002"+
		"\u0004\u0007\u0004\u0002\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002"+
		"\u0007\u0007\u0007\u0002\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002"+
		"\u000b\u0007\u000b\u0002\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e"+
		"\u0002\u000f\u0007\u000f\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011"+
		"\u0002\u0012\u0007\u0012\u0002\u0013\u0007\u0013\u0002\u0014\u0007\u0014"+
		"\u0002\u0015\u0007\u0015\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002"+
		"\u0001\u0003\u0001\u0003\u0001\u0004\u0001\u0004\u0001\u0005\u0004\u0005"+
		"C\b\u0005\u000b\u0005\f\u0005D\u0001\u0006\u0001\u0006\u0003\u0006I\b"+
		"\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0005\u0006N\b\u0006\n\u0006"+
		"\f\u0006Q\t\u0006\u0001\u0007\u0001\u0007\u0001\b\u0001\b\u0001\t\u0001"+
		"\t\u0001\n\u0001\n\u0001\u000b\u0001\u000b\u0001\f\u0001\f\u0001\r\u0001"+
		"\r\u0001\u000e\u0001\u000e\u0001\u000f\u0001\u000f\u0001\u0010\u0001\u0010"+
		"\u0001\u0011\u0001\u0011\u0001\u0012\u0001\u0012\u0001\u0012\u0001\u0012"+
		"\u0001\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0003\u0012"+
		"r\b\u0012\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0014\u0001\u0014"+
		"\u0001\u0014\u0001\u0015\u0004\u0015{\b\u0015\u000b\u0015\f\u0015|\u0001"+
		"\u0015\u0001\u0015\u0000\u0000\u0016\u0001\u0001\u0003\u0002\u0005\u0003"+
		"\u0007\u0000\t\u0000\u000b\u0004\r\u0005\u000f\u0006\u0011\u0007\u0013"+
		"\b\u0015\t\u0017\n\u0019\u000b\u001b\f\u001d\r\u001f\u000e!\u000f#\u0010"+
		"%\u0011\'\u0012)\u0013+\u0014\u0001\u0000\u0004\u0001\u000009\u0002\u0000"+
		"AZaz\u0002\u0000<<>>\u0003\u0000\t\n\r\r  \u0087\u0000\u0001\u0001\u0000"+
		"\u0000\u0000\u0000\u0003\u0001\u0000\u0000\u0000\u0000\u0005\u0001\u0000"+
		"\u0000\u0000\u0000\u000b\u0001\u0000\u0000\u0000\u0000\r\u0001\u0000\u0000"+
		"\u0000\u0000\u000f\u0001\u0000\u0000\u0000\u0000\u0011\u0001\u0000\u0000"+
		"\u0000\u0000\u0013\u0001\u0000\u0000\u0000\u0000\u0015\u0001\u0000\u0000"+
		"\u0000\u0000\u0017\u0001\u0000\u0000\u0000\u0000\u0019\u0001\u0000\u0000"+
		"\u0000\u0000\u001b\u0001\u0000\u0000\u0000\u0000\u001d\u0001\u0000\u0000"+
		"\u0000\u0000\u001f\u0001\u0000\u0000\u0000\u0000!\u0001\u0000\u0000\u0000"+
		"\u0000#\u0001\u0000\u0000\u0000\u0000%\u0001\u0000\u0000\u0000\u0000\'"+
		"\u0001\u0000\u0000\u0000\u0000)\u0001\u0000\u0000\u0000\u0000+\u0001\u0000"+
		"\u0000\u0000\u0001-\u0001\u0000\u0000\u0000\u00031\u0001\u0000\u0000\u0000"+
		"\u00058\u0001\u0000\u0000\u0000\u0007=\u0001\u0000\u0000\u0000\t?\u0001"+
		"\u0000\u0000\u0000\u000bB\u0001\u0000\u0000\u0000\rH\u0001\u0000\u0000"+
		"\u0000\u000fR\u0001\u0000\u0000\u0000\u0011T\u0001\u0000\u0000\u0000\u0013"+
		"V\u0001\u0000\u0000\u0000\u0015X\u0001\u0000\u0000\u0000\u0017Z\u0001"+
		"\u0000\u0000\u0000\u0019\\\u0001\u0000\u0000\u0000\u001b^\u0001\u0000"+
		"\u0000\u0000\u001d`\u0001\u0000\u0000\u0000\u001fb\u0001\u0000\u0000\u0000"+
		"!d\u0001\u0000\u0000\u0000#f\u0001\u0000\u0000\u0000%q\u0001\u0000\u0000"+
		"\u0000\'s\u0001\u0000\u0000\u0000)v\u0001\u0000\u0000\u0000+z\u0001\u0000"+
		"\u0000\u0000-.\u0005i\u0000\u0000./\u0005n\u0000\u0000/0\u0005t\u0000"+
		"\u00000\u0002\u0001\u0000\u0000\u000012\u0005d\u0000\u000023\u0005o\u0000"+
		"\u000034\u0005u\u0000\u000045\u0005b\u0000\u000056\u0005l\u0000\u0000"+
		"67\u0005e\u0000\u00007\u0004\u0001\u0000\u0000\u000089\u0005b\u0000\u0000"+
		"9:\u0005o\u0000\u0000:;\u0005o\u0000\u0000;<\u0005l\u0000\u0000<\u0006"+
		"\u0001\u0000\u0000\u0000=>\u0007\u0000\u0000\u0000>\b\u0001\u0000\u0000"+
		"\u0000?@\u0007\u0001\u0000\u0000@\n\u0001\u0000\u0000\u0000AC\u0003\u0007"+
		"\u0003\u0000BA\u0001\u0000\u0000\u0000CD\u0001\u0000\u0000\u0000DB\u0001"+
		"\u0000\u0000\u0000DE\u0001\u0000\u0000\u0000E\f\u0001\u0000\u0000\u0000"+
		"FI\u0003\t\u0004\u0000GI\u0005_\u0000\u0000HF\u0001\u0000\u0000\u0000"+
		"HG\u0001\u0000\u0000\u0000IO\u0001\u0000\u0000\u0000JN\u0003\t\u0004\u0000"+
		"KN\u0003\u0007\u0003\u0000LN\u0005_\u0000\u0000MJ\u0001\u0000\u0000\u0000"+
		"MK\u0001\u0000\u0000\u0000ML\u0001\u0000\u0000\u0000NQ\u0001\u0000\u0000"+
		"\u0000OM\u0001\u0000\u0000\u0000OP\u0001\u0000\u0000\u0000P\u000e\u0001"+
		"\u0000\u0000\u0000QO\u0001\u0000\u0000\u0000RS\u0005;\u0000\u0000S\u0010"+
		"\u0001\u0000\u0000\u0000TU\u0005=\u0000\u0000U\u0012\u0001\u0000\u0000"+
		"\u0000VW\u0005+\u0000\u0000W\u0014\u0001\u0000\u0000\u0000XY\u0005-\u0000"+
		"\u0000Y\u0016\u0001\u0000\u0000\u0000Z[\u0005*\u0000\u0000[\u0018\u0001"+
		"\u0000\u0000\u0000\\]\u0005/\u0000\u0000]\u001a\u0001\u0000\u0000\u0000"+
		"^_\u0005%\u0000\u0000_\u001c\u0001\u0000\u0000\u0000`a\u0005(\u0000\u0000"+
		"a\u001e\u0001\u0000\u0000\u0000bc\u0005)\u0000\u0000c \u0001\u0000\u0000"+
		"\u0000de\u0005{\u0000\u0000e\"\u0001\u0000\u0000\u0000fg\u0005}\u0000"+
		"\u0000g$\u0001\u0000\u0000\u0000hi\u0005=\u0000\u0000ir\u0005=\u0000\u0000"+
		"jk\u0005!\u0000\u0000kr\u0005=\u0000\u0000lr\u0007\u0002\u0000\u0000m"+
		"n\u0005<\u0000\u0000nr\u0005=\u0000\u0000op\u0005>\u0000\u0000pr\u0005"+
		"=\u0000\u0000qh\u0001\u0000\u0000\u0000qj\u0001\u0000\u0000\u0000ql\u0001"+
		"\u0000\u0000\u0000qm\u0001\u0000\u0000\u0000qo\u0001\u0000\u0000\u0000"+
		"r&\u0001\u0000\u0000\u0000st\u0005&\u0000\u0000tu\u0005&\u0000\u0000u"+
		"(\u0001\u0000\u0000\u0000vw\u0005|\u0000\u0000wx\u0005|\u0000\u0000x*"+
		"\u0001\u0000\u0000\u0000y{\u0007\u0003\u0000\u0000zy\u0001\u0000\u0000"+
		"\u0000{|\u0001\u0000\u0000\u0000|z\u0001\u0000\u0000\u0000|}\u0001\u0000"+
		"\u0000\u0000}~\u0001\u0000\u0000\u0000~\u007f\u0006\u0015\u0000\u0000"+
		"\u007f,\u0001\u0000\u0000\u0000\u0007\u0000DHMOq|\u0001\u0006\u0000\u0000";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}