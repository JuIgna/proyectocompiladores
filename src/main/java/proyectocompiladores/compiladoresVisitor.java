// Generated from c:/Users/vazqu/OneDrive/Documentos/Ingenieria Informatica/5 año/SEMESTRE 9/TC/FINAL COMPILADORES/proyectocompiladores/src/main/java/proyectocompiladores/compiladores.g4 by ANTLR 4.13.1

package proyectocompiladores;

import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link compiladoresParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface compiladoresVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link compiladoresParser#s}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitS(compiladoresParser.SContext ctx);
}