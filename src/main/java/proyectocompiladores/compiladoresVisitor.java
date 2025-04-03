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
	 * Visit a parse tree produced by {@link compiladoresParser#programa}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrograma(compiladoresParser.ProgramaContext ctx);
	/**
	 * Visit a parse tree produced by {@link compiladoresParser#bloqueInstrucciones}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBloqueInstrucciones(compiladoresParser.BloqueInstruccionesContext ctx);
	/**
	 * Visit a parse tree produced by {@link compiladoresParser#instruccion}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInstruccion(compiladoresParser.InstruccionContext ctx);
	/**
	 * Visit a parse tree produced by {@link compiladoresParser#bloque}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBloque(compiladoresParser.BloqueContext ctx);
	/**
	 * Visit a parse tree produced by {@link compiladoresParser#error}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitError(compiladoresParser.ErrorContext ctx);
	/**
	 * Visit a parse tree produced by {@link compiladoresParser#defFuncion}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDefFuncion(compiladoresParser.DefFuncionContext ctx);
	/**
	 * Visit a parse tree produced by {@link compiladoresParser#llamadaFuncion}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLlamadaFuncion(compiladoresParser.LlamadaFuncionContext ctx);
	/**
	 * Visit a parse tree produced by {@link compiladoresParser#bloqueFuncion}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBloqueFuncion(compiladoresParser.BloqueFuncionContext ctx);
	/**
	 * Visit a parse tree produced by {@link compiladoresParser#parametros}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParametros(compiladoresParser.ParametrosContext ctx);
	/**
	 * Visit a parse tree produced by {@link compiladoresParser#param}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParam(compiladoresParser.ParamContext ctx);
	/**
	 * Visit a parse tree produced by {@link compiladoresParser#tipo}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTipo(compiladoresParser.TipoContext ctx);
	/**
	 * Visit a parse tree produced by {@link compiladoresParser#definicion}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDefinicion(compiladoresParser.DefinicionContext ctx);
	/**
	 * Visit a parse tree produced by {@link compiladoresParser#asignar}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAsignar(compiladoresParser.AsignarContext ctx);
	/**
	 * Visit a parse tree produced by {@link compiladoresParser#controlFlujo}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitControlFlujo(compiladoresParser.ControlFlujoContext ctx);
	/**
	 * Visit a parse tree produced by {@link compiladoresParser#ifElse}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfElse(compiladoresParser.IfElseContext ctx);
	/**
	 * Visit a parse tree produced by {@link compiladoresParser#whileLoop}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhileLoop(compiladoresParser.WhileLoopContext ctx);
	/**
	 * Visit a parse tree produced by {@link compiladoresParser#forLoop}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForLoop(compiladoresParser.ForLoopContext ctx);
	/**
	 * Visit a parse tree produced by {@link compiladoresParser#inicializacion}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInicializacion(compiladoresParser.InicializacionContext ctx);
	/**
	 * Visit a parse tree produced by {@link compiladoresParser#condicion}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCondicion(compiladoresParser.CondicionContext ctx);
	/**
	 * Visit a parse tree produced by {@link compiladoresParser#actualizacion}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitActualizacion(compiladoresParser.ActualizacionContext ctx);
	/**
	 * Visit a parse tree produced by {@link compiladoresParser#expresion}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpresion(compiladoresParser.ExpresionContext ctx);
	/**
	 * Visit a parse tree produced by {@link compiladoresParser#expresionLogica}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpresionLogica(compiladoresParser.ExpresionLogicaContext ctx);
	/**
	 * Visit a parse tree produced by {@link compiladoresParser#expresionComparacion}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpresionComparacion(compiladoresParser.ExpresionComparacionContext ctx);
	/**
	 * Visit a parse tree produced by {@link compiladoresParser#expresionAritmetica}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpresionAritmetica(compiladoresParser.ExpresionAritmeticaContext ctx);
	/**
	 * Visit a parse tree produced by {@link compiladoresParser#termino}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTermino(compiladoresParser.TerminoContext ctx);
	/**
	 * Visit a parse tree produced by {@link compiladoresParser#factor}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFactor(compiladoresParser.FactorContext ctx);
	/**
	 * Visit a parse tree produced by {@link compiladoresParser#op_logicas}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOp_logicas(compiladoresParser.Op_logicasContext ctx);
	/**
	 * Visit a parse tree produced by {@link compiladoresParser#llamadaPrints}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLlamadaPrints(compiladoresParser.LlamadaPrintsContext ctx);
	/**
	 * Visit a parse tree produced by {@link compiladoresParser#argumentos}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArgumentos(compiladoresParser.ArgumentosContext ctx);
	/**
	 * Visit a parse tree produced by {@link compiladoresParser#booleano}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBooleano(compiladoresParser.BooleanoContext ctx);
	/**
	 * Visit a parse tree produced by {@link compiladoresParser#incrementoDecremento}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIncrementoDecremento(compiladoresParser.IncrementoDecrementoContext ctx);
	/**
	 * Visit a parse tree produced by {@link compiladoresParser#retorno}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRetorno(compiladoresParser.RetornoContext ctx);
}