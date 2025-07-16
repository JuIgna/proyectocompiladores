// Generated from c:/Users/vazqu/OneDrive/Documentos/Ingenieria Informatica/5 año/SEMESTRE 9/TC/FINAL COMPILADORES/proyectocompiladores/src/main/java/proyectocompiladores/compilador/practicaReglas/ejerciciosResueltos.g4 by ANTLR 4.13.1
 package proyectocompiladores.compilador.practicaReglas; 
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link ejerciciosResueltosParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface ejerciciosResueltosVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link ejerciciosResueltosParser#s}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitS(ejerciciosResueltosParser.SContext ctx);
	/**
	 * Visit a parse tree produced by {@link ejerciciosResueltosParser#condicion}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCondicion(ejerciciosResueltosParser.CondicionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ejerciciosResueltosParser#e}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitE(ejerciciosResueltosParser.EContext ctx);
}