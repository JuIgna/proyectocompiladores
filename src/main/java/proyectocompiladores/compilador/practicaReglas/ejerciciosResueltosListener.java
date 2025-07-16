// Generated from c:/Users/vazqu/OneDrive/Documentos/Ingenieria Informatica/5 año/SEMESTRE 9/TC/FINAL COMPILADORES/proyectocompiladores/src/main/java/proyectocompiladores/compilador/practicaReglas/ejerciciosResueltos.g4 by ANTLR 4.13.1
 package proyectocompiladores.compilador.practicaReglas; 
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link ejerciciosResueltosParser}.
 */
public interface ejerciciosResueltosListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link ejerciciosResueltosParser#s}.
	 * @param ctx the parse tree
	 */
	void enterS(ejerciciosResueltosParser.SContext ctx);
	/**
	 * Exit a parse tree produced by {@link ejerciciosResueltosParser#s}.
	 * @param ctx the parse tree
	 */
	void exitS(ejerciciosResueltosParser.SContext ctx);
	/**
	 * Enter a parse tree produced by {@link ejerciciosResueltosParser#condicion}.
	 * @param ctx the parse tree
	 */
	void enterCondicion(ejerciciosResueltosParser.CondicionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ejerciciosResueltosParser#condicion}.
	 * @param ctx the parse tree
	 */
	void exitCondicion(ejerciciosResueltosParser.CondicionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ejerciciosResueltosParser#e}.
	 * @param ctx the parse tree
	 */
	void enterE(ejerciciosResueltosParser.EContext ctx);
	/**
	 * Exit a parse tree produced by {@link ejerciciosResueltosParser#e}.
	 * @param ctx the parse tree
	 */
	void exitE(ejerciciosResueltosParser.EContext ctx);
}