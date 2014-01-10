package org.ggll.parser.syntax;

import ggll.core.syntax.model.TableGraphNode;
import ggll.core.syntax.model.TableNode;

import org.ggll.parser.syntax.grammar.model.SyntaxDefinitions;

/*
 * Created on 11/08/2003
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
/**
 * @author gohan
 * 
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class SyntacticLoader
{
	int AltR;
	String Flag;
	int IndPrim;
	int MaxNt;
	int MaxT;
	int NoMax;
	String Nomer;
	int NumNo;
	String SemR;
	int SucR;
	TableGraphNode TabGraph[];

	TableNode TabNt[];
	TableNode TabT[];
	char Tipo;

	public SyntacticLoader(TableCreate argTab)
	{
		this.MaxT = 0;
		this.MaxNt = 0;
		this.IndPrim = 1;
		this.NoMax = 0;
		final TableCreate t = argTab;
		final String tab[][] = t.getTab();
		this.TabT = new TableNode[t.getNLines() + 1];
		this.TabNt = new TableNode[t.getNLines() + 1];
		this.TabGraph = new TableGraphNode[t.getNLines() + 1];
		int registrosLidos;
		int iterator;
		final int indicesTabNtEncontrados[] = new int[t.getNLines() + 1];
		int indiceEncontrado;
		/*
		 * 2 Para todos os registros (linhas )presentes na tabela de entrada,
		 * faço:
		 */
		for (registrosLidos = 0; registrosLidos < t.getNLines(); registrosLidos++)
		{
			this.Flag = tab[registrosLidos][0];
			this.Tipo = tab[registrosLidos][1].toCharArray()[0];
			this.Nomer = tab[registrosLidos][2];
			this.NumNo = Integer.parseInt(tab[registrosLidos][3]);
			this.AltR = Integer.parseInt(tab[registrosLidos][4]);
			this.SucR = Integer.parseInt(tab[registrosLidos][5]);
			this.SemR = tab[registrosLidos][6];

			/*
			 * A tabela indicesTabNtEncontrados será util mais adiante, porém,
			 * preciso inicializá-la, toda vez que eu mudar de registro
			 */
			for (int i = 0; i < indicesTabNtEncontrados.length; i++)
			{
				indicesTabNtEncontrados[i] = -1;
			}
			/* 2 Se Tipo for igual Cabeça: */
			if (this.Tipo == 'H')
			{
				/*
				 * Para facilitar a busca, faço todos os valores de
				 * indicesTabNtEncontrados serem iguais a -1
				 */

				/* 2 Faço IndPrim <- IndPrim + NoMax, */
				this.IndPrim = this.IndPrim + this.NoMax;
				/* 2 NoMax <- 0 */
				this.NoMax = 0;
				/* 2 verifico se Nomer se encontra em TABNT */
				/*
				 * iterador e aux serão uteis apenas para a localização de um
				 * simbolo na tabela de não terminais
				 */
				iterator = 1;
				int aux = 0;
				/* enquanto existir entradas em TABNT */
				while (this.TabNt[iterator] != null)
				{
					/* Se eu encontrar um simbolo igual à Nomer... */
					if (this.TabNt[iterator].getName().equals(this.Nomer))
					{
						/*
						 * Guardo o indice do simbolo encontrado em uma tabela
						 * especial
						 */
						indicesTabNtEncontrados[aux] = iterator;
						aux = aux + 1;
					}
					iterator = iterator + 1;
				}
				/* 2 se Nomer não se encontra na TABNT */
				if (indicesTabNtEncontrados[0] == -1)
				{
					this.MaxNt = this.MaxNt + 1;
					/* 2 Coloco na TABNT um o não-terminal TabNT */
					this.TabNt[this.MaxNt] = new TableNode(this.Flag, this.Nomer, this.IndPrim);
				}
				else
				{
					/*
					 * 2 Se existe E tal que 1<=E<=MaxNt e TabNt[E].name() =
					 * Nomer
					 */
					for (int j = 0; j < this.MaxNt; j++)
					{
						if (indicesTabNtEncontrados[j] != -1)
						{
							/* 2 TabNt[E]->prim = 0 ? */
							if (this.TabNt[indicesTabNtEncontrados[j]].getFirstNode() == 0)
							{
								/* 2 Se sim */
								this.TabNt[indicesTabNtEncontrados[j]].setFirstNode(this.IndPrim);
							}
							else
							{
								/* 2 Se não */
								System.out.println("Erro!!Duas cabeças para um mesmo não-terminal");
							}
						}
					}
				} // } else {...
			} // if(Tipo == 'H')...
			/* 2 Se Tipo for diferente de H */
			else
			{
				/* 2 I <- IndPrim + NumNo -1 */
				final int I = this.IndPrim + this.NumNo - 1;
				/* O nó que será inserido em TabGraph é criado agora */
				this.TabGraph[I] = new TableGraphNode();
				/*
				 * IndiceEncontrado será utilizado na localização de uma simbolo
				 * na Tabela de simbolos Terminais
				 */
				indiceEncontrado = -1;
				/* 2 se Tipo for igual a T e Nomer não for um lambda-nó */
				if (this.Tipo == 'T')
				{
					this.TabGraph[I].setIsTerminal(true);

					if (!this.Nomer.equals(new String("-1")) && !this.Nomer.equals(SyntaxDefinitions.EmptyNodeLabel))
					{
						iterator = 1;
						/* 2 verifico se Nomer se encontra em TABT */
						while (this.TabT[iterator] != null)
						{
							if (this.TabT[iterator].getName().equals(this.Nomer))
							{
								indiceEncontrado = iterator;
								break;
							}
							iterator = iterator + 1;
						}
						if (indiceEncontrado == -1)
						{
							/*
							 * 2 Se não foi encontrado, a entrada na tabela de
							 * simbolos terminais terá de ser criada
							 */
							this.MaxT = this.MaxT + 1;
							this.TabT[this.MaxT] = new TableNode(this.Flag, this.Nomer);
							indiceEncontrado = this.MaxT;
						}
					}
				}
				/* 2 Se Tipo for igoal a N */
				else if (this.Tipo == 'N')
				{
					iterator = 1;
					/* 2 verifico se Nomer se encontra em TABT */
					while (this.TabNt[iterator] != null)
					{
						if (this.TabNt[iterator].getName().equals(this.Nomer))
						{
							indiceEncontrado = iterator;
							break;
						}
						iterator = iterator + 1;
					}
					if (indiceEncontrado == -1)
					{
						/*
						 * 2 Se não for encontrado, uma entrada na tabela TabNt,
						 * será criada
						 */
						this.MaxNt = this.MaxNt + 1;
						this.TabNt[this.MaxNt] = new TableNode(this.Flag, this.Nomer, 0);
						indiceEncontrado = this.MaxNt;
					}
					this.TabGraph[I].setIsTerminal(false);
				}
				if (this.Nomer.equals("-1") || this.Nomer.equals(SyntaxDefinitions.EmptyNodeLabel))
				{
					this.TabGraph[I].setNodeReference(0);
				}
				else
				{
					this.TabGraph[I].setNodeReference(indiceEncontrado);
				}
				if (this.AltR != 0)
				{
					this.TabGraph[I].setAlternativeIndex(this.IndPrim + this.AltR - 1);
				}
				else
				{
					this.TabGraph[I].setAlternativeIndex(0);
				}
				if (this.SucR != 0)
				{
					this.TabGraph[I].setSucessorIndex(this.IndPrim + this.SucR - 1);
				}
				else
				{
					this.TabGraph[I].setSucessorIndex(0);
				}
				/* Colocando o valor da rotina semantica */
				this.TabGraph[I].setSemanticRoutine(this.SemR);

				if (this.NoMax < this.NumNo)
				{
					this.NoMax = this.NumNo;
				}
			}
		} // while (registrosLidos < t.linhas()) ...
	} // public CarregadorSintatico() ...

	public TableGraphNode[] tabGraph()
	{
		return this.TabGraph;
	}

	public TableNode[] tabNt()
	{
		return this.TabNt;
	}

	public TableNode[] tabT()
	{
		return this.TabT;
	}
}
